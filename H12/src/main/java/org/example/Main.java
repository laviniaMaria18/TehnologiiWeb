package org.example;

import annotations.Test;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.jar.*;

public class Main {

    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java Main <class|directory|jar>");
            return;
        }

        File input = new File(args[0]);

        List<Class<?>> testClasses = new ArrayList<>();

        if (input.isFile()) {
            if (input.getName().endsWith(".class")) {
                URLClassLoader loader = new URLClassLoader(new URL[]{input.getParentFile().toURI().toURL()});
                String className = getClassName(input);
                testClasses.add(Class.forName(className, true, loader));
            } else if (input.getName().endsWith(".jar")) {
                testClasses.addAll(loadClassesFromJar(input));
            }
        } else if (input.isDirectory()) {
            testClasses.addAll(loadClassesFromDirectory(input));
        }

        for (Class<?> clazz : testClasses) {
            if (!Modifier.isPublic(clazz.getModifiers())) continue;

            System.out.println("===== Class: " + clazz.getName() + " =====");
            printPrototype(clazz);

            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Test.class)) {
                    totalTests++;
                    try {
                        Object instance = Modifier.isStatic(method.getModifiers()) ? null : clazz.getDeclaredConstructor().newInstance();
                        Object[] argsForMethod = generateMockArguments(method);
                        method.setAccessible(true);
                        method.invoke(instance, argsForMethod);
                        passedTests++;
                        System.out.println("[PASS] " + method.getName());
                    } catch (Exception e) {
                        failedTests++;
                        System.out.println("[FAIL] " + method.getName() + ": " + e.getCause());
                    }
                }
            }
        }

        System.out.println("\n=== Test Statistics ===");
        System.out.println("Total: " + totalTests);
        System.out.println("Passed: " + passedTests);
        System.out.println("Failed: " + failedTests);
    }

    private static List<Class<?>> loadClassesFromDirectory(File dir) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        Path base = dir.toPath();
        Files.walk(base)
                .filter(p -> p.toString().endsWith(".class"))
                .forEach(p -> {
                    try {
                        String className = base.relativize(p).toString()
                                .replace(File.separatorChar, '.')
                                .replaceAll(".class$", "");
                        URLClassLoader loader = new URLClassLoader(new URL[]{dir.toURI().toURL()});
                        classes.add(Class.forName(className, true, loader));
                    } catch (Exception ignored) {}
                });
        return classes;
    }

    private static List<Class<?>> loadClassesFromJar(File jarFile) throws Exception {
        List<Class<?>> classes = new ArrayList<>();
        URLClassLoader loader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()});
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName()
                            .replace('/', '.')
                            .replaceAll(".class$", "");
                    try {
                        classes.add(Class.forName(className, true, loader));
                    } catch (Throwable ignored) {}
                }
            }
        }
        return classes;
    }

    private static String getClassName(File classFile) throws IOException {
        String name = classFile.getName();
        return name.substring(0, name.length() - 6); // remove ".class"
    }

    private static Object[] generateMockArguments(Method method) {
        Class<?>[] types = method.getParameterTypes();
        Object[] values = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            Class<?> t = types[i];
            if (t == int.class || t == Integer.class) values[i] = 42;
            else if (t == double.class || t == Double.class) values[i] = 3.14;
            else if (t == boolean.class || t == Boolean.class) values[i] = true;
            else if (t == String.class) values[i] = "mock";
            else values[i] = null; // could be expanded
        }
        return values;
    }

    private static void printPrototype(Class<?> clazz) {
        int modifiers = clazz.getModifiers();
        System.out.print(Modifier.toString(modifiers) + " class " + clazz.getSimpleName());
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && !superClass.equals(Object.class)) {
            System.out.print(" extends " + superClass.getSimpleName());
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            System.out.print(" implements ");
            for (int i = 0; i < interfaces.length; i++) {
                System.out.print(interfaces[i].getSimpleName());
                if (i < interfaces.length - 1) System.out.print(", ");
            }
        }
        System.out.println(" {\n");
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.println("  " + Modifier.toString(method.getModifiers()) + " " +
                    method.getReturnType().getSimpleName() + " " +
                    method.getName() + "(" +
                    Arrays.stream(method.getParameterTypes())
                            .map(Class::getSimpleName)
                            .reduce((a, b) -> a + ", " + b).orElse("") +
                    ");");
        }
        System.out.println("}");
    }
}
