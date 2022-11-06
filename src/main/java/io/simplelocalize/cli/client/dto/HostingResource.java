package io.simplelocalize.cli.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import java.util.Objects;

@Introspected
public final class HostingResource
{
  private final String path;
  private final String environment;

  public HostingResource(@JsonProperty("path") String path, @JsonProperty("environment") String environment)
  {
    this.path = path;
    this.environment = environment;
  }

  public String getPath()
  {
    return path;
  }

  public String getEnvironment()
  {
    return environment;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    HostingResource that = (HostingResource) o;
    return Objects.equals(path, that.path) && Objects.equals(environment, that.environment);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(path, environment);
  }
}
