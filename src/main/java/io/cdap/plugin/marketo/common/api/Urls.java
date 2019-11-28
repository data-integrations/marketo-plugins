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

/**
 * Marketo API urls.
 */
public class Urls {
  public static final String LEADS_DESCRIBE = "/rest/v1/leads/describe.json";
  public static final String BULK_EXPORT_LEADS_LIST = "/bulk/v1/leads/export.json";
  public static final String BULK_EXPORT_LEADS_CREATE = "/bulk/v1/leads/export/create.json";
  public static final String BULK_EXPORT_LEADS_ENQUEUE = "/bulk/v1/leads/export/%s/enqueue.json";
  public static final String BULK_EXPORT_LEADS_STATUS = "/bulk/v1/leads/export/%s/status.json";
  public static final String BULK_EXPORT_LEADS_FILE = "/bulk/v1/leads/export/%s/file.json";
  public static final String BULK_EXPORT_ACTIVITIES_LIST = "/bulk/v1/activities/export.json";
  public static final String BULK_EXPORT_ACTIVITIES_CREATE = "/bulk/v1/activities/export/create.json";
  public static final String BULK_EXPORT_ACTIVITIES_ENQUEUE = "/bulk/v1/activities/export/%s/enqueue.json";
  public static final String BULK_EXPORT_ACTIVITIES_STATUS = "/bulk/v1/activities/export/%s/status.json";
  public static final String BULK_EXPORT_ACTIVITIES_FILE = "/bulk/v1/activities/export/%s/file.json";
  public static final String BUILD_IN_ACTIVITIES_TYPES = "/rest/v1/activities/types.json";
}
