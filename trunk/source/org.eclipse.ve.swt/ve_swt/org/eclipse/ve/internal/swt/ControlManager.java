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
package org.eclipse.ve.internal.swt;

/*
 * $RCSfile: ControlManager.java,v $ $Revision: 1.25 $ $Date: 2006-02-06 23:38:34 $
 */

import java.io.InputStream;
import java.util.*;
import java.util.logging.Level;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.swt.graphics.ImageData;

import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.initParser.tree.NoExpressionValueException;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;

import org.eclipse.ve.internal.swt.common.Common;

/**
 * Manager of swt.Control. It provides the functions of the IVisualComponent for interfacing to the live control, including the image support. It
 * will notify back of any size/position changes. It will also handle validating and refreshing images of any parent composites of this control that
 * may of gone stale do to changes made in this control. That way you don't need to be aware of this in the parent composites and so there is no
 * need to tell them that their image is stale.
 * <p>
 * The callback locations/bounds will be absolute, relative to the swt.Shell that the component is in. This doesn't return relative bounds. These are
 * the VisualComponent bounds. This is different than the actual bounds/location that are set through here.
 * <p>
 * Since it doesn't care about IBeanProxyHost's, just control proxies, it can be used by non-control proxy hosts that use an swt.Control as the
 * visual.
 * <p>
 * It uses IExpression's and IProxy's for many of the calls so that these can be built up all in one Expression. It is more efficient to do it this
 * way. The ModelChangeController is used in many of the API so that at the end of current transaction clean-up transactions can be done. This way we
 * can group many transaction together into one batch job at the end. These API should be called from within a transaction for this to be effective.
 * Otherwise you will loose the batching because they will each be queued off to the UI thread and not grouped together.
 * <p>
 * The way to use it outside of ControlProxyAdapter for when you want to use an SWT Control as the visual component of a non-visual is this. When
 * you create your bean proxy for the control you will do a
 * {@link ControlManager#setComponentBeanProxy(CompomentManagerExtension, IExpression , ModelChangeController)}. This will tell it what control to monitor. When
 * this control is disposed, {@link ControlManager#dispose(IExpression)} should be called to release all of the resources this manager is using.
 * Once disposed, the manager can be used again by just doing the setControlBeanProxy.
 * <p>
 * Whenever changes are being made to a control, you should call {@link ControlManager#invalidate(ModelChangeController)} so that we know this and
 * can invalidate and revalidate this control and any parent composite with a new image. You can all invalidate as often as needed during a
 * transaction. It will use the ModelChangeController to batch it down one transaction at the end of the current transaction.
 * <p>
 * This class in not meant to be subclassed, 
 * use {@link ControlManager#addComponentExtension(CompomentManagerExtension, IExpression)} to add extensions to the manager instead.
 * @since 1.0.0
 */
public class ControlManager implements ControlManagerFeedbackControllerNotifier, IVisualComponent {

	public interface IControlImageListener extends IImageListener {

		/**
		 * Called for any image status as a result of image collection. If the image was successfully collected you will receive first an imageStatus
		 * and then an imageChanged. If the collection was not successful, you will receive only an imageStatus.
		 * <p>
		 * You can use this or ignore this notification. The ImageDataConstants contains the status that can be sent.
		 * 
		 * @param status
		 * 
		 * @see org.eclipse.ve.internal.swt.common.ImageDataConstants#IMAGE_ABORTED
		 * @since 1.1.0
		 */
		public void imageStatus(int status);

		/**
		 * An exception occured during image capture. This is the exception. No status or data will be sent.
		 * 
		 * @param exception
		 * 
		 * @since 1.1.0
		 */
		public void imageException(ThrowableProxy exception);
	}

	protected VisualComponentSupport vcSupport = new VisualComponentSupport();

	protected IProxy controlManagerProxy;

	protected IProxy controlBeanProxy;
	
	protected IBeanProxy displayProxy;

	private Point fLastSignalledLocation;

	private Dimension fLastSignalledSize;

	private Point locationOverride;

	private ImageNotifierSupport imSupport;

	private final Object imageAccessorSemaphore = new Object(); // [73930] Semaphore for access to image stuff, can't use (this) because that is also

	// used for instantiation and deadlock can occur.

	private ImageDataCollector fImageDataCollector;

	// Image validity flag it must be checked/changed only under synchronization on this.
	private int fImageValid = WAITING_FIRST_INVALIDATION;

	private static final int INVALID = 1; // Image is invalid, even if currently collecting, that will be aborted and thrown away.

	private static final int INVALID_COLLECTING = 2; // Image is invalid, but currently collecting to make it valid.

	private static final int VALID = 3; // Image is valid.
	
	private static final int WAITING_FIRST_INVALIDATION = 4;	// Waiting for the first invalidation from proxy.

	private ListenerList controlImageListeners; // KLUDGE need an extra list because we want to signal status and ImageNotifier doesn't signal image status.
	
	private List extensions;
	
	/**
	 * This is the base class for all component manager extensions.
	 * 
	 * @since 1.1.0
	 */
	public abstract static class ControlManagerExtension {
		
		/**
		 * Get the extension proxy to add to the control manager. This may be called
		 * several times so don't get rid of the proxy until disposed.
		 * <p>
		 * The default implementation will ask for the classname and create using default constructor.
		 * It will use primGetExtensionProxy to get the proxy whether it is created or not and
		 * use primSetExtensionProxy to set it back. Subclasses should override if they want
		 * something different.
		 * 
		 * @param expression
		 * @return
		 * 
		 * @since 1.1.0
		 */
		protected IProxy getExtensionProxy(IExpression expression) {
			IProxy managerExtensionProxy = primGetExtensionProxy();
			if (managerExtensionProxy == null || ((managerExtensionProxy.isBeanProxy() && !((IBeanProxy) managerExtensionProxy).isValid()))) {
				// Need a new one.
				String extensionClassname = getExtensionClassname();
				if (extensionClassname != null) {
					managerExtensionProxy = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
					expression.createClassInstanceCreation(ForExpression.ASSIGNMENT_RIGHT, expression.getRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(expression, extensionClassname), 0);
					((ExpressionProxy) managerExtensionProxy).addProxyListener(new ExpressionProxy.ProxyListener() {
					
						public void proxyVoid(ProxyEvent event) {
							primSetExtensionProxy(null);
						}
					
						public void proxyNotResolved(ProxyEvent event) {
							primSetExtensionProxy(null);
						}
					
						public void proxyResolved(ProxyEvent event) {
							primSetExtensionProxy(event.getProxy());
						}
					
					});
					primSetExtensionProxy(managerExtensionProxy);
				}
			}
			return managerExtensionProxy;

		}
		
