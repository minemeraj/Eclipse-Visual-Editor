/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: DefaultMethodTextGenerator.java,v $
 *  $Revision: 1.1 $  $Date: 2004-02-10 23:37:11 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import org.eclipse.emf.ecore.EObject;
import  org.eclipse.core.runtime.Preferences;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.vce.VCEPreferences;
 
/**
 * @author Gili Mendel
 * @since 1.0.0
 */
public class DefaultMethodTextGenerator extends AbstractMethodTextGenerator {

	public static final String METHOD_TEMPLATE_CLASS_NAME = "DefaultMethodTemplate" ; //$NON-NLS-1$
	public static final String TRY_CATCH_METHOD_TEMPLATE_CLASS_NAME = "TryCatchMethodTemplate" ; //$NON-NLS-1$
	public static final String METHOD_TEMPLATE_NAME = METHOD_TEMPLATE_CLASS_NAME+JAVAJET_EXT ;
	public static final String TRY_CATCH_METHOD_TEMPLATE_NAME = TRY_CATCH_METHOD_TEMPLATE_CLASS_NAME+JAVAJET_EXT ;
	public final static  String BASE_PLUGIN = "org.eclipse.ve.java.core"; //$NON-NLS-1$
	public final static  String TEMPLATE_PATH = "templates/org/eclipse/ve/internal/java/codegen/jjet/util" ; //$NON-NLS-1$

	
	
	
	public final static  String[] ignoredFeatures = { "allocation" };
												
	AbstractMethodTextGenerator.MethodInfo fInfo = null ;
	Boolean fgenerateTryCatchBlock = null ;
	
	
	public DefaultMethodTextGenerator(EObject component, IBeanDeclModel model) {
		super(component,model) ;		
	}
	

	/**
	 * getter always has a return type
	 **/	
	protected AbstractMethodTextGenerator.MethodInfo getInfo() {
	   if (fInfo != null) return fInfo ;
	   fComments = new String[] { IMethodTextGenerator.DEFAULT_METHOD_COMMENT+finitbeanName,
	   		"", 
	   		"@return "+((IJavaObjectInstance)fComponent).getJavaType().getQualifiedName()
	   };
       fInfo = new AbstractMethodTextGenerator.MethodInfo(true) ;
       return fInfo ;
	}
		

	protected boolean isTryCatch() {
       if (fgenerateTryCatchBlock != null) return fgenerateTryCatchBlock.booleanValue();
       Preferences store = VCEPreferences.getPlugin().getPluginPreferences();
       fgenerateTryCatchBlock = new Boolean (store.getBoolean(VCEPreferences.GENERATE_TRY_CATCH_BLOCK));
       return fgenerateTryCatchBlock.booleanValue();
    }
	protected IMethodTemplate getMethodTemplate() {

        if (isTryCatch())
			return getMethodTemplate(TRY_CATCH_METHOD_TEMPLATE_NAME,TRY_CATCH_METHOD_TEMPLATE_CLASS_NAME) ;
		else
            return getMethodTemplate(METHOD_TEMPLATE_NAME,METHOD_TEMPLATE_CLASS_NAME) ;
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractMethodTextGenerator#getBasePlugin()
	 */
	protected String getBasePlugin() {
		return BASE_PLUGIN ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractMethodTextGenerator#getTemplatePath()
	 */
	protected String getTemplatePath() {
		return TEMPLATE_PATH ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractMethodTextGenerator#getIgnoreSFnameList()
	 */
	protected String[] getIgnoreSFnameList() {
		return ignoredFeatures;
	}



}
