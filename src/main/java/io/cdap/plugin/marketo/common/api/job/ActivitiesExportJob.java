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
import io.cdap.plugin.marketo.common.api.Urls;
import io.cdap.plugin.marketo.common.api.entities.activities.ActivitiesExport;
import io.cdap.plugin.marketo.common.api.entities.activities.ActivitiesExportResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * Activities export job.
 */
public class ActivitiesExportJob extends AbstractBulkExportJob<ActivitiesExport> {
  private static final Logger LOG = LoggerFactory.getLogger(ActivitiesExportJob.class);

  public ActivitiesExportJob(ActivitiesExport lastState, Marketo marketo) {
    super(lastState.getExportId(), lastState, marketo);
  }

  @Override
  public Logger getLogger() {
    return LOG;
  }

  @Override
  public ActivitiesExport getFreshState() {
    return getMarketo().activitiesExportJobStatus(getJobId());
  }

  @Override
  public String getStateStatus(ActivitiesExport state) {
    return state.getStatus();
  }

  @Override
  public String getFileUrlTemplate() {
    return Urls.BULK_EXPORT_ACTIVITIES_FILE;
  }

  @Override
  public String getLogPrefix() {
    return "BULK ACTIVITIES EXPORT";
  }

  @Override
  protected ActivitiesExport enqueueImpl() {
    return getMarketo().validatedPost(
      String.format(Urls.BULK_EXPORT_ACTIVITIES_ENQUEUE, getJobId()),
      Collections.emptyMap(),
      inputStream -> Helpers.streamToObject(inputStream, ActivitiesExportResponse.class),
      null, null).singleExport();
  }
}
