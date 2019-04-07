package io.simplelocalize.cli;

public enum ConfigProperty {

  SEARCH_DIR("search.dir", true),
  CLIENT_ID("client.id", true),
  CLIENT_SECRET("secret", true),
  PROJECT_TOKEN("project.token", true),
  PROJECT_TYPE("project.type", true);

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
