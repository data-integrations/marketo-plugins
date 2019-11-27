package io.cdap.plugin.marketo.common.api.job;

import io.cdap.plugin.marketo.common.api.Helpers;
import io.cdap.plugin.marketo.common.api.Marketo;
import io.cdap.plugin.marketo.common.api.Urls;
import io.cdap.plugin.marketo.common.api.entities.leads.LeadsExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * Leads export job.
 */
public class LeadsExportJob extends AbstractBulkExportJob<LeadsExport.ExportResponse> {
  private static final Logger LOG = LoggerFactory.getLogger(LeadsExportJob.class);

  public LeadsExportJob(LeadsExport.ExportResponse lastState, Marketo marketo) {
    super(lastState.getExportId(), lastState, marketo);
  }

  @Override
  public Logger getLogger() {
    return LOG;
  }

  @Override
  public LeadsExport.ExportResponse getFreshState() {
    return getMarketo().leadsExportJobStatus(getJobId());
  }

  @Override
  public String getStateStatus(LeadsExport.ExportResponse state) {
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
  protected LeadsExport.ExportResponse enqueueImpl() {
    return getMarketo().validatedPost(
      String.format(Urls.BULK_EXPORT_LEADS_ENQUEUE, getJobId()),
      Collections.emptyMap(),
      inputStream -> Helpers.streamToObject(inputStream, LeadsExport.class),
      null, null).singleExport();
  }
}
