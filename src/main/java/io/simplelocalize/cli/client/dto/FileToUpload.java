package io.simplelocalize.cli.client.dto;

import com.google.common.base.Objects;

import java.nio.file.Path;

public final class FileToUpload
{
  private final Path path;
  private final String language;

  public FileToUpload(Path path, String language)
  {
    this.path = path;
    this.language = language;
  }

  public static FileToUpload of(Path path, String language)
  {
    return new FileToUpload(path, language);
  }

  public Path getPath()
  {
    return path;
  }

  public String getLanguage()
  {
    return language;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FileToUpload that = (FileToUpload) o;
    return Objects.equal(path, that.path) && Objects.equal(language, that.language);
  }

  @Override
  public int hashCode()
  {
    return Objects.hashCode(path, language);
  }
}
