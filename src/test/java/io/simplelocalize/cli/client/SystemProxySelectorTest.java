package io.simplelocalize.cli.client;

import io.simplelocalize.cli.client.dto.ProxyConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.params.provider.Arguments.of;

class SystemProxySelectorTest
{
  private static Stream<Arguments> provideArguments()
  {
    return Stream.of(
            of("http://123.456.789.000", ProxyConfiguration.builder().host("123.456.789.000").port(80).username(null).password(null).build()),
            of("https://123.456.789.000", ProxyConfiguration.builder().host("123.456.789.000").port(443).username(null).password(null).build()),
            of("http://123.456.789.000:8080", ProxyConfiguration.builder().host("123.456.789.000").port(8080).username(null).password(null).build()),
            of("https://123.456.789.000:8080", ProxyConfiguration.builder().host("123.456.789.000").port(8080).username(null).password(null).build()),
            of("http://foo:bar@123.456.789.000", ProxyConfiguration.builder().host("123.456.789.000").port(80).username("foo").password("bar").build()),
            of("https://foo:bar@123.456.789.000", ProxyConfiguration.builder().host("123.456.789.000").port(443).username("foo").password("bar").build()),
            of("http://foo:bar@123.456.789.000:8080", ProxyConfiguration.builder().host("123.456.789.000").port(8080).username("foo").password("bar").build()),
            of("https://foo:bar@123.456.789.000:8080", ProxyConfiguration.builder().host("123.456.789.000").port(8080).username("foo").password("bar").build())
    );
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldReturnNullWhenNoValue(String givenInput)
  {
    //when
    ProxyConfiguration result = SystemProxySelector.getHttpProxyValueOrNull(givenInput);

    //then
    assertNull(result);
  }

  @ParameterizedTest
  @MethodSource("provideArguments")
  void shouldReturnProxyConfigWhenNoAuthAndNoPort(String given, ProxyConfiguration expected)
  {
    //when
    ProxyConfiguration result = SystemProxySelector.getHttpProxyValueOrNull(given);

    //then
    assertEquals(expected, result);
  }

  @Test
  void shouldNotPrintPasswordWhenToString()
  {
    //given
    ProxyConfiguration given = ProxyConfiguration.builder().host("123.456.789.000").port(8080).username("foo").password("bar").build();

    //when
    String result = given.toString();

    //then
    assertEquals("host='123.456.789.000', port=8080, username='foo', password='***'", result);
  }

  @Test
  void shouldPrintNullPasswordWhenToString()
  {
    //given
    ProxyConfiguration given = ProxyConfiguration.builder().host("123.456.789.000").port(8080).build();

    //when
    String result = given.toString();

    //then
    assertEquals("host='123.456.789.000', port=8080, username='null', password='null'", result);
  }

}
