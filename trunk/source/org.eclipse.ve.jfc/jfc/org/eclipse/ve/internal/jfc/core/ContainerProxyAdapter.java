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
 *  $RCSfile: ContainerProxyAdapter.java,v $
 *  $Revision: 1.8 $  $Date: 2005-02-09 13:57:32 $ 
 */

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.notify.*;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.ThrowableProxy;
import org.eclipse.jem.java.JavaClass;
import org.eclipse.jem.java.TypeKind;

import org.eclipse.ve.internal.cde.core.CDEUtilities;
import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.*;

public class ContainerProxyAdapter extends ComponentProxyAdapter implements IHoldProcessing {

	protected EReference sfConstraintConstraint, sfContainerComponents, sfConstraintComponent, sfName, sfLayout;

	public ContainerProxyAdapter(IBeanProxyDomain domain) {
		super(domain);

		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfConstraintConstraint = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_CONSTRAINT);
		sfContainerComponents = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_COMPONENTS);
		sfConstraintComponent = JavaInstantiation.getReference(rset, JFCConstants.SF_CONSTRAINT_COMPONENT);
		sfLayout = JavaInstantiation.getReference(rset, JFCConstants.SF_CONTAINER_LAYOUT);
		sfName = JavaInstantiation.getReference(rset, JFCConstants.SF_COMPONENT_NAME);
	}

	protected void applied(EStructuralFeature as, Object newValue, int position) {

		if (as == sfContainerComponents) {
			if (!holding())
				addComponent((EObject) newValue, position);
		} else {
			super.applied(as, newValue, position);
		}

	}
	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {

		if (sf == sfContainerComponents) {
			if (!holding())
				removeComponent((EObject) oldValue);
		} else {
			super.canceled(sf, oldValue, position);
		}

	}

	public void releaseBeanProxy() {
		// Need to release all of the components. This isn't being done in BeanProxyAdapter because
		// the constraint components aren't bean proxies directly.
		if ((isBeanProxyInstantiated() || getErrorStatus() == ERROR_SEVERE) && ((JavaClass) getEObject().eClass()).getKind() != TypeKind.UNDEFINED_LITERAL) {
			if (getEObject().eIsSet(sfContainerComponents)) {
				// TODO This really does nothing because the settings in the CC are shared settings and so are not
				// released. Next release we need a general way of handling releasing shared settings.
				Iterator itr = ((List) getEObject().eGet(sfContainerComponents)).iterator();
				while (itr.hasNext()) {
					EObject ip = (EObject) itr.next();
					// Get all of the settings and release those that are beans that we own.
					// For constraintComponents that would normally be the constraint and the component.
					Iterator settings = (new BeanProxyUtilities.JavaSettingsEList(ip, false)).basicIterator(); // Use basic iterator so proxies aren't resolved since we are releasing anyway.
					while (settings.hasNext()) {
						// Use getExisting so that we don't fluff up a bean proxy host just to release it.
						IBeanProxyHost value = (IBeanProxyHost) EcoreUtil.getExistingAdapter((Notifier) settings.next(),
								IBeanProxyHost.BEAN_PROXY_TYPE);
						if (value != null)
							value.releaseBeanProxy();
					}
				}
			}

			// Bugzilla 59391 - force a release of the layout TODO Needs to be handled in a generic way because if this goes invalid (i.e. class deleted) then this layout will not be released.
			IJavaInstance layoutValue = (IJavaInstance) getEObject().eGet(getEObject().eClass().getEStructuralFeature("layout"));
			if (layoutValue != null) {
				IBeanProxyHost layoutProxyHost = BeanProxyUtilities.getBeanProxyHost(layoutValue);
				layoutProxyHost.releaseBeanProxy();
			} 
		}

		super.releaseBeanProxy();
	}
	public void reinstantiateChild(IBeanProxyHost aChildBeanProxy) {

		// The child component is asking to be created.  We must create him at the correct index on us
		// To do this we must first get the target object of BeanProxy - the IJavaObjectInstance
		IJavaObjectInstance component = (IJavaObjectInstance) aChildBeanProxy.getTarget();
		// Now get the constraintComponent object
		EObject constraintComponent = InverseMaintenanceAdapter.getIntermediateReference(getEObject(), sfContainerComponents, sfConstraintComponent, component);
		if (constraintComponent != null) {		
			// See what index this constraint component is present in our "components" relationship
			List components = (List) getEObject().eGet(sfContainerComponents);
			int cndx = components.indexOf(constraintComponent);
			removeComponent(constraintComponent);
			addComponent(constraintComponent, cndx);
		} else
			super.reinstantiateChild(aChildBeanProxy);

	}
	protected void removingAdapter() {
		// The adapter is being removed, so remove any Constraint Adapters that are pointing to us.
		if (getEObject().eIsSet(sfContainerComponents)) {
			Iterator itr = ((List) getEObject().eGet(sfContainerComponents)).iterator();
			while (itr.hasNext()) {
				Notifier n = (Notifier) itr.next();
				removeAdapters(n);
			}
		}
		super.removingAdapter();
	}

	protected void removeAdapters(Notifier n) {
		Adapter a = EcoreUtil.getExistingAdapter(n,ComponentConstraintAdapter.class);
		if (a != null)
			n.eAdapters().remove(a);
		n = ((EObject) n).eIsSet(sfConstraintComponent) ? (Notifier)((EObject)n).eGet(sfConstraintComponent) : null;
		if (n != null) {
			a = EcoreUtil.getExistingAdapter(n,ComponentNameAdapter.class);
			if (a != null)
				n.eAdapters().remove(a);
		}
	}

	protected void removeComponent(EObject aConstraintComponent) {
		// Remove the old constraint adapter, if one.
		removeAdapters(aConstraintComponent);

		// The component to actually remove is within the ConstraintComponent too.
		clearError(sfContainerComponents, aConstraintComponent);		
		IJavaInstance component = (IJavaInstance) aConstraintComponent.eGet(sfConstraintComponent);
		IBeanProxyHost aComponentBeanProxyHost = BeanProxyUtilities.getBeanProxyHost(component);

		// It is possible the component or ourselves didn't instantiate, or has already been released.  We then can't cancel it.
		if (aComponentBeanProxyHost != null) {
			// Clear the the error setting, if set.
			((ComponentProxyAdapter) aComponentBeanProxyHost).clearError(sfContainerComponents, aConstraintComponent);
			if (aComponentBeanProxyHost.isBeanProxyInstantiated() && aComponentBeanProxyHost.getErrorStatus() != IBeanProxyHost.ERROR_SEVERE && isBeanProxyInstantiated() && getErrorStatus() != IBeanProxyHost.ERROR_SEVERE) {			
				// It is a valid beanproxy, so remove it.
				BeanAwtUtilities.invoke_remove_Component(getBeanProxy(), aComponentBeanProxyHost.getBeanProxy());
				// This is required because AWT will not invalidate and relayout the container
				revalidateBeanProxy();
				
				// Also it is possible that visibility was changed (for example CardLayout does this). Need to restore to default.
				((ComponentProxyAdapter) aComponentBeanProxyHost).reapplyVisibility();
			}
// TODO we should do a release of both the component and the constraint, but don't have general way yet.	aComponentBeanProxyHost.releaseBeanProxy();			
		}
	}
	
	protected void addComponent(EObject aConstraintComponent, int position) throws ReinstantiationNeeded {
		IJavaInstance constraintAttributeValue = null;
		boolean constraintIsSet = false;
		// Get the value of the constraint attribute of the component, the component in this case is a ConstraintsComponent.
		if (aConstraintComponent.eIsSet(sfConstraintConstraint)) {
			constraintAttributeValue = (IJavaInstance) aConstraintComponent.eGet(sfConstraintConstraint);
			constraintIsSet = true;
		}
		
		// Add a listener to the constraint component for when the constraint changes.
		// To be on the safe side, remove any old ones, they shouldn't be there, but...
		Adapter a = EcoreUtil.getExistingAdapter(aConstraintComponent,ComponentConstraintAdapter.class);
		if (a != null)
			aConstraintComponent.eAdapters().remove(a);
		a = new ComponentConstraintAdapter();
		aConstraintComponent.eAdapters().add(a);
				
		addComponentWithConstraint(aConstraintComponent, constraintAttributeValue, position, constraintIsSet);
	}

	/*
	 * Return the first instantiated bean proxy at or after the given index.
	 * It is assumed that tests for the container being instantiated has already been done.
	 * Return null if not found.
	 */
	protected IBeanProxy getBeanProxyAt(int position) {
		List components = (List) ((EObject) getTarget()).eGet(sfContainerComponents);
		for (int i=position; i<components.size(); i++) {
			EObject componentConstraint = (EObject) components.get(i);
			IBeanProxyHost componentProxyHost =
				BeanProxyUtilities.getBeanProxyHost((IJavaInstance) componentConstraint.eGet(sfConstraintComponent));
			if (componentProxyHost.isBeanProxyInstantiated())
				return componentProxyHost.getBeanProxy();
		}
		
		return null;
	}
	
	protected void addComponentWithConstraint(EObject aConstraintComponent, IJavaInstance constraintAttributeValue, int position, boolean constraintIsSet) throws ReinstantiationNeeded {		
		// The component to actually add is within the ConstraintComponent too.
		IJavaInstance component = (IJavaInstance) aConstraintComponent.eGet(sfConstraintComponent);
		if (component == null) {
			// We have an error, the component couldn't be found. This usually means it was set somewhere else.
			// TODO Is this still valid? This may of been put here when the relationship was still composite.
			processError(sfContainerComponents, new IllegalAccessException(VisualMessages.getString("Container.moved._WARN_")), aConstraintComponent); //$NON-NLS-1$
		}
		IBeanProxyHost componentProxyHost =	BeanProxyUtilities.getBeanProxyHost(component);
		// Ensure the bean is instantiated
		IComponentProxyHost componentAdapter = (IComponentProxyHost) componentProxyHost;
		componentProxyHost.instantiateBeanProxy();
		// It is possible the component didn't instantiate.  We then can't apply it
		// and we should flag it as having a warning error
		if (componentProxyHost.getErrorStatus() == IBeanProxyHost.ERROR_SEVERE || getErrorStatus() == IBeanProxyHost.ERROR_SEVERE) {
			// Either we or the setting could not be instantiated, so don't add. This guy already has his error so we don't need to
			// duplicate it.
			return;
		}
		if (!constraintIsSet) {
			Adapter a = EcoreUtil.getExistingAdapter(component,ComponentNameAdapter.class);
			if (a != null) 
				component.eAdapters().remove(a);
			a = new ComponentNameAdapter();
			component.eAdapters().add(a);
			constraintAttributeValue = componentProxyHost.getBeanPropertyValue(sfName);
		} else {
			Adapter a = EcoreUtil.getExistingAdapter(component,ComponentNameAdapter.class);
			if (a != null) 
				component.eAdapters().remove(a);
		}

		IBeanProxy componentBeanProxy = componentProxyHost.getBeanProxy();
		IBeanProxy beforeBeanProxy = null;	// The beanproxy to go before.

		if (position != -1)
			beforeBeanProxy = getBeanProxyAt(position+1);	// Need to do +1 because we (componentBeanProxy) are already at that position in the EMF list. So we want to go before next guy.

		IBeanProxy constraintBeanProxy = null;
		if (constraintAttributeValue != null) {
			IBeanProxyHost constraintHost = BeanProxyUtilities.getBeanProxyHost(constraintAttributeValue);
			constraintBeanProxy = constraintHost.instantiateBeanProxy();
			if (constraintHost.getErrorStatus() == ERROR_SEVERE)
				((ComponentProxyAdapter) componentProxyHost).processError(sfContainerComponents, ((IErrorHolder.ExceptionError) constraintHost.getErrors().get(0)).error, aConstraintComponent);
		}

		try {
			// Invoke a method to add the component to the container.  Use add(Component) if we don't have a constraint,
			if (constraintBeanProxy == null) {
				if (beforeBeanProxy != null) {
					BeanAwtUtilities.invoke_add_Component_before(getBeanProxy(), componentBeanProxy, beforeBeanProxy);
				} else {
					BeanAwtUtilities.invoke_add_Component(getBeanProxy(), componentBeanProxy);
				}
			} else if (beforeBeanProxy != null) {
				BeanAwtUtilities.invoke_add_Component_Object_before(getBeanProxy(), componentBeanProxy, constraintBeanProxy, beforeBeanProxy);
			} else {
				BeanAwtUtilities.invoke_add_Component_Object(getBeanProxy(), componentBeanProxy, constraintBeanProxy);
			}
	
			// If the target VM layout is null then apply the bounds/size/location
			// Presumabely the target VM layout manager can't be null if it is a non-null value in the EMF model
			// so check this first to avoid VM traffic before querying the live value
			if(!getEObject().eIsSet(sfLayout) && BeanAwtUtilities.invoke_getLayout(getBeanProxy()) == null)
				componentAdapter.applyNullLayoutConstraints(); // Make sure the bounds are applied.
		} catch (ThrowableProxy e) {
			((ComponentProxyAdapter) componentProxyHost).processError(sfContainerComponents, e, aConstraintComponent);
		}
			

		// Now that we've added it, set the parent component.
		componentAdapter.setParentComponentProxyHost((IComponentProxyHost) this);
		// This is required because AWT will not invalidate and relayout the container	
		revalidateBeanProxy();
		
		clearError(sfContainerComponents, aConstraintComponent);
		((ComponentProxyAdapter) componentProxyHost).clearError(sfContainerComponents, aConstraintComponent);
				

	}

	/*
	 * An internal adapter added to the ConstraintComponent to listen for changes to the 
	 * constraints so that we can reapply if necessary.
	 */
	private class ComponentConstraintAdapter extends AdapterImpl {

		public ComponentConstraintAdapter() {
		}

		public boolean isAdapterForType(Object type) {
			return type == ComponentConstraintAdapter.class;
		}

		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sfConstraintConstraint) {
				try {
					switch (msg.getEventType()) {
						// ADD,REMOVE don't apply because this is a single valued feature
						case Notification.SET :
							if (!CDEUtilities.isUnset(msg)) {
								int componentPos = ((List) ((EObject) ContainerProxyAdapter.this.getTarget()).eGet(sfContainerComponents)).indexOf(getTarget());
								addComponentWithConstraint((EObject) getTarget(), (IJavaInstance) msg.getNewValue(), componentPos, true);
								break;
							}	// Else flow into unset.
						case Notification.UNSET :
							// This is still an add but with no constraint this time.
							int componentPos = ((List) ((EObject) ContainerProxyAdapter.this.getTarget()).eGet(sfContainerComponents)).indexOf(getTarget());
							addComponentWithConstraint((EObject) getTarget(), null, componentPos, false);
							break;
					}
				} catch (ReinstantiationNeeded e) {
					reinstantiateBeanProxy();
				}
			}
		}

	}
	/*
	 * An internal adapter added to the Component to listen for changes to the 
	 * <name> property so that we can reapply if necessary to the constraint field in the live bean.
	 */
	private class ComponentNameAdapter extends AdapterImpl {

		public ComponentNameAdapter() {
		}

		public boolean isAdapterForType(Object type) {
			return type == ComponentNameAdapter.class;
		}

		public void notifyChanged(Notification msg) {
			if (msg.getFeature() == sfName) {
				try {				
					switch (msg.getEventType()) {
						// ADD,REMOVE don't apply because this is a single valued feature
						case Notification.SET :
							if (!CDEUtilities.isUnset(msg)) {
								EObject constraintComponent = InverseMaintenanceAdapter.getFirstReferencedBy(getTarget(), sfConstraintComponent);
								int componentPos = ((List) ((EObject) ContainerProxyAdapter.this.getTarget()).eGet(sfContainerComponents)).indexOf(constraintComponent);
								addComponentWithConstraint(constraintComponent, null, componentPos, false);
								break;
							}	// else flow into unset.
						case Notification.UNSET :
							// This is done still an add but with no constraint this time because name was unset.
							EObject constraintComponent = InverseMaintenanceAdapter.getFirstReferencedBy(getTarget(), sfConstraintComponent);
							int componentPos = ((List) ((EObject) ContainerProxyAdapter.this.getTarget()).eGet(sfContainerComponents)).indexOf(constraintComponent);
							addComponentWithConstraint(constraintComponent, null, componentPos, false);
							break;
					}
				} catch (ReinstantiationNeeded e) {
					reinstantiateBeanProxy();
				}
					
			}
		}

	}	
	/**
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#isValidFeature(EStructuralFeature, Object)
	 */
	protected boolean isValidFeature(EStructuralFeature sf, Object object) {
		// Kludge to test component too
		if (sf == sfContainerComponents) {
			IJavaInstance component = (IJavaInstance) ((EObject) object).eGet(sfConstraintComponent);
			if (component != null) {
				ComponentProxyAdapter componentProxyHost =	(ComponentProxyAdapter) BeanProxyUtilities.getBeanProxyHost(component);
				return componentProxyHost.isTrulyValidFeature(sf, object);
			}
		}
		return super.isValidFeature(sf, object);
	}
	
	private int holdCount = 0;
	
	protected final boolean holding() {
		return holdCount > 0;
	}
	
	/**
	 * ContainerProxyAdapter holds the "components" relationship listening to
	 * allow major changes to occur. When resumed, it will completely refresh
	 * the "components" relationship.
	 * @see org.eclipse.ve.internal.jfc.core.IHoldProcessing#holdProcessing()
	 */
	public final void holdProcessing() {
		if (holdCount++ == 0)
			holdStarted();
	}
	
	protected void holdStarted() {
		// Remove all of the components
		List components = (List) getEObject().eGet(sfContainerComponents);
		for (int i=components.size()-1; i >= 0; i--)
			removeComponent((EObject) components.get(i));
	}

	/**
	 * @see org.eclipse.ve.internal.jfc.core.IHoldProcessing#resumeProcessing()
	 */
	public final void resumeProcessing() {
		if (--holdCount == 0) {
			holdEnded();
		} else if (holdCount < 0)
			holdCount = 0;
	}
	
	protected void holdEnded() {
		// Add all of the components back.
		List components = (List) getEObject().eGet(sfContainerComponents);
		for (int i=0; i < components.size(); i++)
			addComponent((EObject) components.get(i), i);
	}

}
