package io.simplelocalize.cli.client;

import io.simplelocalize.cli.client.dto.DownloadRequest;
import io.simplelocalize.cli.client.dto.UploadRequest;
import io.simplelocalize.cli.util.StringUtils;

import java.net.URI;
import java.util.List;

public class SimpleLocalizeUriFactory
{
  private final String baseUrl;

  public SimpleLocalizeUriFactory(String baseUrl)
  {
    this.baseUrl = baseUrl;
  }

  URI buildSendKeysURI()
  {
    return URI.create(baseUrl + "/cli/v1/keys");
  }

  URI buildDownloadUri(DownloadRequest downloadRequest)
  {
    String endpointUrl = baseUrl + "/cli/v2/download?downloadFormat=" + downloadRequest.format();
    String languageKey = downloadRequest.languageKey();
    boolean isRequestedTranslationsForSpecificLanguage = StringUtils.isNotEmpty(languageKey);
    if (isRequestedTranslationsForSpecificLanguage)
    {
      endpointUrl += "&languageKey=" + languageKey;
    }

    List<String> downloadOptions = downloadRequest.options();
    if (!downloadOptions.isEmpty())
    {
      endpointUrl += "&downloadOptions=" + String.join(",", downloadOptions);
    }

    String customerId = downloadRequest.customerId();
    if (StringUtils.isNotEmpty(customerId))
    {
      endpointUrl += "&customerId=" + customerId;
    }

    String sort = downloadRequest.sort();
    if (StringUtils.isNotEmpty(sort))
    {
      endpointUrl += "&sort=" + sort;
    }

    return URI.create(endpointUrl);
  }


  URI buildUploadUri(UploadRequest uploadRequest)
  {
    String endpointUrl = baseUrl + "/cli/v2/upload?uploadFormat=" + uploadRequest.format();
    String languageKey = uploadRequest.languageKey();
    if (StringUtils.isNotEmpty(languageKey))
    {
      endpointUrl += "&languageKey=" + languageKey;
    }

    List<String> uploadOptions = uploadRequest.options();
    if (!uploadOptions.isEmpty())
    {
      endpointUrl += "&uploadOptions=" + String.join(",", uploadOptions);
    }

    String namespace = uploadRequest.namespace();
    if (StringUtils.isNotEmpty(namespace))
    {
      endpointUrl += "&namespace=" + namespace;
    }

    String customerId = uploadRequest.customerId();
    if (StringUtils.isNotEmpty(customerId))
    {
      endpointUrl += "&customerId=" + customerId;
    }

    return URI.create(endpointUrl);
  }

  URI buildGetProjectUri()
  {
    return URI.create(baseUrl + "/api/v1/project");
  }

  public URI buildPublishUri(String environment)
  {
    return URI.create(baseUrl + "/api/v2/environments/" + environment + "/publish?source=CLI");
  }

  public URI buildPurgeTranslations()
  {
    return URI.create(baseUrl + "/api/v1/translations/purge?source=CLI");
  }

  URI buildGetRunningAutoTranslationJobsUri()
  {
    return URI.create(baseUrl + "/api/v2/jobs?status=RUNNING&type=AUTO_TRANSLATION");
  }

  public URI buildStartAutoTranslationUri()
  {
    return URI.create(baseUrl + "/api/v2/jobs/auto-translate");
  }
}
