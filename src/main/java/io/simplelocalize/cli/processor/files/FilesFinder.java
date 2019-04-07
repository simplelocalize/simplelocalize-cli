package io.simplelocalize.cli.processor.files;

import java.nio.file.Path;
import java.util.List;

public interface FilesFinder {

  List<Path> findFilesToProcess(Path path);
}

