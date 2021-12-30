package io.simplelocalize.cli.client.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportResponse
{
  private List<DownloadableFile> files;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<String, Object>();

  public List<DownloadableFile> getFiles()
  {
    return files;
  }

  public void setFiles(List<DownloadableFile> files)
  {
    this.files = files;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExportResponse that = (ExportResponse) o;
    return Objects.equal(files, that.files);
  }

  @Override
  public int hashCode()
  {
    return Objects.hashCode(files);
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
