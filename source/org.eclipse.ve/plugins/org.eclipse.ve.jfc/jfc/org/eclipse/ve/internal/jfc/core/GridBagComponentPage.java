/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: GridBagComponentPage.java,v $
 *  $Revision: 1.11 $  $Date: 2005-06-22 17:13:18 $ 
 */

import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.*;
import org.eclipse.gef.commands.*;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.common.commands.AbstractCommand;

/**
 * This layout page resides on the Customize Layout window's Components tab
 * It shows and allows selection of the "anchor" and "fill" properties of an AWT GridBagConstraints object which is
 * the constraint on a component that is a child of a container that uses a GridBagLayout as it's layout manager. 
 */
public class GridBagComponentPage extends JavaBeanCustomizeLayoutPage {
	protected IEditorPart fEditorPart;
	private final static String[] resAnchorPrefixIDs = { 
		"AnchorAction.northwest", //$NON-NLS-1$
		"AnchorAction.north", //$NON-NLS-1$
		"AnchorAction.northeast", //$NON-NLS-1$
		"AnchorAction.west", //$NON-NLS-1$
		"AnchorAction.center", //$NON-NLS-1$
		"AnchorAction.east", //$NON-NLS-1$
		"AnchorAction.southwest", //$NON-NLS-1$
		"AnchorAction.south", //$NON-NLS-1$
		"AnchorAction.southeast" }; //$NON-NLS-1$
	private final static String[] resAnchorPrefixLabels = { 
		JFCMessages.AnchorAction_northwest_label,
		JFCMessages.AnchorAction_north_label,
		JFCMessages.AnchorAction_northeast_label,
		JFCMessages.AnchorAction_west_label,
		JFCMessages.AnchorAction_center_label,
		JFCMessages.AnchorAction_east_label,
		JFCMessages.AnchorAction_southwest_label,
		JFCMessages.AnchorAction_south_label,
		JFCMessages.AnchorAction_southeast_label};
	private final static String[] resAnchorPrefixImages = { 
		JFCMessages.AnchorAction_northwest_image,
		JFCMessages.AnchorAction_north_image,
		JFCMessages.AnchorAction_northeast_image,
		JFCMessages.AnchorAction_west_image,
		JFCMessages.AnchorAction_center_image,
		JFCMessages.AnchorAction_east_image,
		JFCMessages.AnchorAction_southwest_image,
		JFCMessages.AnchorAction_south_image,
		JFCMessages.AnchorAction_southeast_image};
	private final static String[] resAnchorPrefixTooltips = { 
		JFCMessages.AnchorAction_northwest_tooltip,
		JFCMessages.AnchorAction_north_tooltip,
		JFCMessages.AnchorAction_northeast_tooltip,
		JFCMessages.AnchorAction_west_tooltip,
		JFCMessages.AnchorAction_center_tooltip,
		JFCMessages.AnchorAction_east_tooltip,
		JFCMessages.AnchorAction_southwest_tooltip,
		JFCMessages.AnchorAction_south_tooltip,
		JFCMessages.AnchorAction_southeast_tooltip};

	protected static String[] anchorInitStrings = new String[] {
		"java.awt.GridBagConstraints.NORTHWEST", //$NON-NLS-1$
		"java.awt.GridBagConstraints.NORTH", //$NON-NLS-1$
		"java.awt.GridBagConstraints.NORTHEAST", //$NON-NLS-1$
		"java.awt.GridBagConstraints.WEST", //$NON-NLS-1$
		"java.awt.GridBagConstraints.CENTER", //$NON-NLS-1$
		"java.awt.GridBagConstraints.EAST", //$NON-NLS-1$
		"java.awt.GridBagConstraints.SOUTHWEST", //$NON-NLS-1$
		"java.awt.GridBagConstraints.SOUTH", //$NON-NLS-1$
		"java.awt.GridBagConstraints.SOUTHEAST" //$NON-NLS-1$
	};	

	protected AnchorAction[] anchorActions =
		{
			new AnchorAction(NW),
			new AnchorAction(NORTH),
			new AnchorAction(NE),
			new AnchorAction(WEST),
			new AnchorAction(CENTER),
			new AnchorAction(EAST),
			new AnchorAction(SW),
			new AnchorAction(SOUTH),
			new AnchorAction(SE)};
			
