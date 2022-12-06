package io.simplelocalize.cli.client.dto;



import java.util.Objects;

public class DownloadableFile
{
  private String url;
  private String namespace;
  private String language;

  public void setUrl(String url)
  {
    this.url = url;
  }

  public String getNamespace()
  {
    return namespace;
  }

  public void setNamespace(String namespace)
  {
    this.namespace = namespace;
  }

  public String getUrl()
  {
    return url;
  }

  public String getLanguage()
  {
    return language;
  }

  public void setLanguage(String language)
  {
    this.language = language;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DownloadableFile that = (DownloadableFile) o;
    return Objects.equals(url, that.url) && Objects.equals(namespace, that.namespace) && Objects.equals(language, that.language);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(url, namespace, language);
  }


  public static final class DownloadableFileBuilder
  {
    private String url;
    private String namespace;
    private String language;

    private DownloadableFileBuilder()
    {
    }

    public static DownloadableFileBuilder aDownloadableFile()
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
      DownloadableFile downloadableFile = new DownloadableFile();
      downloadableFile.url = this.url;
      downloadableFile.language = this.language;
      downloadableFile.namespace = this.namespace;
      return downloadableFile;
    }
  }
}
