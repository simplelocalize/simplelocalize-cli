package io.simplelocalize.cli.extraction.processor;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.simplelocalize.cli.extraction.ExtractionResult;
import io.simplelocalize.cli.extraction.files.GenericExtensionFilesFinder;
import io.simplelocalize.cli.extraction.keys.SwiftStringKeyExtractor;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class iOSProcessor implements ExtractionProcessor
{


  @Override
  public ExtractionResult process(Path searchDirectory)
  {
    Set<String> foundKeys = Sets.newLinkedHashSet();
    List<Path> foundFiles = Lists.newArrayList();

    GenericExtensionFilesFinder filesFinder = new GenericExtensionFilesFinder();
    List<Path> swiftFiles = filesFinder.findFilesToProcess(searchDirectory, ".swift");
    foundFiles.addAll(swiftFiles);

    SwiftStringKeyExtractor extractor = new SwiftStringKeyExtractor();
    for (Path path : swiftFiles)
    {
      Set<String> chunk = extractor.extractKeysFromFile(path);
      foundKeys.addAll(chunk);
    }

    return ExtractionResult.of(foundKeys, foundFiles);
  }

  @Override
  public String getProjectTypeSupport()
  {
    return "apple/ios-macos";
  }
}
