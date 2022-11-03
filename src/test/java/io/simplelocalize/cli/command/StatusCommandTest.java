package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import org.junit.jupiter.api.Test;

class StatusCommandTest
{
  private static final String BASE_URL = "http://localhost:8080";
  private final String API_KEY = "41e01D5AD0dd6C4Ea215de4cdbb8461cA336Ac0d05688A2E";

  @Test
  public void shouldFetchProjectDetails() throws Exception
  {
    //given
    SimpleLocalizeClient client = SimpleLocalizeClient.create(BASE_URL, API_KEY);

    //when
    StatusCommand sut = new StatusCommand(client);
    sut.invoke();

    //then
  }
}
