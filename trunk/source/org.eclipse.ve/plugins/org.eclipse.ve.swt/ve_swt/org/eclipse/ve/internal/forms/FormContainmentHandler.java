/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FormContainmentHandler.java,v $
 *  $Revision: 1.1 $  $Date: 2006-02-06 17:14:42 $ 
 */
package org.eclipse.ve.internal.forms;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.ImplicitAllocation;
import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.BeanUtilities;

import org.eclipse.ve.internal.swt.CompositeContainmentHandler;
import org.eclipse.ve.internal.swt.DefaultSWTLayoutPolicy;
 

/**
 * Containment for Forms. It makes sure that the "body" is created local and that the default layout is set on it.
 * @since 1.2.0
 */
public class FormContainmentHandler extends CompositeContainmentHandler {

	/**
	 * @param model
	 * 
	 * @since 1.2.0
	 */
	public FormContainmentHandler(Object model) {
		super(model);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.swt.CompositeContainmentHandler#contributeToDropRequest(java.lang.Object, java.lang.Object, org.eclipse.ve.internal.cde.commands.CommandBuilder, org.eclipse.ve.internal.cde.commands.CommandBuilder, boolean, org.eclipse.ve.internal.cde.core.EditDomain)
	 */
	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws StopRequestException {
		child = super.contributeToDropRequest(parent, child, preCmds, postCmds, creation, domain);
		if (creation && child instanceof IJavaObjectInstance) {
			// If the "body" is not created, create it now and make it local_local, and put on default layout. If "body" is alreade allocated, leave it alone. Somebody else did it
			// and we don't want to mess that up.
			IJavaInstance jChild = (IJavaInstance) child;
			ResourceSet rset = EMFEditDomainHelper.getResourceSet(domain);
			EStructuralFeature sf_body = JavaInstantiation.getSFeature(rset, FormsConstants.SF_FORM_BODY);
			if (!jChild.eIsSet(sf_body)) {
				ImplicitAllocation allocation = InstantiationFactory.eINSTANCE.createImplicitAllocation(jChild, sf_body);
				IJavaInstance body = BeanUtilities.createJavaObject((JavaHelpers) sf_body.getEType(), rset, allocation);
				preCmds.applyAttributeSetting(jChild, sf_body, body);
				// TODO There is a bug in codegen that causes it to ignore property settings that require a local variable or global local if those
				// property settings are being created at the same time as the init method for the main object (in this case Form). 
				// It leaves them as just properties with no variable.
//				// Normally implicits are not made local but left implicit. But for convienence and readability we are going to make a local_local.
//				EStringToStringMapEntryImpl ks =
//					(EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
//				ks.setKey(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
//				ks.setValue("body"); //$NON-NLS-1$
//				preCmds.append(AnnotationPolicy.applyAnnotationSetting(body, ks, domain));
//				KeyedInstanceLocationImpl kl = (KeyedInstanceLocationImpl) JCMFactory.eINSTANCE.create(JCMPackage.eINSTANCE.getKeyedInstanceLocation());
//				kl.setKey(VCEPreSetCommand.BEAN_LOCATION_KEY);
//				kl.setTypedValue(InstanceLocation.GLOBAL_LOCAL_LITERAL);
//				preCmds.append(AnnotationPolicy.applyAnnotationSetting(body, kl, domain));
				preCmds.append(DefaultSWTLayoutPolicy.processDefaultLayout(domain, body, null));
			}
		}
		return child;
	}

}
