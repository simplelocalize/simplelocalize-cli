package io.simplelocalize.cli.processor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProcessResult {

  private Set<String> keys;
  private List<Path> processedFiles;
}
