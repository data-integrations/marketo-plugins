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
import io.cdap.plugin.common.ReferencePluginConfig;
import io.cdap.plugin.marketo.common.api.Helpers;
import io.cdap.plugin.marketo.common.api.Marketo;

import java.net.MalformedURLException;
import java.net.URL;
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
    validateDate(failureCollector);
    validateReportType(failureCollector);
    validateMarketoEndpoint(failureCollector);
    validateSecrets(failureCollector);
  }

  void validateDate(FailureCollector failureCollector) {
    if (!(containsMacro(PROPERTY_START_DATE) && containsMacro(PROPERTY_END_DATE))) {
      try {
        Helpers.getDateRanges(getStartDate(), getEndDate());
      } catch (IllegalArgumentException ex) {
        String message = String.format("Failed to validate dates: %s.", ex.getMessage());
        String correctiveAction = null;
        if (ex.getMessage().contains("start date more than end date")) {
          message = "Start date more than end date.";
          correctiveAction = "Swap dates.";
        }
        failureCollector.addFailure(message, correctiveAction)
          .withConfigProperty(PROPERTY_START_DATE).withConfigProperty(PROPERTY_END_DATE);
      } catch (DateTimeParseException ex) {
        failureCollector.addFailure("Failed to parse one of dates.",
                                    "Correct dates to ISO 8601 format.")
          .withConfigProperty(PROPERTY_START_DATE).withConfigProperty(PROPERTY_END_DATE);
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
      failureCollector.addFailure("Client ID is null or empty.",
                                  "Set Client ID to non empty string.")
        .withConfigProperty(PROPERTY_CLIENT_ID);
    }

    if (!containsMacro(PROPERTY_CLIENT_SECRET) && Strings.isNullOrEmpty(getClientSecret())) {
      failureCollector.addFailure("Client Secret is null or empty.",
                                  "Set Client Secret to non empty string.")
        .withConfigProperty(PROPERTY_CLIENT_SECRET);
    }
  }

  void validateMarketoEndpoint(FailureCollector failureCollector) {
    if (!containsMacro(PROPERTY_REST_API_ENDPOINT)) {
      try {
        new URL(getRestApiEndpoint());
      } catch (MalformedURLException e) {
        failureCollector
          .addFailure(String.format("Malformed Marketo Rest API endpoint URL '%s'.", getRestApiEndpoint()),
                      "Change URL to valid.")
          .withConfigProperty(PROPERTY_REST_API_ENDPOINT);
      }
    }
  }
}
