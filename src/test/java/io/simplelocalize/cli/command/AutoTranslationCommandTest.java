package io.simplelocalize.cli.command;

import ch.qos.logback.classic.spi.ILoggingEvent;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.proxy.AutoTranslationConfiguration;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import io.simplelocalize.cli.util.TestLogEventFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AutoTranslationCommandTest
{

  @Mock
  private SimpleLocalizeClient client = new SimpleLocalizeClient("https://simplelocalize.io", "my-api-key");

  @Test
  void shouldStartNewAutoTranslationJobsForAllLanguages() throws IOException, InterruptedException
  {
    //given
    Configuration configuration = Configuration.defaultConfiguration();
    Mockito.when(client.getAutoTranslationJobs())
            .thenReturn("""
                    {
                       "msg": "OK",
                       "status": 200,
                       "data": []
                    }
                    """);

    AutoTranslationCommand autoTranslationCommand = new AutoTranslationCommand(client, configuration);

    List<ILoggingEvent> logEventList = TestLogEventFactory.createAndGetLogEventList(autoTranslationCommand.getClass());

    //when
    autoTranslationCommand.invoke();

    //then
    Mockito.verify(client, Mockito.times(2)).getAutoTranslationJobs();
    Mockito.verify(client, Mockito.times(1)).startAutoTranslation(List.of());

    assertThat(logEventList.get(0).getFormattedMessage()).isEqualTo("Starting auto-translation for all languages");
    assertThat(logEventList.get(1).getFormattedMessage()).isEqualTo("Auto-translation started");
    assertThat(logEventList.get(2).getFormattedMessage()).isEqualTo("Auto-translation finished");

  }


  @Test
  void shouldStartNewAutoTranslationJobsForSelectedLanguagesWhenSomeJobsAreAlreadyRunning() throws IOException, InterruptedException
  {
    //given
    Configuration configuration = Configuration.defaultConfiguration();

    AutoTranslationConfiguration autoTranslation = AutoTranslationConfiguration.builder()
            .languageKeys(List.of("en", "pl"))
            .build();
    configuration.setAutoTranslation(autoTranslation);

    Mockito.when(client.getAutoTranslationJobs())
            .thenReturn("""
                    {
                       "msg": "OK",
                       "status": 200,
                       "data": [
                         {
                           "jobId": "sample-job-id-old-running",
                           "progress": 0.56,
                           "state": "RUNNING",
                           "message": "sample-message",
                           "type": "AUTO_TRANSLATION",
                           "started": "2023-06-14T14:31:25.626Z",
                           "metadata": {
                             "additionalProp1": "string",
                             "additionalProp2": "string",
                             "additionalProp3": "string"
                           }
                         }
                       ]
                    }
                    """,
                    """
                    {
                       "msg": "OK",
                       "status": 200,
                       "data": []
                    }
                    """,
                    """
                    {
                       "msg": "OK",
                       "status": 200,
                       "data": [
                         {
                           "jobId": "sample-job-id-newly-created",
                           "progress": 0.01,
                           "state": "RUNNING",
                           "message": "sample-message",
                           "type": "AUTO_TRANSLATION",
                           "started": "2023-06-14T14:31:25.626Z",
                           "metadata": {
                             "additionalProp1": "string",
                             "additionalProp2": "string",
                             "additionalProp3": "string"
                           }
                         }
                       ]
                    }
                    """,
                    """
                    {
                       "msg": "OK",
                       "status": 200,
                       "data": []
                    }
                    """
                    );

    AutoTranslationCommand autoTranslationCommand = new AutoTranslationCommand(client, configuration);

    List<ILoggingEvent> logEventList = TestLogEventFactory.createAndGetLogEventList(autoTranslationCommand.getClass());

    //when
    autoTranslationCommand.invoke();

    //then
    Mockito.verify(client, Mockito.times(4)).getAutoTranslationJobs();
    Mockito.verify(client, Mockito.times(1)).startAutoTranslation(List.of("en", "pl"));

    assertThat(logEventList.get(0).getFormattedMessage()).isEqualTo("Waiting for auto-translation to finish");
    assertThat(logEventList.get(1).getFormattedMessage()).isEqualTo("Starting auto-translation for languages: [en, pl]");
    assertThat(logEventList.get(2).getFormattedMessage()).isEqualTo("Auto-translation started");
    assertThat(logEventList.get(3).getFormattedMessage()).isEqualTo("Auto-translation finished");

  }

  @Test
  void getRunningJobsCount() throws IOException, InterruptedException
  {
    //given
    Configuration configuration = Configuration.defaultConfiguration();

    Mockito.when(client.getAutoTranslationJobs())
            .thenReturn("""
                    {
                       "msg": "OK",
                       "status": 200,
                       "data": [
                         {
                           "jobId": "sample-job-id-1",
                           "progress": 0.56,
                           "state": "RUNNING",
                           "message": "sample-message",
                           "type": "AUTO_TRANSLATION",
                           "started": "2023-06-14T14:31:25.626Z",
                           "metadata": {
                             "additionalProp1": "string",
                             "additionalProp2": "string",
                             "additionalProp3": "string"
                           }
                         },
                         {
                           "jobId": "sample-job-id-2",
                           "progress": 0.70,
                           "state": "RUNNING",
                           "message": "sample-message",
                           "type": "AUTO_TRANSLATION",
                           "started": "2023-06-14T14:31:25.626Z",
                           "metadata": {
                             "additionalProp1": "string",
                             "additionalProp2": "string",
                             "additionalProp3": "string"
                           }
                         }
                       ]
                    }
                    """);

    AutoTranslationCommand autoTranslationCommand = new AutoTranslationCommand(client, configuration);

    //when
    int result = autoTranslationCommand.getRunningJobsCount();

    //then
    Assertions.assertThat(result).isEqualTo(2);

    Mockito.verify(client, Mockito.times(1)).getAutoTranslationJobs();
  }
}
