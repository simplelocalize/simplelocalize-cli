package io.simplelocalize.cli.command;

import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.stream.Stream;

public class InitCommand implements CliCommand
{
  private static final Logger log = LoggerFactory.getLogger(InitCommand.class);
  private final String configurationFilename;
  private final Configuration configuration;

  private static final List<String> DIRECTORIES_TO_SKIP = List.of(
          "node_modules",
          "target",
          "Pods",
          "build",
          "dist",
          "out"
  );

  private static final Set<String> TRANSLATION_FILES_EXTENSIONS = Set.of(
          "arb",
          "arsc",
          "cfg",
          "conf",
          "config",
          "csv",
          "dict",
          "ftl",
          "icu",
          "info",
          "ini",
          "js",
          "json",
          "json5",
          "lng",
          "lua",
          "mo",
          "msgl",
          "php",
          "plist",
          "po",
          "pot",
          "properties",
          "rdf",
          "resjson",
          "resw",
          "resx",
          "rrc",
          "sgml",
          "srt",
          "strings",
          "stringsdict",
          "tbx",
          "tjson",
          "tml",
          "tmx",
          "ts",
          "tsv",
          "txt",
          "xlf",
          "xliff",
          "xmb",
          "xml",
          "xsd",
          "xtb",
          "yaml",
          "yml"
  );

  public InitCommand(Configuration configuration, String configurationFilename)
  {
    this.configurationFilename = configurationFilename;
    this.configuration = configuration;
  }

  public void invoke() throws IOException, InterruptedException
  {

    Path simplelocalizeConfigurationFilePath = Path.of(configurationFilename);
    boolean isExists = Files.exists(simplelocalizeConfigurationFilePath);
    boolean isRegularFile = Files.isRegularFile(simplelocalizeConfigurationFilePath);
    if (isExists && isRegularFile)
    {
      log.error("{} already exists. Remove or rename it to initialize.", configurationFilename);
      return;
    }
    fetchConfigurationFile();
  }

  private void runWizard() throws IOException
  {
    log.info("Running wizard...");
    final SimpleLocalizeClient client = SimpleLocalizeClient.create(configuration.getBaseUrl(), configuration.getApiKey());
    try (Stream<Path> allPaths = Files.find(Paths.get("."), Integer.MAX_VALUE, fileMatcher()))
    {
      List<Path> allFiles = allPaths.toList();
      if (allFiles.isEmpty())
      {
        log.error("Wizard didn't find any translation files in the current directory, please configure the configuration file manually according to the documentation.");
        return;
      }
      String operationSystem = System.getProperty("os.name");
    }
  }


  private BiPredicate<Path, BasicFileAttributes> fileMatcher()
  {
    return (filePath, fileAttr) -> {

      if (fileAttr.isRegularFile())
      {
        return false;
      }

      final Path fileName = filePath.getFileName();
      final String path = fileName.toString();
      for (String directory : DIRECTORIES_TO_SKIP)
      {
        if (path.contains(directory + "/") || path.equals(directory + "\\"))
        {
          return false;
        }
      }

      if (path.startsWith("."))
      {
        return false;
      }

      if (fileAttr.size() > 500_000) // 500kb
      {
        return false;
      }

      for (String extension : TRANSLATION_FILES_EXTENSIONS)
      {
        if (!path.endsWith(extension))
        {
          return false;
        }
      }

      return true;
    };
  }

  private void fetchConfigurationFile() throws IOException
  {
    String sampleConfiguration = "https://get.simplelocalize.io/sample-2.7.yml";
    URI u = URI.create(sampleConfiguration);
    try (InputStream inputStream = u.toURL().openStream())
    {
      Files.copy(inputStream, Path.of(configurationFilename), StandardCopyOption.REPLACE_EXISTING);
      log.info("Configuration file created: {}", configurationFilename);
    }
  }


}
