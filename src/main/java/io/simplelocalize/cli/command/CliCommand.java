package io.simplelocalize.cli.command;

import java.io.IOException;

public interface CliCommand
{
  void invoke() throws IOException, InterruptedException;
}
