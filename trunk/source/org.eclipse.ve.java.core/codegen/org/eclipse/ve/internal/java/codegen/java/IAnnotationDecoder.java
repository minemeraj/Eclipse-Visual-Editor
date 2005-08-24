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
 *  $RCSfile: IAnnotationDecoder.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:30:44 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.ve.internal.java.codegen.core.IVEModelInstance;
import org.eclipse.ve.internal.java.codegen.model.BeanPart;
import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;

/**
 * @version 	1.0
 * @author
 */
public interface IAnnotationDecoder {
    
   // A decode method will decode specific Annotaion.
   
   public boolean decode () throws CodeGenException ;
   /**
	 * The decoder should initialized itself.  This is typically called
	 * when the EMF model was constructed from cache, and the BDM is building
	 * in the background.   
	 * A restore should never update the EMF model.  It is implemented
	 * to initialize the decoder propertly, as if a decode was called.
	 * It is possible that the expression can not be decoded and most likely
	 * there are no art effects representing this expression in the model (a case 
	 * where a decode() will return false, or throw an exception)
	 * 
	 * @return same semantics of the decode 
	 * @throws CodeGenException
	 */
   public boolean restore() throws CodeGenException ;
      
   // Generate the source
   public String generate(EStructuralFeature sf, Object[] args) throws CodeGenException ;

   public void setBeanModel(IBeanDeclModel model) ;
   public IBeanDeclModel getBeanModel() ;   
   public void setCompositionModel(IVEModelInstance cm) ;
   public IVEModelInstance getCompositionModel()  ;   
   public void setBeanPart(BeanPart part) ;
   public BeanPart getBeanPart() ;
   public boolean isDeleted() throws CodeGenException  ;
   // Delete from from the source code
   public void delete () ;
   // Clean up and remove from the model
   public void dispose()  ;
   // Return a vector that contain a set of pair elements:
   // child component, the SF associated with the relationship of the child   
   public void deleteFromComposition () ;
   public void reflectMOFchange() ;
}
