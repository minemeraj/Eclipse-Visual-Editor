/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: FreeFormComponentsHostAdapter.java,v $
 *  $Revision: 1.5 $  $Date: 2004-08-27 15:34:46 $ 
 */

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.jem.internal.beaninfo.core.Utilities;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.ve.internal.jcm.JCMPackage;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;

import org.eclipse.jem.internal.proxy.core.*;
/**
 * This class is holds a proxy to the frame that is used to host bean proxies for free form components that are beans
 * so that they are parented
 * It adapts the a BeanSubclassComposition and listens to changes to the freeFormComponents relationship
 */
public class FreeFormComponentsHostAdapter extends AdapterImpl {
		
	/**
	 * This is a IComponentProxyHost wrappering a freeform dialog.
	 * There is one for each type, i.e swing and awt.
	 * @author richkulp
	 */
	private class FreeFormComponentProxyHost implements IComponentProxyHost {

		IBeanProxy dialogProxy;

		public FreeFormComponentProxyHost(String dialogClass) {
			IBeanTypeProxy dialogTypeProxy = beanProxyDomain.getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy(dialogClass);
			try {
				Point loc = BeanAwtUtilities.getOffScreenLocation();				
				dialogProxy = dialogTypeProxy.newInstance("new "+dialogClass+"("+String.valueOf(loc.x)+","+String.valueOf(loc.y)+")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				// For debug purposes it is possible to position this on screen through a preference
				final Shell shell = workbenchPage.getWorkbenchWindow().getShell();
				shell.getDisplay().asyncExec(new Runnable(){
					public void run(){
						shell.forceActive();
					}
				});				
			} catch (ThrowableProxy e) {
				JavaVEPlugin.log(e, Level.WARNING);
			} catch (InstantiationException e) {
				JavaVEPlugin.log(e, Level.WARNING);
			}				
		}

		/**
		 * @see org.eclipse.ve.internal.jfc.core.IComponentProxyHost#disposeOnFreeForm(IBeanProxy)
		 */
		public void disposeOnFreeForm(IBeanProxy freeFormDialog) {
		}

		/**
		 * @see org.eclipse.ve.internal.jfc.core.IComponentProxyHost#getParentComponentProxyHost()
		 */
		public IComponentProxyHost getParentComponentProxyHost() {
			return null;
		}

		/**
		 * @see org.eclipse.ve.internal.jfc.core.IComponentProxyHost#getVisualComponentBeanProxy()
		 */
		public IBeanProxy getVisualComponentBeanProxy() {
			return dialogProxy;
		}

		/**
		 * @see org.eclipse.ve.internal.jfc.core.IComponentProxyHost#hasImageListeners()
		 */
		public boolean hasImageListeners() {
			return false;
		}

		/**
		 * @see org.eclipse.ve.internal.jfc.core.IComponentProxyHost#instantiateOnFreeForm(IBeanProxy)
		 */
		public IBeanProxy instantiateOnFreeForm(IBeanProxy freeFormDialog) {
			return null;
		}

		/**
		 * @see org.eclipse.ve.internal.jfc.core.IComponentProxyHost#reinstantiateChild(IBeanProxyHost)
		 */
		public void reinstantiateChild(IBeanProxyHost aChildProxyHost) {
			EObject aChildComponent = (EObject) aChildProxyHost.getTarget();
			canceled(aChildComponent);
			// Normally we don't release because the BeanSubclassComposition adapter handles this, but we know we need to do it here.				
			aChildProxyHost.releaseBeanProxy();
			applied(aChildComponent);
		}

		/**
		 * @see org.eclipse.ve.internal.jfc.core.IComponentProxyHost#invalidateImage()
		 */
		public void invalidateImage() {
		}

		/**
		 * @see org.eclipse.ve.internal.jfc.core.IComponentProxyHost#refreshImage()
		 */
		public void refreshImage() {
		}

		/**
		 * @see org.eclipse.ve.internal.jfc.core.IComponentProxyHost#setParentComponentProxyHost(IComponentProxyHost)
		 */
		public void setParentComponentProxyHost(IComponentProxyHost aParentComponent) {
		}

		/**
		 * @see org.eclipse.ve.internal.jfc.core.IComponentProxyHost#applyNullLayoutConstraints()
		 */
		public void applyNullLayoutConstraints() {
		}

		IMethodProxy useSizeMethod;
		/*
		 * useComponentSize: JCMMethod called by main adapter when needed.
		 */
		protected void useComponentSize(IComponentProxyHost aProxy, boolean useComponentSize) {
			if (aProxy.getVisualComponentBeanProxy() != null && aProxy.getVisualComponentBeanProxy().isValid()) {
				if (useSizeMethod == null) {
					useSizeMethod = dialogProxy.getTypeProxy().getMethodProxy("setUseComponentSize", //$NON-NLS-1$
						new String[] { "java.awt.Component", "boolean" } //$NON-NLS-1$ //$NON-NLS-2$
						);
				}
	
				useSizeMethod.invokeCatchThrowableExceptions(
					dialogProxy,
					new IBeanProxy[] {
						aProxy.getVisualComponentBeanProxy(),
						beanProxyDomain.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(useComponentSize)});
	
				// Now make sure size set correctly because size would of already been reset by the dialog's pack with preferred size.
				if (useComponentSize)
					aProxy.applyNullLayoutConstraints();		
			}
		}

		/*
		 * Add a component to this dialog.
		 */
		protected void add(IComponentProxyHost aComponentProxyHost) {
			// Host the bean inside the dialog that is used so that free components are instantiated correctly
			aComponentProxyHost.instantiateOnFreeForm(dialogProxy);
			// Make our dialog bean proxy the parent of the component.  That way it will invalidate us correctly
			aComponentProxyHost.setParentComponentProxyHost(this);
			((IBeanProxyHost) aComponentProxyHost).revalidateBeanProxy();
		}
		
		/*
		 * dispose of the proxy.
		 */
		protected void dispose() {
			if (dialogProxy != null && dialogProxy.isValid())
				BeanAwtUtilities.invoke_dispose(dialogProxy);
				useSizeMethod = null;
		}

		/**
		 * @see org.eclipse.ve.internal.jfc.core.IComponentProxyHost#childInvalidated(IComponentProxyHost)
		 */
		public void childInvalidated(IComponentProxyHost childProxy) {
		}

		/**
		 * @see org.eclipse.ve.internal.jfc.core.IComponentProxyHost#childValidated(IComponentProxyHost)
		 */
		public void childValidated(IComponentProxyHost childProxy) {
		}
	}
	
