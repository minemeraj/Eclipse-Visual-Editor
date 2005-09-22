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
 * $RCSfile: BeanAwtUtilities.java,v $ $Revision: 1.39 $ $Date: 2005-09-22 12:55:58 $
 */

import java.awt.Component;
import java.util.List;
import java.util.logging.Level;

import javax.swing.*;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.PreferencesUtil;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.vce.VCEPreferences;
import org.eclipse.ve.internal.java.visual.*;

/**
 * Helper class with useful methods for working with awt bean proxies
 */
public class BeanAwtUtilities {

	public static final String WINDOWMANAGEREXTENSION_CLASSNAME = "org.eclipse.ve.internal.jfc.vm.WindowManagerExtension"; //$NON-NLS-1$

	public static final String COMPONENTMANAGER_CLASSNAME = "org.eclipse.ve.internal.jfc.vm.ComponentManager"; //$NON-NLS-1$

	public static final String FEEDBACKCONTROLLER_CLASSNAME = COMPONENTMANAGER_CLASSNAME + "$ComponentManagerFeedbackController"; //$NON-NLS-1$

	public static final String COMPONENTMANAGEREXTENSION_CLASSNAME = COMPONENTMANAGER_CLASSNAME + "$ComponentManagerExtension"; //$NON-NLS-1$

	private static LayoutList DEFAULT_LAYOUTLIST;

	private Point offscreenLocation;

	/**
	 * Get the offscreen location for windows.
	 * 
	 * @param registry
	 * @return
	 * 
	 * @since 1.1.0.1
	 */
	public static Point getOffScreenLocation(ProxyFactoryRegistry registry) {
		if (VCEPreferences.isLiveWindowOn())
			return new Point(0, 0);
		else {
			BeanAwtUtilities constants = getConstants(registry);
			if (constants.offscreenLocation == null) {
				IBeanProxy p = registry.getMethodProxyFactory().getMethodProxy("org.eclipse.ve.internal.jfc.vm.FreeFormAWTDialog",
						"getOffScreenLocation", null).invokeCatchThrowableExceptions(null);
				if (p instanceof IPointBeanProxy) {
					IPointBeanProxy pb = (IPointBeanProxy) p;
					constants.offscreenLocation = new Point(pb.getX(), pb.getY());
				} else
					constants.offscreenLocation = new Point(10000, 10000);
			}
			return constants.offscreenLocation;
		}
	}

	// JCMMethod proxies are cached in a registry constants.
	private IMethodProxy getLayoutMethodProxy, getBoundsMethodProxy, getLocationMethodProxy, getSizeMethodProxy, getPreferredSizeMethodProxy,
			getParentMethodProxy, getComponentsMethodProxy, getManagerDefaultLocationMethodProxy, getManagerDefaultBoundsMethodProxy,
			getTabSelectedComponentMethodProxy, setTabSelectedComponentMethodProxy, indexOfTabAtLocationMethodProxy, managerJTableGetAllColumnRects;

	private IBeanProxy jFrameDefaultOnClose_DoNothingProxy;

	private IFieldProxy getBoxLayoutAxisFieldProxy;

	private IBeanProxy boxLayoutAxis_XAXIS, jsplitpaneOrientation_HORIZONTAL;

	private ComponentManager.FeedbackController componentManagerFeedbackController;

	// methods that could be retrieved from expressions are complicated because we need
	// to add a listener to them to put in the final resolved value when received. But
	// we can only do this once. Don't want to add a listener each time we go get the
	// expression proxy. Doing this is complicated so we created a helper method. But
	// to do this efficiently (since we can't pass pointers fields except through
	// reflection and that's overkill), we will use an array and constants instead.

	private static final int MANAGER_INVALIDATE_COMPONENT = 0, MANAGER_ADD_COMPONENT_BEFORE_WITH_CONSTRAINT = MANAGER_INVALIDATE_COMPONENT + 1,
			MANAGER_ADD_COMPONENT_BEFORE_WITH_NO_CONSTRAINT = MANAGER_ADD_COMPONENT_BEFORE_WITH_CONSTRAINT + 1,
			MANAGER_CHANGE_CONSTRAINT = MANAGER_ADD_COMPONENT_BEFORE_WITH_NO_CONSTRAINT + 1,
			MANAGER_REMOVE_COMPONENT = MANAGER_CHANGE_CONSTRAINT + 1, MANAGER_SET_COMPONENT = MANAGER_REMOVE_COMPONENT + 1,
			MANAGER_APPLY_BOUNDS = MANAGER_SET_COMPONENT + 1, MANAGER_APPLY_LOCATION = MANAGER_APPLY_BOUNDS + 1,
			MANAGER_OVERRIDE_LOCATION = MANAGER_APPLY_LOCATION + 1, WINDOW_DISPOSE = MANAGER_OVERRIDE_LOCATION + 1,
			COMPONENT_GET_PARENT = WINDOW_DISPOSE + 1, MANAGER_JMENU_REMOVE_COMPONENT = COMPONENT_GET_PARENT + 1,
			MANAGER_JMENU_ADD_COMPONENT = MANAGER_JMENU_REMOVE_COMPONENT + 1, MANAGER_WINDOW_PACK_ON_CHANGE = MANAGER_JMENU_ADD_COMPONENT + 1,
			MANAGER_JTABLE_ADD_COLUMN_BEFORE = MANAGER_WINDOW_PACK_ON_CHANGE + 1,
			MANAGER_JTABLE_INITIALIZE_TABLE_MODEL = MANAGER_JTABLE_ADD_COLUMN_BEFORE + 1,
			MANAGER_JTABLE_REMOVE_ALL_COLUMNS = MANAGER_JTABLE_INITIALIZE_TABLE_MODEL + 1,
			JTABLE_REMOVE_COLUMN = MANAGER_JTABLE_REMOVE_ALL_COLUMNS + 1, MANAGER_JTOOLBAR_ADD_COMPONENT = JTABLE_REMOVE_COLUMN + 1,
			MANAGER_JTOOLBAR_REMOVE_COMPONENT = MANAGER_JTOOLBAR_ADD_COMPONENT + 1,
			MANAGER_JTABBEDPANE_SETDEFAULTTITLE = MANAGER_JTOOLBAR_REMOVE_COMPONENT + 1,
			MANAGER_JTABBEDPANE_SETTABICON = MANAGER_JTABBEDPANE_SETDEFAULTTITLE + 1,
			MANAGER_JTABBEDPANE_SETTABTITLE = MANAGER_JTABBEDPANE_SETTABICON + 1,
			MANAGER_JTABBEDPANE_INSERTTAB = MANAGER_JTABBEDPANE_SETTABTITLE + 1,
			MANAGER_JTABBEDPANE_INSERTTAB_DEFAULT = MANAGER_JTABBEDPANE_INSERTTAB + 1,
			MANAGER_JSPLITPANE_SETDIVIDERLOCATION = MANAGER_JTABBEDPANE_INSERTTAB_DEFAULT + 1,
			WINDOW_APPLYTITLE = MANAGER_JSPLITPANE_SETDIVIDERLOCATION + 1, SCROLLPANE_MAKE_IT_RIGHT = WINDOW_APPLYTITLE + 1,
			MAX_METHODS = SCROLLPANE_MAKE_IT_RIGHT + 1;

