/*
 * Copyright (C) 2005 db4objects Inc.  http://www.db4o.com
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     db4objects - Initial API and implementation
 */
package org.eclipse.ve.sweet.dataeditors.java.internal;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.eclipse.ve.sweet.dataeditors.IPropertyEditor;

/**
 * Db4oBeanProperty. An implementation of IPropertyEditor using dynamic proxies.
 * 
 * @author djo
 */
public class JavaProperty implements InvocationHandler {

    public static IPropertyEditor construct(Object receiver, String propertyName) throws NoSuchMethodException {
        try {
            return (IPropertyEditor) Proxy.newProxyInstance(JavaProperty.class.getClassLoader(),
                    new Class[] { IPropertyEditor.class }, new JavaProperty(
                            receiver, propertyName));
        } catch (IllegalArgumentException e) {
            throw new NoSuchMethodException(e.getMessage());
        }
    }

    private String propertyName;
    private Class propertyType;
    private Object receiver;

    private Class receiverClass;
    
    private Method setter = null;
    private Method getter;
    private Field field;

    /**
     * Construct a JavaBeansProperty object on the specified object and property
     * 
     * @param receiver
     * @param propertyName
     */
    private JavaProperty(Object receiver, String propertyName)
            throws NoSuchMethodException {
        this.receiver = receiver;
        this.receiverClass = receiver.getClass();
        this.propertyName = propertyName;

        // There must be at least a getter or a field...
        try {
            getter = receiverClass.getMethod(realMethodName("get"), noParams);
            propertyType = getter.getReturnType();
        } catch (NoSuchMethodException e) {
            try {
                field = receiverClass.getDeclaredField(propertyName);
            } catch (Exception e2) {
                try {
                    field = receiverClass.getDeclaredField(lowerCaseFirstLetter(propertyName));
                } catch (Exception e1) {
                    throw new NoSuchMethodException("That property does not exist.");
                }
            }
            propertyType = field.getType();
            field.setAccessible(true);
        }
        
        try {
            setter = receiverClass.getMethod(
                    realMethodName("set"), new Class[] {propertyType});
        } catch (NoSuchMethodException e) {}
    }

    private String lowerCaseFirstLetter(String name) {
        String result = name.substring(0, 1).toLowerCase() + name.substring(1);
        return result;
    }

    private Object get() {
        if (getter != null) {
            try {
                return getter.invoke(receiver, new Object[] {});
            } catch (Exception e) {
                return null;
            }
        }
        if (field != null)
            try {
                return field.get(receiver);
            } catch (Exception e) {
                return null;
            }
        else
            return null;
    }
    
    private void set(Object[] args) {
        if (setter != null) {
            try {
                setter.invoke(receiver, args);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (field != null) {
            try {
                field.set(receiver, args[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    /*
     * This implements a semi-relaxed duck-type over IPropertyEditor. The
     * required method is get<propertyName>. getType, getInput, and setInput
     * are implemented internally.
     */
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if ("set".equals(method.getName())) {
            set(args);
            return null;
        } else if ("get".equals(method.getName())) {
            return get();
        } else if ("getType".equals(method.getName())) {
            return propertyType.getName();
        } else if ("getInput".equals(method.getName())) {
            return receiver;
        } else if ("setInput".equals(method.getName())) {
            this.receiver = args[0];
        } else if ("isReadOnly".equals(method.getName())) {
            return new Boolean(setter == null && field == null);
        } else if ("getName".equals(method.getName())) {
            return propertyName;
        } 

        Method realMethod;
        try {
            realMethod = receiverClass.getMethod(
                    realMethodName(method.getName()), method.getParameterTypes());
        } catch (Exception e) {
            return null;
        }
        return realMethod.invoke(receiver, args);
    }

    String realMethodName(String interfaceMethodName) {
        return interfaceMethodName.substring(0, 3) + propertyName
                + interfaceMethodName.substring(3);
    }

    private static final Class[] noParams = new Class[] {};

}
