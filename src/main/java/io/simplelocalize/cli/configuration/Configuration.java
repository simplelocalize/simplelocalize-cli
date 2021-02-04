package io.simplelocalize.cli.configuration;

import io.micronaut.core.annotation.Introspected;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.nio.file.Path;
import java.util.Set;

@Introspected
public class Configuration {

  private String apiKey;
  private String uploadToken;

  private String projectType;
  private String searchDir;
  private Set<String> ignoreKeys = Set.of();

  private Path uploadPath;
  private String uploadFormat;
  private String uploadLanguageKey;
  private String uploadOptions;

  private Path downloadPath;
  private String downloadFormat;

  private String profile;

  public Configuration()
  {
  }

  public Configuration(String searchDir, String uploadToken, String apiKey, String projectType, Path uploadPath, String uploadFormat, String uploadLanguageKey, String uploadOptions, Set<String> ignoreKeys, String profile, Path downloadPath, String downloadFormat)
  {
    this.searchDir = searchDir;

    this.projectType = projectType;
    this.ignoreKeys = ignoreKeys;

    this.uploadPath = uploadPath;
    this.uploadFormat = uploadFormat;
    this.uploadLanguageKey = uploadLanguageKey;
    this.uploadOptions = uploadOptions;

    this.downloadPath = downloadPath;
    this.downloadFormat = downloadFormat;

    if (StringUtils.isEmpty(apiKey))
    {
      this.apiKey = uploadToken;
    } else
    {
      this.apiKey = apiKey;
    }
    this.profile = profile;

  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    Configuration that = (Configuration) o;

    return new EqualsBuilder()
            .append(searchDir, that.searchDir)
            .append(apiKey, that.apiKey)
            .append(profile, that.profile)
            .append(projectType, that.projectType)
            .append(ignoreKeys, that.ignoreKeys)
            .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(searchDir)
            .append(apiKey)
            .append(profile)
            .append(projectType)
            .append(ignoreKeys)
            .toHashCode();
  }

  public String getSearchDir() {
    return searchDir;
  }

  public void setSearchDir(String searchDir) {
    this.searchDir = searchDir;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getProjectType() {
    return projectType;
  }

  public void setProjectType(String projectType) {
    this.projectType = projectType;
  }

  public Set<String> getIgnoreKeys() {
    return ignoreKeys;
  }

  public void setIgnoreKeys(Set<String> ignoreKeys) {
    this.ignoreKeys = ignoreKeys;
  }

  public String getUploadToken() {
    return uploadToken;
  }

  public void setUploadToken(String uploadToken) {
    this.uploadToken = uploadToken;
  }

  public String getProfile() {
    return profile;
  }

  public void setProfile(String profile) {
    this.profile = profile;
  }

  public Path getUploadPath()
  {
    return uploadPath;
  }

  public void setUploadPath(Path uploadPath)
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

  public Path getDownloadPath()
  {
    return downloadPath;
  }

  public void setDownloadPath(Path downloadPath)
  {
    this.downloadPath = downloadPath;
  }

  public String getDownloadFormat()
  {
    return downloadFormat;
  }

  public void setDownloadFormat(String downloadFormat)
  {
    this.downloadFormat = downloadFormat;
  }

  public String getUploadLanguageKey()
  {
    return uploadLanguageKey;
  }

  public void setUploadLanguageKey(String uploadLanguageKey)
  {
    this.uploadLanguageKey = uploadLanguageKey;
  }

  public String getUploadOptions()
  {
    return uploadOptions;
  }

  public void setUploadOptions(String uploadOptions)
  {
    this.uploadOptions = uploadOptions;
  }
}
