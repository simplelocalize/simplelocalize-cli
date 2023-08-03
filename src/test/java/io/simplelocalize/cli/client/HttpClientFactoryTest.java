package io.simplelocalize.cli.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import java.net.http.HttpClient;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SystemStubsExtension.class)
class HttpClientFactoryTest
{

  @SystemStub
  private EnvironmentVariables environmentVariables;


  @Test
  void createHttpClient()
  {
    //when
    HttpClient result = HttpClientFactory.createHttpClient();

    //then
    assertThat(result).isNotNull();
    assertThat(result.connectTimeout().get()).isEqualTo(Duration.of(5, ChronoUnit.MINUTES));
    assertThat(result.version()).isEqualTo(HttpClient.Version.HTTP_2);
    assertThat(result.followRedirects()).isEqualTo(HttpClient.Redirect.NEVER);
    assertThat(result.proxy()).isEmpty();
    assertThat(result.authenticator()).isEmpty();
    assertThat(result.cookieHandler()).isEmpty();
  }

  @Test
  void createHttpClientWithUnauthenticatedProxy()
  {
    //given
    environmentVariables.set("http_proxy", "http://proxy:8080");

    //when
    HttpClient result = HttpClientFactory.createHttpClient();

    //then
    assertThat(result).isNotNull();
    assertThat(result.connectTimeout().get()).isEqualTo(Duration.of(5, ChronoUnit.MINUTES));
    assertThat(result.version()).isEqualTo(HttpClient.Version.HTTP_2);
    assertThat(result.followRedirects()).isEqualTo(HttpClient.Redirect.NEVER);

    assertThat(result.proxy()).isPresent();

    assertThat(result.authenticator()).isEmpty();
    assertThat(result.cookieHandler()).isEmpty();
  }

  @Test
  void createHttpClientWithAuthenticatedProxy()
  {
    //given
    environmentVariables.set("http_proxy", "http://foo:bar@proxy:8080");

    //when
    HttpClient result = HttpClientFactory.createHttpClient();

    //then
    assertThat(result).isNotNull();
    assertThat(result.connectTimeout().get()).isEqualTo(Duration.of(5, ChronoUnit.MINUTES));
    assertThat(result.version()).isEqualTo(HttpClient.Version.HTTP_2);
    assertThat(result.followRedirects()).isEqualTo(HttpClient.Redirect.NEVER);

    assertThat(result.proxy()).isPresent();
    assertThat(result.authenticator()).isPresent();

    assertThat(result.cookieHandler()).isEmpty();
  }
}
