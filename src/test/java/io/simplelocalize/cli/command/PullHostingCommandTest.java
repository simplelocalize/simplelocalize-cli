package io.simplelocalize.cli.command;

import ch.qos.logback.classic.spi.ILoggingEvent;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.Configuration;
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
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PullHostingCommandTest
{

  @InjectMocks
  private PullHostingCommand sut;

  @Mock
  private SimpleLocalizeClient client;

  @Mock
  private Configuration configuration;

  @Test
  void invoke() throws Exception
  {
    //given
    String path = StatusCommandTest.class.getClassLoader().getResource("mock-api-responses/fetch-project.json").getPath();
    String content = Files.readString(Path.of(path), StandardCharsets.UTF_8);
    Mockito.when(client.fetchProject()).thenReturn(content);

    Mockito.when(configuration.getEnvironment()).thenReturn("latest");
    Mockito.when(configuration.getPullPath()).thenReturn("/home/wojtek/simplelocalize");

    List<ILoggingEvent> logEventList = TestLogEventFactory.createAndGetLogEventList(sut.getClass());

    //when
    sut.invoke();

    //then
    Mockito.verify(client, times(6)).downloadFile(Mockito.anyString(), Mockito.any(Path.class));
    assertThat(logEventList.get(0).getFormattedMessage()).isEqualTo("Project name: My project");
    assertThat(logEventList.get(1).getFormattedMessage()).isEqualTo("Project token: dev-e7c0b7686c7b45fea4450a4c4a83c7ff");
    assertThat(logEventList.get(2).getFormattedMessage()).isEqualTo("Environment: latest");
    assertThat(logEventList.get(3).getFormattedMessage()).isEqualTo("Found 6 Translation Hosting resources");
    assertThat(logEventList.get(4).getFormattedMessage()).isEqualTo("Downloaded 6 Translation Hosting resources");
  }
}
