package io.simplelocalize.cli.extraction;

import io.simplelocalize.cli.TestResourcesUtility;
import io.simplelocalize.cli.extraction.processor.AndroidProcessor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

public class AndroidProcessorTest
{

  private final AndroidProcessor processor = new AndroidProcessor();

  @Test
  public void shouldFindFilesToProcess() throws Exception
  {
    //given
    Path path = TestResourcesUtility.read("android");

    //when
    ExtractionResult filesToProcess = processor.process(path);

    //then
    Assertions.assertThat(filesToProcess.getProcessedFiles()).hasSize(3);
    Assertions.assertThat(filesToProcess.getKeys()).hasSize(18);
  }
}
