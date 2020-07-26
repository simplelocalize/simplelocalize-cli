package io.simplelocalize.cli;

import picocli.CommandLine;

@CommandLine.Command(name = "find", description = "Find i18n terms in your source code", mixinStandardHelpOptions = true)
public class FindCommand implements Runnable {


  @CommandLine.Option(names = {"-c", "--config"}, description = "Path to *.yml configuration file")
  String path;

  @Override
  public void run() {
    System.out.println("Searching...");
  }
}
