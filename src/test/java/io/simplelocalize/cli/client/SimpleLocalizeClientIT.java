package io.simplelocalize.cli.client;

import io.simplelocalize.cli.client.dto.UploadRequest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import static io.simplelocalize.cli.client.dto.UploadRequest.UploadFileRequestBuilder.anUploadFileRequest;


/**
 * Tests are disabled due to fact they are using production server.
 * Feel free to remove @Disabled annotation to test CLI against your production project
 */
@Disabled
class SimpleLocalizeClientIT
{

  private static final String BASE_URL = "https://api.simplelocalize.io";

  @Test
  void shouldSendKeys() throws Exception
  {
    //given
    SimpleLocalizeClient client = SimpleLocalizeClient.create(BASE_URL, "237b305f6b2273e92ac857eb44d7f33b");

    //when
    client.uploadKeys(List.of("test"));

    //then
  }

  @Test
  void shouldUploadFile() throws Exception
  {
    //given
    SimpleLocalizeClient client = SimpleLocalizeClient.create(BASE_URL, "81707741b64e68427e1a2c20e75095b1");

    UploadRequest uploadRequest = anUploadFileRequest()
            .withPath(Path.of("./test.json"))
            .withLanguageKey(null)
            .withFormat("multi-language-json")
            .withOptions(Collections.emptyList())
            .build();

    //when
    client.uploadFile(uploadRequest);

    //then
  }

}