		/**
		 * Get the extension class name. Used in the default implementation of getExtensionProxy(). It is the
		 * name of the extension class to instantiate with the default ctor. If using the default implementation
		 * of getExtensionProxy, this method must be overridden to return something valid.
		 * 
		 * @return
		 * 
		 * @since 1.1.0
		 */
		protected String getExtensionClassname() {
			return null;
		}
		
		/**
		 * Used by default implementation of getExtensionProxy to get the current proxy
		 * whatever it may be or null.
		 * <p>
		 * Only needs to be implemented if used default implementation of getExtensionProxy.
		 * @return
		 * 
		 * @since 1.1.0
		 */
		protected IProxy primGetExtensionProxy() {
			return null;
		}
		
		/**
		 * Used by default implementation of getExtensionProxy and disposed to set the
		 * proxy. It may set an ExpressionProxy or an IBeanProxy or null. When finally 
		 * resolved, it will be called again with the final IBeanProxy. If expression proxy,
		 * implementors can do things that require the resolved proxy.
		 * 
		 * 
		 * @param proxy
		 * 
		 * @since 1.1.0
		 */
		protected void primSetExtensionProxy(IProxy proxy) {
			
		}
		
		/**
		 * The control manager proxy is being disposed or the extension is being
		 * removed from the component manager. Subclasses should
		 * implement and get rid of their proxies. If it wasn't a remove, the extension is still
		 * valid and can be reactivated at a later time.
		 * <p>
		 * The default implementation calls primSetExtensionProxy(null).
		 * @param expression
		 * 
		 * @since 1.1.0
		 */
		protected void disposed(IExpression expression) {
			primSetExtensionProxy(null);
		}

	}
	/**
	 * Add a control manager extension. If already added it has no effect.
	 * 
	 * @param controlExtension
	 * @param expression expression to add with, or <code>null</code> ONLY if being added at the same time as the
	 * control manager itself is being constructed, see {@link ControlProxyAdapter#createControlManager()}.
	 * 
	 * @see ControlManager#removeComponentExtension(CompomentManagerExtension, IExpression)
	 * @since 1.1.0
	 */
	public void addControlExtension(ControlManagerExtension controlExtension, IExpression expression) {
		if (extensions == null)
			extensions = new ArrayList(1);
		extensions.add(controlExtension);
		if (controlManagerProxy != null)
			addExtensionProxy(controlExtension, expression);
	}
	
	private void addExtensionProxy(ControlManagerExtension extension, IExpression expression) {
		expression.createSimpleMethodInvoke(expression.getRegistry().getBeanTypeProxyFactory()
				.getBeanTypeProxy(expression, BeanSWTUtilities.CONTROLMANAGER_CLASSNAME).getMethodProxy(expression, "addExtension", new String[] {BeanSWTUtilities.CONTROLMANAGEREXTENSION_CLASSNAME}), 	//$NON-NLS-1$
				controlManagerProxy, new IProxy[] {extension.getExtensionProxy(expression)}, false);		
		
	}
	
	private void removeExtensionProxy(ControlManagerExtension extension, IExpression expression) {
		expression.createSimpleMethodInvoke(expression.getRegistry().getBeanTypeProxyFactory()
				.getBeanTypeProxy(expression, BeanSWTUtilities.CONTROLMANAGER_CLASSNAME).getMethodProxy(expression, "removeExtension", new String[] {BeanSWTUtilities.CONTROLMANAGEREXTENSION_CLASSNAME}), 	//$NON-NLS-1$
				controlManagerProxy, new IProxy[] {extension.getExtensionProxy(expression)}, false);		
		
	}
	
	/**
	 * Remove a control manager extension. If not in list it will be ignored.
	 * 
	 * @param controlExtension
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	public void removeControlExtension(ControlManagerExtension controlExtension, IExpression expression) {
		if (extensions != null) {
			if (extensions.remove(controlExtension)) {
				if (controlManagerProxy != null && controlManagerProxy.isBeanProxy() && ((IBeanProxy) controlManagerProxy).isValid()) {
					removeExtensionProxy(controlExtension, expression);
				}
				controlExtension.disposed(expression);
			}
		}
	}	
	
	/**
	 * Get the display that this control is associated with.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public IBeanProxy getDisplay() {
		return displayProxy;
	}

	/**
	 * Add a visual component listener.
	 * 
	 * @param aListener
	 * 
	 * @since 1.1.0
	 */
	public void addComponentListener(IVisualComponentListener aListener) {
		vcSupport.addComponentListener(aListener);
	}

	/**
	 * Remove a visual component listener.
	 * 
	 * @param aListener
	 * 
	 * @since 1.1.0
	 */
	public void removeComponentListener(IVisualComponentListener aListener) {
		vcSupport.removeComponentListener(aListener);
	}

