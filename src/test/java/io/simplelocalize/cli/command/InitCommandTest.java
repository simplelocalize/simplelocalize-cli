package io.simplelocalize.cli.command;

import ch.qos.logback.classic.spi.ILoggingEvent;
import io.simplelocalize.cli.util.TestLogEventFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InitCommandTest
{


  @Test
  void shouldCreateNewConfigurationFile() throws IOException, InterruptedException
  {
    //given
    var fileName = "simplelocalize-init-command.yml";
    var configurationAtPath = Path.of(fileName);
    Files.deleteIfExists(configurationAtPath);
    var initCommand = new InitCommand(fileName);
    Assertions.assertThat(configurationAtPath).doesNotExist();

    List<ILoggingEvent> logEventList = TestLogEventFactory.createAndGetLogEventList(initCommand.getClass());

    //when
    initCommand.invoke();

    //then
    Assertions
            .assertThat(configurationAtPath)
            .exists()
            .isWritable()
            .isReadable()
            .isRegularFile()
            .hasFileName("simplelocalize-init-command.yml")
            .hasParent(Path.of("."));

    assertThat(logEventList.get(0).getFormattedMessage()).isEqualTo("Configuration file created: simplelocalize-init-command.yml");

  }

  @Test
  void shouldNotOverwriteConfigurationFileWhenExists() throws IOException, InterruptedException
  {
    //given
    var fileName = "simplelocalize-init-command.yml";
    var configurationAtPath = Path.of(fileName);
    Files.deleteIfExists(configurationAtPath);
    Files.write(configurationAtPath, "do not overwrite me".getBytes());
    var initCommand = new InitCommand(fileName);

    List<ILoggingEvent> logEventList = TestLogEventFactory.createAndGetLogEventList(initCommand.getClass());

    //when
    initCommand.invoke();

    //then
    Assertions
            .assertThat(configurationAtPath)
            .exists()
            .isWritable()
            .isReadable()
            .isRegularFile()
            .hasFileName("simplelocalize-init-command.yml")
            .hasParent(Path.of("."))
            .hasContent("do not overwrite me");

    assertThat(logEventList.get(0).getFormattedMessage()).isEqualTo("simplelocalize-init-command.yml already exists. Remove or rename it to initialize.");
  }
}
