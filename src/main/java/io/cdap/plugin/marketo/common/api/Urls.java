package io.cdap.plugin.marketo.common.api;

/**
 * Marketo API urls.
 */
public class Urls {
  public static final String BULK_EXPORT_LEADS_CREATE = "/bulk/v1/leads/export/create.json";
  public static final String BULK_EXPORT_LEADS_ENQUEUE = "/bulk/v1/leads/export/%s/enqueue.json";
  public static final String BULK_EXPORT_LEADS_STATUS = "/bulk/v1/leads/export/%s/status.json";
  public static final String BULK_EXPORT_LEADS_FILE = "/bulk/v1/leads/export/%s/file.json";
  public static final String LEADS_DESCRIBE = "/rest/v1/leads/describe.json";
}
