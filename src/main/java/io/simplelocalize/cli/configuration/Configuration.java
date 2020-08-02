package io.simplelocalize.cli.configuration;

import io.micronaut.core.annotation.Introspected;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Set;

@Introspected
public class Configuration {

  private String searchDir;
  private String uploadToken;
  private String projectType;
  private Set<String> ignoredKeys = Set.of();


  public Configuration() {
  }

  public Configuration(String searchDir, String uploadToken, String projectType, Set<String> ignoredKeys) {
    this.searchDir = searchDir;
    this.uploadToken = uploadToken;
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

  public String getUploadToken() {
    return uploadToken;
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

  public void setUploadToken(String uploadToken) {
    this.uploadToken = uploadToken;
  }

  public void setProjectType(String projectType) {
    this.projectType = projectType;
  }

  public void setIgnoredKeys(Set<String> ignoredKeys) {
    this.ignoredKeys = ignoredKeys;
  }
}
