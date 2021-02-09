package io.simplelocalize.cli.util;

import io.simplelocalize.cli.client.dto.FileToUpload;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class FileListReaderUtilTest
{


  @Test
  public void shouldFindFilesWithLangInDirectoryName() throws IOException
  {
    //given
    String path = "./junit/lang-in-directory/{lang}/strings.xml";

    //when
    List<FileToUpload> result = FileListReaderUtil.getMatchingFilesToUpload(Paths.get(path), "{lang}");

    //then
    Assertions.assertThat(result).hasSize(2);
    Assertions.assertThat(result).extracting(FileToUpload::getLanguage).containsExactlyInAnyOrder("en", "es");
  }

  @Test
  public void shouldFindFilesWithLangInDirectoryNameWithPrefix() throws IOException
  {
    //given
    String path = "./junit/lang-in-directory-with-prefix/values-{lang}/strings.xml";

    //when
    List<FileToUpload> result = FileListReaderUtil.getMatchingFilesToUpload(Paths.get(path), "{lang}");

    //then
    Assertions.assertThat(result).hasSize(2);
    Assertions.assertThat(result).extracting(FileToUpload::getLanguage).containsExactlyInAnyOrder("en", "es");
  }

  @Test
  public void shouldFindFilesWithLangInFilename() throws IOException
  {
    //given
    String path = "./junit/lang-in-filename/{lang}.json";

    //when
    List<FileToUpload> result = FileListReaderUtil.getMatchingFilesToUpload(Paths.get(path), "{lang}");

    //then
    Assertions.assertThat(result).hasSize(2);
    Assertions.assertThat(result).extracting(FileToUpload::getLanguage).containsExactlyInAnyOrder("en", "es");
  }
}
