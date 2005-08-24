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
/*
 *  $RCSfile: CustomizeXMLPersistenceAction.java,v $
 *  $Revision: 1.8 $  $Date: 2005-08-24 23:30:46 $ 
 */

import org.eclipse.ui.IEditorPart;

public class CustomizeXMLPersistenceAction extends CustomizeAction {

	public static final String ACTION_ID = "jcm.CUSTOMIZE"; //$NON-NLS-1$

public CustomizeXMLPersistenceAction(IEditorPart anEditorPart){
	super(anEditorPart);
	setId(ACTION_ID);
	setText(JavaMessages.Action_CustomizeXMLPersistence_Text); 
}
public void run(){
	// TODO - commented entire method... need to fix compile errors
//	try{
//		EditPart editPart = (EditPart) getSelectedObjects().get(0);
//		ProxyFactoryRegistry registry = BeanProxyUtilities.getProxyFactoryRegistry(editPart);
//		IBeanTypeProxy customizerTypeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy(getCustomizerClassName());
//		
//		IBeanProxy customizerProxy = customizerTypeProxy.newInstance();
//		final IBeanProxyHost beanProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance)editPart.getModel());
//		IBeanProxy beanProxy = beanProxyHost.getBeanProxy();
//		
//		// (1) Add the bean to the customizer
//		// (2) Store the initial xml string, for undo.
//		// (3) Launch the remote WindowLauncher
//		// (1)
//		IMethodProxy setObjectMethod = customizerProxy.getTypeProxy().getMethodProxy("setObject", new String[]{"java.lang.Object"}); //$NON-NLS-1$ //$NON-NLS-2$
//		setObjectMethod.invoke(customizerProxy, new IBeanProxy[]{beanProxy});
//		// (2)
//		IBeanTypeProxy helperTypeProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.java.remotevm.XMLHelper"); //$NON-NLS-1$
//		IMethodProxy launcherMethodProxy = helperTypeProxy.getMethodProxy("getEncodedString", new String[] {"java.lang.Object"}); //$NON-NLS-1$ //$NON-NLS-2$
//		// (3)
//		IBeanTypeProxy windowLauncherType = registry.getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.java.remotevm.WindowLauncher"); //$NON-NLS-1$
//		IConstructorProxy ctor = windowLauncherType.getConstructorProxy(new String[] {"java.awt.Component"}); //$NON-NLS-1$
//		IBeanProxy windowLauncherProxy = ctor.newInstance(new IBeanProxy[] {customizerProxy});
//		//
//				
//		IStringBeanProxy xmlOfCustomized = null;
//				
//		WindowLauncher launcher = new WindowLauncher(windowLauncherProxy, org.eclipse.swt.widgets.Display.getDefault().getActiveShell());
//		launcher.addListener(new WindowLauncher.Listener(){
//			public void propertyChange(String eventName){
//				// To revalidate the bean being customized we need to get its BeanProxyAdapter
//				// and revalidate this - this will cause a repaint on the VCE viewer if required
//				beanProxyHost.revalidateBeanProxy();
//			}
//		});
//		
//		
//		launcher.waitUntilWindowCloses();
//		switch( launcher.getWindowState() ){
//			case Common.WIN_CLOSED:
//			case Common.DLG_CANCEL:
//				// Cancel the change.
//				xmlOfCustomized = null;
//				break;
//			case Common.DLG_OK:
//				// Make the change commands.
//				launcherMethodProxy = helperTypeProxy.getMethodProxy("getEncodedString", new String[] {"java.lang.Object"}); //$NON-NLS-1$ //$NON-NLS-2$
//				xmlOfCustomized = (IStringBeanProxy) launcherMethodProxy.invoke(helperTypeProxy, new IBeanProxy[]{beanProxy});
//				break;
//			default:
//				xmlOfCustomized = null;
//				break;
//		};
//		launcher.dispose();
//		windowLauncherProxy = null;
//		launcher = null;
//		
//		if(xmlOfCustomized==null)
//			return;
//		
//		IJavaObjectInstance objectModified = (IJavaObjectInstance)editPart.getModel();
//		CompoundCommand finalCommand = new CompoundCommand();
//		if(objectModified.isSetInitializationString()){
//			ModifyAttributesCommand removeInitializationStringCommand = new ModifyAttributesCommand(""); //$NON-NLS-1$
//			removeInitializationStringCommand.setFeature(JavaInstantiation.getInitializationStringFeature(objectModified));
//			removeInitializationStringCommand.setOperation(ModifyAttributesCommand.FEATURE_REMOVE);
//			removeInitializationStringCommand.setTarget(objectModified);
//			finalCommand.append(removeInitializationStringCommand);
//		}
//		if(objectModified.isSetInstantiateUsing()){
//			ModifyAttributesCommand removeInstantiationStringCommand = new ModifyAttributesCommand(""); //$NON-NLS-1$
//			removeInstantiationStringCommand.setFeature(JavaInstantiation.getInstantiateUsingFeature(objectModified));
//			removeInstantiationStringCommand.setOperation(ModifyAttributesCommand.FEATURE_REMOVE);
//			removeInstantiationStringCommand.setTarget(objectModified);
//			finalCommand.append(removeInstantiationStringCommand);
//		}
//		finalCommand.unwrap();
//
//		ModifyAttributesCommand applySerializeCommand = new ModifyAttributesCommand(""); //$NON-NLS-1$
//		applySerializeCommand.setData(xmlOfCustomized.stringValue());
//		applySerializeCommand.setFeature(JavaInstantiation.getSerializeDataFeature(objectModified));
//		applySerializeCommand.setTarget(objectModified);
//		if(objectModified.isSetSerializeData()) 	// Modify Attribute
//			applySerializeCommand.setOperation(ModifyAttributesCommand.FEATURE_MODIFY);
//		else							// Add attribute;
//			applySerializeCommand.setOperation(ModifyAttributesCommand.FEATURE_ADD);
//		
//		if(finalCommand.isEmpty()){
//			execute(applySerializeCommand);
//		}else{
//			finalCommand.append(applySerializeCommand);
//			execute(finalCommand);
//		}
//	}catch(Exception e){JavaVEPlugin.log(e, Level.WARNING);}
//		
}


}
