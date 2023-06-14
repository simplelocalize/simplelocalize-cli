package io.simplelocalize.cli.client.dto;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UploadRequestTest
{

  @Test
  void shouldReturnTrueIfWhenSameObject()
  {
    //given
    UploadRequest object1 = UploadRequest.UploadFileRequestBuilder.Builder()
            .withFormat("format")
            .withLanguageKey("pl")
            .withOptions(List.of("OPTION"))
            .withPath(Path.of("./"))
            .build();

    UploadRequest object2 = UploadRequest.UploadFileRequestBuilder.Builder()
            .withFormat("format")
            .withLanguageKey("pl")
            .withOptions(List.of("OPTION"))
            .withPath(Path.of("./"))
            .build();

    //when
    boolean result = object1.equals(object2);

    //then
    assertThat(result).isTrue();
    assertThat(object1).isEqualTo(object2);
  }

  @Test
  void shouldReturnFalseIfObjectsAreDifferent()
  {
    //given
    UploadRequest object1 = UploadRequest.UploadFileRequestBuilder.Builder()
            .withFormat("format")
            .withLanguageKey("de")
            .withOptions(List.of("OPTION"))
            .withPath(Path.of("./"))
            .build();

    UploadRequest object2 = UploadRequest.UploadFileRequestBuilder.Builder()
            .withFormat("format")
            .withLanguageKey("pl")
            .withOptions(List.of("OPTION"))
            .withPath(Path.of("./"))
            .build();

    //when
    boolean result = object1.equals(object2);

    //then
    assertThat(result).isFalse();
    assertThat(object1).isNotEqualTo(object2);
  }

  @Test
  void testHashCode()
  {
    //given
    UploadRequest object1 = UploadRequest.UploadFileRequestBuilder.Builder()
            .withFormat("format")
            .withLanguageKey("pl")
            .withOptions(List.of("OPTION"))
            .withPath(Path.of("./"))
            .build();

    UploadRequest object2 = UploadRequest.UploadFileRequestBuilder.Builder()
            .withFormat("format")
            .withLanguageKey("pl")
            .withOptions(List.of("OPTION"))
            .withPath(Path.of("./"))
            .build();

    //when
    int hashCode1 = object1.hashCode();
    int hashCode2 = object2.hashCode();

    //then
    assertThat(hashCode1).isEqualTo(hashCode2);

  }
}
