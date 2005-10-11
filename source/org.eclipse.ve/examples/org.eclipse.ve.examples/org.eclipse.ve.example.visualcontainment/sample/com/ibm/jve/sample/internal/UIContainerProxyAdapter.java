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
 *  $RCSfile: UIContainerProxyAdapter.java,v $
 *  $Revision: 1.3 $  $Date: 2005-10-11 12:55:38 $ 
 */
package com.ibm.jve.sample.internal;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.graphics.ImageData;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.java.core.CompositionProxyAdapter;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;

import org.eclipse.ve.internal.swt.*;
import org.eclipse.ve.internal.swt.common.ImageDataConstants;
 

/**
 * jface WorkbenchPart proxy adapter.
 * It is not a visual widget itself, but it does wrapper two of them
 * <p>
 * <ol>
 * <li>The Workbench host (the tab folder that displays the workbench part)
 * <li>The workbench parent (the parent that is sent to the workbench part createControl)
 * </ol>
 * <br>
 * <p>
 * (1) is represented by "this" when subclassing a workbench part.
 * <br>
 * (2) is represented by "parent" when subclassing a workbench part, it is returned
 *     as the "delegate_control" property as an implicit property. That means it is
 *     not actually in the model as a stored property of this. It will be in the
 *     model as a member of the createPartControl method.
 * <p>    
 * We currently don't support dropping a workbenchpart, only subclassing. When dropping, (1) and
 * (2) would not be created because the createPartControl method will be used instead. We don't
 * code at this time to support that.
 * 
 * @since 1.1.0
 */
public class UIContainerProxyAdapter extends UIThreadOnlyProxyAdapter implements IVisualComponent {
	
	protected IProxy workbenchParent;
	private ControlManager controlManager;
	private static final Object IMAGE_DATA_COLLECTION_ERROR_KEY = new Object();
	private IBeanProxy containerProxy;	
	private EStructuralFeature sf_uicontainer;	

	/**
	 * @param domain
	 * 
	 * @since 1.1.0
	 */
	public UIContainerProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		if(containerProxy == null){ // Do nothing
			
		}
	}
	
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null)
			sf_uicontainer = ((EObject)newTarget).eClass().getEStructuralFeature("uiContainer");
	}	
	
	public IProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature, IExpression expression, ForExpression forExpression) {
		if (aBeanPropertyFeature == sf_uicontainer)
			return containerProxy;
		else
			return super.getBeanPropertyProxyValue(aBeanPropertyFeature, expression, forExpression);
	}	
	
	protected IProxyBeanType getValidSuperClass(IExpression expression) {
		// Override so that we provide the appropriate concrete class.

		notInstantiatedClasses = new ArrayList(2);
		JavaClass thisClass = (JavaClass) ((IJavaInstance) getTarget()).getJavaType();
		notInstantiatedClasses.add(thisClass);
		JavaClass superclass = thisClass.getSupertype();
		while (superclass != null && superclass.isAbstract()) {
			if ("com.ibm.jve.sample.core.UIContainer".equals(superclass.getQualifiedName())) {
				return getBeanTypeProxy("com.ibm.jve.sample.internal.vm.ConcreteUIContainer", expression);
			}
			notInstantiatedClasses.add(superclass);
			superclass = superclass.getSupertype();
		}
		if (superclass != null)
			return getBeanTypeProxy(superclass.getQualifiedNameForReflection(), expression);
		else
			return getBeanTypeProxy("java.lang.Object", expression);
	}
	
	protected FreeFormComponentsHost ffHost;
	
	public void addToFreeForm(CompositionProxyAdapter compositionAdapter) {
		// We will use the standard ffhost to host our workbenchpart host.
		ffHost = (FreeFormComponentsHost) compositionAdapter.getFreeForm(FreeFormComponentsHost.class);
		if (ffHost == null) {
			// Doesn't exist yet, need to create it.
			ffHost = new FreeFormComponentsHost(compositionAdapter);
		}
	}
	
	public void removeFromFreeForm() {
		ffHost = null; // No longer on freeform.
	}
	
	public static final String TARGETVM_WORKBENCHPARTHOST = "org.eclipse.ve.internal.jface.targetvm.WorkbenchPartHost"; //$NON-NLS-1$
	
	protected IProxy primInstantiateBeanProxy(IExpression expression) throws AllocationException {
		IProxy newbean = super.primInstantiateBeanProxy(expression);
		if (newbean != null) {
			// call container.createContents(ffparent);
			// to ensure that the UIControl creates its contents on the free form 
			// Now call container.createContents(ffParent);

			IProxy ffParent = ffHost.add(false, expression);
			expression.createMethodInvocation(ForExpression.ROOTEXPRESSION,"createContents",true,1);
			expression.createProxyExpression(ForExpression.METHOD_RECEIVER,newbean);
			expression.createProxyExpression(ForExpression.METHOD_ARGUMENT,ffParent);
			
			// container.getUIControl();
			ExpressionProxy uiControlProxy = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
			expression.createMethodInvocation(ForExpression.ASSIGNMENT_RIGHT,"getUIContainer",true,0);
			expression.createProxyExpression(ForExpression.METHOD_RECEIVER,newbean);
									
			getControlManager().setControlBeanProxy(uiControlProxy, expression, getModelChangeController());
		}
		return newbean;		
	}

	protected final ControlManager getControlManager() {
		if (controlManager == null)
			controlManager = new ControlManager();
		return controlManager;
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

	protected void primPrimReleaseBeanProxy(IExpression expression) {
		// TODO Auto-generated method stub
		
	}
}
