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
import com.google.common.collect.ImmutableList;
import io.cdap.plugin.marketo.common.api.entities.DateRange;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Various helper methods.
 */
public class Helpers {
  public static String streamToString(InputStream inputStream) {
    try {
      return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(String.format("Failed to read stream completely due to '%s'", e.getMessage()));
    }
  }

  public static <T> T streamToObject(InputStream inputStream, Class<T> cls) {
    return Marketo.GSON.fromJson(new InputStreamReader(inputStream), cls);
  }

  public static RuntimeException failForMethodAndUri(String method, URI uri, Exception ex) {
    String message = ex.getMessage();
    if (Strings.isNullOrEmpty(message)) {
      if (ex.getCause() != null) {
        message = ex.getCause().getMessage();
        if (Strings.isNullOrEmpty(message)) {
          message = "Unknown failure";
        }
      }
    }

    URIBuilder uriBuilder = new URIBuilder(uri);
    List<NameValuePair> queryParameters = uriBuilder.getQueryParams();
    queryParameters.removeIf(queryParameter -> queryParameter.getName().equals("access_token"));
    uriBuilder.setParameters(queryParameters);
    try {
      String uriString = uriBuilder.build().toString();
      return new RuntimeException(String.format("Failed '%s' '%s' - '%s'", method, uriString, message));
    } catch (URISyntaxException e) {
      // this will never happen since we rebuilding already validated uri, just make compiler happy
      return new RuntimeException(e);
    }
  }

  /**
   * Splits date range in 30 day ranges.
   * Return date range as is if difference is less or equals to 30 days.
   *
   * @param beginDate
   * @param endDate
   * @return
   */
  public static List<DateRange> getDateRanges(String beginDate, String endDate) {
    OffsetDateTime start = OffsetDateTime.parse(beginDate);
    OffsetDateTime end = OffsetDateTime.parse(endDate);

    if (start.compareTo(end) > 0) {
      throw new IllegalArgumentException("Start date cannot be greater than the end date.");
    }

    int compareResult = start.plusDays(30).compareTo(end);
    if (compareResult >= 0) {
      // we are in range of 30 days, dates are okay
      return ImmutableList.of(new DateRange(start.toString(), end.toString()));
    } else {
      List<DateRange> result = new ArrayList<>();
      OffsetDateTime currentStart = start;

      while (currentStart.compareTo(end) < 0) {
        OffsetDateTime nextEnd = currentStart.plusDays(30);
        result.add(new DateRange(currentStart.toString(),
                                 min(nextEnd.minusSeconds(1), end).toString()));
        currentStart = nextEnd;
      }

      return result;
    }
  }

  public static <T extends Comparable<T>> T min(T o1, T o2) {
    if (o1.compareTo(o2) < 0) {
      return o1;
    } else {
      return o2;
    }
  }
}
