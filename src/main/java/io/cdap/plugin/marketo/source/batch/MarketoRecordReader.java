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
import io.cdap.plugin.marketo.common.Marketo;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;
import java.util.Map;

/**
 * RecordReader implementation, which reads events from Marketo api.
 */
public class MarketoRecordReader extends RecordReader<NullWritable, Map<String, Object>> {
  private static final Gson GSON = new GsonBuilder().create();
  private Marketo.MarketoPageIterator iterator;
  private Map<String, Object> current = null;

  @Override
  public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException {
    Configuration conf = taskAttemptContext.getConfiguration();
    String configJson = conf.get(MarketoInputFormatProvider.PROPERTY_CONFIG_JSON);
    MarketoBatchSourceConfig config = GSON.fromJson(configJson, MarketoBatchSourceConfig.class);

    iterator = config.getMarketo().iteratePage(config.getEntityType().getGetEndpoint());
  }

  @Override
  public boolean nextKeyValue() {
    if (iterator.hasNext()) {
      current = iterator.next();
      return true;
    }
    return false;
  }

  @Override
  public NullWritable getCurrentKey() {
    return null;
  }

  @Override
  public Map<String, Object> getCurrentValue() {
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
