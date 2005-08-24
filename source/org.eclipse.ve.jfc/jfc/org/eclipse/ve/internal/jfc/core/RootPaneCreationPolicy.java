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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: RootPaneCreationPolicy.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:38:10 $ 
 */

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import org.eclipse.ve.internal.cde.core.AnnotationPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.CDECreationTool.CreationPolicy;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaPackage;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

/**
 * @author richkulp
 *
 * This is used by RootPane implementers to create the content pane if not already have one.
 * It will create the content pane only if the class is a "javax.swing" class. Any other class
 * is a subclass of these and so should not have a content pane created.
 * 
 */
public class RootPaneCreationPolicy implements CreationPolicy {

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.emf.EMFCreationTool.CreationPolicy#getCommand(org.eclipse.gef.commands.Command, org.eclipse.ve.internal.cde.core.EditDomain, org.eclipse.gef.requests.CreateRequest)
	 */
	public Command getCommand(
		Command aCommand,
		EditDomain domain,
		CreateRequest aCreateRequest) {
		
		JavaClass javaClass = (JavaClass) aCreateRequest.getNewObjectType();
		// Only do this for those that are in javax.swing. (i.e. JFrame, JWindow, etc).
		if (((JavaPackage) javaClass.getEPackage()).getPackageName().equals("javax.swing")) { //$NON-NLS-1$
			// We should create the content pane if it doesn't have one.
			IJavaObjectInstance model = (IJavaObjectInstance) aCreateRequest.getNewObject();
			RuledCommandBuilder cbld = new RuledCommandBuilder(domain);
			createContentPane(cbld, domain, model);			
			cbld.append(aCommand);
			return cbld.getCommand(); 
		} 
				
		// Doesn't match the specific class or there was a content pane, so we will not create content pane.
		return aCommand;
	}

	/**
	 * Create the command into the command builder to create a content pane for the model passed in.
	 * @param cbld
	 * @param domain
	 * @param model
	 */
	public static void createContentPane(RuledCommandBuilder cbld, EditDomain domain, IJavaObjectInstance model) {
		EStructuralFeature sf_contentPane = ((JavaClass) model.getJavaType()).getEStructuralFeature("contentPane"); //$NON-NLS-1$			
		if (!model.eIsSet(sf_contentPane)) {
			boolean oldApply = cbld.setApplyRules(false);
				
			ResourceSet rset = EMFEditDomainHelper.getResourceSet(domain);
			final IJavaObjectInstance contentPane = (IJavaObjectInstance) BeanUtilities.createJavaObject("javax.swing.JPanel", //$NON-NLS-1$
				rset, (String)null); //$NON-NLS-1$
				
			IJavaInstance layoutBean = BeanUtilities.createJavaObject("java.awt.BorderLayout", //$NON-NLS-1$
				rset, (String)null); //$NON-NLS-1$
			if (layoutBean != null) {
				EStructuralFeature sf_containerLayout = JavaInstantiation.getSFeature(rset, JFCConstants.SF_CONTAINER_LAYOUT);
				contentPane.eSet(sf_containerLayout, layoutBean);
			}
	
			// We need to create an annotation for this poofed up contentpane
			EStringToStringMapEntryImpl ks =
				(EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
			ks.setKey(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
			ks.setValue("jContentPane"); //$NON-NLS-1$
			Command cmd = AnnotationPolicy.applyAnnotationSetting(contentPane, ks, domain);
			cbld.append(cmd);
						
			// Now apply it to the root container model
			cbld.setApplyRules(true);
			boolean oldProperty = cbld.setPropertyRule(false);
			cbld.applyAttributeSetting(model, sf_contentPane, contentPane);
			cbld.setApplyRules(oldApply);
			cbld.setPropertyRule(oldProperty);
		}
	}

}
