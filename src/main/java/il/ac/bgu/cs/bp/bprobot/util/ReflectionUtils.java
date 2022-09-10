package il.ac.bgu.cs.bp.bprobot.util;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionUtils {
  public static Class<?> getClass(String name, List<String> packages) throws ClassNotFoundException, IOException {
    if (name.contains(".")) {
      return Class.forName(name);
    } else {
      var classes = getAllClassesInPackages(packages).stream().filter(c -> c.getSimpleName().equals(name)).collect(Collectors.toList());
      if (classes.size() > 1) {
        throw new IllegalArgumentException("More than one class found for type " + name + ", please use full package name");
      } else if (classes.size() == 0) {
        throw new IllegalArgumentException("No class found for type " + name);
      } else {
        return List.copyOf(classes).get(0);
      }
    }
  }
  public static Set<Class<?>> getAllClassesInPackages(List<String> packagePath) throws IOException {
    final List<String> finalPackagePath = packagePath == null ? List.of("") : packagePath;
    return ClassPath.from(ClassLoader.getSystemClassLoader())
        .getAllClasses()
        .stream()
        .filter(clazz -> finalPackagePath.stream().anyMatch(p -> clazz.getPackageName().startsWith(p)))
        .map(ClassPath.ClassInfo::load)
        .collect(Collectors.toSet());
  }

  public static <T> T create(String name, List<String> packagePath, Object... params) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException {
    final List<String> finalPackagePath = packagePath == null ? List.of("") : packagePath;
    var classes =
        ClassPath.from(ClassLoader.getSystemClassLoader())
            .getAllClasses()
            .stream()
            .filter(clazz -> finalPackagePath.stream().anyMatch(p -> clazz.getPackageName().startsWith(p)))
            .filter(clazz -> clazz.getSimpleName().equals(name))
            .map(ClassPath.ClassInfo::load)
            .filter(cl -> getCtor(cl, params) != null)
            .collect(Collectors.toSet());
    if (classes.size() == 0) throw new InvalidParameterException("There is no such class " + name);
    if (classes.size() > 1)
      throw new InvalidParameterException("There is more than one class with the name no such class " + name);
    Class<?> cl = classes.stream().findFirst().get();
    var ctor = getCtor(cl, params);
    assert ctor != null;
    return (T) ctor.newInstance(params);
  }

  public static Constructor<?> getDeclaredCtor(Class<?> cl, Class<?>... params) throws NoSuchMethodException {
    return cl.getDeclaredConstructor(params);
  }

  public static Constructor<?> getCtor(Class<?> cl, Object... params) {
    try {
      return cl.getConstructor(Arrays.stream(params).map(Object::getClass).toArray(Class[]::new));
    } catch (NoSuchMethodException e) {
      return null;
    }
  }
}
