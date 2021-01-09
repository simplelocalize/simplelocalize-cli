package io.simplelocalize.cli;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SimplelocalizeCliCommandTest {

    @Test
    public void testWithCommandLineOption() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI)) {
            String[] args = new String[] { "-c" };
            PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
        }
    }

    @Test
    public void testUpload() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI)) {
            String[] args = new String[] { "--apiKey", "57834922", "--uploadPath", "./messages_{lang}.properties", "upload"};
            PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
        }
    }

    @Test
    public void testUpload2() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI)) {
            String[] args = new String[] { "upload", "--apiKey", "3463"};
            PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
        }
    }

    @Test
    public void testDownload() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI)) {
            String[] args = new String[] { "upload" };
            PicocliRunner.run(SimplelocalizeCliCommand.class, ctx, args);
        }
    }
}
