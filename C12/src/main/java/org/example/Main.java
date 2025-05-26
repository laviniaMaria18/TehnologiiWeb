package org.example;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import annotations.Test;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Main <fully.qualified.ClassName>");
            return;
        }

        String className = args[0];

        try {
            // Load the class
            Class<?> clazz = Class.forName(className);
            System.out.println("Class loaded: " + clazz.getName());

            // List methods
            System.out.println("\n--- Methods ---");
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                System.out.println(method);
            }

            // Run @Test methods
            System.out.println("\n--- Running @Test methods ---");
            for (Method method : methods) {
                if (method.isAnnotationPresent(Test.class) &&
                        Modifier.isStatic(method.getModifiers()) &&
                        method.getParameterCount() == 0) {

                    System.out.println("Invoking: " + method.getName());
                    method.invoke(null);
                }
            }

        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + className);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
