package org.eclipse.ve.internal.jfc.core;
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
 *  $RCSfile: JSplitPaneOrientationPropertyDescriptor.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 18:29:32 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.command.ICommandPropertyDescriptor;

/**
 * Property descriptor for JSplitPane.orientation.
 * 
 * This is needed for reseting the components in the split pane to the correct positions. 
 * For example, if the orientation is horizontal, the positions are left and right. If the
 * orientation is changed to vertical, the components need to be reset to top and bottom.
 * This is so the code is gen'd correctly (even though Swing allows for this discrepancy)
 * and so the label provider will reflect the positions correctly.
 * 
 */
public class JSplitPaneOrientationPropertyDescriptor
	extends BeanPropertyDescriptorAdapter
	implements ICommandPropertyDescriptor {

	int VERTICAL_SPLIT = 0, HORIZONTAL_SPLIT = 1;

	public Command setValue(IPropertySource source, Object setValue) {
		IJavaObjectInstance splitpane = (IJavaObjectInstance) source.getEditableValue();
		IBeanProxyHost h = BeanProxyUtilities.getBeanProxyHost(splitpane);
		RuledCommandBuilder cb = new RuledCommandBuilder(h.getBeanProxyDomain().getEditDomain());
		
		EStructuralFeature sfLeftComponent =
			JavaInstantiation.getSFeature(splitpane, JFCConstants.SF_JSPLITPANE_LEFTCOMPONENT),
			sfRightComponent = JavaInstantiation.getSFeature(splitpane, JFCConstants.SF_JSPLITPANE_RIGHTCOMPONENT),
			sfTopComponent = JavaInstantiation.getSFeature(splitpane, JFCConstants.SF_JSPLITPANE_TOPCOMPONENT),
			sfBottomComponent = JavaInstantiation.getSFeature(splitpane, JFCConstants.SF_JSPLITPANE_BOTTOMCOMPONENT);
		IJavaInstance value = (IJavaInstance) setValue;
		
		// First unset those that need to be unset, not under rule since they are not going away.
		// Accumulate the set back to new setting in a separate command builder so that they can be
		// applied AFTER the orientation is changed.
		cb.setApplyRules(false);
		CommandBuilder afterCB = new CommandBuilder();
		
		String initString = value.getInitializationString();
		int orientation = HORIZONTAL_SPLIT; // default is HORIZONTAL_SPLIT
		if (initString.equals("0") //$NON-NLS-1$
			|| initString.equals("VERTICAL_SPLIT") //$NON-NLS-1$
			|| initString.equals("javax.swing.JSplitPane.VERTICAL_SPLIT")) //$NON-NLS-1$
			orientation = VERTICAL_SPLIT;
		else if (
			initString.equals("1") //$NON-NLS-1$
				|| initString.equals("HORIZONTAL_SPLIT") //$NON-NLS-1$
				|| initString.equals("javax.swing.JSplitPane.HORIZONTAL_SPLIT")) //$NON-NLS-1$
			orientation = HORIZONTAL_SPLIT;
		// Handle the case where the orientation is now vertical
		if (orientation == VERTICAL_SPLIT) {
			if (splitpane.eIsSet(sfLeftComponent)) {
				EObject leftComp = (EObject) ((EObject) splitpane).eGet(sfLeftComponent);
				cb.cancelAttributeSetting(splitpane, sfLeftComponent);
				afterCB.applyAttributeSetting((EObject) splitpane, sfTopComponent, leftComp);
			}
			if (splitpane.eIsSet(sfRightComponent)) {
				EObject rightComp = (EObject) ((EObject) splitpane).eGet(sfRightComponent);
				cb.cancelAttributeSetting(splitpane, sfRightComponent);
				afterCB.applyAttributeSetting((EObject) splitpane, sfBottomComponent, rightComp);
			}
			// Handle the case where the orientation is now horizontal
		} else if (orientation == HORIZONTAL_SPLIT) {
			if (splitpane.eIsSet(sfTopComponent)) {
				EObject topComp = (EObject) ((EObject) splitpane).eGet(sfTopComponent);
				cb.cancelAttributeSetting(splitpane, sfTopComponent);
				afterCB.applyAttributeSetting((EObject) splitpane, sfLeftComponent, topComp);
			}
			if (splitpane.eIsSet(sfBottomComponent)) {
				EObject bottomComp = (EObject) ((EObject) splitpane).eGet(sfBottomComponent);
				cb.cancelAttributeSetting(splitpane, sfBottomComponent);
				afterCB.applyAttributeSetting((EObject) splitpane, sfRightComponent, bottomComp);
			}
		}
		// apply the orientation value to the splitpane, but this is under rule control because there is an old and new value to handle.
		cb.setApplyRules(true);
		cb.applyAttributeSetting(splitpane, (EStructuralFeature) getTarget(), setValue);
		cb.append(afterCB.getCommand());
		return cb.getCommand();
	}

	/*
	 * Reset the 'orientation' property. If any of the component are set with top or bottom,
	 * cancel the settings and set them with respective 'left' and 'right' settings.
	 */
	public Command resetValue(IPropertySource source) {
		IJavaObjectInstance splitpane = (IJavaObjectInstance) source.getEditableValue();
		IBeanProxyHost h = BeanProxyUtilities.getBeanProxyHost(splitpane);
		RuledCommandBuilder cb = new RuledCommandBuilder(h.getBeanProxyDomain().getEditDomain());
		
		// First unset those that need to be unset, not under rule since they are not going away.
		// Accumulate the set back to new setting in a separate command builder so that they can be
		// applied AFTER the orientation is canceled.
		cb.setApplyRules(false);
		CommandBuilder afterCB = new CommandBuilder();
				
		EStructuralFeature sfLeftComponent =
			JavaInstantiation.getSFeature(splitpane, JFCConstants.SF_JSPLITPANE_LEFTCOMPONENT),
			sfRightComponent = JavaInstantiation.getSFeature(splitpane, JFCConstants.SF_JSPLITPANE_RIGHTCOMPONENT),
			sfTopComponent = JavaInstantiation.getSFeature(splitpane, JFCConstants.SF_JSPLITPANE_TOPCOMPONENT),
			sfBottomComponent = JavaInstantiation.getSFeature(splitpane, JFCConstants.SF_JSPLITPANE_BOTTOMCOMPONENT);
		if (splitpane.eIsSet(sfTopComponent)) {
			EObject topComp = (EObject) ((EObject) splitpane).eGet(sfTopComponent);
			cb.cancelAttributeSetting(splitpane, sfTopComponent);
			afterCB.applyAttributeSetting((EObject) splitpane, sfLeftComponent, topComp);
		}
		if (splitpane.eIsSet(sfBottomComponent)) {
			EObject bottomComp = (EObject) ((EObject) splitpane).eGet(sfBottomComponent);
			cb.cancelAttributeSetting(splitpane, sfBottomComponent);
			afterCB.applyAttributeSetting((EObject) splitpane, sfRightComponent, bottomComp);
		}

		// Cancel the orientation under rule control.
		cb.setApplyRules(true);
		cb.cancelAttributeSetting(splitpane, (EStructuralFeature) getTarget());
		cb.append(afterCB.getCommand());
		return cb.getCommand();
	}

}