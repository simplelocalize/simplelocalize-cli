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
  public void shouldThrowWhenMissingClientId() throws Exception {
    //given
    Configuration randomConfiguration = EnhancedRandom.random(Configuration.class);
    randomConfiguration.setClientId("");

    //when
    configurationValidator.validate(randomConfiguration);

    //then
  }

  @Test(expected = MissingPropertyException.class)
  public void shouldThrowWhenMissingClientSecret() throws Exception {
    //given
    Configuration randomConfiguration = EnhancedRandom.random(Configuration.class);
    randomConfiguration.setClientSecret("");

    //when
    configurationValidator.validate(randomConfiguration);

    //then
  }

  @Test(expected = MissingPropertyException.class)
  public void shouldThrowWhenMissingProjectToken() throws Exception {
    //given
    Configuration randomConfiguration = EnhancedRandom.random(Configuration.class);
    randomConfiguration.setProjectToken("");

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
