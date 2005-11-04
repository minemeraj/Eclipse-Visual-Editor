/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ViewerContainmentHandler.java,v $
 *  $Revision: 1.1 $  $Date: 2005-11-04 17:30:52 $ 
 */
package org.eclipse.ve.internal.jface;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.java.JavaHelpers;
import org.eclipse.jem.java.JavaRefFactory;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IContainmentHandler;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.swt.*;
 

/**
 * Viewer containment handler. This handles dropping a viewer onto a Composite or a special control (as defined by the {@link IExecutableExtension}.
 * In case of a composite it creates a Viewer and an implicit control. In case of a special control, it
 * creates a Viewer with the explicit control as its control setting.
 * <p>
 * <b>Note:</b> The special control is handled through the {@link IExecutableExtension#setInitializationData(IConfigurationElement, String, Object)} call.
 * This is called during initialization. If the Object is a String, then it is the name of the feature that is the "special" control feature. For
 * example for TreeViewer it would be "tree". If the feature name is set in the intialization, then that feature will be used to determine also
 * whether this viewer can be dropped onto the a control that is the type of the feature. Again for example TreeViewer. The tree feature is of type
 * org.eclipse.swt.widgets.Tree, so that means a TreeViewer can be dropped on a Tree and it will use the Tree as the control instead of creating
 * one for itself. If this feature name is not set, then it will only accept a Composite as the parent and it will create the implicit "control" feature
 * instead.
 *  
 * @since 1.2.0
 */
public class ViewerContainmentHandler implements IContainmentHandler, IExecutableExtension {
	
	protected String controlFeatureName;
	
	public Object contributeToDropRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean creation, EditDomain domain) throws StopRequestException {
		// TODO Assume creation for now. We'll see how to handle add later, this may change for that.
		if (creation) {
			if (parent instanceof IJavaObjectInstance) {
				IJavaObjectInstance pjo = (IJavaObjectInstance) parent;
				IJavaObjectInstance cjo = (IJavaObjectInstance) child;	// Assuming it is a viewer because this handler is being called.
				ResourceSet rset = EMFEditDomainHelper.getResourceSet(domain);
				// Need to check for special before composite because special could be an instance of composite.
				EStructuralFeature controlSF;
				if (controlFeatureName != null) {
					controlSF = cjo.getJavaType().getEStructuralFeature(controlFeatureName);
					if (controlSF.getEType().isInstance(pjo))
						return dropOnSpecialClass(pjo, cjo, controlSF, preCmds, domain);
				} else {
					controlSF = cjo.getJavaType().getEStructuralFeature("control");
				}
				JavaHelpers compositeClass = JavaRefFactory.eINSTANCE.reflectType("org.eclipse.swt.widgets.Composite", rset);
				if (compositeClass.isInstance(pjo)) {
					// Dropping tree viewer onto a composite.
					return dropOnComposite(pjo, cjo, controlSF, preCmds, rset);
				}
			}
		}
		throw new StopRequestException("Parent not valid for a Viewer");
	}

	/*
	 * Dropping on a composite.
	 */
	private Object dropOnComposite(IJavaObjectInstance parent, IJavaObjectInstance viewer, EStructuralFeature controlFeature, CommandBuilder preCmds, ResourceSet rset) {
		// Set the allocation to the parent.
		WidgetContainmentHandler.processAllocation(parent, viewer, preCmds);
		// Create a "control" with implicit allocation to the viewer, and set it into the "control" feature.
		ImplicitAllocation allocation = InstantiationFactory.eINSTANCE.createImplicitAllocation(viewer, controlFeature);
		IJavaInstance control = BeanUtilities.createJavaObject((JavaHelpers) controlFeature.getEType(), rset, allocation);
		preCmds.applyAttributeSetting(viewer, controlFeature, control);
		return control;
	}
	
	/*
	 * Dropping on a the special class.
	 */
	private Object dropOnSpecialClass(IJavaObjectInstance parentControl, IJavaObjectInstance viewer, EStructuralFeature controlFeature, CommandBuilder preCmds, EditDomain domain) {
		if (!viewer.isSetAllocation()) {
			// Needs an allocation. Use default of new Widget(parent, SWT.NONE);
			ParseTreeAllocation parseTreeAllocation = InstantiationFactory.eINSTANCE.createParseTreeAllocation();
			PTClassInstanceCreation classInstanceCreation = InstantiationFactory.eINSTANCE.createPTClassInstanceCreation(viewer.getJavaType().getQualifiedNameForReflection(), null);
			PTInstanceReference parentReference = InstantiationFactory.eINSTANCE.createPTInstanceReference(parentControl);
			classInstanceCreation.getArguments().add(parentReference);
			parseTreeAllocation.setExpression(classInstanceCreation);			
			preCmds.applyAttributeSetting(viewer, JavaInstantiation.getAllocationFeature(viewer), parseTreeAllocation);
		} else if (viewer.isParseTreeAllocation())
			preCmds.append(new EnsureCorrectParentCommand(viewer, parentControl));
		
		// Set the new viewer's "control" feature to the parent control.
		preCmds.applyAttributeSetting(viewer, controlFeature, parentControl);
		
		// Assign membership of the new Viewer to be relative to the control.
		RuledCommandBuilder rcb = new RuledCommandBuilder(domain);
		rcb.assignMembership(viewer, parentControl);
		preCmds.append(rcb.getCommand());
		return null;	// There is no child in this case because the parent is a control already.
	}	

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.IContainmentHandler#contributeToRemoveRequest(java.lang.Object, java.lang.Object, org.eclipse.ve.internal.cde.commands.CommandBuilder, org.eclipse.ve.internal.cde.commands.CommandBuilder, boolean, org.eclipse.ve.internal.cde.core.EditDomain)
	 */
	public Object contributeToRemoveRequest(Object parent, Object child, CommandBuilder preCmds, CommandBuilder postCmds, boolean orphan, EditDomain domain) throws StopRequestException {
		if (orphan)
			postCmds.append(new EnsureOrphanFromParentCommand((IJavaObjectInstance) child, (IJavaObjectInstance) parent));
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
	 */
	public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
		if (data instanceof String)
			controlFeatureName = (String) data;
	}
}
