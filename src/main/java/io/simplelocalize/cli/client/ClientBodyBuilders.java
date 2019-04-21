package io.simplelocalize.cli.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

class ClientBodyBuilders {

  private static ObjectMapper objectMapper = new ObjectMapper();

  private ClientBodyBuilders() {
  }

  static HttpRequest.BodyPublisher ofKeysBody(Set<String> keys) throws JsonProcessingException {
    Map<String, Set<String>> jsonMap = Maps.newHashMap();
    jsonMap.put("keys", keys);
    String jsonString = objectMapper.writeValueAsString(jsonMap);
    return HttpRequest.BodyPublishers.ofString(jsonString);
  }

  static HttpRequest.BodyPublisher ofClientCredentialsGrantType() {
    String encode = URLEncoder.encode("grant_type=client_credentials", StandardCharsets.UTF_8);
    return HttpRequest.BodyPublishers.ofString(encode);
  }
}
