package io.simplelocalize.cli.io;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import io.simplelocalize.cli.client.ObjectMapperSingleton;

public class JsonReader
{

  private final MappingProvider mappingConfiguration;
  private final JacksonJsonProvider jsonProvider;

  public JsonReader()
  {
    this.mappingConfiguration = new JacksonMappingProvider(ObjectMapperSingleton.getInstance());
    this.jsonProvider = new JacksonJsonProvider();
  }

  public DocumentContext read(String json)
  {
    Configuration jsonPathConfiguration = Configuration.defaultConfiguration()
            .jsonProvider(this.jsonProvider)
            .mappingProvider(this.mappingConfiguration);
    return JsonPath.parse(json, jsonPathConfiguration);
  }
}
