/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.swt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.graphics.Point;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.jcm.JCMMethod;
import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.vce.VCEPreferences;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory;
import org.eclipse.ve.internal.java.visual.VisualUtilities;

public class BeanSWTUtilities {

    // JCMMethod proxies are cached in a registry constants.
    public IMethodProxy getLayoutMethodProxy,
		setBoundsMethodProxy,
		getBoundsMethodProxy,
		getLocationMethodProxy,
		getChildrenMethodProxy,
		getParentMethodProxy,
		computeSizeMethodProxy,
		setTabfolderSelectionMethodProxy,
		setCTabfolderSelectionMethodProxy;

    public static final Object REGISTRY_KEY = new Object();

    public static BeanSWTUtilities getConstants(ProxyFactoryRegistry registry) {
        BeanSWTUtilities constants = (BeanSWTUtilities) registry.getConstants(REGISTRY_KEY);
        if (constants == null) registry.registerConstants(REGISTRY_KEY, constants = new BeanSWTUtilities());
        return constants;
    }

    protected static BeanSWTUtilities getConstants(IBeanProxy proxy) {
        return getConstants(proxy.getProxyFactoryRegistry());
    }

    /**
     * Return the ILayoutPolicyFactory for the layout of a compositeProxy
     */
    public static ILayoutPolicyFactory getLayoutPolicyFactory(IBeanProxy compositeProxy, EditDomain domain) {
        IBeanProxy layoutProxy = invoke_getLayout(compositeProxy);
        return getLayoutPolicyFactoryFromLayout(layoutProxy, domain);
    }

    /**
     * Return the ILayoutPolicyFactory for the layout of a LayoutProxy.
     * Note: if compositeProxy is null, then editdomain can be null.
     */
    public static ILayoutPolicyFactory getLayoutPolicyFactoryFromLayout(IBeanProxy layoutProxy, EditDomain domain) {
        if (layoutProxy == null) 
            return new NullLayoutPolicyFactory(); // There is nothing we can check against, so we hardcode null.
        ILayoutPolicyFactory factory = VisualUtilities.getLayoutPolicyFactory(layoutProxy.getTypeProxy(), domain);
        if (factory == null) {
            return getDefaultLayoutPolicyFactory();
        } else {
            return factory;
        }
    }

