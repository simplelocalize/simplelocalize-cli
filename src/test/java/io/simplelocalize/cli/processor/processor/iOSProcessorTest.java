package io.simplelocalize.cli.processor.processor;

import io.simplelocalize.cli.processor.ProcessResult;
import io.simplelocalize.cli.processor.files.TestResourcesUtility;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;

@RunWith(MockitoJUnitRunner.class)
public class iOSProcessorTest
{

  @InjectMocks
  private iOSProcessor processor = new iOSProcessor();

  @Test
  public void shouldFindFilesToProcess() throws Exception
  {
    //given
    Path path = TestResourcesUtility.read("ios");

    //when
    ProcessResult filesToProcess = processor.process(path);

    //then
    Assertions.assertThat(filesToProcess.getProcessedFiles()).hasSize(1);
    Assertions.assertThat(filesToProcess.getKeys()).hasSize(1).contains("bear");
  }
}
