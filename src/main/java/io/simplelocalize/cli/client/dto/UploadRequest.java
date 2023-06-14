package io.simplelocalize.cli.client.dto;

import java.nio.file.Path;
import java.util.List;

public record UploadRequest(
        Path path,
        String languageKey,
        String format,
        List<String> options,
        String namespace,
        String customerId
)
{

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

    public static UploadFileRequestBuilder Builder()
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
      return new UploadRequest(
              this.uploadPath,
              this.languageKey,
              this.uploadFormat,
              this.uploadOptions,
              this.namespace,
              this.customerId
      );
    }
  }
}