	public static final String FREE_FORM_COMPONENTS_HOST = "FREE_FORM_COMPONENTS_HOST_ADAPTER"; //$NON-NLS-1$
	protected FreeFormComponentProxyHost awtDialogHost, swingDialogHost;

	protected IBeanProxyDomain beanProxyDomain;
	protected BeanSubclassComposition composition;
	protected IWorkbenchPage workbenchPage;

	protected EStructuralFeature sfComponentSize, sfComponentBounds;

	protected ResourceSet rset;

	protected EClass classComponent, classJComponent;
	/**
	 * When we are created add ourself as an adaptor to the argument and make it our target
	 */
	public FreeFormComponentsHostAdapter(
		IBeanProxyDomain beanProxyDomain,
		BeanSubclassComposition composition,
		IWorkbenchPage workbenchPage) {
		super();
		this.beanProxyDomain = beanProxyDomain;
		this.composition = composition;
		this.workbenchPage = workbenchPage;

		rset = composition.eResource().getResourceSet();	
		ensureEMFDetailsCached();
	}

	protected boolean isSwingType(IBeanProxyHost aProxy) {
		return classJComponent.isInstance((EObject) aProxy.getTarget());
	}

	protected FreeFormComponentProxyHost getDialogProxy(boolean swingType) {
		if (swingType) {
			if (swingDialogHost == null) {
				swingDialogHost = new FreeFormComponentProxyHost("org.eclipse.ve.internal.jfc.vm.FreeFormSwingDialog"); //$NON-NLS-1$
			}
			return swingDialogHost;
		} else {
			if (awtDialogHost == null) {
				awtDialogHost = new FreeFormComponentProxyHost("org.eclipse.ve.internal.jfc.vm.FreeFormAWTDialog");	//$NON-NLS-1$
			}
			return awtDialogHost;
		}
	}

