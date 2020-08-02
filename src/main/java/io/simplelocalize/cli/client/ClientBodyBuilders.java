package io.simplelocalize.cli.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.simplelocalize.cli.client.dto.ImportForm;
import io.simplelocalize.cli.client.dto.ImportKey;

import java.net.http.HttpRequest;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

class ClientBodyBuilders {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  private ClientBodyBuilders() {
  }

  static HttpRequest.BodyPublisher ofKeysBody(Collection<String> keys) throws JsonProcessingException {
    ImportForm importForm = new ImportForm();

    Set<ImportKey> importContent = keys.stream()
            .map(ImportKey::new)
            .collect(Collectors.toSet());

    importForm.setContent(importContent);

    String jsonString = objectMapper.writeValueAsString(importForm);
    return HttpRequest.BodyPublishers.ofString(jsonString);
  }

}
