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
