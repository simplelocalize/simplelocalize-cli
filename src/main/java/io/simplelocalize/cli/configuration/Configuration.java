package io.simplelocalize.cli.configuration;


import java.util.ArrayList;
import java.util.List;

public class Configuration
{

  private String baseUrl = "https://api.simplelocalize.io";
  private String apiKey;

  private String projectType;
  private String searchDir;
  private List<String> ignoreKeys = new ArrayList<>();

  private String uploadPath;
  private String uploadFormat;
  private List<String> uploadOptions = new ArrayList<>();

  private String downloadPath;
  private String downloadFormat;
  private List<String> downloadOptions = new ArrayList<>();

  private String customerId;

  private String languageKey;

  private String environment;

  private String pullPath;

  public String getSearchDir()
  {
    return searchDir;
  }

  public void setSearchDir(String searchDir)
  {
    this.searchDir = searchDir;
  }

  public String getApiKey()
  {
    return apiKey;
  }

  public void setApiKey(String apiKey)
  {
    this.apiKey = apiKey;
  }

  public String getProjectType()
  {
    return projectType;
  }

  public void setProjectType(String projectType)
  {
    this.projectType = projectType;
  }

  public List<String> getIgnoreKeys()
  {
    return ignoreKeys;
  }

  public void setIgnoreKeys(List<String> ignoreKeys)
  {
    this.ignoreKeys = ignoreKeys;
  }

  public String getUploadPath()
  {
    return uploadPath;
  }

  public void setUploadPath(String uploadPath)
  {
    this.uploadPath = uploadPath;
  }

  public String getUploadFormat()
  {
    return uploadFormat;
  }

  public void setUploadFormat(String uploadFormat)
  {
    this.uploadFormat = uploadFormat;
  }

  public String getDownloadPath()
  {
    return downloadPath;
  }

  public void setDownloadPath(String downloadPath)
  {
    this.downloadPath = downloadPath;
  }

  public String getDownloadFormat()
  {
    return downloadFormat;
  }

  public List<String> getDownloadOptions()
  {
    return downloadOptions;
  }

  public void setDownloadOptions(List<String> downloadOptions)
  {
    this.downloadOptions = downloadOptions;
  }

  public void setDownloadFormat(String downloadFormat)
  {
    this.downloadFormat = downloadFormat;
  }

  public String getLanguageKey()
  {
    return languageKey;
  }

  public void setLanguageKey(String languageKey)
  {
    this.languageKey = languageKey;
  }

  public List<String> getUploadOptions()
  {
    return uploadOptions;
  }

  public void setUploadOptions(List<String> uploadOptions)
  {
    this.uploadOptions = uploadOptions;
  }

  public String getCustomerId()
  {
    return customerId;
  }

  public void setCustomerId(String customerId)
  {
    this.customerId = customerId;
  }

  public String getBaseUrl()
  {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl)
  {
    this.baseUrl = baseUrl;
  }

  public String getEnvironment()
  {
    return environment;
  }

  public void setEnvironment(String environment)
  {
    this.environment = environment;
  }

  public String getPullPath()
  {
    return pullPath;
  }

  public void setPullPath(String pullPath)
  {
    this.pullPath = pullPath;
  }
}
