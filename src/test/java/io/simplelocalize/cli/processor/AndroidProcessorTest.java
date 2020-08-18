package io.simplelocalize.cli.processor;

import io.simplelocalize.cli.processor.files.TestResourcesUtility;
import io.simplelocalize.cli.processor.processor.AndroidProcessor;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;

@RunWith(MockitoJUnitRunner.class)
public class AndroidProcessorTest {

  @InjectMocks
  private AndroidProcessor processor = new AndroidProcessor();

  @Test
  public void shouldFindFilesToProcess() throws Exception {
    //given
    Path path = TestResourcesUtility.read("android");

    //when
    ProcessResult filesToProcess = processor.process(path);

    //then
    Assertions.assertThat(filesToProcess.getProcessedFiles()).hasSize(3);
    Assertions.assertThat(filesToProcess.getKeys()).hasSize(18);
  }
}
