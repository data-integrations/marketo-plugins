/*
 * Copyright © 2019 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.cdap.plugin.marketo.source.batch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.plugin.marketo.common.api.Marketo;
import io.cdap.plugin.marketo.common.api.entities.DateRange;
import io.cdap.plugin.marketo.common.api.entities.activities.ActivitiesExportRequest;
import io.cdap.plugin.marketo.common.api.entities.activities.ExportActivityFilter;
import io.cdap.plugin.marketo.common.api.entities.leads.LeadsExportRequest;
import io.cdap.plugin.marketo.common.api.job.AbstractBulkExportJob;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RecordReader implementation, which reads events from Marketo api.
 */
public class MarketoRecordReader extends RecordReader<NullWritable, Map<String, String>> {
  private static final Logger LOG = LoggerFactory.getLogger(MarketoRecordReader.class);
  private static final Gson GSON = new GsonBuilder().create();
  private Map<String, String> current = null;
  private Iterator<CSVRecord> iterator = null;
  private String beginDate;
  private String endDate;

  public MarketoRecordReader(String beginDate, String endDate) {
    this.beginDate = beginDate;
    this.endDate = endDate;
  }

  @Override
  public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException {
    Configuration conf = taskAttemptContext.getConfiguration();
    String configJson = conf.get(MarketoInputFormatProvider.PROPERTY_CONFIG_JSON);
    MarketoReportingSourceConfig config = GSON.fromJson(configJson, MarketoReportingSourceConfig.class);

    Marketo marketo = config.getMarketo();

    DateRange dateRange = new DateRange(beginDate, endDate);
    AbstractBulkExportJob job;
    switch (config.getReportType()) {
      case LEADS:
        List<String> leadsFields = config.getSchema().getFields().stream()
          .map(Schema.Field::getName).collect(Collectors.toList());
        LeadsExportRequest.ExportLeadFilter leadsFilter = LeadsExportRequest.ExportLeadFilter.builder()
          .createdAt(dateRange).build();
        job = marketo.exportLeads(new LeadsExportRequest(leadsFields, leadsFilter));
        break;
      case ACTIVITIES:
        ExportActivityFilter activitiesFilter = ExportActivityFilter.builder()
          .createdAt(dateRange).build();
        job = marketo.exportActivities(new ActivitiesExportRequest(Marketo.ACTIVITY_FIELDS, activitiesFilter));
        break;
      default:
        throw new IllegalArgumentException("Invalid report type " + config.getReportType());
    }

    LOG.info("BULK EXPORT JOB - job '{}' has date range '{}'", job.getJobId(), dateRange);

    // TODO handle possible concurrent issues here, another mapper can take our slot
    // wait for 10 minutes for available slot and enqueue job
    marketo.onBulkExtractQueueAvailable(job::enqueue, 60 * 10);

    job.waitCompletion();

    String data = job.getFile();
    // TODO stream here
    CSVParser parser = CSVFormat.DEFAULT.withHeader().parse(new StringReader(data));
    iterator = parser.iterator();
  }

  @Override
  public boolean nextKeyValue() {
    if (iterator.hasNext()) {
      current = iterator.next().toMap();
      LOG.debug("Got record '{}'", current.toString());
      return true;
    }
    return false;
  }

  @Override
  public NullWritable getCurrentKey() {
    return null;
  }

  @Override
  public Map<String, String> getCurrentValue() {
    return current;
  }

  @Override
  public float getProgress() {
    return 0;
  }

  @Override
  public void close() {
  }
}
