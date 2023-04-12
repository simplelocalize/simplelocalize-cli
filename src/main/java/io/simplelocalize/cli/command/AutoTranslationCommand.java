package io.simplelocalize.cli.command;

import com.jayway.jsonpath.DocumentContext;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.AutoTranslationConfiguration;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.io.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class AutoTranslationCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(AutoTranslationCommand.class);

  private final SimpleLocalizeClient client;
  private final Configuration configuration;
  private final JsonReader jsonReader;

  public AutoTranslationCommand(SimpleLocalizeClient client, Configuration configuration)
  {
    this.configuration = configuration;
    this.client = client;
    this.jsonReader = new JsonReader();
  }

  public void invoke() throws IOException, InterruptedException
  {
    int runningJobsCount = getRunningJobsCount();
    while (runningJobsCount > 0)
    {
      log.info("There is at least one running auto-translation job. Waiting 5 seconds before checking again");
      Thread.sleep(5_000);
      runningJobsCount = getRunningJobsCount();
    }

    List<String> languageKeys = Optional.of(configuration)
            .map(Configuration::getAutoTranslation)
            .map(AutoTranslationConfiguration::getLanguageKeys)
            .orElse(List.of());
    if (languageKeys.isEmpty())
    {
      log.info("Starting auto-translation for all languages");
    } else
    {
      log.info("Starting auto-translation for languages: {}", languageKeys);
    }
    client.startAutoTranslation(languageKeys);
    log.info("Auto-translation started");
    Thread.sleep(5_000);

    runningJobsCount = getRunningJobsCount();
    while (runningJobsCount > 0)
    {
      System.out.print(".");
      Thread.sleep(5_000);
      runningJobsCount = getRunningJobsCount();
    }
    log.info("Auto-translation finished");
  }

  public int getRunningJobsCount() throws IOException, InterruptedException
  {
    String responseData = client.getAutoTranslationJobs();
    DocumentContext json = jsonReader.read(responseData);
    LinkedList<?> runningJobs = json.read("$.data[*]", LinkedList.class);
    return runningJobs.size();
  }

}
