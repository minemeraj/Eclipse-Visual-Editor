/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: GridLayoutLayoutPage.java,v $
 *  $Revision: 1.8 $  $Date: 2005-06-22 14:36:44 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.Spinner;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;
 

/**
 * 
 * @since 1.0.0
 */
public class GridLayoutLayoutPage extends JavaBeanCustomizeLayoutPage {
	
	EditPart fEditPart = null;
	
	Spinner columnsSpinner;
	Spinner rowsSpinner;
	Spinner hgapSpinner;
	Spinner vgapSpinner;
	
	ResourceSet rset;
	
	protected EReference sfCompositeLayout;
	
	EStructuralFeature sfColumns, sfRows, 
		sfHgap, sfVgap;
	
	boolean initialized = false;

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
		Composite c = new Composite(parent, SWT.NONE);
		c.setLayout(new GridLayout());
		
		Listener spinnerModify = new Listener() {
			public void handleEvent(Event event) {
				spinnerModified((Spinner)event.widget);
			}
		};
		
		Group dimensionsGroup = new Group(c, SWT.NONE);
		GridData gd = new GridData();
		gd.verticalAlignment = GridData.BEGINNING;
		dimensionsGroup.setLayoutData(gd);
		GridLayout g2 = new GridLayout();
		g2.numColumns = 2;
		dimensionsGroup.setLayout(g2);
		dimensionsGroup.setText(JFCMessages.GridLayoutLayoutPage_gridDimensionsTitle); 
		Label label = new Label(dimensionsGroup, SWT.NONE);
		label.setText(JFCMessages.GridLayoutLayoutPage_columns); 
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		label.setLayoutData(gd);
		columnsSpinner = new Spinner(dimensionsGroup, SWT.NONE, 0);
		columnsSpinner.setMinimum(0);
		columnsSpinner.addModifyListener(spinnerModify);
		
		label = new Label(dimensionsGroup, SWT.NONE);
		label.setText(JFCMessages.GridLayoutLayoutPage_rows); 
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		label.setLayoutData(gd);
		rowsSpinner = new Spinner(dimensionsGroup, SWT.NONE, 0);
		rowsSpinner.setMinimum(0);
		rowsSpinner.addModifyListener(spinnerModify);
		
