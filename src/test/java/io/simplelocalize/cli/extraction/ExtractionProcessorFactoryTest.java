package io.simplelocalize.cli.extraction;

import io.simplelocalize.cli.extraction.processor.ExtractionProcessor;
import io.simplelocalize.cli.extraction.processor.YahooReactIntlProcessor;
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
    Assertions.assertThat(forType).isInstanceOf(YahooReactIntlProcessor.class);
  }

  @Test
  public void shouldThrowWhenUnknownProjectType() throws Exception
  {
    //given
    ProjectProcessorFactory projectProcessorFactory = new ProjectProcessorFactory();

    //when
    Assertions.assertThatThrownBy(() -> {
      projectProcessorFactory.createForType("unknown project type");
    });

    //then
  }
}
