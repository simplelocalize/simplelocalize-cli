package io.simplelocalize.cli.client.dto;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class UploadRequest
{

  private Path path;
  private String languageKey;
  private String format;
  private List<String> options;
  private String namespace;

  private String customerId;

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

  public String getNamespace()
  {
    return namespace;
  }

  public String getCustomerId()
  {
    return customerId;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UploadRequest that = (UploadRequest) o;
    return Objects.equals(path, that.path) &&
            Objects.equals(languageKey, that.languageKey) &&
            Objects.equals(format, that.format) &&
            Objects.equals(options, that.options) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(namespace, that.namespace);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(path, languageKey, format, options, customerId, namespace);
  }


  public static final class UploadFileRequestBuilder
  {
    private Path uploadPath;
    private String languageKey;
    private String uploadFormat;
    private List<String> uploadOptions;
    private String namespace;
    private String customerId;

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

    public UploadFileRequestBuilder withNamespace(String namespace)
    {
      this.namespace = namespace;
      return this;
    }

    public UploadFileRequestBuilder withCustomerId(String customerId)
    {
      this.customerId = customerId;
      return this;
    }


    public UploadRequest build()
    {
      UploadRequest uploadRequest = new UploadRequest();
      uploadRequest.languageKey = this.languageKey;
      uploadRequest.format = this.uploadFormat;
      uploadRequest.options = this.uploadOptions;
      uploadRequest.path = this.uploadPath;
      uploadRequest.namespace = this.namespace;
      uploadRequest.customerId = this.customerId;
      return uploadRequest;
    }
  }
}
