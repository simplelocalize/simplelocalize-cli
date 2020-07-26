package io.simplelocalize.cli.client.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import io.micronaut.core.annotation.Introspected;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Map;

@Introspected
public class ImportKey {
  private String key;

  public ImportKey(String key) {
    this.key = key;
  }

  private Map<String, Object> additional = new HashMap<>();

  @JsonAnyGetter
  public Map<String, Object> getAdditional() {
    return additional;
  }

  @JsonAnySetter
  public void setAdditional(String key, String value) {
    if (additional == null) {
      additional = new HashMap<>();
    }
    this.additional.put(key, value);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;

    if (o == null || getClass() != o.getClass()) return false;

    ImportKey importKey = (ImportKey) o;

    return new EqualsBuilder()
            .append(key, importKey.key)
            .append(additional, importKey.additional)
            .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(key)
            .append(additional)
            .toHashCode();
  }

  public String getKey() {
    return key;
  }

  public ImportKey setKey(String key) {
    this.key = key;
    return this;
  }

  public ImportKey setAdditional(Map<String, Object> additional) {
    this.additional = additional;
    return this;
  }
}
