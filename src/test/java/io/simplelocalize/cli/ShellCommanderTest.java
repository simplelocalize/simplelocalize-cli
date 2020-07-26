package io.simplelocalize.cli;

import org.junit.Ignore;
import org.junit.Test;

public class ShellCommanderTest {

  private ShellCommander shellCommander = new ShellCommander();

  @Test
  @Ignore
  public void shouldRun() throws Exception {
    //given
    String resourcePath = ShellCommanderTest.class.getClassLoader().getResource("simplelocalize-it.yml").getPath();

    //when
    shellCommander.run(new String[]{resourcePath});

    //then

  }
}
