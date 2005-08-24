/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
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
 *  $RCSfile: AbstractAnnotationDecoder.java,v $
 *  $Revision: 1.13 $  $Date: 2005-08-24 23:30:44 $ 
 */
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.ve.internal.cdm.*;
import org.eclipse.ve.internal.cdm.impl.KeyedBooleanImpl;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenUtil;

/**
 * @version 	1.0
 * @author
 */
public abstract class AbstractAnnotationDecoder implements IAnnotationDecoder {
    
    IBeanDeclModel fModel = null ;
    IVEModelInstance fCompositionModel = null ;
    BeanPart       fBeanpart = null ;
    
    String         fAnnotationKey = null ;
    String         fContent = null ;

    /**
     * Constructor for AbstractAnnotationDecoder.
     */
    public AbstractAnnotationDecoder(String key,BeanPart bean) {
        super();
        fAnnotationKey = key ;
        fBeanpart = bean ;
        if (bean != null) {
           fModel = bean.getModel() ;          
           fCompositionModel = bean.getModel().getCompositionModel() ;
        }
    }
    
//    public void setAnnotationKey(String key) {
//        fAnnotationKey = key ;
//    }
    
    public String getAnnotationKey() {
        return fAnnotationKey ;
    }
        
    /*
     * @see IAnnotationDecoder#setBeanModel(IBeanDeclModel)
     */
    public void setBeanModel(IBeanDeclModel model) {
        fModel = model ;
    }

    /*
     * @see IAnnotationDecoder#getBeanModel()
     */
    public IBeanDeclModel getBeanModel() {
        return fModel ;
    }

    /*
     * @see IAnnotationDecoder#setCompositionModel(IDiagramModelInstance)
     */
    public void setCompositionModel(IVEModelInstance cm) {
        fCompositionModel = cm ;
    }

    /*
     * @see IAnnotationDecoder#getCompositionModel()
     */
    public IVEModelInstance getCompositionModel() {
        return fCompositionModel ;
    }

    /*
     * @see IAnnotationDecoder#setBeanPart(BeanPart)
     */
    public void setBeanPart(BeanPart part) {
        fBeanpart = part ;
    }

    /*
     * @see IAnnotationDecoder#getBeanPart()
     */
    public BeanPart getBeanPart() {
        return fBeanpart ;
    }


    protected Object getAnnotationValue() throws CodeGenException {
        if (fAnnotationKey == null || fBeanpart == null) 
           throw new CodeGenException ("not initialized") ; //$NON-NLS-1$

        Annotation a = CodeGenUtil.getAnnotation(fBeanpart.getEObject()) ;
        if (a == null) return null ;
        
        VisualInfo vi = a.getVisualInfo(fCompositionModel.getDiagram()) ;
        if (vi == null) return null ;
        return vi.getKeyedValues().get(fAnnotationKey) ;
    }
    
    protected void setAnnotationValue (BasicEMap.Entry val) throws CodeGenException {
        if (fAnnotationKey == null || fBeanpart == null) 
           throw new CodeGenException ("not initialized") ; //$NON-NLS-1$

        Annotation a = CodeGenUtil.getAnnotation(fBeanpart.getEObject()) ;
        if (a == null) throw new CodeGenException ("not initialized") ; //$NON-NLS-1$
        
        VisualInfo vi = a.getVisualInfo(fCompositionModel.getDiagram()) ;
        if (vi == null)  {
            // Create a new Visual Info
	        vi = CDMFactory.eINSTANCE.createVisualInfo() ;	        
            vi.setDiagram(fCompositionModel.getDiagram()) ;
            a.getVisualInfos().add(vi) ;
            
            ICodeGenAdapter adapter = (ICodeGenAdapter)EcoreUtil.getExistingAdapter(a, ICodeGenAdapter.JVE_CODEGEN_ANNOTATION_ADAPTER) ;
            if (adapter == null) throw new CodeGenException ("No Adapter") ; //$NON-NLS-1$
            vi.eAdapters().add(adapter) ;
        }
        
        val.setKey(fAnnotationKey);
        if(vi.getKeyedValues().containsKey(fAnnotationKey))
        	vi.getKeyedValues().removeKey(fAnnotationKey);
       	vi.getKeyedValues().add(val) ;
    }
    
    /*
     * @see IAnnotationDecoder#isDeleted()
     */
          
    public boolean isDeleted() throws CodeGenException {
        
        if (getAnnotationValue() == null) return true ;
        else return false ;
        
    }

    /*
     * @see IAnnotationDecoder#delete()
     */
    public void delete() {
    	//TODO:
    }

    protected void clearAnnotation() {
    	if (fAnnotationKey == null || fBeanpart == null) return ; 

    	Annotation a = CodeGenUtil.getAnnotation(fBeanpart.getEObject()) ;
    	if (a == null) return ;
    	if (a.eContainer() != null)
    		((EList)a.eContainer().eGet(a.eContainmentFeature())).remove(a) ;
    	
    	VisualInfo vi = a.getVisualInfo(fCompositionModel.getDiagram()) ;
    	if (vi == null)  return ;    	    
    	ICodeGenAdapter adapter = (ICodeGenAdapter)EcoreUtil.getExistingAdapter(a, ICodeGenAdapter.JVE_CODEGEN_ANNOTATION_ADAPTER) ;
    	if (adapter != null) 
    		vi.eAdapters().remove(adapter) ;    
    	vi.getKeyedValues().removeKey(fAnnotationKey);
    }
    
    protected boolean noAnnotationInSource(boolean keepOnFreeForm) {
    	boolean decoded = true;
    	if (fAnnotationKey == null || fBeanpart == null) return false; 
    	
    	try {
    	// Smart decode
    	 Object val = getAnnotationValue();
    	 if (val instanceof Boolean) {
    		Boolean bVal = (Boolean)val;
    		if (bVal.booleanValue() == !keepOnFreeForm) // The model has it reversed
    			return decoded;
    	 }
    	}
    	catch (CodeGenException e) {}
     
    	

    	Annotation a = CodeGenUtil.getAnnotation(fBeanpart.getEObject()) ;
    	if (a == null) return false;
    	
      	KeyedBooleanImpl c = (KeyedBooleanImpl) CDMFactory.eINSTANCE.create(CDMPackage.eINSTANCE.getKeyedBoolean());
    	c.setValue(new Boolean(!keepOnFreeForm)) ; // The model has it reversed
    	try {
			setAnnotationValue (c) ;
		}
		catch (Exception e) {
			decoded = false;
		}
    	
		return decoded;
    }
    
    /*
     * @see IAnnotationDecoder#dispose()
     */
    public void dispose() {
    	clearAnnotation();
    	fAnnotationKey=null ;
    	fModel=null ;
    }

    /*
     * @see IAnnotationDecoder#deleteFromComposition()
     */
    public void deleteFromComposition() {
    }
    
    public String toString() {
        return super.toString()+":"+fAnnotationKey+"\n\t"+fContent ; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
