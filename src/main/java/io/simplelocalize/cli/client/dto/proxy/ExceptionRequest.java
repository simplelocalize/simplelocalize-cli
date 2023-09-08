package io.simplelocalize.cli.client.dto.proxy;


import io.simplelocalize.cli.NativeProxy;

@NativeProxy
public record ExceptionRequest(String configuration, String message, String stacktrace)
{
}
