package io.simplelocalize.cli.command;

import io.simplelocalize.cli.configuration.Configuration;

public interface CliCommand
{
  void invoke(Configuration configuration);
}
