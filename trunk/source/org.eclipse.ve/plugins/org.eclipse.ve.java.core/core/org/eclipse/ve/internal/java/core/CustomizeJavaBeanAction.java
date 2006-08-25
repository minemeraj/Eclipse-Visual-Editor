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
package org.eclipse.ve.internal.java.core;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.jem.internal.instantiation.InstantiationFactory;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.commands.CancelAttributeSettingCommand;
import org.eclipse.ve.internal.cde.commands.CommandBuilder;
import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.common.Common;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;

import org.eclipse.ve.internal.propertysheet.IEToolsPropertyDescriptor;
import org.eclipse.ve.internal.propertysheet.INeedData;

public class CustomizeJavaBeanAction extends CustomizeAction {
	// dbk cache IMG_CUSTOMIZE_BEAN image descriptors
	public static final ImageDescriptor IMG_CUSTOMIZE_BEAN_DISABLED = CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/dlcl16/customizebean_co.gif"); //$NON-NLS-1$
	public static final ImageDescriptor IMG_CUSTOMIZE_BEAN = CDEPlugin.getImageDescriptorFromPlugin(JavaVEPlugin.getPlugin(), "icons/full/elcl16/customizebean_co.gif"); //$NON-NLS-1$

	protected EditDomain fEditDomain;
		
	static final int NONE = 0;
	static final int NEWVALUE = 2;
	static final int CHANGED = 3;
	static final int CANCELED = 4;
	public static final String ACTION_ID = "jcm.javabean.CUSTOMIZE"; //$NON-NLS-1$

