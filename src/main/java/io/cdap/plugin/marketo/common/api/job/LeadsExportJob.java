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
import io.cdap.plugin.marketo.common.api.entities.leads.LeadsExport;
import io.cdap.plugin.marketo.common.api.entities.leads.LeadsExportResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * Leads export job.
 */
public class LeadsExportJob extends AbstractBulkExportJob<LeadsExport> {
  private static final Logger LOG = LoggerFactory.getLogger(LeadsExportJob.class);

  public LeadsExportJob(LeadsExport lastState, Marketo marketo) {
    super(lastState.getExportId(), lastState, marketo);
  }

  @Override
  public Logger getLogger() {
    return LOG;
  }

  @Override
  public LeadsExport getFreshState() {
    return getMarketo().leadsExportJobStatus(getJobId());
  }

  @Override
  public String getStateStatus(LeadsExport state) {
    return state.getStatus();
  }

  @Override
  public String getFileUrlTemplate() {
    return Urls.BULK_EXPORT_LEADS_FILE;
  }

  @Override
  public String getLogPrefix() {
    return "BULK LEADS EXPORT";
  }

  @Override
  protected LeadsExport enqueueImpl() {
    return getMarketo().validatedPost(
      String.format(Urls.BULK_EXPORT_LEADS_ENQUEUE, getJobId()),
      Collections.emptyMap(),
      inputStream -> Helpers.streamToObject(inputStream, LeadsExportResponse.class),
      null, null).singleExport();
  }
}