	public final static int NW = 0, NORTH = 1, NE = 2, WEST = 3, CENTER = 4, EAST = 5, SW = 6, SOUTH = 7, SE = 8;
	protected final static int anchorAWTValue[] = { 18, 11, 12, 17, 10, 13, 16, 15, 14 };

	private final static String[] resFillPrefixIDs = { 
		"FillAction.horizontal.", //$NON-NLS-1$
		"FillAction.vertical."}; //$NON-NLS-1$
	private final static String[] resFillPrefixLabels = { 
		JFCMessages.FillAction_horizontal_label,
		JFCMessages.FillAction_vertical_label};
	private final static String[] resFillPrefixTooltips = { 
		JFCMessages.FillAction_horizontal_tooltip,
		JFCMessages.FillAction_vertical_tooltip};
	private final static String[] resFillPrefixImages = { 
		JFCMessages.FillAction_horizontal_image,
		JFCMessages.FillAction_vertical_image};

	protected static String[] fillInitStrings = new String[] {
		"java.awt.GridBagConstraints.HORIZONTAL", //$NON-NLS-1$
		"java.awt.GridBagConstraints.VERTICAL", //$NON-NLS-1$
		"java.awt.GridBagConstraints.NONE", //$NON-NLS-1$
		"java.awt.GridBagConstraints.BOTH" //$NON-NLS-1$
	};	
	private FillAction[] fillActions =
		{
			new FillAction(FILL_HORIZONTAL),
			new FillAction(FILL_VERTICAL)};
			
	public final static int FILL_HORIZONTAL = 0, FILL_VERTICAL = 1, FILL_NONE = 2, FILL_BOTH = 3;
	protected final static int fillAWTValue[] = {2, 3, 0, 1};

	protected EReference sfComponents, sfConstraintComponent, sfConstraintConstraint;
	protected EStructuralFeature sfAnchor, sfFill, sfInsets;
	protected ResourceSet rset;
	protected AnchorAction selectedAnchorAction;
	protected int currentFillValue;
	
	protected Spinner topSpinner, leftSpinner, bottomSpinner, rightSpinner;
	protected Insets componentInsets = null;
	public final static int INSETS_TOP = 0, INSETS_LEFT = 1, INSETS_BOTTOM = 2, INSETS_RIGHT = 3;

	/*
	 * 
	 * Inner class used for the Anchor Actions
	 */
	public class AnchorAction extends Action {

		protected int fAnchorType;

		public AnchorAction(int anchorType) {
			super(null, Action.AS_CHECK_BOX);
			// Default to center anchor if the anchor type is incorrect
			if (!(anchorType >= 0 && anchorType < resAnchorPrefixLabels.length))
				fAnchorType = CENTER;
			else
				fAnchorType = anchorType;
			setText(resAnchorPrefixLabels[fAnchorType]); 
			setToolTipText(resAnchorPrefixTooltips[fAnchorType]); 
			// There are three images, one for full color ( that is the hover one )
			// one for disabled and one for enabled
			String graphicName = resAnchorPrefixImages[fAnchorType];
			setImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/elcl16/" + graphicName)); //$NON-NLS-1$
			setHoverImageDescriptor(getImageDescriptor());
			setDisabledImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/dlcl16/" + graphicName)); //$NON-NLS-1$
			setEnabled(true);
			setId(getActionId(fAnchorType));
		}

		/**
		 * Static method that returns the action id based on the alignment type.
		 */
		public String getActionId(int anchorType) {
			return ((anchorType >= 0 && anchorType < resAnchorPrefixIDs.length) ? resAnchorPrefixIDs[anchorType] : resAnchorPrefixIDs[CENTER]);
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
			if (selectedAnchorAction != this) {
				execute(createAnchorCommand(getSelectedObjects(), fAnchorType));
				selectedAnchorAction = this;
			}
			for (int i = 0; i < anchorActions.length; i++) {
				if (!(anchorActions[i] == this))
					anchorActions[i].setChecked(false);
				else if (!isChecked())
					setChecked(true);
			}
		}

	}
	/*
	 * Inner class used for the Fill actions
	 */
	public class FillAction extends Action {

