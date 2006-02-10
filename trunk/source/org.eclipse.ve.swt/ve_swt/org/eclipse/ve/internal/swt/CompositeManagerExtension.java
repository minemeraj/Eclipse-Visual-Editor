/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CompositeManagerExtension.java,v $
 *  $Revision: 1.6 $  $Date: 2006-02-10 21:53:46 $ 
 */
package org.eclipse.ve.internal.swt;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;

import org.eclipse.ve.internal.java.core.IBeanProxyHost;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;
import org.eclipse.ve.internal.java.visual.ILayoutPolicyFactory;
import org.eclipse.ve.internal.java.visual.VisualUtilities;

import org.eclipse.ve.internal.swt.ControlManager.ControlManagerExtension;
import org.eclipse.ve.internal.swt.common.Common;
 

/**
 * Composite Manager extension to handle the composite on the remote vm.
 * @since 1.1.0
 */
public class CompositeManagerExtension extends ControlManagerExtension implements ControlManagerFeedbackControllerNotifier {

	protected IProxy compositeManagerProxy;
	private final EditDomain editDomain;
	private final CompositeProxyAdapter composite;

	/**
	 * Construct with the editdomain.
	 * @param editDomain
	 * 
	 * @since 1.1.0
	 */
	public CompositeManagerExtension(EditDomain editDomain, CompositeProxyAdapter composite) {
		this.editDomain = editDomain;
		this.composite = composite;
	}
	
	protected String getExtensionClassname() {
		return BeanSWTUtilities.COMPOSITEMANAGEREXTENSION_CLASSNAME;
	}
	
	protected IProxy primGetExtensionProxy() {
		return compositeManagerProxy;
	}
	
