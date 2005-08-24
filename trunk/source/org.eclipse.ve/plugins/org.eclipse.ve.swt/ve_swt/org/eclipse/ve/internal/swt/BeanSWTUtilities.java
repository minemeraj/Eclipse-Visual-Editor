/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
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

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.widgets.*;

import org.eclipse.jem.internal.instantiation.ImplicitAllocation;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
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

import org.eclipse.ve.internal.swt.ControlManager.FeedbackController;

public class BeanSWTUtilities {
	// JCMMethod proxies are cached in a registry constants.
    public IMethodProxy getManagerDefaultLocationMethodProxy,
		getManagerDefaultBoundsMethodProxy,
    	getLayoutMethodProxy,
		setBoundsMethodProxy,
		getBoundsMethodProxy,
		getLocationMethodProxy,
		getChildrenMethodProxy,
		getParentMethodProxy,
		computeSizeMethodProxy,
		setTabfolderSelectionMethodProxy,
		indexOfTabITemMethodProxy,
		indexOfCTabItemMethodProxy,
		setCTabfolderSelectionMethodProxy,
		setCTabFolderSelectionAtLocationMethodProxy,
		imageCaptureAbortMethodProxy,
		imageCaptureStartCaptureMethodProxy,
		managerTableGetAllColumnRects;

	private Map controlManagerFeedbackController;

	// methods that could be retrieved from expressions are complicated because we need
	// to add a listener to them to put in the final resolved value when received. But
	// we can only do this once. Don't want to add a listener each time we go get the
	// expression proxy. Doing this is complicated so we created a helper method. But
	// to do this efficiently (since we can't pass pointers fields except through
	// reflection and that's overkill), we will use an array and constants instead.

	private static final int MANAGER_SET_CONTROL = 0,
		MANAGER_OVERRIDE_LOCATION = MANAGER_SET_CONTROL+1,
		MANAGER_APPLY_BOUNDS = MANAGER_OVERRIDE_LOCATION+1,
		MANAGER_APPLY_LOCATION = MANAGER_APPLY_BOUNDS+1,
		MANAGER_INVALIDATE_CONTROL = MANAGER_APPLY_LOCATION+1,
		COMPOSITE_MANAGER_LAYOUT_VERIFY = MANAGER_INVALIDATE_CONTROL+1,
		MANAGER_APPLY_LAYOUTDATA = COMPOSITE_MANAGER_LAYOUT_VERIFY+1,
		SHELL_MANAGER_APPLYTITLE = MANAGER_APPLY_LAYOUTDATA + 1,
		SHELL_MANAGER_PACKONCHANGE = SHELL_MANAGER_APPLYTITLE + 1,
		MANAGER_DISPOSE = SHELL_MANAGER_PACKONCHANGE + 1,
		MAX_METHODS = MANAGER_DISPOSE + 1;
	

	private IProxyMethod[] methods = new IProxyMethod[MAX_METHODS];

	/**
	 * Called by the get methods for expressions after retrieving the IProxyMethod from the expression. It is used to add to the methods array and to
	 * put the resolved value in the methods array when it is resolved.
	 * 
	 * @param method
	 * @param methods
	 * @param methodID
	 * 
	 * @see BeanSWTUtilities#getControlInvalidate(IExpression) for an example of how to use this.
	 * @since 1.1.0
	 */
	protected static void processExpressionProxy(IProxyMethod method, final IProxyMethod[] methods, final int methodID) {
		if (method != null) {
			if (method.isBeanProxy())
				methods[methodID] = method;
			else {
				if (methods[methodID] == null) {
					((ExpressionProxy) method).addProxyListener(new ExpressionProxy.ProxyListener() {

						public void proxyResolved(ProxyEvent event) {
							IProxyMethod cm = methods[methodID];
							if (cm == null || cm.isExpressionProxy()) {
								methods[methodID] = (IProxyMethod) event.getProxy();
							}
						}

						public void proxyNotResolved(ProxyEvent event) {
							if (methods[methodID] == event.getSource())
								methods[methodID] = null;
						}

						public void proxyVoid(ProxyEvent event) {
							if (methods[methodID] == event.getSource())
								methods[methodID] = null;
						}
					});
				}
			}
		}
	}

