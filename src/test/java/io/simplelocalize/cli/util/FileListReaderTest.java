package io.simplelocalize.cli.util;

import io.simplelocalize.cli.client.dto.FileToUpload;
import io.simplelocalize.cli.io.FileListReader;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static io.simplelocalize.cli.client.dto.FileToUpload.FileToUploadBuilder.aFileToUpload;

class FileListReaderTest
{

  private final FileListReader sut = new FileListReader();


  @Test
  void shouldFindJsonFilesWithInLocaleDirectoryWhenNamespaceFirst() throws IOException
  {
    //given
    String path = "./junit/locale-directory-namespace-first/{ns}/{lang}.json";

    //when
    List<FileToUpload> result = sut.findFilesToUpload(path);

    //then
    Assertions.assertThat(result)
            .containsExactlyInAnyOrder(
                    aFileToUpload()
                            .withPath(Paths.get("./junit/locale-directory-namespace-first/common/en.json"))
                            .withLanguage("en")
                            .withNamespace("common")
                            .build(),
                    aFileToUpload()
                            .withPath(Paths.get("./junit/locale-directory-namespace-first/home/en.json"))
                            .withLanguage("en")
                            .withNamespace("home")
                            .build(),
                    aFileToUpload()
                            .withPath(Paths.get("./junit/locale-directory-namespace-first/common/pl.json"))
                            .withLanguage("pl")
                            .withNamespace("common")
                            .build(),
                    aFileToUpload()
                            .withPath(Paths.get("./junit/locale-directory-namespace-first/home/pl.json"))
                            .withLanguage("pl")
                            .withNamespace("home")
                            .build()
            );
  }

  @Test
  void shouldFindJsonFilesWithInLocaleDirectory() throws IOException
  {
    //given
    String path = "./junit/locale-directory/{lang}/{ns}.json";

    //when
    List<FileToUpload> result = sut.findFilesToUpload(path);

    //then
    Assertions.assertThat(result)
            .containsExactlyInAnyOrder(
                    aFileToUpload()
                            .withPath(Paths.get("./junit/locale-directory/en/common.json"))
                            .withLanguage("en")
                            .withNamespace("common")
                            .build(),
                    aFileToUpload()
                            .withPath(Paths.get("./junit/locale-directory/en/home.json"))
                            .withLanguage("en")
                            .withNamespace("home")
                            .build(),
                    aFileToUpload()
                            .withPath(Paths.get("./junit/locale-directory/pl/common.json"))
                            .withLanguage("pl")
                            .withNamespace("common")
                            .build(),
                    aFileToUpload()
                            .withPath(Paths.get("./junit/locale-directory/pl/home.json"))
                            .withLanguage("pl")
                            .withNamespace("home")
                            .build()
            );
  }

  @Test
  void shouldFindFilesWithLangInDirectoryName() throws IOException
  {
    //given
    String path = "./junit/lang-in-directory/{lang}/strings.xml";

    //when
    List<FileToUpload> result = sut.findFilesToUpload(path);

    //then
    Assertions.assertThat(result).containsExactlyInAnyOrder(
            aFileToUpload()
                    .withPath(Paths.get("./junit/lang-in-directory/en/strings.xml"))
                    .withLanguage("en")
                    .withNamespace(null)
                    .build(),
            aFileToUpload()
                    .withPath(Paths.get("./junit/lang-in-directory/es/strings.xml"))
                    .withLanguage("es")
                    .withNamespace(null)
                    .build()
    );
  }

  @Test
  void shouldFindFilesWithLangInDirectoryNameWithPrefix() throws IOException
  {
    //given
    String path = "./junit/lang-in-directory-with-prefix/values-{lang}/strings.xml";

    //when
    List<FileToUpload> result = sut.findFilesToUpload(path);

    //then
    Assertions.assertThat(result).containsExactlyInAnyOrder(
            aFileToUpload()
                    .withPath(Paths.get("./junit/lang-in-directory-with-prefix/values-en/strings.xml"))
                    .withLanguage("en")
                    .withNamespace(null)
                    .build(),
            aFileToUpload()
                    .withPath(Paths.get("./junit/lang-in-directory-with-prefix/values-es/strings.xml"))
                    .withLanguage("es")
                    .withNamespace(null)
                    .build()
    );
  }

  @Test
  void shouldFindFilesWithLangInFilename() throws IOException
  {
    //given
    String path = "./junit/lang-in-filename/{lang}.json";

    //when
    List<FileToUpload> result = sut.findFilesToUpload(path);

    //then
    Assertions.assertThat(result).containsExactlyInAnyOrder(
            aFileToUpload()
                    .withPath(Paths.get("./junit/lang-in-filename/en.json"))
                    .withLanguage("en")
                    .withNamespace(null)
                    .build(),
            aFileToUpload()
                    .withPath(Paths.get("./junit/lang-in-filename/es.json"))
                    .withLanguage("es")
                    .withNamespace(null)
                    .build()
    );
  }

