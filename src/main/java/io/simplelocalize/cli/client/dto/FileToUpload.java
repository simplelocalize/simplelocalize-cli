package io.simplelocalize.cli.client.dto;


import java.nio.file.Path;

public record FileToUpload(Path path, String language, String namespace)
{

  public static final class FileToUploadBuilder
  {
    private Path path;
    private String language;
    private String namespace;

    private FileToUploadBuilder()
    {
    }

    public static FileToUploadBuilder Builder()
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
      return new FileToUpload(
              this.path,
              this.language,
              this.namespace
      );
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
