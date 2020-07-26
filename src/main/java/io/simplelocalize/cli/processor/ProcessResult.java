package io.simplelocalize.cli.processor;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class ProcessResult {

  private Set<String> keys;
  private List<Path> processedFiles;

  public ProcessResult() {
  }

  public ProcessResult(Set<String> keys, List<Path> processedFiles) {
    this.keys = keys;
    this.processedFiles = processedFiles;
  }

  public static ProcessResult of(Set<String> keys, List<Path> processedFiles) {
    return new ProcessResult(keys, processedFiles);
  }

  public Set<String> getKeys() {
    return Sets.newHashSet(keys);
  }

  public ProcessResult setKeys(Set<String> keys) {
    this.keys = keys;
    return this;
  }

  public List<Path> getProcessedFiles() {
    return Lists.newArrayList(processedFiles);
  }

  public ProcessResult setProcessedFiles(List<Path> processedFiles) {
    this.processedFiles = processedFiles;
    return this;
  }
}
