package org.eclipse.ve.internal.java.codegen.java;
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
 *  $RCSfile: AbstractFeatureMapper.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:29 $ 
 */
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.internal.compiler.ast.Statement;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.adapters.Utilities;
import org.eclipse.jem.internal.core.MsgLogger;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;

import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.jcm.BeanFeatureDecorator;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public abstract class AbstractFeatureMapper implements IJavaFeatureMapper {

	protected EStructuralFeature fSF = null;
	protected String fSFname = null;
	protected PropertyDecorator fPD = null;
	protected IJavaInstance fRefObj = null;
	protected String fMethodName = null;
	protected String fReadMethodName = null;
	protected boolean fisMethod = true;

	/**
	 *  Implementors will provide a specific logic.
	 *  Note: that implementors are designed for a particular set of features.
	 *        One has to load the propper mapper
	 */
	public abstract EStructuralFeature getFeature(Statement expr);

	public boolean isFieldFeature() {
		return !fisMethod;
	}

	protected boolean isFieldAccess(EStructuralFeature sf) {

		BeanFeatureDecorator bdec = (BeanFeatureDecorator) Utilities.getDecorator(sf, BeanFeatureDecorator.class);
		return (bdec != null && "org.eclipse.ve.java.core/org.eclipse.ve.internal.java.core.BeanFieldProxyFeatureMediator".equals(bdec.getBeanProxyMediatorName())); //$NON-NLS-1$
	}

	public void setFeature(EStructuralFeature sf) {
		fSF = sf;
		if (sf != null) {
			try {
				fSFname = ((EStructuralFeature) fSF).getName();
				fPD = Utilities.getPropertyDecorator((EModelElement) sf);
			} catch (Throwable e) {
				JavaVEPlugin.log("Utilities.getPropertyDecorator on:" + sf, MsgLogger.LOG_WARNING); //$NON-NLS-1$
				JavaVEPlugin.log(e, MsgLogger.LOG_WARNING);
			}
			if (fPD == null && isFieldAccess(sf)) {
				fisMethod = false;
				fMethodName = sf.getName();
			}
		}
	}
	public String getFeatureName() {
		return fSFname;
	}

	public void setRefObject(IJavaInstance obj) {
		fRefObj = obj;
	}
	public IJavaInstance getRefObject() {
		return fRefObj;
	}

	public String getMethodName() {
		if (fMethodName == null) {
			if (fPD != null && fPD.getWriteMethod() != null) {
				fMethodName = fPD.getWriteMethod().getName();
			}
		}
		return fMethodName;
	}

	public String getReadMethodName() {
		if (fReadMethodName == null) {
			if (fPD != null && fPD.getReadMethod() != null) {
				fReadMethodName = fPD.getReadMethod().getName();
			}
		}
		return fReadMethodName;
	}

	protected String getMethodName(Statement exp) {
		if (fMethodName == null)
			fMethodName = CodeGenUtil.getWriteMethod(exp);
		return fMethodName;
	}

	public PropertyDecorator getDecorator() {
		return fPD;
	}

	/**
	 * The default, overide if needed
	 */
	public String getIndexMethodName() {
		return getMethodName();
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper#getPriorityIncrement(String)
	 */
	public int getPriorityIncrement(String methodType) {
		return 0;
	}

}