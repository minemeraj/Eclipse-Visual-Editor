/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: AggregateDecoderHelper.java,v $
 *  $Revision: 1.5 $  $Date: 2005-12-14 21:27:57 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jface.util.Assert;

import org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper.VEexpressionPriority;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 

public class AggregateDecoderHelper implements IExpressionDecoderHelper{

	private IExpressionDecoderHelper[] helpers = null;
	private IExpressionDecoderHelper decodedHelper = null;
	
	public AggregateDecoderHelper(BeanPart bean, Statement exp, IJavaFeatureMapper fm, IExpressionDecoder owner, Class[] helperClasses) {
		Assert.isNotNull(helperClasses, "Must have atleast one decoder helper to aggregate"); //$NON-NLS-1$
		Assert.isTrue(helperClasses.length>0, "Must have atleast one decoder helper to aggregate"); //$NON-NLS-1$
		this.helpers = new IExpressionDecoderHelper[helperClasses.length];
		Class paramClasses[] = new Class[4];
		try {
			paramClasses[0] = getClass().getClassLoader().loadClass("org.eclipse.ve.internal.java.codegen.model.BeanPart"); //$NON-NLS-1$
			paramClasses[1] = getClass().getClassLoader().loadClass("org.eclipse.jdt.core.dom.Statement"); //$NON-NLS-1$
			paramClasses[2] = getClass().getClassLoader().loadClass("org.eclipse.ve.internal.java.codegen.java.IJavaFeatureMapper"); //$NON-NLS-1$
			paramClasses[3] = getClass().getClassLoader().loadClass("org.eclipse.ve.internal.java.codegen.java.IExpressionDecoder"); //$NON-NLS-1$
			for (int helperClassCount = 0; helperClassCount < helperClasses.length; helperClassCount++) {
				Constructor constructor = helperClasses[helperClassCount].getConstructor(paramClasses);
				IExpressionDecoderHelper helper = (IExpressionDecoderHelper) constructor.newInstance(new Object[]{bean, exp, fm, owner});
				helpers[helperClassCount] = helper;
			}
		} catch (ClassNotFoundException e) {
			JavaVEPlugin.log(e, Level.WARNING);
		} catch (SecurityException e) {
			JavaVEPlugin.log(e, Level.WARNING);
		} catch (NoSuchMethodException e) {
			JavaVEPlugin.log(e, Level.WARNING);
		} catch (IllegalArgumentException e) {
			JavaVEPlugin.log(e, Level.WARNING);
		} catch (InstantiationException e) {
			JavaVEPlugin.log(e, Level.WARNING);
		} catch (IllegalAccessException e) {
			JavaVEPlugin.log(e, Level.WARNING);
		} catch (InvocationTargetException e) {
			JavaVEPlugin.log(e, Level.WARNING);
		}
	}

	public boolean decode() throws CodeGenException {
		for (int helperCount = 0; helperCount < helpers.length; helperCount++) {
			if(helpers[helperCount].decode()){
				decodedHelper = helpers[helperCount];
				return true;
			}
		}
		return false;
	}

	public boolean restore() throws CodeGenException {
		for (int helperCount = 0; helperCount < helpers.length; helperCount++) {
			if(helpers[helperCount].restore()){
				decodedHelper = helpers[helperCount];
				return true;
			}
		}
		return false;
	}

	public String generate(Object[] args) throws CodeGenException {
		if(decodedHelper!=null)
			return decodedHelper.generate(args);
		return null;
	}

	public void removeFromModel() {
		if(decodedHelper!=null)
			decodedHelper.removeFromModel();
	}

	public String primRefreshFromComposition(String expSig) throws CodeGenException {
		if(decodedHelper!=null)
			return decodedHelper.primRefreshFromComposition(expSig);
		return null;
	}

	public boolean primIsDeleted() {
		if(decodedHelper!=null)
			return decodedHelper.primIsDeleted();
		return false;
	}

	public Object[] getArgsHandles(Statement expr) {
		if(decodedHelper!=null)
			return decodedHelper.getArgsHandles(expr);
		return null;
	}


	public void setDecodingContent(Statement exp) {
		for (int i = 0; i < helpers.length; i++) {
			helpers[i].setDecodingContent(exp);
		}
	}

	public VEexpressionPriority getPriorityOfExpression() {
		if(decodedHelper!=null)
			return decodedHelper.getPriorityOfExpression();
		return null;
	}

	public boolean canRefreshFromComposition() {
		if(decodedHelper!=null)
			return decodedHelper.canRefreshFromComposition();
		return false;
	}

	public String getCurrentExpression() {
		if(decodedHelper!=null)
			return decodedHelper.getCurrentExpression();
		return null;
	}

	public void adaptToCompositionModel(IExpressionDecoder decoder) {
		if(decodedHelper!=null)
			decodedHelper.adaptToCompositionModel(decoder);
	}

	public void unadaptToCompositionModel() {
		if(decodedHelper!=null)
			decodedHelper.unadaptToCompositionModel();
	}


	public Object[] getAddedInstance() {
		if(decodedHelper!=null)
			return decodedHelper.getAddedInstance();
		return null;
	}

	public Object[] getReferencedInstances() {
		if(decodedHelper!=null)
			return decodedHelper.getReferencedInstances();
		return null;
	}

	public boolean isRelevantFeature(EStructuralFeature sf) {
		if(decodedHelper!=null)
			return decodedHelper.isRelevantFeature(sf);
		return false;
	}

}
