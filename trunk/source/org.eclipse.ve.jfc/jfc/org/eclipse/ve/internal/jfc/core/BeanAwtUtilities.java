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
 *  $RCSfile: BeanAwtUtilities.java,v $
 *  $Revision: 1.2 $  $Date: 2004-01-02 20:49:10 $ 
 */

import java.util.List;

import org.eclipse.core.runtime.*;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.beaninfo.adapters.Utilities;
import org.eclipse.jem.internal.core.*;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.core.CDEPlugin;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;
import org.eclipse.ve.internal.java.vce.*;
import org.eclipse.ve.internal.java.visual.*;
import org.eclipse.ve.internal.jcm.BeanDecorator;
import org.eclipse.jem.internal.java.JavaClass;
import org.eclipse.ve.internal.java.core.*;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.awt.*;
/** 
 * Helper class with useful methods for working with awt bean proxies
 */
public class BeanAwtUtilities {
		
public static Point getOffScreenLocation(){
	
	boolean showWindow = VCEPreferences.isLiveWindowOn();
	if (showWindow)
		return new Point(0,0);
	else {
		// Get how big the display is and put it out beyond that.
		Display disp = Display.getCurrent();
		if (disp == null)
			disp = Display.getDefault();
		final Rectangle[] b = new Rectangle[1];
		final Display dsp = disp;
		disp.syncExec(new Runnable() {
			/**
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				b[0] = dsp.getBounds();
			}
		});
		return new Point(Math.max(10000, b[0].width*2), Math.max(10000, b[0].height*2));
	}
}

	// JCMMethod proxies are cached in a registry constants.
	public IMethodProxy
		managerAddComponentBeforeMethodProxy,
		managerAddComponentObjectBeforeMethodProxy,
		invalidateComponentMethodProxy,
		addComponentMethodProxy,
		addComponentIntMethodProxy,
		addComponentObjectMethodProxy,
		addComponentObjectIntMethodProxy,
		removeComponentMethodProxy,
		removeAllComponentsMethodProxy,
		disposeComponentMethodProxy,
		getLayoutMethodProxy,
		setSizeMethodProxy,
		setBoundsMethodProxy,
		getBoundsMethodProxy,
		getLocationMethodProxy,
		getSizeMethodProxy,
		getPreferredSizeMethodProxy,
		getParentMethodProxy,
		getComponentsMethodProxy,
		setComponentMethodProxy,
		setRelativeParentMethodProxy,
		getManagerLocationMethodProxy,
		managerInsertTabBeforeMethodProxy,
		addTabMethodProxy,
		getTabSelectedComponentMethodProxy,
		setTabSelectedComponentMethodProxy,
		managerSetTabIconAt,
		managerSetTabTitleAt,
		managerSetTabTooltipAt,
		menuManagerRemoveJMenuItemString,
		menuManagerRemoveJMenuItemAction,
		popupMenuManagerRevalidate,
		setJSplitPaneManagerMethodProxy,
		setJSplitPaneDividerLocationManagerProxy,
		resetToPreferredSizesJSplitPaneManagerProxy,
		toolbarManagerRemoveItemAction,
		windowPackProxy;
		
	public static final String REGISTRY_KEY = "org.eclipse.ve.internal.jfc.core.BeanAwtUtilities"; //$NON-NLS-1$
	
public static BeanAwtUtilities getConstants(ProxyFactoryRegistry registry) {
	BeanAwtUtilities constants = (BeanAwtUtilities) registry.getConstants(REGISTRY_KEY);
	if (constants == null)
		registry.registerConstants(REGISTRY_KEY, constants = new BeanAwtUtilities());	
	return constants;
}

protected static BeanAwtUtilities getConstants(IBeanProxy proxy) {
	return getConstants(proxy.getProxyFactoryRegistry());
}
		
public static void invoke_invalidate(IBeanProxy aBeanProxy){
	BeanAwtUtilities constants = getConstants(aBeanProxy);
	
	if (constants.invalidateComponentMethodProxy == null) {
		constants.invalidateComponentMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Component").getMethodProxy("invalidate"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	constants.invalidateComponentMethodProxy.invokeCatchThrowableExceptions(aBeanProxy);
}

/**
 * Add component before the specified component.
 * This uses the ContainerManager.
 */
public static void invoke_add_Component_before(IBeanProxy aContainerBeanProxy, IBeanProxy aComponentBeanProxy, IBeanProxy beforeComponentBeanProxy) throws ThrowableProxy {
	BeanAwtUtilities constants = getConstants(aContainerBeanProxy);
	
	if (constants.managerAddComponentBeforeMethodProxy == null) {
		constants.managerAddComponentBeforeMethodProxy = aContainerBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.ContainerManager").getMethodProxy( //$NON-NLS-1$
			"addComponentBefore", //$NON-NLS-1$
			new String[] {"java.awt.Container", "java.awt.Component" , "java.awt.Component"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		);
	}
	constants.managerAddComponentBeforeMethodProxy.invoke(null, new IBeanProxy[] {aContainerBeanProxy, aComponentBeanProxy, beforeComponentBeanProxy});
}

/**
 * Add component before the specified component with the given constraint
 * This uses the ContainerManager.
 */
public static void invoke_add_Component_Object_before(IBeanProxy aContainerBeanProxy, IBeanProxy aComponentBeanProxy, IBeanProxy constraint, IBeanProxy beforeComponentBeanProxy) throws ThrowableProxy {
	BeanAwtUtilities constants = getConstants(aContainerBeanProxy);
	
	if (constants.managerAddComponentObjectBeforeMethodProxy == null) {
		constants.managerAddComponentObjectBeforeMethodProxy = aContainerBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.ContainerManager").getMethodProxy( //$NON-NLS-1$
			"addComponentBefore", //$NON-NLS-1$
			new String[] {"java.awt.Container", "java.awt.Component" , "java.lang.Object", "java.awt.Component"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		);
	}
	constants.managerAddComponentObjectBeforeMethodProxy.invoke(null, new IBeanProxy[] {aContainerBeanProxy, aComponentBeanProxy, constraint, beforeComponentBeanProxy});
}

/**
 * Add component.
 */
public static void invoke_add_Component(IBeanProxy aContainerBeanProxy,IBeanProxy aComponentBeanProxy) throws ThrowableProxy {
	BeanAwtUtilities constants = getConstants(aContainerBeanProxy);
	
	if (constants.addComponentMethodProxy == null) {
		constants.addComponentMethodProxy = aContainerBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Container").getMethodProxy( //$NON-NLS-1$
			"add", //$NON-NLS-1$
			"java.awt.Component" //$NON-NLS-1$
		);
	}
	constants.addComponentMethodProxy.invoke(aContainerBeanProxy,aComponentBeanProxy);
}

/**
 * Add component at specified index.
 */
public static void invoke_add_Component_int(IBeanProxy aContainerBeanProxy,IBeanProxy aComponentBeanProxy,IBeanProxy aPositionBeanProxy){
	BeanAwtUtilities constants = getConstants(aContainerBeanProxy);
	
	if (constants.addComponentIntMethodProxy == null) {
		constants.addComponentIntMethodProxy = aContainerBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Container").getMethodProxy( //$NON-NLS-1$
			"add", //$NON-NLS-1$
			new String[] {"java.awt.Component" , "int"} //$NON-NLS-1$ //$NON-NLS-2$
		);
	}
	constants.addComponentIntMethodProxy.invokeCatchThrowableExceptions(aContainerBeanProxy, new IBeanProxy[] {aComponentBeanProxy, aPositionBeanProxy});
}

/**
 * Add component with constraint.
 */
public static void invoke_add_Component_Object(IBeanProxy aContainerBeanProxy,IBeanProxy aComponentBeanProxy,IBeanProxy aConstraintBeanProxy) throws ThrowableProxy {
	BeanAwtUtilities constants = getConstants(aContainerBeanProxy);
	
	if (constants.addComponentObjectMethodProxy == null) {
		constants.addComponentObjectMethodProxy = aContainerBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Container").getMethodProxy( //$NON-NLS-1$
			"add", //$NON-NLS-1$
			new String[] {"java.awt.Component","java.lang.Object"} //$NON-NLS-1$ //$NON-NLS-2$
		);
	}
	constants.addComponentObjectMethodProxy.invoke(aContainerBeanProxy, new IBeanProxy[] { aComponentBeanProxy , aConstraintBeanProxy });	
}

/**
 * Add component with constraint at specified index.
 */
public static void invoke_add_Component_Object_int(IBeanProxy aContainerBeanProxy,IBeanProxy aComponentBeanProxy,IBeanProxy aConstraintBeanProxy,IBeanProxy anIntBeanProxy){
	BeanAwtUtilities constants = getConstants(aContainerBeanProxy);
	
	if (constants.addComponentObjectIntMethodProxy == null) {
		constants.addComponentObjectIntMethodProxy = aContainerBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Container").getMethodProxy( //$NON-NLS-1$
			"add", //$NON-NLS-1$
			new String[] {"java.awt.Component","java.lang.Object","int"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		);
	}
	constants.addComponentObjectIntMethodProxy.invokeCatchThrowableExceptions(aContainerBeanProxy, new IBeanProxy[] { aComponentBeanProxy , aConstraintBeanProxy , anIntBeanProxy});		
}

public static void invoke_remove_Component(IBeanProxy aContainerBeanProxy,IBeanProxy aComponentBeanProxy){
	BeanAwtUtilities constants = getConstants(aContainerBeanProxy);
	
	if (constants.removeComponentMethodProxy == null) {
		constants.removeComponentMethodProxy = aContainerBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Container").getMethodProxy( //$NON-NLS-1$
			"remove", //$NON-NLS-1$
			"java.awt.Component" //$NON-NLS-1$
		);
	}
	constants.removeComponentMethodProxy.invokeCatchThrowableExceptions(aContainerBeanProxy,aComponentBeanProxy);
}

public static void invoke_removeAll_Components(IBeanProxy aContainerBeanProxy){
	BeanAwtUtilities constants = getConstants(aContainerBeanProxy);
	
	if (constants.removeAllComponentsMethodProxy == null) {
		constants.removeAllComponentsMethodProxy = aContainerBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Container").getMethodProxy( //$NON-NLS-1$
			"removeAll" //$NON-NLS-1$
		);
	}
	constants.removeAllComponentsMethodProxy.invokeCatchThrowableExceptions(aContainerBeanProxy);
}

