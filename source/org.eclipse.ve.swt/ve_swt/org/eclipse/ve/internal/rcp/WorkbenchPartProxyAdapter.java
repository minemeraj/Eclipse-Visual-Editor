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
 *  $RCSfile: WorkbenchPartProxyAdapter.java,v $
 *  $Revision: 1.2 $  $Date: 2005-11-04 00:11:05 $ 
 */
package org.eclipse.ve.internal.rcp;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.logging.Level;

import org.eclipse.draw2d.geometry.*;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.IPreferenceConstants;
import org.eclipse.ui.internal.WorkbenchPlugin;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;
import org.eclipse.jem.internal.proxy.swt.DisplayManager;
import org.eclipse.jem.java.JavaClass;

import org.eclipse.ve.internal.cde.core.*;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;
import org.eclipse.ve.internal.java.vce.PDEUtilities;

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
public class WorkbenchPartProxyAdapter extends UIThreadOnlyProxyAdapter implements IVisualComponent {
	
	protected IProxy workbenchHost;
	protected IProxy workbenchParent;

	private ControlManager controlManager;
	private static final Object IMAGE_DATA_COLLECTION_ERROR_KEY = new Object();
	
	private EStructuralFeature sf_delegate_control;
	
	private int setMinWidth = -1, setMinHeight = -1;

	/**
	 * @param domain
	 * 
	 * @since 1.1.0
	 */
	public WorkbenchPartProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	public void setTarget(Notifier newTarget) {
		super.setTarget(newTarget);
		if (newTarget != null)
			sf_delegate_control = ((EObject)newTarget).eClass().getEStructuralFeature(SwtPlugin.DELEGATE_CONTROL);
	}
	
