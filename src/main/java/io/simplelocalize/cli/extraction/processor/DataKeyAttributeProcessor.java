package io.simplelocalize.cli.extraction.processor;

import io.simplelocalize.cli.extraction.ExtractionResult;
import io.simplelocalize.cli.extraction.files.BaseExtensionFilesFinder;
import io.simplelocalize.cli.io.FileContentReader;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DataKeyAttributeProcessor implements ExtractionProcessor
{

  private final Logger log = LoggerFactory.getLogger(DataKeyAttributeProcessor.class);

  @Override
  public List<ExtractionResult> process(Path inputPath, List<String> ignorePaths)
  {
    BaseExtensionFilesFinder filesFinder = new BaseExtensionFilesFinder(ignorePaths);
    List<Path> foundFiles = filesFinder.findFiles(inputPath);
    List<ExtractionResult> output = new ArrayList<>();
    for (Path file : foundFiles)
    {
      try
      {
        output.addAll(extractTranslationsFromFile(file));
        log.info("Found {} results in file: {}", output.size(), file);
      } catch (Exception e)
      {
        log.error("Error while processing file: {} ({})", file, e.getMessage());
      }
    }
    return output;
  }

  private List<ExtractionResult> extractTranslationsFromFile(Path file)
  {
    String fileContent = FileContentReader.tryReadContent(file);
    List<ExtractionResult> results = new ArrayList<>();

    Jsoup.parse(fileContent).select("[data-i18n-key]").forEach(element -> {
      String key = element.attr("data-i18n-key");
      String text = element.text();
      results.add(ExtractionResult.builder()
              .key(key)
              .translation(text)
              .filePath(file)
              .build());
    });
    return results;
  }


  @Override
  public String getExtractTypeSupport()
  {
    return "simplelocalize/data-i18n-key";
  }
}
