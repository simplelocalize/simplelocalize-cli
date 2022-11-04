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
  void invoke() throws Exception
  {
    //given
    List<ILoggingEvent> logEventList = TestLogEventFactory.createAndGetLogEventList(sut.getClass());

    Mockito.when(configuration.getEnvironment()).thenReturn("latest");

    //when
    sut.invoke();

    //then
    Mockito.verify(client).publish("latest");

    assertThat(logEventList.get(0).getFormattedMessage()).isEqualTo("Publishing translations to 'latest' environment...");
    assertThat(logEventList.get(1).getFormattedMessage()).isEqualTo("Translations published");
  }
}
