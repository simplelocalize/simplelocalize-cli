package io.simplelocalize.cli.command;

import io.simplelocalize.cli.configuration.Configuration;
import org.junit.Test;

import java.nio.file.Path;

public class UploadCommandTest
{

  private UploadCommand sut = new UploadCommand();

  @Test
  public void shouldInvoke() throws Exception
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("----");
    configuration.setUploadPath(Path.of("./messages_{lang}.properties"));

    //when
    sut.invoke(configuration);

    //then
  }

  @Test
  public void shouldInvokeWithFileName() throws Exception
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("----");
    configuration.setUploadPath(Path.of("messages_{lang}.properties"));

    //when
    sut.invoke(configuration);

    //then
  }
}
