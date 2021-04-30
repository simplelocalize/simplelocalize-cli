package io.simplelocalize.cli.extraction;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public final class ExtractionResult
{

  private final Set<String> keys;
  private final List<Path> processedFiles;

  public ExtractionResult(Set<String> keys, List<Path> processedFiles)
  {
    this.keys = keys;
    this.processedFiles = processedFiles;
  }

  public static ExtractionResult of(Set<String> keys, List<Path> processedFiles)
  {
    return new ExtractionResult(keys, processedFiles);
  }

  public Set<String> getKeys()
  {
    return Sets.newHashSet(keys);
  }

  public List<Path> getProcessedFiles()
  {
    return Lists.newArrayList(processedFiles);
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExtractionResult that = (ExtractionResult) o;
    return Objects.equal(keys, that.keys) && Objects.equal(processedFiles, that.processedFiles);
  }

  @Override
  public int hashCode()
  {
    return Objects.hashCode(keys, processedFiles);
  }
}
