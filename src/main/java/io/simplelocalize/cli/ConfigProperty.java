package io.simplelocalize.cli;

public enum ConfigProperty {

  SEARCH_DIR("searchDir", true),
  CLIENT_ID("clientId", true),
  CLIENT_SECRET("secret", true),
  PROJECT_HASH("projectHash", true);

  private String key;
  private boolean required;

  ConfigProperty(String key, boolean required) {
    this.key = key;
    this.required = required;
  }

  public String getKey() {
    return key;
  }

  public boolean isRequired() {
    return required;
  }
}
