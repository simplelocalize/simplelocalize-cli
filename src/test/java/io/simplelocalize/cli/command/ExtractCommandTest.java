package io.simplelocalize.cli.command;

import io.simplelocalize.cli.TestResourcesUtility;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.configuration.Configuration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ExtractCommandTest
{

  @Mock
  private SimpleLocalizeClient client = new SimpleLocalizeClient("https://simplelocalize.io", "my-api-key");


  @Test
  void invoke() throws IOException, InterruptedException
  {
    //given
    Path path = TestResourcesUtility.read("react-intl-small-subset");
    Configuration configuration = new Configuration();
    configuration.setApiKey("my-api-key");
    configuration.setSearchDir(path.toString());
    configuration.setProjectType("yahoo/react-intl");

    //when
    ExtractCommand extractCommand = new ExtractCommand(client, configuration);
    extractCommand.invoke();

    //then
    HashSet<String> keys = new HashSet<>();
    keys.addAll(List.of("DONATION", "FURTHER_WORK"));
    Mockito.verify(client, Mockito.times(1)).uploadKeys(keys);
  }
}