public static void invoke_dispose(IBeanProxy aBeanProxy){
	BeanAwtUtilities constants = getConstants(aBeanProxy);
	
	if (constants.disposeComponentMethodProxy == null) {
		constants.disposeComponentMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Window").getMethodProxy("dispose"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	constants.disposeComponentMethodProxy.invokeCatchThrowableExceptions(aBeanProxy);
}

public static IBeanProxy invoke_getLayout(IBeanProxy aContainerBeanProxy){
	BeanAwtUtilities constants = getConstants(aContainerBeanProxy);
	
	if (constants.getLayoutMethodProxy == null) {
		constants.getLayoutMethodProxy = aContainerBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Container").getMethodProxy("getLayout"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	return constants.getLayoutMethodProxy.invokeCatchThrowableExceptions(aContainerBeanProxy);
}

public static void invoke_setSize(IBeanProxy aBeanProxy,IBeanProxy aDimensionBeanProxy){
	BeanAwtUtilities constants = getConstants(aBeanProxy);
	
	if (constants.setSizeMethodProxy == null) {
		constants.setSizeMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Component").getMethodProxy( //$NON-NLS-1$
			"setSize", //$NON-NLS-1$
			"java.awt.Dimension" //$NON-NLS-1$
		);
	}
	constants.setSizeMethodProxy.invokeCatchThrowableExceptions(aBeanProxy,aDimensionBeanProxy);
}

public static void invoke_setBounds(IBeanProxy aBeanProxy,IBeanProxy aRectangleBeanProxy){
	BeanAwtUtilities constants = getConstants(aBeanProxy);
	
	if (constants.setBoundsMethodProxy == null) {
		constants.setBoundsMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Component").getMethodProxy( //$NON-NLS-1$
			"setBounds", //$NON-NLS-1$
			"java.awt.Rectangle" //$NON-NLS-1$
		);
	}
	constants.setBoundsMethodProxy.invokeCatchThrowableExceptions(aBeanProxy,aRectangleBeanProxy);
}

