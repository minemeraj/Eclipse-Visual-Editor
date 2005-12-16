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
 *  $RCSfile: RowLayoutLayoutPage.java,v $
 *  $Revision: 1.15 $  $Date: 2005-12-16 15:56:53 $ 
 */
package org.eclipse.ve.internal.swt;

import java.util.*;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBooleanBeanProxy;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;
 

/**
 * 
 * @since 1.0.0
 */
public class RowLayoutLayoutPage extends JavaBeanCustomizeLayoutPage {
	
	EditPart fEditPart = null;
	
	Button typeHorizontalRadio;
	Button typeVerticalRadio;
	
	Button fillCheck;
	Button justifyCheck;
	Button packCheck;
	Button wrapCheck;
		
	Spinner spacingSpinner;

	Spinner heightSpinner;
	Spinner widthSpinner;
	Spinner topSpinner;
	Spinner bottomSpinner;
	Spinner leftSpinner;
	Spinner rightSpinner;
	
	ResourceSet rset;
	
	protected EReference sfCompositeLayout;
	
	EStructuralFeature sfFill, sfJustify, sfPack, sfSpacing, sfType, sfWrap,
	   sfMarginHeight, sfMarginWidth, sfMarginBottom, sfMarginLeft, sfMarginRight, sfMarginTop;
	
	boolean initialized = false;
	
	boolean allEnabled;
	
	private static final String[] orientationInitStrings = new String[] {
			"org.eclipse.swt.SWT.HORIZONTAL", //$NON-NLS-1$
			"org.eclipse.swt.SWT.VERTICAL" //$NON-NLS-1$
	};
	
	private static final int HORIZONTAL = 0, VERTICAL = 1;

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
		GridLayout grid = new GridLayout();
		grid.numColumns = 2;
		c.setLayout(grid);
		
		Group orientationGroup = new Group(c, SWT.NONE);
		orientationGroup.setText(SWTMessages.RowLayoutLayoutPage_orientationGroupTitle); 
		GridData gd = new GridData();
		gd.verticalAlignment = GridData.BEGINNING;
		gd.horizontalAlignment = GridData.FILL;
		orientationGroup.setLayoutData(gd);
		grid = new GridLayout();
		orientationGroup.setLayout(grid);
		
		typeHorizontalRadio = makeRadio(orientationGroup, SWTMessages.RowLayoutLayoutPage_horizontalLabel); 
		typeVerticalRadio = makeRadio(orientationGroup, SWTMessages.RowLayoutLayoutPage_verticalLabel); 
		
		Group spacingGroup = new Group(c, SWT.NONE);
		spacingGroup.setText(SWTMessages.RowLayoutLayoutPage_spacingGroupTitle); 
		gd = new GridData();
		gd.verticalAlignment = GridData.FILL;
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.verticalSpan = 2;
		spacingGroup.setLayoutData(gd);
		grid = new GridLayout();
		grid.numColumns = 5;
		spacingGroup.setLayout(grid);
		
		spacingSpinner = makeSpinner(spacingGroup, SWTMessages.RowLayoutLayoutPage_spacingLabel); 
		
		Label seperator = new Label(spacingGroup, SWT.SEPARATOR | SWT.VERTICAL);
		gd = new GridData();
		gd.verticalSpan = 4;
		gd.verticalAlignment = GridData.FILL;
		seperator.setLayoutData(gd);
		
		bottomSpinner = makeSpinner(spacingGroup, SWTMessages.RowLayoutLayoutPage_bottomLabel); 
		heightSpinner = makeSpinner(spacingGroup, SWTMessages.RowLayoutLayoutPage_heightLabel); 
		leftSpinner = makeSpinner(spacingGroup, SWTMessages.RowLayoutLayoutPage_leftLabel); 
		widthSpinner = makeSpinner(spacingGroup, SWTMessages.RowLayoutLayoutPage_widthLabel); 
		rightSpinner = makeSpinner(spacingGroup, SWTMessages.RowLayoutLayoutPage_rightLabel); 
		
