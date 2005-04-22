/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $RCSfile: JTabbedPaneProxyAdapter.java,v $
 *  $Revision: 1.5 $  $Date: 2005-04-22 20:57:54 $ 
 */

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.ThrowableProxy;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.*;

/**
 * Create the bean proxy. 
 * We override because we use addtab(String title, Icon icon, Component component, String tip, int index) or 
 * insertTab(String title, Icon icon, Component component, String tip, int index)
 * to add components as tabs (pages).
 * 
 * This also keeps a List of those who are interested in the page selection
 * of the JTabbedPane. The listener must implement IJTabbedPanePageListener and 
 * notifies the listeners when a page is selected.
 */
public class JTabbedPaneProxyAdapter extends ComponentProxyAdapter {
	
	
	protected EReference
		sfTabs,
		sfComponent,
		sfTitle,
		sfIcon,
		sfTooltip;

	public JTabbedPaneProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfTabs = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABBEDPANE_TABS);
		sfComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABCOMPONENT_COMPONENT);		
		sfTitle = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABCOMPONENT_TITLE);
		sfIcon = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABCOMPONENT_ICON);
		sfTooltip = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABCOMPONENT_TOOLTIP);		
	}
	protected void applied(EStructuralFeature as, Object newValue, int position) {

		if (as == sfTabs) {
			addComponent((EObject) newValue, position);
		} else {
			super.applied(as, newValue, position);
		}
	}
	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {

		if (sf == sfTabs) {
			removeComponent((EObject) oldValue);
		} else {
			super.canceled(sf, oldValue, position);
		}

	}
	
	/*
	 * Return the true component added for the tab at the requested position.
	 * It may not be the one associated with the tab because it may not of been
	 * instantiated, in which case we would of created a dummy one to use.
	 * Return null if not found. Find first one that is instantiated.
	 */
	protected IBeanProxy getBeanProxyAt(int position) {
		List tabs = (List) ((EObject) getTarget()).eGet(sfTabs);
		for (int i=position; i<tabs.size(); i++) {
			IBeanProxy component = getBeanProxyFor((EObject) tabs.get(i));
			if (component != null)
				return component;
		}
		
		return null;
	}	
	
	/*
	 * Return the bean proxy component that was used for the specified component.
	 * It may be different if the specified component could not be instantiated.
	 */
	protected IBeanProxy getBeanProxyFor(EObject tab) {
		JTabComponentAdapter adapter = (JTabComponentAdapter) EcoreUtil.getExistingAdapter(tab,JTabComponentAdapter.class);
		if (adapter != null)
			return adapter.getComponentProxy();
		
		// The adapter doesn't have a component, so some problem, so we go to the end instead.
		return null;		
	}
	
	/**
	 * We don't really do an add component. If an index is specified, do an insertTab(...),
	 * otherwise do an addTab(...). 
	 */
	protected void addComponent(EObject jtabComponent, int position) {	
		// The component to actually add is within the JTabComponent.
		IBeanProxyHost componentProxyHost =
			BeanProxyUtilities.getBeanProxyHost(
				(IJavaInstance) jtabComponent.eGet(sfComponent));
		// Ensure the bean is instantiated
		IComponentProxyHost componentAdapter = (IComponentProxyHost) componentProxyHost;
		componentProxyHost.instantiateBeanProxy();
		if (getErrorStatus() == IBeanProxyHost.ERROR_SEVERE)
			return;	// We arent' instantiated, can't add a tab to us, but we did instantiate child.		
	
		IJavaInstance titleAttributeValue = null;
		IJavaInstance iconAttributeValue = null;
		// Get the values of each of the attributes of the JTabComponent.
		if (jtabComponent.eIsSet(sfTitle))
			titleAttributeValue =
				(IJavaInstance) jtabComponent.eGet(sfTitle);
		if (jtabComponent.eIsSet(sfIcon))
			iconAttributeValue =
				(IJavaInstance) jtabComponent.eGet(sfIcon);
				
		IBeanProxy componentBeanProxy = null;
		boolean poofed = false;
		if (componentProxyHost.getErrorStatus() != IBeanProxyHost.ERROR_SEVERE)
			componentBeanProxy = componentProxyHost.getBeanProxy();
		else {
			// We need to new up a dummy. Just create a JPanel.
			try {
				componentBeanProxy = getBeanProxyDomain().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("javax.swing.JPanel").newInstance(); //$NON-NLS-1$
				poofed = true;
			} catch (ThrowableProxy e) {
				// This really shouldn't of occurred.
				JavaVEPlugin.log(e, Level.WARNING);				
			}
		}
		// If the position is not -1, use insertTab to add the component, otherwise use addTab
		IBeanProxy positionBeanProxy = null;
		if (position != -1) {
			positionBeanProxy = getBeanProxyAt(position+1);	// We've already at the specified position in the EMF list, so we want the next one.
		}
		
		if (positionBeanProxy != null) {
			BeanAwtUtilities.invoke_insert_Tab_before(
					getBeanProxy(),
					BeanProxyUtilities.getBeanProxy(titleAttributeValue),
					BeanProxyUtilities.getBeanProxy(iconAttributeValue),
					componentBeanProxy,
					null,	// Tooltip doesn't matter for us here. It's never visible, so we don't bother.
					positionBeanProxy);
		} else {
			BeanAwtUtilities.invoke_add_Tab(
				getBeanProxy(),
				BeanProxyUtilities.getBeanProxy(titleAttributeValue),
				BeanProxyUtilities.getBeanProxy(iconAttributeValue),
				componentBeanProxy,
				null);
		}

		// Add a listener to the JTabComponent for when the special features changes.
		// To be on the safe side, remove any old ones, they shouldn't be there, but...
		JTabComponentAdapter a = (JTabComponentAdapter) EcoreUtil.getExistingAdapter(jtabComponent,JTabComponentAdapter.class);
		if (a != null)
			jtabComponent.eAdapters().remove(a);
		a = new JTabComponentAdapter();
		a.setComponentProxy(componentBeanProxy, poofed);
		jtabComponent.eAdapters().add(a);

		// Now that we've added it, set the parent component.
		componentAdapter.setParentComponentProxyHost((IComponentProxyHost) this);
		// This is required because AWT will not invalidate and relayout the container	
		revalidateBeanProxy();
	}

	/**
	 * Returns the currently selected index for this tabbed pane as an int. It will
	 * be only for ones we've defined. Any others (such as from a superclass) will not
	 * be selectable and -1 will be returned in that case.
	 */
	public int getSelectedIndex() {
		if (getErrorStatus() != IBeanProxyHost.ERROR_SEVERE) {
			IBeanProxy componentProxy = BeanAwtUtilities.invoke_tab_getSelectedComponent(getBeanProxy());
			if (componentProxy != null) {
				List tabs = (List) ((EObject) getTarget()).eGet(sfTabs);
				for (int i=0; i <tabs.size(); i++)
					if (componentProxy == getBeanProxyFor((EObject) tabs.get(i)))
						return i;
			}
		}
		
		return -1;
	}
	
	protected boolean isJTabComponentFeature(Object feature) {
		if (feature == sfIcon
			|| feature == sfTitle
			|| feature == sfTooltip)
			return true;
		return false;
	}
	/**
	 * Sets the icon for the page at the tab with the given component
	 */
	protected void setIconAt(IBeanProxy componentProxy, IJavaObjectInstance icon) {
		BeanAwtUtilities.invoke_tab_setIconAt(getBeanProxy(), componentProxy, BeanProxyUtilities.getBeanProxy(icon));
	}
	/**
     * Sets the selected component for this tabbedpane. This is public and is used by 
     * the edit parts to bring a selected page to the front so it can be viewed and edited.
     * PageModel is the actual component, not the tab.
	 */
	public void setSelectedComponent(IJavaObjectInstance pageModel) {
		if (pageModel == null || getErrorStatus() == IBeanProxyHost.ERROR_SEVERE)
			return;
		JTabComponentAdapter a = (JTabComponentAdapter) EcoreUtil.getExistingAdapter(InverseMaintenanceAdapter.getFirstReferencedBy(pageModel, sfComponent), JTabComponentAdapter.class);
		BeanAwtUtilities.invoke_tab_setSelectedComponent(getBeanProxy(), a.getComponentProxy());
		revalidateBeanProxy();
	}
	/**
	 * Sets the title text for the page with the given component
	 */
	protected void setTitleAt(IBeanProxy componentProxy, IJavaObjectInstance title) {
		IBeanProxy titleProxy = BeanProxyUtilities.getBeanProxy(title);
		if (titleProxy == null) {
			// Null for title is really invalid. We are using it to mean cancel.
			titleProxy = componentProxy.getProxyFactoryRegistry().getBeanProxyFactory().createBeanProxyWith(""); //$NON-NLS-1$
		}
		BeanAwtUtilities.invoke_tab_setTitleAt(getBeanProxy(), componentProxy, titleProxy);
	}
	
	/**
	 * We need to remove the adapter and the actual component which is part of the JTabComponent
	 */
	protected void removeComponent(EObject aJTabComponent) {
		if (getErrorStatus() == IBeanProxyHost.ERROR_SEVERE)
			return;	// Never added anything
								
		clearError(sfTabs, aJTabComponent);			
		IBeanProxy componentBeanProxy = null;	// Component to remove.
		// Remove the old constraint adapter, if one.
		JTabComponentAdapter a = (JTabComponentAdapter) EcoreUtil.getExistingAdapter(aJTabComponent,JTabComponentAdapter.class);
		if (a != null) {
			componentBeanProxy = a.getComponentProxy();
			BeanAwtUtilities.invoke_remove_Component(getBeanProxy(), componentBeanProxy);
			aJTabComponent.eAdapters().remove(a);			
		}
		
		releaseTab(aJTabComponent);
		
		// This is required because AWT will not invalidate and relayout the container
		revalidateBeanProxy();
	}
	

	public void releaseBeanProxy() {	
		if (isBeanProxyInstantiated() && ((EObject) target).eIsSet(sfTabs)) {
			// Need to release all of the tabs. This isn't being done in BeanProxyAdapter because
			// the tabs aren't bean proxies directly.
			List tabs = (List) ((EObject) target).eGet(sfTabs);
			if (tabs != null) {
				Iterator itr = tabs.iterator();
				while (itr.hasNext()) {
					EObject ip = (EObject) itr.next();
					releaseTab(ip);
				}
			}
		}
		
		super.releaseBeanProxy();
	}

	public void reinstantiateChild(IBeanProxyHost aChildBeanProxy) {

		// The child component is asking to be created.  We must create him at the correct index on us
		// To do this we must first get the target object of BeanProxy - the IJavaObjectInstance
		IJavaObjectInstance component = (IJavaObjectInstance) aChildBeanProxy.getTarget();
		// Now get the constraintComponent object
		EObject tabComponent = InverseMaintenanceAdapter.getIntermediateReference(getEObject(), sfTabs, sfComponent, component);
		if (tabComponent != null) {		
			// See what index this constraint component is present in our "components" relationship
			List components = (List) getEObject().eGet(sfTabs);
			int cndx = components.indexOf(tabComponent);
			removeComponent(tabComponent);
			addComponent(tabComponent, cndx);
		} else
			super.reinstantiateChild(aChildBeanProxy);

	}
	
	protected void removingAdapter() {
		// The adapter is being removed, so remove any Constraint Adapters that are pointing to us.
		if (getEObject().eIsSet(sfTabs)) {
			Iterator itr = ((List) getEObject().eGet(sfTabs)).iterator();
			while (itr.hasNext()) {
				Notifier n = (Notifier) itr.next();
				removeAdapters(n);
			}
		}
		super.removingAdapter();
	}
	
	protected void removeAdapters(Notifier n) {
		Adapter a = EcoreUtil.getExistingAdapter(n,JTabComponentAdapter.class);
		if (a != null)
			n.eAdapters().remove(a);
	}
	
	protected void releaseTab(EObject ip) {
		// Get all of the settings and release those that are beans that we own.
		// For JTabComponents that would normally be the icon, title, and the component.
		Iterator settings = (new BeanProxyUtilities.JavaSettingsEList(ip, false)).basicIterator();	// Use basic iterator so proxies aren't resolved since we are releasing anyway.
		while (settings.hasNext()) {
			// Use getExisting so that we don't fluff up a bean proxy host just to release it.
			IBeanProxyHost value = (IBeanProxyHost) EcoreUtil.getExistingAdapter((Notifier) settings.next(), IBeanProxyHost.BEAN_PROXY_TYPE);
			if (value != null)
				value.releaseBeanProxy();
		}
		
		// TODO For now we need to release the component too because it is a shared relationship. Need general way of handling this in the future. (NOT just here but everywhere, including BeanProxyAdapter)
		IJavaObjectInstance component = (IJavaObjectInstance) ip.eGet(sfComponent);
		IBeanProxyHost value = (IBeanProxyHost) EcoreUtil.getExistingAdapter(component, IBeanProxyHost.BEAN_PROXY_TYPE);
		if (value != null)
			value.releaseBeanProxy();
		
		
		// Also need to remove the tab component adapter and release the componentProxy within it.
		JTabComponentAdapter a = (JTabComponentAdapter) EcoreUtil.getExistingAdapter((Notifier) ip, JTabComponentAdapter.class);
		if (a != null) {
			((Notifier) ip).eAdapters().remove(a);
		}
	}
	
	/**
	 * An internal adapter added to the JTabComponent to listen for changes to
	 * JTabComponent's features so that we can reapply if necessary.
	 * It also holds the actual component that was added. This is necessary
	 * so that when we add an empty entry, we have unique component in it.
	 */
	private class JTabComponentAdapter extends AdapterImpl {
		
	
		protected IBeanProxy componentProxy;	// The proxy for the component in the tab.
		protected boolean poofedUpProxy;	// Was the above proxy a poofed up, if it was, then it can released when this adapter is removed.

		public JTabComponentAdapter() {
		}
		
		public IBeanProxy getComponentProxy() {
			return componentProxy;
		}
		
		public void setComponentProxy(IBeanProxy componentProxy, boolean poofed) {
			this.componentProxy = componentProxy;
			this.poofedUpProxy = poofed;
		}

		public boolean isAdapterForType(Object type) {
			return type == JTabComponentAdapter.class;
		}

		public void notifyChanged(Notification msg) {
			if (msg.getEventType() == Notification.REMOVING_ADAPTER) {
				if (poofedUpProxy)
					componentProxy.getProxyFactoryRegistry().releaseProxy(componentProxy);
				return;				
			}
			EObject sf = (EStructuralFeature)msg.getFeature();
			if (isJTabComponentFeature(sf)) {
				Object newValue = msg.getNewValue();
				int eventType = msg.getEventType();
				switch (eventType) {
					// ADD,REMOVE don't apply because these are single valued features.
					case Notification.SET :
						if (!CDEUtilities.isUnset(msg)) {
							if (sf == sfIcon)
								setIconAt(getComponentProxy(), (IJavaObjectInstance) newValue);
							else if (sf == sfTitle)
								setTitleAt(getComponentProxy(), (IJavaObjectInstance) newValue);
							break;
						}	// else flow into unset
					case Notification.UNSET :
						if (sf == sfIcon)
							setIconAt(getComponentProxy(), null);
						else if (sf == sfTitle)
							setTitleAt(getComponentProxy(), null);
						break;
				}
				revalidateBeanProxy();
			}
		}

	}
}
