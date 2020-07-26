package io.simplelocalize.cli.configuration;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Set;

public class Configuration {

  private String searchDir;
  private String uploadToken;
  private String projectType;
  private Set<String> ignoredKeys = Set.of();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    Configuration that = (Configuration) o;

    return new EqualsBuilder()
            .append(searchDir, that.searchDir)
            .append(uploadToken, that.uploadToken)
            .append(projectType, that.projectType)
            .append(ignoredKeys, that.ignoredKeys)
            .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(searchDir)
            .append(uploadToken)
            .append(projectType)
            .append(ignoredKeys)
            .toHashCode();
  }

  public String getSearchDir() {
    return searchDir;
  }

  public Configuration setSearchDir(String searchDir) {
    this.searchDir = searchDir;
    return this;
  }

  public String getUploadToken() {
    return uploadToken;
  }

  public Configuration setUploadToken(String uploadToken) {
    this.uploadToken = uploadToken;
    return this;
  }

  public String getProjectType() {
    return projectType;
  }

  public Configuration setProjectType(String projectType) {
    this.projectType = projectType;
    return this;
  }

  public Set<String> getIgnoredKeys() {
    return ignoredKeys;
  }

  public Configuration setIgnoredKeys(Set<String> ignoredKeys) {
    this.ignoredKeys = ignoredKeys;
    return this;
  }
}