public static IRectangleBeanProxy invoke_getBounds(IBeanProxy aBeanProxy){
	BeanAwtUtilities constants = getConstants(aBeanProxy);
	
	if (constants.getBoundsMethodProxy == null) {
		constants.getBoundsMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Component").getMethodProxy("getBounds"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	return (IRectangleBeanProxy) constants.getBoundsMethodProxy.invokeCatchThrowableExceptions(aBeanProxy);
}

public static IPointBeanProxy invoke_getLocation(IBeanProxy aBeanProxy){
	BeanAwtUtilities constants = getConstants(aBeanProxy);
	
	if (constants.getLocationMethodProxy == null) {
		constants.getLocationMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Component").getMethodProxy("getLocation"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	return (IPointBeanProxy) constants.getLocationMethodProxy.invokeCatchThrowableExceptions(aBeanProxy);
}

public static IDimensionBeanProxy invoke_getSize(IBeanProxy aBeanProxy){
	BeanAwtUtilities constants = getConstants(aBeanProxy);
	
	if (constants.getSizeMethodProxy == null) {
		constants.getSizeMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Component").getMethodProxy("getSize"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	return (IDimensionBeanProxy) constants.getSizeMethodProxy.invokeCatchThrowableExceptions(aBeanProxy);
}

public static IDimensionBeanProxy invoke_getPreferredSize(IBeanProxy aBeanProxy){
	BeanAwtUtilities constants = getConstants(aBeanProxy);
	
	if (constants.getPreferredSizeMethodProxy == null) {
		constants.getPreferredSizeMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Component").getMethodProxy("getPreferredSize"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	return (IDimensionBeanProxy) constants.getPreferredSizeMethodProxy.invokeCatchThrowableExceptions(aBeanProxy);
}

public static IBeanProxy invoke_getParent(IBeanProxy aBeanProxy){
	BeanAwtUtilities constants = getConstants(aBeanProxy);
	
	if (constants.getParentMethodProxy == null) {
		constants.getParentMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Component").getMethodProxy("getParent"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	return constants.getParentMethodProxy.invokeCatchThrowableExceptions(aBeanProxy);
}

public static IArrayBeanProxy invoke_getComponents(IBeanProxy aContainerBeanProxy){
	BeanAwtUtilities constants = getConstants(aContainerBeanProxy);
	
	if (constants.getComponentsMethodProxy == null) {
		constants.getComponentsMethodProxy = aContainerBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Container").getMethodProxy("getComponents"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	return (IArrayBeanProxy) constants.getComponentsMethodProxy.invokeCatchThrowableExceptions(aContainerBeanProxy);
}

public static void invoke_set_ComponentBean_Manager(IBeanProxy aComponentManager, IBeanProxy aComponentBeanProxy){
	BeanAwtUtilities constants = getConstants(aComponentManager);
	
	if (constants.setComponentMethodProxy == null) {
		constants.setComponentMethodProxy = aComponentManager.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.ComponentManager").getMethodProxy( //$NON-NLS-1$
			"setComponent", //$NON-NLS-1$
			"java.awt.Component" //$NON-NLS-1$
		);
	}
	constants.setComponentMethodProxy.invokeCatchThrowableExceptions(aComponentManager, aComponentBeanProxy);
}

public static void invoke_set_RelativeParent_Manager(IBeanProxy aComponentManager, IBeanProxy aContainerBeanProxy){
	BeanAwtUtilities constants = getConstants(aComponentManager);
	
	if (constants.setRelativeParentMethodProxy == null) {
		constants.setRelativeParentMethodProxy = aComponentManager.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.ComponentManager").getMethodProxy( //$NON-NLS-1$
			"setRelativeParentComponent", //$NON-NLS-1$
			"java.awt.Container" //$NON-NLS-1$
		);
	}
	constants.setRelativeParentMethodProxy.invokeCatchThrowableExceptions(aComponentManager, aContainerBeanProxy);
}

public static IArrayBeanProxy invoke_get_Location_Manager(IBeanProxy aComponentManager){
	BeanAwtUtilities constants = getConstants(aComponentManager);
	
	if (constants.getManagerLocationMethodProxy == null) {
		constants.getManagerLocationMethodProxy = aComponentManager.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.ComponentManager").getMethodProxy("getLocation"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	return (IArrayBeanProxy) constants.getManagerLocationMethodProxy.invokeCatchThrowableExceptions(aComponentManager);
}

/**
 * Return the ILayoutPolicyFactory for the layout manager of a containerProxy
 */
public static ILayoutPolicyFactory getLayoutPolicyFactory(IBeanProxy containerProxy, EditDomain domain) {
	IBeanProxy layoutManagerProxy = invoke_getLayout(containerProxy);
	return getLayoutPolicyFactoryFromLayoutManger(layoutManagerProxy, domain);
}

public static final String LAYOUT_POLICY_FACTORY_CLASSNAME_KEY = "org.eclipse.ve.internal.jfc.core.layoutpolicyfactoryclassnamekey"; //$NON-NLS-1$
/**
 * Return the ILayoutPolicyFactory for the layout manager of a LayoutManagerProxy
 * Note: if containerProxy is null, then editdomain can be null.
 */
public static ILayoutPolicyFactory getLayoutPolicyFactoryFromLayoutManger(IBeanProxy layoutManagerProxy, EditDomain domain) {
	if (layoutManagerProxy == null)
		return new NullLayoutPolicyFactory();	// There is nothing we can check against, so we hardcode null.

	ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);	
	EClassifier javaType = Utilities.getJavaClass(layoutManagerProxy.getTypeProxy(), rset);
	return getLayoutPolicyFactoryFromLayoutManger(javaType, domain);
}

/**
 * Return the ILayoutPolicyFactory for the layout manager of a LayoutManager EClassifier
 * Note: if containerProxy is null, then editdomain can be null.
 */
public static ILayoutPolicyFactory getLayoutPolicyFactoryFromLayoutManger(EClassifier layoutManagerClass, EditDomain domain) {

	if (layoutManagerClass == null)
		return new NullLayoutPolicyFactory();	// There is nothing we can check against, so we hardcode null.
	if (!(layoutManagerClass instanceof JavaClass))
		return null;	// Not a java class.

	ClassDescriptorDecoratorPolicy policy = ClassDescriptorDecoratorPolicy.getPolicy(domain);
	BeanDecorator decr = (BeanDecorator) policy.findDecorator(layoutManagerClass, BeanDecorator.class, LAYOUT_POLICY_FACTORY_CLASSNAME_KEY);
	String layoutFactoryClassname = null;
	if (decr != null)
		layoutFactoryClassname = (String) decr.getKeyedValues().get(LAYOUT_POLICY_FACTORY_CLASSNAME_KEY);
	if (layoutFactoryClassname != null) {
		try {
			Class factoryClass = CDEPlugin.getClassFromString(layoutFactoryClassname);
			ILayoutPolicyFactory fact = (ILayoutPolicyFactory) factoryClass.newInstance();
			CDEPlugin.setInitializationData(fact, layoutFactoryClassname, null);
			return fact;
		} catch (ClassNotFoundException e) {
			JavaVEPlugin.getPlugin().getMsgLogger().log(new Status(IStatus.WARNING, JFCVisualPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "", e), MsgLogger.LOG_WARNING); //$NON-NLS-1$
		} catch (ClassCastException e) {
			JavaVEPlugin.getPlugin().getMsgLogger().log(new Status(IStatus.WARNING, JFCVisualPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "", e), MsgLogger.LOG_WARNING); //$NON-NLS-1$
		} catch (InstantiationException e) {
			JavaVEPlugin.getPlugin().getMsgLogger().log(new Status(IStatus.WARNING, JFCVisualPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "", e), MsgLogger.LOG_WARNING); //$NON-NLS-1$
		} catch (IllegalAccessException e) {
			JavaVEPlugin.getPlugin().getMsgLogger().log(new Status(IStatus.WARNING, JFCVisualPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "", e), MsgLogger.LOG_WARNING); //$NON-NLS-1$
		} catch (CoreException e) {
			JavaVEPlugin.getPlugin().getMsgLogger().log(new Status(IStatus.WARNING, JFCVisualPlugin.getPlugin().getDescriptor().getUniqueIdentifier(), 0, "", e), MsgLogger.LOG_WARNING); //$NON-NLS-1$
		}
	}
	
	// Need to see if it is type LayoutManager2
	if (((JavaClass) Utilities.getJavaClass("java.awt.LayoutManager2", layoutManagerClass.eResource().getResourceSet())).isAssignableFrom(layoutManagerClass)) //$NON-NLS-1$
		return new UnknownLayout2PolicyFactory();
	else
		return new UnknownLayoutPolicyFactory();
}

/**
 * Add tab before the specified component.
 * This uses the JTabbedPaneManager.
 */
public static void invoke_insert_Tab_before(IBeanProxy aJTabbedPaneBeanProxy, IBeanProxy aTitleBeanProxy, IBeanProxy anIconBeanProxy, IBeanProxy aComponentBeanProxy, IBeanProxy aTooltipBeanProxy, IBeanProxy beforeComponentBeanProxy){
	BeanAwtUtilities constants = getConstants(aJTabbedPaneBeanProxy);
	
	if (constants.managerInsertTabBeforeMethodProxy == null) {
		constants.managerInsertTabBeforeMethodProxy = aJTabbedPaneBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager").getMethodProxy( //$NON-NLS-1$
			"insertTabBefore", //$NON-NLS-1$
			new String[] {"javax.swing.JTabbedPane", "java.lang.String", "javax.swing.Icon", "java.awt.Component" , "java.lang.String", "java.awt.Component"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		);
	}
	constants.managerInsertTabBeforeMethodProxy.invokeCatchThrowableExceptions(null, new IBeanProxy[] {aJTabbedPaneBeanProxy, aTitleBeanProxy, anIconBeanProxy, aComponentBeanProxy, aTooltipBeanProxy, beforeComponentBeanProxy});
}

/**
 * Add tab at the end.
 */
public static void invoke_add_Tab(IBeanProxy aJTabbedPaneBeanProxy, IBeanProxy aTitleBeanProxy, IBeanProxy anIconBeanProxy, IBeanProxy aComponentBeanProxy, IBeanProxy aTooltipBeanProxy){
	BeanAwtUtilities constants = getConstants(aJTabbedPaneBeanProxy);
	
	if (constants.addTabMethodProxy== null) {
		constants.addTabMethodProxy = aJTabbedPaneBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("javax.swing.JTabbedPane").getMethodProxy( //$NON-NLS-1$
			"addTab", //$NON-NLS-1$
			new String[] {"java.lang.String", "javax.swing.Icon", "java.awt.Component" , "java.lang.String"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		);
	}
	constants.addTabMethodProxy.invokeCatchThrowableExceptions(aJTabbedPaneBeanProxy, new IBeanProxy[] {aTitleBeanProxy, anIconBeanProxy, aComponentBeanProxy, aTooltipBeanProxy});
}

/**
 * Return the selected tabbed pane component, null if none.
 */
public static IBeanProxy invoke_tab_getSelectedComponent(IBeanProxy aJTabbedPaneBeanProxy) {
	BeanAwtUtilities constants = getConstants(aJTabbedPaneBeanProxy);
	
	if (constants.getTabSelectedComponentMethodProxy == null) {
		constants.getTabSelectedComponentMethodProxy = aJTabbedPaneBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("javax.swing.JTabbedPane").getMethodProxy( //$NON-NLS-1$
			"getSelectedComponent"); //$NON-NLS-1$
	}
	return constants.getTabSelectedComponentMethodProxy.invokeCatchThrowableExceptions(aJTabbedPaneBeanProxy);
}

/**
 * Set the selected tabbed pane component.
 */
public static void invoke_tab_setSelectedComponent(IBeanProxy aJTabbedPaneBeanProxy, IBeanProxy componentBeanProxy) {
	BeanAwtUtilities constants = getConstants(aJTabbedPaneBeanProxy);
	
	if (constants.setTabSelectedComponentMethodProxy == null) {
		constants.setTabSelectedComponentMethodProxy = aJTabbedPaneBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("javax.swing.JTabbedPane").getMethodProxy( //$NON-NLS-1$
			"setSelectedComponent", "java.awt.Component"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	constants.setTabSelectedComponentMethodProxy.invokeCatchThrowableExceptions(aJTabbedPaneBeanProxy, componentBeanProxy);
}

/**
 * Set icon at the specified component. Goes through JTabbedPaneManager
 */
public static void invoke_tab_setIconAt(IBeanProxy aJTabbedPaneBeanProxy, IBeanProxy componentBeanProxy, IBeanProxy anIconBeanProxy){
	BeanAwtUtilities constants = getConstants(aJTabbedPaneBeanProxy);
	
	if (constants.managerSetTabIconAt == null) {
		constants.managerSetTabIconAt = aJTabbedPaneBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager").getMethodProxy( //$NON-NLS-1$
			"setIconAt", //$NON-NLS-1$
			new String[] {"javax.swing.JTabbedPane", "java.awt.Component", "javax.swing.Icon"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		);
	}
	constants.managerSetTabIconAt.invokeCatchThrowableExceptions(null, new IBeanProxy[] {aJTabbedPaneBeanProxy, componentBeanProxy, anIconBeanProxy});
}

/**
 * Set title at the specified component. Goes through JTabbedPaneManager
 */
public static void invoke_tab_setTitleAt(IBeanProxy aJTabbedPaneBeanProxy, IBeanProxy componentBeanProxy, IBeanProxy aStringBeanProxy){
	BeanAwtUtilities constants = getConstants(aJTabbedPaneBeanProxy);
	
	if (constants.managerSetTabTitleAt == null) {
		constants.managerSetTabTitleAt = aJTabbedPaneBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager").getMethodProxy( //$NON-NLS-1$
			"setTitleAt", //$NON-NLS-1$
			new String[] {"javax.swing.JTabbedPane", "java.awt.Component", "java.lang.String"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		);
	}
	constants.managerSetTabTitleAt.invokeCatchThrowableExceptions(null, new IBeanProxy[] {aJTabbedPaneBeanProxy, componentBeanProxy, aStringBeanProxy});
}

/**
 * Set tooltip at the specified component. Goes through JTabbedPaneManager
 */
public static void invoke_tab_setTooltipTextAt(IBeanProxy aJTabbedPaneBeanProxy, IBeanProxy componentBeanProxy, IBeanProxy aStringBeanProxy){
	BeanAwtUtilities constants = getConstants(aJTabbedPaneBeanProxy);
	
	if (constants.managerSetTabTooltipAt == null) {
		constants.managerSetTabTooltipAt = aJTabbedPaneBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager").getMethodProxy( //$NON-NLS-1$
			"setTooltipTextAt", //$NON-NLS-1$
			new String[] {"javax.swing.JTabbedPane", "java.awt.Component", "java.lang.String"} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		);
	}
	constants.managerSetTabTooltipAt.invokeCatchThrowableExceptions(null, new IBeanProxy[] {aJTabbedPaneBeanProxy, componentBeanProxy, aStringBeanProxy});
}
/**
 * Hide and reshow the popup menu so it resizes after adding/removing components. Goes through JPopupMenuManager
 */
public static void invoke_jpopup_revalidate(IBeanProxy aJPopupMenuBeanProxy){
	BeanAwtUtilities constants = getConstants(aJPopupMenuBeanProxy);
	
	if (constants.popupMenuManagerRevalidate == null) {
		constants.popupMenuManagerRevalidate = aJPopupMenuBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.JPopupMenuManager").getMethodProxy( //$NON-NLS-1$
			"revalidate", "javax.swing.JPopupMenu" //$NON-NLS-1$ //$NON-NLS-2$
		);
	}
	constants.popupMenuManagerRevalidate.invokeCatchThrowableExceptions(null, aJPopupMenuBeanProxy);
}
/**
 * Remove the first menuitem whose text equals the string. Goes through JMenuManager
 */
public static void invoke_jmenu_remove_jmenuitem_string(IBeanProxy aJMenuBeanProxy, IStringBeanProxy aStringBeanProxy){
	BeanAwtUtilities constants = getConstants(aJMenuBeanProxy);
	
	if (constants.menuManagerRemoveJMenuItemString == null) {
		constants.menuManagerRemoveJMenuItemString = aJMenuBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.JMenuManager").getMethodProxy( //$NON-NLS-1$
			"removeMenuItemWithString", //$NON-NLS-1$
			new String[] {"java.awt.Container", "java.lang.String"} //$NON-NLS-1$ //$NON-NLS-2$
		);
	}
	constants.menuManagerRemoveJMenuItemString.invokeCatchThrowableExceptions(null, new IBeanProxy[] {aJMenuBeanProxy, aStringBeanProxy});
}
/**
 * Remove the first menuitem whose action equals the action passed in. Goes through JMenuManager
 */
public static void invoke_jmenu_remove_jmenuitem_action(IBeanProxy aJMenuBeanProxy, IBeanProxy aBeanProxy){
	BeanAwtUtilities constants = getConstants(aJMenuBeanProxy);
	
	if (constants.menuManagerRemoveJMenuItemAction == null) {
		constants.menuManagerRemoveJMenuItemAction = aJMenuBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.JMenuManager").getMethodProxy( //$NON-NLS-1$
			"removeMenuItemWithAction", //$NON-NLS-1$
			new String[] {"java.awt.Container", "javax.swing.Action"} //$NON-NLS-1$ //$NON-NLS-2$
		);
	}
	constants.menuManagerRemoveJMenuItemAction.invokeCatchThrowableExceptions(null, new IBeanProxy[] {aJMenuBeanProxy, aBeanProxy});
}

/**
 * Splitpane manager setSplitpane
 */
public static void invoke_set_JSplitPaneBean_Manager(IBeanProxy aSplitPaneManager, IBeanProxy aSplitPaneBeanProxy){
	BeanAwtUtilities constants = getConstants(aSplitPaneManager);
	
	if (constants.setJSplitPaneManagerMethodProxy == null) {
		constants.setJSplitPaneManagerMethodProxy = aSplitPaneManager.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.JSplitPaneManager").getMethodProxy( //$NON-NLS-1$
			"setSplitPane", //$NON-NLS-1$
			"javax.swing.JSplitPane" //$NON-NLS-1$
		);
	}
	constants.setJSplitPaneManagerMethodProxy.invokeCatchThrowableExceptions(aSplitPaneManager, aSplitPaneBeanProxy);
}

/**
 * Splitpane manager setDividerLocation
 */
public static void invoke_set_JSplitPane_DividerLocation_Manager(IBeanProxy aSplitPaneManager, IBeanProxy dividerLocation){
	BeanAwtUtilities constants = getConstants(aSplitPaneManager);
	
	if (constants.setJSplitPaneDividerLocationManagerProxy == null) {
		constants.setJSplitPaneDividerLocationManagerProxy = aSplitPaneManager.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.JSplitPaneManager").getMethodProxy( //$NON-NLS-1$
			"setDividerLocation", //$NON-NLS-1$
			"int" //$NON-NLS-1$
		);
	}
	constants.setJSplitPaneDividerLocationManagerProxy.invokeCatchThrowableExceptions(aSplitPaneManager, dividerLocation);
}

/**
 * Splitpane manager resetToPreferredSizes
 */
public static void invoke_reset_JSplitPane_PreferredSizes_Manager(IBeanProxy aSplitPaneManager){
	BeanAwtUtilities constants = getConstants(aSplitPaneManager);
	
	if (constants.resetToPreferredSizesJSplitPaneManagerProxy == null) {
		constants.resetToPreferredSizesJSplitPaneManagerProxy = aSplitPaneManager.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.JSplitPaneManager").getMethodProxy( //$NON-NLS-1$
			"resetToPreferredSizes" //$NON-NLS-1$
		);
	}
	constants.resetToPreferredSizesJSplitPaneManagerProxy.invokeCatchThrowableExceptions(aSplitPaneManager);
}