		protected int fFillType;

		public FillAction(int fillType) {
			super(null, Action.AS_CHECK_BOX);
			// Default to center anchor if the anchor type is incorrect
			if (!(fillType >= 0 && fillType < resFillPrefixLabels.length))
				fFillType = FILL_HORIZONTAL;
			else
				fFillType = fillType;
			setText(resFillPrefixLabels[fFillType]); 
			setToolTipText(resFillPrefixTooltips[fFillType]); 
			// There are three images, one for full color ( that is the hover one )
			// one for disabled and one for enabled
			String graphicName = resFillPrefixImages[fFillType];
			// The file structure of these is that they exist in the plugin directory with three folder names, e.g.
			// /icons/full/clc16/anchorleft_obj.gif for the color one
			// and elc16 for enabled and dlc16 for disasbled
			setImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/elcl16/" + graphicName)); //$NON-NLS-1$
			setHoverImageDescriptor(getImageDescriptor()); 
			setDisabledImageDescriptor(CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/dlcl16/" + graphicName));	 //$NON-NLS-1$
			setEnabled(true);
			setId(getActionId(fFillType));
		}

		/**
		 * Static method that returns the action id based on the alignment type.
		 */
		public String getActionId(int fillType) {
			return ((fillType >= 0 && fillType < resFillPrefixIDs.length) ? resFillPrefixIDs[fillType] : resFillPrefixIDs[FILL_HORIZONTAL]);
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
		 * This fill type was selected. Determine if the horizontal or vertical or both
		 * buttons are selected. GridBagConstraints.NONE is the default.
		 * Then execute the commands to apply the fill type to the selected editparts.
		 */
		public void run() {
			super.run();
			//Still needs work... will finish up tomorrow
			int fillValue = FILL_NONE;
			if (fillActions[FILL_HORIZONTAL].isChecked() && fillActions[FILL_VERTICAL].isChecked())
				currentFillValue = fillValue = FILL_BOTH;
			else if (fillActions[FILL_HORIZONTAL].isChecked())
				currentFillValue = fillValue = FILL_HORIZONTAL;
			else if (fillActions[FILL_VERTICAL].isChecked())
				currentFillValue = fillValue = FILL_VERTICAL;
			execute(createFillCommand(getSelectedObjects(), fillValue));
		}

	}
	public class LabelAction extends Action {
		Text fText;
		int fActionType;
		public final static int INSETS_MINUS = 0, INSETS_PLUS = 1;
		public LabelAction(String text, int actionType) {
			super(null, Action.AS_PUSH_BUTTON);
			if (actionType != INSETS_MINUS && actionType != INSETS_PLUS)
				fActionType = INSETS_PLUS;
			else
				fActionType = actionType;
			setText(text);
			setEnabled(true);
			setId("labelaction" + text); //$NON-NLS-1$
		}
		public void setTextControl(Text text) {
			fText = text;
		}
		/* (non-Javadoc)
		 * @see org.eclipse.jface.action.IAction#run()
		 */
		public void run() {
			if (fText != null){
				String textValue = fText.getText();
				try {
					int value = Integer.parseInt(textValue);
					if (fActionType == INSETS_PLUS)
						fText.setText(String.valueOf(++value));
					else if (value > 0)
						fText.setText(String.valueOf(--value));
				} catch (NumberFormatException e) {
					fText.setText(textValue);
				}
			}
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
	 * The anchor value is based on the type of action and is retrieved from the anchorAWTValue table.
	 */
	protected Command createAnchorCommand(List editparts, int anchorType) {
		if (!editparts.isEmpty()) {
			CommandBuilder cb = new CommandBuilder();
			for (int i = 0; i < editparts.size(); i++) {
				EditPart editpart = (EditPart)editparts.get(i);
				EObject constraintComponent = InverseMaintenanceAdapter.getIntermediateReference((EObject)editpart.getParent().getModel(), sfComponents, sfConstraintComponent, (IJavaObjectInstance)editpart.getModel());
				if (constraintComponent != null) {
					IJavaObjectInstance gridbagconstraint = (IJavaObjectInstance) constraintComponent.eGet(sfConstraintConstraint);
					if (gridbagconstraint != null) {
						RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(editpart), null, false);
						Object anchorObject = BeanUtilities.createJavaObject("int", rset, anchorInitStrings[anchorType]); //$NON-NLS-1$
						componentCB.applyAttributeSetting(gridbagconstraint, sfAnchor, anchorObject);
						componentCB.applyAttributeSetting(constraintComponent, sfConstraintConstraint, gridbagconstraint);
						cb.append(componentCB.getCommand());
					}
				}
			}
			return cb.getCommand();
		}
		return UnexecutableCommand.INSTANCE;
	}

	/*
	 * Return the commands to set the fill value for the selected editparts
	 * The fill value is based on the type of action and is retrieved from the fillAWTValue table.
	 */
	protected Command createFillCommand(List editparts, int fillType) {
		if (!editparts.isEmpty()) {
			CommandBuilder cb = new CommandBuilder();
			for (int i = 0; i < editparts.size(); i++) {
				EditPart editpart = (EditPart)editparts.get(i);
				EObject constraintComponent = InverseMaintenanceAdapter.getIntermediateReference((EObject)editpart.getParent().getModel(), sfComponents, sfConstraintComponent, (IJavaObjectInstance)editpart.getModel());
				if (constraintComponent != null) {
					IJavaObjectInstance gridbagconstraint = (IJavaObjectInstance) constraintComponent.eGet(sfConstraintConstraint);
					if (gridbagconstraint != null) {
						RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(editpart), null, false);
						Object fillObject = BeanUtilities.createJavaObject("int", rset, fillInitStrings[fillType]); //$NON-NLS-1$
						componentCB.applyAttributeSetting(gridbagconstraint, sfFill, fillObject);
						componentCB.applyAttributeSetting(constraintComponent, sfConstraintConstraint, gridbagconstraint);
						cb.append(componentCB.getCommand());
					}
				}
			}
			return cb.getCommand();
		}
		return UnexecutableCommand.INSTANCE;
	}
	/*
	 * Return the commands to set the GridBagConstraints insets value for the selected editparts
	 */
	protected Command createInsetsCommand(List editparts, Insets changedInsets, int insetsPosition, Spinner spinner) {
		if (!editparts.isEmpty()) {
			CommandBuilder cb = new CommandBuilder();
			for (int i = 0; i < editparts.size(); i++) {
				EditPart editpart = (EditPart)editparts.get(i);
				// Get the insets for this editpart and only change
				// the specific inset position that was modified
				Insets insets = getInsetsValue(editpart);
				if (insets != null) {
					if (insetsPosition == INSETS_TOP)
						insets.top = changedInsets.top;
					else if (insetsPosition == INSETS_LEFT)
						insets.left = changedInsets.left;
					else if (insetsPosition == INSETS_RIGHT)
						insets.right = changedInsets.right;
					else if (insetsPosition == INSETS_BOTTOM)
						insets.bottom = changedInsets.bottom;
					EObject constraintComponent = InverseMaintenanceAdapter.getIntermediateReference((EObject)editpart.getParent().getModel(), sfComponents, sfConstraintComponent, (IJavaObjectInstance)editpart.getModel());
					if (constraintComponent != null) {
						IJavaObjectInstance gridbagconstraint = (IJavaObjectInstance) constraintComponent.eGet(sfConstraintConstraint);
						if (gridbagconstraint != null) {
							RuledCommandBuilder componentCB = new RuledCommandBuilder(EditDomain.getEditDomain(editpart), null, false);
							Object insetsObject = BeanUtilities.createJavaObject("java.awt.Insets", rset, InsetsJavaClassCellEditor.getJavaInitializationString(insets)); //$NON-NLS-1$
							componentCB.applyAttributeSetting(gridbagconstraint, sfInsets, insetsObject);
							componentCB.applyAttributeSetting(constraintComponent, sfConstraintConstraint, gridbagconstraint);
							cb.append(componentCB.getCommand());
						}
					}
				}
			}
			cb.append(new EnableSpinnerCommand(spinner));
			return cb.getCommand();
		}
		spinner.setEnabled(true);
		return UnexecutableCommand.INSTANCE;
	}
	/*
	 * Command that is used to re-enable the spinner since we don't want the user
	 * changing the insets while the insets is being updated. This prevents a ConcurrentModificationException
	 * that is caused when the insets are being read from the spinner side while the apply attribute setting
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
	public Control getControl(Composite parent) {

		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout(3, false));
		Group anchorGroup = createGroup(mainComposite, JFCMessages.GridBagComponentPage_Anchor, 3, 0, 0); 

		for (int i = 0; i < anchorActions.length; i++) {
			ActionContributionItem ac = new ActionContributionItem(anchorActions[i]);
			ac.fill(anchorGroup);
		}
		Group fillGroup = createGroup(mainComposite, JFCMessages.GridBagComponentPage_Fill, 1, 0, 0); 
		for (int i = 0; i < fillActions.length; i++) {
			ActionContributionItem ac = new ActionContributionItem(fillActions[i]);
			ac.fill(fillGroup);
		}
		Group insetsGroup = createGroup(mainComposite, JFCMessages.GridBagComponentPage_Insets, 2, 5, 4); 
		createInsetsControl(insetsGroup);

		return mainComposite;

	}
	protected void createInsetsControl(Group insetsGroup) {
		Label lbl;
		lbl = new Label(insetsGroup, SWT.NONE);
		lbl.setText(JFCMessages.GridBagComponentPage_InsetsGroup_Top); 
		int top =0, left = 0, bottom = 0, right = 0;
		if (componentInsets != null) {
			top = componentInsets.top;
			left = componentInsets.left;
			bottom = componentInsets.bottom;
			right = componentInsets.right;
		}
		topSpinner = new Spinner(insetsGroup, SWT.NONE);
		topSpinner.setEnabled(componentInsets != null ? true : false);
		topSpinner.setSelection(top);
		topSpinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				int top = topSpinner.getSelection();
				if (componentInsets != null && top != componentInsets.top) {
					componentInsets.top = top;
					execute(createInsetsCommand(getSelectedObjects(), componentInsets, INSETS_TOP, topSpinner));
				} else {
					// Need this in the case where no command has been executed and we need to tell the
					// spinner to reset it's 'command in progress' switch so it can except input again.
					topSpinner.setEnabled(true);
				}
			}
		});
		lbl = new Label(insetsGroup, SWT.NONE);
		lbl.setText(JFCMessages.GridBagComponentPage_InsetsGroup_Left); 
		leftSpinner = new Spinner(insetsGroup, SWT.NONE);
		leftSpinner.setEnabled(componentInsets != null ? true : false);
		leftSpinner.setSelection(left);
		leftSpinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				int left = leftSpinner.getSelection();
				if (componentInsets != null && left != componentInsets.left) {
					componentInsets.left = left;
					execute(createInsetsCommand(getSelectedObjects(), componentInsets, INSETS_LEFT, leftSpinner));
				} else {
					// Need this in the case where no command has been executed and we need to tell the
					// spinner to reset it's 'command in progress' switch so it can except input again.
					leftSpinner.setEnabled(true);
				}
			}
		});
		lbl = new Label(insetsGroup, SWT.NONE);
		lbl.setText(JFCMessages.GridBagComponentPage_InsetsGroup_Bottom); 
		bottomSpinner = new Spinner(insetsGroup, SWT.NONE);
		bottomSpinner.setEnabled(componentInsets != null ? true : false);
		bottomSpinner.setSelection(bottom);
		bottomSpinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				int bottom = bottomSpinner.getSelection();
				if (componentInsets != null && bottom != componentInsets.bottom) {
					componentInsets.bottom = bottom;
					execute(createInsetsCommand(getSelectedObjects(), componentInsets, INSETS_BOTTOM, bottomSpinner));
				} else {
					// Need this in the case where no command has been executed and we need to tell the
					// spinner to reset it's 'command in progress' switch so it can except input again.
					bottomSpinner.setEnabled(true);
				}
			}
		});
		lbl = new Label(insetsGroup, SWT.NONE);
		lbl.setText(JFCMessages.GridBagComponentPage_InsetsGroup_Right); 
		rightSpinner = new Spinner(insetsGroup, SWT.NONE);
		rightSpinner.setEnabled(componentInsets != null ? true : false);
		rightSpinner.setSelection(right);
		rightSpinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				int right = rightSpinner.getSelection();
				if (componentInsets != null && right != componentInsets.right) {
					componentInsets.right = right;
					execute(createInsetsCommand(getSelectedObjects(), componentInsets, INSETS_RIGHT, rightSpinner));
				} else {
					// Need this in the case where no command has been executed and we need to tell the
					// spinner to reset it's 'command in progress' switch so it can except input again.
					rightSpinner.setEnabled(true);
				}
			}
		});
	}
	
	protected Group createGroup(Composite aParent, String title, int numColumns, int verticalSpacing, int horizontalSpacing) {
		Group group = new Group(aParent, SWT.NONE);
		group.setText(title);
		GridLayout gridLayout = new GridLayout(numColumns, false);
//		gridLayout.numColumns = numColumns;
		gridLayout.verticalSpacing = verticalSpacing;
		gridLayout.horizontalSpacing = horizontalSpacing;
		group.setLayout(gridLayout);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.verticalAlignment = GridData.FILL_VERTICAL;
//		data.horizontalAlignment = GridData.FILL_HORIZONTAL;
//		data.grabExcessHorizontalSpace = true;
		group.setLayoutData(data);
		return group;
	}

	protected void enableAnchorActions(boolean enable) {
		for (int i = 0; i < anchorActions.length; i++) {
			anchorActions[i].setEnabled(enable);
			if (!enable)
				anchorActions[i].setChecked(false);
		}
	}
	protected void enableFillActions(boolean enable) {
		for (int i = 0; i < fillActions.length; i++) {
			fillActions[i].setEnabled(enable);
			if (!enable)
				fillActions[i].setChecked(false);
		}
	}
	protected void enableInsets(boolean enable) {
		if (topSpinner != null)
			topSpinner.setEnabled(enable);
		if (leftSpinner != null)
			leftSpinner.setEnabled(enable);
		if (bottomSpinner != null)
			bottomSpinner.setEnabled(enable);
		if (rightSpinner != null)
			rightSpinner.setEnabled(enable);
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
		return JFCMessages.GridBagComponentPage_Gridbag; 
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.CustomizeLayoutPage#getToolTipText()
	 */
	public String getToolTipText() {
		return JFCMessages.GridBagComponentPage_ToolTipText; 
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.cde.core.CustomizeLayoutPage#handleEditorPartChanged(org.eclipse.ui.IEditorPart)
	 * 
	 * The editorpart changed. Pass this on to the AnchorActions and fillActions 
	 * and reset the resource set and structural features.
	 */
	protected void handleEditorPartChanged(IEditorPart oldEditorPart) {
		IEditorPart newEditorPart = getEditorPart();
		for (int i = 0; i < anchorActions.length; i++) {
			anchorActions[i].setEditorPart(newEditorPart);
		}
		for (int i = 0; i < fillActions.length; i++) {
			fillActions[i].setEditorPart(newEditorPart);
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
						enableAnchorActions(true);
						enableFillActions(true);
						enableInsets(true);
						handleSelectionChangedForAnchorActions(editparts);
						handleSelectionChangedForFillActions(editparts);
						handleSelectionChangedForInsets(editparts);
						return true;
					}
				}
			}
		}
		// By default if the initial checks failed, disable and uncheck all the actions.
		enableAnchorActions(false);
		enableFillActions(false);
		enableInsets(false);
		return false;
	}
	