  @Test
  void shouldFindFilesWithLangInFilenameAndProperties() throws IOException
  {
    //given
    String path = "./junit/lang-in-filename-suffix/messages_{lang}.properties";

    //when
    List<FileToUpload> result = sut.findFilesToUpload(path);

    //then
    Assertions.assertThat(result).containsExactlyInAnyOrder(
            aFileToUpload()
                    .withPath(Paths.get("./junit/lang-in-filename-suffix/messages_de.properties"))
                    .withLanguage("de")
                    .withNamespace(null)
                    .build(),
            aFileToUpload()
                    .withPath(Paths.get("./junit/lang-in-filename-suffix/messages_pl-PL.properties"))
                    .withLanguage("pl-PL")
                    .withNamespace(null)
                    .build(),
            aFileToUpload()
                    .withPath(Paths.get("./junit/lang-in-filename-suffix/messages_pl.properties"))
                    .withLanguage("pl")
                    .withNamespace(null)
                    .build()
    );
  }

  @Test
  void shouldFindOneWithoutTemplateKeys() throws IOException
  {
    //given
    String path = "./junit/lang-in-filename-suffix/messages_pl.properties";

    //when
    List<FileToUpload> result = sut.findFilesToUpload(path);

    //then
    Assertions.assertThat(result).containsExactlyInAnyOrder(
            aFileToUpload()
                    .withPath(Paths.get("./junit/lang-in-filename-suffix/messages_pl.properties"))
                    .withLanguage(null)
                    .withNamespace(null)
                    .build()
    );
  }

  @Test
  void shouldFindTranslationsWhenExtremeEdgeCases() throws IOException
  {
    //given
    String path = "./junit/extreme-edge-cases/meaningless-directory/{ns}_{lang}.properties";

    //when
    List<FileToUpload> result = sut.findFilesToUpload(path);

    //then

    Assertions
            .assertThat(result)
            .containsExactlyInAnyOrder(
                    aFileToUpload()
                            .withPath(Paths.get("./junit/extreme-edge-cases/meaningless-directory/email_messages_de-DE.properties"))
                            .withLanguage("de-DE")
                            .withNamespace("email_messages")
                            .build(),
                    aFileToUpload()
                            .withPath(Paths.get("./junit/extreme-edge-cases/meaningless-directory/email_messages_en%US.properties"))
                            .withLanguage("en%US")
                            .withNamespace("email_messages")
                            .build(),
                    aFileToUpload()
                            .withPath(Paths.get("./junit/extreme-edge-cases/meaningless-directory/email_messages_es-ES.properties"))
                            .withLanguage("es-ES")
                            .withNamespace("email_messages")
                            .build(),
                    aFileToUpload()
                            .withPath(Paths.get("./junit/extreme-edge-cases/meaningless-directory/home_en-GB.properties"))
                            .withLanguage("en-GB")
                            .withNamespace("home")
                            .build(),
                    aFileToUpload()
                            .withPath(Paths.get("./junit/extreme-edge-cases/meaningless-directory/home-en_GB.properties"))
                            .withLanguage("GB")
                            .withNamespace("home-en")
                            .build(),
                    aFileToUpload()
                            .withPath(Paths.get("./junit/extreme-edge-cases/meaningless-directory/home_pl-PL.properties"))
                            .withLanguage("pl-PL")
                            .withNamespace("home")
                            .build()
            );
  }

  @Test
  void langAsDirectoriesNsAsFilename() throws IOException
  {
    //given
    String path = "./junit/lang-as-directories-ns-as-filename/meaningless-directory/{lang}/{ns}.json";

    //when
    List<FileToUpload> result = sut.findFilesToUpload(path);

    //then
    Assertions.assertThat(result).hasSize(4)
            .containsExactlyInAnyOrder(
                    aFileToUpload()
                            .withPath(Paths.get("./junit/lang-as-directories-ns-as-filename/meaningless-directory/en/common.json"))
                            .withLanguage("en")
                            .withNamespace("common")
                            .build(),
                    aFileToUpload()
                            .withPath(Paths.get("./junit/lang-as-directories-ns-as-filename/meaningless-directory/en/home.json"))
                            .withLanguage("en")
                            .withNamespace("home")
                            .build(),
                    aFileToUpload()
                            .withPath(Paths.get("./junit/lang-as-directories-ns-as-filename/meaningless-directory/es/home.json"))
                            .withLanguage("es")
                            .withNamespace("home")
                            .build(),
                    aFileToUpload()
                            .withPath(Paths.get("./junit/lang-as-directories-ns-as-filename/meaningless-directory/pl/common.json"))
                            .withLanguage("pl")
                            .withNamespace("common")
                            .build()
            );
  }
}