/**
 * Remove the first item whose action equals the action passed in. Goes through JToolBarManager
 */
public static void invoke_jtoolbar_remove_item_action(IBeanProxy aJToolbarBeanProxy, IBeanProxy aBeanProxy){
	BeanAwtUtilities constants = getConstants(aJToolbarBeanProxy);
	
	if (constants.toolbarManagerRemoveItemAction == null) {
		constants.toolbarManagerRemoveItemAction = aJToolbarBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.JToolBarManager").getMethodProxy( //$NON-NLS-1$
			"removeItemWithAction", //$NON-NLS-1$
			new String[] {"javax.swing.JToolBar", "javax.swing.Action"} //$NON-NLS-1$ //$NON-NLS-2$
		);
	}
	constants.toolbarManagerRemoveItemAction.invokeCatchThrowableExceptions(null, new IBeanProxy[] {aJToolbarBeanProxy, aBeanProxy});
}
/**
 * Get the component orientation and invoke the isLeftToRight() method. Return an IBooleanBeanProxy.
 */
public static IBooleanBeanProxy invoke_getComponentOrientation_isLeftToRight(IBeanProxy aBeanProxy) {
	if (aBeanProxy == null) 
		return null;
	IBooleanBeanProxy booleanProxy = null;
	IMethodProxy getComponentOrientationMethodProxy = aBeanProxy.getTypeProxy().getMethodProxy("getComponentOrientation"); //$NON-NLS-1$
	IBeanProxy componentOrientationProxy = getComponentOrientationMethodProxy.invokeCatchThrowableExceptions(aBeanProxy);
	if (componentOrientationProxy != null) {
		IMethodProxy isLeftToRightMethodProxy = componentOrientationProxy.getTypeProxy().getMethodProxy("isLeftToRight"); //$NON-NLS-1$
		booleanProxy = (IBooleanBeanProxy)isLeftToRightMethodProxy.invokeCatchThrowableExceptions(componentOrientationProxy);
	}
	return booleanProxy;
}

/**
 * Pack the java.awt.Window
 */
public static void invoke_window_pack(IBeanProxy aWindowProxy){
	BeanAwtUtilities constants = getConstants(aWindowProxy);
	
	if (constants.windowPackProxy == null) {
		constants.windowPackProxy = aWindowProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("java.awt.Window").getMethodProxy( //$NON-NLS-1$
			"pack"); //$NON-NLS-1$
	}
	constants.windowPackProxy.invokeCatchThrowableExceptions(aWindowProxy);
}
/*
 * Helper class to hide the grid for edit parts and their children that have a grid controller.
 * Used especially for JTabbedPanes and Containers that have a CardLayout. 
 */
public static void hideGrids (EditPart editpart) {
	if (editpart == null) return;
	GridController gridController = GridController.getGridController(editpart);
	if (gridController != null && gridController.isGridShowing())
		gridController.setGridShowing(false);
	List children = editpart.getChildren();
	for (int i = 0; i < children.size(); i++) {
		hideGrids((EditPart)children.get(i));
	}
}

protected BeanAwtUtilities() {
}
}