package io.simplelocalize.cli.client.dto;

import java.util.List;

public class DownloadRequest
{

  private String path;
  private String format;
  private String languageKey;
  private List<String> options;

  public String getPath()
  {
    return path;
  }

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

  public static final class DownloadRequestBuilder
  {
    private String path;
    private String format;
    private String languageKey;
    private List<String> options;

    private DownloadRequestBuilder()
    {
    }

    public static DownloadRequestBuilder aDownloadRequest()
    {
      return new DownloadRequestBuilder();
    }

    public DownloadRequestBuilder withPath(String downloadPath)
    {
      this.path = downloadPath;
      return this;
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
      downloadRequest.path = this.path;
      downloadRequest.languageKey = this.languageKey;
      return downloadRequest;
    }
  }
}
