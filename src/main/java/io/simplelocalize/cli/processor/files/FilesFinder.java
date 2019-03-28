package io.simplelocalize.cli.processor.files;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FilesFinder {


  List<Path> findFiles(Path path) throws IOException;
}
