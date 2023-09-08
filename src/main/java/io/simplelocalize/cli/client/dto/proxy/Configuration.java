package io.simplelocalize.cli.client.dto.proxy;


import io.simplelocalize.cli.NativeProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@NativeProxy
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
  private List<String> uploadFilesExclude = new ArrayList<>();
  private List<String> uploadFilesInclude = new ArrayList<>();

  private Boolean dryRun = false;

  private String downloadPath;
  private String downloadFormat;
  private String downloadSort;
  private List<String> downloadOptions = new ArrayList<>();
  private List<String> downloadFilesExclude = new ArrayList<>();
  private List<String> downloadFilesInclude = new ArrayList<>();

  private String customerId;

  private String languageKey;

  private AutoTranslationConfiguration autoTranslation = new AutoTranslationConfiguration();

  private String environment;

  private String pullPath;

  private String filterRegex;


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

  public Boolean getDryRun()
  {
    return dryRun;
  }

  public void setDryRun(Boolean dryRun)
  {
    this.dryRun = dryRun;
  }

  public AutoTranslationConfiguration getAutoTranslation()
  {
    return autoTranslation;
  }

  public void setAutoTranslation(AutoTranslationConfiguration autoTranslation)
  {
    this.autoTranslation = autoTranslation;
  }

  public String getFilterRegex()
  {
    return filterRegex;
  }

  public void setFilterRegex(String filterRegex)
  {
    this.filterRegex = filterRegex;
  }

  public List<String> getUploadFilesInclude()
  {
    return uploadFilesInclude;
  }

  public void setUploadFilesInclude(List<String> uploadFilesInclude)
  {
    this.uploadFilesInclude = uploadFilesInclude;
  }

  public List<String> getUploadFilesExclude()
  {
    return uploadFilesExclude;
  }

  public void setUploadFilesExclude(List<String> uploadFilesExclude)
  {
    this.uploadFilesExclude = uploadFilesExclude;
  }

  public List<String> getDownloadFilesExclude()
  {
    return downloadFilesExclude;
  }

  public void setDownloadFilesExclude(List<String> downloadFilesExclude)
  {
    this.downloadFilesExclude = downloadFilesExclude;
  }

  public List<String> getDownloadFilesInclude()
  {
    return downloadFilesInclude;
  }

  public void setDownloadFilesInclude(List<String> downloadFilesInclude)
  {
    this.downloadFilesInclude = downloadFilesInclude;
  }

  public String getDownloadSort()
  {
    return downloadSort;
  }

  public Configuration setDownloadSort(String downloadSort)
  {
    this.downloadSort = downloadSort;
    return this;
  }

  @Override
  public String toString()
  {
    return new StringJoiner(", ", Configuration.class.getSimpleName() + "[", "]")
            .add("baseUrl='" + baseUrl + "'")
            .add("apiKey='***'")
            .add("projectType='" + projectType + "'")
            .add("searchDir='" + searchDir + "'")
            .add("ignoreKeys=" + ignoreKeys)
            .add("uploadPath='" + uploadPath + "'")
            .add("uploadFormat='" + uploadFormat + "'")
            .add("uploadOptions=" + uploadOptions)
            .add("uploadFilesExclude=" + uploadFilesExclude)
            .add("uploadFilesInclude=" + uploadFilesInclude)
            .add("dryRun=" + dryRun)
            .add("downloadPath='" + downloadPath + "'")
            .add("downloadFormat='" + downloadFormat + "'")
            .add("downloadSort='" + downloadSort + "'")
            .add("downloadOptions=" + downloadOptions)
            .add("downloadFilesExclude=" + downloadFilesExclude)
            .add("downloadFilesInclude=" + downloadFilesInclude)
            .add("customerId='" + customerId + "'")
            .add("languageKey='" + languageKey + "'")
            .add("autoTranslation=" + autoTranslation)
            .add("environment='" + environment + "'")
            .add("pullPath='" + pullPath + "'")
            .add("filterRegex='" + filterRegex + "'")
            .toString();
  }
}
