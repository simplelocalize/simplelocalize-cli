package io.simplelocalize.cli;

import io.simplelocalize.cli.exception.AccessDeniedException;
import org.junit.Test;

public class ShellCommanderTest {

  private ShellCommander shellCommander = new ShellCommander();

  @Test(expected = AccessDeniedException.class)
  public void shouldRun() throws Exception {
    //given
    String resourcePath = ShellCommanderTest.class.getClassLoader().getResource("simplelocalize.yml").getPath();

    //when
    shellCommander.run(new String[]{resourcePath});

    //then

  }
}
