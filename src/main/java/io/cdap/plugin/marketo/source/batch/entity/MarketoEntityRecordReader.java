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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.cdap.cdap.api.data.format.StructuredRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

/**
 * RecordReader implementation, which reads events from Marketo api.
 */
public class MarketoEntityRecordReader extends RecordReader<NullWritable, StructuredRecord> {
  private static final Logger LOG = LoggerFactory.getLogger(MarketoEntityRecordReader.class);
  private static final Gson GSON = new GsonBuilder().create();
  private Iterator iterator = null;
  private Object current = null;
  private MarketoEntitySourceConfig config;
  private int splitId;
  private int resultsPerPage;
  private int totalSplits;
  private int currentOffset = -1;
  private boolean needSwapPage = false;
  private boolean supportPaging = true;

  public MarketoEntityRecordReader(int splitId, int resultsPerPage, int totalSplits) {
    this.splitId = splitId;
    this.resultsPerPage = resultsPerPage;
    this.totalSplits = totalSplits;
    this.currentOffset = splitId * resultsPerPage;
  }

  @Override
  public void initialize(InputSplit inputSplit, TaskAttemptContext taskAttemptContext) throws IOException {
    deserializeConfiguration(taskAttemptContext.getConfiguration());
    iterator = EntityHelper.iteratorForEntityType(config.getMarketo(), config.getEntityType(),
                                                  currentOffset, resultsPerPage);
    LOG.info("Split '{}' fetched data by offset '{}'", splitId, currentOffset);
  }

  @Override
  public boolean nextKeyValue() {
    if (iterator.hasNext()) {
      current = iterator.next();
      // this page is not empty, try next page as well
      needSwapPage = true;
      return true;
    } else if (swapPage()) {
      // page swapped, try to get new item
      return nextKeyValue();
    } else {
      return false;
    }
  }

  private boolean swapPage() {
    if (needSwapPage && supportPaging) {
      needSwapPage = false;
      currentOffset += totalSplits * resultsPerPage;
      iterator = EntityHelper.iteratorForEntityType(config.getMarketo(), config.getEntityType(), currentOffset,
                                                    resultsPerPage);
      LOG.info("Split '{}' fetched data by offset '{}'", splitId, currentOffset);
      return true;
    }
    return false;
  }

  @Override
  public NullWritable getCurrentKey() {
    return null;
  }

  @Override
  public StructuredRecord getCurrentValue() {
    return EntityHelper.structuredRecordFromEntity(
      config.getEntityType().getValue(),
      current,
      config.getSchema()
    );
  }

  @Override
  public float getProgress() {
    return 0;
  }

  @Override
  public void close() {
  }

  private void deserializeConfiguration(Configuration conf) {
    String configJson = conf.get(MarketoEntityFormatProvider.PROPERTY_CONFIG_JSON);
    config = GSON.fromJson(configJson, MarketoEntitySourceConfig.class);
    supportPaging = EntityHelper.supportPaging(config.getEntityType());
  }
}
