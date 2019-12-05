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

package io.cdap.plugin.marketo.common.api.entities.asset;

/**
 * Form filed entity.
 */
public class FormField {
  String dataType;
  String defaultValue;
  String description;
  String fieldMaskValues;
  Integer fieldWidth;
  String id;
  Boolean initiallyChecked;
  Boolean isLabelToRight;
  Boolean isMultiselect;
  Boolean isRequired;
  Integer labelWidth;
  Integer maxLength;
  String maximumNumber;
  String minimumNumber;
  String picklistValues;
  String placeholderText;
  String validationMessage;
  Integer visibleRows;

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getFieldMaskValues() {
    return fieldMaskValues;
  }

  public void setFieldMaskValues(String fieldMaskValues) {
    this.fieldMaskValues = fieldMaskValues;
  }

  public Integer getFieldWidth() {
    return fieldWidth;
  }

  public void setFieldWidth(Integer fieldWidth) {
    this.fieldWidth = fieldWidth;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Boolean getInitiallyChecked() {
    return initiallyChecked;
  }

  public void setInitiallyChecked(Boolean initiallyChecked) {
    this.initiallyChecked = initiallyChecked;
  }

  public Boolean getLabelToRight() {
    return isLabelToRight;
  }

  public void setLabelToRight(Boolean labelToRight) {
    isLabelToRight = labelToRight;
  }

  public Boolean getMultiselect() {
    return isMultiselect;
  }

  public void setMultiselect(Boolean multiselect) {
    isMultiselect = multiselect;
  }

  public Boolean getRequired() {
    return isRequired;
  }

  public void setRequired(Boolean required) {
    isRequired = required;
  }

  public Integer getLabelWidth() {
    return labelWidth;
  }

  public void setLabelWidth(Integer labelWidth) {
    this.labelWidth = labelWidth;
  }

  public Integer getMaxLength() {
    return maxLength;
  }

  public void setMaxLength(Integer maxLength) {
    this.maxLength = maxLength;
  }

  public String getMaximumNumber() {
    return maximumNumber;
  }

  public void setMaximumNumber(String maximumNumber) {
    this.maximumNumber = maximumNumber;
  }

  public String getMinimumNumber() {
    return minimumNumber;
  }

  public void setMinimumNumber(String minimumNumber) {
    this.minimumNumber = minimumNumber;
  }

  public String getPicklistValues() {
    return picklistValues;
  }

  public void setPicklistValues(String picklistValues) {
    this.picklistValues = picklistValues;
  }

  public String getPlaceholderText() {
    return placeholderText;
  }

  public void setPlaceholderText(String placeholderText) {
    this.placeholderText = placeholderText;
  }

  public String getValidationMessage() {
    return validationMessage;
  }

  public void setValidationMessage(String validationMessage) {
    this.validationMessage = validationMessage;
  }

  public Integer getVisibleRows() {
    return visibleRows;
  }

  public void setVisibleRows(Integer visibleRows) {
    this.visibleRows = visibleRows;
  }
}
