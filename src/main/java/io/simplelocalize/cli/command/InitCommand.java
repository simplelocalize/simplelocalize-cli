package io.simplelocalize.cli.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class InitCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(InitCommand.class);
  private final String fileName;

  public InitCommand(String fileName)
  {
    this.fileName = fileName;
  }

  public void invoke() throws IOException, InterruptedException
  {
    Path simplelocalizeConfigurationFilePath = Path.of(fileName);
    boolean isExists = Files.exists(simplelocalizeConfigurationFilePath);
    boolean isRegularFile = Files.isRegularFile(simplelocalizeConfigurationFilePath);
    if (isExists && isRegularFile)
    {
      log.info("{} already exists. Remove or rename it to initialize.", fileName);
      return;
    }

    String sampleConfiguration = "https://get.simplelocalize.io/sample.yml";
    URI u = URI.create(sampleConfiguration);
    try (InputStream inputStream = u.toURL().openStream())
    {
      Files.copy(inputStream, simplelocalizeConfigurationFilePath, StandardCopyOption.REPLACE_EXISTING);
      log.info("Configuration file created: {}", fileName);
    }
  }

}
