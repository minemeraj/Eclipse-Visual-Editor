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
 *  $RCSfile: RootPaneContainmentHandler.java,v $
 *  $Revision: 1.3 $  $Date: 2005-12-14 21:37:04 $ 
 */

import org.eclipse.core.runtime.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.commands.Command;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.JavaPackage;

import org.eclipse.ve.internal.cdm.DiagramData;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.AnnotationPolicy;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

/**
 * @author richkulp
 *
 * This is used by RootPane implementers to create the content pane (for creation only) if not already have one.
 * It will create the content pane only if the class is a "javax.swing" class. Any other class
 * is a subclass of these and so should not have a content pane created.
 * <p>
 * The executable extension data if a boolean for should this child only be on the freeform. The 
 * default is <code>true</code>. So the override file should only need to use if <code>false</code> as 
 * it doesn't need to be on freeform.
 * 
 * @since 1.2.0
 */
public class RootPaneContainmentHandler extends AbstractComponentModelContainmentHandler implements IExecutableExtension {

	private boolean ffOnly = true;
	
	public RootPaneContainmentHandler(Object model) {
		super(model);
	}

	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws StopRequestException {
		if (ffOnly) {
			// Verify if on Freeform
			if (!(parent instanceof DiagramData))
				throw new StopRequestException(JFCMessages.RootPaneContainmentHandler_StopRequest_CanBeOnlyOnFF);			
		}
		if (creation && child instanceof IJavaObjectInstance) {
			IJavaObjectInstance jo = (IJavaObjectInstance) child;
			JavaClass javaClass = (JavaClass) jo.getJavaType();
			// Only do this for those that are in javax.swing. (i.e. JFrame, JWindow, etc). Not subclasses
			// because the subclass may of already supplied their own content pane.
			// TODO This should be turned into just create implicit content pane when that is working. Then it would be always
			// create an implicit setting.
			if (((JavaPackage) javaClass.getEPackage()).getPackageName().equals("javax.swing")) { //$NON-NLS-1$
				// We should create the content pane if it doesn't have one.
				handleContentPane(jo, domain, preCmds);
			} 
		}
		return child;
	}
	
	/**
	 * Static utility to handle the content pane portion of the RootPaneContainer. This must be called
	 * on creation only and BEFORE the child has been handled and added to the model. Otherwise it could cause a bad 
	 * location for the content pane and the child.
	 * @param jo
	 * @param domain
	 * @param preCmds
	 * 
	 * @since 1.2.0
	 */
	public static void handleContentPane(IJavaObjectInstance jo, EditDomain domain, CommandBuilder preCmds) {
		JavaClass javaClass = (JavaClass) jo.getJavaType();
		// Only do this for those that are in javax.swing. (i.e. JFrame, JWindow, etc). Not subclasses
		// because the subclass may of already supplied their own content pane.
		// TODO This should be turned into just create implicit content pane when that is working. Then it would be always
		// create an implicit setting.
		if (((JavaPackage) javaClass.getEPackage()).getPackageName().equals("javax.swing")) { //$NON-NLS-1$
			// We should create the content pane if it doesn't have one.
			createContentPane(preCmds, domain, jo);			
		} 
	}
	
	/**
	 * Create the command into the command builder to create a content pane for the model passed in.
	 * @param cbld
	 * @param domain
	 * @param model
	 */
	public static void createContentPane(CommandBuilder cbld, EditDomain domain, IJavaObjectInstance model) {
		// Content pane is not one common feature in the heirarchy. Each class implements its own. So we need to query it.
		EStructuralFeature sf_contentPane = ((JavaClass) model.getJavaType()).getEStructuralFeature("contentPane"); //$NON-NLS-1$			
		if (!model.eIsSet(sf_contentPane)) {
			RuledCommandBuilder rcb = cbld instanceof RuledCommandBuilder ? (RuledCommandBuilder) cbld : null;
			// Don't apply rules while building up content pane.
			boolean oldApplyRules = rcb != null ? rcb.setApplyRules(false) : false;
			ResourceSet rset = EMFEditDomainHelper.getResourceSet(domain);
			final IJavaObjectInstance contentPane = (IJavaObjectInstance) BeanUtilities.createJavaObject("javax.swing.JPanel", //$NON-NLS-1$
				rset, (String)null);
				
			IJavaInstance layoutBean = BeanUtilities.createJavaObject("java.awt.BorderLayout", //$NON-NLS-1$
				rset, (String)null);
			EStructuralFeature sf_containerLayout = JavaInstantiation.getSFeature(rset, JFCConstants.SF_CONTAINER_LAYOUT);
			contentPane.eSet(sf_containerLayout, layoutBean);
	
			// We need to create an annotation for this poofed up contentpane
			EStringToStringMapEntryImpl ks =
				(EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
			ks.setKey(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
			ks.setValue("jContentPane"); //$NON-NLS-1$
			Command cmd = AnnotationPolicy.applyAnnotationSetting(contentPane, ks, domain);
			cbld.append(cmd);

			// Now apply it to the root container model
			if (rcb != null) 
				rcb.setApplyRules(true);	// Start applying the rules
			boolean oldProperty = rcb != null ? rcb.setPropertyRule(false) : false;	// Make sure it is child style.
			cbld.applyAttributeSetting(model, sf_contentPane, contentPane);
			if (rcb != null) {
				rcb.setApplyRules(oldApplyRules);
				rcb.setPropertyRule(oldProperty);
			}

		}
	}

	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		if (data != null)
			try {
				ffOnly = Boolean.valueOf((String) data).booleanValue();
			} catch (ClassCastException e) {
			}
	}

}
