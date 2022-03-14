package io.simplelocalize.cli;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@Disabled
public class SimplelocalizeCliCommandIT
{
  private final static String API_KEY = "8fFC852d966FDBe2543428504ADe2Ca1c9039Ea02721CE28d9a62BEC2e5490e8";

  @Test
  public void shouldUpload() throws Exception
  {
    System.setOut(new PrintStream(new ByteArrayOutputStream()));
    try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI))
    {
      String[] args = new String[]{
              "upload",
              "--apiKey", API_KEY,
              "--uploadFormat", "java-properties",
              "--uploadPath", "./junit/lang-in-filename-suffix/messages_{lang}.properties"
      };
      PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
    }
  }

  @Test
  public void shouldDownload() throws Exception
  {
    System.setOut(new PrintStream(new ByteArrayOutputStream()));
    try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI))
    {
      String[] args = new String[]{
              "download",
              "--apiKey", API_KEY,
              "--downloadFormat", "java-properties",
              "--downloadPath", "./junit/lang-in-filename-suffix/messages_{lang}.properties"
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
