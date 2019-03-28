package io.simplelocalize.cli.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

class ClientBodyBuilders {

  private ClientBodyBuilders() {
  }

  private static ObjectMapper objectMapper = new ObjectMapper();

  static HttpEntity ofKeysBody(Set<String> keys) throws JsonProcessingException {
    Map<String, Set<String>> jsonMap = Maps.newHashMap();
    jsonMap.put("keys", keys);
    String jsonString = objectMapper.writeValueAsString(jsonMap);
    return new StringEntity(jsonString, ContentType.APPLICATION_JSON);
  }

  static HttpEntity ofClientCredentialsGrantType() throws UnsupportedEncodingException {
    return new UrlEncodedFormEntity(
            Collections.singletonList(
                    new BasicNameValuePair("grant_type", "client_credentials")
            )
    );
  }
}