    public static final Object REGISTRY_KEY = new Object();

	public static final String CONTROLMANAGER_CLASSNAME = "org.eclipse.ve.internal.swt.targetvm.ControlManager";	//$NON-NLS-1$

	public static final String CONTROLMANAGEREXTENSION_CLASSNAME = CONTROLMANAGER_CLASSNAME+"$ControlManagerExtension";	//$NON-NLS-1$

	public static final String CONTROLMANAGERFEEDBACK_CLASSNAME = CONTROLMANAGER_CLASSNAME+"$ControlManagerFeedbackController";	//$NON-NLS-1$

	public static final String COMPOSITEMANAGEREXTENSION_CLASSNAME = "org.eclipse.ve.internal.swt.targetvm.CompositeManagerExtension";	 //$NON-NLS-1$

    public static final String SHELLMANAGEREXTENSION_CLASSNAME = "org.eclipse.ve.internal.swt.targetvm.ShellManagerExtension";	//$NON-NLS-1$

    protected static BeanSWTUtilities getConstants(ProxyFactoryRegistry registry) {
        BeanSWTUtilities constants = (BeanSWTUtilities) registry.getConstants(REGISTRY_KEY);
        if (constants == null) registry.registerConstants(REGISTRY_KEY, constants = new BeanSWTUtilities());
        return constants;
    }

