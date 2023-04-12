package io.simplelocalize.cli.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.simplelocalize.cli.client.dto.ImportForm;
import io.simplelocalize.cli.client.dto.ImportKey;
import io.simplelocalize.cli.client.dto.StartAutoTranslationRequest;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

final class ClientBodyBuilders
{

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private ClientBodyBuilders()
  {
  }

  static HttpRequest.BodyPublisher ofKeysBody(Collection<String> keys) throws JsonProcessingException
  {

    Set<ImportKey> importContent = keys.stream()
            .map(ImportKey::new)
            .collect(Collectors.toSet());

    ImportForm importForm = new ImportForm(importContent);

    String jsonString = objectMapper.writeValueAsString(importForm);
    return HttpRequest.BodyPublishers.ofString(jsonString);
  }

  static HttpRequest.BodyPublisher ofStartAutoTranslation(Collection<String> languageKeys) throws JsonProcessingException
  {
    StartAutoTranslationRequest request = new StartAutoTranslationRequest(languageKeys, "CLI");
    String jsonString = objectMapper.writeValueAsString(request);
    return HttpRequest.BodyPublishers.ofString(jsonString);
  }

  static HttpRequest.BodyPublisher ofMimeMultipartData(Map<Object, Object> data, String boundary) throws IOException
  {
    ArrayList<byte[]> byteArrays = new ArrayList<>();
    byte[] separator = ("--" + boundary + "\r\nContent-Disposition: form-data; name=").getBytes(StandardCharsets.UTF_8);
    for (Map.Entry<Object, Object> entry : data.entrySet())
    {
      byteArrays.add(separator);

      if (entry.getValue() instanceof Path)
      {
        var path = (Path) entry.getValue();
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
