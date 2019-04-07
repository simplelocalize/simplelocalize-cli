package io.simplelocalize.cli.client;

import com.jayway.jsonpath.JsonPath;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class ClientUtils {

  private static Logger log = LoggerFactory.getLogger(ClientUtils.class);

  private ClientUtils() {
  }

  static String tryExtractMessage(HttpResponse response) {
    try {
      String json = parseToJson(response);
      return JsonPath.read(json, "$.data.message");
    } catch (Exception e) {
      return "unknown error";
    }
  }

  static String extractAccessToken(HttpResponse response) {
    String json = parseToJson(response);
    return JsonPath.read(json, "$.access_token");
  }

  static String parseToJson(HttpResponse response) {
    try {
      return IOUtils.toString(response.getEntity().getContent(), "UTF-8");
    } catch (IOException e) {
      log.error("Could not parse JSON from response");
      return "{}";
    }
  }


}
