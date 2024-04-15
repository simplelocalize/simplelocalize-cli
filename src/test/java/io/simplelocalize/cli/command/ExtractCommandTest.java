package io.simplelocalize.cli.command;

import io.simplelocalize.cli.TestResourcesUtility;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class ExtractCommandTest
{

  @Test
  void invoke() throws IOException, InterruptedException
  {
    Path outputFile = Paths.get("extraction.json");
    Files.deleteIfExists(outputFile);
    assert !Files.exists(outputFile);

    //given
    Path path = TestResourcesUtility.read("react-intl-small-subset");
    Configuration configuration = Configuration.defaultConfiguration();
    configuration.setSearchDir(path.toString());
    configuration.setProjectType("yahoo/react-intl");

    //when
    ExtractCommand extractCommand = new ExtractCommand(configuration);
    extractCommand.invoke();

    //then
    Files.exists(outputFile);
    String content = Files.readString(outputFile);
    Assertions.assertTrue(content.contains("DONATION"));
    Assertions.assertTrue(content.contains("FURTHER_WORK"));
  }
}
