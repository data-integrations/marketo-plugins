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

import io.cdap.cdap.api.annotation.Description;
import io.cdap.cdap.api.annotation.Macro;
import io.cdap.cdap.api.annotation.Name;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.plugin.common.ReferencePluginConfig;
import io.cdap.plugin.marketo.common.api.Marketo;
import io.cdap.plugin.marketo.common.api.entities.MarketoToken;

/**
 * Provides all required configuration for reading Marketo entities.
 */
public class MarketoReportingSourceConfig extends ReferencePluginConfig {
  public static final String PROPERTY_ENTITY_NAME = "entityName";
  public static final String PROPERTY_CLIENT_ID = "clientId";
  public static final String PROPERTY_CLIENT_SECRET = "clientSecret";
  public static final String PROPERTY_REST_API_ENDPOINT = "restApiEndpoint";
  public static final String PROPERTY_REST_API_IDENTITY = "restApiIdentity";
  public static final String PROPERTY_DAILY_API_LIMIT = "dailyApiLimit";
  public static final String PROPERTY_REPORT_TYPE = "reportType";
  public static final String PROPERTY_REPORT_FORMAT = "reportFormat";
  public static final String PROPERTY_START_DATE = "startDate";
  public static final String PROPERTY_END_DATE = "endDate";

  @Name(PROPERTY_ENTITY_NAME)
  @Description("Marketo entity name to fetch.")
  @Macro
  protected String entityName;

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

  @Name(PROPERTY_REST_API_IDENTITY)
  @Description("REST API identity.")
  @Macro
  protected String restApiIdentity;

  @Name(PROPERTY_DAILY_API_LIMIT)
  @Description("Marketo enforced daily API limit.")
  @Macro
  protected String dailyApiLimit;

  @Name(PROPERTY_REPORT_TYPE)
  @Description("Report type format, leads or activities.")
  @Macro
  protected String reportType;

  @Name(PROPERTY_REPORT_FORMAT)
  @Description("Report format.")
  @Macro
  protected String reportFormat;

  @Name(PROPERTY_START_DATE)
  @Description("Start date for the report.")
  @Macro
  protected String startDate;

  @Name(PROPERTY_END_DATE)
  @Description("End date for the report.")
  @Macro
  protected String endDate;


  private transient MarketoToken token = null;
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

  public String getRestApiIdentity() {
    return restApiIdentity;
  }

  public String getDailyApiLimit() {
    return dailyApiLimit;
  }

  public String getReportFormat() {
    return reportFormat;
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
}
