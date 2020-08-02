package io.simplelocalize.cli.configuration;

import com.google.common.base.Strings;
import io.simplelocalize.cli.exception.ConfigurationNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigurationLoader {

  private static final String CURRENT_DIRECTORY = ".";

  private Logger log = LoggerFactory.getLogger(ConfigurationLoader.class);

  public Configuration load(String path) {

    Path configurationFilePath = Paths.get(path);
    log.info("Using configuration file in path: {}", configurationFilePath);

    File file = configurationFilePath.toFile();
    if (!file.exists()) {
      throw new ConfigurationNotFoundException("Could not find configuration file in: " + path);
    }

    Yaml yaml = new Yaml();
    Configuration configuration;
    try {
      InputStream inputStream = new FileInputStream(file);
      configuration = yaml.load(inputStream);
    } catch (Exception e) {
      throw new ConfigurationNotFoundException("Could not read configuration file in: " + path, e);
    }

    String searchDir = configuration.getSearchDir();
    if (Strings.isNullOrEmpty(searchDir)) {
      configuration.setSearchDir(CURRENT_DIRECTORY);
    }
    return configuration;

  }

}