	/**
	 * Get the actual resolved control manager proxy. If not yet resolved a ClassCastException will be thrown.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected IBeanProxy getControlManagerBeanProxy() {
		return (IBeanProxy) controlManagerProxy;
	}

	/**
	 * Get the actual resolved control proxy. If not yet resolved a ClassCaseException will be thrown.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected IBeanProxy getControlBeanProxy() {
		return (IBeanProxy) controlBeanProxy;
	}

	/**
	 * Return whether control manager has been disposed or not.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public boolean isDisposed() {
		return controlManagerProxy == null;
	}

	/**
	 * Set the control bean proxy using the given expression.
	 * <p>
	 * <b>Note:</b> This must be called within a DisplayManager UI callback. Also, the control must of been created on this same display.
	 * 
	 * @param controlProxy
	 * @param expression
	 *            expression to use. It is expected the expression is set up for ForExpression.ROOT_EXPRESSION to be the next expression.
	 * 
	 * @since 1.1.0
	 */
	public void setControlBeanProxy(IProxy controlProxy, IExpression expression, ModelChangeController changeController) {
		// Deregister any listening from the previous non null component bean proxy
		final FeedbackController feedbackController = BeanSWTUtilities.getFeedbackController(expression, displayProxy = DisplayManager.getCurrentDisplay(expression.getRegistry()));
		boolean hadControlBeanProxy = controlBeanProxy != null;
		controlBeanProxy = null;
		if (controlProxy != null) {
			if (controlManagerProxy == null) {
				ExpressionProxy newManager = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
				controlManagerProxy = newManager;
				expression.createClassInstanceCreation(ForExpression.ASSIGNMENT_RIGHT, expression.getRegistry().getBeanTypeProxyFactory()
						.getBeanTypeProxy(expression, BeanSWTUtilities.CONTROLMANAGER_CLASSNAME), 0);
				newManager.addProxyListener(new ExpressionProxy.ProxyAdapter() {

					public void proxyResolved(ProxyEvent event) {
						controlManagerProxy = event.getProxy();
						feedbackController.registerFeedbackNotifier(ControlManager.this, (IBeanProxy) controlManagerProxy);
					}

					public void proxyNotResolved(ProxyEvent event) {
						JavaVEPlugin.log("Control manager proxy not resolved on remote vm.", Level.INFO); //$NON-NLS-1$
						controlManagerProxy = null;
					}
				});
				
				// Since this is new, add in any extensions that are pending.
				if (extensions != null) {
					for (int i = 0; i < extensions.size(); i++) {
						addExtensionProxy((ControlManagerExtension) extensions.get(i), expression);
					}
				}
			}

			expression.createSimpleMethodInvoke(BeanSWTUtilities.getSetControlMethodProxy(expression), controlManagerProxy, new IProxy[] {
					controlProxy, feedbackController.getProxy()}, false);
			if (controlProxy.isExpressionProxy()) {
				((ExpressionProxy) controlProxy).addProxyListener(new ExpressionProxy.ProxyAdapter() {

					public void proxyResolved(ProxyEvent event) {
						controlBeanProxy = event.getProxy();
					}
				});
			} else
				controlBeanProxy = controlProxy;
			if (locationOverride != null) {
				// Need to apply right away.
				expression.createSimpleMethodInvoke(BeanSWTUtilities.getOverrideLocationMethodProxy(expression), controlManagerProxy,
						new IProxy[] { expression.getRegistry().getBeanProxyFactory().createBeanProxyWith(locationOverride.x),
								expression.getRegistry().getBeanProxyFactory().createBeanProxyWith(locationOverride.y)}, false);
			}

			feedbackController.queueInitialRefresh(changeController);
			feedbackController.queueInvalidate(this, changeController); // Also queue up an invalidate and an image refresh to occur also.
		} else if (hadControlBeanProxy && controlManagerProxy != null) {
			// We are just unsetting, don't have a new one. And we have manager active. So tell it to clear out the old.
			expression.createSimpleMethodInvoke(BeanSWTUtilities.getSetControlMethodProxy(expression), controlManagerProxy, new IProxy[] {
				null, feedbackController.getProxy()}, false);
		}
	}

	public void calledBack(int msgID, Object[] parms) {
		switch (msgID) {
			case Common.CL_RESIZED:
				componentResized(((IIntegerBeanProxy) parms[0]).intValue(), ((IIntegerBeanProxy) parms[1]).intValue());
				break;
			case Common.CL_MOVED:
				componentMoved(((IIntegerBeanProxy) parms[0]).intValue(), ((IIntegerBeanProxy) parms[1]).intValue());
				break;
			case Common.CL_REFRESHED:
				// The location is "absolute", i.e. location relative to swt.Shell the component is in.
				fLastSignalledLocation = new Point(((IIntegerBeanProxy) parms[0]).intValue(), ((IIntegerBeanProxy) parms[1]).intValue());
				fLastSignalledSize = new Dimension(((IIntegerBeanProxy) parms[2]).intValue(), ((IIntegerBeanProxy) parms[3]).intValue());
				printmoved("refreshed"); //$NON-NLS-1$
				fireComponentRefresh();
				break;
			case Common.CL_IMAGEINVALID:
				synchronized (imageAccessorSemaphore) {
					fImageValid = INVALID;
				}
				componentValidated();
				// The image for this component is invalid. Go do image refresh if we have any listeners.
				if (imSupport != null && imSupport.hasImageListeners()) {
					// Now we are finally ready for images, create the image data collector if needed.
					getImageCollector();
					invalidateImage();
					refreshImage();
				}
				break;
		}
	}

	int movedCtr;

