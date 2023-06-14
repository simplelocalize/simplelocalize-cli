package io.simplelocalize.cli;

import picocli.CommandLine;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@CommandLine.Command
public @interface NativeProxy
{
}
