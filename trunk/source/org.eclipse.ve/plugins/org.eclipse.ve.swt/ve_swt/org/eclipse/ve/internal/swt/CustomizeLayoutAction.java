/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CustomizeLayoutAction.java,v $
 *  $Revision: 1.1 $  $Date: 2004-05-07 12:46:42 $ 
 */
package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.*;
import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBooleanBeanProxy;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.BeanProxyUtilities;
import org.eclipse.ve.internal.java.core.BeanUtilities;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
 

/**
 * 
 * @since 1.0.0
 */
public class CustomizeLayoutAction extends EditorPartAction {
	
	EditPart fEditPart = null;
	
	Spinner numColumnsSpinner;
	Button equalWidthCheck;
	Spinner horizontalSpinner;
	Spinner verticalSpinner;
	Spinner heightSpinner;
	Spinner widthSpinner;
	
	ResourceSet rset;
	
	protected EReference sfCompositeLayout;
	
	EStructuralFeature sfNumColumns, sfMakeColumnsEqualWidth, 
		sfHorizontalSpacing, sfVerticalSpacing, sfMarginHeight, sfMarginWidth;
	
	boolean initialized = false;

	/**
	 * @param editor
	 * 
	 * @since 1.0.0
	 */
	public CustomizeLayoutAction(IEditorPart editor) {
		super(editor);
		// TODO Auto-generated constructor stub
	}
	

