package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(ValidateCommand.class);

  private final SimpleLocalizeClient client;

  public ValidateCommand(Configuration configuration)
  {
    this.client = SimpleLocalizeClient.withProductionServer(configuration.getApiKey());
  }

  public void invoke()
  {
    try
    {
      int status = client.validateGate();
      System.exit(status);
    } catch (Exception e)
    {
      log.error(" 😝 Project could not be validated. Contact support: contact@simplelocalize.io");
      System.exit(1);
    }
  }
}
