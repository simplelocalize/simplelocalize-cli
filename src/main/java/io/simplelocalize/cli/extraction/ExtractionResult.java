package io.simplelocalize.cli.extraction;


import java.nio.file.Path;
import java.util.*;

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
    return new HashSet<>(keys);
  }

  public List<Path> getProcessedFiles()
  {
    return new ArrayList<>(processedFiles);
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ExtractionResult that = (ExtractionResult) o;
    return keys.equals(that.keys) && processedFiles.equals(that.processedFiles);
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(keys, processedFiles);
  }
}
