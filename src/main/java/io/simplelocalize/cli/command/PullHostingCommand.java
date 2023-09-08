package io.simplelocalize.cli.command;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.TypeRef;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.proxy.Configuration;
import io.simplelocalize.cli.client.dto.proxy.HostingResource;
import io.simplelocalize.cli.io.JsonReader;
import io.simplelocalize.cli.util.EnvironmentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public class PullHostingCommand implements CliCommand
{
  private static final String BASE_URI_CDN = "https://cdn.simplelocalize.io";
  private static final Logger log = LoggerFactory.getLogger(PullHostingCommand.class);

  private final SimpleLocalizeClient client;
  private final Configuration configuration;
  private final JsonReader jsonReader;


  public PullHostingCommand(SimpleLocalizeClient client, Configuration configuration)
  {
    this.configuration = configuration;
    this.client = client;
    this.jsonReader = new JsonReader();
  }

  public void invoke() throws IOException, InterruptedException
  {
    String responseData = client.fetchProject();
    DocumentContext json = jsonReader.read(responseData);

    String projectName = json.read("$.data.name", String.class);
    log.info("Project name: {}", projectName);

    String projectToken = json.read("$.data.projectToken", String.class);
    log.info("Project token: {}", projectToken);

    String environment = EnvironmentUtils.convertDefaultEnvironmentKeyFromPreviousCliVersionsToV3IfNeeded(configuration.getEnvironment());
    log.info("Environment: {}", environment);

    String filterRegex = configuration.getFilterRegex();
    log.info("Filter regex: {}", filterRegex);
    Pattern filterPattern = null;
    if (filterRegex != null)
    {
      filterPattern = Pattern.compile(filterRegex);
    }

    // @formatter:off
    List<HostingResource> hostingResources = json.read("$.data.hostingResources", new TypeRef<>() {});
    // @formatter:on
    List<String> resourcePaths = hostingResources.stream()
            .filter(hostingResource -> hostingResource.key().equals(environment))
            .map(HostingResource::path)
            .sorted()
            .toList();
    log.info("Found {} Translation Hosting resources", resourcePaths.size());
    String pullDirectory = configuration.getPullPath();

    int numberOfDownloadedResources = 0;
    for (String resourcePath : resourcePaths)
    {
      String downloadUrl = BASE_URI_CDN + "/" + resourcePath;
      String resourcePrefix = projectToken + "/" + environment + "/";
      String plainResource = resourcePath.replace(resourcePrefix, "");
      String filePath = plainResource + ".json";
      if (filterPattern != null)
      {
        boolean matches = filterPattern.matcher(filePath).matches();
        if (!matches)
        {
          log.info("Skipping resource '{}' because it does not match the '{}' filter regex", plainResource, filterRegex);
          continue;
        }
      }

      Path savePath = Path.of(pullDirectory, filePath);
      client.downloadFile(downloadUrl, savePath);
      numberOfDownloadedResources++;
    }
    log.info("Downloaded {} Translation Hosting resources", numberOfDownloadedResources);

  }

}
