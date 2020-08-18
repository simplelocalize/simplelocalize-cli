package io.simplelocalize.cli.processor.keys;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Set;

public class EjsKeyExtractorTest {

  private EjsKeyExtractor keyExtractor = new EjsKeyExtractor();

  @Test
  public void shouldExtractKeysFromLines() throws Exception {
    //given
    ClassLoader classLoader = this.getClass().getClassLoader();
    URL resource = classLoader.getResource("ejs/confirm-page.ejs");
    String path = resource.getPath();


    //when
    Set<String> keys = keyExtractor.extractKeysFromFile(Paths.get(path));

    //then
    Assertions.assertThat(keys).hasSize(5);
    Assertions.assertThat(keys).containsExactlyInAnyOrder(
            "confirm.title",
            "common.email",
            "common.signIn",
            "confirm.sendAgain",
            "common.recoverPassword"
    );
  }
}
