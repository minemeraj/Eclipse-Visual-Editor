package org.eclipse.ve.internal.java.codegen.util;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CodegenTypeResolver.java,v $
 *  $Revision: 1.2 $  $Date: 2004-02-06 21:43:09 $ 
 */

import java.util.HashMap;
import java.util.StringTokenizer;

import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.SourceType;

import org.eclipse.ve.internal.java.codegen.java.PropertyFeatureMapper;

/**
 * @version 	1.0
 * @author
 */
public class CodegenTypeResolver implements IElementChangedListener{
	
	private IType fReferenceIType = null;
	private boolean isConnected = false;
	private HashMap tempHash = null;
	
	public CodegenTypeResolver(IType referenceIType){
		fReferenceIType = referenceIType;
	}
	
	public void connect(){
		if(!isConnected){
			JavaCore.addElementChangedListener(this);
			isConnected = true;
		}
	}	/**
	 * Returns a null if not able to resolve, or
	 * returns a {"package","class"} parts if resolved.
	 * It DOES NOT resolve things like "Class.InnerClass".
	 * Resolves ONLY classes inside packages. It resolves
	 * "pkg.pkg1.Class.InnerClass".
	 */
	private String[] resolveSimpleType(String toResolve){
		try{
			String[][] types = fReferenceIType.resolveType(toResolve);
			if(types!=null && types.length>0)
				return types[0];
		}catch(JavaModelException e){
			return null;
		}
		return null;
	}
	private HashMap getNewHashMap() {
		HashMap map = new HashMap();
		map.put("int", "int");
		map.put("float", "float");
		map.put("double", "double");
		map.put("short", "short");
		map.put("long", "long");
		map.put("boolean", "boolean");
		map.put("byte", "byte");
		map.put("char", "char");
		map.put("void", "void");

		map.put("Integer", "java.lang.Integer");
		map.put("Double", "java.lang.Double");
		map.put("Float", "java.lang.Float");
		map.put("Short", "java.lang.Short");
		map.put("Long", "java.lang.Long");
		map.put("Boolean", "java.lang.Boolean");
		map.put("Byte", "java.lang.Byte");
		map.put("Character", "java.lang.Character");
		map.put("String", "java.lang.String");

		map.put("Class", "java.lang.Class");

		return map;
	}

	public String resolveTypeComplex(String toResolve) {
		return resolveTypeComplex(toResolve,false) ;
	}
	/*
	 * P = package
	 * C = class
	 * V = variable
	 * Handles:
	 * (*) C1             >> P1.P2.C1        
	 * (*) P1.P2.C1       >> P1.P2.C1        
	 * (*) C1.C2          >> P1.P2.C1$C2     
	 * (*) P1.P2.C1.C2    >> P1.P2.C1$C2     
	 * (*) P1.P2.C1.C2.V1 >> P1.P2.C1$C2.V1  
	 * (*) C1.C2.V1       >> P1.P2.C1$C2.V1  
	 * noField implies that we need to resolve the accessor of an internal
	 * field, e.g., org.eclipse.swt.SWT.None ... return org.eclipse.swt.SWT
	 */
	public String resolveTypeComplex(String toResolve, boolean noField){
		if(tempHash==null)
			tempHash = getNewHashMap();
		if(!noField && tempHash.containsKey(toResolve))
			return (String) tempHash.get(toResolve);
		StringTokenizer dotTokenizer = new StringTokenizer(toResolve,"$.",false); //$NON-NLS-1$
		String finalResolved = toResolve;
		String[] tokens = new String[dotTokenizer.countTokens()];
		int count = 0;
		while(dotTokenizer.hasMoreTokens()){
			tokens[count] = dotTokenizer.nextToken();
			count++;
		}
		
		// tokens first go into seensofar, and when decided
		// on the seperator go into correctedfinalresolved.
		String correctedFinalResolved = new String();
		String seenSoFar = new String();
		boolean lastTokenWasAClass = false;
		boolean couldResolveAtleastOneToken = false ;
		String lastResolved = null ;
		for(int i=0;i<tokens.length;i++){
			String token = tokens[i];
			seenSoFar = correctedFinalResolved.replace('$','.');
			if(i>0)
				seenSoFar = seenSoFar.concat("."); //$NON-NLS-1$
			seenSoFar = seenSoFar.concat(token);
			String[] ret = resolveSimpleType(seenSoFar);
			if (ret != null) {
			   couldResolveAtleastOneToken = true ;  // could be a temporary compile/parsing issues with the current IType
			   lastResolved = seenSoFar ;
			}
			boolean tokenPresentInHierarchy = false;
			if(ret==null && lastTokenWasAClass)
				ret = checkInHierarchy(seenSoFar);
			if((ret==null || ret.length<1) && !tokenPresentInHierarchy){
				if(i>0)
					correctedFinalResolved = correctedFinalResolved.concat("."); //$NON-NLS-1$
				correctedFinalResolved = correctedFinalResolved.concat(token);
				lastTokenWasAClass = false;
			}else{
				String pkg = ret[0];
				String cls = ret[1];
				if (pkg.length() > 0)
					correctedFinalResolved = pkg+"."+cls.replace('.','$'); //$NON-NLS-1$
				else
					correctedFinalResolved = cls.replace('.','$'); //$NON-NLS-1$
				lastTokenWasAClass = true;
			}
		}
		if (noField) return lastResolved;
		
		if(correctedFinalResolved!=null)
			finalResolved = correctedFinalResolved;
		if(finalResolved.startsWith(".")){ //$NON-NLS-1$
			finalResolved = finalResolved.substring(1,finalResolved.length());
		}
		if (couldResolveAtleastOneToken)
		   tempHash.put(toResolve, finalResolved);
		return finalResolved;
	}
	
