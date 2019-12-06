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

package io.cdap.plugin.marketo.source.batch.entity;

import io.cdap.cdap.api.data.format.StructuredRecord;
import io.cdap.cdap.api.data.schema.Schema;
import io.cdap.plugin.marketo.common.api.entities.asset.Email;
import io.cdap.plugin.marketo.common.api.entities.asset.EmailCCField;
import io.cdap.plugin.marketo.common.api.entities.asset.EmailField;
import io.cdap.plugin.marketo.common.api.entities.asset.EmailTemplate;
import io.cdap.plugin.marketo.common.api.entities.asset.File;
import io.cdap.plugin.marketo.common.api.entities.asset.FileFolder;
import io.cdap.plugin.marketo.common.api.entities.asset.Folder;
import io.cdap.plugin.marketo.common.api.entities.asset.FolderDescriptor;
import io.cdap.plugin.marketo.common.api.entities.asset.Form;
import io.cdap.plugin.marketo.common.api.entities.asset.FormField;
import io.cdap.plugin.marketo.common.api.entities.asset.FormKnownVisitorDTO;
import io.cdap.plugin.marketo.common.api.entities.asset.FormThankYouPageDTO;
import io.cdap.plugin.marketo.common.api.entities.asset.LandingPage;
import io.cdap.plugin.marketo.common.api.entities.asset.LandingPageTemplate;
import io.cdap.plugin.marketo.common.api.entities.asset.Program;
import io.cdap.plugin.marketo.common.api.entities.asset.Recurrence;
import io.cdap.plugin.marketo.common.api.entities.asset.Segmentation;
import io.cdap.plugin.marketo.common.api.entities.asset.SmartCampaign;
import io.cdap.plugin.marketo.common.api.entities.asset.SmartList;
import io.cdap.plugin.marketo.common.api.entities.asset.Snippet;
import io.cdap.plugin.marketo.common.api.entities.asset.StaticList;
import io.cdap.plugin.marketo.common.api.entities.asset.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Schema helper for entity plugin.
 */
@SuppressWarnings("DuplicatedCode")
public final class EntitySchemaHelper {
  public static Schema schemaForEntityName(String entityName) {
    switch (entityName) {
      case "Email": {
        return getEmailSchema();
      }
      case "EmailCCField": {
        return getEmailCCFieldSchema();
      }
      case "EmailField": {
        return getEmailFieldSchema();
      }
      case "EmailTemplate": {
        return getEmailTemplateSchema();
      }
      case "File": {
        return getFileSchema();
      }
      case "FileFolder": {
        return getFileFolderSchema();
      }
      case "Folder": {
        return getFolderSchema();
      }
      case "FolderDescriptor": {
        return getFolderDescriptorSchema();
      }
      case "Form": {
        return getFormSchema();
      }
      case "FormField": {
        return getFormFieldSchema();
      }
      case "FormKnownVisitorDTO": {
        return getFormKnownVisitorDTOSchema();
      }
      case "FormThankYouPageDTO": {
        return getFormThankYouPageDTOSchema();
      }
      case "LandingPage": {
        return getLandingPageSchema();
      }
      case "LandingPageTemplate": {
        return getLandingPageTemplateSchema();
      }
      case "Program": {
        return getProgramSchema();
      }
      case "Recurrence": {
        return getRecurrenceSchema();
      }
      case "Segmentation": {
        return getSegmentationSchema();
      }
      case "SmartCampaign": {
        return getSmartCampaignSchema();
      }
      case "SmartList": {
        return getSmartListSchema();
      }
      case "Snippet": {
        return getSnippetSchema();
      }
      case "StaticList": {
        return getStaticListSchema();
      }
      case "Tag": {
        return getTagSchema();
      }
      default: {
        throw new IllegalArgumentException("Unknown entity name:" + entityName);
      }
    }
  }

