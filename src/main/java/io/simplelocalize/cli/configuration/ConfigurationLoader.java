package io.simplelocalize.cli.configuration;

import io.simplelocalize.cli.exception.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public final class ConfigurationLoader
{

  private static final Path DEFAULT_CONFIG_FILE_NAME = Path.of("simplelocalize.yml");

  private final Logger log = LoggerFactory.getLogger(ConfigurationLoader.class);

  public Configuration loadOrGetDefault(Path configurationFilePath)
  {
    ConfigurationLoader configurationLoader = new ConfigurationLoader();

    if (configurationFilePath == null)
    {
      configurationFilePath = DEFAULT_CONFIG_FILE_NAME;
    }

    return configurationLoader.load(configurationFilePath);
  }

  private Configuration load(Path configurationFilePath)
  {
    File file = new File(URLDecoder.decode(String.valueOf(configurationFilePath.toFile()), StandardCharsets.UTF_8));
    Constructor yamlTargetClass = new Constructor(Configuration.class);
    Yaml yaml = new Yaml(yamlTargetClass);

    log.info("Loading configuration file from: {}", configurationFilePath);
    Configuration configuration;
    try
    {
      InputStream inputStream = new FileInputStream(file);
      configuration = yaml.load(inputStream);
      log.info("Configuration file loaded successfully");
    } catch (FileNotFoundException e)
    {
      log.info("Configuration file not present");
      return new Configuration();
    } catch (Exception e)
    {
      log.error("Unable to load configuration: {}", e.getMessage());
      throw new ConfigurationException();
    }
    return configuration;

  }

}
