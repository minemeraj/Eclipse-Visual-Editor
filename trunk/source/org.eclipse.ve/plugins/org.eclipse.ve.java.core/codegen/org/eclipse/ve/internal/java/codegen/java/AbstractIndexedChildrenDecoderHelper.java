/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: AbstractIndexedChildrenDecoderHelper.java,v $
 *  $Revision: 1.10 $  $Date: 2005-08-24 23:30:45 $ 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jdt.core.dom.Statement;

import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author sri
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public abstract class AbstractIndexedChildrenDecoderHelper extends ExpressionDecoderHelper {

	protected static List getComponentsFromConstraintComponents(List ccs){
		if(ccs==null)
			return null;
		List components = new ArrayList(ccs.size());
		for(int i=0;i<ccs.size();i++){
			EObject cc = (EObject)ccs.get(i);
			if("ConstraintComponent".equals(cc.eClass().getName())) //$NON-NLS-1$
				components.add(CodeGenUtil.getCCcomponent(cc));
			else
				components.add(cc);
		}
		return components;
	}
	
	/**
	 * Constructor for IndexedChildrenDecoderHelper.
	 * @param bean
	 * @param exp
	 * @param fm
	 * @param owner
	 */
	public AbstractIndexedChildrenDecoderHelper(
		BeanPart bean,
		Statement exp,
		IJavaFeatureMapper fm,
		IExpressionDecoder owner) {
		super(bean, exp, fm, owner);
	}
	
	protected abstract List getIndexedEntries(); // get all the index entries
	protected abstract Object getIndexedEntry(); // get the added object
	protected abstract EObject getIndexParent(); // The parent where the index applies

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#getIndexPriority()
	 */
	protected IJavaFeatureMapper.VEexpressionPriority.VEpriorityIndex getIndexPriority() {		
		EStructuralFeature sf = fFmapper.getFeature(null);
		EObject parent = getIndexParent();
		int index  ;
		List indexedEntries = getIndexedEntries();
		Object entry = getIndexedEntry();
		if(indexedEntries!=null && entry!=null){
			index = -1;
			for(int i=0;i<indexedEntries.size();i++){
				Object component = indexedEntries.get(i);
				if(component.equals(entry)){
					index = i;
					break;
				}	
			}
			index++;
			return new IJavaFeatureMapper.VEexpressionPriority.VEpriorityIndex(sf, index, parent);
		}
		return new IJavaFeatureMapper.VEexpressionPriority.VEpriorityIndex(sf, 0, parent);
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ExpressionDecoderHelper#getSFPriority()
	 */
	protected int getSFPriority() {
		return IJavaFeatureMapper.PRIORITY_ADD ;
	}

	/**
	 * Try to figure out where ti insert this object, with accordance to its position
	 * in the code, in relation to other components.
	 */
	protected int findIndex (BeanPart target) {
    
		int index = -1;
    
		if (fOwner.getExprRef().getOffset()>-1){
	    
			int thisOffset = fOwner.getExprRef().getOffset();
	    
			BeanDecoderAdapter targetBPAdapter = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(target.getEObject(), ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
			ICodeGenAdapter[] addExpAdapters = targetBPAdapter.getSettingAdapters(fOwner.getSF());
			if(addExpAdapters!=null && addExpAdapters.length>0){
				index = 0;
				for(int i=0;i<addExpAdapters.length;i++){
					if (addExpAdapters[i] instanceof ExpressionDecoderAdapter) {
						ExpressionDecoderAdapter expAdapter = (ExpressionDecoderAdapter) addExpAdapters[i];
						try {
							int off = expAdapter.getBDMSourceRange().getOffset();
							if(off < thisOffset)
								index ++;
						} catch (CodeGenException e) {
							JavaVEPlugin.log(e, Level.WARNING);
						}
					}
				}
			}
		}
		return index;     
	}
}
