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
import io.cdap.plugin.marketo.common.api.Helpers;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * InputFormat for mapreduce job, which provides a single split of data.
 */
public class MarketoInputFormat extends InputFormat {
  private static final Gson GSON = new Gson();

  @Override
  public List<InputSplit> getSplits(JobContext jobContext) {
    Configuration conf = jobContext.getConfiguration();
    MarketoReportingSourceConfig config = GSON.fromJson(
      conf.get(MarketoInputFormatProvider.PROPERTY_CONFIG_JSON), MarketoReportingSourceConfig.class);

    return Helpers.getDateRanges(config.getStartDate(), config.getEndDate()).stream()
      .map(dateRange -> new MarketoReportingSplit(dateRange.getStartAt(), dateRange.getEndAt()))
      .collect(Collectors.toList());
  }

  @Override
  public RecordReader createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) {
    MarketoReportingSplit split = (MarketoReportingSplit) inputSplit;
    return new MarketoRecordReader(split.getBeginDate(), split.getEndDate());
  }
}
