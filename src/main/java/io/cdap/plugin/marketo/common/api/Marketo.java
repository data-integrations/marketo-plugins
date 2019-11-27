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

package io.cdap.plugin.marketo.common.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import io.cdap.plugin.marketo.common.api.entities.activities.ActivitiesExport;
import io.cdap.plugin.marketo.common.api.entities.activities.ActivitiesExportRequest;
import io.cdap.plugin.marketo.common.api.entities.activities.ActivityTypeResponse;
import io.cdap.plugin.marketo.common.api.entities.leads.LeadsDescribe;
import io.cdap.plugin.marketo.common.api.entities.leads.LeadsExport;
import io.cdap.plugin.marketo.common.api.entities.leads.LeadsExportRequest;
import io.cdap.plugin.marketo.common.api.job.ActivitiesExportJob;
import io.cdap.plugin.marketo.common.api.job.LeadsExportJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Class that expose marketo rest api endpoints.
 */
public class Marketo extends MarketoHttp {
  private static final Logger LOG = LoggerFactory.getLogger(Marketo.class);
  static final Gson GSON = new Gson();
  public static final List<String> ACTIVITY_FIELDS = ImmutableList.of("marketoGUID", "leadId", "activityDate",
                                                                      "activityTypeId", "campaignId",
                                                                      "primaryAttributeValueId",
                                                                      "primaryAttributeValue", "attributes");

  public Marketo(String marketoEndpoint, String clientId, String clientSecret) {
    super(marketoEndpoint, clientId, clientSecret);
  }

  public List<LeadsDescribe.LeadAttribute> describeLeads() {
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
      iteratePage(Urls.LEADS_DESCRIBE, LeadsDescribe.class, LeadsDescribe::getResult),
      Spliterator.ORDERED), false).collect(Collectors.toList());
  }

  public List<ActivityTypeResponse.ActivityType> describeBuildInActivities() {
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
      iteratePage(Urls.BUILD_IN_ACTIVITIES_TYPES, ActivityTypeResponse.class, ActivityTypeResponse::getResult),
      Spliterator.ORDERED), false).collect(Collectors.toList());
  }

  public LeadsExportJob exportLeads(LeadsExportRequest request) {
    LeadsExport export = validatedPost(Urls.BULK_EXPORT_LEADS_CREATE, Collections.emptyMap(),
                                       Marketo::streamToLeadsExport,
                                       request,
                                       GSON::toJson);
    return new LeadsExportJob(export.singleExport(), this);
  }

  public LeadsExport.ExportResponse leadsExportJobStatus(String jobId) {
    LeadsExport currentResp = validatedGet(
      String.format(Urls.BULK_EXPORT_LEADS_STATUS, jobId),
      Collections.emptyMap(), Marketo::streamToLeadsExport);
    return currentResp.singleExport();
  }

  public ActivitiesExportJob exportActivities(ActivitiesExportRequest request) {
    ActivitiesExport export = validatedPost(Urls.BULK_EXPORT_ACTIVITIES_CREATE, Collections.emptyMap(),
                                            Marketo::streamToActivitiesExport,
                                            request,
                                            GSON::toJson);
    return new ActivitiesExportJob(export.singleExport(), this);
  }

  public ActivitiesExport.ExportResponse activitiesExportJobStatus(String jobId) {
    ActivitiesExport currentResp = validatedGet(
      String.format(Urls.BULK_EXPORT_ACTIVITIES_STATUS, jobId),
      Collections.emptyMap(), Marketo::streamToActivitiesExport);
    return currentResp.singleExport();
  }

  /**
   * Waits until bulk extract queue has available slot and executes given action.
   *
   * @param action         action to execute once slot is available
   * @param timeoutSeconds timeout in seconds
   */
  public void onBulkExtractQueueAvailable(Runnable action, long timeoutSeconds) {
    long timeoutMillis = TimeUnit.SECONDS.toMillis(timeoutSeconds);
    long startTime = System.currentTimeMillis();
    while (System.currentTimeMillis() - startTime < timeoutMillis) {
      if (canEnqueueJob()) {
        action.run();
        return;
      } else {
        try {
          Thread.sleep(TimeUnit.SECONDS.toMillis(60));
        } catch (InterruptedException e) {
          throw new RuntimeException("Failed to get slot in bulk export queue - interrupted");
        }
      }
    }
    throw new RuntimeException("Failed to get slot in bulk export queue - timeout");
  }

  public static LeadsExport streamToLeadsExport(InputStream inputStream) {
    return Helpers.streamToObject(inputStream, LeadsExport.class);
  }

  public static ActivitiesExport streamToActivitiesExport(InputStream inputStream) {
    return Helpers.streamToObject(inputStream, ActivitiesExport.class);
  }

  private boolean canEnqueueJob() {
    LeadsExport leadsExportJobs = validatedGet(Urls.BULK_EXPORT_LEADS_LIST,
                                               ImmutableMap.of("status", "queued,processing"),
                                               Marketo::streamToLeadsExport
    );

    int jobsInQueue = leadsExportJobs.getResult().size();

    ActivitiesExport activitiesExportJobs = validatedGet(Urls.BULK_EXPORT_ACTIVITIES_LIST,
                                                         ImmutableMap.of("status", "queued,processing"),
                                                         Marketo::streamToActivitiesExport
    );
    jobsInQueue += activitiesExportJobs.getResult().size();

    LOG.debug("Jobs in queue: {}", jobsInQueue);

    return jobsInQueue < 10;
  }
}
