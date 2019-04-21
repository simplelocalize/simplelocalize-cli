package io.simplelocalize.cli.processor;

import io.simplelocalize.cli.exception.NoProcessorMatchException;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ProjectProcessorFactoryTest {

  @Test
  public void shouldCreateProjectProcessorWhenKnownProjectType() {
    //given

    //when
    ProjectProcessor forType = ProjectProcessorFactory.createForType("yahoo/react-intl");

    //then
    Assertions.assertThat(forType).isNotNull();
  }

  @Test(expected = NoProcessorMatchException.class)
  public void shouldThrowWhenUnknownProjectType() throws Exception {
    //given

    //when
    ProjectProcessor forType = ProjectProcessorFactory.createForType("unknown projec type");

    //then
  }
}
