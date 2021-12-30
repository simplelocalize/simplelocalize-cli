package io.simplelocalize.cli.client.dto;

import com.google.common.base.Objects;

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
    return Objects.equal(url, that.url) && Objects.equal(projectPath, that.projectPath);
  }

  @Override
  public int hashCode()
  {
    return Objects.hashCode(url, projectPath);
  }
}
