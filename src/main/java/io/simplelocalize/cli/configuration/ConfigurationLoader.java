package io.simplelocalize.cli.configuration;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public final class ConfigurationLoader
{

  private static final String CURRENT_DIRECTORY = ".";
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

    Configuration configuration;
    try
    {
      InputStream inputStream = new FileInputStream(file);
      configuration = yaml.load(inputStream);
      log.info(" 🗄 Loaded configuration file from: {}", configurationFilePath);
    } catch (Exception e)
    {
      log.error(" 🗄 Unable to load configuration", e);
      return new Configuration();
    }

    String uploadToken = configuration.getUploadToken();

    if (StringUtils.isNotEmpty(uploadToken))
    {
      log.warn("DEPRECATION WARNING: Please use 'apiKey' property instead 'uploadToken' in your configuration file.");
    }

    String apiKey = configuration.getApiKey();
    if (StringUtils.isEmpty(apiKey))
    {
      configuration.setApiKey(uploadToken);
    }

    String searchDir = configuration.getSearchDir();
    if (StringUtils.isEmpty(searchDir))
    {
      configuration.setSearchDir(CURRENT_DIRECTORY);
    }

    return configuration;

  }

}
