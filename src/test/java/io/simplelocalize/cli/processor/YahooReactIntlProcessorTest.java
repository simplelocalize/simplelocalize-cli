package io.simplelocalize.cli.processor;

import io.simplelocalize.cli.exception.ProjectProcessException;
import io.simplelocalize.cli.processor.files.TestResourcesUtility;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(MockitoJUnitRunner.class)
public class YahooReactIntlProcessorTest {

  @InjectMocks
  private YahooReactIntlProcessor processor = new YahooReactIntlProcessor();

  @Test(expected = ProjectProcessException.class)
  public void shouldThrowWhenWrongPath() throws Exception {
    //given
    String path = "fakePath";

    //when
    ProcessResult filesToProcess = processor.process(Paths.get(path));

    //then
    Assertions.assertThat(filesToProcess.getProcessedFiles()).hasSize(0);
  }

  @Test
  public void shouldFindFilesToProcess() throws Exception {
    //given
    Path path = TestResourcesUtility.read("react-intl");

    //when
    ProcessResult filesToProcess = processor.process(path);

    //then
    Assertions.assertThat(filesToProcess.getProcessedFiles()).hasSize(4);
    Assertions.assertThat(filesToProcess.getKeys()).hasSize(15);
  }
}
