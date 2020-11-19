package io.simplelocalize.cli.configuration;

import io.micronaut.core.annotation.Introspected;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Set;

@Introspected
public class Configuration {

  private String searchDir;
  private String apiKey;
  private String uploadToken;
  private String projectType;
  private Set<String> ignoredKeys = Set.of();


  public Configuration() {
  }

  public Configuration(String searchDir, String uploadToken, String apiKey, String projectType, Set<String> ignoredKeys) {
    this.searchDir = searchDir;

    if(StringUtils.isEmpty(apiKey)){
      this.apiKey = uploadToken;
    } else {
      this.apiKey = apiKey;
    }
    this.projectType = projectType;
    this.ignoredKeys = ignoredKeys;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    Configuration that = (Configuration) o;

    return new EqualsBuilder()
            .append(searchDir, that.searchDir)
            .append(apiKey, that.apiKey)
            .append(projectType, that.projectType)
            .append(ignoredKeys, that.ignoredKeys)
            .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(searchDir)
            .append(apiKey)
            .append(projectType)
            .append(ignoredKeys)
            .toHashCode();
  }

  public String getSearchDir() {
    return searchDir;
  }

  public String getApiKey() {
    return apiKey;
  }

  public String getProjectType() {
    return projectType;
  }

  public Set<String> getIgnoredKeys() {
    return ignoredKeys;
  }

  public void setSearchDir(String searchDir) {
    this.searchDir = searchDir;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public void setProjectType(String projectType) {
    this.projectType = projectType;
  }

  public void setIgnoredKeys(Set<String> ignoredKeys) {
    this.ignoredKeys = ignoredKeys;
  }

  public String getUploadToken() {
    return uploadToken;
  }

  public void setUploadToken(String uploadToken) {
    this.uploadToken = uploadToken;
  }
}