	/**
	 * 
	 * 
	 * @since 1.1.0
	 */
	private void printmoved(String type) {
		if (VisualComponentsLayoutPolicy.DO_VC_TRACING) {
			movedCtr++;
			if (controlBeanProxy.isBeanProxy())
				System.out.println("Component " + ((IBeanProxy) controlBeanProxy).getTypeProxy().getTypeName() + "(" + hashCode() + ") cntr:" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						+ movedCtr + ' ' + type + " to:(" + fLastSignalledLocation+ ' '+fLastSignalledSize+')'); //$NON-NLS-1$
			else
				System.out.println("Component (" + hashCode() + ") cntr:" + movedCtr + ' ' + type + " to:(" + fLastSignalledLocation+ ' '+fLastSignalledSize+')'); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
	}

	/**
	 * Component resized. Subclasses may override to know when resized occurred. Should call super.
	 * 
	 * @param width
	 * @param height
	 * 
	 * @since 1.1.0
	 */
	protected void componentResized(int width, int height) {
		fLastSignalledSize = new Dimension(width, height);
		printmoved("resized"); //$NON-NLS-1$
		vcSupport.fireComponentResized(width, height);
	}

	/**
	 * Component moved. 
	 * 
	 * @param x
	 * @param y
	 * 
	 * @since 1.1.0
	 */
	protected void componentMoved(int x, int y) {
		// The location is "absolute" in that it is relative to the swt.Shell that the component is in.
		fLastSignalledLocation = new Point(x, y);
		printmoved("moved"); //$NON-NLS-1$
		vcSupport.fireComponentMoved(x, y);
	}

	/**
	 * fireComponentRefresh. Send out a refresh notification.
	 */
	protected void fireComponentRefresh() {
		vcSupport.fireComponentRefreshed();
	}


	/**
	 * fireComponentValidated. Send out a validated notification.
	 */
	protected void componentValidated() {
		vcSupport.fireComponentValidated();
	}
	
	/**
	 * Dispose the component manager.
	 * 
	 * @param expression
	 *            expression to use. It is expected the expression is set up for ForExpression.ROOT_EXPRESSION to be the next expression. It may
	 *            be <code>null</code> if the registry is gone and we are just cleaning up.
	 * 
	 * @since 1.1.0
	 */
	public void dispose(IExpression expression) {
		// Null for expression is only valid internally. It will be called only when the component manager proxy is invalid. It is
		// not considered to be valid for outside users to call with null.
		
		if (fImageDataCollector != null) {
			// Be on the safe so no spurious last minute notifications are sent out.
			fImageDataCollector.release();
			fImageDataCollector = null;
		}

		if (controlManagerProxy != null) {
			if (expression != null && controlManagerProxy.isBeanProxy() && ((IBeanProxy)controlManagerProxy).isValid()) {
				FeedbackController feedback = getFeedbackController();
				feedback.deregisterFeedbackNotifier((IBeanProxy) controlManagerProxy);
				if (getControlManagerBeanProxy().isValid()) {
					expression.createSimpleMethodInvoke(BeanSWTUtilities.getSetControlMethodProxy(expression), controlManagerProxy,
							new IProxy[] { null, feedback.getProxy()}, false);
					getControlManagerBeanProxy().getProxyFactoryRegistry().releaseProxy(getControlManagerBeanProxy());
				}
			}
			
			if (extensions != null) {
				// Dispose the extensions.
				for (int i = 0; i < extensions.size(); i++) {
					((ControlManagerExtension) extensions.get(i)).disposed(expression);
				}
			}
			locationOverride = null;
			controlManagerProxy = null;
			controlBeanProxy = null;
			fLastSignalledLocation = null;
			fLastSignalledSize = null;
			displayProxy = null;
		}
	}
	
	/**
	 * Answer whether the component manager proxy is valid or not. It is valid if not null, and is either an ExpressionProxy or is a
	 * valid IBeanProxy (i.e. the registry is still valid). It could be possible the registry is now invalid but we still have a bean. This
	 * occurs if the registry was terminated and we hadn't gotton around to releasing the component manager yet.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected boolean isComponentManagerProxyValid() {
		if (controlManagerProxy == null)
			return false;
		if (controlManagerProxy.isExpressionProxy())
			return true;
		if (((IBeanProxy) controlManagerProxy).isValid())
			return true;
		dispose(null);	// Dispose with no expression to clean it up.
		return false;
	}

	/**
	 * Get the visual component bounds. This is "absolute" by being relative to the swt.Shell that the component is in. This is different than the
	 * default bounds, and the applyBounds. Those are the true bounds used by the component. If the component has not yet sent the original refresh
	 * this will return (0,0,0,0).
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Rectangle getBounds() {

		if (fLastSignalledLocation != null && fLastSignalledSize != null) {
			if (VisualComponentsLayoutPolicy.DO_VC_TRACING)
				System.out.println("Requested bounds (" + hashCode() + ") cntr:" + movedCtr + " bounds: " + fLastSignalledLocation+' '+fLastSignalledSize); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			return new Rectangle(fLastSignalledLocation.x, fLastSignalledLocation.y, fLastSignalledSize.width, fLastSignalledSize.height);
		} else
			return new Rectangle();
	}

	/**
	 * Get the visual component location. This is "absolute" by being relative to the swt.Shell that the component is in. This is different than the
	 * default location, and the applyLocation. Those are the true locations used by the component. If the component has not yet sent back the
	 * location from the initial refresh it will return (0,0).
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Point getLocation() {

		if (fLastSignalledLocation != null)
			return fLastSignalledLocation;
		else
			return new Point();
	}

	/**
	 * Get the size of the component. It will return an empty size until the component has signaled that it is up and ready.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Dimension getSize() {

		if (fLastSignalledSize != null)
			return fLastSignalledSize;
		else
			return new Dimension();
	}


	/**
	 * Layout data needs to be applied specially so that composite manager can handle an invalid layout data setting.
	 * If this is being used as just a control manager and guarenteed that all layout data is valid, then it can
	 * be applied directly and doesn't need to go through this method.
	 * 
	 * @param layoutData
	 * @param wantOldValue
	 * @param expression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public IProxy applyLayoutData(IProxy layoutData, boolean wantOldValue, IExpression expression) {
		return expression.createSimpleMethodInvoke(BeanSWTUtilities.getApplyLayoutDataMethodProxy(expression), controlManagerProxy, new IProxy[] {layoutData}, true);
	}
	
	/**
	 * Apply the bounds using the expression, and return the old value if the wantOldValue is true. This apply will honor location override.
	 * 
	 * @param bounds
	 * @param wantOldValue
	 *            <code>true</code> if want old value returned
	 * @param expression
	 *            expression to use. It is expected the expression is set up for ForExpression.ROOT_EXPRESSION to be the next expression.
	 * @param controller
	 * @return oldValue if wantOldValue is true, or null if false.
	 * 
	 * @since 1.1.0
	 */
	public IProxy applyBounds(IProxy bounds, boolean wantOldValue, IExpression expression, ModelChangeController controller) {
		FeedbackController feedback = BeanSWTUtilities.getFeedbackController(expression, displayProxy);
		feedback.startChanges(controller, expression); // Let the server know that there is something that could cause multiple changes and that it
														// should queue up.

		IProxy oldValue;
		if (!wantOldValue)
			oldValue = null;
		else {
			oldValue = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
			expression.createClassInstanceCreation(ForExpression.ASSIGNMENT_RIGHT, expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, "org.eclipse.swt.graphics.Rectangle"), 4); //$NON-NLS-1$
			expression.createPrimitiveLiteral(ForExpression.CLASSINSTANCECREATION_ARGUMENT, 0);
			expression.createPrimitiveLiteral(ForExpression.CLASSINSTANCECREATION_ARGUMENT, 0);
			expression.createPrimitiveLiteral(ForExpression.CLASSINSTANCECREATION_ARGUMENT, 0);
			expression.createPrimitiveLiteral(ForExpression.CLASSINSTANCECREATION_ARGUMENT, 0);
		}
		expression.createSimpleMethodInvoke(BeanSWTUtilities.getApplyBoundsMethodProxy(expression), controlManagerProxy, new IProxy[] { bounds,
				oldValue}, false);
		return oldValue;
	}