	/* (non-Javadoc)
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void run() {
		fEditPart = (EditPart) ((IStructuredSelection)getEditorPart().getSite().getSelectionProvider().getSelection()).getFirstElement();
		ModifyWindow window = new ModifyWindow(getEditorPart().getSite().getWorkbenchWindow().getShell());
		window.open();
		getResourceSet(fEditPart);
		initializeValues();
		
	}
	
	private void initializeValues() {
		if (!initialized) {
			numColumnsSpinner.setValue(getIntValue(fEditPart, sfNumColumns));
			horizontalSpinner.setValue(getIntValue(fEditPart, sfHorizontalSpacing));
			verticalSpinner.setValue(getIntValue(fEditPart, sfVerticalSpacing));
			heightSpinner.setValue(getIntValue(fEditPart, sfMarginHeight));
			widthSpinner.setValue(getIntValue(fEditPart, sfMarginWidth));
			equalWidthCheck.setSelection(getBooleanValue(fEditPart, sfMakeColumnsEqualWidth));
		}
		initialized = true;
	}
	
	private EStructuralFeature getSFForSpinner(Spinner s) {
		if (s == numColumnsSpinner)
			return sfNumColumns;
		else if (s == horizontalSpinner)
			return sfHorizontalSpacing;
		else if (s == verticalSpinner)
			return sfVerticalSpacing;
		else if (s == heightSpinner)
			return sfMarginHeight;
		else if (s == widthSpinner)
			return sfMarginWidth;
		else 
			return null;
	}
	
	private void checkModified() {
		if (initialized)
			execute(createBooleanCommand(fEditPart, sfMakeColumnsEqualWidth, equalWidthCheck.getSelection()));
	}
	
	private void spinnerModified(Spinner s) {
		if (initialized)
			execute(createIntCommand(fEditPart, getSFForSpinner(s), s.getValue()));				
	}
	
	/*
	 * Return the commands to set the anchor value for the selected editpart
	 * The alignment value is based on the type of action.
	 */
	protected Command createIntCommand(EditPart editpart, EStructuralFeature sf, int value) {
		CommandBuilder cb = new CommandBuilder();
		EObject control = (EObject)editpart.getModel();
		if (control != null) {
			IJavaInstance gridLayout = (IJavaInstance) control.eGet(sfCompositeLayout);
			if (gridLayout != null) {
				RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(editpart), null, false);
				String init = String.valueOf(value);
				Object intObject = BeanUtilities.createJavaObject("int", rset, init); //$NON-NLS-1$
				componentCB.applyAttributeSetting(gridLayout, sf, intObject);
				componentCB.applyAttributeSetting(control, sfCompositeLayout, gridLayout);
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
		if (getResourceSet(ep) != null && (IPropertySource) ep.getAdapter(IPropertySource.class) instanceof IPropertySource) {
			IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
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
	
	protected Command createBooleanCommand(EditPart editpart, EStructuralFeature sf, boolean value) {
		CommandBuilder cb = new CommandBuilder();
		EObject control = (EObject)editpart.getModel();
		if (control != null) {
			IJavaInstance gridLayout = (IJavaInstance) control.eGet(sfCompositeLayout);
			if (gridLayout != null) {
				RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(editpart), null, false);
				String init = String.valueOf(value);
				Object booleanObject = BeanUtilities.createJavaObject("boolean", rset, init); //$NON-NLS-1$
				componentCB.applyAttributeSetting(gridLayout, sf, booleanObject);
				componentCB.applyAttributeSetting(control, sfCompositeLayout, gridLayout);
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
	
	protected boolean getBooleanValue(EditPart ep, EStructuralFeature sf) {
		if (getResourceSet(ep) != null && (IPropertySource) ep.getAdapter(IPropertySource.class) instanceof IPropertySource) {
			IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
			IPropertySource gridLayout = (IPropertySource) ps.getPropertyValue(sfCompositeLayout);
			if (gridLayout != null) {
				Object booleanPV = gridLayout.getPropertyValue(sf);
				if (booleanPV != null && booleanPV instanceof IJavaDataTypeInstance) {
					IBooleanBeanProxy booleanProxy = (IBooleanBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaDataTypeInstance) booleanPV, rset);
					return booleanProxy.booleanValue();
				}
			}
		}
		return false;
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
	 * Return the ResourceSet for this editpart. Initialize the structural features also. 
	 */
	protected ResourceSet getResourceSet(EditPart editpart) {
		if (rset == null) {
			rset = EMFEditDomainHelper.getResourceSet(EditDomain.getEditDomain(editpart));
			sfCompositeLayout = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_LAYOUT); 
			sfNumColumns = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_LAYOUT_NUM_COLUMNS);
			sfMakeColumnsEqualWidth = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_LAYOUT_MAKE_COLUMNS_EQUAL_WIDTH);
			sfHorizontalSpacing = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_LAYOUT_HORIZONTAL_SPACING);
			sfVerticalSpacing = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_LAYOUT_VERTICAL_SPACING);
			sfMarginHeight = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_LAYOUT_MARGIN_HEIGHT);
			sfMarginWidth = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_LAYOUT_MARGIN_WIDTH);
		}
		return rset;
	}
	
	
	private class ModifyWindow extends Window {
		/**
		 * @param parentShell
		 * 
		 * @since 1.0.0
		 */
		protected ModifyWindow(Shell parentShell) {
			super(parentShell);
			setShellStyle(SWT.CLOSE | SWT.MODELESS | SWT.BORDER | SWT.TITLE | SWT.RESIZE);
		}
		
		protected void configureShell(Shell newShell) {
			super.configureShell(newShell);
			newShell.setText("Customize Layout"); //$NON-NLS-1$
		}
		
		protected Control createContents(Composite parent) {
			Composite c = new Composite(parent, SWT.NONE);
			GridLayout g1 = new GridLayout();
			c.setLayout(g1);
			
			Label l = new Label(c, SWT.NONE);
			l.setText(SWTMessages.getString("CustomizeLayoutAction.gridLayoutTitle")); //$NON-NLS-1$

			Listener spinnerModify = new Listener() {
				public void handleEvent(Event event) {
					spinnerModified((Spinner)event.widget);
				}
			};
			
			Group colGroup = new Group(c, SWT.NONE);
			GridLayout g2 = new GridLayout();
			g2.numColumns = 2;
			colGroup.setLayout(g2);
			colGroup.setText(SWTMessages.getString("CustomizeLayoutAction.columnsTitle")); //$NON-NLS-1$
			Label l1 = new Label(colGroup, SWT.NONE);
			l1.setText(SWTMessages.getString("CustomizeLayoutAction.numColumns")); //$NON-NLS-1$
			GridData gd4 = new GridData();
			gd4.grabExcessHorizontalSpace = true;
			l1.setLayoutData(gd4);
			numColumnsSpinner = new Spinner(colGroup, SWT.NONE, 1);
			numColumnsSpinner.setMinimum(1);
			numColumnsSpinner.addModifyListener(spinnerModify);
			
			equalWidthCheck = new Button(colGroup, SWT.CHECK);
			equalWidthCheck.setText(SWTMessages.getString("CustomizeLayoutAction.columnsEqualWidth")); //$NON-NLS-1$
			GridData gd1 = new GridData();
			gd1.horizontalSpan = 2;
			equalWidthCheck.setLayoutData(gd1);
			equalWidthCheck.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					checkModified();
				}
			});
			
			Group spaceGroup = new Group(c, SWT.NONE);
			GridData gd2 = new GridData();
			gd2.horizontalAlignment = GridData.FILL;
			spaceGroup.setLayoutData(gd2);
			
			GridLayout g3 = new GridLayout();
			g3.numColumns = 2;
			spaceGroup.setLayout(g3);
			spaceGroup.setText(SWTMessages.getString("CustomizeLayoutAction.spacingTitle")); //$NON-NLS-1$
			
			Label l2 = new Label(spaceGroup, SWT.NONE);
			l2.setText(SWTMessages.getString("CustomizeLayoutAction.horizontalSpacing")); //$NON-NLS-1$
			horizontalSpinner = new Spinner(spaceGroup, SWT.NONE, 5);
			horizontalSpinner.addModifyListener(spinnerModify);
			GridData gd3 = new GridData();
			gd3.grabExcessHorizontalSpace = true;
			l2.setLayoutData(gd3);
			
			Label l3 = new Label(spaceGroup, SWT.NONE);
			l3.setText(SWTMessages.getString("CustomizeLayoutAction.verticalSpacing")); //$NON-NLS-1$
			verticalSpinner = new Spinner(spaceGroup, SWT.NONE, 5);
			verticalSpinner.addModifyListener(spinnerModify);
			
			Label l4 = new Label(spaceGroup, SWT.NONE);
			l4.setText(SWTMessages.getString("CustomizeLayoutAction.marginHeight")); //$NON-NLS-1$
			heightSpinner = new Spinner(spaceGroup, SWT.NONE, 5);
			heightSpinner.addModifyListener(spinnerModify);
			
			Label l5 = new Label(spaceGroup, SWT.NONE);
			l5.setText(SWTMessages.getString("CustomizeLayoutAction.marginWidth")); //$NON-NLS-1$
			widthSpinner = new Spinner(spaceGroup, SWT.NONE, 5);
			widthSpinner.addModifyListener(spinnerModify);
			
			return c;
		}
		
	}

}
