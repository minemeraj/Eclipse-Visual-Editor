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
/*
 *  $RCSfile: GridLayoutComponentPage.java,v $
 *  $Revision: 1.21 $  $Date: 2005-12-01 20:19:43 $ 
 */

package org.eclipse.ve.internal.swt;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.*;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBooleanBeanProxy;
import org.eclipse.jem.internal.proxy.core.IIntegerBeanProxy;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

/**
 * This layout page resides on the Customize Layout window's Components tab
 * It shows and allows selection of the "alignment", "span" and "grab" properties of an SWT GridData object which is
 * the constraint on a component that is a child of a container that uses a GridLayout as it's layout manager. 
 */
public class GridLayoutComponentPage extends JavaBeanCustomizeLayoutPage {
	protected IEditorPart fEditorPart;
	
	private final static String[] resAlignmentValueLabels = {
		SWTMessages.AlignmentAction_beginning_beginning_label,
		SWTMessages.AlignmentAction_beginning_center_label,
		SWTMessages.AlignmentAction_beginning_end_label,
		SWTMessages.AlignmentAction_center_beginning_label,
		SWTMessages.AlignmentAction_center_center_label,
		SWTMessages.AlignmentAction_center_end_label,
		SWTMessages.AlignmentAction_end_beginning_label,
		SWTMessages.AlignmentAction_end_center_label,
		SWTMessages.AlignmentAction_end_end_label
	};
	private final static String[] resAlignmentValueTooltips = {
		SWTMessages.AlignmentAction_beginning_beginning_tooltip,
		SWTMessages.AlignmentAction_beginning_center_tooltip,
		SWTMessages.AlignmentAction_beginning_end_tooltip,
		SWTMessages.AlignmentAction_center_beginning_tooltip,
		SWTMessages.AlignmentAction_center_center_tooltip,
		SWTMessages.AlignmentAction_center_end_tooltip,
		SWTMessages.AlignmentAction_end_beginning_tooltip,
		SWTMessages.AlignmentAction_end_center_tooltip,
		SWTMessages.AlignmentAction_end_end_tooltip
	};
	private final static String[] resAlignmentValueImages = {
		SWTMessages.AlignmentAction_beginning_beginning_image,
		SWTMessages.AlignmentAction_beginning_center_image,
		SWTMessages.AlignmentAction_beginning_end_image,
		SWTMessages.AlignmentAction_center_beginning_image,
		SWTMessages.AlignmentAction_center_center_image,
		SWTMessages.AlignmentAction_center_end_image,
		SWTMessages.AlignmentAction_end_beginning_image,
		SWTMessages.AlignmentAction_end_center_image,
		SWTMessages.AlignmentAction_end_end_image
	};
	private final static String[] resAlignmentValueIDs = {
		"AlignmentAction.beginning.beginning", //$NON-NLS-1$
		"AlignmentAction.beginning.center", //$NON-NLS-1$
		"AlignmentAction.beginning.end", //$NON-NLS-1$
		"AlignmentAction.center.beginning", //$NON-NLS-1$
		"AlignmentAction.center.center", //$NON-NLS-1$
		"AlignmentAction.center.end", //$NON-NLS-1$
		"AlignmentAction.end.beginning", //$NON-NLS-1$
		"AlignmentAction.end.center", //$NON-NLS-1$
		"AlignmentAction.end.end", //$NON-NLS-1$
	};
	
