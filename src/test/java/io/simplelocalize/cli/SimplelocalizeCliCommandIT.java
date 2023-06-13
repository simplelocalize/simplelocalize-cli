package io.simplelocalize.cli;

import org.junit.jupiter.api.Disabled;

@Disabled
public class SimplelocalizeCliCommandIT // NOSONAR
{
  private final static String API_KEY = "my-test-key";
  private final static String BASE_URL = "http://localhost:8080";

//  @Test
//  public void shouldUpload() throws Exception
//  {
//    System.setOut(new PrintStream(new ByteArrayOutputStream()));
//    try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI))
//    {
//      String[] args = new String[]{
//              "upload",
//              "--baseUrl", BASE_URL,
//              "--apiKey", API_KEY,
//              "--uploadFormat", "java-properties",
//              "--uploadPath", "./junit/lang-in-filename-suffix/messages_{lang}.properties"
//      };
//      PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
//    }
//  }
//
//  @Test
//  public void shouldStartAutoTranslation() throws Exception
//  {
//    System.setOut(new PrintStream(new ByteArrayOutputStream()));
//    try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI))
//    {
//      String[] args = new String[]{
//              "auto-translate",
//              "--baseUrl", BASE_URL,
//              "--apiKey", API_KEY
//      };
//      PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
//    }
//  }
//
//  @Test
//  public void shouldInitFile() throws Exception
//  {
//    System.setOut(new PrintStream(new ByteArrayOutputStream()));
//    try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI))
//    {
//      String[] args = new String[]{
//              "init"
//      };
//      PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
//    }
//  }
//
//  @Test
//  public void shouldDownload() throws Exception
//  {
//    System.setOut(new PrintStream(new ByteArrayOutputStream()));
//    try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI))
//    {
//      String[] args = new String[]{
//              "download",
//              "--baseUrl", BASE_URL,
//              "--apiKey", API_KEY,
//              "--downloadFormat", "java-properties",
//              "--downloadPath", "./junit/lang-in-filename-suffix/messages_{lang}.properties"
//      };
//      PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
//    }
//  }
//
//  @Test
//  public void shouldUploadWithMultiFileMode() throws Exception
//  {
//    System.setOut(new PrintStream(new ByteArrayOutputStream()));
//    try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI))
//    {
//      String[] args = new String[]{
//              "upload",
//              "--baseUrl", BASE_URL,
//              "--apiKey", API_KEY,
//              "--uploadFormat", "multi-language-json",
//              "--uploadPath", "/Users/jpomykala/Workspace/simplelocalize.io/application/cli/junit/multi-file",
//              "--uploadOptions", "SPLIT_BY_NAMESPACES"
//      };
//      PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
//    }
//  }
//
//  @Test
//  public void shouldDownloadWithMultiFileMode() throws Exception
//  {
//    System.setOut(new PrintStream(new ByteArrayOutputStream()));
//
//    try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI))
//    {
//      String[] args = new String[]{
//              "download",
//              "--baseUrl", BASE_URL,
//              "--apiKey", API_KEY,
//              "--downloadFormat", "multi-language-json",
//              "--downloadPath", "/Users/jpomykala/Workspace/simplelocalize.io/application/cli/junit/multi-file/",
//              "--downloadOptions", "SPLIT_BY_NAMESPACES"
//      };
//      PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
//    }
//  }
//
//  @Test
//  public void shouldUploadWhenMinimalParameters() throws Exception
//  {
//    System.setOut(new PrintStream(new ByteArrayOutputStream()));
//    try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI))
//    {
//      String[] args = new String[]{"upload", "--apiKey", API_KEY};
//      PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
//    }
//  }
//
//  @Test
//  public void shouldDownloadWhenNoParameters() throws Exception
//  {
//    System.setOut(new PrintStream(new ByteArrayOutputStream()));
//    try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI))
//    {
//      String[] args = new String[]{"download"};
//      PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
//    }
//  }
}
