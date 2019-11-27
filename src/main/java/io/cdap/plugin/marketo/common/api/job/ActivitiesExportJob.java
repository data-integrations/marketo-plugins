package io.cdap.plugin.marketo.common.api.job;

import io.cdap.plugin.marketo.common.api.Helpers;
import io.cdap.plugin.marketo.common.api.Marketo;
import io.cdap.plugin.marketo.common.api.Urls;
import io.cdap.plugin.marketo.common.api.entities.activities.ActivitiesExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

/**
 * Activities export job.
 */
public class ActivitiesExportJob extends AbstractBulkExportJob<ActivitiesExport.ExportResponse> {
  private static final Logger LOG = LoggerFactory.getLogger(ActivitiesExportJob.class);

  public ActivitiesExportJob(ActivitiesExport.ExportResponse lastState, Marketo marketo) {
    super(lastState.getExportId(), lastState, marketo);
  }

  @Override
  public Logger getLogger() {
    return LOG;
  }

  @Override
  public ActivitiesExport.ExportResponse getFreshState() {
    return getMarketo().activitiesExportJobStatus(getJobId());
  }

  @Override
  public String getStateStatus(ActivitiesExport.ExportResponse state) {
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
  protected ActivitiesExport.ExportResponse enqueueImpl() {
    return getMarketo().validatedPost(
      String.format(Urls.BULK_EXPORT_ACTIVITIES_ENQUEUE, getJobId()),
      Collections.emptyMap(),
      inputStream -> Helpers.streamToObject(inputStream, ActivitiesExport.class),
      null, null).singleExport();
  }
}
