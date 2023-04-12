package io.simplelocalize.cli.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

public class JsonReader
{

  private final ObjectMapper objectMapper;

  public JsonReader()
  {
    this.objectMapper = new ObjectMapper();
  }

  public DocumentContext read(String json)
  {
    Configuration mappingConfiguration = Configuration.defaultConfiguration()
            .jsonProvider(new JacksonJsonProvider())
            .mappingProvider(new JacksonMappingProvider(objectMapper));
    return JsonPath.parse(json, mappingConfiguration);
  }
}
