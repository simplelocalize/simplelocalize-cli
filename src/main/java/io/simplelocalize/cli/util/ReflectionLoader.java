package io.simplelocalize.cli.util;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.simplelocalize.cli.processor.ProjectProcessor;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

public class ReflectionLoader {

  private static Logger log = LoggerFactory.getLogger(ReflectionLoader.class);

  private ReflectionLoader() {
  }

  public static Set<ProjectProcessor> loadProcessors() {
    Set<ProjectProcessor> processors = Sets.newHashSet();


    String searchPackageName = getSearchPackageName();
    Reflections reflections = new Reflections(searchPackageName);
    Set<Class<? extends ProjectProcessor>> classes = reflections.getSubTypesOf(ProjectProcessor.class);
    for (Class<? extends ProjectProcessor> clazz : classes) {
      ProjectProcessor projectProcessor = tryCreateInstance(clazz);

      String projectTypeSupport = projectProcessor.getProjectTypeSupport();
      if (Strings.isNullOrEmpty(projectTypeSupport)) {
        log.warn("{} returned null or empty project support type and it WILL NOT be used in further processing process", clazz);
        continue;
      }


      processors.add(projectProcessor);
    }

    return processors;

  }

  private static ProjectProcessor tryCreateInstance(Class<? extends ProjectProcessor> clazz) {
    ProjectProcessor projectProcessor = null;
    try {
      projectProcessor = clazz.getDeclaredConstructor().newInstance();
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
      log.error("Could not create instance of " + clazz.getSimpleName(), e);
    }
    return projectProcessor;
  }

  private static String getSearchPackageName() {
    String packageName = ProjectProcessor.class.getPackageName();
    String[] split = packageName.split("\\.");
    List<String> packages = Lists.newArrayList(split);
    packages.remove(packages.size() - 1);
    return String.join(".", packages);
  }


}
