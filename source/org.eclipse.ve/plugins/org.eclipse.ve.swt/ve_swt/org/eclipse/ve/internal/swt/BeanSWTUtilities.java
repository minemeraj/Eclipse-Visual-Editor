package org.eclipse.ve.internal.swt;

import org.eclipse.emf.ecore.EClassifier;

import org.eclipse.jem.internal.proxy.awt.IRectangleBeanProxy;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.EditDomain;

import org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory;
import org.eclipse.ve.internal.java.visual.VisualUtilities;

public class BeanSWTUtilities {

    // JCMMethod proxies are cached in a registry constants.
    public IMethodProxy getLayoutMethodProxy,
		setBoundsMethodProxy,
		getBoundsMethodProxy;

    public static final String REGISTRY_KEY = "org.eclipse.ve.internal.swt.BeanSWTUtilities"; //$NON-NLS-1$

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
}
