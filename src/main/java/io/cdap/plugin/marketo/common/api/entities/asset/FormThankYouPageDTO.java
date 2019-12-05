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

import com.google.gson.annotations.SerializedName;
import io.cdap.plugin.marketo.common.api.entities.asset.gen.Entity;

import java.util.Collections;
import java.util.List;

/**
 * Thank you page behaviors for the form.
 */
@Entity
public class FormThankYouPageDTO {
  @SerializedName("default")
  Boolean isDefault;
  String followupType;
  String followupValue;
  String operator;
  String subjectField;
  List<String> values = Collections.emptyList();

  public Boolean getDefault() {
    return isDefault;
  }

  public String getFollowupType() {
    return followupType;
  }

  public String getFollowupValue() {
    return followupValue;
  }

  public String getOperator() {
    return operator;
  }

  public String getSubjectField() {
    return subjectField;
  }

  public List<String> getValues() {
    return values;
  }
}
