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

  /**
   * Common configuration
   */
  @Builder.Default
  private String baseUrl = "https://api.simplelocalize.io";
  private String apiKey;

  /**
   * Extract command
   */
  private String projectType;

  @Builder.Default
  private String searchDir = "./";

  @Builder.Default
  private String output = "extraction.json";

  @Builder.Default
  private List<String> ignoreKeys = new ArrayList<>();

  @Builder.Default
  private List<String> ignorePaths = new ArrayList<>();

  /**
   * Upload command
   */
  private String uploadPath;
  private String uploadFormat;
  private String uploadLanguageKey;
  private String uploadCustomerId;
  private String uploadNamespace;

  @Builder.Default
  private List<String> uploadOptions = new ArrayList<>();

  @Builder.Default
  private Boolean dryRun = false;

  @Builder.Default
  private Boolean preview = false;

  /**
   * Download command
   */
  private String downloadPath;
  private String downloadFormat;
  private String downloadSort;

  @Builder.Default
  private List<String> downloadOptions = new ArrayList<>();

  @Builder.Default
  private List<String> downloadLanguageKeys = new ArrayList<>();

  private String downloadCustomerId;
  private String downloadNamespace;

  /**
   * Auto-translate command
   */
  private List<String> autoTranslateLanguageKeys = new ArrayList<>();
  @Builder.Default
  private List<String> autoTranslateOptions = new ArrayList<>();

  /**
   * Pull & Publish command
   */
  private String environment;

  private String pullPath;

  private String filterRegex;

  public static Configuration defaultConfiguration()
  {
    return Configuration.builder().build();
  }
}
