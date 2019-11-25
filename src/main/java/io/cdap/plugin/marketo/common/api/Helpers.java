package io.cdap.plugin.marketo.common.api;

import com.google.common.base.Strings;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Helpers {
  public static String streamToString(InputStream inputStream) {
    try {
      return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static <T> T streamToObject(InputStream inputStream, Class<T> cls) {
    return Marketo.GSON.fromJson(new InputStreamReader(inputStream), cls);
  }

  public static RuntimeException failForUri(String method, URI uri, Exception ex) {
    String message = ex.getMessage();
    if (Strings.isNullOrEmpty(message)) {
      if (ex.getCause() != null) {
        message = ex.getCause().getMessage();
        if (Strings.isNullOrEmpty(message)) {
          message = "failed to make request";
        }
      }
    }

    URIBuilder uriBuilder = new URIBuilder(uri);
    List<NameValuePair> queryParameters = uriBuilder.getQueryParams();
    queryParameters.removeIf(queryParameter -> queryParameter.getName().equals("access_token"));
    uriBuilder.setParameters(queryParameters);
    try {
      String uriString = uriBuilder.build().toString();
      return new RuntimeException(String.format("Failed %s %s - %s", method, uriString, message));
    } catch (URISyntaxException e) {
      return new RuntimeException(message);
    }
  }
}
