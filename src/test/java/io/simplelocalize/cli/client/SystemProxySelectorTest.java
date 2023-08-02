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
            of("http://123.456.789.000", new ProxyConfiguration().setHost("123.456.789.000").setPort(80).setUsername(null).setPassword(null)),
            of("http://123.456.789.000:8080", new ProxyConfiguration().setHost("123.456.789.000").setPort(8080).setUsername(null).setPassword(null)),
            of("http://foo:bar@123.456.789.000", new ProxyConfiguration().setHost("123.456.789.000").setPort(80).setUsername("foo").setPassword("bar")),
            of("http://foo:bar@123.456.789.000:8080", new ProxyConfiguration().setHost("123.456.789.000").setPort(8080).setUsername("foo").setPassword("bar"))
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
    assertEquals(result, expected);
  }

  @Test
  void shouldNotPrintPasswordWhenToString()
  {
    //given
    ProxyConfiguration given = new ProxyConfiguration().setHost("123.456.789.000").setPort(8080).setUsername("foo").setPassword("bar");

    //when
    String result = given.toString();

    //then
    assertEquals(result, "host='123.456.789.000', port=8080, username='foo', password='*****'");
  }

  @Test
  void shouldPrintNullPasswordWhenToString()
  {
    //given
    ProxyConfiguration given = new ProxyConfiguration().setHost("123.456.789.000").setPort(8080);

    //when
    String result = given.toString();

    //then
    assertEquals(result, "host='123.456.789.000', port=8080, username='null', password='null'");
  }

}
