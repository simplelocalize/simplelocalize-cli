package io.simplelocalize.cli.util;

import io.simplelocalize.cli.client.dto.FileToUpload;
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
  void shouldFindMarkdownBlogPostsWithNamespaceAndLanguageKey() throws IOException
  {
    //given
    String path = "./junit/blog-posts-with-lang-name-and-namespace/{lang}/{translationKey}_{ns}.md";

    //when
    List<FileToUpload> result = sut.findFilesToUpload(path);

    //then
    Assertions.assertThat(result)
            .containsExactlyInAnyOrder(
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/blog-posts-with-lang-name-and-namespace/en/ai-translations_common.md"))
                            .withLanguage("en")
                            .withNamespace("common")
                            .withTranslationKey("ai-translations")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/blog-posts-with-lang-name-and-namespace/en/how-to-translate-website_common.md"))
                            .withLanguage("en")
                            .withNamespace("common")
                            .withTranslationKey("how-to-translate-website")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/blog-posts-with-lang-name-and-namespace/pl/ai-translations_common.md"))
                            .withLanguage("pl")
                            .withNamespace("common")
                            .withTranslationKey("ai-translations")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/blog-posts-with-lang-name-and-namespace/pl/how-to-translate-website_common.md"))
                            .withLanguage("pl")
                            .withNamespace("common")
                            .withTranslationKey("how-to-translate-website")
                            .build()
            );
  }

  @Test
  void shouldFindMarkdownBlogPostsWithoutExtension() throws IOException
  {
    //given
    String path = "./junit/blog-posts/{translationKey}.md";

    //when
    List<FileToUpload> result = sut.findFilesToUpload(path);

    //then
    Assertions.assertThat(result)
            .containsExactlyInAnyOrder(
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/blog-posts/ai-translations.md"))
                            .withTranslationKey("ai-translations")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/blog-posts/how-to-translate-website.md"))
                            .withTranslationKey("how-to-translate-website")
                            .build()
            );
  }

  @Test
  void shouldFindMarkdownBlogPostsWithExtension() throws IOException
  {
    //given
    String path = "./junit/blog-posts/{translationKey}";

    //when
    List<FileToUpload> result = sut.findFilesToUpload(path);

    //then
    Assertions.assertThat(result)
            .containsExactlyInAnyOrder(
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/blog-posts/ai-translations.md"))
                            .withTranslationKey("ai-translations.md")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/blog-posts/how-to-translate-website.md"))
                            .withTranslationKey("how-to-translate-website.md")
                            .build()
            );
  }

  @Test
  void shouldFindMarkdownBlogPostsWhenKeyAsDirName() throws IOException
  {
    //given
    String path = "./junit/blog-posts-keys-as-dir-name/{translationKey}/index.md";

    //when
    List<FileToUpload> result = sut.findFilesToUpload(path);

    //then
    Assertions.assertThat(result)
            .containsExactlyInAnyOrder(
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/blog-posts-keys-as-dir-name/ai-translations/index.md"))
                            .withTranslationKey("ai-translations")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/blog-posts-keys-as-dir-name/how-to-translate-website/index.md"))
                            .withTranslationKey("how-to-translate-website")
                            .build()
            );
  }

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
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/locale-directory-namespace-first/common/en.json"))
                            .withLanguage("en")
                            .withNamespace("common")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/locale-directory-namespace-first/home/en.json"))
                            .withLanguage("en")
                            .withNamespace("home")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/locale-directory-namespace-first/common/pl.json"))
                            .withLanguage("pl")
                            .withNamespace("common")
                            .build(),
                    FileToUpload.builder()
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
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/locale-directory/en/common.json"))
                            .withLanguage("en")
                            .withNamespace("common")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/locale-directory/en/home.json"))
                            .withLanguage("en")
                            .withNamespace("home")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/locale-directory/pl/common.json"))
                            .withLanguage("pl")
                            .withNamespace("common")
                            .build(),
                    FileToUpload.builder()
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
            FileToUpload.builder()
                    .withPath(Paths.get("./junit/lang-in-directory/en/strings.xml"))
                    .withLanguage("en")
                    .withNamespace(null)
                    .build(),
            FileToUpload.builder()
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
            FileToUpload.builder()
                    .withPath(Paths.get("./junit/lang-in-directory-with-prefix/values-en/strings.xml"))
                    .withLanguage("en")
                    .withNamespace(null)
                    .build(),
            FileToUpload.builder()
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
            FileToUpload.builder()
                    .withPath(Paths.get("./junit/lang-in-filename/en.json"))
                    .withLanguage("en")
                    .withNamespace(null)
                    .build(),
            FileToUpload.builder()
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
            FileToUpload.builder()
                    .withPath(Paths.get("./junit/lang-in-filename-suffix/messages_de.properties"))
                    .withLanguage("de")
                    .withNamespace(null)
                    .build(),
            FileToUpload.builder()
                    .withPath(Paths.get("./junit/lang-in-filename-suffix/messages_pl-PL.properties"))
                    .withLanguage("pl-PL")
                    .withNamespace(null)
                    .build(),
            FileToUpload.builder()
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
            FileToUpload.builder()
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
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/extreme-edge-cases/meaningless-directory/email_messages_de-DE.properties"))
                            .withLanguage("de-DE")
                            .withNamespace("email_messages")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/extreme-edge-cases/meaningless-directory/email_messages_en%US.properties"))
                            .withLanguage("en%US")
                            .withNamespace("email_messages")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/extreme-edge-cases/meaningless-directory/email_messages_es-ES.properties"))
                            .withLanguage("es-ES")
                            .withNamespace("email_messages")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/extreme-edge-cases/meaningless-directory/home_en-GB.properties"))
                            .withLanguage("en-GB")
                            .withNamespace("home")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/extreme-edge-cases/meaningless-directory/home-en_GB.properties"))
                            .withLanguage("GB")
                            .withNamespace("home-en")
                            .build(),
                    FileToUpload.builder()
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
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/lang-as-directories-ns-as-filename/meaningless-directory/en/common.json"))
                            .withLanguage("en")
                            .withNamespace("common")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/lang-as-directories-ns-as-filename/meaningless-directory/en/home.json"))
                            .withLanguage("en")
                            .withNamespace("home")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/lang-as-directories-ns-as-filename/meaningless-directory/es/home.json"))
                            .withLanguage("es")
                            .withNamespace("home")
                            .build(),
                    FileToUpload.builder()
                            .withPath(Paths.get("./junit/lang-as-directories-ns-as-filename/meaningless-directory/pl/common.json"))
                            .withLanguage("pl")
                            .withNamespace("common")
                            .build()
            );
  }
}
