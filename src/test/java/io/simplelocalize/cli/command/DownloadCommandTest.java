package io.simplelocalize.cli.command;

import io.simplelocalize.cli.configuration.Configuration;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.nio.file.Path;

public class DownloadCommandTest
{

  @Test
  public void shouldDownloadToDirectoryWithPrefix() throws Exception
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("a72ca590eaba1a4e642189d102fe255ca65e14fbbf07dd82892841b9ac5ddbdb");
    configuration.setDownloadPath(Path.of("./download-test/values-{lang}/strings.xml"));
    configuration.setDownloadFormat("android");

    //when
    DownloadCommand downloadCommand = new DownloadCommand();
    downloadCommand.invoke(configuration);

    //then
    Assertions.assertThat(Path.of("./download-test/values-pl/strings.xml")).exists();
    Assertions.assertThat(Path.of("./download-test/values-it/strings.xml")).exists();
    Assertions.assertThat(Path.of("./download-test/values-de/strings.xml")).exists();
  }

  @Test
  public void shouldDownloadToDirectoryWithSuffixAndPrefix() throws Exception
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("a72ca590eaba1a4e642189d102fe255ca65e14fbbf07dd82892841b9ac5ddbdb");
    configuration.setDownloadPath(Path.of("./download-test/values-{lang}-test/strings.xml"));
    configuration.setDownloadFormat("android");

    //when
    DownloadCommand downloadCommand = new DownloadCommand();
    downloadCommand.invoke(configuration);

    //then
    Assertions.assertThat(Path.of("./download-test/values-pl-test/strings.xml")).exists();
    Assertions.assertThat(Path.of("./download-test/values-it-test/strings.xml")).exists();
    Assertions.assertThat(Path.of("./download-test/values-de-test/strings.xml")).exists();
  }

  @Test
  public void shouldDownloadToFileWithLanguageName() throws Exception
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("a72ca590eaba1a4e642189d102fe255ca65e14fbbf07dd82892841b9ac5ddbdb");
    configuration.setDownloadPath(Path.of("./download-test/jsons/{lang}.json"));
    configuration.setDownloadFormat("single-language-json");

    //when
    DownloadCommand downloadCommand = new DownloadCommand();
    downloadCommand.invoke(configuration);

    //then
    Assertions.assertThat(Path.of("./download-test/jsons/de.json")).exists();
    Assertions.assertThat(Path.of("./download-test/jsons/pl.json")).exists();
    Assertions.assertThat(Path.of("./download-test/jsons/it.json")).exists();
  }

  @Test
  public void shouldDownloadToOneFile() throws Exception
  {
    //given
    Configuration configuration = new Configuration();
    configuration.setApiKey("a72ca590eaba1a4e642189d102fe255ca65e14fbbf07dd82892841b9ac5ddbdb");
    configuration.setDownloadPath(Path.of("./download-test/multi-language-file/multi-language-file.json"));
    configuration.setDownloadFormat("multi-language-json");

    //when
    DownloadCommand downloadCommand = new DownloadCommand();
    downloadCommand.invoke(configuration);

    //then
    Assertions.assertThat(Path.of("./download-test/multi-language-file/multi-language-file.json")).exists();
  }
}