		Group spaceGroup = new Group(c, SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		spaceGroup.setLayoutData(gd);
		
		GridLayout g3 = new GridLayout();
		g3.numColumns = 2;
		spaceGroup.setLayout(g3);
		spaceGroup.setText(JFCMessages.GridLayoutLayoutPage_spacingTitle); 
		
		label = new Label(spaceGroup, SWT.NONE);
		label.setText(JFCMessages.GridLayoutLayoutPage_horizontalGap); 
		hgapSpinner = new Spinner(spaceGroup, SWT.NONE, 5);
		hgapSpinner.addModifyListener(spinnerModify);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		label.setLayoutData(gd);
		
		label = new Label(spaceGroup, SWT.NONE);
		label.setText(JFCMessages.GridLayoutLayoutPage_verticalGap); 
		vgapSpinner = new Spinner(spaceGroup, SWT.NONE, 5);
		vgapSpinner.addModifyListener(spinnerModify);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		label.setLayoutData(gd);
		
		if (fEditPart != null) {
			initialized = false;
			initializeValues();
		}
		
		return c;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.CustomizeLayoutPage#handleSelectionChanged(org.eclipse.jface.viewers.ISelection)
	 */
	protected boolean handleSelectionChanged(ISelection oldSelection) {
		ISelection newSelection = getSelection();
		if (newSelection != null && newSelection instanceof IStructuredSelection && !((IStructuredSelection) newSelection).isEmpty()) {
			List editparts = ((IStructuredSelection) newSelection).toList();
			EditPart firstParent;
			boolean enableAll = true;
			
			// Check to see if this is a single selected container
			if (editparts.size() == 1 && editparts.get(0) instanceof EditPart) {
				firstParent = (EditPart)editparts.get(0);
				// check to see if this is a container with a GridLayout
				if (isValidTarget(firstParent)) {
					fEditPart = firstParent;
					initialized = false;
					initializeValues();
					return true;
				}
			}
			
			// Else check to see if the parent of all the selected components is a grid layout
			if (editparts.get(0) instanceof EditPart && ((EditPart) editparts.get(0)).getParent() != null) {
				firstParent = ((EditPart) editparts.get(0)).getParent();
				// Check the parent to ensure its layout policy is a GridLayout
				if (isValidTarget(firstParent)) {
					EditPart ep = (EditPart) editparts.get(0);
					/*
					 * Need to iterate through the selection list and ensure each selection is:
					 * - an EditPart
					 * - they share the same parent
					 * - it's parent has a GridLayout as it's layout manager
					 */
					for (int i = 1; i < editparts.size(); i++) {
						if (editparts.get(i) instanceof EditPart) {
							ep = (EditPart) editparts.get(i);
							// Check to see if we have the same parent
							if (ep.getParent() == null || ep.getParent() != firstParent) {
								enableAll = false;
								break;
							}
						} else {
							enableAll = false;
							break;
						}
					}
					// If the parent is the same, enable all the actions and see if all the anchor & fill values are the same.
					if (enableAll) {
						fEditPart = firstParent;
						initialized = false;
						initializeValues();
						return true;
					}
				}
			}
		}
		fEditPart = null;
		// By default if the initial checks failed, disable and uncheck all the actions.
		return false;
	}
	
	/*
	 * Return true if the parent's layout policy is a GridLayout.
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
		if (af != null && af.testAttribute(target, LAYOUT_FILTER_KEY, GridLayoutEditPolicy.LAYOUT_ID)) { //$NON-NLS-1$ //$NON-NLS-2$
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
				// check to see if this is a container with a GridLayout
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
	
	private void initializeValues() {
		if (!initialized) {
			getResourceSet(fEditPart);
			// break out early if getControl() hasn't been called yet.
			if (columnsSpinner == null) {
				return;
			}
			columnsSpinner.setValue(getIntValue(fEditPart, sfColumns));
			columnsSpinner.setEnabled(true);
			rowsSpinner.setValue(getIntValue(fEditPart, sfRows));
			rowsSpinner.setEnabled(true);
			hgapSpinner.setValue(getIntValue(fEditPart, sfHgap));
			hgapSpinner.setEnabled(true);
			vgapSpinner.setValue(getIntValue(fEditPart, sfVgap));
			vgapSpinner.setEnabled(true);
			initialized = true;
		}
	}
	
	private EStructuralFeature getSFForSpinner(Spinner s) {
		if (s == columnsSpinner)
			return sfColumns;
		else if (s == rowsSpinner)
			return sfRows;
		else if (s == hgapSpinner)
			return sfHgap;
		else if (s == vgapSpinner)
			return sfVgap;
		else 
			return null;
	}
	
	private void spinnerModified(Spinner s) {
		if (initialized)
			execute(createSpinnerCommand(fEditPart, getSFForSpinner(s), s));				
	}
	
	/*
	 * Return the commands to set the anchor value for the selected editpart
	 * The alignment value is based on the type of action.
	 */
	protected Command createSpinnerCommand(EditPart editpart, EStructuralFeature sf, Spinner spinner) {
		CommandBuilder cb = new CommandBuilder();
		EObject control = (EObject)editpart.getModel();
		if (control != null) {
			IJavaInstance gridLayout = (IJavaInstance) control.eGet(sfCompositeLayout);
			if (gridLayout != null) {
				RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(editpart), null, false);
				String init = String.valueOf(spinner.getValue());
				Object intObject = BeanUtilities.createJavaObject("int", rset, init); //$NON-NLS-1$
				componentCB.applyAttributeSetting(gridLayout, sf, intObject);
				componentCB.applyAttributeSetting(control, sfCompositeLayout, gridLayout);
				cb.append(componentCB.getCommand());
			} else {
				// this shouldn't happen
				return UnexecutableCommand.INSTANCE;
			}
			cb.append(new EnableSpinnerCommand(spinner));
			return cb.getCommand();
		} else {
			return UnexecutableCommand.INSTANCE;
		}
	}

	/*
	 * Command that is used to re-enable the spinner since we don't want the user
	 * changing the span while the span is being updated. This prevents a ConcurrentModificationException
	 * that is caused when the span is being read from the spinner side while the apply attribute setting
	 * command is being executed in a separate thread.
	 * 
	 * This command should be the last command executed after all the insets commands are complete
	 */
	protected class EnableSpinnerCommand extends AbstractCommand {
		protected Spinner spinner;
		public EnableSpinnerCommand(Spinner spinner) {
			super();
			this.spinner = spinner;
		}

		/* 
		 * Enable the spinner
		 */
		public void execute() {
			if (spinner != null)
				spinner.setEnabled(true);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.gef.commands.Command#canExecute()
		 */
		public boolean canExecute() {
			return true;
		}

	};
	
	protected int getIntValue(EditPart ep, EStructuralFeature sf) {
		IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		if (ps != null && getResourceSet(ep) != null) {
			IPropertySource gridLayout = (IPropertySource) ps.getPropertyValue(sfCompositeLayout);
			if (gridLayout != null) {
				Object intPV = gridLayout.getPropertyValue(sf);
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
		sfColumns = null;
		sfRows = null;
		sfHgap = null;
		sfVgap = null;
		initialized = false;
	}
	
	/*
	 * Return the ResourceSet for this editpart. Initialize the structural features also. 
	 */
	protected ResourceSet getResourceSet(EditPart editpart) {
		if (rset == null) {
			rset = EMFEditDomainHelper.getResourceSet(EditDomain.getEditDomain(editpart));
			sfCompositeLayout = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_LAYOUT); 
			sfColumns = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDLAYOUT_COLUMNS);
			sfRows = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDLAYOUT_ROWS);
			sfHgap = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDLAYOUT_HGAP);
			sfVgap = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDLAYOUT_VGAP);
		}
		return rset;
	}

}
