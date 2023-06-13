package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class PullHostingCommandIT
{

  @Test
  public void shouldFetchHostingResources() throws Exception
  {
    //given
    String apiKey = "test-project";
    String baseUrl = "http://localhost:8080";
    SimpleLocalizeClient client = SimpleLocalizeClient.create(baseUrl, apiKey);
    Configuration configuration = new Configuration();
    configuration.setPullPath("/Users/jpomykala/Desktop/simplelocalize-hosting");
    configuration.setEnvironment("latest");

    //when
    PullHostingCommand sut = new PullHostingCommand(client, configuration);
    sut.invoke();

    //then
  }
}
