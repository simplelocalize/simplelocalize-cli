package io.simplelocalize.cli.processor;

import io.simplelocalize.cli.processor.files.JavascriptFilesFinder;
import io.simplelocalize.cli.processor.keys.ReactIntlKeyExtractor;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

@RunWith(MockitoJUnitRunner.class)
public class ReactIntlProcessorTest {

  @InjectMocks
  private ReactIntlProcessor processor = new ReactIntlProcessor();

  @Mock
  private JavascriptFilesFinder javascriptFilesFinder = new JavascriptFilesFinder();

  @Mock
  private ReactIntlKeyExtractor reactIntlKeyExtractor = new ReactIntlKeyExtractor();

  @Test
  public void shouldFindFilesToProcess() throws Exception {
    //given
    String path = "fakePath";
    Mockito.when(javascriptFilesFinder.findFilesToProcess(Mockito.any())).thenReturn(List.of());

    //when
    List<Path> filesToProcess = processor.findFilesToProcess(Paths.get(path));

    //then
    Assertions.assertThat(filesToProcess).hasSize(0);
  }

  @Test
  public void shouldExtractKeysFromFile() throws Exception {
    //given
    String path = "fakePath";
    Mockito.when(reactIntlKeyExtractor.extractKeysFromFile(Mockito.any())).thenReturn(Set.of());

    //when
    Set<String> result = processor.extractKeysFromFile(Paths.get(path));

    //then
    Assertions.assertThat(result).hasSize(0);
  }
}
