package io.simplelocalize.cli;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public final class TestResourcesUtility
{

  private TestResourcesUtility()
  {

  }

  public static Path read(String fileOrDirectoryInResourcesDirectory)
  {
    try
    {
      URL resource = TestResourcesUtility.class.getClassLoader().getResource(fileOrDirectoryInResourcesDirectory);
      Objects.requireNonNull(resource);
      return Paths.get(resource.toURI());
    } catch (URISyntaxException e)
    {
      e.printStackTrace();
    }
    return null;
  }
}
