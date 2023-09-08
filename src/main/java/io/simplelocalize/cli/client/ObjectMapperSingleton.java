package io.simplelocalize.cli.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperSingleton
{

  private static ObjectMapper INSTANCE = null; //NOSONAR

  private ObjectMapperSingleton()
  {
  }

  public static synchronized ObjectMapper getInstance()
  {
    if (INSTANCE == null)
    {
      INSTANCE = new ObjectMapper();
      INSTANCE.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      return INSTANCE;
    }
    return INSTANCE;
  }
}
