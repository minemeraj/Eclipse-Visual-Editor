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

import java.text.MessageFormat;
import java.util.List;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.swt.graphics.ImageData;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.beaninfo.core.Utilities;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.swt.JavaStandardSWTBeanConstants;
import org.eclipse.jem.java.JavaHelpers;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

import org.eclipse.ve.internal.swt.common.ImageDataConstants;

/**
 * Proxy adapter for swt.Control
 * 
 * @since 1.1.0
 */
public class ControlProxyAdapter extends WidgetProxyAdapter implements IVisualComponent {

	// Need these features often, but they depend upon the class we are in,
	// can't get them as statics because they would be different for each Eclipse project.
	protected EStructuralFeature sfControlVisible, sfControlLocation, sfControlBounds, sfControlSize, sfLayoutData;

	private ControlManager controlManager;
	private static final Object IMAGE_DATA_COLLECTION_ERROR_KEY = new Object();
	
	public ControlProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfLayoutData = JavaInstantiation.getReference(rset, SWTConstants.SF_CONTROL_LAYOUTDATA);
		sfControlVisible = JavaInstantiation.getReference(rset, SWTConstants.SF_CONTROL_VISIBLE);
		sfControlBounds = JavaInstantiation.getReference(rset, SWTConstants.SF_CONTROL_BOUNDS);
		sfControlLocation = JavaInstantiation.getReference(rset, SWTConstants.SF_CONTROL_LOCATION);
		sfControlSize = JavaInstantiation.getReference(rset, SWTConstants.SF_CONTROL_SIZE);
	}


	protected FreeFormComponentsHost ffHost;
	
	public void addToFreeForm(CompositionProxyAdapter compositionAdapter) {
		ffHost = (FreeFormComponentsHost) compositionAdapter.getFreeForm(FreeFormComponentsHost.class);
		if (ffHost == null) {
			// Doesn't exist yet, need to create it.
			ffHost = new FreeFormComponentsHost(compositionAdapter);
		}
	}
	
	public void removeFromFreeForm() {
		ffHost = null; // No longer on freeform.
	}
	
	/**
	 * Override the location with this location. This is used when on the freeform. This is so that the true location on the freeform does not filter
	 * through to the settings of location/bounds in the java object.
	 * 
	 * @param loc
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	public void overrideLocation(Point loc, IExpression expression) {
		getControlManager().overrideLocation(loc, expression);
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
		overrideProperty(sfControlVisible, getBeanProxyFactory().createBeanProxyWith(visibility), expression);
	}
	
	/**
	 * Remove the override of the visibility.
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	public void removeVisibilityOverride(IExpression expression) {
		removeOverrideProperty(sfControlVisible, expression);
	}
	
	/**
	 * Create the control manager. Subclasses that want to add in a Extension manager at this time should
	 * override and do so after calling super.createControlManager.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected ControlManager createControlManager() {
		return new ControlManager();
	}

	/**
	 * Get the component manager. This should be used for all access so that it is lazily created.
	 * 
	 * @return
	 * 
	 * @since 1.1.0
	 */
	protected final ControlManager getControlManager() {
		if (controlManager == null)
			controlManager = createControlManager();
		return controlManager;
	}

	protected IProxy primInstantiateBeanProxy(IExpression expression) throws AllocationException {
		IProxy newbean = super.primInstantiateBeanProxy(expression);
		if (newbean != null) {
			getControlManager().setControlBeanProxy(newbean, expression, getModelChangeController());
			// Also we need to have the original unmolested layout data. This is because if the original
			// is null, and the layout is grid or row, or some other, the layout will change the layout
			// data to be one that is valid for that layout. Then if we later set the layout data, we
			// would not get the true original value. We would instead get the  mucked up one. So
			// we need to get it now before any layouts occur.
			setOriginalValue(sfLayoutData, primGetBeanProperyProxyValue(newbean, Utilities.getPropertyDecorator(sfLayoutData), expression, ForExpression.ROOTEXPRESSION));
		}
		return newbean;
	}
	
	protected IProxy primInstantiateThisPart(IProxyBeanType targetClass, IExpression expression) throws AllocationException {
		if (ffHost != null) {
			// We are the this part. This means we should be on the freeform. Currently only thispart can be on freeform, so we are putting the
			// code for that here.
			// We are on the freeform, instantiate for this. This needs to be done for each instantiation because it is reset in the release.
			// Subclasses (such as Shell) will want to do freeform differently so ffHost will be null and we won't go through here.
			overrideVisibility(true, expression);
			overrideLocation(new Point(), expression); // Go to (0,0) because we are in a freeform dialog which will control position.

			boolean changedTarget = false;
			// KLUDGE: Something special for control and "this" part. Since Control is Abstract, someone may of tried create an abstract subclass.
			// In that case
			// the bean proxy that comes in would be for Object. So what we will do is since we are not a control, we will change it a Canvas (that
			// way something will display).
			// Don't need to worry about any other kind of class because all of the subclasses of Control that we are implementing
			// are non-abstract.
			// 
			// If we find one of the notinstantiated classes is control, then we know we went to far, so we will create it
			// as gray canvas.
			// newBean = new java.awt.Canvas();
			// newBean.setBackground(lightGray);
			// return newBean;
			for (int i = 0; i < notInstantiatedClasses.size(); i++) {
				if (!changedTarget && ((JavaHelpers) notInstantiatedClasses.get(i)).getQualifiedName().equals("org.eclipse.swt.widgets.Control")) { //$NON-NLS-1$
					targetClass = getBeanTypeProxy("org.eclipse.swt.widgets.Canvas", expression);
					changedTarget = true;
				}
				if (changedTarget) {
					notInstantiatedClasses.remove(i--);	// Remove "control" and any above control because we want to be able to apply "control" properties to the live bean.
				}
			}

			// Now create using new Control(ffparent, SWT.NONE);
			IProxy parent = ffHost.add(getEObject().eIsSet(sfControlBounds) || getEObject().eIsSet(sfControlSize), expression);

			// newbean = new targetClass(Composite parent, int SWT.NONE);
			ExpressionProxy newbean = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
			expression.createClassInstanceCreation(ForExpression.ASSIGNMENT_RIGHT, targetClass, 2);
			expression.createProxyExpression(ForExpression.CLASSINSTANCECREATION_ARGUMENT, parent);
			expression.createFieldAccess(ForExpression.CLASSINSTANCECREATION_ARGUMENT, getBeanTypeProxy("org.eclipse.swt.SWT", expression)
					.getFieldProxy(expression, "NONE"), false);

			if (changedTarget) {
				expression.createMethodInvocation(ForExpression.ROOTEXPRESSION, targetClass.getMethodProxy(expression, "setBackground", //$NON-NLS-1$
						new String[] { "org.eclipse.swt.graphics.Color"}), true, 1); //$NON-NLS-1$
				expression.createProxyExpression(ForExpression.METHOD_RECEIVER, newbean);
				expression.createFieldAccess(ForExpression.METHOD_ARGUMENT, "lightGray", true); //$NON-NLS-1$
				expression.createProxyExpression(ForExpression.FIELD_RECEIVER, JavaStandardSWTBeanConstants.getConstants(expression.getRegistry()).getEnvironmentProxy());
			}
			return newbean;
		} else
			return null;	// This shouldn't happen.

	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IVisualComponent#addComponentListener(org.eclipse.ve.internal.cde.core.IVisualComponentListener)
	 */
	public synchronized void addComponentListener(IVisualComponentListener aListener) {
		getControlManager().addComponentListener(aListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IVisualComponent#removeComponentListener(org.eclipse.ve.internal.cde.core.IVisualComponentListener)
	 */
	public void removeComponentListener(IVisualComponentListener aListener) {
		if (controlManager != null)
			getControlManager().removeComponentListener(aListener);
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
			imageListener = new ControlManager.IControlImageListener() {

				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.ve.internal.jfc.core.ComponentManager.IComponentImageListener#imageStatus(int)
				 */
				public void imageStatus(int status) {
					switch (status) {
						case ImageDataConstants.COMPONENT_IMAGE_CLIPPED:
							// Bit of kludge, but if image clipped, create an info message for it.
							ErrorType err = new MessageError(SWTMessages.ControlProxyAdapter_Picture_too_large_WARN_, 
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
										SWTMessages.ControlProxyAdapter_Image_collection_exception_EXC_, new Object[] { eType.getTypeName()}); 
					}
					ErrorType err = new MessageError(MessageFormat.format(SWTMessages.ControlProxyAdapter_Image_collection_failed_ERROR_, new Object[] { eMsg}), IErrorHolder.ERROR_INFO); //$NON-NLS-1$
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
		getControlManager().addImageListener(imageListener);
		getControlManager().addImageListener(aListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IImageNotifier#removeImageListener(org.eclipse.ve.internal.cde.core.IImageListener)
	 */
	public synchronized void removeImageListener(IImageListener aListener) {
		if (controlManager != null) {
			// KLUDGE Remove our image listener first, so that after we remove this input listener we can see if there
			// still any listeners. If there are, we add ours back. If not we don't add ours back. This is because we
			// don't want to get images sent if we are the only one listening. Our listener is simply to get errors
			// back if someone is asking for images.
			if (imageListener != null)
				getControlManager().removeImageListener(imageListener);
			getControlManager().removeImageListener(aListener);
			if (imageListener != null && getControlManager().hasImageListeners())
				getControlManager().addImageListener(imageListener);
		}
	}

	protected IProxy primApplyBeanProperty(PropertyDecorator propertyDecorator, IProxy settingProxy, IExpression expression, boolean getOriginalValue)
			throws NoSuchMethodException, NoSuchFieldException {
		// Override for loc and bounds so that it goes through the component manager.
		if (propertyDecorator.getEModelElement() == sfControlBounds) {
			return getControlManager().applyBounds(settingProxy, getOriginalValue, expression, getModelChangeController());
		} else if (propertyDecorator.getEModelElement() == sfControlLocation)
			return getControlManager().applyLocation(settingProxy, getOriginalValue, expression, getModelChangeController());
		else if (propertyDecorator.getEModelElement() == sfLayoutData)
			return getControlManager().applyLayoutData(settingProxy, getOriginalValue, expression);
		else
			return super.primApplyBeanProperty(propertyDecorator, settingProxy, expression, getOriginalValue);
	}

	protected void applySetting(EStructuralFeature feature, Object value, int index, IExpression expression) {
		if (ffHost != null) {
			if (feature == sfControlBounds || feature == sfControlSize) {
				ffHost.setUseComponentSize(getProxy(), true, expression);
			}			
		}

		super.applySetting(feature, value, index, expression);
	}
	protected void cancelSetting(EStructuralFeature feature, Object oldValue, int index, IExpression expression) {
		// Little tricker, need to see if the other setting is still set.
		if (ffHost != null) {
			if ((feature == sfControlBounds && !getEObject().eIsSet(sfControlSize)) || 
					(feature == sfControlSize && !getEObject().eIsSet(sfControlBounds))) {
				// The complementary feature is not set, so we need to reset to use the preferred size.
				ffHost.setUseComponentSize(getProxy(), false, expression);
			}
		}

		if (feature == sfControlBounds && (getEObject().eIsSet(sfControlSize) || getEObject().eIsSet(sfControlLocation)))
			return; // Don't apply the cancel for bounds because loc and size are set and this would wipe them out.
		else if ((feature == sfControlSize || feature == sfControlLocation) && getEObject().eIsSet(sfControlBounds))
			return; // Don't apply the cancel for size or loc because bounds is set and this would wipe that setting out.
		else
			super.cancelSetting(feature, oldValue, index, expression);
	}
	
	protected void primPrimReleaseBeanProxy(IExpression expression) {
		if (imageListener != null) {
			clearError(IMAGE_DATA_COLLECTION_ERROR_KEY); // In case one is hanging around.
		}

		if (controlManager != null) {
			// Be on the safe so no spurious last minute notifications are sent out.
			if (expression != null) {
				expression.createTry();
				controlManager.dispose(expression);
				expression.createTryCatchClause("java.lang.RuntimeException", false);	//$NON-NLS-1$
				expression.createTryEnd();
			} else
				controlManager.dispose(null);	// Give it a chance to clean up without an expression.
			// Note: Do not get rid of the control manager. This bean may of had component listeners
			// and this bean may be about to be reinstantiated. We don't want to loose the listeners
			// when the reinstantiation occurs.
		}

		// We need to dispose of stuff above before we do the super.release because by the time the
		// release comes back the bean proxy will of been released and can't be used. It is needed
		// to do the above disposes.
		super.primPrimReleaseBeanProxy(expression);
	}
	
	public IProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyAttribute, IExpression exp, ForExpression forExpression) {
		// Override for loc and bounds so that it goes through the component manager.
		// Even though an expression is past in, we aren't using it here. Probably could in the future.
		// The get default bounds and location do not need to be on UI thread. The ControlManager handles this for us.
		if (aBeanPropertyAttribute == sfControlBounds)
			return getControlManager().getDefaultBounds();
		else if (aBeanPropertyAttribute == sfControlLocation)
			return getControlManager().getDefaultLocation();
		else
			return super.getBeanPropertyProxyValue(aBeanPropertyAttribute, exp, forExpression);
	}
	
	public Rectangle getBounds() {
		if (controlManager != null) {
			return controlManager.getBounds();
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
		if (controlManager != null) {
			return controlManager.getAbsoluteLocation();
		} else {
			// No proxy. Either called too soon, or there was an instantiation error and we can't get
			// a live component. So return just a default.
			return new Point();
		}
	}

	/**
	 * Get the origin offset. This is the offset from the upper-left corner of the control to where (0,0) is in the control. Most
	 * controls will return (0,0) as the offset, but shell is different. With shell when you set something at (0,0), it will actually be
	 * at some other value relative to the upper-left corner. This is that offset.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public Point getOriginOffset() {
		if (controlManager != null)
			return controlManager.getOriginOffset();
		else
			return new Point();
	}

	public Point getLocation() {
		if (controlManager != null) {
			return controlManager.getLocation();
		} else {
			// No proxy. Either called too soon, or there was an instantiation error and we can't get
			// a live component. So return just a default.
			return new Point();
		}
	}

	public Dimension getSize() {
		if (controlManager != null) {
			return controlManager.getSize();
		} else {
			// No proxy. Either called too soon, or there was an instantiation error and we can't get
			// a live component. So return just a default.
			return new Dimension();
		}
	}
	
	public boolean hasImageListeners() {
		return (controlManager != null && getControlManager().hasImageListeners());
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IImageNotifier#invalidateImage()
	 */
	public void invalidateImage() {
		if (controlManager != null)
			getControlManager().invalidateImage();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.cde.core.IImageNotifier#refreshImage()
	 */
	public void refreshImage() {
		if (isBeanProxyInstantiated() && controlManager != null) {
			clearError(IMAGE_DATA_COLLECTION_ERROR_KEY);
			getControlManager().refreshImage();
		}
	}

	public void revalidateBeanProxy() {
		if (isBeanProxyInstantiated())
			getControlManager().invalidate(getModelChangeController());
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
			IInternalBeanProxyHost settingProxyHost = getSettingBeanProxyHost(setting);
			if (settingProxyHost.isBeanProxyInstantiated() || settingProxyHost.inInstantiation())
				return settingProxyHost.getProxy();
		}
		
		return null;
	}

}