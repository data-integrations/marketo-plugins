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

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import io.cdap.plugin.marketo.common.api.entities.BaseResponse;
import io.cdap.plugin.marketo.common.api.entities.Error;
import io.cdap.plugin.marketo.common.api.entities.MarketoToken;
import io.cdap.plugin.marketo.common.api.entities.Warning;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MarketoHttpTest {
  private static final Gson GSON = new Gson();

  @Rule
  public WireMockRule wireMockRule = new WireMockRule(
    WireMockConfiguration.wireMockConfig().dynamicPort()
  );

  public static class StubResponse extends BaseResponse {
    StubResponse(boolean success, List<Error> errors, List<Warning> warnings) {
      setSuccess(success);
      setErrors(errors);
      setWarnings(warnings);
    }
  }

  public static class PageResponse extends BaseResponse {
    private List<String> results;

    PageResponse(boolean moreResults, String nextPageToken, String... items) {
      setSuccess(true);
      setMoreResult(moreResults);
      results = Arrays.asList(items);
      setNextPageToken(nextPageToken);
    }

    public List<String> getResults() {
      return results;
    }
  }

  @Test
  public void testPaging() {
    setupToken();

    WireMock.stubFor(
      WireMock.get(WireMock.urlPathMatching("/rest/v1/paged.json")).inScenario("page")
        .whenScenarioStateIs(Scenario.STARTED)
        .willReturn(
          WireMock.aResponse().withBody(
            GSON.toJson(new PageResponse(true, "page1", "1", "2", "3"))
          )
        )
        .willSetStateTo("page1")
    );
    WireMock.stubFor(
      WireMock.get(WireMock.urlPathMatching("/rest/v1/paged.json")).inScenario("page")
        .whenScenarioStateIs("page1")
        .withQueryParam("nextPageToken", WireMock.equalTo("page1"))
        .willReturn(
          WireMock.aResponse().withBody(
            GSON.toJson(new PageResponse(true, "page2", "4", "5", "6"))
          )
        )
        .willSetStateTo("page2")
    );
    WireMock.stubFor(
      WireMock.get(WireMock.urlPathMatching("/rest/v1/paged.json")).inScenario("page")
        .whenScenarioStateIs("page2")
        .withQueryParam("nextPageToken", WireMock.equalTo("page2"))
        .willReturn(
          WireMock.aResponse().withBody(
            GSON.toJson(new PageResponse(false, null, "7", "8", "9"))
          )
        )
        .willSetStateTo("page3")
    );

    MarketoHttp m = new MarketoHttp(getApiUrl(), "clientNiceId", "clientNiceSecret");

    List<String> results = StreamSupport.stream(Spliterators.spliteratorUnknownSize(
      m.iteratePage("/rest/v1/paged.json", PageResponse.class, PageResponse::getResults),
      Spliterator.ORDERED), false).sorted().collect(Collectors.toList());

    Assert.assertArrayEquals(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9"}, results.toArray());
  }

  @Test
  public void testToken() {
    WireMock.stubFor(
      WireMock.get(WireMock.urlPathMatching("/identity/oauth/token"))
        .withQueryParam("grant_type", WireMock.equalTo("client_credentials"))
        .withQueryParam("client_id", WireMock.equalTo("clientNiceId"))
        .withQueryParam("client_secret", WireMock.equalTo("clientNiceSecret"))
        .willReturn(
          WireMock.aResponse().withBody(
            GSON.toJson(new MarketoToken("niceToken", "hello@world.com", "3600", "bearer")
            )
          )
        )
    );

    MarketoHttp m = new MarketoHttp(getApiUrl(), "clientNiceId", "clientNiceSecret");
    Assert.assertEquals("niceToken", m.getCurrentToken().getAccessToken());
  }

  @Test
  public void testTokenRefresh() {
    setupToken();

    WireMock.stubFor(
      WireMock.get(WireMock.urlPathMatching("/rest/v1/stub.json")).inScenario("retry")
        .whenScenarioStateIs(Scenario.STARTED)
        .willReturn(
          WireMock.aResponse().withBody(
            GSON.toJson(new StubResponse(false,
                                         Collections.singletonList(
                                           new Error(602, "Access token expired")),
                                         Collections.emptyList()))
          )
        )
        .willSetStateTo("refreshed")
    );
    WireMock.stubFor(
      WireMock.get(WireMock.urlPathMatching("/rest/v1/stub.json")).inScenario("retry")
        .whenScenarioStateIs("refreshed")
        .willReturn(
          WireMock.aResponse().withBody(
            GSON.toJson(new StubResponse(true, Collections.emptyList(), Collections.emptyList()))
          )
        )
    );
    MarketoHttp m = new MarketoHttp(getApiUrl(), "clientNiceId", "clientNiceSecret");
    m.validatedGet("/rest/v1/stub.json", Collections.emptyMap(),
                   inputStream -> Helpers.streamToObject(inputStream, StubResponse.class));
    WireMock.verify(WireMock.exactly(2),
                    WireMock.getRequestedFor(WireMock.urlPathEqualTo("/identity/oauth/token")));
    WireMock.verify(WireMock.exactly(2),
                    WireMock.getRequestedFor(WireMock.urlPathEqualTo("/rest/v1/stub.json")));
  }

  @Test
  public void testPost() {
    setupToken();

    WireMock.stubFor(
      WireMock.post(WireMock.urlPathMatching("/rest/v1/post.json"))
        .willReturn(
          WireMock.aResponse().withBody(
            GSON.toJson(new StubResponse(true, Collections.emptyList(), Collections.emptyList()))
          )
        )
    );

    MarketoHttp m = new MarketoHttp(getApiUrl(), "clientNiceId", "clientNiceSecret");
    m.validatedPost("/rest/v1/post.json", Collections.emptyMap(),
                    inputStream -> Helpers.streamToObject(inputStream, StubResponse.class), "body", String::toString);

    WireMock.verify(
      WireMock.postRequestedFor(WireMock.urlPathEqualTo("/rest/v1/post.json"))
        .withRequestBody(WireMock.equalTo("body"))
    );
  }

  @Test
  public void testMessages() {
    setupToken();

    WireMock.stubFor(
      WireMock.get(WireMock.urlPathMatching("/rest/v1/justWarnings.json"))
        .willReturn(
          WireMock.aResponse().withBody(
            GSON.toJson(new StubResponse(true, Collections.emptyList(), Arrays.asList(
              new Warning(700, "Reversed agent 007"),
              new Warning(777, "Result of 1000 - 333")
            ))))
        )
    );

    WireMock.stubFor(
      WireMock.get(WireMock.urlPathMatching("/rest/v1/errors.json"))
        .willReturn(
          WireMock.aResponse().withBody(
            GSON.toJson(new StubResponse(false,
                                         Collections.singletonList(new Error(123, "No way")),
                                         Collections.emptyList())))
        )
    );

    MarketoHttp m = new MarketoHttp(getApiUrl(), "clientNiceId", "clientNiceSecret");
    m.validatedGet("/rest/v1/justWarnings.json", Collections.emptyMap(),
                   inputStream -> Helpers.streamToObject(inputStream, StubResponse.class));

    try {
      m.validatedGet("/rest/v1/errors.json", Collections.emptyMap(),
                     inputStream -> Helpers.streamToObject(inputStream, StubResponse.class));
      Assert.fail("This call expected to fail.");
    } catch (RuntimeException ex) {
      Assert.assertTrue(ex.getMessage().contains("123"));
      Assert.assertTrue(ex.getMessage().contains("No way"));
    }
  }

  @Test
  public void testBuildUri() {
    setupToken();
    MarketoHttp m = new MarketoHttp(getApiUrl(), "clientNiceId", "clientNiceSecret");
    String uriWithToken = m.buildUri("/hello", Collections.emptyMap()).toString();
    Assert.assertTrue(uriWithToken.contains("access_token"));
    String uriWithoutToken = m.buildUri("/hello", ImmutableMap.of("param", "value"), false).toString();
    Assert.assertFalse(uriWithoutToken.contains("access_token"));
    Assert.assertTrue(uriWithoutToken.contains("param=value"));
  }

  @Test
  public void testHttpError() {
    setupToken();

    WireMock.stubFor(
      WireMock.get(WireMock.urlPathMatching("/rest/v1/fail.json"))
        .willReturn(
          WireMock.aResponse().withStatus(500).withBody("GJ server.")
        )
    );

    try {
      MarketoHttp m = new MarketoHttp(getApiUrl(), "clientNiceId", "clientNiceSecret");
      m.validatedGet("/rest/v1/fail.json", Collections.emptyMap(),
                     inputStream -> Helpers.streamToObject(inputStream, StubResponse.class));
      Assert.fail("This call expected to fail.");
    } catch (RuntimeException ex) {
      Assert.assertTrue(ex.getMessage().contains("GJ server"));
    }
  }

  @Test
  public void invalidEndpoint() {
    try {
      new MarketoHttp("%^%^&%^", "clientNiceId", "clientNiceSecret");
      Assert.fail("This call expected to fail.");
    } catch (IllegalArgumentException ex) {
      Assert.assertEquals("'%^%^&%^/identity/oauth/token' is invalid URI", ex.getMessage());
    }
  }

  void setupToken() {
    WireMock.stubFor(
      WireMock.get(WireMock.urlPathMatching("/identity/oauth/token"))
        .willReturn(
          WireMock.aResponse().withBody(
            GSON.toJson(new MarketoToken("niceToken", "hello@world.com", "3600", "bearer")
            )
          )
        )
    );
  }

  String getApiUrl() {
    return String.format("http://localhost:%d", wireMockRule.port());
  }
}
