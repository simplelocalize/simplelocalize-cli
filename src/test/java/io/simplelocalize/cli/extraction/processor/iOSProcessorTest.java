package io.simplelocalize.cli.extraction.processor;

import io.simplelocalize.cli.TestResourcesUtility;
import io.simplelocalize.cli.extraction.ExtractionResult;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

@ExtendWith(MockitoExtension.class)
public class iOSProcessorTest
{

  @InjectMocks
  private final iOSProcessor processor = new iOSProcessor();

  @Test
  public void shouldFindFilesToProcess() throws Exception
  {
    //given
    Path path = TestResourcesUtility.read("ios");

    //when
    ExtractionResult filesToProcess = processor.process(path);

    //then
    Assertions.assertThat(filesToProcess.getProcessedFiles()).hasSize(1);
    Assertions.assertThat(filesToProcess.getKeys()).hasSize(1).contains("bear");
  }
}