    protected static BeanSWTUtilities getConstants(IBeanProxy proxy) {
        return getConstants(proxy.getProxyFactoryRegistry());
    }
	
	
	/**
	 * Get the feedback controller for this display. Create it if necessary.
	 * 
	 * @param expression
	 * @param displayProxy proxy to the display.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static ControlManager.FeedbackController getFeedbackController(IExpression expression, final IBeanProxy displayProxy) {
		final BeanSWTUtilities constants = getConstants(expression.getRegistry());
		if (constants.controlManagerFeedbackController == null) {
			constants.controlManagerFeedbackController = new HashMap(1);
		}
		ControlManager.FeedbackController feedbackController;
		if ((feedbackController = (FeedbackController) constants.controlManagerFeedbackController.get(displayProxy)) == null) {
			ExpressionProxy feedbackProxy = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
			expression.createMethodInvocation(ForExpression.ASSIGNMENT_RIGHT, "createFeedbackController", true, 1);
			expression.createTypeReceiver(BeanSWTUtilities.CONTROLMANAGERFEEDBACK_CLASSNAME);
			expression.createProxyExpression(ForExpression.METHOD_ARGUMENT, displayProxy);
			constants.controlManagerFeedbackController.put(displayProxy, feedbackController = new ControlManager.FeedbackController(feedbackProxy));
			expression.getRegistry().getCallbackRegistry().registerCallback(feedbackProxy, feedbackController, expression);
			feedbackProxy.addProxyListener(new ExpressionProxy.ProxyListener() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyListener#proxyResolved(org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent)
				 */
				public void proxyResolved(ProxyEvent event) {
					// this is ok, no need to do anything.
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyListener#proxyNotResolved(org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent)
				 */
				public void proxyNotResolved(ProxyEvent event) {
					constants.controlManagerFeedbackController.remove(displayProxy); // It was bad, so set to get a new one next time.
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyListener#proxyVoid(org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent)
				 */
				public void proxyVoid(ProxyEvent event) {
					constants.controlManagerFeedbackController.remove(displayProxy); // It was bad, so set to get a new one next time.
				}
			});
		}
		return feedbackController;
	}

	/**
	 * Get the feedback controller for this display. It will not create one if not already created. Use
	 * {@link BeanSWTUtilities#getFeedbackController(IExpression, IBeanProxy)}to create one if necessary.
	 * 
	 * @param registry
	 * @param displayProxy
	 * @return feedback controller for this registry or <code>null</code> if none yet created.
	 * 
	 * @since 1.1.0
	 */
	public static ControlManager.FeedbackController getFeedbackController(ProxyFactoryRegistry registry, IBeanProxy displayProxy) {
		BeanSWTUtilities constants = getConstants(registry);
		return (FeedbackController) (constants.controlManagerFeedbackController != null ? constants.controlManagerFeedbackController.get(displayProxy) : null);
	}
	
	/**
	 * Get set control method proxy. <package-protected> because only ControlManager should access it.
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IProxyMethod getSetControlMethodProxy(IExpression expression) {
		BeanSWTUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_SET_CONTROL];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression
					.getRegistry()
					.getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, BeanSWTUtilities.CONTROLMANAGER_CLASSNAME).getMethodProxy( //$NON-NLS-1$
							expression,
							"setControl", //$NON-NLS-1$
							new String[] { "org.eclipse.swt.widgets.Control", BeanSWTUtilities.CONTROLMANAGERFEEDBACK_CLASSNAME} //$NON-NLS-1$ 
					);
			processExpressionProxy(method, constants.methods, MANAGER_SET_CONTROL);
		}
		return method;
	}
	
	/**
	 * Get the ControlManager.overrideLocation method using the expression.
	 * <package-protected> because only ControlManager should access it.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IProxyMethod getOverrideLocationMethodProxy(IExpression expression) {
		BeanSWTUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_OVERRIDE_LOCATION];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, BeanSWTUtilities.CONTROLMANAGER_CLASSNAME).getMethodProxy( //$NON-NLS-1$
							expression, "overrideLoc", //$NON-NLS-1$
							new String[] { "int", "int"}); //$NON-NLS-1$ //$NON-NLS-2$
			processExpressionProxy(method, constants.methods, MANAGER_OVERRIDE_LOCATION);
		}
		return method;
	}
	
	/**
	 * Apply bounds method proxy. <package-protected> because only ControlManager should access this.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IProxyMethod getApplyLayoutDataMethodProxy(IExpression expression) {
		BeanSWTUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_APPLY_LAYOUTDATA];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, BeanSWTUtilities.CONTROLMANAGER_CLASSNAME).getMethodProxy( //$NON-NLS-1$
							expression, "applyLayoutData", //$NON-NLS-1$
							new String[] { "java.lang.Object"}); //$NON-NLS-1$ //$NON-NLS-2$
			processExpressionProxy(method, constants.methods, MANAGER_APPLY_LAYOUTDATA);
		}
		return method;
	}
	
	/**
	 * Apply bounds method proxy. <package-protected> because only ControlManager should access this.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IProxyMethod getApplyBoundsMethodProxy(IExpression expression) {
		BeanSWTUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_APPLY_BOUNDS];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, BeanSWTUtilities.CONTROLMANAGER_CLASSNAME).getMethodProxy( //$NON-NLS-1$
							expression, "applyBounds", //$NON-NLS-1$
							new String[] {SWTConstants.RECTANGLE_CLASS_NAME, SWTConstants.RECTANGLE_CLASS_NAME});
			processExpressionProxy(method, constants.methods, MANAGER_APPLY_BOUNDS);
		}
		return method;
	}
	
	/**
	 * Get apply location method proxy. <package-protected> because only ControlManager should access this.
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IProxyMethod getApplyLocationMethodProxy(IExpression expression) {
		BeanSWTUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_APPLY_LOCATION];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, BeanSWTUtilities.CONTROLMANAGER_CLASSNAME).getMethodProxy(
							expression, "applyLocation", //$NON-NLS-1$
							new String[] { SWTConstants.POINT_CLASS_NAME, SWTConstants.POINT_CLASS_NAME}); 
			processExpressionProxy(method, constants.methods, MANAGER_APPLY_LOCATION);
		}
		return method;
	}

	/**
	 * Get ControlManager.getDefaultLocation method proxy. <package-protected> because only ControlManager should access it.
	 * 
	 * @param registry
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IMethodProxy getDefaultLocationMethodProxy(ProxyFactoryRegistry registry) {
		BeanSWTUtilities constants = getConstants(registry);

		if (constants.getManagerDefaultLocationMethodProxy == null) {
			constants.getManagerDefaultLocationMethodProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy(
					BeanSWTUtilities.CONTROLMANAGER_CLASSNAME).getMethodProxy( //$NON-NLS-1$
					"getDefaultLocation" //$NON-NLS-1$
			);
		}
		return constants.getManagerDefaultLocationMethodProxy;
	}
	
	/**
	 * Get ControlManager.getDefaultBounds method proxy. <package-protected> because only ControlManager should access it.
	 * 
	 * @param registry
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IMethodProxy getDefaultBoundsMethodProxy(ProxyFactoryRegistry registry) {
		BeanSWTUtilities constants = getConstants(registry);

		if (constants.getManagerDefaultBoundsMethodProxy == null) {
			constants.getManagerDefaultBoundsMethodProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy(
					BeanSWTUtilities.CONTROLMANAGER_CLASSNAME).getMethodProxy( //$NON-NLS-1$
					"getDefaultBounds" //$NON-NLS-1$
			);
		}
		return constants.getManagerDefaultBoundsMethodProxy;
	}
	
	/**
	 * Get the {@link ControlManager#invalidate() invalidate}proxy method.
	 * <package-protected> because only ControlManager should access it.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IProxyMethod getControlInvalidate(IExpression expression) {
		BeanSWTUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_INVALIDATE_CONTROL];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, BeanSWTUtilities.CONTROLMANAGER_CLASSNAME).getMethodProxy( //$NON-NLS-1$
							expression, "invalidate"); //$NON-NLS-1$
			processExpressionProxy(method, constants.methods, MANAGER_INVALIDATE_CONTROL);
		}
		return method;
	}
	
	/**
	 * Invoke the image capture abort method.
	 * <p>
	 * <package-protected> because only ImageDataCollector should call it.
	 * @param imageCaptureProxy
	 * @param wait
	 * @return
	 * 
	 * @since 1.1.0
	 */
    static boolean invoke_imageCaptureAbort(IBeanProxy imageCaptureProxy, boolean wait) {
        BeanSWTUtilities constants = getConstants(imageCaptureProxy);
        if (constants.imageCaptureAbortMethodProxy == null) {
            constants.imageCaptureAbortMethodProxy = imageCaptureProxy.getTypeProxy().getMethodProxy("abortImage", "boolean"); //$NON-NLS-1$ //$NON-NLS-2$
        }
		IBooleanBeanProxy r = (IBooleanBeanProxy) constants.imageCaptureAbortMethodProxy.invokeCatchThrowableExceptions(imageCaptureProxy, imageCaptureProxy.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(wait));
		return r != null ? r.booleanValue() : false; 
    }
	
	/**
	 * Invoke start image capture on a control.
	 * <p>
	 * <package-protected> because only ImageDataCollector should access it.
	 * @param imageCaptureProxy
	 * @param controlProxy
	 * @param maxWidth
	 * @param maxHeight
	 * @param abortAndwait
	 * @return
	 * 
	 * @since 1.1.0
	 */
    static IBooleanBeanProxy invoke_imageCaptureStartCapture(IBeanProxy imageCaptureProxy, IBeanProxy controlProxy, int maxWidth, int maxHeight, boolean abortAndwait) {
        BeanSWTUtilities constants = getConstants(imageCaptureProxy);
        if (constants.imageCaptureStartCaptureMethodProxy == null) {
            constants.imageCaptureStartCaptureMethodProxy = imageCaptureProxy.getTypeProxy().getMethodProxy("captureImage", new String[] {"org.eclipse.swt.widgets.Control", "int", "int", "boolean"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        }
		IStandardBeanProxyFactory beanProxyFactory = imageCaptureProxy.getProxyFactoryRegistry().getBeanProxyFactory();
		return (IBooleanBeanProxy) constants.imageCaptureStartCaptureMethodProxy.invokeCatchThrowableExceptions(imageCaptureProxy, new IBeanProxy[] {
				controlProxy,
				beanProxyFactory.createBeanProxyWith(maxWidth),
				beanProxyFactory.createBeanProxyWith(maxHeight),
				beanProxyFactory.createBeanProxyWith(abortAndwait)});
    }
	
	/**
	 * Return the {@link org.eclipse.ve.internal.swt.targetvm.CompositeManagerExtension#setVerifyLayoutData(Class)} method proxy.
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IProxyMethod getCompositeManagerExtension_LayoutVerify(IExpression expression) {
		BeanSWTUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[COMPOSITE_MANAGER_LAYOUT_VERIFY];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, BeanSWTUtilities.COMPOSITEMANAGEREXTENSION_CLASSNAME).getMethodProxy( 
							expression, "setVerifyLayoutData"); //$NON-NLS-1$
			processExpressionProxy(method, constants.methods, COMPOSITE_MANAGER_LAYOUT_VERIFY);
		}
		return method;
	}
	
	/**
	 * Return the {@link org.eclipse.ve.internal.swt.targetvm.ShellManagerExtension#applyShellTitle(Shell, String, boolean)} method proxy.
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IProxyMethod getShellApplyTitleMethodProxy(IExpression expression) {
		BeanSWTUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[SHELL_MANAGER_APPLYTITLE];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			IStandardBeanTypeProxyFactory beanTypeProxyFactory = expression.getRegistry().getBeanTypeProxyFactory();
			method = beanTypeProxyFactory.getBeanTypeProxy(expression, SHELLMANAGEREXTENSION_CLASSNAME).getMethodProxy(	//$NON-NLS-1$ 
							expression, "applyShellTitle", //$NON-NLS-1$
							new IProxyBeanType[] {beanTypeProxyFactory.getBeanTypeProxy(expression, "org.eclipse.swt.widgets.Shell"), beanTypeProxyFactory.getBeanTypeProxy(expression, "java.lang.String"), beanTypeProxyFactory.getBeanTypeProxy(expression, "boolean")}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$							
			processExpressionProxy(method, constants.methods, SHELL_MANAGER_APPLYTITLE);
		}
		return method;
	}
	
	/**
	 * Get the {@link org.eclipse.ve.internal.swt.targetvm.ShellManagerExtension#setPackOnChange(boolean) packOnChange}proxy method.
	 * <package-protected> because only ShellManager should access it.
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IProxyMethod getWindowPackOnChange(IExpression expression) {
		BeanSWTUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[SHELL_MANAGER_PACKONCHANGE];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, SHELLMANAGEREXTENSION_CLASSNAME).getMethodProxy( //$NON-NLS-1$
							expression, "setPackOnChange", //$NON-NLS-1$
							new String[] { "boolean"}); //$NON-NLS-1$
			processExpressionProxy(method, constants.methods, SHELL_MANAGER_PACKONCHANGE);
		}
		return method;
	}
	
	/**
	 * Get the {@link org.eclipse.ve.internal.swt.targetvm.ControlManager#disposeWidget(Widget)}proxy method.
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private static IProxyMethod getManagerDispose(IExpression expression) {
		BeanSWTUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_DISPOSE];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, CONTROLMANAGER_CLASSNAME).getMethodProxy(
							expression, "disposeWidget", //$NON-NLS-1$
							new String[] { "org.eclipse.swt.widgets.Widget"}); //$NON-NLS-1$
			processExpressionProxy(method, constants.methods, MANAGER_DISPOSE);
		}
		return method;
	}

	/**
	 * Invoke the widget dispose method.
	 * @param widget
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	public static void invoke_WidgetDispose(IProxy widget, IExpression expression) {
		IProxyMethod dispose = getManagerDispose(expression);
		expression.createSimpleMethodInvoke(dispose, null, new IProxy[] {widget}, false);
	}

	/**
	 * Invoke get all column rectangles for a table.
	 * @param table
	 * @return 5-tuple array (TableColumnProxy, xIntegerProxy, yIntegerProxy, widthIntegerProxy, heightIntegerProxy, ....)
	 * or <code>null</code> if no columns or not visible
	 * 
	 * @see org.eclipse.ve.internal.swt.targetvm.TableManager#getColumnRects(Table)
	 * @since 1.1.0
	 */
	public static IArrayBeanProxy invoke_Table_getAllColumnRects(IBeanProxy table) {
		BeanSWTUtilities constants = getConstants(table);

		if (constants.managerTableGetAllColumnRects == null) {
			constants.managerTableGetAllColumnRects = table.getProxyFactoryRegistry().getBeanTypeProxyFactory()
			.getBeanTypeProxy("org.eclipse.ve.internal.swt.targetvm.TableManager").getMethodProxy( //$NON-NLS-1$
					"getColumnRects", //$NON-NLS-1$
					new String[] {"org.eclipse.swt.widgets.Table"}); //$NON-NLS-1$
		}
		return (IArrayBeanProxy) constants.managerTableGetAllColumnRects.invokeCatchThrowableExceptions(null, new IBeanProxy[] {table});
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
            return (IBeanProxy) JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(aControlProxy.getProxyFactoryRegistry(),
                    new DisplayManager.DisplayRunnable() {

                        public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
                            return getParentMethodProxy.invoke(aControlProxy);
                        }
                    });
        }
    	return null;
    }
	
    private Point offscreenLocation;
    
	/**
	 * Get the offscreen location for windows.
	 * @param registry TODO
	 * @param registry
	 * @return
	 * 
	 * @since 1.1.0.1
	 */
	public static Point getOffScreenLocation(ProxyFactoryRegistry registry) {
		if (VCEPreferences.isLiveWindowOn())
			return new Point(0, 0);
		else {
			BeanSWTUtilities constants = getConstants(registry);
			if (constants.offscreenLocation == null) {
				IBeanProxy env = JavaStandardSWTBeanConstants.getConstants(registry).getEnvironmentProxy();
				IBeanProxy p = env.getTypeProxy().getMethodProxy("getOffScreenLocation", (String[]) null).invokeCatchThrowableExceptions(env);
				if (p instanceof IPointBeanProxy) {
					IPointBeanProxy pb = (IPointBeanProxy) p;
					constants.offscreenLocation = new Point(pb.getX(), pb.getY());
				} else
					constants.offscreenLocation = new Point(10000, 10000);
			}
			return constants.offscreenLocation;
		}
	}
    /**
     * Set the selection on the TabFolder which brings the respective tab to the front.
     */
    public static void invoke_tabfolder_setSelection(final IBeanProxy aTabFolderBeanProxy, final IBeanProxy tabItemProxy) {
    	final BeanSWTUtilities constants = getConstants(aTabFolderBeanProxy);
    	
    	if (constants.setTabfolderSelectionMethodProxy == null) {
    		constants.setTabfolderSelectionMethodProxy = aTabFolderBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.widgets.TabFolder").getMethodProxy( //$NON-NLS-1$
    			"setSelection", "int"); //$NON-NLS-1$ //$NON-NLS-2$
    	}
    	if (constants.indexOfTabITemMethodProxy == null) {
    		constants.indexOfTabITemMethodProxy = aTabFolderBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.widgets.TabFolder").getMethodProxy( //$NON-NLS-1$
    			"indexOf", "org.eclipse.swt.widgets.TabItem"); //$NON-NLS-1$ //$NON-NLS-2$
    	}    	
        if (constants.setTabfolderSelectionMethodProxy != null && constants.indexOfTabITemMethodProxy != null) {
            JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(aTabFolderBeanProxy.getProxyFactoryRegistry(),
                    new DisplayManager.DisplayRunnable() {

                        public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
                        	IBeanProxy index = constants.indexOfTabITemMethodProxy.invoke(aTabFolderBeanProxy, tabItemProxy);
                            return constants.setTabfolderSelectionMethodProxy.invoke(aTabFolderBeanProxy, index);
                        }
                    });
        }
    }
	
    /**
     * Set the selection on the CTabFolder which brings the respective tab to the front.
     */
    public static void invoke_ctabfolder_setSelection(final IBeanProxy aCTabFolderBeanProxy, final IBeanProxy ctabItemBeanProxy) {
    	BeanSWTUtilities constants = getConstants(aCTabFolderBeanProxy);
    	
    	if (constants.setCTabfolderSelectionMethodProxy == null) {
    		constants.setCTabfolderSelectionMethodProxy = aCTabFolderBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.custom.CTabFolder").getMethodProxy( //$NON-NLS-1$
    			"setSelection", "org.eclipse.swt.custom.CTabItem"); //$NON-NLS-1$ //$NON-NLS-2$
    	}
        if (constants.setCTabfolderSelectionMethodProxy != null) {
        	final IMethodProxy setCTabfolderSelectionMethodProxy = constants.setCTabfolderSelectionMethodProxy;
            JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(aCTabFolderBeanProxy.getProxyFactoryRegistry(),
                    new DisplayManager.DisplayRunnable() {

                        public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
                            return setCTabfolderSelectionMethodProxy.invoke(aCTabFolderBeanProxy, ctabItemBeanProxy);
                        }
                    });
        }
    }
    
    /**
     * Get the index of the CTabItem from the CTabFolder at the given location.
     * 
     * @since 1.1.0.1
     */
    public static int invoke_ctabfolder_getItemFromLocation(final IBeanProxy aCTabFolderBeanProxy, final IBeanProxy pointBeanProxy) {
    	BeanSWTUtilities constants = getConstants(aCTabFolderBeanProxy);
    	int retVal = -1;
    	
    	if (constants.setCTabFolderSelectionAtLocationMethodProxy == null) {
    		constants.setCTabFolderSelectionAtLocationMethodProxy = aCTabFolderBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.custom.CTabFolder").getMethodProxy( //$NON-NLS-1$
    			"getItem", "org.eclipse.swt.graphics.Point"); //$NON-NLS-1$ //$NON-NLS-2$
    	}
    	if (constants.indexOfCTabItemMethodProxy == null) {
    		constants.indexOfCTabItemMethodProxy = aCTabFolderBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.swt.custom.CTabFolder").getMethodProxy( //$NON-NLS-1$
    			"indexOf", "org.eclipse.swt.custom.CTabItem"); //$NON-NLS-1$ //$NON-NLS-2$
    	} 

        if (constants.setCTabFolderSelectionAtLocationMethodProxy != null && constants.indexOfCTabItemMethodProxy != null) {
        	final IMethodProxy getItemAtLocationMethodProxy = constants.setCTabFolderSelectionAtLocationMethodProxy;
        	final IMethodProxy indexOfCTabItemMethodProxy = constants.indexOfCTabItemMethodProxy;
        	
            IBeanProxy pageNum = (IBeanProxy) JavaStandardSWTBeanConstants.invokeSyncExecCatchThrowableExceptions(aCTabFolderBeanProxy.getProxyFactoryRegistry(),
                    new DisplayManager.DisplayRunnable() {

                        public Object run(IBeanProxy displayProxy) throws ThrowableProxy {
                            IBeanProxy cTabItemBeanProxy = getItemAtLocationMethodProxy.invoke(aCTabFolderBeanProxy, pointBeanProxy);
                            
                            if(cTabItemBeanProxy == null)
                            	return cTabItemBeanProxy;

                            return indexOfCTabItemMethodProxy.invoke(aCTabFolderBeanProxy,cTabItemBeanProxy);
                        }
                    });
            if(pageNum != null && pageNum instanceof INumberBeanProxy)
            	retVal = ((INumberBeanProxy) pageNum).intValue();
        }
        return retVal;
    }

	public static boolean isValidBeanLocation(EditDomain domain, EObject childComponent, EObject targetContainer) {
		try{
			if(((IJavaInstance)childComponent).getAllocation() instanceof ImplicitAllocation){
				return false;
			}
		} catch (ClassCastException e){	
		}
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
		if(parent == null) return false;
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
