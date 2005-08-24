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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JSplitPaneOrientationPropertyDescriptor.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:10 $ 
 */

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cde.commands.CommandBuilder;
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
public class JSplitPaneOrientationPropertyDescriptor extends BeanPropertyDescriptorAdapter implements ICommandPropertyDescriptor {

	private static final int VERTICAL_SPLIT = 0, HORIZONTAL_SPLIT = 1;

	public Command setValue(IPropertySource source, Object setValue) {
		IJavaObjectInstance splitpane = (IJavaObjectInstance) source.getEditableValue();
		IBeanProxyHost h = BeanProxyUtilities.getBeanProxyHost(splitpane);
		RuledCommandBuilder cb = new RuledCommandBuilder(h.getBeanProxyDomain().getEditDomain());

		EStructuralFeature sfLeftComponent = JavaInstantiation.getSFeature(splitpane, JFCConstants.SF_JSPLITPANE_LEFTCOMPONENT),
			sfRightComponent = JavaInstantiation.getSFeature(splitpane, JFCConstants.SF_JSPLITPANE_RIGHTCOMPONENT),
			sfTopComponent = JavaInstantiation.getSFeature(splitpane, JFCConstants.SF_JSPLITPANE_TOPCOMPONENT),
			sfBottomComponent = JavaInstantiation.getSFeature(splitpane, JFCConstants.SF_JSPLITPANE_BOTTOMCOMPONENT);
		IJavaInstance value = (IJavaInstance) setValue;

		// First unset those that need to be unset, not under rule since they are not going away.
		// Accumulate the set back to new setting in a separate command builder so that they can be
		// applied AFTER the orientation is changed.
		cb.setApplyRules(false);
		CommandBuilder afterCB = new CommandBuilder();

		IBeanProxyHost orientH = BeanProxyUtilities.getBeanProxyHost(value, splitpane.eResource().getResourceSet());
		orientH.instantiateBeanProxy(); // Because if a property setting it may not be instantiated yet.

		int orientation = HORIZONTAL_SPLIT;
		if (!orientH
			.getBeanProxy()
			.equals(BeanAwtUtilities.getJSplitPaneOrientationHorizontal(h.getBeanProxyDomain().getProxyFactoryRegistry())))
			orientation = VERTICAL_SPLIT;

		// Handle the case where the orientation is now vertical
		if (orientation == VERTICAL_SPLIT) {
			if (splitpane.eIsSet(sfLeftComponent)) {
				EObject leftComp = (EObject) ((EObject) splitpane).eGet(sfLeftComponent);
				cb.cancelAttributeSetting(splitpane, sfLeftComponent);
				afterCB.applyAttributeSetting(splitpane, sfTopComponent, leftComp);
			}
			if (splitpane.eIsSet(sfRightComponent)) {
				EObject rightComp = (EObject) ((EObject) splitpane).eGet(sfRightComponent);
				cb.cancelAttributeSetting(splitpane, sfRightComponent);
				afterCB.applyAttributeSetting(splitpane, sfBottomComponent, rightComp);
			}
			// Handle the case where the orientation is now horizontal
		} else if (orientation == HORIZONTAL_SPLIT) {
			if (splitpane.eIsSet(sfTopComponent)) {
				EObject topComp = (EObject) ((EObject) splitpane).eGet(sfTopComponent);
				cb.cancelAttributeSetting(splitpane, sfTopComponent);
				afterCB.applyAttributeSetting(splitpane, sfLeftComponent, topComp);
			}
			if (splitpane.eIsSet(sfBottomComponent)) {
				EObject bottomComp = (EObject) ((EObject) splitpane).eGet(sfBottomComponent);
				cb.cancelAttributeSetting(splitpane, sfBottomComponent);
				afterCB.applyAttributeSetting(splitpane, sfRightComponent, bottomComp);
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

		EStructuralFeature sfLeftComponent = JavaInstantiation.getSFeature(splitpane, JFCConstants.SF_JSPLITPANE_LEFTCOMPONENT),
			sfRightComponent = JavaInstantiation.getSFeature(splitpane, JFCConstants.SF_JSPLITPANE_RIGHTCOMPONENT),
			sfTopComponent = JavaInstantiation.getSFeature(splitpane, JFCConstants.SF_JSPLITPANE_TOPCOMPONENT),
			sfBottomComponent = JavaInstantiation.getSFeature(splitpane, JFCConstants.SF_JSPLITPANE_BOTTOMCOMPONENT);
		if (splitpane.eIsSet(sfTopComponent)) {
			EObject topComp = (EObject) ((EObject) splitpane).eGet(sfTopComponent);
			cb.cancelAttributeSetting(splitpane, sfTopComponent);
			afterCB.applyAttributeSetting(splitpane, sfLeftComponent, topComp);
		}
		if (splitpane.eIsSet(sfBottomComponent)) {
			EObject bottomComp = (EObject) ((EObject) splitpane).eGet(sfBottomComponent);
			cb.cancelAttributeSetting(splitpane, sfBottomComponent);
			afterCB.applyAttributeSetting(splitpane, sfRightComponent, bottomComp);
		}

		// Cancel the orientation under rule control.
		cb.setApplyRules(true);
		cb.cancelAttributeSetting(splitpane, (EStructuralFeature) getTarget());
		cb.append(afterCB.getCommand());
		return cb.getCommand();
	}

}
