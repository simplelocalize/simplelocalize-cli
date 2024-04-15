package io.simplelocalize.cli.client;

import io.simplelocalize.cli.Version;
import io.simplelocalize.cli.client.dto.UploadRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class SimpleLocalizeHttpRequestFactory
{
  private static final String TOKEN_HEADER_NAME = "X-SimpleLocalize-Token";
  private static final String CLI_VERSION_HEADER_NAME = "X-SimpleLocalize-Cli-Version";
  private static final String CONTENT_TYPE_HEADER_NAME = "Content-Type";
  private final String apiKey;
  private final SecureRandom random;


  public SimpleLocalizeHttpRequestFactory(String apiKey)
  {
    this.apiKey = apiKey;
    this.random = new SecureRandom();
  }

  HttpRequest createUploadFileRequest(URI uri, UploadRequest uploadRequest) throws IOException
  {
    int pseudoRandomNumber = (int) (random.nextDouble() * 1_000_000_000);
    String boundary = "simplelocalize-" + pseudoRandomNumber;
    Map<Object, Object> formData = new HashMap<>();
    Path uploadPath = uploadRequest.path();
    formData.put("file", uploadPath);
    return createBaseRequest(uri)
            .POST(ClientBodyBuilders.ofMimeMultipartData(formData, boundary))
            .header(CONTENT_TYPE_HEADER_NAME, "multipart/form-data; boundary=" + boundary)
            .build();

  }

  HttpRequest.Builder createGetRequest(URI uri)
  {
    return createBaseRequest(uri).GET();
  }

  HttpRequest.Builder createDeleteRequest(URI uri)
  {
    return createBaseRequest(uri).DELETE();
  }

  HttpRequest.Builder createBaseRequest(URI uri)
  {
    return HttpRequest.newBuilder()
            .uri(uri)
            .header(CLI_VERSION_HEADER_NAME, Version.NUMBER)
            .header(TOKEN_HEADER_NAME, apiKey);
  }

}
