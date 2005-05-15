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
 *  $Revision: 1.9 $  $Date: 2005-05-15 00:18:35 $ 
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
import org.eclipse.jem.internal.proxy.core.IBooleanBeanProxy;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
 

/**
 * 
 * @since 1.0.0
 */
public class GridLayoutLayoutPage extends JavaBeanCustomizeLayoutPage {
	
	public final static int SHOW_GRID_CHANGED = 1;
	public final static int NUM_COLUMNS_CHANGED = 2;
	public final static int HORIZONTAL_SPACING_CHANGED = 3;
	public final static int VERTICAL_SPACING_CHANGED = 4;
	public final static int MARGIN_HEIGHT_CHANGED = 5;
	public final static int MARGIN_WIDTH_CHANGED = 6;
	public final static int MAKE_COLS_EQUAL_WIDTH_CHANGED = 7;

	EditPart fEditPart = null;
	protected GridController gridController;
	
	ResourceSet rset;
	
	protected EReference sfCompositeLayout;
	
	EStructuralFeature sfNumColumns, sfMakeColumnsEqualWidth, 
		sfHorizontalSpacing, sfVerticalSpacing, sfMarginHeight, sfMarginWidth;
	
	boolean initialized = false;
	
	private GridLayoutLayoutComposite gridComposite = null;
//	private IGridListener gridListener = new IGridListener() {
//		public void gridHeightChanged(int gridHeight,int oldGridHeight) {};
//		public void gridVisibilityChanged(boolean isShowing) {
//			if (gridComposite != null)
//				gridComposite.setShowGrid(isShowing);
//		};
//		public void gridMarginChanged(int gridMargin,int oldGridMargin) {};
//		public void gridWidthChanged(int gridWidth,int oldGridWidth) {};
//	};

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
		gridComposite = new GridLayoutLayoutComposite(this, parent, SWT.NONE);
		initializeValues();
//		gridComposite.addDisposeListener(new DisposeListener() {
//			public void widgetDisposed(org.eclipse.swt.events.DisposeEvent e) {
//				if (gridController != null)
//					gridController.removeGridListener(gridListener);
//			};
//		});
		return gridComposite;
	}

	/*
	 * Handle property changes from the GridLayoutLayoutComposite
	 */
	protected void propertyChanged (int type, Object value) {
		switch (type) {
			case SHOW_GRID_CHANGED:
				boolean isShowing = ((Boolean)value).booleanValue();
				gridController = GridController.getGridController(fEditPart);
				if (gridController != null && isShowing != gridController.isGridShowing())
					gridController.setGridShowing(isShowing);
				break;
			case NUM_COLUMNS_CHANGED:
			case HORIZONTAL_SPACING_CHANGED:
			case VERTICAL_SPACING_CHANGED:
			case MARGIN_HEIGHT_CHANGED:
			case MARGIN_WIDTH_CHANGED:
				execute(createSpinnerCommand(fEditPart, getSFForType(type), (String)value));
				break;
			case MAKE_COLS_EQUAL_WIDTH_CHANGED:
				execute(createBooleanCommand(fEditPart, sfMakeColumnsEqualWidth, ((Boolean)value).booleanValue()));
				break;

			default:
				break;
		}
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
					gridController = GridController.getGridController(fEditPart);
					if (gridController != null) {
						initializeValues();
//						gridController.addGridListener(gridListener);
						return true;
					}
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
						gridController = GridController.getGridController(fEditPart);
						if (gridController != null) {
							initializeValues();
							return true;
						}
					}
				}
			}
		}
		fEditPart = null;
		gridController = null;
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
		getResourceSet(fEditPart);
		// break out early if getControl() hasn't been called yet.
		gridController = GridController.getGridController(fEditPart);
		if (gridComposite == null || gridController == null) { return; }
		Object[] values = new Object[7];
		values[0] = new Boolean(gridController.isGridShowing());
		values[1] = new Integer(getIntValue(fEditPart, sfNumColumns));
		values[2] = new Integer(getIntValue(fEditPart, sfHorizontalSpacing));
		values[3] = new Integer(getIntValue(fEditPart, sfVerticalSpacing));
		values[4] = new Integer(getIntValue(fEditPart, sfMarginHeight));
		values[5] = new Integer(getIntValue(fEditPart, sfMarginWidth));
		values[6] = new Boolean(getBooleanValue(fEditPart, sfMakeColumnsEqualWidth));
		gridComposite.setInitialValues(values);
	}
	
	private EStructuralFeature getSFForType(int type) {
		switch (type) {
			case NUM_COLUMNS_CHANGED:
				return sfNumColumns;
			case HORIZONTAL_SPACING_CHANGED:
				return sfHorizontalSpacing;
			case VERTICAL_SPACING_CHANGED:
				return sfVerticalSpacing;
			case MARGIN_HEIGHT_CHANGED:
				return sfMarginHeight;
			case MARGIN_WIDTH_CHANGED:
				return sfMarginWidth;
			default:
				return null;
		}
	}
	
	/*
	 * Return the commands to set the GridLayout value of a Spinner for the selected editpart
	 */
	protected Command createSpinnerCommand(EditPart editpart, EStructuralFeature sf, String spinnerValue) {
		CommandBuilder cb = new CommandBuilder();
		EObject control = (EObject)editpart.getModel();
		if (control != null) {
			IJavaInstance gridLayout = (IJavaInstance) control.eGet(sfCompositeLayout);
			if (gridLayout != null) {
				RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(editpart), null, false);
				Object intObject = BeanUtilities.createJavaObject("int", rset, spinnerValue); //$NON-NLS-1$
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
		sfNumColumns = null;
		sfMakeColumnsEqualWidth = null;
		sfHorizontalSpacing = null;
		sfVerticalSpacing = null;
		sfMarginHeight = null;
		sfMarginWidth = null;
		initialized = false;
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

}
