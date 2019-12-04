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
import io.cdap.plugin.marketo.common.api.entities.activities.ActivitiesExportResponse;
import io.cdap.plugin.marketo.common.api.entities.activities.ActivityTypeResponse;
import io.cdap.plugin.marketo.common.api.entities.leads.LeadsDescribeResponse;
import io.cdap.plugin.marketo.common.api.entities.leads.LeadsExport;
import io.cdap.plugin.marketo.common.api.entities.leads.LeadsExportRequest;
import io.cdap.plugin.marketo.common.api.entities.leads.LeadsExportResponse;
import io.cdap.plugin.marketo.common.api.job.ActivitiesExportJob;
import io.cdap.plugin.marketo.common.api.job.LeadsExportJob;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.Callable;
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
  /**
   * Job queue will be checked every 60 seconds.
   */
  private static final long JOB_QUEUE_POLL_INTERVAL = 60;
  /**
   * Wait for 10 seconds before trying to enqueue job, this will minimize chance of race condition.
   */
  private static final long JOB_QUEUE_POLL_DELAY = 10;

  public Marketo(String marketoEndpoint, String clientId, String clientSecret) {
    super(marketoEndpoint, clientId, clientSecret);
  }

  public List<LeadsDescribeResponse.LeadAttribute> describeLeads() {
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
      iteratePage(Urls.LEADS_DESCRIBE, LeadsDescribeResponse.class, LeadsDescribeResponse::getResult),
      Spliterator.ORDERED), false).collect(Collectors.toList());
  }

  public List<ActivityTypeResponse.ActivityType> describeBuildInActivities() {
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
      iteratePage(Urls.BUILD_IN_ACTIVITIES_TYPES, ActivityTypeResponse.class, ActivityTypeResponse::getResult),
      Spliterator.ORDERED), false).collect(Collectors.toList());
  }

  public LeadsExportJob exportLeads(LeadsExportRequest request) {
    LeadsExportResponse export = validatedPost(Urls.BULK_EXPORT_LEADS_CREATE, Collections.emptyMap(),
                                               Marketo::streamToLeadsExport,
                                               request,
                                               GSON::toJson);
    return new LeadsExportJob(export.singleExport(), this);
  }

  public LeadsExport leadsExportJobStatus(String jobId) {
    LeadsExportResponse currentResp = validatedGet(
      String.format(Urls.BULK_EXPORT_LEADS_STATUS, jobId),
      Collections.emptyMap(), Marketo::streamToLeadsExport);
    return currentResp.singleExport();
  }

  public ActivitiesExportJob exportActivities(ActivitiesExportRequest request) {
    ActivitiesExportResponse export = validatedPost(Urls.BULK_EXPORT_ACTIVITIES_CREATE, Collections.emptyMap(),
                                                    Marketo::streamToActivitiesExport,
                                                    request,
                                                    GSON::toJson);
    return new ActivitiesExportJob(export.singleExport(), this);
  }

  public ActivitiesExport activitiesExportJobStatus(String jobId) {
    ActivitiesExportResponse currentResp = validatedGet(
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
  public void onBulkExtractQueueAvailable(Callable<Boolean> action, long timeoutSeconds) {
    try {
      Awaitility.given()
        .ignoreException(TooManyJobsException.class) // ignore exception in case another reader took our slot
        .atMost(timeoutSeconds, TimeUnit.SECONDS)
        .pollInterval(JOB_QUEUE_POLL_INTERVAL, TimeUnit.SECONDS)
        .pollDelay(JOB_QUEUE_POLL_DELAY, TimeUnit.SECONDS)
        .until(action);
    } catch (ConditionTimeoutException ex) {
      throw new RuntimeException("Failed to get slot in bulk export queue due to timeout");
    }
  }

  public static LeadsExportResponse streamToLeadsExport(InputStream inputStream) {
    return Helpers.streamToObject(inputStream, LeadsExportResponse.class);
  }

  public static ActivitiesExportResponse streamToActivitiesExport(InputStream inputStream) {
    return Helpers.streamToObject(inputStream, ActivitiesExportResponse.class);
  }

  /**
   * Check if job can be enqueued.
   *
   * @return true, if job can be enqueued
   */
  public boolean canEnqueueJob() {
    LeadsExportResponse leadsExportResponseJobs = validatedGet(Urls.BULK_EXPORT_LEADS_LIST,
                                                               ImmutableMap.of("status", "queued,processing"),
                                                               Marketo::streamToLeadsExport
    );

    int jobsInQueue = leadsExportResponseJobs.getResult().size();

    ActivitiesExportResponse activitiesExportResponceJobs = validatedGet(Urls.BULK_EXPORT_ACTIVITIES_LIST,
                                                                         ImmutableMap.of("status", "queued,processing"),
                                                                         Marketo::streamToActivitiesExport
    );
    jobsInQueue += activitiesExportResponceJobs.getResult().size();

    LOG.debug("Jobs in queue: {}", jobsInQueue);

    return jobsInQueue < 10;
  }
}
