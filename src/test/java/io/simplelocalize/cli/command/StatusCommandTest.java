package io.simplelocalize.cli.command;

import ch.qos.logback.classic.spi.ILoggingEvent;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
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
class StatusCommandTest
{

  @InjectMocks
  private StatusCommand sut;

  @Mock
  private SimpleLocalizeClient client;

  @Test
  public void shouldFetchProjectDetails() throws Exception
  {
    //given
    String path = StatusCommandTest.class.getClassLoader().getResource("mock-api-responses/fetch-project.json").getPath();
    String content = Files.readString(Path.of(path), StandardCharsets.UTF_8);

    Mockito.when(client.fetchProject()).thenReturn(content);

    List<ILoggingEvent> logsList = TestLogEventFactory.createAndGetLogEventList(sut.getClass());

    //when
    sut.invoke();

    //then
    Mockito.verify(client).fetchProject();

    assertThat(logsList.get(0).getFormattedMessage()).isEqualTo("Project name: My project");
    assertThat(logsList.get(1).getFormattedMessage()).isEqualTo("Project token: dev-e7c0b7686c7b45fea4450a4c4a83c7ff");
    assertThat(logsList.get(3).getFormattedMessage()).isEqualTo("Translation keys: 92");
    assertThat(logsList.get(2).getFormattedMessage()).isEqualTo("Translated: 96%");
    assertThat(logsList.get(4).getFormattedMessage()).isEqualTo("Languages: [\"de_DE\",\"en\",\"fr_FR\",\"pl_PL\"]");
    assertThat(logsList.get(6).getFormattedMessage()).isEqualTo("Environments: []");
    assertThat(logsList.get(5).getFormattedMessage()).isEqualTo("Namespaces: []");
    assertThat(logsList.get(6).getFormattedMessage()).isEqualTo("Customers: []");
    assertThat(logsList.get(7).getFormattedMessage()).isEqualTo("Last activity: 2022-11-03T20:32:34");
    assertThat(logsList.get(8).getFormattedMessage()).isEqualTo("Last edit: 2022-10-26T12:27:20");

  }

}
