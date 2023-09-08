package io.simplelocalize.cli.command;

import com.jayway.jsonpath.JsonPath;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.proxy.AutoTranslationConfiguration;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class AutoTranslationCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(AutoTranslationCommand.class);

  private final SimpleLocalizeClient client;
  private final Configuration configuration;

  public AutoTranslationCommand(SimpleLocalizeClient client, Configuration configuration)
  {
    this.configuration = configuration;
    this.client = client;
  }

  public void invoke() throws IOException, InterruptedException
  {
    int runningJobsCount = getRunningJobsCount();
    while (runningJobsCount > 0)
    {
      log.info("Waiting for auto-translation to finish");
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
    return JsonPath.read(responseData, "$.data.length()");
  }

}
