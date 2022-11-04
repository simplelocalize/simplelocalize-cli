package io.simplelocalize.cli.command;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class StatusCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(StatusCommand.class);

  private final SimpleLocalizeClient client;

  public StatusCommand(SimpleLocalizeClient client)
  {
    this.client = client;
  }

  public void invoke()
  {
    try
    {
      String responseData = client.fetchProject();
      DocumentContext json = JsonPath.parse(responseData);
      log.info("Project name: {}", json.read("$.data.name", String.class));
      log.info("Project token: {}", json.read("$.data.projectToken", String.class));
      log.info("Translated: {}", json.read("$.data.translatedPercentage", String.class));
      log.info("Keys: {}", json.read("$.data.keys", String.class));
      log.info("Languages: {}", json.read("$.data.languages[*].key").toString());
      log.info("Namespaces: {}", json.read("$.data.namespaces[*].name").toString());
      log.info("Customers: {}", json.read("$.data.customers[*].key").toString());
      log.info("Last activity: {}", json.read("$.data.lastActivityAt", String.class));
      log.info("Last edit: {}", json.read("$.data.lastEditedAt", String.class));
    } catch (InterruptedException e)
    {
      log.error("Project status could not be fetched", e);
      Thread.currentThread().interrupt();
    } catch (IOException e)
    {
      log.error("Project status could not be fetched", e);
      System.exit(1);
    }
  }

}
