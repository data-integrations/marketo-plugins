package io.cdap.plugin.marketo.common.api;

import io.cdap.plugin.marketo.common.api.entities.leads.LeadsExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Leads export job.
 */
public class LeadsExportJob {
  private static final Logger LOG = LoggerFactory.getLogger(LeadsExportJob.class);
  private static final List<String> WAIT_ABLE_STATE = Arrays.asList("Queued", "Processing");
  private static final String ENQUEUE_ABLE_STATUS = "Created";
  private static final String COMPLETED_STATUS = "Completed";

  private String jobId;
  private LeadsExport.ExportResponse lastStatus;
  private Marketo marketo;

  public LeadsExportJob(LeadsExport.ExportResponse lastStatus, Marketo marketo) {
    this.jobId = lastStatus.getExportId();
    this.lastStatus = lastStatus;
    this.marketo = marketo;
    LOG.info("BULK LEADS EXPORT - created job '{}'", this.jobId);
  }

  public String getLastStatus() {
    return lastStatus.getStatus();
  }

  public void waitCompletion() throws InterruptedException {
    if (!WAIT_ABLE_STATE.contains(getLastStatus())) {
      throw new IllegalStateException("Job must be enqueued before waiting for completion.");
    }

    do {
      Thread.sleep(TimeUnit.SECONDS.toMillis(30));
      LeadsExport.ExportResponse newState = marketo.leadsExportJobStatus(jobId);
      logStatusChange(getLastStatus(), newState.getStatus());
      lastStatus = newState;
    } while (WAIT_ABLE_STATE.contains(getLastStatus()));

    if (!getLastStatus().equals(COMPLETED_STATUS)) {
      throw new IllegalStateException("Job expected to be in Completed state, but was in " + getLastStatus());
    }
  }

  public void enqueue() {
    if (!getLastStatus().equals(ENQUEUE_ABLE_STATUS)) {
      throw new IllegalStateException("Job must be in Created status before enqueuing, but was in " + getLastStatus());
    }

    LeadsExport.ExportResponse newState = marketo.validatedPost(
      String.format(Urls.BULK_EXPORT_LEADS_ENQUEUE, jobId),
      Collections.emptyMap(),
      inputStream -> Helpers.streamToObject(inputStream, LeadsExport.class),
      null, null).singleExport();

    logStatusChange(getLastStatus(), newState.getStatus());

    if (!(newState.getStatus().equals("Queued") || newState.getStatus().equals("Processing"))) {
      throw new IllegalStateException(
        String.format("Expected Queued|Processing state for job '%s' but got '%s'", jobId, newState.getStatus()));
    }

    lastStatus = newState;
  }

  private void logStatusChange(String oldStatus, String newStatus) {
    if (!oldStatus.equals(newStatus)) {
      LOG.info("BULK LEADS EXPORT - job '{}' changed state '{}' -> '{}'", jobId, oldStatus, newStatus);
    }
  }

  public String getFile() {
    return marketo.get(marketo.buildUri(String.format(Urls.BULK_EXPORT_LEADS_FILE, jobId), Collections.emptyMap()),
                       Helpers::streamToString);
  }

  public String getJobId() {
    return jobId;
  }
}
