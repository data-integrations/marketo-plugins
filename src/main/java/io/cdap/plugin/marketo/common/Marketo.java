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

package io.cdap.plugin.marketo.common;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import io.cdap.plugin.marketo.common.response.MarketoPage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import javax.net.ssl.HttpsURLConnection;

/**
 * Helper class to perform Marketo Api operations.
 */
public class Marketo {
  /**
   * Marketo page iterator.
   */
  public static class MarketoPageIterator implements Iterator<Map<String, Object>> {
    private MarketoPage currentPage;
    private Marketo marketo;
    private String queryUrl;
    private Iterator<Map<String, Object>> currentPageResultIterator;

    private MarketoPageIterator(MarketoPage page, Marketo marketo, String queryUrl) {
      this.currentPage = page;
      this.marketo = marketo;
      this.queryUrl = queryUrl;
      currentPageResultIterator = currentPage.getResults().iterator();
    }

    @Override
    public boolean hasNext() {
      if (currentPageResultIterator.hasNext()) {
        return true;
      } else {
        MarketoPage nextPage = marketo.getNextPage(currentPage, queryUrl);
        if (nextPage != null) {
          currentPage = nextPage;
          currentPage.getResults().iterator();
          return hasNext();
        } else {
          return false;
        }
      }
    }

    @Override
    public Map<String, Object> next() {
      if (hasNext()) {
        return currentPageResultIterator.next();
      } else {
        throw new NoSuchElementException();
      }
    }
  }

  private static final Gson GSON = new Gson();

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

  public MarketoPage getPage(String queryUrl) {
    return get(queryUrl, MarketoPage.class);
  }

  public MarketoPage getNextPage(MarketoPage page, String queryUrl) {
    if (!Strings.isNullOrEmpty(page.getNextPageToken())) {
      return getPage(queryUrl + "&nextPageToken=" + page.getNextPageToken());
    }
    return null;
  }

  public MarketoPageIterator iteratePage(String queryUrl) {
    return new MarketoPageIterator(getPage(queryUrl), this, queryUrl);
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

  public <T> T get(String queryUrl, Class<T> cls) {
    return doGet(appendToken(marketoEndpoint + queryUrl), cls);
  }

  public static MarketoToken getToken(String marketoEndpoint, String clientId, String clientSecret) {
    String marketoIdURL = marketoEndpoint + "/identity";
    String idEndpoint = marketoIdURL + "/oauth/token?grant_type=client_credentials&client_id="
      + clientId + "&client_secret=" + clientSecret;

    return doGet(idEndpoint, MarketoToken.class);
  }


  private static <T> T doGet(String queryUrl, Class<T> cls) {
    try {
      URL url = new URL(queryUrl);
      HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
      urlConn.setRequestMethod("GET");
      urlConn.setRequestProperty("accept", "application/json");
      int responseCode = urlConn.getResponseCode();
      if (responseCode == 200) {
        InputStream inStream = urlConn.getInputStream();
        Reader reader = new InputStreamReader(inStream);
        return GSON.fromJson(reader, cls);
      } else {
        throw new IOException("Status: " + responseCode);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