    public static ILayoutPolicyFactory getLayoutPolicyFactoryFromLayout(EClassifier classifier, EditDomain editDomain) {
    	if (classifier == null)
    		return new NullLayoutPolicyFactory();	// There is nothing we can check against, so we hardcode null.
    	if (!(classifier instanceof JavaClass))
    		return null;	// Not a java class.
    	ILayoutPolicyFactory layoutPolicyFactory = VisualUtilities.getLayoutPolicyFactory(classifier, editDomain);
    	if(layoutPolicyFactory == null){
    		return getDefaultLayoutPolicyFactory();
    	} else {
    		return layoutPolicyFactory;
    	}
    }
    public static IBeanProxy invoke_getLayout(final IBeanProxy aCompositeBeanProxy) {
        BeanSWTUtilities constants = getConstants(aCompositeBeanProxy);
        if (constants.getLayoutMethodProxy == null) {
            constants.getLayoutMethodProxy = aCompositeBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(
                    "org.eclipse.swt.widgets.Composite").getMethodProxy("getLayout"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        if (constants.getLayoutMethodProxy != null) {
            final IMethodProxy layoutMethodProxy = constants.getLayoutMethodProxy;
            return (IBeanProxy) JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(aCompositeBeanProxy.getProxyFactoryRegistry(),
                    new DisplayManager.DisplayRunnable() {

                        public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
                            return layoutMethodProxy.invoke(aCompositeBeanProxy);
                        }
                    });
        }
        return null;
    }
    private static ILayoutPolicyFactory getDefaultLayoutPolicyFactory(){
   		return new UnknownLayoutPolicyFactory();
    }
    
    public static void invoke_setBounds(IBeanProxy aBeanProxy, final IBeanProxy aRectangleBeanProxy){
    	BeanSWTUtilities constants = getConstants(aBeanProxy);

    	if (constants.setBoundsMethodProxy == null) {
    		constants.setBoundsMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.widgets.Control").getMethodProxy( //$NON-NLS-1$
    			"setBounds", //$NON-NLS-1$
    			"org.eclipse.swt.graphics.Rectangle" //$NON-NLS-1$
    		);
    	}
        if (constants.setBoundsMethodProxy != null) {
        	final IMethodProxy setBoundsMethodProxy = constants.setBoundsMethodProxy;
            JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(aRectangleBeanProxy.getProxyFactoryRegistry(),
                    new DisplayManager.DisplayRunnable() {

                        public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
                            return setBoundsMethodProxy.invoke(aRectangleBeanProxy);
                        }
                    });
        }
    }

    public static IRectangleBeanProxy invoke_getBounds(final IBeanProxy aBeanProxy){
    	BeanSWTUtilities constants = getConstants(aBeanProxy);
    	
    	if (constants.getBoundsMethodProxy == null) {
    		constants.getBoundsMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.widgets.Control").getMethodProxy("getBounds"); //$NON-NLS-1$ //$NON-NLS-2$
    	}
        if (constants.getBoundsMethodProxy != null) {
        	final IMethodProxy getBoundsMethodProxy = constants.getBoundsMethodProxy;
            return (IRectangleBeanProxy) JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(aBeanProxy.getProxyFactoryRegistry(),
                    new DisplayManager.DisplayRunnable() {

                        public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
                            return getBoundsMethodProxy.invoke(aBeanProxy);
                        }
                    });
        }
    	return null;
    }

    public static IPointBeanProxy invoke_computeSize(final IBeanProxy aBeanProxy, final IBeanProxy aDefaultX, final IBeanProxy aDefaultY ){
    	BeanSWTUtilities constants = getConstants(aBeanProxy);
    	
    	if (constants.computeSizeMethodProxy == null) {
    		constants.computeSizeMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.widgets.Control").getMethodProxy("computeSize", //$NON-NLS-1$ //$NON-NLS-2$
    				new String[] {"int" , "int"}); //$NON-NLS-1$ //$NON-NLS-2$
    	}
        if (constants.computeSizeMethodProxy != null) {
        	final IMethodProxy computeSizeMethodProxy = constants.computeSizeMethodProxy;
            return (IPointBeanProxy) JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(aBeanProxy.getProxyFactoryRegistry(),
                    new DisplayManager.DisplayRunnable() {

                        public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
                        	
                            return computeSizeMethodProxy.invoke(aBeanProxy,new IBeanProxy[] {aDefaultX , aDefaultY});
                        }
                    });
        }
    	return null;
    }
    public static IPointBeanProxy invoke_getLocation(final IBeanProxy aBeanProxy){
    	BeanSWTUtilities constants = getConstants(aBeanProxy);
    	
    	if (constants.getLocationMethodProxy == null) {
    		constants.getLocationMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.widgets.Control").getMethodProxy("getLocation"); //$NON-NLS-1$ //$NON-NLS-2$
    	}
        if (constants.getLocationMethodProxy != null) {
        	final IMethodProxy getLocationMethodProxy = constants.getLocationMethodProxy;
            return (IPointBeanProxy) JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(aBeanProxy.getProxyFactoryRegistry(),
                    new DisplayManager.DisplayRunnable() {

                        public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
                            return getLocationMethodProxy.invoke(aBeanProxy);
                        }
                    });
        }
    	return null;
    }
    public static IArrayBeanProxy invoke_getChildren(final IBeanProxy aBeanProxy){
    	BeanSWTUtilities constants = getConstants(aBeanProxy);
    	
    	if (constants.getChildrenMethodProxy == null) {
    		constants.getChildrenMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.widgets.Composite").getMethodProxy("getChildren"); //$NON-NLS-1$ //$NON-NLS-2$
    	}
        if (constants.getChildrenMethodProxy != null) {
        	final IMethodProxy getChildrenMethodProxy = constants.getChildrenMethodProxy;
            return (IArrayBeanProxy) JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(aBeanProxy.getProxyFactoryRegistry(),
                    new DisplayManager.DisplayRunnable() {

                        public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
                            return getChildrenMethodProxy.invoke(aBeanProxy);
                        }
                    });
        }
    	return null;
    }
	
    public static IBeanProxy invoke_getParent(final IBeanProxy aControlProxy){
    	BeanSWTUtilities constants = getConstants(aControlProxy);
    	
    	if (constants.getParentMethodProxy == null) {
    		constants.getParentMethodProxy = aControlProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.widgets.Control").getMethodProxy("getParent"); //$NON-NLS-1$ //$NON-NLS-2$
    	}
        if (constants.getParentMethodProxy != null) {
        	final IMethodProxy getParentMethodProxy = constants.getParentMethodProxy;
            return (IRectangleBeanProxy) JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(aControlProxy.getProxyFactoryRegistry(),
                    new DisplayManager.DisplayRunnable() {

                        public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
                            return getParentMethodProxy.invoke(aControlProxy);
                        }
                    });
        }
    	return null;
    }
	
    public static Point getOffScreenLocation(){
    	
    	boolean showWindow = VCEPreferences.isLiveWindowOn();
    	if (showWindow)
    		return new Point(0,0);
    	else {
    		return new Point(10000, 10000);
    	}
    }
    /**
     * Set the selection on the TabFolder which brings the respective tab to the front.
     */
    public static void invoke_tabfolder_setSelection(final IBeanProxy aTabFolderBeanProxy, final IIntegerBeanProxy intBeanProxy) {
    	BeanSWTUtilities constants = getConstants(aTabFolderBeanProxy);
    	
    	if (constants.setTabfolderSelectionMethodProxy == null) {
    		constants.setTabfolderSelectionMethodProxy = aTabFolderBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.widgets.TabFolder").getMethodProxy( //$NON-NLS-1$
    			"setSelection", "int"); //$NON-NLS-1$ //$NON-NLS-2$
    	}
        if (constants.setTabfolderSelectionMethodProxy != null) {
        	final IMethodProxy setTabfolderSelectionMethodProxy = constants.setTabfolderSelectionMethodProxy;
            JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(aTabFolderBeanProxy.getProxyFactoryRegistry(),
                    new DisplayManager.DisplayRunnable() {

                        public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
                            return setTabfolderSelectionMethodProxy.invoke(aTabFolderBeanProxy, intBeanProxy);
                        }
                    });
        }
    }
	
    /**
     * Set the selection on the CTabFolder which brings the respective tab to the front.
     */
    public static void invoke_ctabfolder_setSelection(final IBeanProxy aCTabFolderBeanProxy, final IIntegerBeanProxy intBeanProxy) {
    	BeanSWTUtilities constants = getConstants(aCTabFolderBeanProxy);
    	
    	if (constants.setCTabfolderSelectionMethodProxy == null) {
    		constants.setCTabfolderSelectionMethodProxy = aCTabFolderBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.custom.CTabFolder").getMethodProxy( //$NON-NLS-1$
    			"setSelection", "int"); //$NON-NLS-1$ //$NON-NLS-2$
    	}
        if (constants.setCTabfolderSelectionMethodProxy != null) {
        	final IMethodProxy setCTabfolderSelectionMethodProxy = constants.setCTabfolderSelectionMethodProxy;
            JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(aCTabFolderBeanProxy.getProxyFactoryRegistry(),
                    new DisplayManager.DisplayRunnable() {

                        public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
                            return setCTabfolderSelectionMethodProxy.invoke(aCTabFolderBeanProxy, intBeanProxy);
                        }
                    });
        }
    }

	public static boolean isValidBeanLocation(EditDomain domain, EObject childComponent, EObject targetContainer) {
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
		EReference sfControls = JavaInstantiation.getReference(rset, SWTConstants.SF_COMPOSITE_CONTROLS);
		// Check the child's init method and the target init method. It's valid if the same
		// since the adding is done in the same init method.
		EObject childRef = InverseMaintenanceAdapter.getFirstReferencedBy(childComponent, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		EObject targetRef = InverseMaintenanceAdapter.getFirstReferencedBy(targetContainer, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		// If the child's init method and the target init method is the same, this is valid.
		if (childRef instanceof JCMMethod && targetRef instanceof JCMMethod && childRef == targetRef)
			return true;

		EObject parent = InverseMaintenanceAdapter.getFirstReferencedBy(childComponent, sfControls);
		EObject parentRef = InverseMaintenanceAdapter.getFirstReferencedBy(parent, JCMPackage.eINSTANCE.getJCMMethod_Initializes());
		/* 
		 * The only valid case is one in which the child's init method is different
		 * from the parent's init method.
		 */
		if (childRef instanceof JCMMethod && parentRef instanceof JCMMethod)
			if (childRef != parentRef)
				return true;
		return false;
	}
	
	/**
	 * Helper class to determine if the JFace plugin id is visible to this java project.
	 * 
	 * @param editDomain
	 * @return 
	 * 		true if it's visible 
	 * 		false if it's not visible, not found, or JavaModelException is thrown
	 * 
	 * @since 1.1.0
	 */
	public static boolean isJFaceProject(EditDomain editDomain) {
		IJavaProject proj = JavaEditDomainHelper.getJavaProject(editDomain);
		Map containers = new HashMap(), plugins = new HashMap();
		try {
			ProxyPlugin.getPlugin().getIDsFound(proj, containers, new HashMap(), plugins, new HashMap());
			return plugins.get("org.eclipse.jface") != null ? ((Boolean) plugins.get("org.eclipse.jface")).booleanValue() : false; //$NON-NLS-1$ //$NON-NLS-2$
		} catch (JavaModelException e) {
		}
		return false;
	}
}
