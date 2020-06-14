package me.rarstman.basictools.util;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ClassUtil {

    public static Set<Class<?>> getClassesFromPackage(final String packageName) {
        try {
            return ClassPath.from(ClassLoader.getSystemClassLoader()).getTopLevelClasses(packageName)
                    .stream()
                    .map(ClassPath.ClassInfo::load)
                    .collect(Collectors.toSet());
        } catch (final IOException ignored) {}
        return null;
    }

    public static Set<Class<?>> getClassesFromPackages(final String... packagesName) {
        return Arrays.stream(packagesName)
                .map(packageName -> getClassesFromPackage(packageName).iterator().next())
                .collect(Collectors.toSet());
    }
}
