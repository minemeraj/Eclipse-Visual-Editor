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
 *  $RCSfile: IAnnotationDecoder.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.ve.internal.java.codegen.core.IDiagramModelInstance;
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
      
   // Generate the source
   public String generate(EStructuralFeature sf, Object[] args) throws CodeGenException ;

   public void setBeanModel(IBeanDeclModel model) ;
   public IBeanDeclModel getBeanModel() ;   
   public void setCompositionModel(IDiagramModelInstance cm) ;
   public IDiagramModelInstance getCompositionModel()  ;   
   public void setBeanPart(BeanPart part) ;
   public BeanPart getBeanPart() ;
   public boolean isDeleted() throws CodeGenException  ;
   public void delete () ;
   public void dispose()  ;
   // Return a vector that contain a set of pair elements:
   // child component, the SF associated with the relationship of the child   
   public void deleteFromComposition () ;
   public void reflectMOFchange() ;
}
