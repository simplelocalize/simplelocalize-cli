package io.simplelocalize.cli.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.TypeRef;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import io.simplelocalize.cli.client.SimpleLocalizeClient;
import io.simplelocalize.cli.client.dto.HostingResource;
import io.simplelocalize.cli.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class PullHostingCommand implements CliCommand
{
  private static final String BASE_URI_CDN = "https://cdn.simplelocalize.io";
  private static final Logger log = LoggerFactory.getLogger(PullHostingCommand.class);

  private final SimpleLocalizeClient client;
  private final Configuration configuration;

  private final ObjectMapper objectMapper;

  public PullHostingCommand(SimpleLocalizeClient client, Configuration configuration)
  {
    this.configuration = configuration;
    this.client = client;
    this.objectMapper = new ObjectMapper();
  }

  public void invoke() throws IOException, InterruptedException
  {
    String responseData = client.fetchProject();
    com.jayway.jsonpath.Configuration mappingConfiguration = com.jayway.jsonpath.Configuration.defaultConfiguration()
            .jsonProvider(new JacksonJsonProvider())
            .mappingProvider(new JacksonMappingProvider(objectMapper));
    DocumentContext json = JsonPath.parse(responseData, mappingConfiguration);

    String projectName = json.read("$.data.name", String.class);
    log.info("Project name: {}", projectName);

    String projectToken = json.read("$.data.projectToken", String.class);
    log.info("Project token: {}", projectToken);

    String environment = configuration.getEnvironment();
    log.info("Environment: {}", environment);

    // @formatter:off
    List<HostingResource> hostingResources = json.read("$.data.hostingResources", new TypeRef<>() {});
    // @formatter:on
    List<String> resourcePaths = hostingResources.stream()
            .filter(hostingResource -> hostingResource.getEnvironment().equals(environment))
            .map(HostingResource::getPath)
            .sorted()
            .collect(Collectors.toList());
    log.info("Found {} Translation Hosting resources", resourcePaths.size());
    String pullDirectory = configuration.getPullPath();

    for (String resourcePath : resourcePaths)
    {
      String downloadUrl = BASE_URI_CDN + "/" + resourcePath;
      String resourcePrefix = projectToken + "/_" + environment + "/";
      String plainResource = resourcePath.replace(resourcePrefix, "");
      Path savePath = Path.of(pullDirectory, plainResource + ".json");
      client.downloadFile(downloadUrl, savePath);
    }
    log.info("Downloaded {} resources from Translation Hosting", resourcePaths.size());

  }

}
