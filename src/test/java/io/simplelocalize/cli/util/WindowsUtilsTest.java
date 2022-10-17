package io.simplelocalize.cli.util;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class WindowsUtilsTest
{

  private static String originalOsName;

  @BeforeAll
  static void beforeAll()
  {
    originalOsName = System.getProperty("os.name");
  }

  @AfterAll
  static void tearDown()
  {
    System.setProperty("os.name", originalOsName);
  }

  @Test
  void shouldReturnTrueWhenWindows()
  {
    //given
    System.setProperty("os.name", "Windows 10");

    //when
    boolean isWindows = WindowsUtils.isWindows();

    //then
    assertThat(isWindows).isTrue();
  }

  @Test
  void shouldReturnFalseWhenMacOS()
  {
    //given
    System.setProperty("os.name", "MAC OS X");

    //when
    boolean isWindows = WindowsUtils.isWindows();

    //then
    assertThat(isWindows).isFalse();
  }

  @Test
  void convertToWindowsPathWhenWindows()
  {
    //given
    System.setProperty("os.name", "Windows 10");
    String path = "./public/locales/{ns}/{lng}.json";

    //when
    String result = WindowsUtils.convertToWindowsPath(path);

    //then
    assertThat(result).isEqualTo(".\\public\\locales\\{ns}\\{lng}.json");
  }

}
