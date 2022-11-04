package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PublishHostingCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(PublishHostingCommand.class);

  private final SimpleLocalizeClient client;
  private final Configuration configuration;


  public PublishHostingCommand(SimpleLocalizeClient client, Configuration configuration)
  {
    this.configuration = configuration;
    this.client = client;
  }

  public void invoke()
  {
    String environment = configuration.getEnvironment();
    try
    {
      log.info("Publishing translations to '{}' environment...", environment);
      client.publish(environment);
      log.info("Translations published");
    } catch (InterruptedException e)
    {
      log.error("Publication interrupted", e);
      Thread.currentThread().interrupt();
    } catch (IOException e)
    {
      log.error("Publication failed", e);
      System.exit(1);
    }
  }

}
