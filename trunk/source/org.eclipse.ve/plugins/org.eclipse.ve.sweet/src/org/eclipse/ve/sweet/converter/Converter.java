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
package org.eclipse.ve.sweet.converter;

import java.util.HashMap;

import org.eclipse.ve.sweet.converters.ConvertBoolean2String;
import org.eclipse.ve.sweet.converters.ConvertByte2String;
import org.eclipse.ve.sweet.converters.ConvertCharacter2String;
import org.eclipse.ve.sweet.converters.ConvertDouble2String;
import org.eclipse.ve.sweet.converters.ConvertFloat2String;
import org.eclipse.ve.sweet.converters.ConvertInteger2String;
import org.eclipse.ve.sweet.converters.ConvertLong2String;
import org.eclipse.ve.sweet.converters.ConvertObject2String;
import org.eclipse.ve.sweet.converters.ConvertShort2String;
import org.eclipse.ve.sweet.converters.ConvertString2Boolean;
import org.eclipse.ve.sweet.converters.ConvertString2Byte;
import org.eclipse.ve.sweet.converters.ConvertString2Character;
import org.eclipse.ve.sweet.converters.ConvertString2Double;
import org.eclipse.ve.sweet.converters.ConvertString2Float;
import org.eclipse.ve.sweet.converters.ConvertString2Integer;
import org.eclipse.ve.sweet.converters.ConvertString2Long;
import org.eclipse.ve.sweet.converters.ConvertString2Object;
import org.eclipse.ve.sweet.converters.ConvertString2Short;
import org.eclipse.ve.sweet.converters.TheIdentityConverter;
import org.eclipse.ve.sweet.converters.TheNullConverter;
import org.eclipse.ve.sweet.metalogger.Logger;


/**
 * Converter.  The base converter from which all converters can be found.
 *
 * @author djo
 */
public class Converter {
	private static HashMap converters;
    
    /*
     * Returns the set of converters to convert from a specified source class
     */
    private static HashMap getSourceClassConverters(String sourceClass) {
        HashMap result = (HashMap) converters.get(sourceClass);
        
        if (result == null) {
            result = new HashMap();
            converters.put(sourceClass, result);
        }
        
        return result;
    }
    
    /**
     * Associate a particular converter with a particular pair of classes.
     * 
     * @param sourceClass The type to convert from
     * @param destClass The type to convert to
     * @param converter The IConverter
     */
    public static void associate(String sourceClass, String destClass, IConverter converter) {
        HashMap sourceClassConverters = getSourceClassConverters(sourceClass);
        sourceClassConverters.put(destClass, converter);
    }
    
    /**
     * Return an IConverter for a specific class2class conversion.
     * 
     * @param sourceClass
     * @param destClass
     * @return An appropriate IConverter
     */
    public static IConverter get(String sourceClass, String destClass) {
        if (sourceClass.equals(destClass))
            return TheIdentityConverter.IDENTITY;
        
        HashMap sourceClassConverters = (HashMap) converters.get(sourceClass);
        
        if (sourceClassConverters == null) {
            Logger.log().message("No converters for pair (" + sourceClass + ", " + destClass + ") have been registered");
            return TheNullConverter.NULL;
        }
        
        IConverter result = (IConverter) sourceClassConverters.get(destClass);
        
        if (result == null) {
            Logger.log().message("No converters for pair (" + sourceClass + ", " + destClass + ") have been registered");
            return TheNullConverter.NULL;
        }
        
        return result;
    }
    
    static {
        converters = new HashMap();
        
        associate(Object.class.getName(), String.class.getName(), new ConvertObject2String());
        associate(String.class.getName(), Object.class.getName(), new ConvertString2Object());
        
        associate(Character.TYPE.getName(), String.class.getName(), new ConvertCharacter2String());
        associate(String.class.getName(), Character.TYPE.getName(), new ConvertString2Character());

        associate(Boolean.TYPE.getName(), String.class.getName(), new ConvertBoolean2String());
        associate(String.class.getName(), Boolean.TYPE.getName(), new ConvertString2Boolean());
        
        associate(Integer.TYPE.getName(), String.class.getName(), new ConvertInteger2String());
        associate(String.class.getName(), Integer.TYPE.getName(), new ConvertString2Integer());
        
        associate(Byte.TYPE.getName(), String.class.getName(), new ConvertByte2String());
        associate(String.class.getName(), Byte.TYPE.getName(), new ConvertString2Byte());
        
        associate(Short.TYPE.getName(), String.class.getName(), new ConvertShort2String());
        associate(String.class.getName(), Short.TYPE.getName(), new ConvertString2Short());
        
        associate(Long.TYPE.getName(), String.class.getName(), new ConvertLong2String());
        associate(String.class.getName(), Long.TYPE.getName(), new ConvertString2Long());
        
        associate(Float.TYPE.getName(), String.class.getName(), new ConvertFloat2String());
        associate(String.class.getName(), Float.TYPE.getName(), new ConvertString2Float());
        
        associate(Double.TYPE.getName(), String.class.getName(), new ConvertDouble2String());
        associate(String.class.getName(), Double.TYPE.getName(), new ConvertString2Double());
        
        associate(Boolean.class.getName(), String.class.getName(), new ConvertBoolean2String());
        associate(String.class.getName(), Boolean.class.getName(), new ConvertString2Boolean());
        
        associate(Integer.class.getName(), String.class.getName(), new ConvertInteger2String());
        associate(String.class.getName(), Integer.class.getName(), new ConvertString2Integer());
        
        associate(Byte.class.getName(), String.class.getName(), new ConvertByte2String());
        associate(String.class.getName(), Byte.class.getName(), new ConvertString2Byte());
        
        associate(Short.class.getName(), String.class.getName(), new ConvertShort2String());
        associate(String.class.getName(), Short.class.getName(), new ConvertString2Short());
        
        associate(Long.class.getName(), String.class.getName(), new ConvertLong2String());
        associate(String.class.getName(), Long.class.getName(), new ConvertString2Long());
        
        associate(Float.class.getName(), String.class.getName(), new ConvertFloat2String());
        associate(String.class.getName(), Float.class.getName(), new ConvertString2Float());
        
        associate(Double.class.getName(), String.class.getName(), new ConvertDouble2String());
        associate(String.class.getName(), Double.class.getName(), new ConvertString2Double());
    }
}


