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

package io.cdap.plugin.marketo.source.batch;

import com.google.common.base.Strings;
import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Macro;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.cdap.etl.api.FailureCollector;
import io.cdap.plugin.common.IdUtils;
import io.cdap.plugin.common.ReferencePluginConfig;
import io.cdap.plugin.marketo.common.api.Marketo;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

/**
 * Provides all required configuration for reading Marketo entities.
 */
public class MarketoReportingSourceConfig extends ReferencePluginConfig {
  public static final String PROPERTY_CLIENT_ID = "clientId";
  public static final String PROPERTY_CLIENT_SECRET = "clientSecret";
  public static final String PROPERTY_REST_API_ENDPOINT = "restApiEndpoint";
  public static final String PROPERTY_REPORT_TYPE = "reportType";
  public static final String PROPERTY_START_DATE = "startDate";
  public static final String PROPERTY_END_DATE = "endDate";

  @Name(PROPERTY_CLIENT_ID)
  @Description("Marketo Client ID.")
  @Macro
  protected String clientId;

  @Name(PROPERTY_CLIENT_SECRET)
  @Description("Marketo Client secret.")
  @Macro
  protected String clientSecret;

  @Name(PROPERTY_REST_API_ENDPOINT)
  @Description("REST API endpoint URL.")
  @Macro
  protected String restApiEndpoint;

  @Name(PROPERTY_REPORT_TYPE)
  @Description("Report type format, leads or activities.")
  @Macro
  protected String reportType;

  @Name(PROPERTY_START_DATE)
  @Description("Start date for the report.")
  @Macro
  protected String startDate;

  @Name(PROPERTY_END_DATE)
  @Description("End date for the report.")
  @Macro
  protected String endDate;

  private transient Schema schema = null;
  private transient Marketo marketo = null;

  public MarketoReportingSourceConfig(String referenceName) {
    super(referenceName);
  }

  public Schema getSchema() {
    if (schema == null) {
      schema = MarketoReportingSchemaHelper.getSchema(this);
    }
    return schema;
  }

  public Marketo getMarketo() {
    if (marketo == null) {
      marketo = new Marketo(getRestApiEndpoint(), getClientId(), getClientSecret());
    }
    return marketo;
  }

  public String getClientId() {
    return clientId;
  }

  public String getClientSecret() {
    return clientSecret;
  }

  public String getRestApiEndpoint() {
    return restApiEndpoint;
  }

  public String getStartDate() {
    return startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public ReportType getReportType() {
    return ReportType.fromString(reportType);
  }

  void validate(FailureCollector failureCollector) {
    IdUtils.validateReferenceName(referenceName, failureCollector);
    validateDate(failureCollector);
    validateReportType(failureCollector);
    validateMarketoEndpoint(failureCollector);
    validateSecrets(failureCollector);
  }

  void validateDate(FailureCollector failureCollector) {
    if (!containsMacro(PROPERTY_START_DATE)) {
      try {
        OffsetDateTime.parse(getStartDate());
      } catch (DateTimeParseException ex) {
        failureCollector.addFailure("Failed to parse start date.",
                                    "Correct date to ISO 8601 format.")
          .withConfigProperty(PROPERTY_START_DATE);
      }
    }

    if (!containsMacro(PROPERTY_END_DATE)) {
      try {
        OffsetDateTime.parse(getStartDate());
      } catch (DateTimeParseException ex) {
        failureCollector.addFailure("Failed to parse end date.",
                                    "Correct date to ISO 8601 format.")
          .withConfigProperty(PROPERTY_END_DATE);
      }
    }

    if (!(containsMacro(PROPERTY_START_DATE) && containsMacro(PROPERTY_END_DATE))) {
      try {
        OffsetDateTime start = OffsetDateTime.parse(getStartDate());
        OffsetDateTime end = OffsetDateTime.parse(getEndDate());

        if (start.compareTo(end) > 0) {
          failureCollector.addFailure("Start date cannot be greater than the end date.", "Swap dates.")
            .withConfigProperty(PROPERTY_START_DATE).withConfigProperty(PROPERTY_END_DATE);
        }
      } catch (DateTimeParseException ex) {
        // silently ignore parsing exceptions, we already pushed messages for malformed dates
      }
    }
  }

  void validateReportType(FailureCollector failureCollector) {
    if (!containsMacro(PROPERTY_REPORT_TYPE)) {
      try {
        getReportType();
      } catch (IllegalArgumentException ex) {
        failureCollector.addFailure(String.format("Incorrect reporting type '%s'.", reportType),
                                    "Set reporting type to 'activities' or 'leads'.")
          .withConfigProperty(PROPERTY_REPORT_TYPE);
      }
    }
  }

  void validateSecrets(FailureCollector failureCollector) {
    if (!containsMacro(PROPERTY_CLIENT_ID) && Strings.isNullOrEmpty(getClientId())) {
      failureCollector.addFailure("Client ID is empty.", null)
        .withConfigProperty(PROPERTY_CLIENT_ID);
    }

    if (!containsMacro(PROPERTY_CLIENT_SECRET) && Strings.isNullOrEmpty(getClientSecret())) {
      failureCollector.addFailure("Client Secret is empty.", null)
        .withConfigProperty(PROPERTY_CLIENT_SECRET);
    }
  }

  void validateMarketoEndpoint(FailureCollector failureCollector) {
    if (!containsMacro(PROPERTY_REST_API_ENDPOINT)) {
      try {
        new URL(getRestApiEndpoint());
      } catch (MalformedURLException e) {
        failureCollector
          .addFailure(String.format("Malformed Marketo Rest API endpoint URL '%s'.", getRestApiEndpoint()), null)
          .withConfigProperty(PROPERTY_REST_API_ENDPOINT);
      }
    }
  }
}