	/**
	 * Apply the location using the expression, and return the old value if the wantOldValue is true. This apply will honor the location override.
	 * 
	 * @param location
	 * @param wantOldValue
	 *            <code>true</code> if want old value returned
	 * @param expression
	 *            expression to use. It is expected the expression is set up for ForExpression.ROOT_EXPRESSION to be the next expression.
	 * @param controller
	 * @return oldValue if wantOldValue is true, or null if false.
	 * 
	 * @since 1.1.0
	 */
	public IProxy applyLocation(IProxy location, boolean wantOldValue, IExpression expression, ModelChangeController controller) {
		FeedbackController feedback = BeanSWTUtilities.getFeedbackController(expression, displayProxy);
		feedback.startChanges(controller, expression); // Let the server know that there is something that could cause multiple changes and that it
														// should queue up.

		IProxy oldValue;
		if (!wantOldValue)
			oldValue = null;
		else {
			oldValue = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
			expression.createClassInstanceCreation(ForExpression.ASSIGNMENT_RIGHT, expression.getRegistry().getBeanTypeProxyFactory()
					.getBeanTypeProxy(expression, "org.eclipse.swt.graphics.Point"), 2); //$NON-NLS-1$
			expression.createPrimitiveLiteral(ForExpression.CLASSINSTANCECREATION_ARGUMENT, 0);
			expression.createPrimitiveLiteral(ForExpression.CLASSINSTANCECREATION_ARGUMENT, 0);
		}
		expression.createSimpleMethodInvoke(BeanSWTUtilities.getApplyLocationMethodProxy(expression), controlManagerProxy, new IProxy[] {
				location, oldValue}, false);
		return oldValue;
	}

	/**
	 * Override the location with this location. If the manager has not yet been instantiated, this override will be queued up and applied at
	 * instantiation time. This should not be called during instantiation of the manager because the expression is still in process. The override
	 * can't be canceled once applied until the manager has been disposed. At that time the override will be canceled.
	 * 
	 * @param point
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	public void overrideLocation(Point point, IExpression expression) {
		if (isComponentManagerProxyValid() && controlManagerProxy.isBeanProxy()) {
			// Apply directly
			ProxyFactoryRegistry registry = getControlManagerBeanProxy().getProxyFactoryRegistry();
			expression.createSimpleMethodInvoke(BeanSWTUtilities.getOverrideLocationMethodProxy(expression), getControlManagerBeanProxy(),
					new IProxy[] { registry.getBeanProxyFactory().createBeanProxyWith(point.x),
							registry.getBeanProxyFactory().createBeanProxyWith(point.y)}, false);
		}
		locationOverride = point; // Save for when instantiate occurs.
	}

	/**
	 * Get the default location. If no override applied, it returns the current location, else it returns the location at the time before the override
	 * was applied.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public IBeanProxy getDefaultLocation() {
		if (isComponentManagerProxyValid()) {
			ProxyFactoryRegistry registry = getControlManagerBeanProxy().getProxyFactoryRegistry();
			return BeanSWTUtilities.getDefaultLocationMethodProxy(registry).invokeCatchThrowableExceptions(getControlManagerBeanProxy());
		} else
			return null;
	}

	/**
	 * Get the default bounds. If no override applied, it returns the current bounds, else it returns the location at the time before the override was
	 * applied, and current size as the bounds.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public IBeanProxy getDefaultBounds() {
		if (isComponentManagerProxyValid() && controlManagerProxy.isBeanProxy()) {
			ProxyFactoryRegistry registry = getControlManagerBeanProxy().getProxyFactoryRegistry();
			return BeanSWTUtilities.getDefaultBoundsMethodProxy(registry).invokeCatchThrowableExceptions(getControlManagerBeanProxy());
		} else
			return null;
	}

	/**
	 * Get component manager proxy.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected IProxy getComponentManagerProxy() {
		return controlManagerProxy;
	}

	/**
	 * Invalidate the component. The controller is used so that the invalidation/validation is gathered up and only done once at the end.
	 * 
	 * @param controller
	 * 
	 * @since 1.1.0
	 */
	public void invalidate(ModelChangeController controller) {
		FeedbackController feedback = getFeedbackController();
		feedback.queueInvalidate(this, controller);
	}

	/**
	 * Get the feedback controller for this manager. This is only valid after this manager has been
	 * initialized with a control bean.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public FeedbackController getFeedbackController() {
		return BeanSWTUtilities.getFeedbackController(getRegistry(), displayProxy);
	}

	/**
	 * Get the registry that this manager is created under.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	private ProxyFactoryRegistry getRegistry() {
		return controlManagerProxy.isBeanProxy() ? ((IBeanProxy) controlManagerProxy).getProxyFactoryRegistry()
				: ((ExpressionProxy) controlManagerProxy).getExpression().getRegistry();
	}

	/*
	 * Do actual invalidation. This is called only from the feedback controller.
	 */
	private void invalidate(IExpression expression) {
		if (isComponentManagerProxyValid())
			expression.createSimpleMethodInvoke(BeanSWTUtilities.getControlInvalidate(expression), controlManagerProxy, null, false);
	}

	/**
	 * Add image listener.
	 * 
	 * @param listener
	 * 
	 * @see IImageNotifier#addImageListener(IImageListener)
	 * @since 1.1.0
	 */
	public void addImageListener(IImageListener listener) {
		if (imSupport == null)
			imSupport = new ImageNotifierSupport();
		if (listener instanceof IControlImageListener) {
			if (controlImageListeners == null)
				controlImageListeners = new ListenerList(ListenerList.IDENTITY);
			controlImageListeners.add(listener);
		}
		imSupport.addImageListener(listener);
		if (controlBeanProxy != null && controlBeanProxy.isBeanProxy())
			getImageCollector(); // Start up image collecting, we have listeners and we have a live bean. They go together that way.
	}

