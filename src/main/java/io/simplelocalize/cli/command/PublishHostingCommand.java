package io.simplelocalize.cli.command;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import io.simplelocalize.cli.configuration.ConfigurationValidatorUtil;
import io.simplelocalize.cli.util.EnvironmentUtils;
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

  public void invoke() throws IOException, InterruptedException
  {
    String configurationEnvironment = configuration.getEnvironment();
    ConfigurationValidatorUtil.validateIsNotEmptyOrNull(configurationEnvironment, "environment");

    String responseData = client.fetchProject();
    DocumentContext json = JsonPath.parse(responseData);

    String projectName = json.read("$.data.name", String.class);
    log.info("Project name: {}", projectName);

    String projectToken = json.read("$.data.projectToken", String.class);
    log.info("Project token: {}", projectToken);

    String environment = EnvironmentUtils.convertDefaultEnvironmentKeyFromPreviousCliVersionsToV3IfNeeded(configurationEnvironment);
    log.info("Environment: {}", environment);

    log.info("Publishing translations...");
    client.publish(environment);
    log.info("Translations published");
  }

}