	/*
	 * If the anchor value for each component is the same, check the appropriate action
	 * otherwise, uncheck all of them. 
	 */
	protected void handleSelectionChangedForAnchorActions(List editparts) {
		boolean setChecked = true;
		int firstAnchorValue = getAnchorValue((EditPart) editparts.get(0));
		for (int i = 1; i < editparts.size(); i++) {
			if (firstAnchorValue != getAnchorValue((EditPart) editparts.get(i))) {
				setChecked = false;
				break;
			}
		}
		if (setChecked) {
			for (int i = 0; i < anchorActions.length; i++) {
				if (anchorAWTValue[i] == firstAnchorValue) {
					anchorActions[i].setChecked(true);
					selectedAnchorAction = anchorActions[i];
				}
				else
					anchorActions[i].setChecked(false);
			}
		} else {
			for (int i = 0; i < anchorActions.length; i++) {
				anchorActions[i].setChecked(false);
			}
			selectedAnchorAction = null;
		}
	}

	/*
	 * If the fill value for each component is the same, check the appropriate action
	 * otherwise, uncheck all of them. 
	 */
	protected void handleSelectionChangedForFillActions(List editparts) {
		boolean setChecked = true;
		int firstFillValue = getFillValue((EditPart) editparts.get(0));
		for (int i = 1; i < editparts.size(); i++) {
			if (firstFillValue != getFillValue((EditPart) editparts.get(i))) {
				setChecked = false;
				break;
			}
		}
		if (setChecked) {
			if (fillAWTValue[FILL_NONE] == firstFillValue){
				fillActions[FILL_HORIZONTAL].setChecked(false);
				fillActions[FILL_VERTICAL].setChecked(false);
				currentFillValue = FILL_NONE;
			} else if (fillAWTValue[FILL_HORIZONTAL] == firstFillValue){
				fillActions[FILL_HORIZONTAL].setChecked(true);
				fillActions[FILL_VERTICAL].setChecked(false);
				currentFillValue = FILL_HORIZONTAL;
			} else if (fillAWTValue[FILL_VERTICAL] == firstFillValue) {
				fillActions[FILL_VERTICAL].setChecked(true);
				fillActions[FILL_HORIZONTAL].setChecked(false);
				currentFillValue = FILL_VERTICAL;
			} else if (fillAWTValue[FILL_BOTH] == firstFillValue) {
				fillActions[FILL_HORIZONTAL].setChecked(true);
				fillActions[FILL_VERTICAL].setChecked(true);
				currentFillValue = FILL_BOTH;
			}
		} else {
			for (int i = 0; i < fillActions.length; i++) {
				fillActions[i].setChecked(false);
			}
		}
	}
	/*
	 * Set the insets value bases on the primary selected editpart
	 */
	protected void handleSelectionChangedForInsets(List editparts) {
		for (int i = 0; i < editparts.size(); i++) {
			EditPart ep = (EditPart) editparts.get(i);
			if ( ep.getSelected() == AbstractEditPart.SELECTED_PRIMARY && ep.getModel() instanceof IJavaObjectInstance) {
				componentInsets = getInsetsValue(ep);
				break;
			}
		}
		setInsetsValues(componentInsets);
	}
	protected void setInsetsValues(Insets componentInsets) {
		if (componentInsets == null) {
			enableInsets(false);
			return;
		} else
			enableInsets(true);
		
		if (topSpinner != null)
			topSpinner.setSelection(componentInsets.top);
		if (leftSpinner != null)
			leftSpinner.setSelection(componentInsets.left);
		if (bottomSpinner != null)
			bottomSpinner.setSelection(componentInsets.bottom);
		if (rightSpinner != null)
			rightSpinner.setSelection(componentInsets.right);
	}

