package org.eclipse.ve.internal.cde.emf;
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
 *  $RCSfile: DefaultTreeEditPart.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:07 $ 
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
 * A simple tree editpart for a EMF object. It is simply
 * an icon (somepart.gif) with a label which is the EMF ID of the
 * object. This can be used as a default for any object that don't
 * know what to do with. This is marked in ClassDescriptorDecoratorPolicy
 * as the default graph edit part.
 */
public class DefaultTreeEditPart extends AbstractTreeEditPart {

	protected ILabelDecorator labelDecorator;
	protected DefaultLabelProviderNotifier labelProviderNotifier;

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
				DefaultTreeEditPart.this.refreshVisuals();
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

	public DefaultTreeEditPart(Object model) {
		setModel(model);
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new TreePrimaryDragRoleEditPolicy());
	}


	/**
	 * This is used to add a label decorator to the label provider.
	 * The decorator is not owned by this edit part. If it needs to
	 * be disposed, that is the responsibility of the caller.
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
			return EcoreUtil.getRegisteredAdapter((EObject) getModel(), IPropertySource.class);
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
		return labelProviderNotifier != null ? labelProviderNotifier.getLabelProvider().getText(getModel()) : CDEEmfMessages.getString("DefaultTreeEditPart.getText.NoLabelProvider"); //$NON-NLS-1$
	}

}
