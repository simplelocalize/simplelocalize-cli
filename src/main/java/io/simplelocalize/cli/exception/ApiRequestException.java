package io.simplelocalize.cli.exception;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class ApiRequestException extends RuntimeException
{

  private final transient HttpResponse<?> httpResponse;

  public ApiRequestException(String apiMessage, HttpResponse<?> httpResponse)
  {
    super(apiMessage);
    this.httpResponse = httpResponse;
  }

  public HttpResponse<?> getHttpResponse()
  {
    return httpResponse;
  }

  public HttpRequest getHttpRequest()
  {
    return Optional.ofNullable(httpResponse).map(HttpResponse::request).orElse(null);
  }
}
