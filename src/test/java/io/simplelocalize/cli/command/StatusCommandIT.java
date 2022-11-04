package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
class StatusCommandIT
{

  @Test
  public void shouldFetchProjectDetails() throws Exception
  {
    //given
    String apiKey = "test-project";
    String baseUrl = "http://localhost:8080";
    SimpleLocalizeClient client = SimpleLocalizeClient.create(baseUrl, apiKey);

    //when
    StatusCommand sut = new StatusCommand(client);
    sut.invoke();

  }
}
