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
 *  $RCSfile: ComponentProxyAdapter.java,v $
 *  $Revision: 1.22 $  $Date: 2005-05-18 18:39:17 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.graphics.ImageData;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.initParser.tree.NoExpressionValueException;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

import org.eclipse.ve.internal.jfc.common.ImageDataConstants;

/**
 * Proxy Adapter for AWT Components.
 * 
 * @since 1.1.0
 */
public class ComponentProxyAdapter extends BeanProxyAdapter2 implements IVisualComponent {

	private ComponentManager fComponentManager;

	// Need these features often, but they depend upon the class we are in,
	// can't get them as statics because they would be different for each Eclipse project.
	protected EStructuralFeature sfComponentVisible, sfComponentLocation, sfComponentBounds, sfComponentSize;

	private static final Object IMAGE_DATA_COLLECTION_ERROR_KEY = new Object();

	/**
	 * Construct the proxy adapter.
	 * 
	 * @param domain
	 * 
	 * @since 1.1.0
	 */
	public ComponentProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	/**
	 * Create the component manager. Subclasses that require a difference component manager should return the different one.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected ComponentManager createComponentManager() {
		return new ComponentManager();
	}

	/**
	 * Get the component manager. This should be used for all access so that it is lazily created.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected final ComponentManager getComponentManager() {
		if (fComponentManager == null)
			fComponentManager = createComponentManager();
		return fComponentManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.emf.common.notify.Adapter#setTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null) {
			sfComponentVisible = JavaInstantiation.getSFeature((IJavaObjectInstance) newTarget, JFCConstants.SF_COMPONENT_VISIBLE);
			sfComponentLocation = JavaInstantiation.getSFeature((IJavaObjectInstance) newTarget, JFCConstants.SF_COMPONENT_LOCATION);
			sfComponentBounds = JavaInstantiation.getSFeature((IJavaObjectInstance) newTarget, JFCConstants.SF_COMPONENT_BOUNDS);
			sfComponentSize = JavaInstantiation.getSFeature((IJavaObjectInstance) newTarget, JFCConstants.SF_COMPONENT_SIZE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter2#primInstantiateBeanProxy(org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected IProxy primInstantiateBeanProxy(IExpression expression) throws AllocationException {
		if (ffHost != null) {
			// We are on the freeform, instantiate for this. This needs to be done for each instantiation because it is reset in the release.
			overrideVisibility(true, expression);
			overrideLocation(new Point());
		}

		IProxy newbean = super.primInstantiateBeanProxy(expression);

		if (isThisPart()) {
			// KLUDGE: Something special for component and "this" part. Since Component is Abstract, someone may of tried create an abstract subclass.
			// In that case
			// the bean proxy that comes in would be for Object. So what we will do is since we are not a component, we will throw away the
			// object that came in and change it a Canvas (that way something will display).
			// Don't need to worry about any other kind of class because all of the subclasses of Component that we are implementing
			// are non-abstract.
			// 
			// If we find one of the notinstantiated classes is component, then we know we went to far, so we will recreate it
			// as gray canvas.
			//   newBean = new java.awt.Canvas();
			//   newBean.setBackground(lightGray);
			//   return newBean;
			for (int i = 0; i < notInstantiatedClasses.size(); i++) {
				if (((JavaHelpers) notInstantiatedClasses.get(i)).getQualifiedName().equals("java.awt.Component")) { //$NON-NLS-1$
					expression.createProxyReassignmentExpression(ForExpression.ROOTEXPRESSION, (ExpressionProxy) newbean);
					IProxyBeanType componentType = getBeanTypeProxy("java.awt.Component", expression); //$NON-NLS-1$
					expression.createClassInstanceCreation(ForExpression.ASSIGNMENT_RIGHT, getBeanTypeProxy("java.awt.Canvas", expression), 0); //$NON-NLS-1$
					expression.createMethodInvocation(ForExpression.ROOTEXPRESSION, componentType.getMethodProxy(expression, "setBackground", //$NON-NLS-1$
							new String[] { "java.awt.Color"}), true, 1); //$NON-NLS-1$
					expression.createProxyExpression(ForExpression.METHOD_RECEIVER, newbean);
					expression.createFieldAccess(ForExpression.METHOD_ARGUMENT, "lightGray", true); //$NON-NLS-1$
					expression.createTypeReceiver(getBeanTypeProxy("java.awt.Color", expression)); //$NON-NLS-1$
					notInstantiatedClasses.remove(notInstantiatedClasses.size() - 1); // The last one WILL be for Component. We want to be able to
					// apply component settings.
					break;
				}
			}
		}

		getComponentManager().setComponentBeanProxy(newbean, expression, getModelChangeController());

		if (ffHost != null) {
			// Now finish the instantiation on the freeform.
			try {
				// try {
				//   .. do freeform stuff ..
				expression.createTry();
				ffProxy = ffHost.add(this, newbean, expression);

				// Set the initial use component size.
				ffProxy.useComponentSize(newbean, getEObject().eIsSet(sfComponentBounds) || getEObject().eIsSet(sfComponentSize), expression);
				//   } catch (Exception e) {
				//     ... send back thru ExpressionProxy to mark an instantiation error ...
				//   }
				ExpressionProxy expProxy = expression.createTryCatchClause(getBeanTypeProxy("java.lang.Exception", expression), true); //$NON-NLS-1$
				expProxy.addProxyListener(new ExpressionProxy.ProxyAdapter() {

					public void proxyResolved(ProxyEvent event) {
						ThrowableProxy throwableProxy = (ThrowableProxy) event.getProxy();
						processInstantiationError(new BeanExceptionError(throwableProxy, ERROR_SEVERE));
					}
				});
				expression.createTryEnd();
			} catch (IllegalStateException e) {
				// This means there was a expression creation error occured in the freeform stuff, treat as instantiation error.and so is not
				// recoverable.
				// Log this as the instantiate error. This shouldn't occur.
				JavaVEPlugin.log(e, Level.SEVERE);
				processInstantiationError(new ExceptionError(e, ERROR_SEVERE));
			}
		}
		return newbean;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IVisualComponent#addComponentListener(org.eclipse.ve.internal.cde.core.IVisualComponentListener)
	 */
	public synchronized void addComponentListener(IVisualComponentListener aListener) {
		getComponentManager().addComponentListener(aListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IVisualComponent#removeComponentListener(org.eclipse.ve.internal.cde.core.IVisualComponentListener)
	 */
	public void removeComponentListener(IVisualComponentListener aListener) {
		if (fComponentManager != null)
			getComponentManager().removeComponentListener(aListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.jfc.core.IComponentProxyHost#getVisualComponentBeanProxy()
	 */
	public IBeanProxy getVisualComponentBeanProxy() {
		return getBeanProxy();
	}

	private IImageListener imageListener; // A local one used to process image errors.

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IImageNotifier#addImageListener(org.eclipse.ve.internal.cde.core.IImageListener)
	 */
	public synchronized void addImageListener(IImageListener aListener) {
		if (imageListener == null) {
			imageListener = new ComponentManager.IComponentImageListener() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.ve.internal.jfc.core.ComponentManager.IComponentImageListener#imageStatus(int)
				 */
				public void imageStatus(int status) {
					switch (status) {
						case ImageDataConstants.COMPONENT_IMAGE_CLIPPED:
							// Bit of kludge, but if image clipped, create an info message for it.
							ErrorType err = new MessageError(VisualMessages.getString("ComponentProxyAdapter.Picture_too_large_WARN_"), //$NON-NLS-1$
									IErrorHolder.ERROR_INFO);
							processError(err, IMAGE_DATA_COLLECTION_ERROR_KEY);
							break;
						default:
							// Other status, even aborted or error we don't register as an error. We just clear it.
							clearError(IMAGE_DATA_COLLECTION_ERROR_KEY);
							break;
					}
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.ve.internal.jfc.core.ComponentManager.IComponentImageListener#imageException(org.eclipse.jem.internal.proxy.core.ThrowableProxy)
				 */
				public void imageException(final ThrowableProxy exception) {
					String eMsg = exception.getProxyLocalizedMessage();
					if (eMsg == null) {
						// No localized msg. Get the exception type.
						IBeanTypeProxy eType = exception.getTypeProxy();
						eMsg = MessageFormat
								.format(
										VisualMessages.getString("ComponentProxyAdapter.Image_collection_exception_EXC_"), new Object[] { eType.getTypeName()}); //$NON-NLS-1$
					}
					ErrorType err = new MessageError(MessageFormat.format(VisualMessages
							.getString("ComponentProxyAdapter.Image_collection_failed_ERROR_"), new Object[] { eMsg}), IErrorHolder.ERROR_INFO); //$NON-NLS-1$
					processError(err, IMAGE_DATA_COLLECTION_ERROR_KEY);
				}

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.ve.internal.cde.core.IImageListener#imageChanged(org.eclipse.swt.graphics.ImageData)
				 */
				public void imageChanged(ImageData imageData) {
				}
			};
		}
		getComponentManager().addImageListener(imageListener);
		getComponentManager().addImageListener(aListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IImageNotifier#removeImageListener(org.eclipse.ve.internal.cde.core.IImageListener)
	 */
	public synchronized void removeImageListener(IImageListener aListener) {
		if (fComponentManager != null) {
			// KLUDGE Remove our image listener first, so that after we remove this input listener we can see if there
			// still any listeners. If there are, we add ours back. If not we don't add ours back. This is because we
			// don't want to get images sent if we are the only one listening. Our listener is simply to get errors
			// back if someone is asking for images.
			if (imageListener != null)
				getComponentManager().removeImageListener(imageListener);
			getComponentManager().removeImageListener(aListener);
			if (imageListener != null && getComponentManager().hasImageListeners())
				getComponentManager().addImageListener(imageListener);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter2#applyBeanProperty(org.eclipse.jem.internal.beaninfo.PropertyDecorator,
	 *      org.eclipse.jem.internal.proxy.core.IProxy, org.eclipse.jem.internal.proxy.core.IExpression, boolean)
	 */
	protected IProxy applyBeanProperty(PropertyDecorator propertyDecorator, IProxy settingProxy, IExpression expression, boolean getOriginalValue)
			throws NoSuchMethodException, NoSuchFieldException {
		// Override for loc and bounds so that it goes through the component manager.
		if (propertyDecorator.getEModelElement() == sfComponentBounds)
			return getComponentManager().applyBounds(settingProxy, getOriginalValue, expression, getModelChangeController());
		else if (propertyDecorator.getEModelElement() == sfComponentLocation)
			return getComponentManager().applyLocation(settingProxy, getOriginalValue, expression, getModelChangeController());
		else
			return super.applyBeanProperty(propertyDecorator, settingProxy, expression, getOriginalValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter2#cancelSetting(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int,
	 *      org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected void cancelSetting(EStructuralFeature feature, Object oldValue, int index, IExpression expression) {
		if (feature == sfComponentBounds && (getEObject().eIsSet(sfComponentSize) || getEObject().eIsSet(sfComponentLocation)))
			return; // Don't apply the cancel for bounds because loc and size are set and this would wipe them out.
		else if ((feature == sfComponentSize || feature == sfComponentLocation) && getEObject().eIsSet(sfComponentBounds))
			return; // Don't apply the cancel for size or loc because bounds is set and this would wipe that setting out.
		else
			super.cancelSetting(feature, oldValue, index, expression);
	}

	/**
	 * Override the location with this location. This is used when on the freeform. This is so that the true location on the freeform does not filter
	 * through to the settings of location/bounds in the java object.
	 * 
	 * @param loc
	 * 
	 * @since 1.1.0
	 */
	public void overrideLocation(Point loc) {
		getComponentManager().overrideLocation(loc);
	}

	/**
	 * Override the visibility with this setting. An example of this is used when on the freeform for non-window components to make sure they are
	 * visible no matter what the original setting and any subsequent settings are. This must be called before instantiation.
	 * 
	 * @param visibility
	 * @param expression expression to use if the component is already instantiated. If not yet instantiated then this parm will be ignored.
	 * 
	 * @since 1.1.0
	 */
	public void overrideVisibility(boolean visibility, IExpression expression) {
		overrideProperty(sfComponentVisible, getBeanProxyFactory().createBeanProxyWith(visibility), expression);
	}
	
	/**
	 * Remove the override of the visibility.
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	public void removeVisibilityOverride(IExpression expression) {
		removeOverrideProperty(sfComponentVisible, expression);
	}

	protected void primReleaseBeanProxy(IExpression expression) {

		if (ffProxy != null) {
			if (expression != null) {
				expression.createTry();
				ffProxy.remove(getBeanProxy(), expression);
				expression.createTryCatchClause("java.lang.RuntimeException", false);	//$NON-NLS-1$
				expression.createTryEnd();
			}
			ffProxy = null;
		}

		if (imageListener != null) {
			clearError(IMAGE_DATA_COLLECTION_ERROR_KEY); // In case one is hanging around.
		}

		if (fComponentManager != null) {
			// Be on the safe so no spurious last minute notifications are sent out.
			if (expression != null) {
				expression.createTry();
				fComponentManager.dispose(expression);
				expression.createTryCatchClause("java.lang.RuntimeException", false);	//$NON-NLS-1$
				expression.createTryEnd();
			} else
				fComponentManager.dispose(null);	// Give it a chance to clean up without an expression.
			// Note: Do not get rid of the component manager. This bean may of had component listeners
			// and this bean may be about to be reinstantiated. We don't want to loose the listeners
			// when the reinstantiation occurs.
		}

		// We need to dispose of stuff above before we do the super.release because by the time the
		// release comes back the bean proxy will of been released and can't be used. It is needed
		// to do the above disposes.
		super.primReleaseBeanProxy(expression);
	}

	public IProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyAttribute, IExpression exp, ForExpression forExpression) {
		// Override for loc and bounds so that it goes through the component manager.
		// Even though an expression is past in, we aren't using it here. Probably could in the future.
		if (aBeanPropertyAttribute == sfComponentBounds)
			return getComponentManager().getDefaultBounds();
		else if (aBeanPropertyAttribute == sfComponentLocation)
			return getComponentManager().getDefaultLocation();
		else
			return super.getBeanPropertyProxyValue(aBeanPropertyAttribute, exp, forExpression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IVisualComponent#getBounds()
	 */
	public Rectangle getBounds() {
		if (isBeanProxyInstantiated()) {
			return fComponentManager.getBounds();
		} else {
			// No proxy. Either called too soon, or there was an instantiation error and we can't get
			// a live component. So return just a default.
			return new Rectangle(0, 0, 0, 0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IVisualComponent#getAbsoluteLocation()
	 */
	public Point getAbsoluteLocation() {
		return getLocation(); // For components, the location and the absolute location are the same.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IVisualComponent#getLocation()
	 */
	public Point getLocation() {
		if (isBeanProxyInstantiated()) {
			return getComponentManager().getLocation();
		} else {
			// No proxy. Either called too soon, or there was an instantiation error and we can't get
			// a live component. So return just a default.
			return new Point(0, 0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IVisualComponent#getSize()
	 */
	public Dimension getSize() {
		if (isBeanProxyInstantiated()) {
			return getComponentManager().getSize();
		} else {
			// No proxy. Either called too soon, or there was an instantiation error and we can't get
			// a live component. So return just a default.
			return new Dimension(20, 20);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IImageNotifier#hasImageListeners()
	 */
	public boolean hasImageListeners() {
		return (fComponentManager != null && getComponentManager().hasImageListeners());
	}

	protected FreeFormComponentsHost.FreeFormComponentProxy ffProxy;

	protected FreeFormComponentsHost ffHost;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.IInternalBeanProxyHost2#addToFreeForm(org.eclipse.ve.internal.java.core.CompositionProxyAdapter)
	 */
	public void addToFreeForm(CompositionProxyAdapter compositionAdapter) {
		ffHost = (FreeFormComponentsHost) compositionAdapter.getFreeForm(FreeFormComponentsHost.class);
		if (ffHost == null) {
			// Doesn't exist yet, need to create it.
			ffHost = new FreeFormComponentsHost(getBeanProxyDomain().getEditDomain(), compositionAdapter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter2#removeFromFreeForm()
	 */
	public void removeFromFreeForm() {
		ffHost = null; // No longer on freeform.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter2#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification notification) {
		if (ffProxy != null) {
			// May be changing useComponentSize setting. This must be done before the
			// actual apply/cancel so that it is set for when the apply occurs. Otherwise
			// timing could cause it to think it should pack to the wrong size.
			EStructuralFeature sf = (EStructuralFeature) notification.getFeature();
			if (sf == sfComponentBounds || sf == sfComponentSize) {
				switch (notification.getEventType()) {
					case Notification.SET:
						if (!CDEUtilities.isUnset(notification)) {
							if (!notification.wasSet()) {
								if (isBeanProxyInstantiated()) {
									// TODO See if we can actually group the expression up to all notifications for this transaction instead of just
									// this one notification.
									IExpression expression = getBeanProxyFactory().createExpression();
									try {
										ffProxy.useComponentSize(getBeanProxy(), true, expression);
									} finally {
										try {
											if (expression.isValid())
												expression.invokeExpression();
											else
												expression.close();
										} catch (IllegalStateException e) {
											// Shouldn't occur. Should be taken care of in applied.
											JavaVEPlugin.log(e, Level.WARNING);
										} catch (ThrowableProxy e) {
											// Shouldn't occur. Should be taken care of in applied.
											JavaVEPlugin.log(e, Level.WARNING);
										} catch (NoExpressionValueException e) {
											// Shouldn't occur. Should be taken care of in applied.
											JavaVEPlugin.log(e, Level.WARNING);
										}
									}
								}
							}
							break;
						} // Else flow into unset.
					case Notification.UNSET:
						if (isBeanProxyInstantiated()) {
							// Little tricker, need to see if the other setting is still set.
							if (sf == sfComponentBounds)
								if (getEObject().eIsSet(sfComponentSize))
									break; // The other is still set, so leave alone.
							if (sf == sfComponentSize)
								if (getEObject().eIsSet(sfComponentBounds))
									break; // The other is still set, so leave alone.

							// TODO See if we can actually group the expression up to all notifications for this transaction instead of just this one
							// notification.
							IExpression expression = getBeanProxyFactory().createExpression();
							try {
								ffProxy.useComponentSize(getBeanProxy(), false, expression);
							} finally {
								try {
									if (expression.isValid())
										expression.invokeExpression();
								} catch (IllegalStateException e) {
									// Shouldn't occur. Should be taken care of in applied.
									JavaVEPlugin.log(e, Level.WARNING);
								} catch (ThrowableProxy e) {
									// Shouldn't occur. Should be taken care of in applied.
									JavaVEPlugin.log(e, Level.WARNING);
								} catch (NoExpressionValueException e) {
									// Shouldn't occur. Should be taken care of in applied.
									JavaVEPlugin.log(e, Level.WARNING);
								}
							}
						}
						break;
				}
			}
		}
		super.notifyChanged(notification);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IImageNotifier#invalidateImage()
	 */
	public void invalidateImage() {
		if (fComponentManager != null)
			getComponentManager().invalidateImage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IImageNotifier#refreshImage()
	 */
	public void refreshImage() {
		if (isBeanProxyInstantiated() && fComponentManager != null) {
			clearError(IMAGE_DATA_COLLECTION_ERROR_KEY);
			getComponentManager().refreshImage();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#invalidateBeanProxy()
	 */
	public void invalidateBeanProxy() {
		if (isBeanProxyInstantiated())
			getComponentManager().invalidate(getModelChangeController());
	}

	/**
	 * <b>KLUDGE </b>
	 * <p>
	 * Restore the visibility to whatever the current visibility setting is. This is needed because some layouts will set the visibility to false
	 * (e.g. CardLayout). This causes a problem when layouts are switched because now the visibility is false and it should of been true for the new
	 * layout. This is meant to be called only from the container's proxy adapter because it knows when it should be done.
	 * <p>
	 * <b>Note:</b>
	 * This can be called after we having started releasing. So in that case we don't want to do anything.
	 * This happens because of this being a KLUDGE needed by ContainerProxyAdapter. It calls it when
	 * the component is removed, which happens during release. It doesn't know that we are about to be
	 * released, so it tries anyway. So we need to test that we are being released by looking at the
	 * component manager and see if it is disposed.
	 * 
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	public void restoreVisibility(IExpression expression) {
		if (fComponentManager == null || fComponentManager.isDisposed())
			return;	// We are not instantiated fully or we are being released. Either way this should be ignored.
		Object initial = ((IJavaObjectInstance) getTarget()).eGet(sfComponentVisible);
		if (initial == null) {
			// Never set, so we will just force it to true.
			initial = BeanProxyUtilities.wrapperBeanProxy(getBeanProxyDomain().getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(
					true), getEObject().eResource().getResourceSet(), null, true);
		}
		int mark = expression.mark();
		try {
			super.applied(sfComponentVisible, initial, Notification.NO_INDEX, false, expression, false);
		} finally {
			expression.endMark(mark);
		}
	}

	/**
	 * Return the first instantiated (or in instantiation) proxy at or after the given index.
	 * Return null if no setting after the given position are instantiated or are in instantiation.
	 * <p>
	 * This is a useful method for working with isMany features for doing the add() on the
	 * target vm. It is usually used so that we put it in the right place (since the index
	 * may not actually coorespond to the correct index on the target vm due to superclasses
	 * may of added their own settings that we don't see).
	 * 
	 * @param position position to start looking at
	 * @param feature feature to look into (must be an isMany() feature).
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected IProxy getProxyAt(int position, EStructuralFeature feature) {
		List settings = (List) getEObject().eGet(feature);
		for (int i=position; i<settings.size(); i++) {
			IJavaInstance setting = (IJavaInstance) settings.get(i);
			IInternalBeanProxyHost2 settingProxyHost =
				(IInternalBeanProxyHost2) BeanProxyUtilities.getBeanProxyHost(setting);
			if (settingProxyHost.isBeanProxyInstantiated() || settingProxyHost.inInstantiation())
				return settingProxyHost.getProxy();
		}
		
		return null;
	}

}