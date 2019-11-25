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

package io.cdap.plugin.marketo.common.api;

import com.google.common.base.Strings;
import com.google.gson.reflect.TypeToken;
import io.cdap.plugin.marketo.common.api.entities.BaseResponse;
import io.cdap.plugin.marketo.common.api.entities.MarketoToken;
import io.cdap.plugin.marketo.common.api.entities.leads.LeadsDescribe;
import io.cdap.plugin.marketo.common.api.entities.leads.LeadsExport;
import io.cdap.plugin.marketo.common.api.entities.leads.LeadsExportRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Helper class to perform Marketo Api operations.
 */
public class Marketo {

  private static final Logger LOG = LoggerFactory.getLogger(Marketo.class);
  private static final TypeToken<LeadsDescribe> LEADS_DESCRIBE_TYPE_TOKEN = new TypeToken<LeadsDescribe>() {
  };
  private String marketoEndpoint;
  private MarketoToken token;

  public Marketo(String marketoEndpoint, String clientId, String clientSecret) {
    this.marketoEndpoint = marketoEndpoint;
    token = getToken(marketoEndpoint, clientId, clientSecret);
  }

  public Marketo(String marketoEndpoint, MarketoToken token) {
    this.marketoEndpoint = marketoEndpoint;
    this.token = token;
  }

  public List<LeadsDescribe.LeadAttribute> describeLeads() {
    return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
      iteratePage(Urls.LEADS_DESCRIBE, LEADS_DESCRIBE_TYPE_TOKEN, LeadsDescribe::getResult),
      Spliterator.ORDERED), false).collect(Collectors.toList());
  }

  public LeadsExportJob exportLeads(LeadsExportRequest request) {
    LeadsExport export = post(Urls.BULK_EXPORT_LEADS_CREATE, request, LeadsExportRequest.class, LeadsExport.class);
    return new LeadsExportJob(export, this);
  }

  <T extends BaseResponse> T getPage(String queryUrl, TypeToken<T> pageTypeToken) {
    return get(queryUrl, pageTypeToken);
  }

  <T extends BaseResponse> T getNextPage(T currentPage, String queryUrl, TypeToken<T> pageTypeToken) {
    if (!Strings.isNullOrEmpty(currentPage.getNextPageToken())) {
      return getPage(queryUrl + "&nextPageToken=" + currentPage.getNextPageToken(), pageTypeToken);
    }
    return null;
  }

  <T extends BaseResponse, I> MarketoPageIterator<T, I> iteratePage(String queryUrl,
                                                                    TypeToken<T> pageTypeToken,
                                                                    Function<T, List<I>> resultsGetter) {
    return new MarketoPageIterator<>(getPage(queryUrl, pageTypeToken), this, queryUrl, pageTypeToken, resultsGetter);
  }

  <T extends BaseResponse> T get(String queryUrl, TypeToken<T> pageTypeToken) {
    return HttpHelper.doGet(appendToken(marketoEndpoint + queryUrl), pageTypeToken);
  }

  <T extends BaseResponse> T get(String queryUrl, Class<T> cls) {
    return HttpHelper.doGet(appendToken(marketoEndpoint + queryUrl), cls);
  }

  <T extends BaseResponse, R> T post(String queryUrl, R body, Class<R> requestCls, Class<T> responseCls) {
    return HttpHelper.doPost(appendToken(marketoEndpoint + queryUrl), body, requestCls, responseCls);
  }

  String get(String queryUrl) {
    return HttpHelper.doGet(queryUrl);
  }

  public static MarketoToken getToken(String marketoEndpoint, String clientId, String clientSecret) {
    LOG.info("Requesting marketo token");
    String marketoIdURL = marketoEndpoint + "/identity";
    String idEndpoint = marketoIdURL + "/oauth/token?grant_type=client_credentials&client_id="
      + clientId + "&client_secret=" + clientSecret;

    return HttpHelper.doGet(idEndpoint, MarketoToken.class);
  }

  private String appendToken(String requestUrl) {
    if (!requestUrl.contains("access_token")) {
      if (requestUrl.contains("?")) {
        return requestUrl + "&access_token=" + token.getAccessToken();
      } else {
        return requestUrl + "?access_token=" + token.getAccessToken();
      }
    }
    return requestUrl;
  }

}
