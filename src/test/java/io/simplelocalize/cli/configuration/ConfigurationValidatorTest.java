package io.simplelocalize.cli.configuration;

import io.github.benas.randombeans.api.EnhancedRandom;
import io.simplelocalize.cli.exception.MissingPropertyException;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigurationValidatorTest {

  private ConfigurationValidator configurationValidator= new ConfigurationValidator();

  @Test
  public void shouldValidate() throws Exception {
    //given
    Configuration randomConfiguration = EnhancedRandom.random(Configuration.class);

    //when
    configurationValidator.validate(randomConfiguration);

    //then
  }

  @Test(expected = MissingPropertyException.class)
  public void shouldThrowWhenMissingToken() throws Exception {
    //given
    Configuration randomConfiguration = EnhancedRandom.random(Configuration.class);
    randomConfiguration.setToken("");

    //when
    configurationValidator.validate(randomConfiguration);

    //then
  }

  @Test(expected = MissingPropertyException.class)
  public void shouldThrowWhenMissingProjectType() throws Exception {
    //given
    Configuration randomConfiguration = EnhancedRandom.random(Configuration.class);
    randomConfiguration.setProjectType("");

    //when
    configurationValidator.validate(randomConfiguration);

    //then
  }
}