	/*
	 * This is used to tell the freeform dialog that for this component it should use
	 * the size of the component and not its preferred size. This is for when a size has
	 * been explicitly set onto the component. In those cases the set size is the size to use.
	 */
	protected void useComponentSize(IComponentProxyHost aProxy, boolean useComponentSize) {
		IComponentProxyHost parent = aProxy.getParentComponentProxyHost();
		if (parent instanceof FreeFormComponentProxyHost)
			 ((FreeFormComponentProxyHost) parent).useComponentSize(aProxy, useComponentSize);
	}

	/*
	 * Add this component to the property dialog.
	 * Note: The component proxy host in this case, must also be an IBeanProxyHost.
	 */
	protected void add(IComponentProxyHost aComponentProxyHost) {
		FreeFormComponentProxyHost dialogHost = getDialogProxy(isSwingType((IBeanProxyHost) aComponentProxyHost));
		dialogHost.add(aComponentProxyHost);
	}

	private static final EStructuralFeature SF_COMPONENTS, SF_THIS;
	static {
		SF_COMPONENTS = JCMPackage.eINSTANCE.getBeanComposition_Components();
		SF_THIS = JCMPackage.eINSTANCE.getBeanSubclassComposition_ThisPart();
	}
	protected void ensureEMFDetailsCached(){
		if ( sfComponentSize == null ) 
			sfComponentSize = JavaInstantiation.getSFeature(rset, JFCConstants.SF_COMPONENT_SIZE);
		if ( sfComponentBounds == null )
			sfComponentBounds = JavaInstantiation.getSFeature(rset, JFCConstants.SF_COMPONENT_BOUNDS);
		if ( classComponent == null ) 
			classComponent = (EClass) Utilities.getJavaClass("java.awt.Component", rset); //$NON-NLS-1$
		if ( classJComponent == null )
			classJComponent = (EClass) Utilities.getJavaClass("javax.swing.JComponent", rset); //$NON-NLS-1$
	}
	
	public void notifyChanged(Notification msg) {
		if (msg.getEventType() == Notification.REMOVING_ADAPTER) {
			disposeDialogs();
		} else {
			ensureEMFDetailsCached();			
			if (msg.getFeature() == SF_COMPONENTS || msg.getFeature() == SF_THIS) {
				// Add/Remove are used because the feature root components is multi-valued.
				switch (msg.getEventType()) {
					case Notification.ADD_MANY :
						Iterator itr = ((List) msg.getNewValue()).iterator();
						while (itr.hasNext())
							applied(itr.next());
						break;				
					case Notification.ADD :
					case Notification.SET :
						if (!CDEUtilities.isUnset(msg)) {
							applied(msg.getNewValue());
							break;
						}	// Else flow into unset
					case Notification.UNSET :
					case Notification.REMOVE :
						canceled(msg.getOldValue());
						break;
					case Notification.REMOVE_MANY :
						itr = ((List) msg.getOldValue()).iterator();
						while (itr.hasNext())
							canceled(itr.next());
						break;
				}
			} else if (msg.getEventType() == CompositionProxyAdapter.RELEASE_PROXIES) {
				disposeDialogs();
			} else if (msg.getEventType() == CompositionProxyAdapter.INSTANTIATE_PROXIES) {
				instantiateDialogs();
			};
		}
	}

