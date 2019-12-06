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

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputSplit;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * A no-op split.
 */
public class MarketoEntitySplit extends InputSplit implements Writable {
  private int splitId;
  private int resultsPerPage;
  private int totalSplits;

  public MarketoEntitySplit() {
  }

  public MarketoEntitySplit(int splitId, int resultsPerPage, int totalSplits) {
    this.splitId = splitId;
    this.resultsPerPage = resultsPerPage;
    this.totalSplits = totalSplits;
  }

  @Override
  public void readFields(DataInput dataInput) throws IOException {
    splitId = dataInput.readInt();
    resultsPerPage = dataInput.readInt();
    totalSplits = dataInput.readInt();
  }

  @Override
  public void write(DataOutput dataOutput) throws IOException {
    dataOutput.writeInt(splitId);
    dataOutput.writeInt(resultsPerPage);
    dataOutput.writeInt(totalSplits);
  }

  @Override
  public long getLength() {
    return 0;
  }

  @Override
  public String[] getLocations() {
    return new String[0];
  }

  public int getSplitId() {
    return splitId;
  }

  public int getResultsPerPage() {
    return resultsPerPage;
  }

  public int getTotalSplits() {
    return totalSplits;
  }
}
