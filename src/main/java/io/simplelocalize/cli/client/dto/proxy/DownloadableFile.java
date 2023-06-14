package io.simplelocalize.cli.client.dto.proxy;

import io.simplelocalize.cli.NativeProxy;

@NativeProxy
public record DownloadableFile(String url, String namespace, String language)
{

  public static final class DownloadableFileBuilder
  {
    private String url;
    private String namespace;
    private String language;

    private DownloadableFileBuilder()
    {
    }

    public static DownloadableFileBuilder Builder()
    {
      return new DownloadableFileBuilder();
    }

    public DownloadableFileBuilder withUrl(String url)
    {
      this.url = url;
      return this;
    }

    public DownloadableFileBuilder withNamespace(String namespace)
    {
      this.namespace = namespace;
      return this;
    }

    public DownloadableFileBuilder withLanguage(String language)
    {
      this.language = language;
      return this;
    }

    public DownloadableFile build()
    {
      return new DownloadableFile(
              this.url,
              this.namespace,
              this.language
      );
    }
  }
}
