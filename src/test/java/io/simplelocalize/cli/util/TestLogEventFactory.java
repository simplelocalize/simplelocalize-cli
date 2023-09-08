package io.simplelocalize.cli.util;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TestLogEventFactory
{
  public static List<ILoggingEvent> createAndGetLogEventList(Class<?> clazz)
  {
    Logger logger = (Logger) LoggerFactory.getLogger(clazz);
    ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
    listAppender.start();
    logger.addAppender(listAppender);
    return listAppender.list;
  }

  public static void verifyLogEvents(List<ILoggingEvent> logEventList, List<String> strings)
  {
    if (logEventList.isEmpty())
    {
      throw new AssertionError("Log event list is empty");
    }

    for (int i = 0; i < logEventList.size(); i++)
    {
      ILoggingEvent loggingEvent = logEventList.get(i);
      String expected = strings.get(i);
      String actual = loggingEvent.getFormattedMessage();
      if (!actual.equals(expected))
      {
        throw new AssertionError("Expected: " + expected + " but was: " + actual);
      }
    }
  }
}