	private class ComponentAdapter extends AdapterImpl {
		public void notifyChanged(Notification msg) {
			EObject sf = (EStructuralFeature)msg.getFeature();
			if (sf == sfComponentBounds || sf == sfComponentSize) {
				switch (msg.getEventType()) {
					case Notification.SET :
						if (!CDEUtilities.isUnset(msg)) {
							useComponentSize(
								(IComponentProxyHost) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getTarget()),
								true);
							break;
						}	// Else flow into unset.
					case Notification.UNSET :
						// Little tricker, need to see if the other setting is still set.
						if (sf == sfComponentBounds)
							if (((EObject) getTarget()).eIsSet(sfComponentSize))
								break; // The other is still set, so leave alone.
						if (sf == sfComponentSize)
							if (((EObject) getTarget()).eIsSet(sfComponentBounds))
								break; // The other is still set, so leave alone.
						useComponentSize(
							(IComponentProxyHost) BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) getTarget()),
							false);
						break;
				}
			}
		}

		public boolean isAdapterForType(Object type) {
			return ComponentAdapter.class == type;
		}
	}

	/**
	 * List for the freeFormParts relationship and see whether or not this is a component bean
	 * that requires hosting
	 */
	protected void applied(Object newValue) {
		ensureEMFDetailsCached();
		if (!beanProxyDomain.getProxyFactoryRegistry().isValid() || !classComponent.isInstance(newValue))
			return; // Proxy VM not yet up or it isn't even an awt component.

		IJavaObjectInstance comp = (IJavaObjectInstance) newValue;
		IBeanProxyHost beanProxyHost = BeanProxyUtilities.getBeanProxyHost(comp);
		if (beanProxyHost instanceof IComponentProxyHost) {
			IComponentProxyHost compProxyHost = (IComponentProxyHost) beanProxyHost;

			ComponentAdapter a = (ComponentAdapter) EcoreUtil.getExistingAdapter(comp,ComponentAdapter.class);
			if (a == null) {
				// Need to listen for size/bounds change so that we know whether to be preferred size or actual size.
				a = new ComponentAdapter();
				a.setTarget(comp);
				comp.eAdapters().add(a);
			}

			add(compProxyHost);

			// Need to see if we should initially turn on useComponentSize.
			if (comp.eIsSet(sfComponentBounds) || comp.eIsSet(sfComponentSize))
				useComponentSize(compProxyHost, true);
		}
	}
	/**
	 * List for the freeFormParts relationship and see whether or not this is a component bean
	 * that requires hosting
	 */
	protected void canceled(Object oldValue) {
		if (!classComponent.isInstance(oldValue))
			return; // It isn't even an awt component.

		IJavaObjectInstance comp = (IJavaObjectInstance) oldValue;
		ComponentAdapter a = (ComponentAdapter) EcoreUtil.getExistingAdapter(comp,ComponentAdapter.class);
		if (a != null)
			comp.eAdapters().remove(a);

		IBeanProxyHost componentProxyHost = BeanProxyUtilities.getBeanProxyHost(comp);
		if (componentProxyHost instanceof IComponentProxyHost) {
			IComponentProxyHost ph = (IComponentProxyHost) componentProxyHost;
			if (ph.getParentComponentProxyHost() != null)
				ph.disposeOnFreeForm(ph.getParentComponentProxyHost().getVisualComponentBeanProxy());
		}
	}

	/** 
	 * Throwaway the dialogs being used to host free form components
	 */
	public void disposeDialogs() {
		Iterator components = composition.getComponents().iterator();
		while (components.hasNext()) {
			canceled(components.next());
		}
		if (composition.eIsSet(JCMPackage.eINSTANCE.getBeanSubclassComposition_ThisPart())) {
			canceled(composition.getThisPart());
		}

		if (awtDialogHost != null)
			awtDialogHost.dispose();
		if (swingDialogHost != null)
			swingDialogHost.dispose();

		awtDialogHost = swingDialogHost = null;

	}

	public synchronized void instantiateDialogs() {
		// Having instantiated the bean proxy we need to apply all attribute settings that are free form parts
		Iterator components = composition.getComponents().iterator();
		while (components.hasNext()) {
			applied(components.next());
		}
		if (composition.eIsSet(JCMPackage.eINSTANCE.getBeanSubclassComposition_ThisPart())) {
			applied(composition.getThisPart());
		}
	}

	/**
	 * isAdaptorForType method comment.
	 */
	public boolean isAdapterForType(Object type) {
		return FREE_FORM_COMPONENTS_HOST.equals(type);
	}
}
