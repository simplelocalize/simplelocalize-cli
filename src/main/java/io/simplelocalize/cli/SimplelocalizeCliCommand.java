package io.simplelocalize.cli;

import io.micronaut.configuration.picocli.PicocliRunner;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "simplelocalize-cli", description = "...",
        mixinStandardHelpOptions = true, subcommands = {FindCommand.class})
public class SimplelocalizeCliCommand implements Runnable {

  @Option(names = {"-v", "--verbose"}, description = "...")
  boolean verbose;

  public static void main(String[] args) throws Exception {
    PicocliRunner.run(SimplelocalizeCliCommand.class, args);
  }

  public void run() {
    // business logic here
    if (verbose) {
      System.out.println("Hi!");
    }
  }
}