	private IProxyMethod[] methods = new IProxyMethod[MAX_METHODS];

	/**
	 * Called by the get methods for expressions after retrieving the IProxyMethod from the expression. It is used to add to the methods array and to
	 * put the resolved value in the methods array when it is resolved.
	 * 
	 * @param method
	 * @param methods
	 * @param methodID
	 * 
	 * @see BeanAwtUtilities#getComponentInvalidate(IExpression) for an example of how to use this.
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

	/**
	 * Get the constants given a registry.
	 * 
	 * @param registry
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected static BeanAwtUtilities getConstants(ProxyFactoryRegistry registry) {
		BeanAwtUtilities constants = (BeanAwtUtilities) registry.getConstants(REGISTRY_KEY);
		if (constants == null)
			registry.registerConstants(REGISTRY_KEY, constants = new BeanAwtUtilities());
		return constants;
	}

	/**
	 * Get the constants given a bean proxy.
	 * 
	 * @param proxy
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected static BeanAwtUtilities getConstants(IBeanProxy proxy) {
		return getConstants(proxy.getProxyFactoryRegistry());
	}

	/**
	 * Get the feedback controller for this registry.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static ComponentManager.FeedbackController getFeedbackController(IExpression expression) {
		final BeanAwtUtilities constants = getConstants(expression.getRegistry());
		if (constants.componentManagerFeedbackController == null) {
			ExpressionProxy feedbackProxy = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
			expression.createClassInstanceCreation(ForExpression.ASSIGNMENT_RIGHT, FEEDBACKCONTROLLER_CLASSNAME, 0); //$NON-NLS-1$
			constants.componentManagerFeedbackController = new ComponentManager.FeedbackController(feedbackProxy);
			expression.getRegistry().getCallbackRegistry().registerCallback(feedbackProxy, constants.componentManagerFeedbackController, expression);
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
					constants.componentManagerFeedbackController = null; // It was bad, so set to get a new one next time.
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyListener#proxyVoid(org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent)
				 */
				public void proxyVoid(ProxyEvent event) {
					constants.componentManagerFeedbackController = null; // It was bad, so set to get a new one next time.
				}
			});
		}
		return constants.componentManagerFeedbackController;
	}

	/**
	 * Get the feedback controller for this registry. It will not create one if not already created. Use
	 * {@link BeanAwtUtilities#getFeedbackController(IExpression) getFeedBackControllerWithExpression}to create one if necessary.
	 * 
	 * @param registry
	 * @return feedback controller for this registry or <code>null</code> if none yet created.
	 * 
	 * @since 1.1.0
	 */
	public static ComponentManager.FeedbackController getFeedbackController(ProxyFactoryRegistry registry) {
		BeanAwtUtilities constants = getConstants(registry);
		return constants.componentManagerFeedbackController;
	}

	/**
	 * Get the value of "javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE" as a bean proxy.
	 * 
	 * @param registry
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static IBeanProxy getJFrame_DefaultOnClose_DoNothing(ProxyFactoryRegistry registry) {
		BeanAwtUtilities constants = getConstants(registry);
		if (constants.jFrameDefaultOnClose_DoNothingProxy == null) {
			try {
				constants.jFrameDefaultOnClose_DoNothingProxy = registry.getBeanProxyFactory().createBeanProxyFrom(
						"javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE"); //$NON-NLS-1$
			} catch (ThrowableProxy e) {
				JavaVEPlugin.log(e, Level.WARNING);
			} catch (ClassCastException e) {
				JavaVEPlugin.log(e, Level.WARNING);
			} catch (InstantiationException e) {
				JavaVEPlugin.log(e, Level.WARNING);
			}
		}
		return constants.jFrameDefaultOnClose_DoNothingProxy;
	}

	/**
	 * Get the JSplitPane horizontal orientation bean proxy.
	 * 
	 * @param registry
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static IBeanProxy getJSplitPaneOrientationHorizontal(ProxyFactoryRegistry registry) {
		BeanAwtUtilities constants = getConstants(registry);

		if (constants.jsplitpaneOrientation_HORIZONTAL == null) {
			try {
				constants.jsplitpaneOrientation_HORIZONTAL = registry.getBeanTypeProxyFactory().getBeanTypeProxy("javax.swing.JSplitPane") //$NON-NLS-1$
						.getFieldProxy("HORIZONTAL_SPLIT").get(null); //$NON-NLS-1$
			} catch (ThrowableProxy e) {
				JavaVEPlugin.log(e, Level.WARNING);
			}
		}

		return constants.jsplitpaneOrientation_HORIZONTAL;
	}

	/**
	 * Get the {@link org.eclipse.ve.internal.jfc.vm.ComponentManager#invalidate() invalidate}proxy method. <package-protected> because only
	 * ComponentManager should access it.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IProxyMethod getComponentInvalidate(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_INVALIDATE_COMPONENT];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression, COMPONENTMANAGER_CLASSNAME).getMethodProxy( //$NON-NLS-1$
					expression, "invalidate"); //$NON-NLS-1$
			processExpressionProxy(method, constants.methods, MANAGER_INVALIDATE_COMPONENT);
		}
		return method;
	}

	/**
	 * Get the {@link org.eclipse.ve.internal.jfc.vm.ContainerManager#addComponentBefore(Component, Object, Component) addComponentWithConstraint}
	 * proxy method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private static IProxyMethod getAddComponentWithConstraint(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_ADD_COMPONENT_BEFORE_WITH_CONSTRAINT];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression,
					"org.eclipse.ve.internal.jfc.vm.ContainerManager").getMethodProxy( //$NON-NLS-1$
					expression, "addComponentBefore", //$NON-NLS-1$
					new String[] { "java.awt.Container", "java.awt.Component", "java.lang.Object", "java.awt.Component"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			processExpressionProxy(method, constants.methods, MANAGER_ADD_COMPONENT_BEFORE_WITH_CONSTRAINT);
		}
		return method;
	}

	/**
	 * Invoke ContainerManager.addComponent with constraint command.
	 * 
	 * @param container
	 * @param component
	 * @param beforeComponent
	 * @param constraint
	 * @param expression
	 * 
	 * @see org.eclipse.ve.internal.jfc.vm.ContainerManager#addComponentBefore(java.awt.Container, java.awt.Component, Object, java.awt.Component)
	 * @since 1.1.0
	 */
	public static void invoke_addComponent(IProxy container, IProxy component, IProxy beforeComponent, IProxy constraint, IExpression expression) {
		expression.createSimpleMethodInvoke(getAddComponentWithConstraint(expression), null, new IProxy[] { container, component, constraint,
				beforeComponent}, false);
	}

	/**
	 * Get the
	 * {@link org.eclipse.ve.internal.jfc.vm.ContainerManager#addComponentBefore(java.awt.Component, java.awt.Component, boolean) addComponentBeforeNoExplicitConstraint}
	 * proxy method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private static IProxyMethod getAddComponentBefore(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_ADD_COMPONENT_BEFORE_WITH_NO_CONSTRAINT];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression,
					"org.eclipse.ve.internal.jfc.vm.ContainerManager").getMethodProxy( //$NON-NLS-1$
					expression, "addComponentBefore", //$NON-NLS-1$
					new String[] { "java.awt.Container", "java.awt.Component", "java.awt.Component", "boolean"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			processExpressionProxy(method, constants.methods, MANAGER_ADD_COMPONENT_BEFORE_WITH_NO_CONSTRAINT);
		}
		return method;
	}

	/**
	 * Invoke the add the component before the given component, using no constraint or the default constraint (component.getName()).
	 * 
	 * @param container
	 * @param component
	 * @param beforeComponent
	 *            beforeComponent, if <code>null</code> it will add to the end.
	 * @param useDefaultConstraint
	 *            <code>true</code> means use <code>component.getName()</code> for the constraint, or <code>false</code> means no constraint at
	 *            all.
	 * @param expression
	 * 
	 * @see org.eclipse.ve.internal.jfc.vm.ContainerManager#addComponentBefore(java.awt.Container, java.awt.Component, java.awt.Component, boolean)
	 * @since 1.1.0
	 */
	public static void invoke_addComponent(IProxy container, IProxy component, IProxy beforeComponent, boolean useDefaultConstraint,
			IExpression expression) {
		expression.createSimpleMethodInvoke(getAddComponentBefore(expression), null, new IProxy[] { container, component, beforeComponent,
				expression.getRegistry().getBeanProxyFactory().createBeanProxyWith(useDefaultConstraint)}, false);
	}

	/**
	 * Get the
	 * {@link org.eclipse.ve.internal.jfc.vm.ContainerManager#addComponentBefore(java.awt.Component, java.awt.Component, boolean) addComponentBeforeNoExplicitConstraint}
	 * proxy method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private static IProxyMethod getChangeConstraint(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_CHANGE_CONSTRAINT];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression,
					"org.eclipse.ve.internal.jfc.vm.ContainerManager").getMethodProxy( //$NON-NLS-1$
					expression, "changeConstraint", //$NON-NLS-1$
					new String[] { "java.awt.Container", "java.awt.Component", "java.lang.Object", "boolean"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
			processExpressionProxy(method, constants.methods, MANAGER_CHANGE_CONSTRAINT);
		}
		return method;
	}

	/**
	 * Invoke the ContainerManager.changeConstraint command.
	 * 
	 * @param container
	 * @param component
	 * @param constraint
	 * @param useDefaultConstraint
	 * @param expression
	 * 
	 * @see org.eclipse.ve.internal.jfc.vm.ContainerManager#changeConstraint(java.awt.Container, java.awt.Component, Object, boolean)
	 * @since 1.1.0
	 */
	public static void invoke_changeConstraint(IProxy container, IProxy component, IProxy constraint, boolean useDefaultConstraint,
			IExpression expression) {
		expression.createSimpleMethodInvoke(getChangeConstraint(expression), null, new IProxy[] { container, component, constraint,
				expression.getRegistry().getBeanProxyFactory().createBeanProxyWith(useDefaultConstraint)}, false);
	}

	/**
	 * Get the {@link org.eclipse.ve.internal.jfc.vm.ContainerManager#removeComponent(java.awt.Component) removeComponent}proxy method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private static IProxyMethod getRemoveComponent(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_REMOVE_COMPONENT];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression,
					"org.eclipse.ve.internal.jfc.vm.ContainerManager").getMethodProxy( //$NON-NLS-1$
					expression, "removeComponent", //$NON-NLS-1$
					new String[] { "java.awt.Container", "java.awt.Component"}); //$NON-NLS-1$ //$NON-NLS-2$
			processExpressionProxy(method, constants.methods, MANAGER_REMOVE_COMPONENT);
		}
		return method;
	}

	/**
	 * Remove the component from the container.
	 * 
	 * @param component
	 * @param expression
	 * 
	 * @see org.eclipse.ve.internal.jfc.vm.ContainerManager#removeComponent(java.awt.Container, java.awt.Component)
	 * @since 1.1.0
	 */
	public static void invoke_removeComponent(IProxy container, IProxy component, IExpression expression) {
		expression.createSimpleMethodInvoke(getRemoveComponent(expression), null, new IProxy[] { container, component}, false);
	}

	/**
	 * Get the window dispose method proxy. It is {@link org.eclipse.ve.internal.jfc.vm.WindowManager#disposeWindow(java.awt.Window)}.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static IProxyMethod getWindowDisposeMethodProxy(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[WINDOW_DISPOSE];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			IStandardBeanTypeProxyFactory beanTypeProxyFactory = expression.getRegistry().getBeanTypeProxyFactory();
			method = beanTypeProxyFactory.getBeanTypeProxy(expression, WINDOWMANAGEREXTENSION_CLASSNAME).getMethodProxy( //$NON-NLS-1$
					expression, "disposeWindow", //$NON-NLS-1$
					new IProxyBeanType[] { beanTypeProxyFactory.getBeanTypeProxy(expression, "java.awt.Window")}); //$NON-NLS-1$
			processExpressionProxy(method, constants.methods, WINDOW_DISPOSE);
		}
		return method;
	}

	/**
	 * Get the window manager apply frame title proxy. It is
	 * {@link org.eclipse.ve.internal.jfc.vm.WindowManagerExtension#applyFrameTitle(java.awt.Frame, String, boolean)}.
	 * <p>
	 * <package-protected> because only FrameProxyAdapter should access it.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IProxyMethod getWindowApplyFrameTitleMethodProxy(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[WINDOW_APPLYTITLE];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			IStandardBeanTypeProxyFactory beanTypeProxyFactory = expression.getRegistry().getBeanTypeProxyFactory();
			method = beanTypeProxyFactory
					.getBeanTypeProxy(expression, WINDOWMANAGEREXTENSION_CLASSNAME)
					.getMethodProxy(
							//$NON-NLS-1$
							expression,
							"applyFrameTitle", //$NON-NLS-1$
							new IProxyBeanType[] {
									beanTypeProxyFactory.getBeanTypeProxy(expression, "java.awt.Frame"), beanTypeProxyFactory.getBeanTypeProxy(expression, "java.lang.String"), beanTypeProxyFactory.getBeanTypeProxy(expression, "boolean")}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			processExpressionProxy(method, constants.methods, WINDOW_APPLYTITLE);
		}
		return method;
	}

	/**
	 * Get the awt.Component.getParent() proxy method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static IProxyMethod getParentMethodProxy(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[COMPONENT_GET_PARENT];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression, "java.awt.Component").getMethodProxy( //$NON-NLS-1$
					expression, "getParent"); //$NON-NLS-1$
		}
		return method;
	}

	/**
	 * Get the {@link org.eclipse.ve.internal.jfc.vm.WindowManager#setPackOnChange(boolean) packOnChange}proxy method. <package-protected> because
	 * only WindowManager should access it.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IProxyMethod getWindowPackOnChange(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_WINDOW_PACK_ON_CHANGE];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression, WINDOWMANAGEREXTENSION_CLASSNAME)
					.getMethodProxy( //$NON-NLS-1$
							expression, "setPackOnChange", //$NON-NLS-1$
							new String[] { "boolean"}); //$NON-NLS-1$
			processExpressionProxy(method, constants.methods, MANAGER_WINDOW_PACK_ON_CHANGE);
		}
		return method;
	}

	/**
	 * Get the
	 * {@link org.eclipse.ve.internal.jfc.vm.JTableManager#addColumnBefore(JTable, javax.swing.table.TableColumn, javax.swing.table.TableColumn) addComponentBefore}proxy
	 * method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static IProxyMethod getJTableAddColumnBefore(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_JTABLE_ADD_COLUMN_BEFORE];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, "org.eclipse.ve.internal.jfc.vm.JTableManager").getMethodProxy( //$NON-NLS-1$
							expression, "addColumnBefore", //$NON-NLS-1$
							new String[] { "javax.swing.JTable", "javax.swing.table.TableColumn", "javax.swing.table.TableColumn"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			processExpressionProxy(method, constants.methods, MANAGER_JTABLE_ADD_COLUMN_BEFORE);
		}
		return method;
	}

	/**
	 * Get the {@link org.eclipse.ve.internal.jfc.vm.JTableManager#initializeTableModel(JTable) initializeTableModel}proxy method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static IProxyMethod getJTableInitializeTableModel(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_JTABLE_INITIALIZE_TABLE_MODEL];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, "org.eclipse.ve.internal.jfc.vm.JTableManager").getMethodProxy( //$NON-NLS-1$
							expression, "initializeTableModel", //$NON-NLS-1$
							new String[] { "javax.swing.JTable"}); //$NON-NLS-1$
			processExpressionProxy(method, constants.methods, MANAGER_JTABLE_INITIALIZE_TABLE_MODEL);
		}
		return method;
	}

	/**
	 * Get the {@link org.eclipse.ve.internal.jfc.vm.JTableManager#removeAllColumns(JTable) removeAllColumns}proxy method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static IProxyMethod getJTableRemoveAllColumns(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_JTABLE_REMOVE_ALL_COLUMNS];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, "org.eclipse.ve.internal.jfc.vm.JTableManager").getMethodProxy( //$NON-NLS-1$
							expression, "removeAllColumns", //$NON-NLS-1$
							new String[] { "javax.swing.JTable"}); //$NON-NLS-1$
			processExpressionProxy(method, constants.methods, MANAGER_JTABLE_REMOVE_ALL_COLUMNS);
		}
		return method;
	}

	/**
	 * Get the {@link javax.swing.JTable#removeColumn(javax.swing.table.TableColumn) removeColumn}proxy method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static IProxyMethod getJTableRemoveColumn(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[JTABLE_REMOVE_COLUMN];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression, "javax.swing.JTable").getMethodProxy( //$NON-NLS-1$
					expression, "removeColumn", //$NON-NLS-1$
					new String[] { "javax.swing.table.TableColumn"}); //$NON-NLS-1$
			processExpressionProxy(method, constants.methods, JTABLE_REMOVE_COLUMN);
		}
		return method;
	}

	/**
	 * Invoke get all column rectangles for a table.
	 * 
	 * @param jtable
	 * @return 5-tuple array (TableColumnProxy, xIntegerProxy, yIntegerProxy, widthIntegerProxy, heightIntegerProxy, ....) or <code>null</code> if
	 *         no columns.
	 * 
	 * @see org.eclipse.ve.internal.jfc.vm.JTableManager#getColumnRects(JTable)
	 * @since 1.1.0
	 */
	public static IArrayBeanProxy invoke_JTable_getAllColumnRects(IBeanProxy jtable) {
		BeanAwtUtilities constants = getConstants(jtable);

		if (constants.managerJTableGetAllColumnRects == null) {
			constants.managerJTableGetAllColumnRects = jtable.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(
					"org.eclipse.ve.internal.jfc.vm.JTableManager").getMethodProxy( //$NON-NLS-1$
					"getColumnRects", //$NON-NLS-1$
					new String[] { "javax.swing.JTable"}); //$NON-NLS-1$
		}
		return (IArrayBeanProxy) constants.managerJTableGetAllColumnRects.invokeCatchThrowableExceptions(null, new IBeanProxy[] { jtable});
	}

	/**
	 * Get the {@link org.eclipse.ve.internal.jfc.vm.JMenuManager#removeComponent(Object) removeComponent}proxy method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private static IProxyMethod getJMenuRemoveComponent(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_JMENU_REMOVE_COMPONENT];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, "org.eclipse.ve.internal.jfc.vm.JMenuManager").getMethodProxy( //$NON-NLS-1$
							expression, "removeComponent", //$NON-NLS-1$
							new String[] { "java.lang.Object"}); //$NON-NLS-1$
			processExpressionProxy(method, constants.methods, MANAGER_JMENU_REMOVE_COMPONENT);
		}
		return method;
	}

	/**
	 * Invoke jmenu remove component (either JMenu or JPopupMenu)
	 * 
	 * @param container
	 *            JMenu or JPopupMenu to remove component from.
	 * @param component
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	public static void invoke_JMenu_removeComponent(IProxy container, IProxy component, IExpression expression) {
		expression.createSimpleMethodInvoke(BeanAwtUtilities.getJMenuRemoveComponent(expression), null, new IProxy[] { container, component}, false);
	}

	/**
	 * Get the {@link org.eclipse.ve.internal.jfc.vm.JMenuManager#addComponent(Object, Object) addComponent}proxy method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private static IProxyMethod getJMenuAddComponent(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_JMENU_ADD_COMPONENT];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, "org.eclipse.ve.internal.jfc.vm.JMenuManager").getMethodProxy( //$NON-NLS-1$
							expression, "addComponent", //$NON-NLS-1$
							new String[] { "java.awt.Container", "java.lang.Object", "java.lang.Object"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			processExpressionProxy(method, constants.methods, MANAGER_JMENU_ADD_COMPONENT);
		}
		return method;
	}

	/**
	 * Add component to JMenu/JPopupMenu
	 * 
	 * @param container
	 *            container to add to. Must be either a JMenu or a JPopupMenu.
	 * @param component
	 *            component to add, may be a Component, String, or Action
	 * @param beforeComponent
	 *            before component, may be a Component, String, or Action, or <code>null</code> for at end
	 * @param expression
	 * 
	 * @see org.eclipse.ve.internal.jfc.vm.JMenuManager#addComponent(java.awt.Container, Object, Object)
	 * @since 1.1.0
	 */
	public static void invoke_JMenu_addComponent(IProxy container, IProxy component, IProxy beforeComponent, IExpression expression) {
		expression.createSimpleMethodInvoke(BeanAwtUtilities.getJMenuAddComponent(expression), null, new IProxy[] { container, component,
				beforeComponent}, false);
	}

	/**
	 * Get the {@link org.eclipse.ve.internal.jfc.vm.JToolBarManager#addComponent(Object, Object) addComponent}proxy method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private static IProxyMethod getJToolBarAddComponent(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_JTOOLBAR_ADD_COMPONENT];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, "org.eclipse.ve.internal.jfc.vm.JToolBarManager").getMethodProxy( //$NON-NLS-1$
							expression, "addComponent", //$NON-NLS-1$
							new String[] { "javax.swing.JToolBar", "java.lang.Object", "java.lang.Object"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			processExpressionProxy(method, constants.methods, MANAGER_JTOOLBAR_ADD_COMPONENT);
		}
		return method;
	}

	/**
	 * Add component to JToolBar
	 * 
	 * @param jtoolbar
	 * @param component
	 *            component to add, may be a Component or Action
	 * @param beforeComponent
	 *            before component, may be a Component or Action, or <code>null</code> for at end
	 * @param expression
	 * 
	 * @see org.eclipse.ve.internal.jfc.vm.JToolBarManager#addComponent(JToolBar, Object, Object)
	 * @since 1.1.0
	 */
	public static void invoke_JToolBar_addComponent(IProxy jtoolbar, IProxy component, IProxy beforeComponent, IExpression expression) {
		expression.createSimpleMethodInvoke(BeanAwtUtilities.getJToolBarAddComponent(expression), null, new IProxy[] { jtoolbar, component,
				beforeComponent}, false);
	}

	/**
	 * Get the {@link org.eclipse.ve.internal.jfc.vm.JToolBarManager#removeComponent(Object) removeComponent}proxy method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private static IProxyMethod getJToolBarRemoveComponent(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_JTOOLBAR_REMOVE_COMPONENT];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, "org.eclipse.ve.internal.jfc.vm.JToolBarManager").getMethodProxy( //$NON-NLS-1$
							expression, "removeComponent", //$NON-NLS-1$
							new String[] { "javax.swing.JToolBar", "java.lang.Object"}); //$NON-NLS-1$ //$NON-NLS-2$
			processExpressionProxy(method, constants.methods, MANAGER_JTOOLBAR_REMOVE_COMPONENT);
		}
		return method;
	}

	/**
	 * Invoke JToolBar remove component.
	 * 
	 * @param jtoolbar
	 * @param component
	 * @param expression
	 * 
	 * @see org.eclipse.ve.internal.jfc.vm.JToolBarManager#removeComponent(JToolBar, Object)
	 * @since 1.1.0
	 */
	public static void invoke_JToolBar_removeComponent(IProxy jtoolbar, IProxy component, IExpression expression) {
		expression
				.createSimpleMethodInvoke(BeanAwtUtilities.getJToolBarRemoveComponent(expression), null, new IProxy[] { jtoolbar, component}, false);
	}

	public static IBeanProxy invoke_getLayout(IBeanProxy aContainerBeanProxy) {
		BeanAwtUtilities constants = getConstants(aContainerBeanProxy);

		if (constants.getLayoutMethodProxy == null) {
			constants.getLayoutMethodProxy = aContainerBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(
					"java.awt.Container").getMethodProxy("getLayout"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return constants.getLayoutMethodProxy.invokeCatchThrowableExceptions(aContainerBeanProxy);
	}

	public static IRectangleBeanProxy invoke_getBounds(IBeanProxy aBeanProxy) {
		BeanAwtUtilities constants = getConstants(aBeanProxy);

		if (constants.getBoundsMethodProxy == null) {
			constants.getBoundsMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy("java.awt.Component").getMethodProxy("getBounds"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return (IRectangleBeanProxy) constants.getBoundsMethodProxy.invokeCatchThrowableExceptions(aBeanProxy);
	}

	public static IPointBeanProxy invoke_getLocation(IBeanProxy aBeanProxy) {
		BeanAwtUtilities constants = getConstants(aBeanProxy);

		if (constants.getLocationMethodProxy == null) {
			constants.getLocationMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy("java.awt.Component").getMethodProxy("getLocation"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return (IPointBeanProxy) constants.getLocationMethodProxy.invokeCatchThrowableExceptions(aBeanProxy);
	}

	public static IDimensionBeanProxy invoke_getSize(IBeanProxy aBeanProxy) {
		BeanAwtUtilities constants = getConstants(aBeanProxy);

		if (constants.getSizeMethodProxy == null) {
			constants.getSizeMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy("java.awt.Component").getMethodProxy("getSize"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return (IDimensionBeanProxy) constants.getSizeMethodProxy.invokeCatchThrowableExceptions(aBeanProxy);
	}

	public static IDimensionBeanProxy invoke_getPreferredSize(IBeanProxy aBeanProxy) {
		BeanAwtUtilities constants = getConstants(aBeanProxy);

		if (constants.getPreferredSizeMethodProxy == null) {
			constants.getPreferredSizeMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(
					"java.awt.Component").getMethodProxy("getPreferredSize"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return (IDimensionBeanProxy) constants.getPreferredSizeMethodProxy.invokeCatchThrowableExceptions(aBeanProxy);
	}

	public static IBeanProxy invoke_getParent(IBeanProxy aBeanProxy) {
		BeanAwtUtilities constants = getConstants(aBeanProxy);

		if (constants.getParentMethodProxy == null) {
			constants.getParentMethodProxy = aBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy("java.awt.Component").getMethodProxy("getParent"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return constants.getParentMethodProxy.invokeCatchThrowableExceptions(aBeanProxy);
	}

	public static IArrayBeanProxy invoke_getComponents(IBeanProxy aContainerBeanProxy) {
		BeanAwtUtilities constants = getConstants(aContainerBeanProxy);

		if (constants.getComponentsMethodProxy == null) {
			constants.getComponentsMethodProxy = aContainerBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(
					"java.awt.Container").getMethodProxy("getComponents"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return (IArrayBeanProxy) constants.getComponentsMethodProxy.invokeCatchThrowableExceptions(aContainerBeanProxy);
	}

	/**
	 * Get set component method proxy. <package-protected> because only ComponentManager should access it.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IProxyMethod getSetComponentMethodProxy(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_SET_COMPONENT];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression, COMPONENTMANAGER_CLASSNAME).getMethodProxy( //$NON-NLS-1$
					expression, "setComponent", //$NON-NLS-1$
					new String[] { "java.awt.Component", FEEDBACKCONTROLLER_CLASSNAME} //$NON-NLS-1$ //$NON-NLS-2$
					);
			processExpressionProxy(method, constants.methods, MANAGER_SET_COMPONENT);
		}
		return method;
	}

	/**
	 * Apply bounds method proxy. <package-protected> because only ComponentManager should access this.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IProxyMethod getApplyBoundsMethodProxy(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_APPLY_BOUNDS];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression, COMPONENTMANAGER_CLASSNAME).getMethodProxy( //$NON-NLS-1$
					expression, "applyBounds", //$NON-NLS-1$
					new String[] { "java.awt.Rectangle", "java.awt.Rectangle"}); //$NON-NLS-1$ //$NON-NLS-2$
			processExpressionProxy(method, constants.methods, MANAGER_APPLY_BOUNDS);
		}
		return method;
	}

	/**
	 * Get apply location method proxy. <package-protected> because only ComponentManager should access this.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IProxyMethod getApplyLocationMethodProxy(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_APPLY_LOCATION];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression, COMPONENTMANAGER_CLASSNAME).getMethodProxy( //$NON-NLS-1$
					expression, "applyLocation", //$NON-NLS-1$
					new String[] { "java.awt.Point", "java.awt.Point"}); //$NON-NLS-1$ //$NON-NLS-2$
			processExpressionProxy(method, constants.methods, MANAGER_APPLY_LOCATION);
		}
		return method;
	}

	/**
	 * Get the ComponentManager.overrideLocation method using the expression. <package-protected> because only ComponentManager should access it.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IProxyMethod getOverrideLocationMethodProxy(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_OVERRIDE_LOCATION];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression, COMPONENTMANAGER_CLASSNAME).getMethodProxy( //$NON-NLS-1$
					expression, "overrideLoc", //$NON-NLS-1$
					new String[] { "int", "int"}); //$NON-NLS-1$ //$NON-NLS-2$
			processExpressionProxy(method, constants.methods, MANAGER_OVERRIDE_LOCATION);
		}
		return method;
	}

	/**
	 * Get ComponentManager.getDefaultBounds method proxy. <package-protected> because only ComponentManager should access it.
	 * 
	 * @param registry
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IMethodProxy getDefaultBoundsMethodProxy(ProxyFactoryRegistry registry) {
		BeanAwtUtilities constants = getConstants(registry);

		if (constants.getManagerDefaultBoundsMethodProxy == null) {
			constants.getManagerDefaultBoundsMethodProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy(COMPONENTMANAGER_CLASSNAME)
					.getMethodProxy( //$NON-NLS-1$
							"getDefaultBounds" //$NON-NLS-1$
					);
		}
		return constants.getManagerDefaultBoundsMethodProxy;
	}

	/**
	 * Get ComponentManager.getDefaultLocation method proxy. <package-protected> because only ComponentManager should access it.
	 * 
	 * @param registry
	 * @return
	 * 
	 * @since 1.1.0
	 */
	static IMethodProxy getDefaultLocationMethodProxy(ProxyFactoryRegistry registry) {
		BeanAwtUtilities constants = getConstants(registry);

		if (constants.getManagerDefaultLocationMethodProxy == null) {
			constants.getManagerDefaultLocationMethodProxy = registry.getBeanTypeProxyFactory().getBeanTypeProxy(COMPONENTMANAGER_CLASSNAME)
					.getMethodProxy( //$NON-NLS-1$
							"getDefaultLocation" //$NON-NLS-1$
					);
		}
		return constants.getManagerDefaultLocationMethodProxy;
	}

	/**
	 * Return the ILayoutPolicyFactory for the layout manager of a container
	 */
	public static ILayoutPolicyFactory getLayoutPolicyFactoryFromLayoutManager(IJavaInstance layoutManager, EditDomain domain) {

		if (layoutManager == null) {
			return new NullLayoutPolicyFactory(); // There is nothing we can check against, so we hardcode null.
		} else {
			return getLayoutPolicyFactoryFromLayoutManger(layoutManager.eClass(), domain);
		}

	}

	/**
	 * Return the ILayoutPolicyFactory for the layout manager of a LayoutManagerProxy Note: if containerProxy is null, then editdomain can be null.
	 */
	public static ILayoutPolicyFactory getLayoutPolicyFactoryFromLayoutManger(IBeanProxy layoutManagerProxy, EditDomain domain) {
		if (layoutManagerProxy == null)
			return new NullLayoutPolicyFactory(); // There is nothing we can check against, so we hardcode null.

		ILayoutPolicyFactory factory = VisualUtilities.getLayoutPolicyFactory(layoutManagerProxy.getTypeProxy(), domain);
		if (factory == null) {
			// Need to see if it is type LayoutManager2
			ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain);
			EClassifier layoutManagerClass = Utilities.getJavaClass(layoutManagerProxy.getTypeProxy(), rset);
			return getDefaultLayoutPolicyFactory(layoutManagerClass);
		} else {
			return factory;
		}

	}

	private static ILayoutPolicyFactory getDefaultLayoutPolicyFactory(EClassifier layoutManagerClass) {
		if ((Utilities.getJavaClass("java.awt.LayoutManager2", layoutManagerClass.eResource().getResourceSet())).isAssignableFrom(layoutManagerClass)) //$NON-NLS-1$
			return new UnknownLayout2PolicyFactory();
		else
			return new UnknownLayoutPolicyFactory();
	}

	/**
	 * @param classifier
	 * @param editDomain
	 * @return layoutPolicyFactory
	 * 
	 * @since 1.0.0
	 */
	public static ILayoutPolicyFactory getLayoutPolicyFactoryFromLayoutManger(EClassifier classifier, EditDomain editDomain) {
		if (classifier == null)
			return new NullLayoutPolicyFactory(); // There is nothing we can check against, so we hardcode null.
		if (!(classifier instanceof JavaClass))
			return null; // Not a java class.
		ILayoutPolicyFactory layoutPolicyFactory = VisualUtilities.getLayoutPolicyFactory(classifier, editDomain);
		if (layoutPolicyFactory == null) {
			return getDefaultLayoutPolicyFactory(classifier);
		} else {
			return layoutPolicyFactory;
		}
	}

	/**
	 * Get the Axis orientation for the BoxLayout.
	 * 
	 * @param boxlayoutProxy
	 *            Proxy for the BoxLayout.
	 * @return The orientation. <code>true</code> if X_AXIS.
	 * 
	 * @since 1.0.0
	 */
	public static boolean getBoxLayoutAxis(IBeanProxy boxlayoutProxy) {
		BeanAwtUtilities constants = getConstants(boxlayoutProxy);

		if (constants.getBoxLayoutAxisFieldProxy == null) {
			IBeanTypeProxy boxlayoutType = boxlayoutProxy.getTypeProxy();
			constants.getBoxLayoutAxisFieldProxy = boxlayoutType.getDeclaredFieldProxy("axis"); //$NON-NLS-1$
			try {
				constants.getBoxLayoutAxisFieldProxy.setAccessible(true);
				constants.boxLayoutAxis_XAXIS = boxlayoutType.getFieldProxy("X_AXIS").get(null); //$NON-NLS-1$
			} catch (Exception e) {
				JavaVEPlugin.getPlugin().getLogger().log(e);
			}
		}
		try {
			return constants.getBoxLayoutAxisFieldProxy.get(boxlayoutProxy).equals(constants.boxLayoutAxis_XAXIS);
		} catch (ThrowableProxy e) {
			JavaVEPlugin.getPlugin().getLogger().log(e);
			return true;
		}
	}

	/**
	 * Get the {@link org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager#insertTabBefore(JTabbedPane, String, Icon, Component, Component) insertTab}
	 * proxy method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private static IProxyMethod getJTabbedPaneInsertTab(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_JTABBEDPANE_INSERTTAB];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression,
					"org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager").getMethodProxy( //$NON-NLS-1$
					expression, "insertTabBefore", //$NON-NLS-1$
					new String[] { "javax.swing.JTabbedPane", "java.lang.String", "javax.swing.Icon", "java.awt.Component", "java.awt.Component"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			processExpressionProxy(method, constants.methods, MANAGER_JTABBEDPANE_INSERTTAB);
		}
		return method;
	}

	/**
	 * Invoke the insert tab, non default.
	 * 
	 * @param jtabbedpane
	 * @param title
	 * @param icon
	 * @param component
	 * @param beforeComponent
	 * @param expression
	 * @return component proxy for the inserted tab.
	 * 
	 * @since 1.1.0
	 */
	public static IProxy invoke_JTabbedPane_insertTab(IProxy jtabbedpane, IProxy title, IProxy icon, IProxy component, IProxy beforeComponent,
			IExpression expression) {
		return expression.createSimpleMethodInvoke(getJTabbedPaneInsertTab(expression), null, new IProxy[] { jtabbedpane, title, icon, component,
				beforeComponent}, true);
	}

	/**
	 * Get the {@link org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager#insertTabBefore(JTabbedPane, Component, Component) insertTabDefault} proxy
	 * method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private static IProxyMethod getJTabbedPaneInsertTabDefault(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_JTABBEDPANE_INSERTTAB_DEFAULT];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression,
					"org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager").getMethodProxy( //$NON-NLS-1$
					expression, "insertTabBefore", //$NON-NLS-1$
					new String[] { "javax.swing.JTabbedPane", "java.awt.Component", "java.awt.Component"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			processExpressionProxy(method, constants.methods, MANAGER_JTABBEDPANE_INSERTTAB_DEFAULT);
		}
		return method;
	}

	/**
	 * Invoke the insert tab, default.
	 * 
	 * @param jtabbedpane
	 * @param component
	 * @param beforeComponent
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	public static void invoke_JTabbedPane_insertTab_Default(IProxy jtabbedpane, IProxy component, IProxy beforeComponent, IExpression expression) {
		expression.createSimpleMethodInvoke(getJTabbedPaneInsertTabDefault(expression), null,
				new IProxy[] { jtabbedpane, component, beforeComponent}, false);
	}

	/**
	 * Return the selected tabbed pane component, null if none.
	 */
	public static IBeanProxy invoke_tab_getSelectedComponent(IBeanProxy aJTabbedPaneBeanProxy) {
		BeanAwtUtilities constants = getConstants(aJTabbedPaneBeanProxy);

		if (constants.getTabSelectedComponentMethodProxy == null) {
			constants.getTabSelectedComponentMethodProxy = aJTabbedPaneBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy("javax.swing.JTabbedPane").getMethodProxy( //$NON-NLS-1$
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
			constants.setTabSelectedComponentMethodProxy = aJTabbedPaneBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy("javax.swing.JTabbedPane").getMethodProxy( //$NON-NLS-1$
							"setSelectedComponent", "java.awt.Component"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		constants.setTabSelectedComponentMethodProxy.invokeCatchThrowableExceptions(aJTabbedPaneBeanProxy, componentBeanProxy);
	}

	/**
	 * get the index of the tab at the given location
	 */
	public static int invoke_JTabbedPane_getItemFromLocation(IBeanProxy aJTabbedPaneBeanProxy, IBeanProxy xPointBeanProxy, IBeanProxy yPointBeanProxy) {
		BeanAwtUtilities constants = getConstants(aJTabbedPaneBeanProxy);
		int retVal = -1;

		if (constants.indexOfTabAtLocationMethodProxy == null) {
			constants.indexOfTabAtLocationMethodProxy = aJTabbedPaneBeanProxy.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(
					"javax.swing.JTabbedPane").getMethodProxy( //$NON-NLS-1$
					"indexAtLocation", new String[] { "int", "int"}); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if (constants.indexOfTabAtLocationMethodProxy != null) {
			IBeanProxy pageNum = constants.indexOfTabAtLocationMethodProxy.invokeCatchThrowableExceptions(aJTabbedPaneBeanProxy, new IBeanProxy[] {
					xPointBeanProxy, yPointBeanProxy});

			if (pageNum != null && pageNum instanceof INumberBeanProxy)
				retVal = ((INumberBeanProxy) pageNum).intValue();
		}
		return retVal;
	}

	/**
	 * Get the {@link org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager#setDefaultTitle(JTabbedPane, Component) setDefaultTitle}proxy method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private static IProxyMethod getJTabbedPaneSetTabIcon(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_JTABBEDPANE_SETTABICON];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression,
					"org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager").getMethodProxy( //$NON-NLS-1$
					expression, "setIconAt", //$NON-NLS-1$
					new String[] { "javax.swing.JTabbedPane", "java.awt.Component", "javax.swing.Icon"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			processExpressionProxy(method, constants.methods, MANAGER_JTABBEDPANE_SETTABICON);
		}
		return method;
	}

	/**
	 * Set the tab Icon for the tab for the given component.
	 * 
	 * @param jtabbedpane
	 * @param component
	 * @param icon
	 * @param expression
	 * 
	 * @see org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager#setIconAt(JTabbedPane, Component, Icon)
	 * @since 1.1.0
	 */
	public static void invoke_JTabbedPane_setIconAt(IProxy jtabbedpane, IProxy component, IProxy icon, IExpression expression) {
		expression.createSimpleMethodInvoke(BeanAwtUtilities.getJTabbedPaneSetTabIcon(expression), null,
				new IProxy[] { jtabbedpane, component, icon}, false);
	}

	/**
	 * Get the {@link org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager#setDefaultTitle(JTabbedPane, Component) setDefaultTitle}proxy method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private static IProxyMethod getJTabbedPaneSetTitle(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_JTABBEDPANE_SETTABTITLE];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression,
					"org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager").getMethodProxy( //$NON-NLS-1$
					expression, "setTitleAt", //$NON-NLS-1$
					new String[] { "javax.swing.JTabbedPane", "java.awt.Component", "java.lang.String"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			processExpressionProxy(method, constants.methods, MANAGER_JTABBEDPANE_SETTABTITLE);
		}
		return method;
	}

	/**
	 * Set the title on the tab component.
	 * 
	 * @param jtabbedPane
	 * @param component
	 * @param title
	 * @param expression
	 * 
	 * @see org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager#setTitleAt(JTabbedPane, Component, String)
	 * @since 1.1.0
	 */
	public static void invoke_JTabbedPane_setTitle(IProxy jtabbedPane, IProxy component, IProxy title, IExpression expression) {
		expression.createSimpleMethodInvoke(BeanAwtUtilities.getJTabbedPaneSetTitle(expression), null, new IProxy[] { jtabbedPane, component, title},
				false);
	}

	/**
	 * Get the {@link org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager#setDefaultTitle(JTabbedPane, Component) setDefaultTitle}proxy method.
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private static IProxyMethod getJTabbedPaneSetDefaultTitle(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_JTABBEDPANE_SETDEFAULTTITLE];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression,
					"org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager").getMethodProxy( //$NON-NLS-1$
					expression, "setDefaultTitle", //$NON-NLS-1$
					new String[] { "javax.swing.JTabbedPane", "java.awt.Component"}); //$NON-NLS-1$ //$NON-NLS-2$
			processExpressionProxy(method, constants.methods, MANAGER_JTABBEDPANE_SETDEFAULTTITLE);
		}
		return method;
	}

	/**
	 * Invoke the JTabbedPane manager set default title.
	 * 
	 * @param jtabbedPane
	 * @param component
	 * @param expression
	 * 
	 * @see org.eclipse.ve.internal.jfc.vm.JTabbedPaneManager#setDefaultTitle(JTabbedPane, Component)
	 * @since 1.1.0
	 */
	public static void invoke_JTabbedPane_setDefaultTitle(IProxy jtabbedPane, IProxy component, IExpression expression) {
		expression.createSimpleMethodInvoke(BeanAwtUtilities.getJTabbedPaneSetDefaultTitle(expression), null, new IProxy[] { jtabbedPane, component},
				false);
	}

	/**
	 * Get the {@link org.eclipse.ve.internal.jfc.vm.JSplitPaneManager#setDividerLocation(int)}
	 * 
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private static IProxyMethod getJSplitPaneSetDividerLocation(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[MANAGER_JSPLITPANE_SETDIVIDERLOCATION];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression,
					"org.eclipse.ve.internal.jfc.vm.JSplitPaneManagerExtension").getMethodProxy( //$NON-NLS-1$
					expression, "setDividerLocation", //$NON-NLS-1$
					new String[] { "int"}); //$NON-NLS-1$
			processExpressionProxy(method, constants.methods, MANAGER_JSPLITPANE_SETDIVIDERLOCATION);
		}
		return method;
	}

	/**
	 * Invoke set splitpane divider location on split pane manager.
	 * <p>
	 * <package-protected> because only JSplitPaneManager should access it.
	 * 
	 * @param splitPaneManager
	 * @param dividerLocation
	 * @param expression
	 * 
	 * @return old Location, if asked for.
	 * @since 1.1.0
	 */
	static IProxy invoke_JSplitPane_setDividerLocation(IProxy splitPaneManager, IProxy dividerLocation, boolean getOldLocation, IExpression expression) {
		return expression.createSimpleMethodInvoke(getJSplitPaneSetDividerLocation(expression), splitPaneManager, new IProxy[] { dividerLocation},
				getOldLocation);
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
			booleanProxy = (IBooleanBeanProxy) isLeftToRightMethodProxy.invokeCatchThrowableExceptions(componentOrientationProxy);
		}
		return booleanProxy;
	}

	/*
	 * Helper class to hide the grid for edit parts and their children that have a grid controller. Used especially for JTabbedPanes and Containers
	 * that have a CardLayout.
	 */
	public static void hideGrids(EditPart editpart) {
		if (editpart == null)
			return;
		GridController gridController = GridController.getGridController(editpart);
		if (gridController != null && gridController.isGridShowing())
			gridController.setGridShowing(false);
		List children = editpart.getChildren();
		for (int i = 0; i < children.size(); i++) {
			hideGrids((EditPart) children.get(i));
		}
	}

	/**
	 * See bug 69514 - Dropping AWT ScrollPane fails on Linux Exceptions are thrown because the scroll pane doesn't have children. To prevent this,
	 * we'll add a temporary child (a special Panel) when there isn't one.
	 * 
	 * @param scrollPane
	 *            ScrollPane to be processed
	 * 
	 * @since 1.1.0.1
	 */
	public static IProxyMethod getScrollPaneMakeItRight(IExpression expression) {
		BeanAwtUtilities constants = getConstants(expression.getRegistry());

		IProxyMethod method = constants.methods[SCROLLPANE_MAKE_IT_RIGHT];
		if (method == null || (method.isExpressionProxy() && ((ExpressionProxy) method).getExpression() != expression)) {
			method = expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression,
					"org.eclipse.ve.internal.jfc.vm.ScrollPaneManager").getMethodProxy( //$NON-NLS-1$
					expression, "makeItRight", //$NON-NLS-1$
					new String[] { "java.awt.ScrollPane"}); //$NON-NLS-1$
			processExpressionProxy(method, constants.methods, SCROLLPANE_MAKE_IT_RIGHT);
		}
		return method;
	}

	protected BeanAwtUtilities() {
	}

	public static LayoutList getDefaultLayoutList() {
		if (DEFAULT_LAYOUTLIST == null){
			DEFAULT_LAYOUTLIST = new LayoutList(){
				public void fillMenuManager(MenuManager aMenuManager) {
					aMenuManager.add(new Action(){
						public void run() {
							String prefID = JFCVisualPlugin.PREFERENCE_PAGE_ID;
							PreferencesUtil.createPreferenceDialogOn(Display.getCurrent().getActiveShell(), prefID, new String[]{prefID}, null).open();
						}
						public String getText() {
							return InternalVisualMessages.getString("LayoutListMenuContributor.Preferences"); //$NON-NLS-1$
						}
					});					
				}				
			};
		}
		return DEFAULT_LAYOUTLIST;
	}	

}
