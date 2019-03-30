package io.simplelocalize.cli;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.util.PropertiesValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

import static io.simplelocalize.cli.ConfigProperty.*;

class ShellCommander {

  private static final String DEFAULT_CONFIG_FILE_NAME = "simplelocalize.properties";
  private Logger log = LoggerFactory.getLogger(ShellCommander.class);

  void run(String[] args) {

    Path configurationFilePath = resolveConfigurationPath(args);

    if (!configurationFilePath.toFile().exists()) {
      log.warn("Could not find configuration file in {}", configurationFilePath);
      return;
    }

    Properties properties;
    try {
      properties = loadProperties(configurationFilePath);
    } catch (Exception e) {
      log.error("Cannot load '" + DEFAULT_CONFIG_FILE_NAME + "' config file. Make sure it's in same directory as *.jar.", e);
      return;
    }

    try {
      invokeWithProperties(properties);
    } catch (InterruptedException e) {
      log.error("Work interrupted", e);
    } catch (IOException e) {
      log.error("Could not read source files", e);
    }
  }

  private void invokeWithProperties(Properties properties) throws IOException, InterruptedException {
    PropertiesValidator.validate(properties);

    String searchDir = properties.getProperty(SEARCH_DIR.getKey());
    Set<String> keys = KeysFinderFacade.invoke(searchDir);

    String clientId = properties.getProperty(CLIENT_ID.getKey());
    String secret = properties.getProperty(CLIENT_SECRET.getKey());
    String projectHash = properties.getProperty(PROJECT_WRITE_TOKEN.getKey());

    SimpleLocalizeClient client = new SimpleLocalizeClient(clientId, secret);
    client.pushKeys(projectHash, keys);
  }

  private Path resolveConfigurationPath(String[] args) {
    if (args.length > 0) {
      return Paths.get(args[0]);
    }
    return Paths.get(DEFAULT_CONFIG_FILE_NAME);
  }

  private Properties loadProperties(Path path) throws IOException {
    Properties properties = new Properties();
    FileInputStream inStream = new FileInputStream(path.toFile());
    properties.load(inStream);
    return properties;
  }
}

