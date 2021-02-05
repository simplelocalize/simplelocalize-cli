package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(ValidateCommand.class);

  public void invoke(Configuration configuration)
  {
    String apiKey = configuration.getApiKey();
    String profile = configuration.getProfile();
    SimpleLocalizeClient client = new SimpleLocalizeClient(apiKey, profile);

    try
    {
      int status = client.fetchGateCheckStatus();
      System.exit(status);
    } catch (Exception e)
    {
      log.error(" 😝 Project could not be validated. Contact support: contact@simplelocalize.io");
      System.exit(1);
    }
  }
}
