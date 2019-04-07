package io.simplelocalize.cli.processor;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ProjectProcessorFactoryTest {

  @Test
  public void shouldName() throws Exception {
    //given

    //when
    ProjectProcessor forType = ProjectProcessorFactory.createForType("react-intl");

    //then
    Assertions.assertThat(forType).isNotNull();
  }
}
