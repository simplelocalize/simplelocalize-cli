package io.simplelocalize.cli.client;

import io.simplelocalize.cli.client.dto.ExportRequest;
import io.simplelocalize.cli.client.dto.UploadRequest;
import io.simplelocalize.cli.util.StringUtils;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;

public class SimpleLocalizeUriFactory
{
  private final String baseUrl;

  public SimpleLocalizeUriFactory(String baseUrl)
  {
    this.baseUrl = baseUrl;
  }

  URI buildDownloadUri(ExportRequest exportRequest)
  {
    String endpointUrl = baseUrl + "/cli/v2/download?downloadFormat=" + exportRequest.format();
    final List<String> languageKeys = exportRequest.languageKeys();
    boolean isRequestedTranslationsForSpecificLanguage = languageKeys != null && !languageKeys.isEmpty();
    if (isRequestedTranslationsForSpecificLanguage)
    {
      endpointUrl += "&languageKey=" + String.join(",", languageKeys);
    }

    List<String> downloadOptions = exportRequest.options();
    if (!downloadOptions.isEmpty())
    {
      endpointUrl += "&downloadOptions=" + String.join(",", downloadOptions);
    }

    String namespace = exportRequest.namespace();
    if (StringUtils.isNotEmpty(namespace))
    {
      endpointUrl += "&namespace=" + namespace;
    }

    String customerId = exportRequest.customerId();
    if (StringUtils.isNotEmpty(customerId))
    {
      endpointUrl += "&customerId=" + customerId;
    }

    String sort = exportRequest.sort();
    if (StringUtils.isNotEmpty(sort))
    {
      endpointUrl += "&sort=" + sort;
    }

    return URI.create(endpointUrl);
  }

  URI buildUploadUri(UploadRequest uploadRequest, boolean isPreviewMode)
  {
    String command = "upload";
    if (isPreviewMode)
    {
      command = "upload-preview";
    }

    String endpointUrl = baseUrl + "/cli/v2/" + command + "?uploadFormat=" + uploadRequest.format();

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

    String translationKey = uploadRequest.translationKey();
    if (StringUtils.isNotEmpty(translationKey))
    {
      if (translationKey.length() > 500)
      {
        throw new IllegalArgumentException("Translation key is too long. Max length is 500 characters");
      }
      endpointUrl += "&translationKey=" + translationKey;
    }

    Path filePath = uploadRequest.path();
    String fileName = filePath.getFileName().toString();
    if (StringUtils.isNotEmpty(fileName))
    {
      endpointUrl += "&fileName=" + fileName;
    }

    boolean includeFilePath = uploadOptions.contains("INCLUDE_FILE_PATH");
    if (includeFilePath)
    {
      endpointUrl += "&path=" + filePath;
    }

    return URI.create(endpointUrl);
  }

  URI buildGetProjectUri()
  {
    return URI.create(baseUrl + "/api/v2/project");
  }

  public URI buildPublishUri(String environment)
  {
    return URI.create(baseUrl + "/api/v2/environments/" + environment + "/publish?source=CLI");
  }

  public URI buildPurgeTranslations()
  {
    return URI.create(baseUrl + "/api/v1/translations/purge?source=CLI");
  }

  public URI buildStacktraceUri()
  {
    return URI.create(baseUrl + "/cli/v1/stacktrace");
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
