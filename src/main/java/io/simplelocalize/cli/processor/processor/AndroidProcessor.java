package io.simplelocalize.cli.processor.processor;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.simplelocalize.cli.processor.ProcessResult;
import io.simplelocalize.cli.processor.ProjectProcessor;
import io.simplelocalize.cli.processor.files.GenericExtensionFilesFinder;
import io.simplelocalize.cli.processor.keys.AndroidRStringKeyExtractor;
import io.simplelocalize.cli.processor.keys.AndroidXmlKeysExtractor;
import io.simplelocalize.cli.util.ListsUtil;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class AndroidProcessor implements ProjectProcessor {


  @Override
  public ProcessResult process(Path searchDirectory) {

    Set<String> foundKeys = Sets.newLinkedHashSet();
    List<Path> foundFiles = Lists.newArrayList();

    GenericExtensionFilesFinder filesFinder = new GenericExtensionFilesFinder();
    List<Path> javaFiles = filesFinder.findFilesToProcess(searchDirectory, ".java");
    List<Path> kotlinFiles = filesFinder.findFilesToProcess(searchDirectory, ".kt");
    List<Path> combinedLists = ListsUtil.combine(javaFiles, kotlinFiles);
    foundFiles.addAll(combinedLists);

    AndroidRStringKeyExtractor javaKeysExtractor = new AndroidRStringKeyExtractor();
    for (Path javaFile : combinedLists) {
      Set<String> chunk = javaKeysExtractor.extractKeysFromFile(javaFile);
      foundKeys.addAll(chunk);
    }

    List<Path> xmlFiles = filesFinder.findFilesToProcess(searchDirectory, ".xml");
    foundFiles.addAll(xmlFiles);
    AndroidXmlKeysExtractor xmlKeysExtractor = new AndroidXmlKeysExtractor();
    for (Path xmlFile : xmlFiles) {
      Set<String> chunk = xmlKeysExtractor.extractKeysFromFile(xmlFile);
      foundKeys.addAll(chunk);
    }

    return ProcessResult.of(foundKeys, foundFiles);
  }

  @Override
  public String getProjectTypeSupport() {
    return "google/android";
  }
}
