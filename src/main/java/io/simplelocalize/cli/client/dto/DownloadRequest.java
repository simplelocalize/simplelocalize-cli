package io.simplelocalize.cli.client.dto;

import java.util.List;
import java.util.Objects;

public class DownloadRequest
{

  private String path;
  private String format;
  private String languageKey;
  private List<String> options;
  private String namespace;


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

  public String getNamespace()
  {
    return namespace;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DownloadRequest that = (DownloadRequest) o;
    return Objects.equals(path, that.path) &&
            Objects.equals(format, that.format) &&
            Objects.equals(languageKey, that.languageKey) &&
            Objects.equals(options, that.options) &&
            Objects.equals(namespace, that.namespace);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(path, format, languageKey, options, namespace);
  }

  public static final class DownloadRequestBuilder
  {
    private String path;
    private String format;
    private String languageKey;
    private List<String> options;
    private String namespace;

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

    public DownloadRequestBuilder withNamespace(String namespace)
    {
      this.namespace = namespace;
      return this;
    }

    public DownloadRequest build()
    {
      DownloadRequest downloadRequest = new DownloadRequest();
      downloadRequest.options = this.options;
      downloadRequest.format = this.format;
      downloadRequest.path = this.path;
      downloadRequest.languageKey = this.languageKey;
      downloadRequest.namespace = this.namespace;
      return downloadRequest;
    }
  }

}
