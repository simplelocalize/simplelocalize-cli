package io.simplelocalize.cli.client.dto;

import java.util.List;
import java.util.Objects;

public class DownloadRequest
{
  private String format;
  private String languageKey;
  private String customerId;
  private List<String> options;

  public String getFormat()
  {
    return format;
  }

  public String getLanguageKey()
  {
    return languageKey;
  }

  public List<String> getOptions()
  {
    return options;
  }

  public String getCustomerId()
  {
    return customerId;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DownloadRequest that = (DownloadRequest) o;
    return Objects.equals(format, that.format) && Objects.equals(languageKey, that.languageKey) && Objects.equals(customerId, that.customerId) && Objects.equals(options, that.options);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(format, languageKey, customerId, options);
  }

  public static final class DownloadRequestBuilder
  {
    private String format;
    private String languageKey;
    private String customerId;
    private List<String> options;

    private DownloadRequestBuilder()
    {
    }

    public static DownloadRequestBuilder aDownloadRequest()
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

    public DownloadRequest build()
    {
      DownloadRequest downloadRequest = new DownloadRequest();
      downloadRequest.options = this.options;
      downloadRequest.format = this.format;
      downloadRequest.languageKey = this.languageKey;
      downloadRequest.customerId = this.customerId;
      return downloadRequest;
    }
  }
}