  public static Schema getEmailSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("createdAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("description", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("folder", Schema.nullableOf(EntitySchemaHelper.getFolderDescriptorSchema())));
    fields.add(Schema.Field.of("fromEmail", Schema.nullableOf(EntitySchemaHelper.getEmailFieldSchema())));
    fields.add(Schema.Field.of("fromName", Schema.nullableOf(EntitySchemaHelper.getEmailFieldSchema())));
    fields.add(Schema.Field.of("id", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("name", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("operational", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("publishToMSI", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("replyEmail", Schema.nullableOf(EntitySchemaHelper.getEmailFieldSchema())));
    fields.add(Schema.Field.of("status", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("subject", Schema.nullableOf(EntitySchemaHelper.getEmailFieldSchema())));
    fields.add(Schema.Field.of("template", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("textOnly", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("updatedAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("url", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("version", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("webView", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("workspace", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("autoCopyToText", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("ccFields",
                               Schema.nullableOf(Schema.arrayOf(EntitySchemaHelper.getEmailCCFieldSchema()))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getEmailCCFieldSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("attributeId", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("objectName", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("displayName", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("apiName", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getEmailFieldSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("type", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("value", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getEmailTemplateSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("createdAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("description", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("folder", Schema.nullableOf(EntitySchemaHelper.getFolderDescriptorSchema())));
    fields.add(Schema.Field.of("id", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("name", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("status", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("updatedAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("url", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("version", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("workspace", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getFileSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("createdAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("description", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("folder", Schema.nullableOf(EntitySchemaHelper.getFileFolderSchema())));
    fields.add(Schema.Field.of("id", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("mimeType", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("name", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("size", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("updatedAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("url", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getFileFolderSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("id", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("name", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("type", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getFolderSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("accessZoneId", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("createdAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("description", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("folderId", Schema.nullableOf(EntitySchemaHelper.getFolderDescriptorSchema())));
    fields.add(Schema.Field.of("folderType", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("id", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("isArchive", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("isSystem", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("name", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("parent", Schema.nullableOf(EntitySchemaHelper.getFolderDescriptorSchema())));
    fields.add(Schema.Field.of("path", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("updatedAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("url", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("workspace", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getFolderDescriptorSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("id", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("type", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("folderName", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getFormSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("buttonLabel", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("buttonLocation", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("createdAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("description", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("folder", Schema.nullableOf(EntitySchemaHelper.getFolderDescriptorSchema())));
    fields.add(Schema.Field.of("fontFamily", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("fontSize", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("id", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("knownVisitor",
                               Schema.nullableOf(EntitySchemaHelper.getFormKnownVisitorDTOSchema())));
    fields.add(Schema.Field.of("labelPosition", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("language", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("locale", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("name", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("progressiveProfiling", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("status", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("thankYouList",
                               Schema.nullableOf(Schema.arrayOf(EntitySchemaHelper.getFormThankYouPageDTOSchema()))));
    fields.add(Schema.Field.of("theme", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("updatedAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("url", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("waitingLabel", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getFormFieldSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("dataType", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("defaultValue", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("description", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("fieldMaskValues", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("fieldWidth", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("id", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("initiallyChecked", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("isLabelToRight", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("isMultiselect", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("isRequired", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("labelWidth", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("maxLength", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("maximumNumber", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("minimumNumber", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("picklistValues", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("placeholderText", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("validationMessage", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("visibleRows", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getFormKnownVisitorDTOSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("template", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("type", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getFormThankYouPageDTOSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("isDefault", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("followupType", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("followupValue", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("operator", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("subjectField", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("values", Schema.nullableOf(Schema.arrayOf(Schema.of(Schema.Type.STRING)))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getLandingPageSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("url", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("computedUrl", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("createdAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("customHeadHTML", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("description", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("facebookOgTags", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("folder", Schema.nullableOf(EntitySchemaHelper.getFolderDescriptorSchema())));
    fields.add(Schema.Field.of("formPrefill", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("id", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("keywords", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("mobileEnabled", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("name", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("robots", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("status", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("template", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("title", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("updatedAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("workspace", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getLandingPageTemplateSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("createdAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("description", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("enableMunchkin", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("folder", Schema.nullableOf(EntitySchemaHelper.getFolderDescriptorSchema())));
    fields.add(Schema.Field.of("id", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("name", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("status", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("templateType", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("updatedAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("url", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("workspace", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getProgramSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("channel", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("createdAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("description", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("folder", Schema.nullableOf(EntitySchemaHelper.getFolderDescriptorSchema())));
    fields.add(Schema.Field.of("id", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("name", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("sfdcId", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("sfdcName", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("status", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("type", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("updatedAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("url", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("workspace", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getRecurrenceSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("startAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("endAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("intervalType", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("interval", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("weekdayOnly", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("weekdayMask",
                               Schema.nullableOf(Schema.arrayOf(Schema.of(Schema.Type.STRING)))));
    fields.add(Schema.Field.of("dayOfMonth", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("dayOfWeek", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("weekOfMonth", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getSegmentationSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("createdAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("description", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("folder", Schema.nullableOf(EntitySchemaHelper.getFolderDescriptorSchema())));
    fields.add(Schema.Field.of("id", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("name", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("status", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("updatedAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("url", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("workspace", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getSmartCampaignSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("id", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("name", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("description", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("type", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("isSystem", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("isActive", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("isRequestable", Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("recurrence", Schema.nullableOf(EntitySchemaHelper.getRecurrenceSchema())));
    fields.add(Schema.Field.of("qualificationRuleType", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("qualificationRuleInterval", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("qualificationRuleUnit", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("maxMembers", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("isCommunicationLimitEnabled",
                               Schema.nullableOf(Schema.of(Schema.Type.BOOLEAN))));
    fields.add(Schema.Field.of("smartListId", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("flowId", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("folder", Schema.nullableOf(EntitySchemaHelper.getFolderDescriptorSchema())));
    fields.add(Schema.Field.of("createdAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("updatedAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("workspace", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("status", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getSmartListSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("id", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("name", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("description", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("createdAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("updatedAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("url", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("folder", Schema.nullableOf(EntitySchemaHelper.getFolderDescriptorSchema())));
    fields.add(Schema.Field.of("workspace", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getSnippetSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("createdAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("description", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("folder", Schema.nullableOf(EntitySchemaHelper.getFolderDescriptorSchema())));
    fields.add(Schema.Field.of("id", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("name", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("status", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("updatedAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("url", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("workspace", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getStaticListSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("id", Schema.nullableOf(Schema.of(Schema.Type.INT))));
    fields.add(Schema.Field.of("name", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("description", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("createdAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("updatedAt", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("url", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("folder", Schema.nullableOf(EntitySchemaHelper.getFolderDescriptorSchema())));
    fields.add(Schema.Field.of("workspace", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("computedUrl", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static Schema getTagSchema() {
    List<Schema.Field> fields = new ArrayList<>();
    fields.add(Schema.Field.of("applicableProgramTypes", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("required", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    fields.add(Schema.Field.of("tagType", Schema.nullableOf(Schema.of(Schema.Type.STRING))));
    return Schema.recordOf(UUID.randomUUID().toString().replace("-", ""), fields);
  }

  public static StructuredRecord structuredRecordFromEntity(String entityName, Object entity,
                                                            Schema schema) {
    StructuredRecord.Builder builder = StructuredRecord.builder(schema);
    switch (entityName) {
      case "Email": {
        Email email = (Email) entity;
        if (entity == null) {
          break;
        }
        builder.set("createdAt", email.getCreatedAt());
        builder.set("description", email.getDescription());
        builder.set("folder", EntitySchemaHelper.structuredRecordFromEntity(
          "FolderDescriptor",
          email.getFolder(),
          schema.getField("folder").getSchema().getNonNullable()));
        builder.set("fromEmail", EntitySchemaHelper.structuredRecordFromEntity(
          "EmailField",
          email.getFromEmail(),
          schema.getField("fromEmail").getSchema().getNonNullable()));
        builder.set("fromName", EntitySchemaHelper.structuredRecordFromEntity(
          "EmailField",
          email.getFromName(),
          schema.getField("fromName").getSchema().getNonNullable()));
        builder.set("id", email.getId());
        builder.set("name", email.getName());
        builder.set("operational", email.getOperational());
        builder.set("publishToMSI", email.getPublishToMSI());
        builder.set("replyEmail", EntitySchemaHelper.structuredRecordFromEntity(
          "EmailField",
          email.getReplyEmail(),
          schema.getField("replyEmail").getSchema().getNonNullable()));
        builder.set("status", email.getStatus());
        builder.set("subject", EntitySchemaHelper.structuredRecordFromEntity(
          "EmailField",
          email.getSubject(),
          schema.getField("subject").getSchema().getNonNullable()));
        builder.set("template", email.getTemplate());
        builder.set("textOnly", email.getTextOnly());
        builder.set("updatedAt", email.getUpdatedAt());
        builder.set("url", email.getUrl());
        builder.set("version", email.getVersion());
        builder.set("webView", email.getWebView());
        builder.set("workspace", email.getWorkspace());
        builder.set("autoCopyToText", email.getAutoCopyToText());
        builder.set(
          "ccFields",
          email.getCcFields().stream()
            .map(ent -> EntitySchemaHelper.structuredRecordFromEntity(
              "EmailCCField",
              ent,
              schema.getField("ccFields").getSchema().getNonNullable().getComponentSchema()))
            .collect(Collectors.toList())
        );
        break;
      }
      case "EmailCCField": {
        EmailCCField emailccfield = (EmailCCField) entity;
        if (entity == null) {
          break;
        }
        builder.set("attributeId", emailccfield.getAttributeId());
        builder.set("objectName", emailccfield.getObjectName());
        builder.set("displayName", emailccfield.getDisplayName());
        builder.set("apiName", emailccfield.getApiName());
        break;
      }
      case "EmailField": {
        EmailField emailfield = (EmailField) entity;
        if (entity == null) {
          break;
        }
        builder.set("type", emailfield.getType());
        builder.set("value", emailfield.getValue());
        break;
      }
      case "EmailTemplate": {
        EmailTemplate emailtemplate = (EmailTemplate) entity;
        if (entity == null) {
          break;
        }
        builder.set("createdAt", emailtemplate.getCreatedAt());
        builder.set("description", emailtemplate.getDescription());
        builder.set("folder", EntitySchemaHelper.structuredRecordFromEntity(
          "FolderDescriptor",
          emailtemplate.getFolder(),
          schema.getField("folder").getSchema().getNonNullable()));
        builder.set("id", emailtemplate.getId());
        builder.set("name", emailtemplate.getName());
        builder.set("status", emailtemplate.getStatus());
        builder.set("updatedAt", emailtemplate.getUpdatedAt());
        builder.set("url", emailtemplate.getUrl());
        builder.set("version", emailtemplate.getVersion());
        builder.set("workspace", emailtemplate.getWorkspace());
        break;
      }
      case "File": {
        File file = (File) entity;
        if (entity == null) {
          break;
        }
        builder.set("createdAt", file.getCreatedAt());
        builder.set("description", file.getDescription());
        builder.set("folder", EntitySchemaHelper.structuredRecordFromEntity(
          "FileFolder",
          file.getFolder(),
          schema.getField("folder").getSchema().getNonNullable()));
        builder.set("id", file.getId());
        builder.set("mimeType", file.getMimeType());
        builder.set("name", file.getName());
        builder.set("size", file.getSize());
        builder.set("updatedAt", file.getUpdatedAt());
        builder.set("url", file.getUrl());
        break;
      }
      case "FileFolder": {
        FileFolder filefolder = (FileFolder) entity;
        if (entity == null) {
          break;
        }
        builder.set("id", filefolder.getId());
        builder.set("name", filefolder.getName());
        builder.set("type", filefolder.getType());
        break;
      }
      case "Folder": {
        Folder folder = (Folder) entity;
        if (entity == null) {
          break;
        }
        builder.set("accessZoneId", folder.getAccessZoneId());
        builder.set("createdAt", folder.getCreatedAt());
        builder.set("description", folder.getDescription());
        builder.set("folderId", EntitySchemaHelper.structuredRecordFromEntity(
          "FolderDescriptor",
          folder.getFolderId(),
          schema.getField("folderId").getSchema().getNonNullable()));
        builder.set("folderType", folder.getFolderType());
        builder.set("id", folder.getId());
        builder.set("isArchive", folder.getArchive());
        builder.set("isSystem", folder.getSystem());
        builder.set("name", folder.getName());
        builder.set("parent", EntitySchemaHelper.structuredRecordFromEntity(
          "FolderDescriptor",
          folder.getParent(),
          schema.getField("parent").getSchema().getNonNullable()));
        builder.set("path", folder.getPath());
        builder.set("updatedAt", folder.getUpdatedAt());
        builder.set("url", folder.getUrl());
        builder.set("workspace", folder.getWorkspace());
        break;
      }
      case "FolderDescriptor": {
        FolderDescriptor folderdescriptor = (FolderDescriptor) entity;
        if (entity == null) {
          break;
        }
        builder.set("id", folderdescriptor.getId());
        builder.set("type", folderdescriptor.getType());
        builder.set("folderName", folderdescriptor.getFolderName());
        break;
      }
      case "Form": {
        Form form = (Form) entity;
        if (entity == null) {
          break;
        }
        builder.set("buttonLabel", form.getButtonLabel());
        builder.set("buttonLocation", form.getButtonLocation());
        builder.set("createdAt", form.getCreatedAt());
        builder.set("description", form.getDescription());
        builder.set("folder", EntitySchemaHelper.structuredRecordFromEntity(
          "FolderDescriptor",
          form.getFolder(),
          schema.getField("folder").getSchema().getNonNullable()));
        builder.set("fontFamily", form.getFontFamily());
        builder.set("fontSize", form.getFontSize());
        builder.set("id", form.getId());
        builder.set("knownVisitor", EntitySchemaHelper.structuredRecordFromEntity(
          "FormKnownVisitorDTO",
          form.getKnownVisitor(),
          schema.getField("knownVisitor").getSchema().getNonNullable()));
        builder.set("labelPosition", form.getLabelPosition());
        builder.set("language", form.getLanguage());
        builder.set("locale", form.getLocale());
        builder.set("name", form.getName());
        builder.set("progressiveProfiling", form.getProgressiveProfiling());
        builder.set("status", form.getStatus());
        builder.set(
          "thankYouList",
          form.getThankYouList().stream()
            .map(ent -> EntitySchemaHelper.structuredRecordFromEntity(
              "FormThankYouPageDTO",
              ent,
              schema.getField("thankYouList").getSchema().getNonNullable().getComponentSchema()))
            .collect(Collectors.toList())
        );
        builder.set("theme", form.getTheme());
        builder.set("updatedAt", form.getUpdatedAt());
        builder.set("url", form.getUrl());
        builder.set("waitingLabel", form.getWaitingLabel());
        break;
      }
      case "FormField": {
        FormField formfield = (FormField) entity;
        if (entity == null) {
          break;
        }
        builder.set("dataType", formfield.getDataType());
        builder.set("defaultValue", formfield.getDefaultValue());
        builder.set("description", formfield.getDescription());
        builder.set("fieldMaskValues", formfield.getFieldMaskValues());
        builder.set("fieldWidth", formfield.getFieldWidth());
        builder.set("id", formfield.getId());
        builder.set("initiallyChecked", formfield.getInitiallyChecked());
        builder.set("isLabelToRight", formfield.getLabelToRight());
        builder.set("isMultiselect", formfield.getMultiselect());
        builder.set("isRequired", formfield.getRequired());
        builder.set("labelWidth", formfield.getLabelWidth());
        builder.set("maxLength", formfield.getMaxLength());
        builder.set("maximumNumber", formfield.getMaximumNumber());
        builder.set("minimumNumber", formfield.getMinimumNumber());
        builder.set("picklistValues", formfield.getPicklistValues());
        builder.set("placeholderText", formfield.getPlaceholderText());
        builder.set("validationMessage", formfield.getValidationMessage());
        builder.set("visibleRows", formfield.getVisibleRows());
        break;
      }
      case "FormKnownVisitorDTO": {
        FormKnownVisitorDTO formknownvisitordto = (FormKnownVisitorDTO) entity;
        if (entity == null) {
          break;
        }
        builder.set("template", formknownvisitordto.getTemplate());
        builder.set("type", formknownvisitordto.getType());
        break;
      }
      case "FormThankYouPageDTO": {
        FormThankYouPageDTO formthankyoupagedto = (FormThankYouPageDTO) entity;
        if (entity == null) {
          break;
        }
        builder.set("isDefault", formthankyoupagedto.getDefault());
        builder.set("followupType", formthankyoupagedto.getFollowupType());
        builder.set("followupValue", formthankyoupagedto.getFollowupValue());
        builder.set("operator", formthankyoupagedto.getOperator());
        builder.set("subjectField", formthankyoupagedto.getSubjectField());
        builder.set("values", formthankyoupagedto.getValues());
        break;
      }
      case "LandingPage": {
        LandingPage landingpage = (LandingPage) entity;
        if (entity == null) {
          break;
        }
        builder.set("url", landingpage.getUrl());
        builder.set("computedUrl", landingpage.getComputedUrl());
        builder.set("createdAt", landingpage.getCreatedAt());
        builder.set("customHeadHTML", landingpage.getCustomHeadHTML());
        builder.set("description", landingpage.getDescription());
        builder.set("facebookOgTags", landingpage.getFacebookOgTags());
        builder.set("folder", EntitySchemaHelper.structuredRecordFromEntity(
          "FolderDescriptor",
          landingpage.getFolder(),
          schema.getField("folder").getSchema().getNonNullable()));
        builder.set("formPrefill", landingpage.getFormPrefill());
        builder.set("id", landingpage.getId());
        builder.set("keywords", landingpage.getKeywords());
        builder.set("mobileEnabled", landingpage.getMobileEnabled());
        builder.set("name", landingpage.getName());
        builder.set("robots", landingpage.getRobots());
        builder.set("status", landingpage.getStatus());
        builder.set("template", landingpage.getTemplate());
        builder.set("title", landingpage.getTitle());
        builder.set("updatedAt", landingpage.getUpdatedAt());
        builder.set("workspace", landingpage.getWorkspace());
        break;
      }
      case "LandingPageTemplate": {
        LandingPageTemplate landingpagetemplate = (LandingPageTemplate) entity;
        if (entity == null) {
          break;
        }
        builder.set("createdAt", landingpagetemplate.getCreatedAt());
        builder.set("description", landingpagetemplate.getDescription());
        builder.set("enableMunchkin", landingpagetemplate.getEnableMunchkin());
        builder.set("folder", EntitySchemaHelper.structuredRecordFromEntity(
          "FolderDescriptor",
          landingpagetemplate.getFolder(),
          schema.getField("folder").getSchema().getNonNullable()));
        builder.set("id", landingpagetemplate.getId());
        builder.set("name", landingpagetemplate.getName());
        builder.set("status", landingpagetemplate.getStatus());
        builder.set("templateType", landingpagetemplate.getTemplateType());
        builder.set("updatedAt", landingpagetemplate.getUpdatedAt());
        builder.set("url", landingpagetemplate.getUrl());
        builder.set("workspace", landingpagetemplate.getWorkspace());
        break;
      }
      case "Program": {
        Program program = (Program) entity;
        if (entity == null) {
          break;
        }
        builder.set("channel", program.getChannel());
        builder.set("createdAt", program.getCreatedAt());
        builder.set("description", program.getDescription());
        builder.set("folder", EntitySchemaHelper.structuredRecordFromEntity(
          "FolderDescriptor",
          program.getFolder(),
          schema.getField("folder").getSchema().getNonNullable()));
        builder.set("id", program.getId());
        builder.set("name", program.getName());
        builder.set("sfdcId", program.getSfdcId());
        builder.set("sfdcName", program.getSfdcName());
        builder.set("status", program.getStatus());
        builder.set("type", program.getType());
        builder.set("updatedAt", program.getUpdatedAt());
        builder.set("url", program.getUrl());
        builder.set("workspace", program.getWorkspace());
        break;
      }
      case "Recurrence": {
        Recurrence recurrence = (Recurrence) entity;
        if (entity == null) {
          break;
        }
        builder.set("startAt", recurrence.getStartAt());
        builder.set("endAt", recurrence.getEndAt());
        builder.set("intervalType", recurrence.getIntervalType());
        builder.set("interval", recurrence.getInterval());
        builder.set("weekdayOnly", recurrence.getWeekdayOnly());
        builder.set("weekdayMask", recurrence.getWeekdayMask());
        builder.set("dayOfMonth", recurrence.getDayOfMonth());
        builder.set("dayOfWeek", recurrence.getDayOfWeek());
        builder.set("weekOfMonth", recurrence.getWeekOfMonth());
        break;
      }
      case "Segmentation": {
        Segmentation segmentation = (Segmentation) entity;
        if (entity == null) {
          break;
        }
        builder.set("createdAt", segmentation.getCreatedAt());
        builder.set("description", segmentation.getDescription());
        builder.set("folder", EntitySchemaHelper.structuredRecordFromEntity(
          "FolderDescriptor",
          segmentation.getFolder(),
          schema.getField("folder").getSchema().getNonNullable()));
        builder.set("id", segmentation.getId());
        builder.set("name", segmentation.getName());
        builder.set("status", segmentation.getStatus());
        builder.set("updatedAt", segmentation.getUpdatedAt());
        builder.set("url", segmentation.getUrl());
        builder.set("workspace", segmentation.getWorkspace());
        break;
      }
      case "SmartCampaign": {
        SmartCampaign smartcampaign = (SmartCampaign) entity;
        if (entity == null) {
          break;
        }
        builder.set("id", smartcampaign.getId());
        builder.set("name", smartcampaign.getName());
        builder.set("description", smartcampaign.getDescription());
        builder.set("type", smartcampaign.getType());
        builder.set("isSystem", smartcampaign.getSystem());
        builder.set("isActive", smartcampaign.getActive());
        builder.set("isRequestable", smartcampaign.getRequestable());
        builder.set("recurrence", EntitySchemaHelper.structuredRecordFromEntity(
          "Recurrence",
          smartcampaign.getRecurrence(),
          schema.getField("recurrence").getSchema().getNonNullable()));
        builder.set("qualificationRuleType", smartcampaign.getQualificationRuleType());
        builder.set("qualificationRuleInterval", smartcampaign.getQualificationRuleInterval());
        builder.set("qualificationRuleUnit", smartcampaign.getQualificationRuleUnit());
        builder.set("maxMembers", smartcampaign.getMaxMembers());
        builder.set("isCommunicationLimitEnabled", smartcampaign.getCommunicationLimitEnabled());
        builder.set("smartListId", smartcampaign.getSmartListId());
        builder.set("flowId", smartcampaign.getFlowId());
        builder.set("folder", EntitySchemaHelper.structuredRecordFromEntity(
          "FolderDescriptor",
          smartcampaign.getFolder(),
          schema.getField("folder").getSchema().getNonNullable()));
        builder.set("createdAt", smartcampaign.getCreatedAt());
        builder.set("updatedAt", smartcampaign.getUpdatedAt());
        builder.set("workspace", smartcampaign.getWorkspace());
        builder.set("status", smartcampaign.getStatus());
        break;
      }
      case "SmartList": {
        SmartList smartlist = (SmartList) entity;
        if (entity == null) {
          break;
        }
        builder.set("id", smartlist.getId());
        builder.set("name", smartlist.getName());
        builder.set("description", smartlist.getDescription());
        builder.set("createdAt", smartlist.getCreatedAt());
        builder.set("updatedAt", smartlist.getUpdatedAt());
        builder.set("url", smartlist.getUrl());
        builder.set("folder", EntitySchemaHelper.structuredRecordFromEntity(
          "FolderDescriptor",
          smartlist.getFolder(),
          schema.getField("folder").getSchema().getNonNullable()));
        builder.set("workspace", smartlist.getWorkspace());
        break;
      }
      case "Snippet": {
        Snippet snippet = (Snippet) entity;
        if (entity == null) {
          break;
        }
        builder.set("createdAt", snippet.getCreatedAt());
        builder.set("description", snippet.getDescription());
        builder.set("folder", EntitySchemaHelper.structuredRecordFromEntity(
          "FolderDescriptor",
          snippet.getFolder(),
          schema.getField("folder").getSchema().getNonNullable()));
        builder.set("id", snippet.getId());
        builder.set("name", snippet.getName());
        builder.set("status", snippet.getStatus());
        builder.set("updatedAt", snippet.getUpdatedAt());
        builder.set("url", snippet.getUrl());
        builder.set("workspace", snippet.getWorkspace());
        break;
      }
      case "StaticList": {
        StaticList staticlist = (StaticList) entity;
        if (entity == null) {
          break;
        }
        builder.set("id", staticlist.getId());
        builder.set("name", staticlist.getName());
        builder.set("description", staticlist.getDescription());
        builder.set("createdAt", staticlist.getCreatedAt());
        builder.set("updatedAt", staticlist.getUpdatedAt());
        builder.set("url", staticlist.getUrl());
        builder.set("folder", EntitySchemaHelper.structuredRecordFromEntity(
          "FolderDescriptor",
          staticlist.getFolder(),
          schema.getField("folder").getSchema().getNonNullable()));
        builder.set("workspace", staticlist.getWorkspace());
        builder.set("computedUrl", staticlist.getComputedUrl());
        break;
      }
      case "Tag": {
        Tag tag = (Tag) entity;
        if (entity == null) {
          break;
        }
        builder.set("applicableProgramTypes", tag.getApplicableProgramTypes());
        builder.set("required", tag.getRequired());
        builder.set("tagType", tag.getTagType());
        break;
      }
      default: {
        throw new IllegalArgumentException("Unknown entity name:" + entityName);
      }
    }
    return builder.build();
  }

  /**
   * Valid entity types.
   */
  public enum EntityType {
    EMAIL("Email"),
    EMAILTEMPLATE("EmailTemplate"),
    FILE("File"),
    FOLDER("Folder"),
    FORM("Form"),
    FORMFIELD("FormField"),
    LANDINGPAGE("LandingPage"),
    LANDINGPAGETEMPLATE("LandingPageTemplate"),
    PROGRAM("Program"),
    SEGMENTATION("Segmentation"),
    SMARTCAMPAIGN("SmartCampaign"),
    SMARTLIST("SmartList"),
    SNIPPET("Snippet"),
    STATICLIST("StaticList"),
    TAG("Tag");

    private final String value;

    EntityType(String value) {
      this.value = value;
    }

    public static EntityType fromString(String value) {
      for (EntityType entityType : EntityType.values()) {
        if (entityType.value.equals(value)) {
          return entityType;
        }
      }
      throw new IllegalArgumentException("Unknown entity type type: " + value);
    }
  }
}
