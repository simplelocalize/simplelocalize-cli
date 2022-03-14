package io.simplelocalize.cli.util;

import io.simplelocalize.cli.client.dto.FileToUpload;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.io.FileListReader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class FileListReaderTest
{

  private final FileListReader sut = new FileListReader();

  @Test
  public void shouldFindJsonFilesWithInDirectory() throws IOException
  {
    //given
    String path = "./junit/multi-file";

    Configuration configuration = new Configuration();
    configuration.setUploadFormat("multi-language-json");
    configuration.setIgnorePaths(List.of("./junit/*/ignore-me/*"));
    configuration.setUploadPath(path);

    //when
    List<FileToUpload> result = sut.findFilesForMultiFileUpload(configuration);

    //then
    Assertions.assertThat(result).hasSize(2);
    Assertions.assertThat(result).extracting(FileToUpload::getPath).containsExactlyInAnyOrder(Paths.get("./junit/multi-file/about-page/componentY.json"), Paths.get("./junit/multi-file/welcome-page/componentX.json"));
    Assertions.assertThat(result).extracting(FileToUpload::getLanguage).containsExactlyInAnyOrder(null, null);
  }

  @Test
  public void shouldIgnoreFiles() throws IOException
  {
    //given
    String path = "./junit/multi-file/ignore-me";

    Configuration configuration = new Configuration();
    configuration.setUploadFormat("multi-language-json");
    configuration.setIgnorePaths(List.of("./junit/multi-file/ignore-me/*-second.json"));
    configuration.setUploadPath(path);

    //when
    List<FileToUpload> result = sut.findFilesForMultiFileUpload(configuration);

    //then
    Assertions.assertThat(result).hasSize(1);
    Assertions.assertThat(result).extracting(FileToUpload::getPath).containsExactlyInAnyOrder(Paths.get("./junit/multi-file/ignore-me/ignore-me.json"));
  }

  @Test
  public void shouldFindFilesWithLangInDirectoryName() throws IOException
  {
    //given
    String path = "./junit/lang-in-directory/{lang}/strings.xml";

    //when
    List<FileToUpload> result = sut.findFilesWithTemplateKey(path, "{lang}");

    //then
    Assertions.assertThat(result).hasSize(2);
    Assertions.assertThat(result).extracting(FileToUpload::getPath).containsExactlyInAnyOrder(Paths.get("./junit/lang-in-directory/en/strings.xml"), Paths.get("./junit/lang-in-directory/es/strings.xml"));
    Assertions.assertThat(result).extracting(FileToUpload::getLanguage).containsExactlyInAnyOrder("en", "es");
  }

  @Test
  public void shouldFindFilesWithLangInDirectoryNameWithPrefix() throws IOException
  {
    //given
    String path = "./junit/lang-in-directory-with-prefix/values-{lang}/strings.xml";

    //when
    List<FileToUpload> result = sut.findFilesWithTemplateKey(path, "{lang}");

    //then
    Assertions.assertThat(result).hasSize(2);
    Assertions.assertThat(result).extracting(FileToUpload::getPath).containsExactlyInAnyOrder(Paths.get("./junit/lang-in-directory-with-prefix/values-en/strings.xml"), Paths.get("./junit/lang-in-directory-with-prefix/values-es/strings.xml"));
    Assertions.assertThat(result).extracting(FileToUpload::getLanguage).containsExactlyInAnyOrder("en", "es");
  }

  @Test
  public void shouldFindFilesWithLangInFilename() throws IOException
  {
    //given
    String path = "./junit/lang-in-filename/{lang}.json";

    //when
    List<FileToUpload> result = sut.findFilesWithTemplateKey(path, "{lang}");

    //then
    Assertions.assertThat(result).hasSize(2);
    Assertions.assertThat(result).extracting(FileToUpload::getPath).containsExactlyInAnyOrder(Paths.get("./junit/lang-in-filename/en.json"), Paths.get("./junit/lang-in-filename/es.json"));
    Assertions.assertThat(result).extracting(FileToUpload::getLanguage).containsExactlyInAnyOrder("en", "es");
  }

  @Test
  public void shouldFindFilesWithLangInFilenameAndProperties() throws IOException
  {
    //given
    String path = "./junit/lang-in-filename-suffix/messages_{lang}.properties";

    //when
    List<FileToUpload> result = sut.findFilesWithTemplateKey(path, "{lang}");

    //then
    Assertions.assertThat(result).hasSize(3);
    Assertions.assertThat(result).extracting(FileToUpload::getPath).containsExactlyInAnyOrder(
            Paths.get("./junit/lang-in-filename-suffix/messages_de.properties"),
            Paths.get("./junit/lang-in-filename-suffix/messages_pl-PL.properties"),
            Paths.get("./junit/lang-in-filename-suffix/messages_pl.properties")
    );
    Assertions.assertThat(result).extracting(FileToUpload::getLanguage).containsExactlyInAnyOrder("pl", "pl-PL", "de");
  }
}
