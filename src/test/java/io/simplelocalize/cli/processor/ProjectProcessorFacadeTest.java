package io.simplelocalize.cli.processor;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ProjectProcessorFacadeTest {

  private ProjectProcessorFacade projectProcessorFacade = new ProjectProcessorFacade("react-intl");

  @Test
  public void shouldProcess() throws Exception {
    //given
    String searchDir = "./target/test-classes/react-intl";

    //when
    ProcessResult processResult = projectProcessorFacade.process(searchDir);

    //then
    Assertions.assertThat(processResult).isNotNull();
    Assertions.assertThat(processResult.getProcessedFiles()).hasSize(4);
    Assertions.assertThat(processResult.getKeys()).hasSize(13);
  }
}
