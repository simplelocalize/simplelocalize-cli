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
  private static final String VERSION_1_API = "/api/v1";
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
    String endpointUrl = baseUrl + CLI_VERSION_2_API + "/download?downloadFormat=" + downloadRequest.format();
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
    String endpointUrl = baseUrl + CLI_VERSION_2_API + "/upload?uploadFormat=" + uploadRequest.format();
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
    return URI.create(baseUrl + VERSION_1_API + "/project");
  }

  public URI buildPublishUri(String environment)
  {
    if (environment.equals("latest"))
    {
      return URI.create(baseUrl + "/api/v1/translations/publish");
    }

    if (environment.equals("production"))
    {
      return URI.create(baseUrl + "/api/v1/translations/deploy?sourceEnvironment=_latest&targetEnvironment=_production");
    }
    throw new IllegalArgumentException("Unknown environment: " + environment);
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