	/**
	 * Remove image listener.
	 * 
	 * @param listener
	 * 
	 * @see IImageNotifier#removeImageListener(IImageListener)
	 * @since 1.1.0
	 */
	public void removeImageListener(IImageListener listener) {
		if (imSupport != null)
			imSupport.removeImageListener(listener);
		if (listener instanceof IControlImageListener && controlImageListeners != null)
			controlImageListeners.remove(listener);
	}

	/**
	 * Has image listeners?
	 * 
	 * @return
	 * 
	 * @see IImageNotifier#hasImageListeners()
	 * @since 1.1.0
	 */
	public boolean hasImageListeners() {
		if (imSupport != null)
			return imSupport.hasImageListeners();
		else
			return false;
	}

	/**
	 * Invalidate the image. This just marks the current image as invalid. It causes no other action.
	 * 
	 * @see IImageNotifier#invalidateImage()
	 * @since 1.1.0
	 */
	public void invalidateImage() {
		synchronized (imageAccessorSemaphore) {
			if (fImageDataCollector != null && fImageValid != WAITING_FIRST_INVALIDATION) {
				if (fImageValid == INVALID_COLLECTING && fImageDataCollector.isCollectingData())
					fImageDataCollector.abort(false); // We're currently do a valid collection, so abort it.
				fImageValid = INVALID; // Mark it as invalid.
			}
		}
	}

	private ImageDataCollector getImageCollector() {
		if (fImageDataCollector == null) {
			synchronized (imageAccessorSemaphore) {
				if (fImageDataCollector == null) {
					fImageDataCollector = new ImageDataCollector(getRegistry());
				}
			}
		}
		return fImageDataCollector;
	}

	/*
	 * fire the image status to the component image listeners.
	 */
	private void fireImageStatus(int status) {
		Object[] listeners = controlImageListeners.getListeners();
		for (int i = 0; i < listeners.length; i++) {
			((IControlImageListener) listeners[i]).imageStatus(status);
		}
	}

	/*
	 * fire the image exception to the component image listeners.
	 */
	private void fireImageException(ThrowableProxy exception) {
		Object[] listeners = controlImageListeners.getListeners();
		for (int i = 0; i < listeners.length; i++) {
			((IControlImageListener) listeners[i]).imageException(exception);
		}
	}

	/**
	 * Refresh the image. This will not wait for the image complete refreshing. It will just start it. The image will notify when it is done through
	 * the image notifier.
	 * 
	 * @see IImageNotifier#refreshImage()
	 * @since 1.1.0
	 */
	public void refreshImage() {
		// Don't do refresh if we have no image support or listeners yet. Waste of time in that case.
		// Don't do refresh if we don't have a valid component to get an image of.
		if (imSupport != null && imSupport.hasImageListeners() && controlBeanProxy.isBeanProxy() && ((IBeanProxy) controlBeanProxy).isValid()) {
			// Need to capture validity before going in and start because there would be a deadlock while
			// waiting for completion because it would tie up "this" while DataCollectedRunnable would also
			// try to synchronize on "this" and it couldn't because we had and are waiting in waitForCompletion.
			boolean doRefresh = false;
			synchronized (imageAccessorSemaphore) {
				if (fImageValid != WAITING_FIRST_INVALIDATION) {
					doRefresh = fImageValid == INVALID && fImageDataCollector != null;
					if (doRefresh)
						fImageValid = INVALID_COLLECTING;
				}
			}
			if (doRefresh) {
				try {
					// Wait if running so that we don't start it while running. Must be outside of sync block because completion requires syncing on a
					// separate thread. Would have a deadlock then
					getImageCollector().abort(true);

					// Sync back so that no one else can come in until the collection has been started
					synchronized (imageAccessorSemaphore) {
						getImageCollector().startComponent(getControlBeanProxy(), new ImageDataCollector.DataCollectedRunnable() {

							public void imageData(ImageData data, int status) {
								synchronized (imageAccessorSemaphore) {
									fImageValid = VALID;
								}
								fireImageStatus(status);
								imSupport.fireImageChanged(data);
							}

							public void imageNotCollected(int status) {
								synchronized (imageAccessorSemaphore) {
									fImageValid = INVALID; // Invalid, but no longer collecting.
								}
								fireImageStatus(status);
							}

							public void imageException(ThrowableProxy exception) {
								synchronized (imageAccessorSemaphore) {
									fImageValid = INVALID; // Invalid, but no longer collecting.
								}
								fireImageException(exception);
							}
						});
					}
				} catch (ThrowableProxy e) {
					synchronized (imageAccessorSemaphore) {
						fImageValid = INVALID;
					}
					JavaVEPlugin.log(e, Level.WARNING);
					fireImageException(e);
				}
			}
		}

	}

	/**
	 * The feedback controller. There is one per display per registry. It is retrieved from the BeanSwtUtilities. Once created it will stay around, even when all
	 * components are released.
	 * <p>
	 * The purposes of this controller is to allow batching of requests when more than one component manager can be involved in a transaction so that
	 * transactions can be minimized. It handles:
	 * <ul>
	 * <li>All callbacks from the proxy side are funneled through the proxy side's feedback controller and queued up to the most appropriate time and
	 * will then send them back through this controller in one transaction. This controller will then distribute the callbacks to the appropriate
	 * component managers.
	 * <li>All {@link ControlManager#setComponentBeanProxy(CompomentManagerExtension, IExpression, ModelChangeController) componentManagersetComponent}calls queue up
	 * on the proxy side a request for an initial refresh (send back of initial bounds). This class will, through
	 * {@link FeedbackController#queueInitialRefresh(ModelChangeController) queueInitialRefresh}, add to the change controller an end of transaction
	 * request. This request at the end of the transaction will then ask the feedback controller on the proxy side to send all in one transaction all
	 * of the pending refreshes.
	 * <li>All {@link ControlManager#invalidate(ModelChangeController) componentManager.invalidate}requests will be queued up through the change
	 * controller. Then at end of transaction these pending invalidates (which there could be more than one per component, but they are compacted into
	 * one request per component) will be sent in one transaction to the proxy component managers. Also in this transaction, the proxy feedback
	 * controller is told to send back the invalid images that have been accumulated (see the next bullet).
	 * <li>When the proxy component managers receive the invalidate requests, they queue up on the proxy feedback controller that the component now
	 * has an invalid image. They also walk their parent chain to mark the parent as invalid image too. Then at the end of all of the invalidates, a
	 * notification is sent back from the proxy feedback controller for each invalid image in one transaction. This is received and distributed to
	 * each on the respective component managers.
	 * </ul>
	 * 
	 * @since 1.1.0
	 */
	protected static class FeedbackController implements ICallback {
		
