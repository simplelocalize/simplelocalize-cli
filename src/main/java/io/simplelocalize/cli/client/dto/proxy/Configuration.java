package io.simplelocalize.cli.client.dto.proxy;


import io.simplelocalize.cli.NativeProxy;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NativeProxy
@Builder
@Data
@ToString(exclude = "apiKey")
@RequiredArgsConstructor
@AllArgsConstructor
public class Configuration
{

  @Builder.Default
  private String baseUrl = "https://api.simplelocalize.io";
  private String apiKey;

  private String projectType;

  @Builder.Default
  private String searchDir = "./";

  @Builder.Default
  private String output = "extraction.json";

  @Builder.Default
  private List<String> ignoreKeys = new ArrayList<>();

  @Builder.Default
  private List<String> ignorePaths = new ArrayList<>();

  private String uploadPath;
  private String uploadFormat;

  @Builder.Default
  private List<String> uploadOptions = new ArrayList<>();

  @Builder.Default
  private List<String> uploadFilesExclude = new ArrayList<>();

  @Builder.Default
  private List<String> uploadFilesInclude = new ArrayList<>();

  @Builder.Default
  private Boolean dryRun = false;

  private String downloadPath;
  private String downloadFormat;
  private String downloadSort;

  @Builder.Default
  private List<String> downloadOptions = new ArrayList<>();

  @Builder.Default
  private List<String> downloadFilesExclude = new ArrayList<>();

  @Builder.Default
  private List<String> downloadFilesInclude = new ArrayList<>();

  private String customerId;

  private String namespace;

  private String languageKey;

  private AutoTranslationConfiguration autoTranslation = AutoTranslationConfiguration.defaultConfiguration();

  private String environment;

  private String pullPath;

  private String filterRegex;

  public static Configuration defaultConfiguration()
  {
    return Configuration.builder().build();
  }
}
