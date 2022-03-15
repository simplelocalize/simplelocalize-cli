package io.simplelocalize.cli.util;

import io.simplelocalize.cli.client.dto.FileToUpload;
import io.simplelocalize.cli.configuration.Configuration;
import io.simplelocalize.cli.io.FileListReader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

class FileListReaderTest
{

  private final FileListReader sut = new FileListReader();


  @Test
  void shouldFindJsonFilesWithInLocaleDirectory() throws IOException
  {
    //given
    String path = "./junit/locale-directory/{lang}/";

    //when
    List<FileToUpload> result = sut.findFilesWithTemplateKey(path);

    //then
    Assertions.assertThat(result).hasSize(4);
    Assertions.assertThat(result).extracting(FileToUpload::getPath)
            .containsExactlyInAnyOrder(
                    Paths.get("./junit/locale-directory/en/common.json"),
                    Paths.get("./junit/locale-directory/en/home.json"),
                    Paths.get("./junit/locale-directory/pl/common.json"),
                    Paths.get("./junit/locale-directory/pl/home.json")
            );
    Assertions.assertThat(result).extracting(FileToUpload::getLanguage)
            .containsExactlyInAnyOrder("en", "en", "pl", "pl");
  }

  @Test
  void shouldFindJsonFilesWithInDirectory() throws IOException
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
  void shouldIgnoreFiles() throws IOException
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
  void shouldFindFilesWithLangInDirectoryName() throws IOException
  {
    //given
    String path = "./junit/lang-in-directory/{lang}/strings.xml";

    //when
    List<FileToUpload> result = sut.findFilesWithTemplateKey(path);

    //then
    Assertions.assertThat(result).hasSize(2);
    Assertions.assertThat(result).extracting(FileToUpload::getPath).containsExactlyInAnyOrder(Paths.get("./junit/lang-in-directory/en/strings.xml"), Paths.get("./junit/lang-in-directory/es/strings.xml"));
    Assertions.assertThat(result).extracting(FileToUpload::getLanguage).containsExactlyInAnyOrder("en", "es");
  }

  @Test
  void shouldFindFilesWithLangInDirectoryNameWithPrefix() throws IOException
  {
    //given
    String path = "./junit/lang-in-directory-with-prefix/values-{lang}/strings.xml";

    //when
    List<FileToUpload> result = sut.findFilesWithTemplateKey(path);

    //then
    Assertions.assertThat(result).hasSize(2);
    Assertions.assertThat(result).extracting(FileToUpload::getPath).containsExactlyInAnyOrder(Paths.get("./junit/lang-in-directory-with-prefix/values-en/strings.xml"), Paths.get("./junit/lang-in-directory-with-prefix/values-es/strings.xml"));
    Assertions.assertThat(result).extracting(FileToUpload::getLanguage).containsExactlyInAnyOrder("en", "es");
  }

  @Test
  void shouldFindFilesWithLangInFilename() throws IOException
  {
    //given
    String path = "./junit/lang-in-filename/{lang}.json";

    //when
    List<FileToUpload> result = sut.findFilesWithTemplateKey(path);

    //then
    Assertions.assertThat(result).hasSize(2);
    Assertions.assertThat(result).extracting(FileToUpload::getPath).containsExactlyInAnyOrder(Paths.get("./junit/lang-in-filename/en.json"), Paths.get("./junit/lang-in-filename/es.json"));
    Assertions.assertThat(result).extracting(FileToUpload::getLanguage).containsExactlyInAnyOrder("en", "es");
  }

  @Test
  void shouldFindFilesWithLangInFilenameAndProperties() throws IOException
  {
    //given
    String path = "./junit/lang-in-filename-suffix/messages_{lang}.properties";

    //when
    List<FileToUpload> result = sut.findFilesWithTemplateKey(path);

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
