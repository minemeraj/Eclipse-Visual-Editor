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
 *  $RCSfile: DefaultGraphicalEditPart.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:12:48 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.draw2d.*;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
/**
 * A simple graphical editpart for a EMF object. It is simply
 * an icon (somepart.gif) with a label which is the EMF ID of the
 * object. This can be used as a default for any object that don't
 * know what to do with. This is marked in ClassDescriptorDecoratorPolicy
 * as the default graph edit part.
 */

public class DefaultGraphicalEditPart extends AbstractGraphicalEditPart {

	protected DefaultLabelProviderNotifier labelProviderNotifier;

	public void activate() {
		super.activate();
		setupLabelProvider();
		refreshVisuals(); // Because refreshVisuals is called normally BEFORE activation.
	}
	
	protected void setupLabelProvider(){
		
		labelProviderNotifier = new DefaultLabelProviderNotifier();
		labelProviderNotifier
			.setModel(
				(EObject) getModel(),
				EditDomain.getEditDomain(this),
				new DefaultLabelProviderNotifier.IDefaultLabelProviderListener() {
					public void refreshLabel(final ILabelProvider provider) {
						CDEUtilities.displayExec(DefaultGraphicalEditPart.this, "REFRESH_VISUALS", new EditPartRunnable(DefaultGraphicalEditPart.this) { //$NON-NLS-1$

							protected void doRun() {
								DefaultGraphicalEditPart.this.refreshVisuals(provider);
							}
						});
					}
				},
				ClassDescriptorDecoratorPolicy.getPolicy(this).getLabelProvider(((EObject) getModel()).eClass()));		
		
	}

	public void deactivate() {
		if (labelProviderNotifier != null) {
			ILabelProvider provider = labelProviderNotifier.getLabelProvider();
			labelProviderNotifier.setModel(null, null, null, null);
			labelProviderNotifier = null;
			if (provider != null)
				provider.dispose();
		}
		super.deactivate();
	}

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DefaultComponentEditPolicy());
	}

	protected IFigure createFigure() {
		Label label = new Label("?"); //$NON-NLS-1$
		label.setTextPlacement(PositionConstants.SOUTH);
		return label;
	}

	protected void refreshVisuals() {
		if (labelProviderNotifier != null)
			refreshVisuals(labelProviderNotifier.getLabelProvider());
	}

	private void refreshVisuals(ILabelProvider provider) {
		Label fig = (Label) getFigure();
		if (provider != null) {
			setFigureImage(fig, provider.getImage(getModel()));
			setFigureLabel(fig, provider.getText(getModel()));
		} else {
			fig.setText("?"); //$NON-NLS-1$
		}
	}

	protected void setFigureLabel(Label aLabel, String aString) {
		aLabel.setText(aString);
	}

	protected void setFigureImage(Label aLabel, Image anImage) {
		aLabel.setIcon(anImage);
	}

	public Object getAdapter(Class key) {
		if (key == IPropertySource.class)
			return EcoreUtil.getRegisteredAdapter((EObject) getModel(), IPropertySource.class);
		else
			return super.getAdapter(key);
	}

}