		Label blank = new Label(spacingGroup, SWT.NONE);
		gd = new GridData();
		gd.horizontalSpan = 2;
		blank.setLayoutData(gd);
		
		topSpinner = makeSpinner(spacingGroup, SWTMessages.RowLayoutLayoutPage_topLabel); 
		
		// Now set the spacing group tab order so that it goes top/down instead of left/right. Works better.
		spacingGroup.setTabList(new Control[] {spacingSpinner, heightSpinner, widthSpinner, bottomSpinner, leftSpinner, rightSpinner, topSpinner});
		
		Group configGroup = new Group(c, SWT.NONE);
		configGroup.setText(SWTMessages.RowLayoutLayoutPage_configGroupTitle); 
		gd = new GridData();
		gd.verticalAlignment = GridData.FILL;
		configGroup.setLayoutData(gd);
		grid = new GridLayout();
		grid.numColumns = 2;
		configGroup.setLayout(grid);
		
		fillCheck = makeCheck(configGroup, SWTMessages.RowLayoutLayoutPage_fillLabel); 
		justifyCheck = makeCheck(configGroup, SWTMessages.RowLayoutLayoutPage_justifyLabel); 
		packCheck = makeCheck(configGroup, SWTMessages.RowLayoutLayoutPage_packLabel); 
		wrapCheck = makeCheck(configGroup, SWTMessages.RowLayoutLayoutPage_wrapLabel); 
		
		// Now set the overall tabbing so that it goes up/down instead of left right. Works better.
		c.setTabList(new Control[]{orientationGroup, configGroup, spacingGroup});
		
		if (fEditPart != null) {
			initialized = false;
			initializeValues();
		}
		
