package io.simplelocalize.cli.util;

import io.simplelocalize.cli.TestResourcesUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class ZipUtilsTest
{

  @BeforeEach
  void setUp()
  {
    try
    {
      TestResourcesUtility.read("zip-test/output").toFile().delete();
    } catch (Exception e)
    {

    }

  }

  @Test
  void unzip()
  {
    //given
    String input = TestResourcesUtility.read("zip-test/input/Archive.zip").toString();
    String output = TestResourcesUtility.read("zip-test").toString() + "/output/{fileName}";
    String templateKey = "{fileName}";

    //when
    ZipUtils.unzip(input, output, templateKey);

    //then

    Path fileOne = TestResourcesUtility.read("zip-test/output/file-1.txt");
    assertThat(fileOne).exists().hasContent("my file content 1\n");

    Path fileTwo = TestResourcesUtility.read("zip-test/output/file-2.txt");
    assertThat(fileTwo).exists().hasContent("my file content 2\n");


  }
}
