package io.cdap.plugin.marketo.common.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.cdap.plugin.marketo.common.api.entities.BaseResponse;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.util.function.Function;
import javax.net.ssl.HttpsURLConnection;

/**
 * Helper class with http routines.
 */
class HttpHelper {
  private static final Gson GSON = new Gson();

  static String doGet(String queryUrl) {
    return HttpHelper.doGet(queryUrl, inputStream -> {
      try {
        return IOUtils.toString(inputStream, Charsets.UTF_8);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    });
  }

  static <T> T doGet(String queryUrl, Class<T> cls) {
    return doGet(queryUrl, inputStream -> {
      Reader reader = new InputStreamReader(inputStream);
      return GSON.fromJson(reader, cls);
    });
  }

  static <T> T doGet(String queryUrl, TypeToken<T> pageTypeToken) {
    return doGet(queryUrl, inputStream -> {
      Reader reader = new InputStreamReader(inputStream);
      return GSON.fromJson(reader, pageTypeToken.getType());
    });
  }

  private static <T> T doGet(String queryUrl, Function<InputStream, T> transformer) {
    try {
      URL url = new URL(queryUrl);
      HttpsURLConnection httpConnection = (HttpsURLConnection) url.openConnection();
      httpConnection.setRequestMethod("GET");
      httpConnection.setRequestProperty("accept", "application/json");
      int responseCode = httpConnection.getResponseCode();
      if (responseCode == 200) {
        InputStream inStream = httpConnection.getInputStream();
        return transformer.apply(inStream);
      } else {
        throw new IOException(String.format("Failed to make http request, code: %s, message: %s",
                                            responseCode, getConnectionError(httpConnection)));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static String getConnectionError(HttpsURLConnection connection) throws IOException {
    InputStream inStream = connection.getErrorStream();
    return IOUtils.toString(inStream, Charsets.UTF_8);
  }

  static <T extends BaseResponse, R> T doPost(String queryUrl, R body, Class<R> requestCls,
                                              Class<T> responseCls) {
    try {
      URL url = new URL(queryUrl);
      HttpsURLConnection urlConn = (HttpsURLConnection) url.openConnection();
      urlConn.setRequestMethod("POST");
      urlConn.setRequestProperty("accept", "application/json");

      if (body != null && requestCls != null) {
        urlConn.setDoOutput(true);
        byte[] requestData = GSON.toJson(body, requestCls).getBytes();
        urlConn.setFixedLengthStreamingMode(requestData.length);
        urlConn.setRequestProperty("Content-Type", "application/json");
        urlConn.connect();
        try (OutputStream os = urlConn.getOutputStream()) {
          os.write(requestData);
        }
      } else {
        urlConn.connect();
      }

      int responseCode = urlConn.getResponseCode();

      if (responseCode == 200) {
        InputStream inStream = urlConn.getInputStream();
        Reader reader = new InputStreamReader(inStream);
        return GSON.fromJson(reader, responseCls);
      } else {
        throw new IOException("Status: " + responseCode);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
