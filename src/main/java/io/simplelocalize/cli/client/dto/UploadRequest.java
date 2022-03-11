package io.simplelocalize.cli.client.dto;

import java.nio.file.Path;
import java.util.List;

public class UploadRequest
{

  private Path path;
  private String languageKey;
  private String format;
  private List<String> options;
  private String relativePath;

  public Path getPath()
  {
    return path;
  }

  public String getLanguageKey()
  {
    return languageKey;
  }

  public String getFormat()
  {
    return format;
  }

  public List<String> getOptions()
  {
    return options;
  }

  public String getRelativePath()
  {
    return relativePath;
  }

  public static final class UploadFileRequestBuilder
  {
    private Path uploadPath;
    private String languageKey;
    private String uploadFormat;
    private List<String> uploadOptions;
    private String relativePath;

    private UploadFileRequestBuilder()
    {
    }

    public static UploadFileRequestBuilder anUploadFileRequest()
    {
      return new UploadFileRequestBuilder();
    }

    public UploadFileRequestBuilder withPath(Path uploadPath)
    {
      this.uploadPath = uploadPath;
      return this;
    }

    public UploadFileRequestBuilder withLanguageKey(String languageKey)
    {
      this.languageKey = languageKey;
      return this;
    }

    public UploadFileRequestBuilder withFormat(String uploadFormat)
    {
      this.uploadFormat = uploadFormat;
      return this;
    }

    public UploadFileRequestBuilder withOptions(List<String> uploadOptions)
    {
      this.uploadOptions = uploadOptions;
      return this;
    }

    public UploadFileRequestBuilder withRelativePath(String relativePath)
    {
      this.relativePath = relativePath;
      return this;
    }

    public UploadRequest build()
    {
      UploadRequest uploadRequest = new UploadRequest();
      uploadRequest.languageKey = this.languageKey;
      uploadRequest.format = this.uploadFormat;
      uploadRequest.relativePath = this.relativePath;
      uploadRequest.options = this.uploadOptions;
      uploadRequest.path = this.uploadPath;
      return uploadRequest;
    }
  }
}
