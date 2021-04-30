package io.simplelocalize.cli.extraction.files;

import java.nio.file.Path;
import java.util.List;

public interface FilesFinder {

  List<Path> findFilesToProcess(Path path);
}

