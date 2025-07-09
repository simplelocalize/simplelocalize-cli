package io.simplelocalize.cli.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.simplelocalize.cli.client.dto.proxy.*;
import io.simplelocalize.cli.util.StackTraceUtils;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

final class ClientBodyBuilders
{


  private ClientBodyBuilders()
  {
  }

  static HttpRequest.BodyPublisher ofKeysBody(Collection<String> keys) throws JsonProcessingException
  {

    Set<ImportKey> importContent = keys.stream()
            .map(ImportKey::new)
            .collect(Collectors.toSet());

    ImportForm importForm = new ImportForm(importContent);

    String jsonString = ObjectMapperSingleton.getInstance().writeValueAsString(importForm);
    return HttpRequest.BodyPublishers.ofString(jsonString);
  }

  static HttpRequest.BodyPublisher ofStartAutoTranslation(Collection<String> languageKeys, Collection<String> options) throws JsonProcessingException
  {
    StartAutoTranslationRequest request = new StartAutoTranslationRequest(languageKeys, "CLI", options);
    String jsonString = ObjectMapperSingleton.getInstance().writeValueAsString(request);
    return HttpRequest.BodyPublishers.ofString(jsonString);
  }

  static HttpRequest.BodyPublisher ofException(Configuration configuration, Exception exception) throws JsonProcessingException
  {
    String configurationString = Optional.ofNullable(configuration).map(Configuration::toString).orElse("");
    String message = Optional.ofNullable(exception).map(Throwable::getMessage).orElse("");
    String stackTrace = StackTraceUtils.getStackTrace(exception);
    ExceptionRequest request = new ExceptionRequest(configurationString, message, stackTrace);
    String jsonString = ObjectMapperSingleton.getInstance().writeValueAsString(request);
    return HttpRequest.BodyPublishers.ofString(jsonString);
  }

  static HttpRequest.BodyPublisher ofMimeMultipartData(Map<Object, Object> data, String boundary) throws IOException
  {
    ArrayList<byte[]> byteArrays = new ArrayList<>();
    byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=").getBytes(StandardCharsets.UTF_8);
    for (Map.Entry<Object, Object> entry : data.entrySet())
    {
      byteArrays.add(separator);

      if (entry.getValue() instanceof Path path)
      {
        String mimeType = Files.probeContentType(path);
        byteArrays.add(("\"" + entry.getKey() + "\"; filename=\"" + path.getFileName() + "\"\r\nContent-Type: " + mimeType + "\r\n\r\n").getBytes(StandardCharsets.UTF_8));
        byteArrays.add(Files.readAllBytes(path));
        byteArrays.add("\r\n".getBytes(StandardCharsets.UTF_8));
      } else
      {
        byteArrays.add(("\"" + entry.getKey() + "\"\r\n\r\n" + entry.getValue() + "\r\n").getBytes(StandardCharsets.UTF_8));
      }
    }
    byteArrays.add(("--" + boundary + "--").getBytes(StandardCharsets.UTF_8));
    return HttpRequest.BodyPublishers.ofByteArrays(byteArrays);
  }

}
