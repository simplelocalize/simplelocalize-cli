package io.simplelocalize.cli.command;

import ch.qos.logback.classic.spi.ILoggingEvent;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import io.simplelocalize.cli.util.TestLogEventFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PublishHostingCommandTest
{

  @InjectMocks
  private PublishHostingCommand sut;

  @Mock
  private SimpleLocalizeClient client;

  @Mock
  private Configuration configuration;


  @Test
  void invokeWhenPassedDefaultEnvironmentKeyWithoutUnderscore() throws Exception
  {
    //given
    List<ILoggingEvent> logEventList = TestLogEventFactory.createAndGetLogEventList(sut.getClass());

    String path = StatusCommandTest.class.getClassLoader().getResource("mock-api-responses/fetch-project.json").getPath();
    String content = Files.readString(Path.of(path), StandardCharsets.UTF_8);
    Mockito.when(client.fetchProject()).thenReturn(content);

    Mockito.when(configuration.getEnvironment()).thenReturn("latest");

    //when
    sut.invoke();

    //then
    Mockito.verify(client).publish("_latest");

    assertThat(logEventList.get(0).getFormattedMessage()).isEqualTo("Project name: My project");
    assertThat(logEventList.get(1).getFormattedMessage()).isEqualTo("Project token: dev-e7c0b7686c7b45fea4450a4c4a83c7ff");
    assertThat(logEventList.get(2).getFormattedMessage()).isEqualTo("Environment: _latest");
    assertThat(logEventList.get(3).getFormattedMessage()).isEqualTo("Publishing translations...");
    assertThat(logEventList.get(4).getFormattedMessage()).isEqualTo("Translations published");
  }

  @Test
  void invoke() throws Exception
  {
    //given
    List<ILoggingEvent> logEventList = TestLogEventFactory.createAndGetLogEventList(sut.getClass());

    String path = StatusCommandTest.class.getClassLoader().getResource("mock-api-responses/fetch-project.json").getPath();
    String content = Files.readString(Path.of(path), StandardCharsets.UTF_8);
    Mockito.when(client.fetchProject()).thenReturn(content);

    Mockito.when(configuration.getEnvironment()).thenReturn("_latest");

    //when
    sut.invoke();

    //then
    Mockito.verify(client).publish("_latest");

    assertThat(logEventList.get(0).getFormattedMessage()).isEqualTo("Project name: My project");
    assertThat(logEventList.get(1).getFormattedMessage()).isEqualTo("Project token: dev-e7c0b7686c7b45fea4450a4c4a83c7ff");
    assertThat(logEventList.get(2).getFormattedMessage()).isEqualTo("Environment: _latest");
    assertThat(logEventList.get(3).getFormattedMessage()).isEqualTo("Publishing translations...");
    assertThat(logEventList.get(4).getFormattedMessage()).isEqualTo("Translations published");
  }
}