	public CustomizeJavaBeanAction(IWorkbenchPart anEditorPart, EditDomain anEditDomain) {
		super(anEditorPart);
		fEditDomain = anEditDomain;
		setId(ACTION_ID);
		setText(JavaMessages.Action_CustomizeJavaBean_Text); 
		setToolTipText(JavaMessages.Action_CustomizeJavaBean_ToolTipText); 
		setImageDescriptor(IMG_CUSTOMIZE_BEAN);
		setHoverImageDescriptor(getImageDescriptor());
		setDisabledImageDescriptor(IMG_CUSTOMIZE_BEAN_DISABLED);
	}
	public void run() {
		try {
			EditPart editPart = (EditPart) getSelectedObjects().get(0);
			ProxyFactoryRegistry registry = BeanProxyUtilities.getProxyFactoryRegistry(editPart);
			IBeanTypeProxy customizerTypeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy(getCustomizerClassName());

			final IJavaObjectInstance bean = (IJavaObjectInstance) editPart.getModel();
			IBeanProxy customizerProxy = customizerTypeProxy.newInstance();
			final IBeanProxyHost beanProxyHost = BeanProxyUtilities.getBeanProxyHost(bean);
			IBeanProxy beanProxy = beanProxyHost.getBeanProxy();

			// (1) Add the bean to the customizer
			// (2) Store the initial property values, for undo.
			// (3) Launch the remote WindowLauncher
			//     The window launcher on the remote VM will collect a list of what properties have changed
			// (4) Query the current properties against the stored ones to see what changed
			//	 and use the property editors for these to generate initialization strings and mof settings
			//     so we can code generate from them
			// (1)
			IMethodProxy setObjectMethod = customizerProxy.getTypeProxy().getMethodProxy("setObject", new String[] { "java.lang.Object" }); //$NON-NLS-1$ //$NON-NLS-2$
			setObjectMethod.invoke(customizerProxy, new IBeanProxy[] { beanProxy });
			// (2)
			// The only properties we are about are those that have get and set method pairs
			// Get the existing beanProxy object and put it in a map keyed by property descriptor.
			
			IPropertyDescriptor[] propertyDescriptors = ((IPropertySource) editPart.getAdapter(IPropertySource.class)).getPropertyDescriptors();
			final Map oldValues = new HashMap();
			final Map namesToPropDesc = new HashMap();
			for (int i=0; i<propertyDescriptors.length; i++) {
				IPropertyDescriptor propDescriptor = propertyDescriptors[i];
				Object id = propDescriptor.getId();
				if (id instanceof EStructuralFeature) {
					EStructuralFeature sf = (EStructuralFeature) id;
					boolean isetool = propDescriptor instanceof IEToolsPropertyDescriptor;
					// if is etool, the isReadonly takes precedence. Otherwise use the features isChangable.
					if ((isetool && (!((IEToolsPropertyDescriptor) propDescriptor).isReadOnly())) || 
						(!isetool && sf.isChangeable())) {
						// Query the bean object
						IBeanProxy existingValue = beanProxyHost.getBeanPropertyProxyValue(sf);
						oldValues.put(propDescriptor, existingValue);
						namesToPropDesc.put(sf.getName(), propDescriptor);
					}
				}
			}
			// (3)
			IBeanTypeProxy windowLauncherType = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.java.remotevm.WindowLauncher"); //$NON-NLS-1$
			// See whether or not buttons should be added to the customizer depending on how the author of it wants the VE to handle 
			// things like when to apply values.  If the property change is explicit by the customizer no OK and Cancel buttons are added
			// and the default constructor is used, otherwise one that takes a boolean to determine whether to add buttons or not is used
			IBeanProxy windowLauncherProxy = null;
			if (!explicitPropertyChange) {
				IConstructorProxy ctor = windowLauncherType.getConstructorProxy(new String[] { "java.awt.Component" }); //$NON-NLS-1$
				windowLauncherProxy = ctor.newInstance(new IBeanProxy[] { customizerProxy });								
			} else {
				IConstructorProxy ctor = windowLauncherType.getConstructorProxy(new String[] { "java.awt.Component" , "boolean" }); //$NON-NLS-1$ //$NON-NLS-2$
				IBeanProxy explicitPropertyChangeProxy = customizerProxy.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(explicitPropertyChange);
				windowLauncherProxy = ctor.newInstance(new IBeanProxy[] { customizerProxy , explicitPropertyChangeProxy });				
			}

			final Display display = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getDisplay();
			WindowLauncher launcher =
				new WindowLauncher(windowLauncherProxy, PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
			launcher.addListener(new WindowLauncher.Listener() {
				public void propertyChange(final String eventName) {
					// Revalidate so the UI refreshes as the target VM bean has changed
					display.asyncExec(new Runnable() {
						public void run() {
							beanProxyHost.revalidateBeanProxy();
							if(eventName != null && explicitPropertyChange){
								// If explicit property change is being done by the customize then apply the change immediately
								IPropertyDescriptor propDesc = (IPropertyDescriptor) namesToPropDesc.get(eventName);
								if(propDesc != null){
									// It is one of interest.
									IBeanProxy oldValue = (IBeanProxy) oldValues.get(propDesc);
									IBeanProxy currentValue = beanProxyHost.getBeanPropertyProxyValue((EStructuralFeature) propDesc.getId());	
									RuledCommandBuilder cmdBuilder = new RuledCommandBuilder(fEditDomain); 
									recordProperyChange(
										currentValue,
										oldValue,
										bean,
										propDesc,
										cmdBuilder);
									fEditDomain.getCommandStack().execute(cmdBuilder.getCommand());
								} else {
									JavaVEPlugin.getPlugin().getLogger().log(MessageFormat.format(
											JavaMessages.CustomizePropertyNotFound_ERROR,new Object[]{eventName,bean.getJavaType().getName()}));																		
								}
							}							
						}
					});
				}
			});

			launcher.waitUntilWindowCloses();
			if(!explicitPropertyChange){
				switch (launcher.getWindowState()) {
					case Common.WIN_CLOSED :
					case Common.DLG_CANCEL :
						// 	Cancel the change.
						customizerCancel(oldValues, beanProxyHost, bean);
						;
						break;
					case Common.DLG_OK :
						// 	Make the change commands.
						customizerOK(oldValues, beanProxyHost, bean);
						break;
				};
			}
			launcher.dispose(); // Deregister the callback and ensure cleanup occurs
			windowLauncherProxy = null;
			launcher = null;				
		} catch (Exception exc) {
			JavaVEPlugin.log(exc, Level.WARNING);
		} 
	}
	protected void customizerCancel(Map oldValues, IBeanProxyHost aBeanProxyHost, IJavaObjectInstance aBean) {
		// When the customizer is cancelled the model will be fine, the only problem is that the customizer may have changed the target VM Java bean
		// We therefore need to iterate over all the values and see which are different
		Iterator entries = oldValues.entrySet().iterator();

		while (entries.hasNext()) {
			Map.Entry entry = (Entry) entries.next();
			IPropertyDescriptor pd = (IPropertyDescriptor) entry.getKey();
			IBeanProxy oldValue = (IBeanProxy) entry.getValue();
			IInternalBeanProxyHost internalHost = (IInternalBeanProxyHost) aBeanProxyHost;
			EStructuralFeature sf = (EStructuralFeature) pd.getId();
			IBeanProxy currentValue = aBeanProxyHost.getBeanPropertyProxyValue(sf);
			int changeType = NONE;
			if (oldValue == currentValue) {
				changeType = NONE;
			} else if (oldValue == null || !oldValue.equals(currentValue)) {
				changeType = CHANGED;
			}
			// If the value has been changed or is a new value then apply the old value directly to the bean.  Commands are not used
			// ( which they are for OK ) because we are just restoring the target VM to the model's state to undo the changes the customizer made
			// We could just apply all of the old values from the oldValues but it is cleaner code to just apply the ones
			// that we detect have changed because their .equals doesn't match
			switch (changeType) {
				case CHANGED :
					internalHost.applyBeanPropertyProxyValue(sf, oldValue);
			}
		}

		// Having directly called set methods on the target VM to reset the old values for the ones that changed
		// we now need to revalidate the bean so it can refresh its visual appearance ( if applicable )
		aBeanProxyHost.revalidateBeanProxy();

	}
	protected void customizerOK(Map oldValues, IBeanProxyHost aBeanProxyHost, IJavaObjectInstance aBean) {
		// The bean has been modified by the customize - apply the changes
		// There are two ways that changed properties are determined:
		// a)   The default is that the set of properties when the customizer was launched are compared against 
		//      the current values
		// b)   The propertyChangeEvents fired by the customizer are used.		
		CommandBuilder commandBuilder = new RuledCommandBuilder(fEditDomain);
		
		Iterator oldValuesItr = oldValues.entrySet().iterator();
		// We need a shell for the cell editors we will create to get the init strings.	
		Map originalSettings = ((IInternalBeanProxyHost) aBeanProxyHost).getOriginalSettingsTable();

		while (oldValuesItr.hasNext()) {
			Map.Entry oldValueEntry = (Entry) oldValuesItr.next();
			IPropertyDescriptor pd = (IPropertyDescriptor) oldValueEntry.getKey();
			EStructuralFeature sf = (EStructuralFeature) pd.getId();
			IBeanProxy oldValue = (IBeanProxy) oldValueEntry.getValue();
			IBeanProxy currentValue = aBeanProxyHost.getBeanPropertyProxyValue(sf);
			int changeType = NONE;
			// 	If the customizer signalled a change in the property then we should 
			if (oldValue == currentValue) {
				changeType = NONE;
			} else if (oldValue == null || !oldValue.equals(currentValue)) {
				if (aBean.eIsSet(sf) && originalSettings.containsKey(sf)) {
					// 	It is set in EMF and we know what the original setting value was.
					IBeanProxy origValue = (IBeanProxy) originalSettings.get(sf);
				if (origValue == currentValue)
					changeType = CANCELED;
				else if (origValue == null || !origValue.equals(currentValue))
					changeType = CHANGED;
				else
					changeType = CANCELED;
			} else {
				changeType = CHANGED;
				// Not currently set in EMF, or it is set, but we don't have an orig setting to verify against, so it is changed.
			}
		}
			switch (changeType) {
			case CHANGED :
				// (1) Create a new java bean that wrappers either the new or original beanProxy based on the isNew argument
				// (2) Get the javaInitializationString for this beanProxy that involves going through the 
				//     cellEditor that is created in a non-visual manner
				// (3) create a command to apply this - This will actaully be picked up by the BeanProxyAdapter 
				//     and re-applied to the live bean but so be it
				recordProperyChange(
						currentValue,
						oldValue,
						aBean,
						pd,
						commandBuilder);
				break;
			case CANCELED :
				CancelAttributeSettingCommand cancelCmd = new CancelAttributeSettingCommand();
				cancelCmd.setTarget(aBean);
				cancelCmd.setAttribute(sf);
				commandBuilder .append(cancelCmd);
			}
		}
		// Push the command on the stack to be executed
		fEditDomain.getCommandStack().execute(commandBuilder.getCommand());
	}
		
	protected void recordProperyChange(IBeanProxy currentValue, IBeanProxy oldValue, IJavaObjectInstance aBean, IPropertyDescriptor propDesc, CommandBuilder commandBuilder){
		IJavaInstance newBean =
			BeanProxyUtilities.wrapperBeanProxy(currentValue, aBean.eResource().getResourceSet(), null, true);
		CellEditor cellEditor = propDesc.createPropertyEditor(getWorkbenchPart().getSite().getShell());
		if (cellEditor instanceof INeedData) {
			((INeedData) cellEditor).setData(fEditDomain);
			// We must call doSetValue(...) with the new bean
			cellEditor.setValue(newBean);
			if (cellEditor instanceof IJavaCellEditor2) {
				newBean.setAllocation(((IJavaCellEditor2) cellEditor).getJavaAllocation());
			} else if(cellEditor instanceof IJavaCellEditor){
				// Can't convert init strings because they may contain static field accesses, and these cannot be succesfully converted.
				String initString = ((IJavaCellEditor) cellEditor).getJavaInitializationString();
				newBean.setAllocation(InstantiationFactory.eINSTANCE.createInitStringAllocation(initString));
			} else {
				newBean.setAllocation(InstantiationFactory.eINSTANCE.createInitStringAllocation("UNABLE TO DETERMINE INITIALIZATION STRING")); //$NON-NLS-1$
			}
		}
		// Now we have a newBean to set
		// If the oldBean exists and there is an explicit setting
		// then undo is straightforward - it just does an undo of the old value
		// however if there is no oldBean refValue then we need to tell the command
		// the oldBeanProxy so it can apply this when it does an undo.
		// This is all done in the ApplyCustomizedValueCommand
		
		commandBuilder.applyAttributeSetting(
				aBean,
				(EStructuralFeature) propDesc.getId(),
				newBean);
	}
}
