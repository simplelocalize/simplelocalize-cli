package io.simplelocalize.cli.extraction;

import io.simplelocalize.cli.extraction.processor.ExtractionProcessor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExtractionProcessorFactoryTest
{

  @Test
  public void shouldCreateProjectProcessorWhenKnownProjectType()
  {
    //given
    ProjectProcessorFactory projectProcessorFactory = new ProjectProcessorFactory();

    //when
    ExtractionProcessor forType = projectProcessorFactory.createForType("yahoo/react-intl");

    //then
    Assertions.assertThat(forType).isNotNull();
  }

  @Test
  public void shouldThrowWhenUnknownProjectType() throws Exception
  {
    //given
    ProjectProcessorFactory projectProcessorFactory = new ProjectProcessorFactory();

    //when
    Assertions.assertThatThrownBy(() -> {
      projectProcessorFactory.createForType("unknown projec type");
    });

    //then
  }
}