	protected int getAnchorValue(EditPart ep) {
		IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		if (ps != null && getResourceSet(ep) != null) {
			IPropertySource gridbagconstraint = (IPropertySource) ps.getPropertyValue(sfConstraintConstraint);
			if (gridbagconstraint != null) {
				Object anchorPV = gridbagconstraint.getPropertyValue(sfAnchor);
				if (anchorPV != null && anchorPV instanceof IJavaDataTypeInstance) {
					IIntegerBeanProxy intProxy = (IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaDataTypeInstance) anchorPV, rset);
					return intProxy.intValue();
				}
			}
		}
		return anchorAWTValue[CENTER];
	}
	protected int getFillValue(EditPart ep) {
		IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		if (ps != null && getResourceSet(ep) != null) {
			IPropertySource gridbagconstraint = (IPropertySource) ps.getPropertyValue(sfConstraintConstraint);
			if (gridbagconstraint != null) {
				Object fillPV = gridbagconstraint.getPropertyValue(sfFill);
				if (fillPV != null && fillPV instanceof IJavaDataTypeInstance) {
					IIntegerBeanProxy intProxy = (IIntegerBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaDataTypeInstance) fillPV, rset);
					return intProxy.intValue();
				}
			}
		}
		return fillAWTValue[FILL_NONE];
	}
	/*
	 * Return the value of the GridBagConstraints.insets for this component
	 */
	protected Insets getInsetsValue(EditPart ep) {
		int top = 0, left = 0, bottom = 0, right = 0;
		IPropertySource ps = (IPropertySource) ep.getAdapter(IPropertySource.class);
		if (ps != null && getResourceSet(ep) != null) {
			IPropertySource gridbagconstraint = (IPropertySource) ps.getPropertyValue(sfConstraintConstraint);
			if (gridbagconstraint != null) {
				IPropertySource insetsPS = (IPropertySource)gridbagconstraint.getPropertyValue(sfInsets);
				Object insetsPV = insetsPS.getEditableValue();
				if (insetsPV != null && insetsPV instanceof IJavaObjectInstance) {
					IBeanProxy insetsProxy = BeanProxyUtilities.getBeanProxy((IJavaObjectInstance) insetsPV, rset);
					if (insetsProxy != null) {
						IIntegerBeanProxy intProxy = null;
						try {
							IFieldProxy fieldProxy = insetsProxy.getTypeProxy().getFieldProxy("top"); //$NON-NLS-1$
							intProxy = (IIntegerBeanProxy) fieldProxy.get(insetsProxy);
							if (intProxy != null) { 
								top = intProxy.intValue();
							}
							fieldProxy = insetsProxy.getTypeProxy().getFieldProxy("left"); //$NON-NLS-1$
							intProxy = (IIntegerBeanProxy) fieldProxy.get(insetsProxy);
							if (intProxy != null) { 
								left = intProxy.intValue();
							}
							fieldProxy = insetsProxy.getTypeProxy().getFieldProxy("bottom"); //$NON-NLS-1$
							intProxy = (IIntegerBeanProxy) fieldProxy.get(insetsProxy);
							if (intProxy != null) { 
								bottom = intProxy.intValue();
							}
							fieldProxy = insetsProxy.getTypeProxy().getFieldProxy("right"); //$NON-NLS-1$
							intProxy = (IIntegerBeanProxy) fieldProxy.get(insetsProxy);
							if (intProxy != null) { 
								right = intProxy.intValue();
							}
						} catch (ThrowableProxy e) {
							// Do nothing. null will be returned.
						}
						return new Insets(top, left, bottom, right);
					}
				}
			}
		}
		return null;
	}
	/*
	 * reset the resource set and structural features
	 */
	private void resetVariables() {
		rset = null;
		sfConstraintConstraint = null;
		sfAnchor = null;
	}
	/*
	 * Return the ResourceSet for this editpart. Initialize the structural features also. 
	 */
	protected ResourceSet getResourceSet(EditPart editpart) {
		if (rset == null) {
			rset = EMFEditDomainHelper.getResourceSet(EditDomain.getEditDomain(editpart));
			sfConstraintConstraint = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_CONSTRAINT);
			sfComponents = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_COMPONENTS);
			sfConstraintComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_COMPONENT);
			sfAnchor = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDBAGCONSTRAINTS_ANCHOR);
			sfFill = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDBAGCONSTRAINTS_FILL);
			sfInsets = JavaInstantiation.getSFeature(rset, JFCConstants.SF_GRIDBAGCONSTRAINTS_INSETS);
		}
		return rset;
	}
	
	protected void handleSelectionProviderInitialization(ISelectionProvider selectionProvider) {
		// We don't use GEF SelectionActions, so don't need this.
	}
	/*
	 * Return true if the parent's layout policy is a GridBagLayout.
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
		if (af != null && af.testAttribute(parent, LAYOUT_FILTER_KEY, GridBagLayoutEditPolicy.LAYOUT_ID)) { //$NON-NLS-1$
			return true;
		}
		return false;
	}
}
