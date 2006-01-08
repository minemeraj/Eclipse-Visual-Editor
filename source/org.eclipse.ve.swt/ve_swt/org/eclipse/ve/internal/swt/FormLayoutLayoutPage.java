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
 *  $RCSfile: FormLayoutLayoutPage.java,v $
 *  $Revision: 1.1 $  $Date: 2006-01-08 03:28:35 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
 

/**
 * 
 * @since 1.0.0
 */
public class FormLayoutLayoutPage extends JavaBeanCustomizeLayoutPage {
	
	public final static int SPACING_CHANGED = 1;
	public final static int MARGIN_HEIGHT_CHANGED = 2;
	public final static int MARGIN_WIDTH_CHANGED = 3;

	EditPart fEditPart = null;
	
	ResourceSet rset;
	
	boolean allEnabled;
	
	protected EReference sfCompositeLayout;
	
	EStructuralFeature sfSpacing, sfMarginHeight, sfMarginWidth;

	private int spacingValue, marginHeightValue, marginWidthValue;
	
	private FormLayoutLayoutComposite formComposite = null;

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.CustomizeLayoutPage#handleSelectionProviderInitialization(org.eclipse.jface.viewers.ISelectionProvider)
	 */
	protected void handleSelectionProviderInitialization(ISelectionProvider selectionProvider) {
		// We don't use GEF SelectionActions, so don't need this.
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.CustomizeLayoutPage#getControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control getControl(Composite parent) {
		formComposite = new FormLayoutLayoutComposite(this, parent, SWT.NONE);
		if (allEnabled)
			initializeValues();
		return formComposite;
	}

