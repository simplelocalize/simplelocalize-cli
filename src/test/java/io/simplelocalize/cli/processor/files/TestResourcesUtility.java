package io.simplelocalize.cli.processor.files;

import io.simplelocalize.cli.util.FileContentUtil;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public final class TestResourcesUtility {

  private TestResourcesUtility() {

  }

  public static List<String> readFile(String file) {
    Path read = read(file);
    return FileContentUtil.tryReadLines(read);
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
