package io.simplelocalize.cli.command;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;


public class StatusCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(StatusCommand.class);

  private final SimpleLocalizeClient client;

  public StatusCommand(SimpleLocalizeClient client)
  {
    this.client = client;
  }

  public void invoke() throws IOException, InterruptedException
  {
    String response = client.fetchProject();
    DocumentContext json = JsonPath.parse(response);
    log.info("Project name: {}", json.read("$.data.name", String.class));
    log.info("Project token: {}", json.read("$.data.projectToken", String.class));
    log.info("Translation keys: {}", json.read("$.data.keys", String.class));
    Float translatedNullable = json.read("$.data.translatedPercentage", Float.class);
    Float translated = Objects.requireNonNullElse(translatedNullable, 0.0f);
    Float translatedPercentage = translated * 100;
    String translatedPercentageFormatted = String.format("%.0f", translatedPercentage);
    log.info("Translated: {}%", translatedPercentageFormatted);
    log.info("Languages: {}", json.read("$.data.languages[*].key").toString());
    log.info("Environments: {}", json.read("$.data.environments[*].key").toString());
    log.info("Namespaces: {}", json.read("$.data.namespaces[*].name").toString());
    log.info("Customers: {}", json.read("$.data.customers[*].key").toString());
    log.info("Last activity: {}", json.read("$.data.lastActivityAt", String.class));
    log.info("Last edit: {}", json.read("$.data.lastEditedAt", String.class));
  }

}
