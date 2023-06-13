package io.simplelocalize.cli.client.dto.proxy;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.simplelocalize.cli.NativeProxy;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@CommandLine.Command(name = "config-er", mixinStandardHelpOptions = true)
@NativeProxy
public class ExportResponse
{
  @JsonProperty("files")
  private List<DownloadableFile> files;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExportResponse that = (ExportResponse) o;
    return files.equals(that.files) && additionalProperties.equals(that.additionalProperties);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(files, additionalProperties);
  }

  public List<DownloadableFile> getFiles()
  {
    return files;
  }

  public void setFiles(List<DownloadableFile> files)
  {
    this.files = files;
  }


  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties()
  {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value)
  {
    this.additionalProperties.put(name, value);
  }

}
