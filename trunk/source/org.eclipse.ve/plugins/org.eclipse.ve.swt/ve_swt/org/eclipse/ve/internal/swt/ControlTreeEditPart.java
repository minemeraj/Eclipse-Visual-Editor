/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ControlTreeEditPart.java,v $
 *  $Revision: 1.5 $  $Date: 2005-06-15 20:19:21 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.core.IErrorNotifier;
import org.eclipse.ve.internal.cde.properties.PropertySourceAdapter;

import org.eclipse.ve.internal.java.core.*;
 
/**
 * TreeEditPart for a SWT Control.
 */
public class ControlTreeEditPart extends JavaBeanTreeEditPart {
	
	private IPropertyDescriptor sfDirectEditProperty;

	private BeanTreeDirectEditManager manager;

	protected IPropertySource propertySource;
	
	private IErrorNotifier otherNotifier;
	
	protected IErrorNotifier.CompoundErrorNotifier errorNotifier = new IErrorNotifier.CompoundErrorNotifier();	

	public ControlTreeEditPart(Object model) {
		super(model);
	}
	
	protected IErrorNotifier getErrorNotifier() {
		return errorNotifier;
	}
	
	public void activate() {
		super.activate();
		errorNotifier.addErrorNotifier((IErrorNotifier) EcoreUtil.getExistingAdapter((Notifier) getModel(), IErrorNotifier.ERROR_NOTIFIER_TYPE));	// This will signal initial severity if not none.
		errorNotifier.addErrorNotifier(otherNotifier);

	}
	
	public void deactivate() {
		errorNotifier.dispose();
		super.deactivate();
	}

	public Object getAdapter(Class type) {
		if (type == IPropertySource.class)
			if (propertySource != null)
				return propertySource;
			else
				return EcoreUtil.getRegisteredAdapter((EObject)getModel(), IPropertySource.class);
				
		return super.getAdapter(type);
	}
	
	protected void createEditPolicies() {
		super.createEditPolicies();
		sfDirectEditProperty = getDirectEditTargetProperty();
		if (sfDirectEditProperty != null) {
			EditDomain ed = EditDomain.getEditDomain(this);
			EditPartViewer viewer = getRoot().getViewer();
			manager = BeanTreeDirectEditManager.getDirectEditManager(ed, viewer);
		}
		installEditPolicy(CopyAction.REQ_COPY,new ControlCopyEditPolicy());		
	}
	
	private IPropertyDescriptor getDirectEditTargetProperty() {

		EStructuralFeature feature = null;
		IJavaObjectInstance component = (IJavaObjectInstance) getModel();
		JavaClass modelType = (JavaClass) component.eClass();
		// Hard coded string properties to direct edit.
		// If more than one is available, it'll choose the first in the list
		// below
		feature = modelType.getEStructuralFeature("label"); //$NON-NLS-1$
		if (feature == null) {
			feature = modelType.getEStructuralFeature("text"); //$NON-NLS-1$
		}
		if (feature == null) {
			feature = modelType.getEStructuralFeature("title"); //$NON-NLS-1$	
		}
		if (feature != null) {
			IPropertySource source = (IPropertySource) getAdapter(IPropertySource.class);
			return PropertySourceAdapter.getDescriptorForID(source, feature);
		} else
			return null;
	}

	
	private void performDirectEdit(){
		if(manager != null)
			manager.performDirectEdit(this, sfDirectEditProperty);
	}

	public void performRequest(Request request){
		if (request.getType() == RequestConstants.REQ_DIRECT_EDIT && sfDirectEditProperty != null)
			performDirectEdit();
		else
			super.performRequest(request);
	}

	/**
	 * Used by other graphical editparts to say even though this is modeling an swt.Control, use this
	 * guy as the property source. This is used by CompositeTreeEditPart, or TabFolderTreeEditPart, or
	 * any other container type editpart that uses an intermediate object. The intermediate object will
	 * be responsible for showing through the correct awt.Component properties.
	 * @param source
	 * 
	 * @since 1.1.0
	 */
	public void setPropertySource(IPropertySource source) {
		propertySource = source;
	}
	
	/**
	 * Used by other graphical editparts to say even though this is modeling an swt.Control, use this guy as an error notifier. This is used by
	 * CompositeTreeEditPart, or TabFolderTreeEditPart, or any other container type editpart that uses an intermediate object. This component will
	 * then show the errors from itself (the awt.Component) and from the error notifier set in. Only one can be set at a time. A new set will remove
	 * the old one from the list.
	 * 
	 * @param otherNotifier
	 * 
	 * @since 1.1.0
	 */
	public void setErrorNotifier(IErrorNotifier otherNotifier) {
		if (this.otherNotifier != null)
			errorNotifier.removeErrorNotifier(this.otherNotifier);
		this.otherNotifier = otherNotifier;
		if (isActive())
			errorNotifier.addErrorNotifier(this.otherNotifier);	// Don't do if not active. When activated it will add it.
	}	

}