	public final static int BEGINNING = 0, CENTER = 1, END = 2, FILL = 3;
	protected final static ParseTreeAllocation[] alignmentAllocations;
	static {
		alignmentAllocations = new ParseTreeAllocation[4];
		alignmentAllocations[BEGINNING] = createAlignmentAllocation("org.eclipse.swt.layout.GridData", "BEGINNING"); //$NON-NLS-1$ //$NON-NLS-2$
		alignmentAllocations[CENTER] = createAlignmentAllocation("org.eclipse.swt.layout.GridData", "CENTER"); //$NON-NLS-1$ //$NON-NLS-2$
		alignmentAllocations[END] = createAlignmentAllocation("org.eclipse.swt.layout.GridData", "END"); //$NON-NLS-1$ //$NON-NLS-2$
		alignmentAllocations[FILL] = createAlignmentAllocation("org.eclipse.swt.layout.GridData", "FILL"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	private static ParseTreeAllocation createAlignmentAllocation(String classname, String fieldName) {
		PTExpression fieldAccess = InstantiationFactory.eINSTANCE.createPTFieldAccess(InstantiationFactory.eINSTANCE.createPTName(classname), fieldName);
		return InstantiationFactory.eINSTANCE.createParseTreeAllocation(fieldAccess);
	}
	
	protected static int[] alignmentSWTValues = new int[] {
			GridData.BEGINNING,
			GridData.CENTER,
			GridData.END,
			GridData.FILL,
	};
	
	protected static int getOffetFromConstant(int constant) {
		for ( int i = 0; i < alignmentSWTValues.length; i++) {
			if (alignmentSWTValues[i] == constant) {
				return i;
			}
		}
		return 0;
	}


	public final static int HORIZONTAL = 0, VERTICAL = 1;
	
	protected AlignmentAction[] alignmentActions = {
			new AlignmentAction(BEGINNING, BEGINNING),
			new AlignmentAction(CENTER, BEGINNING),
			new AlignmentAction(END, BEGINNING),
			new AlignmentAction(BEGINNING, CENTER),
			new AlignmentAction(CENTER, CENTER),
			new AlignmentAction(END, CENTER),
			new AlignmentAction(BEGINNING, END),
			new AlignmentAction(CENTER, END),
			new AlignmentAction(END, END)
	};
	
	private final static String[] resFillIDs = {
			"FillAction.horizontal", //$NON-NLS-1$
			"FillAction.vertical" //$NON-NLS-1$
	};
	private final static String[] resFillLabels = {
		SWTMessages.FillAction_horizontal_label,
		SWTMessages.FillAction_vertical_label
	};
	private final static String[] resFillImages = {
		SWTMessages.FillAction_horizontal_image,
		SWTMessages.FillAction_vertical_image
	};
	private final static String[] resFillTooltips = {
		SWTMessages.FillAction_horizontal_tooltip,
		SWTMessages.FillAction_vertical_tooltip
	};
	
	private FillAction[] fillActions = {
			new FillAction(HORIZONTAL),
			new FillAction(VERTICAL)
	};
	
	private final static String[] resGrabIDs = { 
		"GrabAction.horizontal", //$NON-NLS-1$
		"GrabAction.vertical" //$NON-NLS-1$
	};
	private final static String[] resGrabLabels = { 
		SWTMessages.GrabAction_horizontal_label,
		SWTMessages.GrabAction_vertical_label
	};
	private final static String[] resGrabTooltips = { 
		SWTMessages.GrabAction_horizontal_tooltip,
		SWTMessages.GrabAction_vertical_tooltip
	};
	private final static String[] resGrabImages = { 
		SWTMessages.GrabAction_horizontal_image,
		SWTMessages.GrabAction_vertical_image
	};
	
	private GrabAction[] grabActions = {
		new GrabAction(HORIZONTAL),
		new GrabAction(VERTICAL)
	};

	protected EReference sfControlLayoutData;
	protected EStructuralFeature sfHorizontalAlignment, sfVerticalAlignment, sfHorizontalGrab, sfVerticalGrab, sfHorizontalSpan, sfVerticalSpan,
			sfHorizontalIndent, sfHeightHint, sfWidthHint;
	protected ResourceSet rset;
	protected AlignmentAction selectedAlignmentAction;
	protected boolean fillVertical = false, fillHorizontal = false;
	
	protected Spinner horizontalSpanSpinner, verticalSpanSpinner, horizontalIndentSpinner;
	protected org.eclipse.ve.internal.java.core.Spinner heightHintSpinner, widthHintSpinner;
	protected int horizontalSpanValue = 1, verticalSpanValue = 1, horizontalIndentValue = 0, heightHintValue = -1, widthHintValue = -1;

	private Button restoreAllButton;

	private boolean hasGridDataValue = false;

	/*
	 * 
	 * Inner class used for the Alignment Actions
	 */
	public class AlignmentAction extends Action {

		protected int fHorizontalAlign;
		protected int fVerticalAlign;

		public AlignmentAction(int horizontalAlign, int verticalAlign) {
			super(null, Action.AS_CHECK_BOX);
			
			
			// Default to center anchor if the anchor type is incorrect
			if (!(horizontalAlign >= 0 && horizontalAlign < 3))
				fHorizontalAlign = CENTER;
			else
				fHorizontalAlign = horizontalAlign;
			if (!(verticalAlign >= 0 && verticalAlign < 3))
				fVerticalAlign = CENTER;
			else
				fVerticalAlign = verticalAlign;

			
			setText(getAlignmentValue(resAlignmentValueLabels, fHorizontalAlign, fVerticalAlign)); 
			setToolTipText(getAlignmentValue(resAlignmentValueTooltips, fHorizontalAlign, fVerticalAlign)); 
			// There are three images, one for full color ( that is the hover one )
			// one for disabled and one for enabled
			String graphicName = getAlignmentValue(resAlignmentValueImages, fHorizontalAlign, fVerticalAlign);
			setImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/elcl16/" + graphicName)); //$NON-NLS-1$
			setHoverImageDescriptor(getImageDescriptor());
 			setDisabledImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/dlcl16/" + graphicName)); //$NON-NLS-1$
			
			setId(getAlignmentValue(resAlignmentValueIDs, fHorizontalAlign, fVerticalAlign));
			setEnabled(true);
		}
		
		public int getHorizontalAlignment() {
			return fHorizontalAlign;
		}
		
		public int getVerticalAlignment() {
			return fVerticalAlign;
		}

		/**
		 * Static method that returns the action id based on the alignment type.
		 */
		public String getAlignmentValue(String[] array, int horizontalAlign, int verticalAlign) {
			if (horizontalAlign < 0 || horizontalAlign > END) horizontalAlign = CENTER;
			if (verticalAlign < 0 || verticalAlign > END) verticalAlign = CENTER;

			return array[(horizontalAlign*3)+verticalAlign];
		}
		
		protected boolean calculateEnabled() {
			return true;
		}
		protected void setEditorPart(IEditorPart part) {
			fEditorPart = part;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.action.IAction#run()
		 * This anchor type was selected. Deselect all the others to
		 * emulate a checkbox group so that only one checkbox button shows selected.
		 * Then execute the commands to apply the anchor type to the selected editparts.
		 */
		public void run() {
			super.run();
//			if (selectedAlignmentAction != this) {
				execute(createAlignmentCommand(getSelectedObjects(), fHorizontalAlign, fVerticalAlign));
				selectedAlignmentAction = this;
//			}
			for (int i = 0; i < alignmentActions.length; i++) {
				if (!(alignmentActions[i] == this))
					alignmentActions[i].setChecked(false);
				else if (!isChecked())
					setChecked(true);
			}
		}

	}
	
	/*
	 * 
	 * Inner class used for the Fill Actions
	 */
	public class FillAction extends Action {

		protected int fOrientation;
		protected int previousValue = CENTER;

		public FillAction(int orientation) {
			super(null, Action.AS_CHECK_BOX);

			// Default to HORIZONTAL if an invalid orientation is given.
			if (orientation < 0 || orientation >= resFillLabels.length) {
				fOrientation = HORIZONTAL;
			} else {
				fOrientation = orientation;
			}
			
			setText(resFillLabels[orientation]);
			setToolTipText(resFillTooltips[orientation]); 
			// There are three images, one for full color ( that is the hover one )
			// one for disabled and one for enabled
			String graphicName = resFillImages[orientation];
			setImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/elcl16/" + graphicName)); //$NON-NLS-1$
			setHoverImageDescriptor(getImageDescriptor()); 
			setDisabledImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/dlcl16/" + graphicName)); //$NON-NLS-1$
			
			setId(resFillIDs[orientation]);
			setEnabled(true);
		}

		/**
		 * Static method that returns the action id based on the fill type.
		 */
		public String getActionId(int orientation) {
			return (orientation >= 0 && orientation < resFillIDs.length) ? resFillIDs[orientation] : resFillIDs[HORIZONTAL];
		}
		
		protected boolean calculateEnabled() {
			return true;
		}
		protected void setEditorPart(IEditorPart part) {
			fEditorPart = part;
		}
		
		protected void updateAlignmentEnablement() {
			for (int i = 0; i < alignmentActions.length; i++) {
				AlignmentAction c = alignmentActions[i];
				c.setEnabled(! ((fillHorizontal && c.getHorizontalAlignment() != CENTER) || 
						(fillVertical && c.getVerticalAlignment() != CENTER)));
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.action.IAction#run()
		 * This fill type was selected.
		 */
		public void run() {
			super.run();
			int newValue = -1;
			
			// enable the alignment buttons cooresponding with this orientation
			if (fOrientation == HORIZONTAL) {
				fillHorizontal = this.isChecked();
			} else {
				fillVertical = this.isChecked();
			}
			updateAlignmentEnablement();
			
			if (this.isChecked()) {				
				if (selectedAlignmentAction != null) {
					AlignmentAction newSelection;
					if (fOrientation == HORIZONTAL) {
						previousValue = selectedAlignmentAction.getHorizontalAlignment();
						newValue = selectedAlignmentAction.getVerticalAlignment();
						// index into alignment actions will pull out the vertical alignemnt
						// button with the horizontal alignment centered
						newSelection = alignmentActions[3 * newValue + CENTER];
					} else {
						previousValue = selectedAlignmentAction.getVerticalAlignment();
						newValue = selectedAlignmentAction.getHorizontalAlignment();
						// index into alignment actions will pull out the horizontal alignemnt
						// button with the vertical alignment centered
						newSelection = alignmentActions[3 * CENTER + newValue];
					}
					if (previousValue != CENTER) {
						if (newSelection.isEnabled()) {
							newSelection.setChecked(true);
							newSelection.run();
						}
					} else {
						selectedAlignmentAction.run();
					}
				}
			} else {			
				// check the previously selected alignment
				if (selectedAlignmentAction != null) {
					if (previousValue != CENTER) {
						AlignmentAction newSelection;
						if (fOrientation == HORIZONTAL) {
							newValue = selectedAlignmentAction.getVerticalAlignment();
							newSelection = alignmentActions[3 * newValue + previousValue];
						} else {
							newValue = selectedAlignmentAction.getHorizontalAlignment();
							newSelection = alignmentActions[3 * previousValue + newValue];
						}
						if (newSelection.isEnabled()) {
							newSelection.setChecked(true);
							newSelection.run();
						}
					} else {
						selectedAlignmentAction.run();
					}
				}
				previousValue = CENTER;
			}
		}

	}
	
	/*
	 * Inner class used for the Grab actions
	 */
	public class GrabAction extends Action {

		protected int fGrabType;

		public GrabAction(int grabType) {
			super(null, Action.AS_CHECK_BOX);
			// Default to center anchor if the anchor type is incorrect
			if (!(grabType >= 0 && grabType < resGrabLabels.length))
				fGrabType = HORIZONTAL;
			else
				fGrabType = grabType;
			setText(resGrabLabels[fGrabType]); //$NON-NLS-1$
			setToolTipText(resGrabTooltips[fGrabType]); //$NON-NLS-1$
			// There are three images, one for full color ( that is the hover one )
			// one for disabled and one for enabled
			String graphicName = resGrabImages[fGrabType];
			// The file structure of these is that they exist in the plugin directory with three folder names, e.g.
			// /icons/full/clc16/anchorleft_obj.gif for the color one
			// and elc16 for enabled and dlc16 for disasbled
			setImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/elcl16/" + graphicName)); //$NON-NLS-1$
			setHoverImageDescriptor(getImageDescriptor()); 
			setDisabledImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/dlcl16/" + graphicName));	 //$NON-NLS-1$
			setEnabled(true);
			setId(getActionId(fGrabType));
		}

		/**
		 * Static method that returns the action id based on the alignment type.
		 */
		public String getActionId(int grabType) {
			return ((grabType >= 0 && grabType < resGrabIDs.length) ? resGrabIDs[grabType] : resGrabIDs[HORIZONTAL]);
		}
		protected boolean calculateEnabled() {
			return true;
		}
		protected void setEditorPart(IEditorPart part) {
			fEditorPart = part;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.action.IAction#run()
		 * 
		 * This grab type was selected.
		 */
		public void run() {
			super.run();			
			execute(createGrabCommand(getSelectedObjects(), fGrabType, grabActions[fGrabType].isChecked()));
		}

	}
	
	/*
	 * Returns a List containing the currently selected objects.
	 */
	protected List getSelectedObjects() {
		if (!(getSelection() instanceof IStructuredSelection))
			return Collections.EMPTY_LIST;
		return ((IStructuredSelection)getSelection()).toList();
	}
	/*
	 * Return the commands to set the anchor value for the selected editparts
	 * The alignment value is based on the type of action.
	 */
	protected Command createAlignmentCommand(List editparts, int horizontalAlign, int verticalAlign) {
		if (!editparts.isEmpty()) {
			CommandBuilder cb = new CommandBuilder();
			for (int i = 0; i < editparts.size(); i++) {
				EditPart editpart = (EditPart)editparts.get(i);
				EObject control = (EObject)editpart.getModel();
				if (control != null) {
					IJavaInstance gridData = (IJavaInstance) control.eGet(sfControlLayoutData);
					if (gridData == null) {
						// Create a new grid data if one doesn't already exist.
						gridData = BeanUtilities.createJavaObject("org.eclipse.swt.layout.GridData", rset, (String) null); //$NON-NLS-1$
					}
					if (gridData != null) {
						RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(editpart), null, false);
						
						JavaAllocation initAlloc;
						// Apply horizontal alignment
						if (fillHorizontal) {
							initAlloc = (JavaAllocation) EcoreUtil.copy(alignmentAllocations[FILL]);
						} else {
							initAlloc = (JavaAllocation) EcoreUtil.copy(alignmentAllocations[horizontalAlign]);
						}
						Object alignObject = BeanUtilities.createJavaObject("int", rset, initAlloc); //$NON-NLS-1$
						componentCB.applyAttributeSetting(gridData, sfHorizontalAlignment, alignObject);

						if (fillVertical) {
							initAlloc = (JavaAllocation) EcoreUtil.copy(alignmentAllocations[FILL]);
						} else {
							initAlloc = (JavaAllocation) EcoreUtil.copy(alignmentAllocations[verticalAlign]);
						}
						alignObject = BeanUtilities.createJavaObject("int", rset, initAlloc); //$NON-NLS-1$
						componentCB.applyAttributeSetting(gridData, sfVerticalAlignment, alignObject);

						componentCB.applyAttributeSetting(control, sfControlLayoutData, gridData);
						cb.append(componentCB.getCommand());
					}
				}
			}
			if (!restoreAllButton.getEnabled())
				restoreAllButton.setEnabled(true);
			return cb.getCommand();
		}
		return UnexecutableCommand.INSTANCE;
	}

	/*
	 * Return the commands to set the fill value for the selected editparts
	 * The fill value is based on the type of action and is retrieved from the fillAWTValue table.
	 */
	protected Command createGrabCommand(List editparts, int grabType, boolean value) {
		if (!editparts.isEmpty()) {
			CommandBuilder cb = new CommandBuilder();
			for (int i = 0; i < editparts.size(); i++) {
				EditPart editpart = (EditPart)editparts.get(i);
				EObject control = (EObject)editpart.getModel();
				if (control != null) {
					IJavaInstance gridData = (IJavaInstance) control.eGet(sfControlLayoutData);
					if (gridData == null) {
						// Create a new grid data if one doesn't already exist.
						gridData = BeanUtilities.createJavaObject("org.eclipse.swt.layout.GridData", rset, (String) null); //$NON-NLS-1$ 
					}
					if (gridData != null) {
						RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(editpart), null, false);
						String init = String.valueOf(value);
						Object alignObject = BeanUtilities.createJavaObject("boolean", rset, init); //$NON-NLS-1$
						if (grabType == HORIZONTAL) {
							componentCB.applyAttributeSetting(gridData, sfHorizontalGrab, alignObject);
						} else {
							componentCB.applyAttributeSetting(gridData, sfVerticalGrab, alignObject);
						}
						componentCB.applyAttributeSetting(control, sfControlLayoutData, gridData);
						cb.append(componentCB.getCommand());
					}
				}
			}
			if (!restoreAllButton.getEnabled())
				restoreAllButton.setEnabled(true);
			return cb.getCommand();
		}
		return UnexecutableCommand.INSTANCE;
	}
	
	protected Command createSpinnerCommand(List editparts, EStructuralFeature sf, int spinnerValue ) {
		if (!editparts.isEmpty()) {
			CommandBuilder cb = new CommandBuilder();
			for (int i = 0; i < editparts.size(); i++) {
				EditPart editpart = (EditPart)editparts.get(i);
				EObject control = (EObject)editpart.getModel();
				if (control != null) {
					IJavaInstance gridData = (IJavaInstance) control.eGet(sfControlLayoutData);
					if (gridData == null) {
						// Create a new grid data if one doesn't already exist.
						gridData = BeanUtilities.createJavaObject("org.eclipse.swt.layout.GridData", rset, (String) null); //$NON-NLS-1$
					}
					if (gridData != null) {
						try {
							RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(editpart), null, false);
							String init = String.valueOf(spinnerValue);
							Object intObject = BeanUtilities.createJavaObject("int", rset, init); //$NON-NLS-1$
							componentCB.applyAttributeSetting(gridData, sf, intObject);
							componentCB.applyAttributeSetting(control, sfControlLayoutData, gridData);
							cb.append(componentCB.getCommand());
						} catch (IllegalArgumentException e) {
							return UnexecutableCommand.INSTANCE;	// Feature not a valid feature for the griddata. (Griddata not really a griddata).
						}
					}
				}
			}
			if (!restoreAllButton.getEnabled())
				restoreAllButton.setEnabled(true);
			return cb.getCommand();
		}
		return UnexecutableCommand.INSTANCE;
	}
	
	protected Command createSpanSpinnerCommand(List editparts, EStructuralFeature sf, int spinnerValue ) {
		if (!editparts.isEmpty()) {
			EditPart parentEditPart = ((EditPart) editparts.get(0)).getParent();	// We know they all have the same parent.
			CompositeContainerPolicy cp = new CompositeContainerPolicy(EditDomain.getEditDomain(parentEditPart));
			cp.setContainer(parentEditPart.getModel());
			GridLayoutPolicyHelper helper = new GridLayoutPolicyHelper(cp);
			helper.startRequest();
			for (int i = 0; i < editparts.size(); i++) {
				EditPart editpart = (EditPart)editparts.get(i);
				
				EObject control = (EObject)editpart.getModel();
				if (control != null) {
					Point spanTo;
					boolean horizontalSpan = "horizontalSpan".equals(sf.getName());
					if (horizontalSpan)
						spanTo = new Point(spinnerValue, 0);
					else
						spanTo = new Point(0, spinnerValue);
					helper.spanChild(control, spanTo, horizontalSpan ? PositionConstants.EAST : PositionConstants.SOUTH, null);
				}
			}
			if (!restoreAllButton.getEnabled())
				restoreAllButton.setEnabled(true);
			return helper.stopRequest();
		}
		return UnexecutableCommand.INSTANCE;
	}
	

	/*
	 * Return the command to cancel the GridData settings for this control
	 */
	protected Command createRestoreDefaultsCommand(List editparts) {
		if (!editparts.isEmpty()) {
			EditPart parentEditPart = ((EditPart) editparts.get(0)).getParent();	// We know they all have the same parent.
			CompositeContainerPolicy cp = new CompositeContainerPolicy(EditDomain.getEditDomain(parentEditPart));
			cp.setContainer(parentEditPart.getModel());
			GridLayoutPolicyHelper helper = new GridLayoutPolicyHelper(cp);
			helper.startRequest();
			RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(parentEditPart), null, false);
			for (int i = 0; i < editparts.size(); i++) {
				EditPart editpart = (EditPart) editparts.get(i);
				EObject control = (EObject) editpart.getModel();
				if (control != null) {
					if (control.eIsSet(sfControlLayoutData)) {
						helper.spanChild(control, new Point(1,0), PositionConstants.EAST, null);
						helper.spanChild(control, new Point(0,1), PositionConstants.SOUTH, null);
						componentCB.cancelAttributeSetting(control, sfControlLayoutData);
					}
				}
			}
			componentCB.append(helper.stopRequest());
			return componentCB.getCommand();
		}
		return UnexecutableCommand.INSTANCE;
	}

	/**
	 * Create the contents of this tab page
	 */
	public Control getControl(Composite parent) {

		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout(3, false));
		
		Group alignmentGroup = createGroup(mainComposite, SWTMessages.GridLayoutComponentPage_Alignment, 2, 5, 0); 
		GridData gd1 = new GridData();
		gd1.verticalSpan = 2;
		gd1.verticalAlignment = GridData.FILL;
		alignmentGroup.setLayoutData(gd1);
		
		Composite alignmentGrid = new Composite(alignmentGroup, SWT.NONE);
		GridLayout grid = new GridLayout();
		grid.numColumns = 3;
		grid.horizontalSpacing = 0;
		grid.verticalSpacing = 0;
		grid.marginHeight = 0;
		alignmentGrid.setLayout(grid);
		for (int i = 0; i < alignmentActions.length; i++) {
			ActionContributionItem ac = new ActionContributionItem(alignmentActions[i]);
			ac.fill(alignmentGrid);
		}
		
		Group fillGroup = createGroup(alignmentGroup, SWTMessages.GridLayoutComponentPage_Fill, 1, 0, 0); 
		for (int i = 0; i < fillActions.length; i++) {
			ActionContributionItem ac = new ActionContributionItem(fillActions[i]);
			ac.fill(fillGroup);
		}
		Label horizontalIndentLabel = new Label(alignmentGroup, SWT.NONE);
		horizontalIndentLabel.setText(SWTMessages.GridLayoutComponentPage_HorizontalIndent); 
		
		horizontalIndentSpinner = new Spinner(alignmentGroup, SWT.BORDER);
		horizontalIndentSpinner.setSelection(horizontalIndentValue);
		horizontalIndentSpinner.setMaximum(9999);
		horizontalIndentSpinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				int value = horizontalIndentSpinner.getSelection();
				if (value != horizontalIndentValue) {
					horizontalIndentValue = value;
					execute(createSpinnerCommand(getSelectedObjects(), sfHorizontalIndent, horizontalIndentValue));
				}
			}
		});

		Group spanGroup = createGroup(mainComposite, SWTMessages.GridLayoutComponentPage_Span, 2, 4, 4); 
		createSpanControl(spanGroup);
		
		Group grabGroup = createGroup(mainComposite, SWTMessages.GridLayoutComponentPage_Grab, 2, 0, 0); 
		for (int i = 0; i < grabActions.length; i++) {
			ActionContributionItem ac = new ActionContributionItem(grabActions[i]);
			ac.fill(grabGroup);
		}
		GridData gd2 = new GridData();
		gd2.verticalSpan = 1;
		gd2.verticalAlignment = GridData.FILL;
		grabGroup.setLayoutData(gd2);
		
		Group hintsGroup = createGroup(mainComposite, SWTMessages.GridLayoutComponentPage_Hints, 2, 4, 4); 
		GridData gd3 = new GridData();
		gd3.horizontalAlignment = GridData.FILL;
		hintsGroup.setLayoutData(gd3);
		
		Label horizontalHintLabel = new Label(hintsGroup, SWT.NONE);
		horizontalHintLabel.setText(SWTMessages.GridLayoutComponentPage_HeightHint); 
		
		// NOTE: Need to use VE spinner because we need a -1 value, swt.spinner won't allow that. 
		heightHintSpinner = new org.eclipse.ve.internal.java.core.Spinner(hintsGroup, SWT.NONE, -1);
		heightHintSpinner.setMinimum(-1);
		heightHintSpinner.setValue(heightHintValue);
		heightHintSpinner.addModifyListener(new Listener() {
			public void handleEvent(Event e) {
				int value = heightHintSpinner.getValue();
				if (value != heightHintValue) {
					heightHintValue = value;
					execute(createSpinnerCommand(getSelectedObjects(), sfHeightHint, heightHintValue));
				}
			}
		});

		Label verticalHintLabel = new Label(hintsGroup, SWT.NONE);
		verticalHintLabel.setText(SWTMessages.GridLayoutComponentPage_WidthHint); 
		
		// NOTE: Need to use VE spinner because we need a -1 value, swt.spinner won't allow that.
		widthHintSpinner = new org.eclipse.ve.internal.java.core.Spinner(hintsGroup, SWT.NONE, -1);
		widthHintSpinner.setMinimum(-1);
		widthHintSpinner.setValue(widthHintValue);
		widthHintSpinner.addModifyListener(new Listener() {
			public void handleEvent(Event e) {
				int value = widthHintSpinner.getValue();
				if (value != widthHintValue) {
					widthHintValue = value;
					execute(createSpinnerCommand(getSelectedObjects(), sfWidthHint, widthHintValue));
				} 
			}
		});

		Label spacer = new Label(mainComposite, SWT.None);
		spacer.setText(""); //$NON-NLS-1$
		
		restoreAllButton = new Button(mainComposite, SWT.NONE);
		restoreAllButton.setEnabled(hasGridDataValue);
		restoreAllButton.setText(SWTMessages.GridLayoutComponentPage_Button_RestoreDefaults_Text); 
		restoreAllButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				restoreAllDefaultValues();
			};
		});
		
		return mainComposite;
	}
	
	/*
	 * Restore all the GridData default values by removing the GridData for each selected control.
	 */
	protected void restoreAllDefaultValues() {
		List editparts = getSelectedObjects();
		if (!editparts.isEmpty()) {
			Command cmd = createRestoreDefaultsCommand(editparts);
			if (cmd != UnexecutableCommand.INSTANCE) {
				execute(cmd);
				refreshAllValues(editparts);
				restoreAllButton.setEnabled(false);
			}
		}
	}
	
	protected Group createGroup(Composite aParent, String title, int numColumns, int verticalSpacing, int horizontalSpacing) {
		Group group = new Group(aParent, SWT.NONE);
		group.setText(title);
		GridLayout gridLayout = new GridLayout(numColumns, false);
		gridLayout.verticalSpacing = verticalSpacing;
		gridLayout.horizontalSpacing = horizontalSpacing;
		group.setLayout(gridLayout);
		return group;
	}
	
	protected void createSpanControl(Group spanGroup) {
		Label horizontalLabel = new Label(spanGroup, SWT.NONE);
		horizontalLabel.setText(SWTMessages.GridLayoutComponentPage_SpanHorizontal); 
		
		horizontalSpanSpinner = new Spinner(spanGroup, SWT.BORDER);
		horizontalSpanSpinner.setMinimum(1);
		horizontalSpanSpinner.setMaximum(9999);
		horizontalSpanSpinner.setSelection(horizontalSpanValue);
		
		Label verticalLabel = new Label(spanGroup, SWT.NONE);
		verticalLabel.setText(SWTMessages.GridLayoutComponentPage_SpanVertical); 
		
		verticalSpanSpinner = new Spinner(spanGroup, SWT.BORDER);
		verticalSpanSpinner.setMinimum(1);
		verticalSpanSpinner.setMaximum(9999);
		verticalSpanSpinner.setSelection(verticalSpanValue);
		
		horizontalSpanSpinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				int value = horizontalSpanSpinner.getSelection();
				if (value != horizontalSpanValue) {
					horizontalSpanValue = value;
					execute(createSpanSpinnerCommand(getSelectedObjects(), sfHorizontalSpan, value));
				}
			}
		});
		
		verticalSpanSpinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				int value = verticalSpanSpinner.getSelection();
				if (value != verticalSpanValue) {
					verticalSpanValue = value;
					execute(createSpanSpinnerCommand(getSelectedObjects(), sfVerticalSpan, value));
				}
			}
		});
	}

	protected void enableAlignmentActions(boolean enable) {
		for (int i = 0; i < alignmentActions.length; i++) {
			alignmentActions[i].setEnabled(enable);
			if (!enable) {
				alignmentActions[i].setChecked(false);
			}
		}
		for (int i = 0; i < fillActions.length; i++) {
			fillActions[i].setEnabled(enable);
			if (!enable) {
				fillActions[i].setChecked(false);
			}
		}
	}
	protected void enableGrabActions(boolean enable) {
		for (int i = 0; i < grabActions.length; i++) {
			grabActions[i].setEnabled(enable);
			if (!enable)
				grabActions[i].setChecked(false);
		}
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

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.CustomizeLayoutPage#getImage()
	 */
	public Image getImage() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.CustomizeLayoutPage#getText()
	 */
	public String getText() {
		return SWTMessages.GridLayoutComponentPage_Grid; 
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.CustomizeLayoutPage#getToolTipText()
	 */
	public String getToolTipText() {
		return SWTMessages.GridLayoutComponentPage_ToolTipText; 
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.CustomizeLayoutPage#handleEditorPartChanged(org.eclipse.ui.IEditorPart)
	 * 
	 * The editorpart changed. Pass this on to the AnchorActions and fillActions 
	 * and reset the resource set and structural features.
	 */
	protected void handleEditorPartChanged(IEditorPart oldEditorPart) {
		IEditorPart newEditorPart = getEditorPart();
		for (int i = 0; i < alignmentActions.length; i++) {
			alignmentActions[i].setEditorPart(newEditorPart);
		}
		for (int i = 0; i < grabActions.length; i++) {
			grabActions[i].setEditorPart(newEditorPart);
		}
		resetVariables();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.CustomizeLayoutPage#handleSelectionChanged(org.eclipse.jface.viewers.ISelection)
	 * 
	 * The selection list has changed, enable/disable and check/uncheck the AnchorActions based on whether the
	 * components selected have the same parent, the parent's layout is a GridBagLayout, and whether the anchor
	 * property values are equal.
	 */
	protected boolean handleSelectionChanged(ISelection oldSelection) {
		ISelection newSelection = getSelection();
		if (newSelection != null && newSelection instanceof IStructuredSelection && !((IStructuredSelection) newSelection).isEmpty()) {
			List editparts = ((IStructuredSelection) newSelection).toList();
			EditPart firstParent;
			boolean enableAll = true;
			if (editparts.get(0) instanceof EditPart && ((EditPart) editparts.get(0)).getParent() != null) {
				firstParent = ((EditPart) editparts.get(0)).getParent();
				// Check the parent to ensure its layout policy is a GridBagLayout
				if (isValidParent(firstParent)) {
					EditPart ep = (EditPart) editparts.get(0);
					/*
					 * Need to iterate through the selection list and ensure each selection is:
					 * - an EditPart
					 * - they share the same parent
					 * - it's parent has a GridBagLayout as it's layout manager
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
						enableAlignmentActions(true);
						enableGrabActions(true);
						refreshAllValues(editparts);
						if (restoreAllButton != null)
							if (hasGridData(editparts))
								restoreAllButton.setEnabled(true);
							else
								restoreAllButton.setEnabled(false);
						return true;
					}
				}
			}
		}
		// By default if the initial checks failed, disable and uncheck all the actions.
		enableAlignmentActions(false);
		enableGrabActions(false);
		return false;
	}
	private void refreshAllValues(List editparts) {
		handleSelectionChangedForAlignmentActions(editparts);
		handleSelectionChangedForGrabActions(editparts);
		handleSelectionChangedForSpinners(editparts);
		hasGridDataValue = hasGridData(editparts);
	}
	
	/*
	 * If the alignment value for each component is the same, check the appropriate action
	 * otherwise, uncheck all of them. 
	 */
	protected void handleSelectionChangedForAlignmentActions(List editparts) {
		boolean setChecked = true;
		int firstHorizontalValue = getHorizontalAlignValue((EditPart) editparts.get(0));
		int firstVerticalValue = getVerticalAlignValue((EditPart) editparts.get(0));
		for (int i = 1; i < editparts.size(); i++) {
			if (firstHorizontalValue != getHorizontalAlignValue((EditPart) editparts.get(i)) ||
					firstVerticalValue != getVerticalAlignValue((EditPart) editparts.get(i))) {
				setChecked = false;
				break;
			}
		}

		if (setChecked) {
			// calculate the fill button values
			fillHorizontal = firstHorizontalValue == alignmentSWTValues[FILL];
			fillVertical = firstVerticalValue == alignmentSWTValues[FILL];
			// enable/disable the alignment grid depening on fill values
			fillActions[HORIZONTAL].updateAlignmentEnablement();
			// check the fill buttons if necessary
			fillActions[HORIZONTAL].setChecked(fillHorizontal);
			fillActions[VERTICAL].setChecked(fillVertical);

			// set the alignment grid selection to the center value if necessary
			if (fillHorizontal)
				firstHorizontalValue = alignmentSWTValues[CENTER];
			if (fillVertical)
				firstVerticalValue = alignmentSWTValues[CENTER];
			
			for (int i = 0; i < alignmentActions.length; i++) {		
				if (alignmentSWTValues[i % 3] == firstHorizontalValue &&
						alignmentSWTValues[i / 3] == firstVerticalValue) {
					alignmentActions[i].setChecked(true);
					selectedAlignmentAction = alignmentActions[i];
				}
				else
					alignmentActions[i].setChecked(false);
			}
		} else {
			for (int i = 0; i < alignmentActions.length; i++) {
				alignmentActions[i].setChecked(false);
			}
		}

	}

	/*
	 * If the fill value for each component is the same, check the appropriate action
	 * otherwise, uncheck all of them. 
	 */
	protected void handleSelectionChangedForGrabActions(List editparts) {
		boolean setChecked = true;
		boolean firstGrabHorizValue = getGrabValue((EditPart) editparts.get(0), HORIZONTAL);
		for (int i = 1; i < editparts.size(); i++) {
			if (firstGrabHorizValue != getGrabValue((EditPart) editparts.get(i), HORIZONTAL)) {
				setChecked = false;
				break;
			}
		}
		if (setChecked) {
			grabActions[HORIZONTAL].setChecked(firstGrabHorizValue);
		} else {
			grabActions[HORIZONTAL].setChecked(false);
		}
		
		setChecked = true;
		boolean firstGrabVerticalValue = getGrabValue((EditPart) editparts.get(0), VERTICAL);
		for (int i = 1; i < editparts.size(); i++) {
			if (firstGrabVerticalValue != getGrabValue((EditPart) editparts.get(i), VERTICAL)) {
				setChecked = false;
				break;
			}
		}
		if (setChecked) {
			grabActions[VERTICAL].setChecked(firstGrabVerticalValue);
		} else {
			grabActions[VERTICAL].setChecked(false);
		}
	}
	
	protected void handleSelectionChangedForSpinners(List editparts) {
		for (int i = 0; i < editparts.size(); i++) {
			EditPart ep = (EditPart) editparts.get(i);
			if ( ep.getSelected() == AbstractEditPart.SELECTED_PRIMARY && ep.getModel() instanceof IJavaObjectInstance) {
				horizontalSpanValue = getSpanValue(ep, HORIZONTAL);
				verticalSpanValue = getSpanValue(ep, VERTICAL);
				heightHintValue = getHintValue(ep, sfHeightHint);
				widthHintValue = getHintValue(ep, sfWidthHint);
				horizontalIndentValue = getHorizontalIndentValue(ep);
				break;
			}
		}
		if (horizontalSpanSpinner != null)
			horizontalSpanSpinner.setSelection(horizontalSpanValue);
		if (verticalSpanSpinner != null)
			verticalSpanSpinner.setSelection(verticalSpanValue);
		if (heightHintSpinner != null && heightHintValue != 0)
			heightHintSpinner.setValue(heightHintValue);
		if (widthHintSpinner != null && widthHintValue != 0)
			widthHintSpinner.setValue(widthHintValue);
		if (horizontalIndentSpinner != null)
			horizontalIndentSpinner.setSelection(horizontalIndentValue);
	}
	
	/*
	 * Return true if any of the select controls has GridData for it's layout data. 
	 */
	protected boolean hasGridData(List editparts) {
		if (!editparts.isEmpty()) {
			for (int i = 0; i < editparts.size(); i++) {
				EditPart editpart = (EditPart) editparts.get(i);
				EObject control = (EObject) editpart.getModel();
				if (control != null) {
					if (control.eIsSet(sfControlLayoutData)) { return true; }
				}
			}
		}
		return false;
	}

	protected int getHorizontalAlignValue(EditPart ep) {
		IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		if (ps != null && getResourceSet(ep) != null) {
			IPropertySource gridData = (IPropertySource) ps.getPropertyValue(sfControlLayoutData);
			if (gridData != null) {
				Object horizPV = gridData.getPropertyValue(sfHorizontalAlignment);
				if (horizPV != null && horizPV instanceof IJavaDataTypeInstance) {
					IIntegerBeanProxy intProxy = (IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaDataTypeInstance) horizPV, rset);
					return intProxy.intValue();
				}
			}
		}
		return GridData.CENTER;
	}
	
	protected int getVerticalAlignValue(EditPart ep) {
		IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		if (ps != null && getResourceSet(ep) != null) {
			IPropertySource gridData = (IPropertySource) ps.getPropertyValue(sfControlLayoutData);
			if (gridData != null) {
				Object vertPV = gridData.getPropertyValue(sfVerticalAlignment);
				if (vertPV != null && vertPV instanceof IJavaDataTypeInstance) {
					IIntegerBeanProxy intProxy = (IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaDataTypeInstance) vertPV, rset);
					return intProxy.intValue();
				}
			}
		}
		return GridData.CENTER;
	}
	
	protected int getHorizontalIndentValue(EditPart ep) {
		IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		if (ps != null && getResourceSet(ep) != null) {
			IPropertySource gridData = (IPropertySource) ps.getPropertyValue(sfControlLayoutData);
			if (gridData != null) {
				Object horizPV = gridData.getPropertyValue(sfHorizontalIndent);
				if (horizPV != null && horizPV instanceof IJavaDataTypeInstance) {
					IIntegerBeanProxy intProxy = (IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaDataTypeInstance) horizPV, rset);
					return intProxy.intValue();
				}
			}
		}
		return 0;
	}
	
	protected boolean getGrabValue(EditPart ep, int grabType) {
		IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		if (ps != null && getResourceSet(ep) != null) {
			IPropertySource gridData = (IPropertySource) ps.getPropertyValue(sfControlLayoutData);
			if (gridData != null) {
				Object grabPV = gridData.getPropertyValue((grabType == HORIZONTAL) ? sfHorizontalGrab : sfVerticalGrab);
				if (grabPV != null && grabPV instanceof IJavaDataTypeInstance) {
					IBooleanBeanProxy booleanProxy = (IBooleanBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaDataTypeInstance) grabPV, rset);
					return booleanProxy.booleanValue();
				}
			}
		}
		return false;
	}
	
	protected int getSpanValue(EditPart ep, int orientation) {
		IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		if (ps != null && getResourceSet(ep) != null) {
			IPropertySource gridData = (IPropertySource) ps.getPropertyValue(sfControlLayoutData);
			if (gridData != null) {
				Object spanPV = gridData.getPropertyValue((orientation == HORIZONTAL) ? sfHorizontalSpan : sfVerticalSpan);
				if (spanPV != null && spanPV instanceof IJavaDataTypeInstance) {
					IIntegerBeanProxy intProxy = (IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaDataTypeInstance) spanPV, rset);
					return intProxy.intValue();
				}
			}
		}
		return 1;
	}
	
	protected int getHintValue(EditPart ep, EStructuralFeature sf) {
		IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		if (ps != null && getResourceSet(ep) != null) {
			IPropertySource gridData = (IPropertySource) ps.getPropertyValue(sfControlLayoutData);
			if (gridData != null) {
				Object hintPV = gridData.getPropertyValue(sf);
				if (hintPV != null && hintPV instanceof IJavaDataTypeInstance) {
					IIntegerBeanProxy intProxy = (IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaDataTypeInstance) hintPV, rset);
					return intProxy.intValue();
				}
			}
		}
		return -1;
	}
	
	/*
	 * reset the resource set and structural features
	 */
	private void resetVariables() {
		rset = null;
		sfControlLayoutData = null;
		sfHorizontalAlignment = null;
		sfVerticalAlignment = null;
		sfHorizontalGrab = null;
		sfVerticalGrab = null;
		sfHorizontalSpan = null;
		sfVerticalSpan = null;
		sfHorizontalIndent = null;
		sfHeightHint = null;
		sfWidthHint = null;
	}
	/*
	 * Return the ResourceSet for this editpart. Initialize the structural features also. 
	 */
	protected ResourceSet getResourceSet(EditPart editpart) {
		if (rset == null) {
			rset = EMFEditDomainHelper.getResourceSet(EditDomain.getEditDomain(editpart));
			sfControlLayoutData = JavaInstantiation.getReference(rset, SWTConstants.SF_CONTROL_LAYOUTDATA);
			sfHorizontalAlignment = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_DATA_HORIZONTAL_ALIGN);
			sfVerticalAlignment = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_DATA_VERTICAL_ALIGN);
			sfHorizontalGrab = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_DATA_HORIZONTAL_GRAB);
			sfVerticalGrab = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_DATA_VERTICAL_GRAB);
			sfHorizontalSpan = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_DATA_HORIZONTAL_SPAN);
			sfVerticalSpan = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_DATA_VERTICAL_SPAN);
			sfHorizontalIndent = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_DATA_HORIZONTAL_INDENT);
			sfHeightHint = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_DATA_HEIGHT_HINT);
			sfWidthHint = JavaInstantiation.getSFeature(rset, SWTConstants.SF_GRID_DATA_WIDTH_HINT);
		}
		return rset;
	}
	
	protected void handleSelectionProviderInitialization(ISelectionProvider selectionProvider) {
		// We don't use GEF SelectionActions, so don't need this.
	}
	
	/*
	 * Return true if the parent's layout policy is a GridLayout.
	 * If parent is a tree editpart (selected from the Beans viewer, we need to get its
	 * corresponding graphical editpart from the Graph viewer in order to check its layout policy.
	 */
	public boolean isValidParent(EditPart parent) {
		if (parent instanceof TreeEditPart) {
			EditDomain ed = EditDomain.getEditDomain(parent);
			EditPartViewer viewer = (EditPartViewer) ed.getEditorPart().getAdapter(EditPartViewer.class);
			if (viewer != null) {
				// Get the graphical editpart using the model that is common between the two viewers
				EditPart ep = (EditPart) viewer.getEditPartRegistry().get(parent.getModel());
				if (ep != null)
					parent = ep;
			}
		}
		IActionFilter af = (IActionFilter) ((IAdaptable) parent).getAdapter(IActionFilter.class);
		if (af != null && af.testAttribute(parent, LAYOUT_FILTER_KEY, GridLayoutEditPolicy.LAYOUT_ID)) { //$NON-NLS-1$
			return true;
		}
		return false;
	}
}
