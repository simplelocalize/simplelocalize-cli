package io.simplelocalize.cli.client;

import io.simplelocalize.cli.client.dto.DownloadRequest;
import io.simplelocalize.cli.client.dto.UploadRequest;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.List;

public class SimpleLocalizeUriFactory
{
  private static final String CLI_VERSION_1_API = "/cli/v1";
  private static final String CLI_VERSION_2_API = "/cli/v2";
  private final String baseUrl;

  public SimpleLocalizeUriFactory(String baseUrl)
  {
    this.baseUrl = baseUrl;
  }

  URI buildSendKeysURI()
  {
    return URI.create(baseUrl + CLI_VERSION_1_API + "/keys");
  }

  URI buildDownloadUri(DownloadRequest downloadRequest)
  {
    String endpointUrl = baseUrl + CLI_VERSION_2_API + "/download?downloadFormat=" + downloadRequest.getFormat();
    String languageKey = downloadRequest.getLanguageKey();
    boolean isRequestedTranslationsForSpecificLanguage = StringUtils.isNotEmpty(languageKey);
    if (isRequestedTranslationsForSpecificLanguage)
    {
      endpointUrl += "&languageKey=" + languageKey;
    }

    List<String> downloadOptions = downloadRequest.getOptions();
    if (!downloadOptions.isEmpty())
    {
      endpointUrl += "&downloadOptions=" + String.join(",", downloadOptions);
    }
    return URI.create(endpointUrl);
  }


  URI buildUploadUri(UploadRequest uploadRequest)
  {
    String endpointUrl = baseUrl + CLI_VERSION_2_API + "/upload?uploadFormat=" + uploadRequest.getFormat();
    String languageKey = uploadRequest.getLanguageKey();
    if (StringUtils.isNotEmpty(languageKey))
    {
      endpointUrl += "&languageKey=" + languageKey;
    }

    List<String> uploadOptions = uploadRequest.getOptions();
    if (!uploadOptions.isEmpty())
    {
      endpointUrl += "&uploadOptions=" + String.join(",", uploadOptions);
    }

    String namespace = uploadRequest.getNamespace();
    if (StringUtils.isNotEmpty(namespace))
    {
      endpointUrl += "&namespace=" + namespace;
    }

    return URI.create(endpointUrl);
  }

  URI buildValidateGateUri()
  {
    return URI.create(baseUrl + CLI_VERSION_1_API + "/validate/gate");
  }

}
