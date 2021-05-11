package io.simplelocalize.cli.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

class ClientBodyBuildersTest
{

  @Test
  void ofKeysBody() throws JsonProcessingException
  {
    //given
    List<String> input = List.of("key1", "key2");

    //when
    HttpRequest.BodyPublisher result = ClientBodyBuilders.ofKeysBody(input);

    //then
    Assertions.assertThat(result).isNotNull();
  }

  @Test
  void ofMimeMultipartData() throws IOException
  {
    //given
    String boundary = "894758275029";
    Map<Object, Object> data = Map.of(
            "file", Path.of("./junit/client-body-builders-test/de.json"),
            "language", "en"
    );

    //when
    HttpRequest.BodyPublisher result = ClientBodyBuilders.ofMimeMultipartData(data, boundary);

    //then
    Assertions.assertThat(result).isNotNull();
  }
}
