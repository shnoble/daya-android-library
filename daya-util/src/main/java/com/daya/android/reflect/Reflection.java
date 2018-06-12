package com.daya.android.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by shhong on 2017. 9. 22..
 */

public class Reflection {
    Reflection() {
    }

    public static Object newInstance(String className)
            throws ClassNotFoundException,
            IllegalAccessException, InstantiationException {
        return getClass(className).newInstance();
    }

    public static Object newInstance(Class<?> clazz)
            throws ClassNotFoundException,
            IllegalAccessException, InstantiationException {
        return clazz.newInstance();
    }

    public static Object newInstance(Constructor<?> constructor,
                                     Object ... initArgs)
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return constructor.newInstance(initArgs);
    }

    public static Constructor<?> getDeclaredConstructor(String className,
                                                        Class<?>... parameterTypes)
            throws ClassNotFoundException, NoSuchMethodException {
        return getClass(className).getDeclaredConstructor(parameterTypes);
    }

    public static Constructor<?> getDeclaredConstructor(Class<?> clazz,
                                                        Class<?>... parameterTypes)
            throws ClassNotFoundException, NoSuchMethodException {
        return clazz.getDeclaredConstructor(parameterTypes);
    }

    public static Class<?> getClass(String className)
            throws ClassNotFoundException {
        return Class.forName(className);
    }

    public static Method getMethod(Class<?> clazz, String methodName)
            throws NoSuchMethodException {
        return clazz.getMethod(methodName);
    }

    public static Method getMethod(Class<?> clazz, String methodName, Class... parameterTypes)
            throws NoSuchMethodException {
        return clazz.getMethod(methodName, parameterTypes);
    }

    public static Method getMethod(String className, String methodName)
            throws ClassNotFoundException, NoSuchMethodException {
        return getMethod(getClass(className), methodName);
    }

    public static Method getMethod(String className, String methodName, Class... parameterTypes)
            throws ClassNotFoundException, NoSuchMethodException {
        return getMethod(getClass(className), methodName, parameterTypes);
    }

    public static Object invoke(Object methodObject, Method method)
            throws InvocationTargetException, IllegalAccessException {
        return method.invoke(methodObject);
    }

    public static Object invoke(Object methodObject, Method method, Object... parameters)
            throws InvocationTargetException, IllegalAccessException {
        return method.invoke(methodObject, parameters);
    }

    public static Object invoke(String className, Object methodObject, String methodName)
            throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Method method = getMethod(getClass(className), methodName);
        return method.invoke(methodObject);
    }

    public static Object invoke(Class<?> clazz, Object methodObject, String methodName)
            throws IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {
        Method method = getMethod(clazz, methodName);
        return invoke(methodObject, method);
    }

    public static Object invokeStatic(Method method)
            throws IllegalAccessException, InvocationTargetException {
        return method.invoke(null);
    }

    public static Object invokeStatic(Method method, Object... parameters)
            throws IllegalAccessException, InvocationTargetException {
        return method.invoke(null, parameters);
    }

    public static Object invokeStatic(Class<?> clazz, String methodName)
            throws IllegalAccessException, InstantiationException,
            NoSuchMethodException, InvocationTargetException {
        Method method = getMethod(clazz, methodName);
        return invokeStatic(method);
    }

    public static Object invokeStatic(String className, String methodName)
            throws ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Method method = getMethod(className, methodName);
        return invokeStatic(method);
    }
}
