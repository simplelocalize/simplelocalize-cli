package io.simplelocalize.cli;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SimplelocalizeCliCommandIT
{
  private final static String API_KEY = "b3BFCf5E7c53C3A62319B51D7d4f7E0394e7c213702cecf217Bb3fc293Ef361B";

  @Test
  public void shouldUpload() throws Exception
  {
    System.setOut(new PrintStream(new ByteArrayOutputStream()));
    try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI))
    {
      String[] args = new String[]{
              "upload",
              "--apiKey", API_KEY,
              "--uploadPath", "./messages_{lang}.properties"
      };
      PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
    }
  }

  @Test
  public void shouldUploadWithMultiFileMode() throws Exception
  {
    System.setOut(new PrintStream(new ByteArrayOutputStream()));
    try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI))
    {
      String[] args = new String[]{
              "upload",
              "--apiKey", API_KEY,
              "--uploadFormat", "multi-language-json",
              "--uploadPath", "/Users/jpomykala/Workspace/simplelocalize.io/application/cli/junit/multi-file",
              "--uploadOptions", "MULTI_FILE"
      };
      PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
    }
  }

  @Test
  public void shouldDownloadWithMultiFileMode() throws Exception
  {
    System.setOut(new PrintStream(new ByteArrayOutputStream()));

    try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI))
    {
      String[] args = new String[]{
              "download",
              "--apiKey", API_KEY,
              "--downloadFormat", "multi-language-json",
              "--downloadPath", "/Users/jpomykala/Workspace/simplelocalize.io/application/cli/junit/multi-file/",
              "--downloadOptions", "MULTI_FILE"
      };
      PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
    }
  }

  @Test
  public void shouldUploadWhenMinimalParameters() throws Exception
  {
    System.setOut(new PrintStream(new ByteArrayOutputStream()));
    try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI))
    {
      String[] args = new String[]{"upload", "--apiKey", API_KEY};
      PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
    }
  }

  @Test
  public void shouldDownloadWhenNoParameters() throws Exception
  {
    System.setOut(new PrintStream(new ByteArrayOutputStream()));
    try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI))
    {
      String[] args = new String[]{"download"};
      PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
    }
  }
}
