package io.simplelocalize.cli.client.dto;

import java.nio.file.Path;

public class FileToUpload
{
  private Path path;
  private String language;

  public FileToUpload(Path path, String language)
  {
    this.path = path;
    this.language = language;
  }

  public static FileToUpload of(Path path, String language){
    return new FileToUpload(path, language);
  }

  public Path getPath()
  {
    return path;
  }

  public void setPath(Path path)
  {
    this.path = path;
  }

  public String getLanguage()
  {
    return language;
  }

  public void setLanguage(String language)
  {
    this.language = language;
  }
}
