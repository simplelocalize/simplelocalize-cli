package io.simplelocalize.cli.util;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileContentUtilTest
{

  @Test
  public void shouldTryReadLines()
  {
    //given
    String path = FileListReaderUtilTest.class.getClassLoader().getResource("example-test.txt").getPath();

    //when
    List<String> result = FileContentUtil.tryReadLines(Paths.get(path));

    //then
    Assertions.assertThat(result).hasSize(3);
    Assertions.assertThat(result).containsExactly("call", "me", "maybe");
  }

  @Test
  public void shouldReturnEmptyStringWhenFileNotFound()
  {
    //given
    Path given = Paths.get("some path");

    //when
    String result = FileContentUtil.tryReadContent(given);

    //then
    Assertions.assertThat(result).isNotNull();
    Assertions.assertThat(result).isEmpty();
  }

  @Test
  public void shouldReturnEmptyListWhenFileNotFound()
  {
    //given
    Path given = Paths.get("some path");

    //when
    List<String> result = FileContentUtil.tryReadLines(given);

    //then
    Assertions.assertThat(result).isNotNull();
    Assertions.assertThat(result).isEmpty();
  }

  @Test
  public void shouldTryReadContent()
  {
    //given
    String path = FileListReaderUtilTest.class.getClassLoader().getResource("example-test.txt").getPath();

    //when
    String result = FileContentUtil.tryReadContent(Paths.get(path));

    //then
    Assertions.assertThat(result).isNotEmpty();
  }

}