	/*
	 * Handle property changes from the FormLayoutLayoutComposite
	 */
	protected void propertyChanged (int type, int intValue) {
		// toggle allEnabled to prevent recursive change events
		allEnabled = false;
		switch (type) {
			case SPACING_CHANGED:
				if (spacingValue != intValue) {
					spacingValue = intValue;
					execute(createSpinnerCommand(fEditPart, sfSpacing, String.valueOf(intValue)));
				}
					break;
			case MARGIN_HEIGHT_CHANGED:
				if (marginHeightValue != intValue) {
					marginHeightValue = intValue;
					execute(createSpinnerCommand(fEditPart, sfMarginHeight, String.valueOf(intValue)));
				}
					break;
			case MARGIN_WIDTH_CHANGED:
				if (marginWidthValue != intValue) {
					marginWidthValue = intValue;
					execute(createSpinnerCommand(fEditPart, sfMarginWidth, String.valueOf(intValue)));
				}
					break;
			default:
				break;
		}
		allEnabled = true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.CustomizeLayoutPage#handleSelectionChanged(org.eclipse.jface.viewers.ISelection)
	 */
	protected boolean handleSelectionChanged(ISelection oldSelection) {
		ISelection newSelection = getSelection();
		allEnabled = false;
		if (newSelection != null && newSelection instanceof IStructuredSelection && !((IStructuredSelection) newSelection).isEmpty()) {
			List editparts = ((IStructuredSelection) newSelection).toList();
			EditPart firstParent;
			allEnabled = true;
			
			// Check to see if this is a single selected container
			if (editparts.size() == 1 && editparts.get(0) instanceof EditPart) {
				firstParent = (EditPart)editparts.get(0);
				// check to see if this is a container with a FormLayout
				if (isValidTarget(firstParent)) {
					fEditPart = firstParent;
					initializeValues();
					return true;
				}
			}
			
			// Else check to see if the parent of all the selected components is a form layout
			if (editparts.get(0) instanceof EditPart && ((EditPart) editparts.get(0)).getParent() != null) {
				firstParent = ((EditPart) editparts.get(0)).getParent();
				// Check the parent to ensure its layout policy is a FormLayout
				if (isValidTarget(firstParent)) {
					EditPart ep = (EditPart) editparts.get(0);
					/*
					 * Need to iterate through the selection list and ensure each selection is:
					 * - an EditPart
					 * - they share the same parent
					 * - it's parent has a FormLayout as it's layout manager
					 */
					for (int i = 1; i < editparts.size(); i++) {
						if (editparts.get(i) instanceof EditPart) {
							ep = (EditPart) editparts.get(i);
							// Check to see if we have the same parent
							if (ep.getParent() == null || ep.getParent() != firstParent) {
								allEnabled = false;
								break;
							}
						} else {
							allEnabled = false;
							break;
						}
					}
					// If the parent is the same, enable all the actions and see if all the anchor & fill values are the same.
					if (allEnabled) {
						fEditPart = firstParent;
						initializeValues();
						return true;
					}
				}
			}
		}
		allEnabled = false;
		fEditPart = null;
		// By default if the initial checks failed, disable and uncheck all the actions.
		return false;
	}
	
	/*
	 * Return true if the parent's layout policy is a FormLayout.
	 * If parent is a tree editpart (selected from the Beans viewer, we need to get its
	 * corresponding graphical editpart from the Graph viewer in order to check its layout policy.
	 */
	public boolean isValidTarget(EditPart target) {
		if (target instanceof TreeEditPart) {
			EditDomain ed = EditDomain.getEditDomain(target);
			EditPartViewer viewer = (EditPartViewer) ed.getEditorPart().getAdapter(EditPartViewer.class);
			if (viewer != null) {
				// Get the graphical editpart using the model that is common between the two viewers
				EditPart ep = (EditPart) viewer.getEditPartRegistry().get(target.getModel());
				if (ep != null)
					target = ep;
			}
		}
		IActionFilter af = (IActionFilter) ((IAdaptable) target).getAdapter(IActionFilter.class);
		if (af != null && af.testAttribute(target, LAYOUT_FILTER_KEY, FormLayoutEditPolicy.LAYOUT_ID)) { //$NON-NLS-1$ //$NON-NLS-2$
			return true;
		}
		return false;
	}
	
	protected boolean selectionIsContainer(ISelection oldSelection) {
		ISelection newSelection = getSelection();
		if (newSelection != null && newSelection instanceof IStructuredSelection && !((IStructuredSelection) newSelection).isEmpty()) {
			List editparts = ((IStructuredSelection) newSelection).toList();
			EditPart firstParent;
			
			// Check to see if this is a single selected container
			if (editparts.size() == 1 && editparts.get(0) instanceof EditPart) {
				firstParent = (EditPart)editparts.get(0);
				// check to see if this is a container with a FormLayout
				if (isValidTarget(firstParent)) {
					return true;
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.CustomizeLayoutPage#handleEditorPartChanged(org.eclipse.ui.IEditorPart)
	 */
	protected void handleEditorPartChanged(IEditorPart oldEditorPart) {
		resetVariables();
	}
	
	protected void refresh() {
		if (allEnabled) {
			initializeValues();
		}
	}
	
	private void initializeValues() {
		getResourceSet(fEditPart);
		// break out early if getControl() hasn't been called yet.
		if (formComposite == null) { return; }
		Object[] values = new Object[3];
		spacingValue = getIntValue(fEditPart, sfSpacing);
		marginHeightValue = getIntValue(fEditPart, sfMarginHeight);
		marginWidthValue = getIntValue(fEditPart, sfMarginWidth);
		values[0] = new Integer(spacingValue);
		values[1] = new Integer(marginHeightValue);
		values[2] = new Integer(marginWidthValue);
		formComposite.setInitialValues(values);
	}
	
	/*
	 * Return the commands to set the FormLayout value of a Spinner for the selected editpart
	 */
	protected Command createSpinnerCommand(EditPart editpart, EStructuralFeature sf, String spinnerValue) {
		CommandBuilder cb = new CommandBuilder();
		EObject control = (EObject)editpart.getModel();
		if (control != null) {
			IJavaInstance formLayout = (IJavaInstance) control.eGet(sfCompositeLayout);
			if (formLayout != null) {
				RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(editpart), null, false);
				Object intObject = BeanUtilities.createJavaObject("int", rset, spinnerValue); //$NON-NLS-1$
				componentCB.applyAttributeSetting(formLayout, sf, intObject);
				componentCB.applyAttributeSetting(control, sfCompositeLayout, formLayout);
				cb.append(componentCB.getCommand());
			} else {
				// this shouldn't happen
				return UnexecutableCommand.INSTANCE;
			}
			return cb.getCommand();
		} else {
			return UnexecutableCommand.INSTANCE;
		}
	}

	protected int getIntValue(EditPart ep, EStructuralFeature sf) {
		IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		if (ps != null && getResourceSet(ep) != null) {
			IPropertySource formLayout = (IPropertySource) ps.getPropertyValue(sfCompositeLayout);
			if (formLayout != null) {
				Object intPV = formLayout.getPropertyValue(sf);
				if (intPV != null && intPV instanceof IJavaDataTypeInstance) {
					IIntegerBeanProxy intProxy = (IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaDataTypeInstance) intPV, rset);
					return intProxy.intValue();
				}
			}
		}
		return 0;
	}

	/*
	 * Executes the given command
	 */
	protected void execute(Command command) {
		if (command == null || !command.canExecute())
			return;
		CommandStack cmdStack = (CommandStack)getEditorPart().getAdapter(CommandStack.class);
		if (cmdStack != null)
			cmdStack.execute(command);
	}
	
	/*
	 * reset the resource set and structural features
	 */
	private void resetVariables() {
		rset = null;
		sfCompositeLayout = null;
		sfSpacing = null;
		sfMarginHeight = null;
		sfMarginWidth = null;
		allEnabled = false;
	}
	
	/*
	 * Return the ResourceSet for this editpart. Initialize the structural features also. 
	 */
	protected ResourceSet getResourceSet(EditPart editpart) {
		if (rset == null) {
			rset = EMFEditDomainHelper.getResourceSet(EditDomain.getEditDomain(editpart));
			sfCompositeLayout = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_LAYOUT); 
			sfSpacing = JavaInstantiation.getSFeature(rset, SWTConstants.SF_FORM_LAYOUT_SPACING);
			sfMarginHeight = JavaInstantiation.getSFeature(rset, SWTConstants.SF_FORM_LAYOUT_MARGIN_HEIGHT);
			sfMarginWidth = JavaInstantiation.getSFeature(rset, SWTConstants.SF_FORM_LAYOUT_MARGIN_WIDTH);
		}
		return rset;
	}

}
