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
package org.eclipse.ve.internal.cde.emf;
/*
 *  $RCSfile: DefaultTreeEditPart.java,v $
 *  $Revision: 1.10 $  $Date: 2005-08-24 23:12:48 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.*;
/**
 *  A base tree editpart for EMF objects that will be using the CDE extensions paradighm. 
 *  (I.e. gets label provider from ClassDescriptorDecoratorPolicy, among other things.
 *  
 *  @since 1.0.0
 */
public class DefaultTreeEditPart extends AbstractTreeEditPart {

	protected ILabelDecorator labelDecorator;
	protected DefaultLabelProviderNotifier labelProviderNotifier;

	/**
	 * Construct with model.
	 * @param model
	 * 
	 * @since 1.1.0
	 */
	public DefaultTreeEditPart(Object model) {
		setModel(model);
	}
	
	public void activate() {
		super.activate();
		ILabelProvider provider = getDecoratedLabelProvider();
		labelProviderNotifier = new DefaultLabelProviderNotifier();
		labelProviderNotifier
			.setModel(
				(EObject) getModel(),
				EditDomain.getEditDomain(this),
				new DefaultLabelProviderNotifier.IDefaultLabelProviderListener() {
			public void refreshLabel(ILabelProvider provider) {
				CDEUtilities.displayExec(DefaultTreeEditPart.this, "REFRESH_VISUALS", new EditPartRunnable(DefaultTreeEditPart.this) { //$NON-NLS-1$

					protected void doRun() {
						DefaultTreeEditPart.this.refreshVisuals();
					}
				});
			}
		}, provider);
		refreshVisuals(); // Because refreshVisuals is usually called BEFORE activation.
	}

	private ILabelProvider getDecoratedLabelProvider() {
		ILabelProvider provider = ClassDescriptorDecoratorPolicy.getPolicy(this).getLabelProvider(((EObject) getModel()).eClass());
		if (labelDecorator != null) {
			provider = new DecoratingLabelProvider(provider, labelDecorator);
		}
		return provider;
	}

	public void deactivate() {
		if (labelProviderNotifier != null) {
			ILabelProvider provider = labelProviderNotifier.getLabelProvider();
			labelProviderNotifier.setModel(null, null, null, null);
			labelProviderNotifier = null;
			if (provider instanceof DecoratingLabelProvider)
				provider = ((DecoratingLabelProvider) provider).getLabelProvider();
			// Really only want to dispose provider, not label decorator, that will be reused.
			if (provider != null)
				provider.dispose();
		}
		super.deactivate();
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new TreePrimaryDragRoleEditPolicy());
	}


	/**
	 * Add a label decorator to the editpart. This is used to provide additional decoration to the 
	 * default label provider. 
	 * <p>
	 * The decorator is not owned by this edit part. If it needs to
	 * be disposed, and that is the responsibility of the caller.
	 * @param decorator
	 * 
	 * @since 1.0.0
	 */
	public void setLabelDecorator(ILabelDecorator decorator) {
		labelDecorator = decorator;
		if (isActive()) {
			ILabelProvider provider = labelProviderNotifier.getLabelProvider();
			if (provider instanceof DecoratingLabelProvider)
				 ((DecoratingLabelProvider) provider).setLabelDecorator(labelDecorator);
			else {
				ILabelProvider newProvider = new DecoratingLabelProvider(provider, labelDecorator);
				labelProviderNotifier.setLabelProvider(newProvider);
			}
			refreshVisuals();
		}
	}

	public Object getAdapter(Class key) {
		if (key == IPropertySource.class)
			return EcoreUtil.getRegisteredAdapter((EObject) getModel(), IPropertySource.class);	// For the property sheet.
		else
			return super.getAdapter(key);
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getImage()
	 */
	protected Image getImage() {
		return labelProviderNotifier != null ? labelProviderNotifier.getLabelProvider().getImage(getModel()) : super.getImage();
	}

	/**
	 * @see org.eclipse.gef.editparts.AbstractTreeEditPart#getText()
	 */
	protected String getText() {
		return labelProviderNotifier != null ? labelProviderNotifier.getLabelProvider().getText(getModel()) : CDEEmfMessages.DefaultTreeEditPart_getText_NoLabelProvider; 
	}

	/**
	 * Get the label decorator, if any.
	 * @return Returns the labelDecorator or <code>null</code> if there isn't one.
	 */
	public ILabelDecorator getLabelDecorator() {
		return labelDecorator;
	}
}
