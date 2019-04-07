package io.simplelocalize.cli.util;

import io.simplelocalize.cli.processor.ProjectProcessor;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.Set;

public class ReflectionLoaderTest {

  @Test
  public void shouldLoadProcessors() throws Exception {
    //given

    //when
    Set<ProjectProcessor> processors = ReflectionLoader.loadProcessors();

    //then
    Assertions.assertThat(processors).isNotNull();
    Assertions.assertThat(processors.size()).isGreaterThan(0);
  }
}