	protected IProxyBeanType getValidSuperClass(IExpression expression) {
		// Override so that we stop at EditorPart/ViewPart and provide the appropriate concrete class.

		notInstantiatedClasses = new ArrayList(2);
		JavaClass thisClass = (JavaClass) ((IJavaInstance) getTarget()).getJavaType();
		notInstantiatedClasses.add(thisClass);
		JavaClass superclass = thisClass.getSupertype();
		while (superclass != null && superclass.isAbstract()) {
			if ("org.eclipse.ui.part.ViewPart".equals(superclass.getQualifiedName())) {
				return getBeanTypeProxy("org.eclipse.ve.internal.jface.targetvm.ConcreteViewPart", expression);
			} else if ("org.eclipse.ui.part.EditorPart".equals(superclass.getQualifiedName())) {
				return getBeanTypeProxy("org.eclipse.ve.internal.jface.targetvm.ConcreteEditorPart", expression);
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
	
	protected IProxy primInstantiateThisPart(IProxyBeanType targetClass, IExpression expression) throws AllocationException {
		if (ffHost != null) {
			IProxy workbenchPart = super.primInstantiateThisPart(targetClass, expression);
			
			// Now we need to add it to the workbench host.
			PDEUtilities pdeUtilities = PDEUtilities.getUtilities(getBeanProxyDomain().getEditDomain());
			// The name of the View comes from the plugin.xml or if none supplied use the type name
			String javaTypeName = getJavaObject().getJavaType().getQualifiedName();		
			String viewTitle = pdeUtilities.getViewName(javaTypeName);
			if(viewTitle == null){
				// If no title in the XML use the unqualified name of the type
				viewTitle = ((IJavaInstance)getTarget()).getJavaType().getName();
			}
			
			// The path for the icon comes from the plugin.xml or if none exists a default is used
			// depending on whether our type inherits from ViewPart or EditorPart. The WorkbenchHost will
			// handle that if no specific location found.
			String iconLocation = pdeUtilities.getIconPath(javaTypeName);
			
			// Now we have a WorkbenchPart subclass, we want to get it correctly instantiated on the target VM
			IProxyBeanType workbenchPartHostTypeProxy = getBeanTypeProxy(TARGETVM_WORKBENCHPARTHOST, expression);
			
			// Tab style (square or rounded)
			boolean traditionalTabStyle = PlatformUI.getPreferenceStore().getBoolean(IWorkbenchPreferenceConstants.SHOW_TRADITIONAL_STYLE_TABS);
			// Tab location (top or bottom)
			int tabPosition = WorkbenchPlugin.getDefault().getPreferenceStore().getInt(IPreferenceConstants.VIEW_TAB_POSITION);
			
			
			IProxy ffParent = ffHost.add(false, expression);
			
			/** 
			 * Call {@link org.eclipse.ve.internal.jface.targetvm.WorkbenchPartHost#createViewPart(Composite, IWorkbenchPart, String, String, boolean, int)} .
			 */
			ExpressionProxy methodResult = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
			expression.createMethodInvocation(ForExpression.ASSIGNMENT_RIGHT, 
					workbenchPartHostTypeProxy.getMethodProxy(expression, "createWorkbenchPart", 
					new String[] {"org.eclipse.swt.widgets.Composite", "org.eclipse.ui.IWorkbenchPart", "java.lang.String", "java.lang.String", "boolean", "int"}),
					false,
					6);
			expression.createProxyExpression(ForExpression.METHOD_ARGUMENT, ffParent);
			expression.createProxyExpression(ForExpression.METHOD_ARGUMENT, workbenchPart);
			expression.createStringLiteral(ForExpression.METHOD_ARGUMENT, viewTitle);
			if (iconLocation != null)
				expression.createStringLiteral(ForExpression.METHOD_ARGUMENT, iconLocation);
			else
				expression.createNull(ForExpression.METHOD_ARGUMENT);
			expression.createPrimitiveLiteral(ForExpression.METHOD_ARGUMENT, traditionalTabStyle);
			expression.createPrimitiveLiteral(ForExpression.METHOD_ARGUMENT, tabPosition);
			
			// Now get the results array and access the two pieces.
			// workbenchHost = methodResult[0]; workbenchParent = methodResult[1];
			workbenchHost = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
			expression.createArrayAccess(ForExpression.ASSIGNMENT_RIGHT, 1);
			expression.createProxyExpression(ForExpression.ARRAYACCESS_ARRAY, methodResult);
			expression.createPrimitiveLiteral(ForExpression.ARRAYACCESS_INDEX, 0);

			workbenchParent = expression.createProxyAssignmentExpression(ForExpression.ROOTEXPRESSION);
			expression.createArrayAccess(ForExpression.ASSIGNMENT_RIGHT, 1);
			expression.createProxyExpression(ForExpression.ARRAYACCESS_ARRAY, methodResult);
			expression.createPrimitiveLiteral(ForExpression.ARRAYACCESS_INDEX, 1);

			((ExpressionProxy) workbenchHost).addProxyListener(new ExpressionProxy.ProxyListener() {
			
				public void proxyVoid(ProxyEvent event) {
					workbenchHost = null;
				}
			
				public void proxyNotResolved(ProxyEvent event) {
					workbenchHost = null;
				}
			
				public void proxyResolved(ProxyEvent event) {
					workbenchHost = event.getProxy();
				}
			
			});
			
			((ExpressionProxy) workbenchParent).addProxyListener(new ExpressionProxy.ProxyListener() {
				
				public void proxyVoid(ProxyEvent event) {
					workbenchParent = null;
				}
			
				public void proxyNotResolved(ProxyEvent event) {
					workbenchParent = null;
				}
			
				public void proxyResolved(ProxyEvent event) {
					workbenchParent = event.getProxy();
				}
			
			});
			
			/**
			 * Call {@link org.eclipse.ve.internal.jface.targetvm.WorkbenchPartHost#setWorkbenchPartWorkingSize(Composite, int, int)}
			 */
			expression.createMethodInvocation(ForExpression.ROOTEXPRESSION,
					workbenchPartHostTypeProxy.getMethodProxy(expression, "setWorkbenchPartWorkingSize", new String[] {"org.eclipse.swt.widgets.Composite", "int", "int"}),
					false,
					3
					);
			expression.createProxyExpression(ForExpression.METHOD_ARGUMENT, workbenchParent);
			expression.createPrimitiveLiteral(ForExpression.METHOD_ARGUMENT, setMinWidth);
			expression.createPrimitiveLiteral(ForExpression.METHOD_ARGUMENT, setMinHeight);
			
			return workbenchPart;
		} else
			return null;	// This should never happen.
	}
	
	/**
	 * Called to set the minimum displayed size of the workbench part.
	 * @param width
	 * @param height
	 * 
	 * @since 1.2.0
	 */
	public void setWorkbenchPartMinDisplayedSize(int width, int height) {
		setMinWidth = width;
		setMinHeight = height;
		if (isBeanProxyInstantiated()) {
			try {
				DisplayManager.asyncExec(getBeanProxyDomain().getProxyFactoryRegistry(), new DisplayManager.DisplayRunnable() {

					public Object run(IBeanProxy displayProxy) throws ThrowableProxy, RunnableException {
						IMethodProxy setWorkSizeMethod = getBeanTypeProxy(TARGETVM_WORKBENCHPARTHOST).getMethodProxy("setWorkbenchPartWorkingSize",
								new String[] { "org.eclipse.swt.widgets.Composite", "int", "int"});
						IStandardBeanProxyFactory factory = workbenchParent.getProxyFactoryRegistry().getBeanProxyFactory();
						setWorkSizeMethod.invoke(null, new IBeanProxy[] { (IBeanProxy) workbenchParent,
								factory.createBeanProxyWith(setMinWidth), factory.createBeanProxyWith(setMinHeight)});
						return null;
					}
				});
				revalidateBeanProxy();
			} catch (ThrowableProxy e) {
				JavaVEPlugin.log(e, Level.WARNING);
			}
		}
	}
	
	protected IProxy primInstantiateBeanProxy(IExpression expression) throws AllocationException {
		IProxy newbean = super.primInstantiateBeanProxy(expression);
		if (workbenchHost != null)
			getControlManager().setControlBeanProxy(workbenchHost, expression, getModelChangeController());	
		return newbean;		
	}
	
	protected void primPrimReleaseBeanProxy(IExpression expression) {		
		if (isOwnsProxy() && isBeanProxyInstantiated()) {
			if (workbenchHost != null)
				BeanSWTUtilities.invoke_WidgetDispose(workbenchHost, expression, getModelChangeController());
			
			// Don't need to do workbenchparent because that will automatically be disposed too by disposing host.
			workbenchHost = workbenchParent = null;
			IBeanProxyHost value = (IBeanProxyHost) EcoreUtil.getExistingAdapter((IJavaInstance) getJavaObject().eGet(sf_delegate_control), IBeanProxyHost.BEAN_PROXY_TYPE);
			if (value != null)
				value.releaseBeanProxy(expression);
		}

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
	
	public IProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature, IExpression expression, ForExpression forExpression) {
		if (aBeanPropertyFeature == sf_delegate_control)
			return workbenchParent;
		else
			return super.getBeanPropertyProxyValue(aBeanPropertyFeature, expression, forExpression);
	}

	protected void applySetting(EStructuralFeature feature, Object value, int index, IExpression expression) {
		if (feature == sf_delegate_control) {
			// Just instantiate it.
			IInternalBeanProxyHost settingBean = getSettingBeanProxyHost((IJavaInstance) value);						
			if (settingBean != null) {
				expression.createTry();
				instantiateSettingBean(settingBean, expression, feature, value, null);	// Errors will show on setting itself since it is a child and not a property.
				expression.createTryCatchClause(getBeanInstantiationExceptionTypeProxy(expression), false);
				expression.createTryEnd();
			}			
		} else
			super.applySetting(feature, value, index, expression);
	}
}
