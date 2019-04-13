package io.simplelocalize.cli;

import java.io.IOException;

public class Main {

  public static void main(String[] args) throws IOException {
    ShellCommander shellCommander = new ShellCommander();
    shellCommander.run(args);
  }

}
