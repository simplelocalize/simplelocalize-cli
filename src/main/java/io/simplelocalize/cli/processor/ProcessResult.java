package io.simplelocalize.cli.processor;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Data
@AllArgsConstructor(staticName = "of")
public class ProcessResult {

  private Set<String> keys;
  private List<Path> processedFiles;

  public Set<String> getKeys() {
    return Sets.newHashSet(keys);
  }

  public List<Path> getProcessedFiles() {
    return Lists.newArrayList(processedFiles);
  }
}
