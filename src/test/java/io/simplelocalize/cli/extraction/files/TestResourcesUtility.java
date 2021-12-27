package io.simplelocalize.cli.extraction.files;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class TestResourcesUtility {

  private TestResourcesUtility() {

  }

  public static Path read(String inResourcesFileOrDirectory) {
    URL resource = TestResourcesUtility.class.getClassLoader().getResource(inResourcesFileOrDirectory);
    try {
      assert resource != null;
      return Paths.get(resource.toURI());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return null;
  }
}
