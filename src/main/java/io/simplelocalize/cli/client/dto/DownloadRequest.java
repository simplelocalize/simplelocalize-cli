package io.simplelocalize.cli.client.dto;

import java.util.List;

public record DownloadRequest(String format, String languageKey, String customerId, List<String> options, String sort)
{
  public static final class DownloadRequestBuilder
  {
    private String format;
    private String languageKey;
    private String customerId;
    private List<String> options;
    private String sort;

    private DownloadRequestBuilder()
    {
    }

    public static DownloadRequestBuilder builder()
    {
      return new DownloadRequestBuilder();
    }

    public DownloadRequestBuilder withFormat(String downloadFormat)
    {
      this.format = downloadFormat;
      return this;
    }

    public DownloadRequestBuilder withLanguageKey(String languageKey)
    {
      this.languageKey = languageKey;
      return this;
    }

    public DownloadRequestBuilder withCustomerId(String customerId)
    {
      this.customerId = customerId;
      return this;
    }

    public DownloadRequestBuilder withOptions(List<String> downloadOptions)
    {
      this.options = downloadOptions;
      return this;
    }

    public DownloadRequestBuilder withSort(String sort)
    {
      this.sort = sort;
      return this;
    }

    public DownloadRequest build()
    {
      return new DownloadRequest(
              this.format,
              this.languageKey,
              this.customerId,
              this.options,
              this.sort
      );
    }
  }
}
