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

package io.cdap.plugin.marketo.common.api.job;

import io.cdap.plugin.marketo.common.api.Helpers;
import io.cdap.plugin.marketo.common.api.Marketo;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Base bulk export job wrapper.
 *
 * @param <T> object that represents job status
 */
public abstract class AbstractBulkExportJob<T> {
  private static final List<String> WAIT_ABLE_STATE = Arrays.asList("Queued", "Processing");
  private static final String ENQUEUE_ABLE_STATUS = "Created";
  private static final String COMPLETED_STATUS = "Completed";

  private String jobId;
  private T lastState;
  private Marketo marketo;

  /**
   * @param jobId     job id
   * @param lastState last status
   * @param marketo   marketo api instance
   */
  public AbstractBulkExportJob(String jobId,
                               T lastState,
                               Marketo marketo) {

    this.jobId = jobId;
    this.lastState = lastState;
    this.marketo = marketo;
    getLogger().info("{} - created job '{}'", getLogPrefix(), this.jobId);
  }

  public abstract Logger getLogger();

  public abstract T getFreshState();

  public abstract String getStateStatus(T state);

  public abstract String getFileUrlTemplate();

  public abstract String getLogPrefix();

  protected abstract T enqueueImpl();

  public T getLastState() {
    return lastState;
  }

  public Marketo getMarketo() {
    return marketo;
  }

  public void waitCompletion() {
    if (!WAIT_ABLE_STATE.contains(getStateStatus(getLastState()))) {
      throw new IllegalStateException("Job must be enqueued before waiting for completion.");
    }

    do {
      try {
        Thread.sleep(TimeUnit.SECONDS.toMillis(30));
      } catch (InterruptedException e) {
        throw new IllegalStateException("Failed to wait for job completion - interrupted");
      }

      T newState = getFreshState();
      logStatusChange(getStateStatus(getLastState()), getStateStatus(newState));
      lastState = newState;
    } while (WAIT_ABLE_STATE.contains(getStateStatus(getLastState())));

    if (!getStateStatus(getLastState()).equals(COMPLETED_STATUS)) {
      throw new IllegalStateException("Job expected to be in Completed state, but was in " +
                                        getStateStatus(getLastState()));
    }
  }

  public boolean enqueue() {
    if (!getStateStatus(getLastState()).equals(ENQUEUE_ABLE_STATUS)) {
      throw new IllegalStateException("Job must be in Created status before enqueuing, but was in " +
                                        getStateStatus(getLastState()));
    }

    if (!marketo.canEnqueueJob()) {
      return false;
    }

    T newState = enqueueImpl();

    logStatusChange(getStateStatus(getLastState()), getStateStatus(newState));

    if (!(getStateStatus(newState).equals("Queued") || getStateStatus(newState).equals("Processing"))) {
      throw new IllegalStateException(
        String.format("Expected Queued|Processing state for job '%s' but got '%s'", jobId, getStateStatus(newState)));
    }

    lastState = newState;

    return true;
  }

  private void logStatusChange(String oldStatus, String newStatus) {
    if (!oldStatus.equals(newStatus)) {
      getLogger().info("{} - job '{}' changed state '{}' -> '{}'", getLogPrefix(), jobId, oldStatus, newStatus);
    }
  }

  public String getFile() {
    return marketo.get(marketo.buildUri(String.format(getFileUrlTemplate(), jobId), Collections.emptyMap()),
                       Helpers::streamToString);
  }

  public String getJobId() {
    return jobId;
  }
}
