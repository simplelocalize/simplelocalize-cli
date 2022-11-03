package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.Configuration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class PullHostingCommandTest
{
  private static final String BASE_URL = "http://localhost:8080";
  private final String API_KEY = "test-project";

  @Test
  public void shouldFetchHostingResources() throws Exception
  {
    //given
    SimpleLocalizeClient client = SimpleLocalizeClient.create(BASE_URL, API_KEY);
    Configuration configuration = new Configuration();
    configuration.setPullPath("/Users/jpomykala/Desktop/hosting");
    configuration.setEnvironment("latest");

    //when
    PullHostingCommand sut = new PullHostingCommand(client, configuration);
    sut.invoke();

    //then
  }
}
