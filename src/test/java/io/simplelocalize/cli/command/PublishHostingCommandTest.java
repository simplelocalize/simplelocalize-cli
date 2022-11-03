package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.Configuration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class PublishHostingCommandTest
{

  private static final String BASE_URL = "http://localhost:8080";
    private final String API_KEY = "test-project";

  @Test
  void invoke()
  {
    //given
    SimpleLocalizeClient client = SimpleLocalizeClient.create(BASE_URL, API_KEY);
    Configuration configuration = new Configuration();
    configuration.setEnvironment("production");

    //when
    PublishHostingCommand sut = new PublishHostingCommand(client, configuration);
    sut.invoke();

    //then
  }
}
