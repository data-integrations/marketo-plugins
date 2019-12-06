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

package io.cdap.plugin.marketo.common.api.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Represents marketo token response.
 */
public class MarketoToken {
  @SerializedName("access_token")
  private String accessToken;
  private String scope;
  @SerializedName("expires_in")
  private String expiresIn;
  @SerializedName("token_type")
  private String tokenType;

  public MarketoToken(String accessToken, String scope, String expiresIn, String tokenType) {
    this.accessToken = accessToken;
    this.scope = scope;
    this.expiresIn = expiresIn;
    this.tokenType = tokenType;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public String getScope() {
    return scope;
  }

  public String getExpiresIn() {
    return expiresIn;
  }

  public String getTokenType() {
    return tokenType;
  }
}
