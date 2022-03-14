package io.simplelocalize.cli.client.dto;


import java.util.Objects;

public class DownloadableFile
{
  private String url;
  private String projectPath;

  public String getUrl()
  {
    return url;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  public String getProjectPath()
  {
    return projectPath;
  }

  public void setProjectPath(String projectPath)
  {
    this.projectPath = projectPath;
  }


  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DownloadableFile that = (DownloadableFile) o;
    return url.equals(that.url) && projectPath.equals(that.projectPath);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(url, projectPath);
  }

  public static final class DownloadableFileBuilder
  {
    private String url;
    private String projectPath;

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

    public DownloadableFileBuilder withProjectPath(String projectPath)
    {
      this.projectPath = projectPath;
      return this;
    }

    public DownloadableFile build()
    {
      DownloadableFile downloadableFile = new DownloadableFile();
      downloadableFile.setUrl(url);
      downloadableFile.setProjectPath(projectPath);
      return downloadableFile;
    }
  }
}
