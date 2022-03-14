package io.simplelocalize.cli.extraction;

import io.simplelocalize.cli.TestResourcesUtility;
import io.simplelocalize.cli.extraction.processor.YahooReactIntlProcessor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.nio.file.Paths;

@ExtendWith(MockitoExtension.class)
public class YahooReactIntlProcessorTest {

  @InjectMocks
  private final YahooReactIntlProcessor processor = new YahooReactIntlProcessor();

  @Test
  public void shouldThrowWhenWrongPath() throws Exception
  {
    //given
    String path = "fakePath";

    //when & then
    Assertions.assertThatThrownBy(() -> processor.process(Paths.get(path)));
  }

  @Test
  public void shouldFindFilesToProcess() throws Exception {
    //given
    Path path = TestResourcesUtility.read("react-intl");

    //when
    ExtractionResult filesToProcess = processor.process(path);

    //then
    Assertions.assertThat(filesToProcess.getProcessedFiles()).hasSize(5);
    Assertions.assertThat(filesToProcess.getKeys()).hasSize(18);
  }
}
