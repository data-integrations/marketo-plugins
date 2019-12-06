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

package io.cdap.plugin.marketo.source.batch.entity;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * InputFormat for mapreduce job.
 */
public class MarketoEntityInputFormat extends InputFormat {
  private static final Gson GSON = new Gson();
  private static final Logger LOG = LoggerFactory.getLogger(MarketoEntityInputFormat.class);

  @Override
  public List<InputSplit> getSplits(JobContext jobContext) {
    Configuration conf = jobContext.getConfiguration();
    MarketoEntitySourceConfig config = GSON.fromJson(
      conf.get(MarketoEntityFormatProvider.PROPERTY_CONFIG_JSON), MarketoEntitySourceConfig.class);

    if (EntityHelper.supportPaging(config.getEntityType())) {
      return IntStream.range(0, config.getSplitsCount())
        .mapToObj(splitId -> new MarketoEntitySplit(splitId, config.getResultsPerPage(), config.getSplitsCount()))
        .collect(Collectors.toList());
    } else {
      LOG.info("Entity '{}' does not support paging, single split used", config.getEntityType().getValue());
      return ImmutableList.of(new MarketoEntitySplit(0, 200, 1));
    }
  }

  @Override
  public RecordReader createRecordReader(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) {
    MarketoEntitySplit split = (MarketoEntitySplit) inputSplit;
    return new MarketoEntityRecordReader(split.getSplitId(), split.getResultsPerPage(), split.getTotalSplits());
  }
}
