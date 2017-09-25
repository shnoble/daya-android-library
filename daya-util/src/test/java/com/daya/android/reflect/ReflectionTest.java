package com.daya.android.reflect;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by shhong on 2017. 9. 22..
 */
public class ReflectionTest {
    @Test
    public void testNewInstanceWithClassName() throws Exception {
        Object object = Reflection.newInstance("java.lang.String");
        assertNotNull(object);
    }

    @Test
    public void testNewInstanceWithClass() throws Exception {
        Class<?> clazz = Reflection.getClass("java.lang.String");
        Object object = Reflection.newInstance(clazz);
        assertNotNull(object);
    }

    @Test
    public void testGetClass() throws Exception {
        Class<?> clazz = Reflection.getClass("java.lang.String");
        assertNotNull(clazz);
        assertTrue(String.class.isAssignableFrom(clazz));
    }

    @Test
    public void testGetMethodWithClass() throws Exception {
        Class<?> clazz = Reflection.getClass("java.lang.String");
        Method method = Reflection.getMethod(clazz, "isEmpty");
        assertNotNull(method);
    }

    @Test
    public void testGetMethodWithClassName() throws Exception {
        Method method = Reflection.getMethod("java.lang.String", "isEmpty");
        assertNotNull(method);
    }

    @Test
    public void testInvokeWithMethod() throws Exception {
        Object object = Reflection.newInstance("java.lang.String");
        Method method = Reflection.getMethod("java.lang.String", "isEmpty");

        assertNotNull(object);
        assertNotNull(method);

        boolean isEmpty = (Boolean) Reflection.invoke(object, method);
        assertTrue(isEmpty);
    }

    @Test
    public void testInvokeWithMethodAndArgs() throws Exception {
        Object object = Reflection.newInstance("java.lang.StringBuilder");
        Method method = Reflection.getMethod("java.lang.StringBuilder", "append", String.class);

        assertNotNull(object);
        assertNotNull(method);

        StringBuilder builder = (StringBuilder) Reflection.invoke(object, method, "message");
        assertNotNull(builder);
        assertEquals("message", builder.toString());
    }

    @Test
    public void testInvokeWithMethodName() throws Exception {
        Object object = Reflection.newInstance("java.lang.String");

        assertNotNull(object);

        boolean isEmpty = (Boolean) Reflection.invoke("java.lang.String", object, "isEmpty");
        assertTrue(isEmpty);
    }

    @Test
    public void invokeStatic() throws Exception {
        Method method = Reflection.getMethod("java.lang.String", "valueOf", Object.class);
        String result = (String) Reflection.invokeStatic(method, "Hello");
        assertNotNull(result);
        assertEquals("Hello", result);
    }
}