package io.simplelocalize.cli.command;


import com.jayway.jsonpath.JsonPath;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PurgeCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(PurgeCommand.class);

  private final SimpleLocalizeClient client;
  private final Boolean force;


  public PurgeCommand(SimpleLocalizeClient client, Boolean force)
  {
    this.client = client;
    this.force = force;
  }

  @Override
  public void invoke() throws IOException, InterruptedException
  {
    String response = client.fetchProject();
    String projectName = JsonPath.parse(response).read("$.name", String.class);

    boolean isForceUsed = force != null && force;
    if (isForceUsed)
    {
      forcePurgeTranslations(projectName);
    } else
    {
      askUserForConfirmation(projectName);
    }
  }

  private void forcePurgeTranslations(String projectName) throws IOException, InterruptedException
  {
    log.info("Force purging translations for {} project...", projectName);
    client.purgeTranslations();
    log.info("Translations purged");
  }

  private void askUserForConfirmation(String projectName) throws IOException, InterruptedException
  {
    String message = "Are you sure you want to delete all translations for %s project? (yes/no): ".formatted(projectName);
    String userInput = System.console().readLine(message);
    boolean isUserSure = "yes".equalsIgnoreCase(userInput);
    if (isUserSure)
    {
      log.info("Purging translations for {} project...", projectName);
      client.purgeTranslations();
      log.info("Translations purged");
    } else
    {
      log.info("Purge aborted, no changes were made");
    }
  }
}