	/**
	 * JCMMethod checkInHierarchy.
	 * Calls to this are made ONLY when the previous class
	 * has been confirmed to be a class, and an inner class 
	 * checking is required. 
	 * 
	 * @param inner  fully resolved inner class name.
	 * @return String[]  null if not an inner class, else
	 * returns array of strings: {{package},{class}}. where
	 * class is either <class> or <class>.<class>
	 * 
	 */
	private String[] checkInHierarchy(String inner){
		try {
			String outerClassName = inner.substring(0,inner.lastIndexOf('.'));
			String innerClassName = inner.substring(inner.lastIndexOf('.')+1,inner.length());
			IType outerClassType = fReferenceIType.getJavaProject().findType(outerClassName);
			if(outerClassType==null)
				return null;
			return traverseHierarchy(outerClassType, innerClassName);
		} catch (JavaModelException e) {
		}
		return null;
	}
	
	private String[] traverseHierarchy(IType outerType, String innerClassName){
		try {
			
			String[] ret = resolveSimpleType(outerType.getFullyQualifiedName()+"."+innerClassName); //$NON-NLS-1$
			if(ret!=null)
				return ret;	

			// Go through the interfaces first
			String[] implementsNames = outerType.getSuperInterfaceNames();
			if(implementsNames!=null && implementsNames.length>0){
				for(int i=0;i<implementsNames.length;i++){
					IType implementType = null;
					if(outerType instanceof SourceType){
						String[][] tmp = outerType.resolveType(implementsNames[i]);
						if(tmp==null || tmp.length<1)
							continue;
						String implement[] = tmp[0];
						implementType = fReferenceIType.getJavaProject().findType(implement[0],implement[1]);
					}else{ // Binary type
						implementType = fReferenceIType.getJavaProject().findType(implementsNames[i]);
					}
					if(implementType!=null){
						ret = traverseHierarchy(implementType,innerClassName);
						if(ret!=null)
							return ret;
					}
				}
			}

			// Go through the super hierarchy next
			String superName = outerType.getSuperclassName();
			if(superName!=null){
				IType superType = null;
				if(outerType instanceof SourceType){
					String[][] tmp = outerType.resolveType(superName);
					if(tmp!=null || tmp.length>0){
						String supers[] = tmp[0];
						superType = fReferenceIType.getJavaProject().findType(supers[0],supers[1]);
					}
				}else{ // Binary type
					superType = fReferenceIType.getJavaProject().findType(superName);
				}
				if(superType!=null){
					ret = traverseHierarchy(superType,innerClassName);
					if(ret!=null)
						return ret;
				}
			}
		} catch (JavaModelException e) {
		}
		return null;
	}
	
	/*
	 * @see IElementChangedListener#elementChanged(ElementChangedEvent)
	 */
	public void elementChanged(ElementChangedEvent event) {
		if(tempHash!=null)
			tempHash.clear();
	    // TODO Temporary
		PropertyFeatureMapper.clearCache() ;	    
		CodeGenUtil.clearCache()  ;		
		org.eclipse.ve.internal.java.codegen.java.rules.InstanceVariableCreationRule.clearCache() ;
		org.eclipse.ve.internal.java.codegen.java.rules.InstanceVariableRule.clearCache() ;
	}	
	
	public void disconnect() {
		if(isConnected){
			JavaCore.removeElementChangedListener(this);
			isConnected = false;
		}
	}
}