	protected void primSetExtensionProxy(IProxy proxy) {
		if (compositeManagerProxy != null && compositeManagerProxy.isBeanProxy() && ((IBeanProxy) compositeManagerProxy).isValid())
			composite.getControlManager().getFeedbackController().deregisterFeedbackNotifier((IBeanProxy) compositeManagerProxy);	// Remove the old.
		compositeManagerProxy = proxy;
		// See if it has been registered yet or not. If proxy is null, then we are either invalid or disposed. In either case we don't care
		// about the callback.
		final ProxyFactoryRegistry registry;
		if (proxy != null) {
			if (proxy.isBeanProxy())
				composite.getControlManager().getFeedbackController().registerFeedbackNotifier(CompositeManagerExtension.this, (IBeanProxy) proxy);
			if ((registry = proxy.getProxyFactoryRegistry()).getConstants(LayoutDataTypeCallback.class) == null) {
				// We need to create it. It will be registered only once for registry and it never goes away.
				LayoutDataTypeCallback cb = new LayoutDataTypeCallback(editDomain);
				registry.registerConstants(LayoutDataTypeCallback.class, cb);
				if (proxy.isExpressionProxy()) {
					// Create the cooresponding server on the remote vm that will do the callback.
					IExpression expression = ((ExpressionProxy) proxy).getExpression();
					ExpressionProxy pcb = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
					expression.createClassInstanceCreation(ForExpression.ASSIGNMENT_RIGHT, expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression, BeanSWTUtilities.COMPOSITEMANAGEREXTENSION_CLASSNAME+"$LayoutDataTypeCallback"), 0); //$NON-NLS-1$
					registry.getCallbackRegistry().registerCallback(pcb, cb, expression);
					pcb.addProxyListener(new ExpressionProxy.ProxyAdapter() {
						public void proxyNotResolved(ExpressionProxy.ProxyEvent event) {
							registry.deregisterConstants(LayoutDataTypeCallback.class);	// The registration didn't work, so remove from the constants.
						}
					});
				} else {
					try {
						registry.getCallbackRegistry().registerCallback(registry.getBeanTypeProxyFactory().getBeanTypeProxy(BeanSWTUtilities.COMPOSITEMANAGEREXTENSION_CLASSNAME+"$LayoutDataTypeCallback").newInstance(), cb); //$NON-NLS-1$
					} catch (ThrowableProxy e) {
						JavaVEPlugin.log(e, Level.WARNING);
					}
				}
			}
		}
	}
	
	/**
	 * Flag to manager extension on remote vm that it should check for valid layout data class
	 * on next layout.
	 * 
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	public void setVerifyLayoutData(IExpression expression) {
		expression.createSimpleMethodInvoke(BeanSWTUtilities.getCompositeManagerExtension_LayoutVerify(expression), compositeManagerProxy, null, false);
	}
	
	/**
	 * Bit of a kludge, but we don't know what the valid layout data types are for the layout data classes on
	 * the remote vm. We can't send the type ahead of time because until the composite has been instantiated
	 * we don't know the layout type (it may of been implicitly set through inheritance). So we will need to call back
	 * the first time we meet each new layout type to get the valid layoutdata class. This class is what is
	 * called to. There will be one instance per registry, and it will be given to the extension at creation time.
	 * 
	 * @since 1.1.0
	 */
	private static class LayoutDataTypeCallback implements ICallback {
		
		protected final EditDomain editDomain;
		
		/**
		 * Construct with the domain.
		 * @param editDomain
		 * 
		 * @since 1.1.0
		 */
		public LayoutDataTypeCallback(EditDomain editDomain) {
			this.editDomain = editDomain;
		}
			
		public Object calledBack(int msgID, IBeanProxy parm) {
			// This is the call back that will be called. The layout data class will
			// be the parm. We won't bother checking the msg id. Nobody else should be calling.
			// The parm must not be null. If there is no layout, then it shouldn't call back because it knows the answer.
			// It will return the constraint data type or null if not known.
			
			ILayoutPolicyFactory layoutPolicyFactory = VisualUtilities.getLayoutPolicyFactory((IBeanTypeProxy) parm, editDomain);
			// If no policy factory, then we can't test constraint, if the constraint class is null, then that means no constraint (we send Void.TYPE in that case).
			JavaHelpers constraintType;
			if (layoutPolicyFactory != null) {
			        ResourceSet resourceSet = EMFEditDomainHelper.getResourceSet(editDomain);
					constraintType = layoutPolicyFactory.getConstraintClass(resourceSet);
			        if (constraintType == null)
			        	constraintType = Utilities.getJavaType("void",resourceSet); //$NON-NLS-1$
			} else
				return null;
			return parm.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(constraintType.getQualifiedNameForReflection());
		}

		public Object calledBack(int msgID, Object[] parms) {
			return null;
		}

		public Object calledBack(int msgID, Object parm) {
			return null;
		}

		public void calledBackStream(int msgID, InputStream is) {
		}

	}

	public void calledBack(int msgID, Object[] parms) {
		// Should only be called for layout data type failed request.
		switch (msgID) {
			case Common.CMPL_INVALID_LAYOUT:
				// The parms are the children (as proxies) that are invalid. This requires us walking the controls and see if the proxies
				// are in the parms. We are going to assume there are only a handfull of invalids, so we will do linear search.
				// (Note: don't want to add the proxies to a set because the "equals" method on a proxy will cause it to do a
				// remote vm call if they are the same identical object. 
				List children = (List) ((EObject) composite.getTarget()).eGet(composite.sfCompositeControls);
				String layoutDataValidType = parms != null ? ((IStringBeanProxy)parms[0]).stringValue() : null;
nextChild:		for (int i = 0; i < children.size(); i++) {
					IJavaObjectInstance child = (IJavaObjectInstance) children.get(i);
					IBeanProxyHost childHost = (IBeanProxyHost) EcoreUtil.getExistingAdapter(child, IBeanProxyHost.BEAN_PROXY_TYPE);
					if (childHost != null) {
						CompositeProxyAdapter.ControlLayoutDataAdapter a = composite.getControlLayoutDataAdapter(child);
						if (a != null) {
							a.clearLayoutDataError(); // Clear it out, add back in only if an error.
							if (parms != null) {
								IBeanProxy childProxy = childHost.getBeanProxy();
								if (childProxy != null) {
									for (int j = 1; j < parms.length; j += 2) {
										if (childProxy.sameAs((IBeanProxy) parms[j])) {
											// We have a winner!
											a.processLayoutDataError(layoutDataValidType, ((IStringBeanProxy) parms[j + 1]).stringValue());
											break nextChild;
										}
									}
								}
							}
						}						
					}
					
				}
				break;
				
			case Common.CMPL_CLIENTAREA_CHANGED:
				composite.setOriginOffset(new Point(((IIntegerBeanProxy) parms[0]).intValue(), ((IIntegerBeanProxy) parms[1]).intValue()));
				composite.setClientArea(new Rectangle(((IIntegerBeanProxy) parms[2]).intValue(), ((IIntegerBeanProxy) parms[3]).intValue(), ((IIntegerBeanProxy) parms[4]).intValue(), ((IIntegerBeanProxy) parms[5]).intValue()));
				break;
		}
	}

}
