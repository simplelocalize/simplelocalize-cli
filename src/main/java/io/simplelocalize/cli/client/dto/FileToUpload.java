package io.simplelocalize.cli.client.dto;


import java.nio.file.Path;
import java.util.Objects;

public class FileToUpload
{
  private Path path;
  private String language;
  private String namespace;


  public Path getPath()
  {
    return path;
  }

  public String getLanguage()
  {
    return language;
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
    FileToUpload that = (FileToUpload) o;
    return Objects.equals(path, that.path) && Objects.equals(language, that.language) && Objects.equals(namespace, that.namespace);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(path, language, namespace);
  }

  public static final class FileToUploadBuilder
  {
    private Path path;
    private String language;
    private String namespace;

    private FileToUploadBuilder()
    {
    }

    public static FileToUploadBuilder aFileToUpload()
    {
      return new FileToUploadBuilder();
    }

    public FileToUploadBuilder withPath(Path path)
    {
      this.path = path;
      return this;
    }

    public FileToUploadBuilder withLanguage(String language)
    {
      this.language = language;
      return this;
    }

    public FileToUploadBuilder withNamespace(String namespace)
    {
      this.namespace = namespace;
      return this;
    }

    public FileToUpload build()
    {
      FileToUpload fileToUpload = new FileToUpload();
      fileToUpload.namespace = this.namespace;
      fileToUpload.path = this.path;
      fileToUpload.language = this.language;
      return fileToUpload;
    }

    @Override
    public String toString()
    {
      return "FileToUploadBuilder{" +
             "path=" + path +
             ", language='" + language + '\'' +
             ", namespace='" + namespace + '\'' +
             '}';
    }
  }
}
