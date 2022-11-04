package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.Configuration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class PublishHostingCommandIT
{

  @Test
  void invoke()
  {
    //given
    String apiKey = "test-project";
    String baseUrl = "http://localhost:8080";
    SimpleLocalizeClient client = SimpleLocalizeClient.create(baseUrl, apiKey);
    Configuration configuration = new Configuration();
    configuration.setEnvironment("production");

    //when
    PublishHostingCommand sut = new PublishHostingCommand(client, configuration);
    sut.invoke();
  }
}
