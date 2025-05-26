package tests;

import annotations.Test;

public class SampleTest {

    public static void hello() {
        System.out.println("Hello from SampleTest!");
    }

    @Test
    public static void test1() {
        System.out.println("Test 1 executed");
    }

    @Test
    public static void test2() {
        System.out.println("Test 2 executed");
    }

    public void notATest() {
        System.out.println("This should not run");
    }
}
