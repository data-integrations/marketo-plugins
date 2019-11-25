package io.cdap.plugin.marketo.common.api;

import io.cdap.plugin.marketo.common.api.entities.leads.LeadsExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Leads export job.
 */
public class LeadsExportJob {
  private static final Logger LOG = LoggerFactory.getLogger(LeadsExportJob.class);
  private static final List<String> WAITABLE_STATE = Arrays.asList("Queued", "Processing");
  private static final List<String> COMPLETED_STATUS = Arrays.asList("Canceled", "Completed", "Failed");

  private String jobId;
  private LeadsExport.ExportResponse last;
  private Marketo marketo;

  public LeadsExportJob(LeadsExport lastStatus, Marketo marketo) {
    this.jobId = lastStatus.singleExport().getExportId();
    this.last = lastStatus.singleExport();
    this.marketo = marketo;
    LOG.info("Created bulk lead export job with id '{}'", this.jobId);
  }

  public String getStatus() {
    return last.getStatus();
  }

  public void waitCompletion() throws InterruptedException {
    if (!WAITABLE_STATE.contains(getStatus())) {
      throw new IllegalStateException("Job must be enqueued before waiting for completion.");
    }

    while (!COMPLETED_STATUS.contains(getStatus())) {
      LeadsExport currentResp = marketo.validatedGet(
        String.format(Urls.BULK_EXPORT_LEADS_STATUS, jobId),
        Collections.emptyMap(), inputStream -> Helpers.streamToObject(inputStream, LeadsExport.class));
      LeadsExport.ExportResponse current = currentResp.singleExport();
      String previousStatus = getStatus();
      String currentStatus = current.getStatus();
      if (!currentStatus.equals(previousStatus)) {
        LOG.info("Bulk lead export job with id '{}' changed status from '{}' to '{}'", jobId, previousStatus,
                 currentStatus);
      }
      last = current;
      Thread.sleep(30 * 1000);
    }
    LOG.info("Bulk lead export job with id '{}' finished with status '{}'", jobId, getStatus());
  }

  public void enqueue() {
    last = marketo.post(String.format(Urls.BULK_EXPORT_LEADS_ENQUEUE, jobId),
                        null, LeadsExport.class).singleExport();

    LOG.info("Bulk lead export job with id '{}' enqueued", jobId);
  }

  public String getFile() {
    return marketo.get(marketo.buildUri(String.format(Urls.BULK_EXPORT_LEADS_FILE, jobId), Collections.emptyMap()),
                       Helpers::streamToString);
  }
}
