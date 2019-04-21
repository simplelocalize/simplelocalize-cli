package io.simplelocalize.cli.client.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
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
}