		return c;
	}
	
	protected SelectionListener radioSelection = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			radioModified((Button)e.widget);
		}
	};
	
	private Button makeRadio(Composite parent, String label) {
		Button b = new Button(parent, SWT.RADIO);
		b.setText(label);
		b.addSelectionListener(radioSelection);
		
		return b;
	}
	
	protected ModifyListener spinnerModify = new ModifyListener() {
		public void modifyText(ModifyEvent event) {
			spinnerModified((Spinner)event.widget);
		}
	};
	
	private Spinner makeSpinner(Composite parent, String labelText) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(labelText);
		
		Spinner spin = new Spinner(parent, SWT.BORDER);
		spin.setSelection(0);
		spin.setMaximum(9999);
		spin.addModifyListener(spinnerModify);
		
		return spin;
	}
	
	protected SelectionListener checkSelection = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			checkModified((Button)e.widget);
		}	
	};
	
	private Button makeCheck(Composite parent, String label) {
		Button b = new Button(parent, SWT.CHECK);
		b.setText(label);
		b.addSelectionListener(checkSelection);
		
		return b;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.CustomizeLayoutPage#handleSelectionChanged(org.eclipse.jface.viewers.ISelection)
	 */
	protected boolean handleSelectionChanged(ISelection oldSelection) {
		ISelection newSelection = getSelection();
		if (newSelection != null && newSelection instanceof IStructuredSelection && !((IStructuredSelection) newSelection).isEmpty()) {
			List editparts = ((IStructuredSelection) newSelection).toList();
			EditPart firstParent;
			allEnabled = true;
			
			// Check to see if this is a single selected container
			if (editparts.size() == 1 && editparts.get(0) instanceof EditPart) {
				firstParent = (EditPart)editparts.get(0);
				// check to see if this is a container with a GridLayout
				if (isValidTarget(firstParent)) {
					fEditPart = firstParent;
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
		if (af != null && af.testAttribute(target, LAYOUT_FILTER_KEY, RowLayoutEditPolicy.LAYOUT_ID)) { //$NON-NLS-1$ //$NON-NLS-2$
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
	
	protected void refresh() {
		if (allEnabled)
			initializeValues();
	}
	
	private void initializeValues() {
		if (fEditPart == null) return;
		initialized = false;
		getResourceSet(fEditPart);
		// break out early if getControl() hasn't been called yet.
		if (typeHorizontalRadio == null) {
			return;
		}
		
		int orientationValue = getIntValue(fEditPart, sfType);
		if (orientationValue == SWT.HORIZONTAL){
			typeHorizontalRadio.setSelection(true);
			typeVerticalRadio.setSelection(false);
		}
		else if (orientationValue == SWT.VERTICAL){
			typeVerticalRadio.setSelection(true);
			typeHorizontalRadio.setSelection(false);
		}
		
		spacingSpinner.setSelection(getIntValue(fEditPart, sfSpacing));
		spacingSpinner.setEnabled(true);
		heightSpinner.setSelection(getIntValue(fEditPart, sfMarginHeight));
		heightSpinner.setEnabled(true);
		widthSpinner.setSelection(getIntValue(fEditPart, sfMarginWidth));
		widthSpinner.setEnabled(true);
		topSpinner.setSelection(getIntValue(fEditPart, sfMarginTop));
		topSpinner.setEnabled(true);
		bottomSpinner.setSelection(getIntValue(fEditPart, sfMarginBottom));
		bottomSpinner.setEnabled(true);
		leftSpinner.setSelection(getIntValue(fEditPart, sfMarginLeft));
		leftSpinner.setEnabled(true);
		rightSpinner.setSelection(getIntValue(fEditPart, sfMarginRight));
		rightSpinner.setEnabled(true);
		
		fillCheck.setSelection(getBooleanValue(fEditPart, sfFill));
		justifyCheck.setSelection(getBooleanValue(fEditPart, sfJustify));
		packCheck.setSelection(getBooleanValue(fEditPart, sfPack));
		wrapCheck.setSelection(getBooleanValue(fEditPart, sfWrap));
		
		initialized = true;
	}
	
	
	private Map sfMap = null;
	private EStructuralFeature getSFForWidget(Widget w) {
		if (sfMap == null) {
			sfMap = new HashMap();
			sfMap.put(fillCheck,sfFill);
			sfMap.put(justifyCheck,sfJustify);
			sfMap.put(packCheck,sfPack);
			sfMap.put(wrapCheck,sfWrap);
			sfMap.put(spacingSpinner,sfSpacing);
			sfMap.put(heightSpinner,sfMarginHeight);
			sfMap.put(widthSpinner,sfMarginWidth);
			sfMap.put(topSpinner,sfMarginTop);
			sfMap.put(bottomSpinner,sfMarginBottom);
			sfMap.put(leftSpinner,sfMarginLeft);
			sfMap.put(rightSpinner,sfMarginRight);
		}
		return (EStructuralFeature)sfMap.get(w);
	}
	
	private void radioModified(Button b) {
		if (initialized)
			execute(createOrientationCommand(fEditPart, (b == typeHorizontalRadio) ? HORIZONTAL : VERTICAL ));
	}
	
	private void checkModified(Button b) {
		if (initialized)
			execute(createBooleanCommand(fEditPart, getSFForWidget(b), b.getSelection()));
	}
	
	private void spinnerModified(Spinner s) {
		if (initialized)
			execute(createSpinnerCommand(fEditPart, getSFForWidget(s), s));				
	}
	
	protected Command createOrientationCommand(EditPart editpart, int orientation) {
		CommandBuilder cb = new CommandBuilder();
		EObject control = (EObject)editpart.getModel();
		if (control != null) {
			IJavaInstance layout = (IJavaInstance) control.eGet(sfCompositeLayout);
			if (layout != null) {
				RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(editpart), null, false);
				String init = orientationInitStrings[orientation];
				Object intObject = BeanUtilities.createJavaObject("int", rset, init); //$NON-NLS-1$
				componentCB.applyAttributeSetting(layout, sfType, intObject);
				componentCB.applyAttributeSetting(control, sfCompositeLayout, layout);
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
	
	/*
	 * Return the commands to set the anchor value for the selected editpart
	 * The alignment value is based on the type of action.
	 */
	protected Command createSpinnerCommand(EditPart editpart, EStructuralFeature sf, Spinner spinner) {
		CommandBuilder cb = new CommandBuilder();
		EObject control = (EObject)editpart.getModel();
		if (control != null) {
			IJavaInstance layout = (IJavaInstance) control.eGet(sfCompositeLayout);
			if (layout != null) {
				RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(editpart), null, false);
				String init = String.valueOf(spinner.getSelection());
				Object intObject = BeanUtilities.createJavaObject("int", rset, init); //$NON-NLS-1$
				componentCB.applyAttributeSetting(layout, sf, intObject);
				componentCB.applyAttributeSetting(control, sfCompositeLayout, layout);
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
	
	protected Command createBooleanCommand(EditPart editpart, EStructuralFeature sf, boolean value) {
		CommandBuilder cb = new CommandBuilder();
		EObject control = (EObject)editpart.getModel();
		if (control != null) {
			IJavaInstance layout = (IJavaInstance) control.eGet(sfCompositeLayout);
			if (layout != null) {
				RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(editpart), null, false);
				String init = String.valueOf(value);
				Object booleanObject = BeanUtilities.createJavaObject("boolean", rset, init); //$NON-NLS-1$
				componentCB.applyAttributeSetting(layout, sf, booleanObject);
				componentCB.applyAttributeSetting(control, sfCompositeLayout, layout);
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
		IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		if (ps != null && getResourceSet(ep) != null) {
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
	 * reset the resource set and structural features
	 */
	private void resetVariables() {
		rset = null;
		sfCompositeLayout = null;
		sfFill = null;
		sfJustify = null;
		sfPack = null;
		sfSpacing = null;
		sfType = null;
		sfWrap = null;
		sfMarginHeight = null;
		sfMarginWidth = null;
		sfMarginBottom = null;
		sfMarginLeft = null;
		sfMarginRight = null;
		sfMarginTop = null;
		initialized = false;
		allEnabled = false;
	}
	
	/*
	 * Return the ResourceSet for this editpart. Initialize the structural features also. 
	 */
	protected ResourceSet getResourceSet(EditPart editpart) {
		if (rset == null) {
			rset = EMFEditDomainHelper.getResourceSet(EditDomain.getEditDomain(editpart));
			sfCompositeLayout = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_LAYOUT);
			sfFill = JavaInstantiation.getSFeature(rset, SWTConstants.SF_ROW_LAYOUT_FILL);
			sfJustify = JavaInstantiation.getSFeature(rset, SWTConstants.SF_ROW_LAYOUT_JUSTIFY);
			sfPack = JavaInstantiation.getSFeature(rset, SWTConstants.SF_ROW_LAYOUT_PACK);
			sfSpacing = JavaInstantiation.getSFeature(rset, SWTConstants.SF_ROW_LAYOUT_SPACING);
			sfType = JavaInstantiation.getSFeature(rset, SWTConstants.SF_ROW_LAYOUT_TYPE);
			sfWrap = JavaInstantiation.getSFeature(rset, SWTConstants.SF_ROW_LAYOUT_WRAP);
			sfMarginHeight = JavaInstantiation.getSFeature(rset, SWTConstants.SF_ROW_LAYOUT_MARGIN_HEIGHT);
			sfMarginWidth = JavaInstantiation.getSFeature(rset, SWTConstants.SF_ROW_LAYOUT_MARGIN_WIDTH);
			sfMarginBottom = JavaInstantiation.getSFeature(rset, SWTConstants.SF_ROW_LAYOUT_MARGIN_BOTTOM);
			sfMarginLeft = JavaInstantiation.getSFeature(rset, SWTConstants.SF_ROW_LAYOUT_MARGIN_LEFT);
			sfMarginRight = JavaInstantiation.getSFeature(rset, SWTConstants.SF_ROW_LAYOUT_MARGIN_RIGHT);
			sfMarginTop = JavaInstantiation.getSFeature(rset, SWTConstants.SF_ROW_LAYOUT_MARGIN_TOP);
		}
		return rset;
	}

}
