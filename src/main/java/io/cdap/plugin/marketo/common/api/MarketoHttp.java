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
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import io.cdap.plugin.marketo.common.api.entities.BaseResponse;
import io.cdap.plugin.marketo.common.api.entities.Error;
import io.cdap.plugin.marketo.common.api.entities.MarketoToken;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Class that encapsulates common http functions for marketo rest api.
 */
class MarketoHttp {
  private static final Logger LOG = LoggerFactory.getLogger(Marketo.class);
  private static final Gson GSON = new Gson();
  private String marketoEndpoint;
  private String clientId;
  private String clientSecret;
  private MarketoToken token;
  private HttpClientContext httpClientContext = HttpClientContext.create();

  MarketoHttp(String marketoEndpoint, String clientId, String clientSecret) {
    this.marketoEndpoint = marketoEndpoint;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    token = refreshToken();
  }

  private <T extends BaseResponse> T getPage(String queryUrl, Class<T> pageClass) {
    return validatedGet(queryUrl, Collections.emptyMap(),
                        inputStream -> Helpers.streamToObject(inputStream, pageClass));
  }

  <T extends BaseResponse> T getNextPage(T currentPage, String queryUrl, Class<T> pageClass) {
    if (!Strings.isNullOrEmpty(currentPage.getNextPageToken())) {
      return validatedGet(queryUrl,
                          ImmutableMap.of("nextPageToken", currentPage.getNextPageToken()),
                          inputStream -> Helpers.streamToObject(inputStream, pageClass));
    }
    return null;
  }

  <T extends BaseResponse, I> MarketoPageIterator<T, I> iteratePage(String queryUrl,
                                                                    Class<T> pageClass,
                                                                    Function<T, List<I>> resultsGetter) {
    return new MarketoPageIterator<>(getPage(queryUrl, pageClass), this, queryUrl, pageClass, resultsGetter);
  }

  <T extends BaseResponse> T validatedGet(String queryUrl, Map<String, String> parameters,
                                          Function<InputStream, T> deserializer) {
    String logUri = "GET " + buildUri(queryUrl, parameters, false).toString();
    return retryableValidate(logUri, () -> {
      URI queryUri = buildUri(queryUrl, parameters, true);
      return get(queryUri, deserializer);
    });
  }

  public <T extends BaseResponse, B> T validatedPost(String queryUrl, Map<String, String> parameters,
                                                     Function<InputStream, T> deserializer,
                                                     B body, Function<B, String> qSerializer) {
    String logUri = "POST " + buildUri(queryUrl, parameters, false).toString();
    return retryableValidate(logUri, () -> {
      URI queryUri = buildUri(queryUrl, parameters, true);
      return post(queryUri, deserializer, body, qSerializer);
    });
  }

  // code: 1029, message: Too many jobs (10) in queue
  private <T extends BaseResponse> T retryableValidate(String logUri, Supplier<T> tryQuery) {
    T result = tryQuery.get();
    // check for expired token
    if (!result.isSuccess()) {
      for (Error error : result.getErrors()) {
        if (error.getCode() == 602 && error.getMessage().equals("Access token expired")) {
          // refresh token and retry
          token = refreshToken();
          LOG.info("Refreshed token");
          return tryQuery.get();
        }
      }
    }

    // log warnings if required
    if (result.getWarnings().size() > 0) {
      String warnings = result.getWarnings().stream()
        .map(error -> String.format("code: %s, message: %s", error.getCode(), error.getMessage()))
        .collect(Collectors.joining("; "));
      LOG.warn("Warnings when calling '{}' - {}", logUri, warnings);
    }

    if (!result.isSuccess()) {
      String msg = String.format("Errors when calling '%s'", logUri);
      // log errors if required
      if (result.getErrors().size() > 0) {
        String errors = result.getErrors().stream()
          .map(error -> String.format("code: %s, message: %s", error.getCode(), error.getMessage()))
          .collect(Collectors.joining("; "));
        msg = msg + " - " + errors;
        LOG.error(msg);
      }
      throw mapErrorsToException(result.getErrors(), msg);
    }
    return result;
  }

  private RuntimeException mapErrorsToException(List<Error> errors, String defaultMessage) {
    if (errors.size() == 1) {
      Error e = errors.get(0);
      String message = e.getMessage();
      if (e.getCode() == 1029 && message != null && message.contains("many jobs")) {
        return new TooManyJobsException();
      } else {
        // this error don't require specific handling
        return new RuntimeException(defaultMessage);
      }
    } else {
      // something outstanding happened and we have more than one error, we can't handle this in specific way
      return new RuntimeException(defaultMessage);
    }
  }

  public <T> T get(URI uri, Function<InputStream, T> deserializer) {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpGet request = new HttpGet(uri);
      try (CloseableHttpResponse response = httpClient.execute(request, httpClientContext)) {
        checkResponseCode(response);
        return deserializer.apply(response.getEntity().getContent());
      }
    } catch (Exception e) {
      throw Helpers.failForMethodAndUri("GET", uri, e);
    }
  }

  private <T, B> T post(URI uri, Function<InputStream, T> respDeserializer, B body, Function<B, String> qSerializer) {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      HttpPost request = new HttpPost(uri);
      if (body != null) {
        Objects.requireNonNull(qSerializer, "body serializer must be specified with body");
        request.setEntity(new StringEntity(qSerializer.apply(body), ContentType.APPLICATION_JSON));
      }
      try (CloseableHttpResponse response = httpClient.execute(request, httpClientContext)) {
        checkResponseCode(response);
        return respDeserializer.apply(response.getEntity().getContent());
      }
    } catch (Exception e) {
      throw Helpers.failForMethodAndUri("POST", uri, e);
    }
  }

  private static void checkResponseCode(CloseableHttpResponse response) throws IOException {
    int statusCode = response.getStatusLine().getStatusCode();
    if (statusCode >= 300) {
      String responseBody = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
      throw new RuntimeException(String.format("Http code '%s', response '%s'", statusCode, responseBody));
    }
  }

  public URI buildUri(String queryUrl, Map<String, String> parameters) {
    return buildUri(queryUrl, parameters, true);
  }

  URI buildUri(String queryUrl, Map<String, String> parameters, boolean includeToken) {
    try {
      URIBuilder builder = new URIBuilder(marketoEndpoint + queryUrl);
      parameters.forEach(builder::setParameter);
      if (includeToken) {
        builder.setParameter("access_token", token.getAccessToken());
      }
      return builder.build();
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(String.format("'%s' is invalid URI", marketoEndpoint + queryUrl));
    }
  }

  MarketoToken getCurrentToken() {
    return this.token;
  }

  private MarketoToken refreshToken() {
    LOG.debug("Requesting marketo token");
    URI getTokenUri = buildUri("/identity/oauth/token",
                               ImmutableMap.of("grant_type", "client_credentials", "client_id", clientId,
                                               "client_secret", clientSecret), false);
    return get(getTokenUri, inputStream -> GSON.fromJson(new InputStreamReader(inputStream), MarketoToken.class));
  }
}