		private Map managerProxyToNotifier = new HashMap(); // Map from proxy to notifier.

		private IProxy feedbackControllerProxy;

		private IMethodProxy postInitialRefresh;

		private IMethodProxy postInvalidImages;

		// Note: This is an IProxyMethod because it actually can be called BEFORE the expression that created the
		// feedback controller has been evaluated. So we need to be able to use it in that case.
		private IProxyMethod startingChanges;

		private IMethodProxy postChangesDone;

		private boolean changesHeld; // Are we holding changes.

		private Set pendingInvalidates = new HashSet(); // Set of componentManagers that have invalidates pending.

		/**
		 * Construct with the feedproxy.
		 * 
		 * @param feedbackControllerProxy
		 * 
		 * @since 1.1.0
		 */
		public FeedbackController(ExpressionProxy feedbackControllerProxy) {
			this.feedbackControllerProxy = feedbackControllerProxy;
			feedbackControllerProxy.addProxyListener(new ExpressionProxy.ProxyListener() {

				public void proxyResolved(ProxyEvent event) {
					FeedbackController.this.feedbackControllerProxy = event.getProxy();
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyListener#proxyNotResolved(org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent)
				 */
				public void proxyNotResolved(ProxyEvent event) {
					FeedbackController.this.feedbackControllerProxy = null;
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyListener#proxyVoid(org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent)
				 */
				public void proxyVoid(ProxyEvent event) {
					FeedbackController.this.feedbackControllerProxy = null;
				}
			});

			IProxyMethod postInitial = feedbackControllerProxy.getExpression().getRegistry().getMethodProxyFactory().getMethodProxy(
					feedbackControllerProxy.getExpression(), BeanSWTUtilities.CONTROLMANAGERFEEDBACK_CLASSNAME, //$NON-NLS-1$
					"postInitialRefresh", null); //$NON-NLS-1$
			if (postInitial.isBeanProxy()) {
				postInitialRefresh = (IMethodProxy) postInitial;
			} else {
				((ExpressionProxy) postInitial).addProxyListener(new ExpressionProxy.ProxyAdapter() {

					public void proxyResolved(ProxyEvent event) {
						postInitialRefresh = (IMethodProxy) event.getProxy();
					}
				});
			}

			IProxyMethod postImages = feedbackControllerProxy.getExpression().getRegistry().getMethodProxyFactory().getMethodProxy(
					feedbackControllerProxy.getExpression(), BeanSWTUtilities.CONTROLMANAGERFEEDBACK_CLASSNAME, //$NON-NLS-1$
					"postInvalidImages", null); //$NON-NLS-1$
			if (postImages.isBeanProxy()) {
				postInvalidImages = (IMethodProxy) postImages;
			} else {
				((ExpressionProxy) postImages).addProxyListener(new ExpressionProxy.ProxyAdapter() {

					public void proxyResolved(ProxyEvent event) {
						postInvalidImages = (IMethodProxy) event.getProxy();
					}
				});
			}

			startingChanges = feedbackControllerProxy.getExpression().getRegistry().getMethodProxyFactory().getMethodProxy(
					feedbackControllerProxy.getExpression(), BeanSWTUtilities.CONTROLMANAGERFEEDBACK_CLASSNAME, //$NON-NLS-1$
					"startingChanges", null); //$NON-NLS-1$
			if (startingChanges.isExpressionProxy()) {
				((ExpressionProxy) startingChanges).addProxyListener(new ExpressionProxy.ProxyAdapter() {

					public void proxyResolved(ProxyEvent event) {
						startingChanges = (IProxyMethod) event.getProxy();
					}
				});
			}

			IProxyMethod postChangesDone = feedbackControllerProxy.getExpression().getRegistry().getMethodProxyFactory().getMethodProxy(
					feedbackControllerProxy.getExpression(), BeanSWTUtilities.CONTROLMANAGERFEEDBACK_CLASSNAME, //$NON-NLS-1$
					"postChanges", null); //$NON-NLS-1$
			if (postChangesDone.isBeanProxy()) {
				this.postChangesDone = (IMethodProxy) postChangesDone;
			} else {
				((ExpressionProxy) postChangesDone).addProxyListener(new ExpressionProxy.ProxyAdapter() {

					public void proxyResolved(ProxyEvent event) {
						FeedbackController.this.postChangesDone = (IMethodProxy) event.getProxy();
					}
				});
			}

		}

		/**
		 * Called to start changes being made. It will tell server to send all of the queued changes at end of transaction.
		 * 
		 * @param controller
		 * @param expression
		 * 
		 * @since 1.1.0
		 */
		void startChanges(ModelChangeController controller, IExpression expression) {
			if (!changesHeld) {
				changesHeld = true;
				expression.createSimpleMethodInvoke(startingChanges, feedbackControllerProxy, null, false);
				controller.execAtEndOfTransaction(changesDoneRunnable, changesDoneRunnable);
			}
		}

		private Runnable changesDoneRunnable = new Runnable() {

			public void run() {
				changesHeld = false;
				// This shouldn't occur, but test that we have a valid proxy. We may not.
				if (feedbackControllerProxy != null) {
					if (feedbackControllerProxy.isBeanProxy()) {
						if (postChangesDone != null) {
							postChangesDone.invokeCatchThrowableExceptions((IBeanProxy) feedbackControllerProxy);
						} else {
							// Something was wrong, we didn't get the initial refresh proxy.
							JavaVEPlugin.log("swt.FeedbackComponentManager didn't resolve postChanges method!", Level.WARNING); //$NON-NLS-1$
						}
					} else {
						// We're still not valid. This shouldn't of happened. We should of always been within a transaction. We should not be
						// creating component manager's outside of a transaction.
						JavaVEPlugin.log("swt.FeedbackComponentManager didn't resolve itself!", Level.WARNING); //$NON-NLS-1$
					}
				}
			}
		};

		/**
		 * This is called by the component manager to queue up the request for initial refresh. It will put at the end of the current transaction to
		 * tell the remote vm to gather all of the initial refreshes and send them back.
		 * <p>
		 * The vm side of the feedback controller is accumulating all new component sets waiting for this initial refresh request. Then when the
		 * transaction is finished, it will take all of the pending refreshes and send them all back in one transaction.
		 * 
		 * @since 1.1.0
		 */
		void queueInitialRefresh(ModelChangeController controller) {
			controller.execAtEndOfTransaction(initialRefreshRunnable, initialRefreshRunnable);
		}

		private Runnable initialRefreshRunnable = new Runnable() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.lang.Runnable#run()
			 */
			public void run() {
				// This shouldn't occur, but test that we have a valid proxy. We may not.
				if (feedbackControllerProxy != null) {
					if (feedbackControllerProxy.isBeanProxy()) {
						if (postInitialRefresh != null) {
							postInitialRefresh.invokeCatchThrowableExceptions((IBeanProxy) feedbackControllerProxy);
						} else {
							// Something was wrong, we didn't get the initial refresh proxy.
							JavaVEPlugin.log("swt.FeedbackComponentManager didn't resolve postInitialRefresh method!", Level.WARNING); //$NON-NLS-1$
						}
					} else {
						// We're still not valid. This shouldn't of happened. We should of always been within a transaction. We should not be
						// creating component manager's outside of a transaction.
						JavaVEPlugin.log("swt.FeedbackComponentManager didn't resolve itself!", Level.WARNING); //$NON-NLS-1$
					}
				}
			}
		};

		/**
		 * Return the proxy for the feedback controller.
		 * 
		 * @return
		 * 
		 * @since 1.1.0
		 */
		public IProxy getProxy() {
			return feedbackControllerProxy;
		}

		/**
		 * Register the proxy for the notifier so that the controller knows who's who.
		 * 
		 * @param notifier
		 * @param notifierProxy
		 * 
		 * @since 1.1.0
		 */
		public void registerFeedbackNotifier(ControlManagerFeedbackControllerNotifier notifier, IBeanProxy notifierProxy) {
			managerProxyToNotifier.put(notifierProxy, notifier);
		}

		/**
		 * Deregister the manager.
		 * 
		 * @param notifierProxy
		 * 
		 * @since 1.1.0
		 */
		public void deregisterFeedbackNotifier(IBeanProxy notifierProxy) {
			managerProxyToNotifier.remove(notifierProxy);
		}

		/**
		 * Queue an invalidate for this component.
		 * 
		 * @param manager
		 * @param controller
		 * 
		 * @since 1.1.0
		 */
		void queueInvalidate(ControlManager manager, ModelChangeController controller) {
			pendingInvalidates.add(manager);
			invalidate(controller);
		}
		
		/**
		 * Call this to indicate there may be side-effect invalidates that will occur. Ones
		 * that are directly controlled. This will just queue up that at end of transaction
		 * any invalid images should be sent back. The remote vm will handle which images
		 * are invalid. This most likely occurs because a widget was deleted that was
		 * not under direct control of a component manager.
		 * 
		 * @param controller
		 * 
		 * @since 1.2.0
		 */
		public void invalidate(ModelChangeController controller) {
			controller.execAtEndOfTransaction(invalidateRunnable, invalidateRunnable);
		}

		private Runnable invalidateRunnable = new Runnable() {

			public void run() {
				if (feedbackControllerProxy != null && feedbackControllerProxy.isBeanProxy() && ((IBeanProxy) feedbackControllerProxy).isValid()) {
					IExpression exp = ((IBeanProxy) feedbackControllerProxy).getProxyFactoryRegistry().getBeanProxyFactory().createExpression();
					try {
						for (Iterator itr = pendingInvalidates.iterator(); itr.hasNext();) {
							ControlManager manager = (ControlManager) itr.next();
							manager.invalidate(exp);
						}
						pendingInvalidates.clear();
						exp.createSimpleMethodInvoke(postInvalidImages, feedbackControllerProxy, null, false);
						exp.invokeExpression();
					} catch (IllegalStateException e) {
						JavaVEPlugin.log(e, Level.WARNING);
					} catch (ThrowableProxy e) {
						JavaVEPlugin.log(e, Level.WARNING);
					} catch (NoExpressionValueException e) {
						JavaVEPlugin.log(e, Level.WARNING);
					} finally {
						exp.close();
					}
				}
			}
		};

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jem.internal.proxy.core.ICallback#calledBack(int, org.eclipse.jem.internal.proxy.core.IBeanProxy)
		 */
		public Object calledBack(int msgID, IBeanProxy parm) {
			throw new RuntimeException("A component listener has been called back incorrectly"); //$NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jem.internal.proxy.core.ICallback#calledBack(int, java.lang.Object[])
		 */
		public Object calledBack(int msgID, Object[] parms) {
			if (msgID == Common.CL_TRANSACTIONS) {
				if (VisualComponentsLayoutPolicy.DO_VC_TRACING)
					System.out.println("Start feedback transaction. #trans=" + parms.length / 3); //$NON-NLS-1$
				// This will be called with parms. They will be 3-tuples of (ComponentManagerProxy, callbackID, parms);
				for (int i = 0; i < parms.length;) {
					ControlManagerFeedbackControllerNotifier notifier = (ControlManagerFeedbackControllerNotifier) managerProxyToNotifier.get(parms[i++]);
					if (notifier != null) {
						notifier.calledBack(((IIntegerBeanProxy) parms[i++]).intValue(), (Object[]) parms[i++]);
					} else
						i+=2;	// To compensate for missing manager.
				}
				if (VisualComponentsLayoutPolicy.DO_VC_TRACING)
					System.out.println("Stop feedback transaction."); //$NON-NLS-1$
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jem.internal.proxy.core.ICallback#calledBack(int, java.lang.Object)
		 */
		public Object calledBack(int msgID, Object parm) {
			throw new RuntimeException("A component listener has been called back incorrectly"); //$NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jem.internal.proxy.core.ICallback#calledBackStream(int, java.io.InputStream)
		 */
		public void calledBackStream(int msgID, InputStream is) {
			throw new RuntimeException("A component listener has been called back incorrectly"); //$NON-NLS-1$
		}
	}
	
	public Point getAbsoluteLocation() {
		return getLocation();
	}
}
