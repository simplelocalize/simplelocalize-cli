package io.simplelocalize.cli.processor;

import java.nio.file.Path;

public interface ProjectProcessor {

  ProcessResult process(Path searchDirectory);

  String getProjectTypeSupport();
}
