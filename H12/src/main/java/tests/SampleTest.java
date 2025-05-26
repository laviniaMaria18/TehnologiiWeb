package tests;

import annotations.Test;

public class SampleTest {

    @Test
    public static void testStatic() {
        System.out.println("Static test passed");
    }

    @Test
    public void testInstance(String msg, int x) {
        System.out.println("Instance test: " + msg + ", value: " + x);
    }

    public void helper() {
        System.out.println("Helper method");
    }
}
