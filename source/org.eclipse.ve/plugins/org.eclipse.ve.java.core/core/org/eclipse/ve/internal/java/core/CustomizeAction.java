package org.eclipse.ve.internal.java.core;
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
 *  $RCSfile: CustomizeAction.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.jem.internal.beaninfo.BeanDecorator;
import org.eclipse.jem.internal.beaninfo.adapters.Utilities;
import org.eclipse.jem.internal.core.*;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.common.Common;
import org.eclipse.ve.internal.propertysheet.common.commands.CompoundCommand;

import org.eclipse.jem.internal.java.JavaClass;

public class CustomizeAction extends SelectionAction {
	public static final String ACTION_ID = "jcm.CUSTOMIZE"; //$NON-NLS-1$

	public CustomizeAction(IWorkbenchPart anEditorPart) {
		super(anEditorPart);
		setId(ACTION_ID);
		setText(JavaMessages.getString("Action.Customize.Text")); //$NON-NLS-1$
	}
	/**
	 * @see EditorPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled() {
		List editParts = getSelectedObjects();
		JavaClass customizerClass = getCustomizerClass(editParts);
		return customizerClass != null;
	}
	/**
	 * Return the customizer class for the selected object.  Null if there is not a 
	 * customizer or more than one object is selected
	 */
	protected JavaClass getCustomizerClass(List editParts) {

		if (editParts.size() != 1)
			return null; // Only customizer a single bean at a time
		if (!(editParts.get(0) instanceof EditPart))
			return null; // Only work with an EditPart
		Object model = ((EditPart) editParts.get(0)).getModel();
		if (model instanceof IJavaObjectInstance) {
			// TODO This is quick hack to not use thisPart for customization. Need to get a better way of doing this."); //$NON-NLS-1$
			IBeanProxyHost ba = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) model);
			if (ba instanceof BeanProxyAdapter && ((BeanProxyAdapter) ba).isThisPart())
				return null;
			JavaClass beanClass = (JavaClass) ((EObject) model).eClass();
			BeanDecorator beanDecor = Utilities.getBeanDecorator(beanClass);
			return beanDecor != null ? beanDecor.getCustomizerClass() : null;	// If class invalid, bean decor will be null.
		}
		return null;
	}
	protected String getCustomizerClassName() {
		JavaClass customizerClass = getCustomizerClass(getSelectedObjects());
		if (customizerClass != null) {
			return customizerClass.getQualifiedNameForReflection();
		}
		return null;
	}
	
	public void run() {
		try {
			EditPart editPart = (EditPart) getSelectedObjects().get(0);
			ProxyFactoryRegistry registry = BeanProxyUtilities.getProxyFactoryRegistry(editPart);
			IBeanTypeProxy customizerTypeProxy =
				registry.getBeanTypeProxyFactory().getBeanTypeProxy(getCustomizerClassName());

			IBeanProxy customizerProxy = customizerTypeProxy.newInstance();
			IBeanProxy beanProxy = BeanProxyUtilities.getBeanProxy((IJavaObjectInstance) editPart.getModel());

			// (1) Add the bean to the customizer
			// (2) Store the initial xml string, for undo.
			// (3) Launch the remote WindowLauncher
			// (1)
			IMethodProxy setObjectMethod =
				customizerProxy.getTypeProxy().getMethodProxy("setObject", new String[] { "java.lang.Object" }); //$NON-NLS-1$ //$NON-NLS-2$
			setObjectMethod.invoke(customizerProxy, new IBeanProxy[] { beanProxy });
			// (2)
			IBeanTypeProxy helperTypeProxy =
				registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.java.remotevm.XMLHelper"); //$NON-NLS-1$
			IMethodProxy launcherMethodProxy =
				helperTypeProxy.getMethodProxy("getEncodedString", new String[] { "java.lang.Object" }); //$NON-NLS-1$ //$NON-NLS-2$
			// (3)
			IBeanTypeProxy windowLauncherType =
				registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.java.remotevm.WindowLauncher"); //$NON-NLS-1$
			IConstructorProxy ctor =
				windowLauncherType.getConstructorProxy(new String[] { "java.awt.Component" }); //$NON-NLS-1$
			IBeanProxy windowLauncherProxy = ctor.newInstance(new IBeanProxy[] { customizerProxy });
			//

			IStringBeanProxy xmlOfCustomized = null;

			WindowLauncher launcher =
				new WindowLauncher(
					windowLauncherProxy,
					org.eclipse.swt.widgets.Display.getDefault().getActiveShell());
			launcher.waitUntilWindowCloses();
			switch (launcher.getWindowState()) {
				case Common.WIN_CLOSED :
				case Common.DLG_CANCEL :
					// Cancel the change.
					xmlOfCustomized = null;
					break;
				case Common.DLG_OK :
					// Make the change commands.
					launcherMethodProxy =
						helperTypeProxy.getMethodProxy("getEncodedString", new String[] { "java.lang.Object" }); //$NON-NLS-1$ //$NON-NLS-2$
					xmlOfCustomized =
						(IStringBeanProxy) launcherMethodProxy.invoke(helperTypeProxy, new IBeanProxy[] { beanProxy });
					break;
				default :
					xmlOfCustomized = null;
					break;
			};
			windowLauncherProxy = null;
			launcher = null;

			if (xmlOfCustomized == null)
				return;

			IJavaObjectInstance objectModified = (IJavaObjectInstance) editPart.getModel();
			CompoundCommand finalCommand =
				new CompoundCommand();
			if (objectModified.isSetInitializationString()) {
				ModifyAttributesCommand removeInitializationStringCommand =
					new ModifyAttributesCommand(""); //$NON-NLS-1$
				removeInitializationStringCommand.setFeature(
					JavaInstantiation.getInitializationStringFeature(objectModified));
				removeInitializationStringCommand.setOperation(ModifyAttributesCommand.FEATURE_REMOVE);
				removeInitializationStringCommand.setTarget(objectModified);
				finalCommand.append(removeInitializationStringCommand);
			}
			if (objectModified.isSetInstantiateUsing()) {
				ModifyAttributesCommand removeInstantiationStringCommand =
					new ModifyAttributesCommand(""); //$NON-NLS-1$
				removeInstantiationStringCommand.setFeature(
					JavaInstantiation.getInstantiateUsingFeature(objectModified));
				removeInstantiationStringCommand.setOperation(ModifyAttributesCommand.FEATURE_REMOVE);
				removeInstantiationStringCommand.setTarget(objectModified);
				finalCommand.append(removeInstantiationStringCommand);
			}
			finalCommand.unwrap();

			ModifyAttributesCommand applySerializeCommand =
				new ModifyAttributesCommand(""); //$NON-NLS-1$
			applySerializeCommand.setData(xmlOfCustomized.stringValue());
			applySerializeCommand.setFeature(JavaInstantiation.getSerializeDataFeature(objectModified));
			applySerializeCommand.setTarget(objectModified);
			if (objectModified.isSetSerializeData()) // Modify Attribute
				applySerializeCommand.setOperation(ModifyAttributesCommand.FEATURE_MODIFY);
			else // Add attribute;
				applySerializeCommand.setOperation(ModifyAttributesCommand.FEATURE_ADD);

			if (finalCommand.isEmpty()) {
				execute(applySerializeCommand);
			} else {
				finalCommand.append(applySerializeCommand);
				execute(finalCommand);
			}
		} catch (Exception e) {
			JavaVEPlugin.log(e, MsgLogger.LOG_WARNING);
		}

	}

}