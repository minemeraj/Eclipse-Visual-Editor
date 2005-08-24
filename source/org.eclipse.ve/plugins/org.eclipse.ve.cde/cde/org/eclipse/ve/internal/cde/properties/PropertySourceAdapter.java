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
package org.eclipse.ve.internal.cde.properties;
/*
 *  $RCSfile: PropertySourceAdapter.java,v $
 *  $Revision: 1.10 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.lang.reflect.Constructor;
import java.text.MessageFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.decorators.PropertyDescriptorDecorator;
import org.eclipse.ve.internal.cde.decorators.PropertyDescriptorInformation;
import org.eclipse.ve.internal.cde.emf.EMFEditDomainHelper;
import org.eclipse.ve.internal.propertysheet.INeedData;
import org.eclipse.ve.internal.propertysheet.ISourcedPropertyDescriptor;

public class PropertySourceAdapter extends AdapterImpl implements IPropertySource, INeedData {
	protected EditDomain domain;

	/**
	 * Helper method to return the descriptor with the given id.
	 * @param source
	 * @param propertyID
	 * @return descriptor with that id or <code>null</code> if not found.
	 * 
	 * @since 1.1.0
	 */
	public static IPropertyDescriptor getDescriptorForID(IPropertySource source, Object propertyID) {
		IPropertyDescriptor[] descriptors = source.getPropertyDescriptors();
		for (int i = 0; i < descriptors.length; i++) {
			if (propertyID.equals(descriptors[i].getId()))
				return descriptors[i];
		}
		return null;
	}
	
	/**
	 * A helper method to handle command/sourced property descriptors without having to do the
	 * checks over and over.
	 * 
	 * @param source
	 * @param descriptor
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static boolean isPropertySet(IPropertySource source, IPropertyDescriptor descriptor) {
		// If the descriptor is also an ISourcedPropertyDescriptor, then
		// route over to it, but use the wrappered source instead. Else
		// Use the standard mechanism for testing the value from the wrappered source.
		if (descriptor instanceof ISourcedPropertyDescriptor)
			return ((ISourcedPropertyDescriptor) descriptor).isSet(source);
		else
			return source.isPropertySet(descriptor.getId());
		
	}
	
	/**
	 * A helper method to handle command/sourced property descriptors without having to checks over and over.
	 * @param source
	 * @param descriptor
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public static Object getPropertyValue(IPropertySource source, IPropertyDescriptor descriptor) {
		// If the descriptor is also an ISourcedPropertyDescriptor, then
		// route over to it, but use the wrappered source instead. Else
		// Use the standard mechanism for getting the value from the wrappered source.
		if (descriptor instanceof ISourcedPropertyDescriptor)
			return ((ISourcedPropertyDescriptor) descriptor).getValue(source);
		else
			return source.getPropertyValue(descriptor.getId());
	}
	
	public PropertySourceAdapter() {
	}

	public void setData(Object data) {
		domain = (EditDomain) data;
	}

	protected IPropertyDescriptor createPropertyDescriptorAdapter(EStructuralFeature sFeature) {
		// Couldn't find the correct adapter factory from the sFeature, so use the one from the target instead.
		Resource resource = ((EObject) target).eResource();
		ResourceSet resourceSet = resource != null ? resource.getResourceSet() : EMFEditDomainHelper.getResourceSet(domain);	// If not yet contained, then use the rset from the domain.
		if (resourceSet != null) {
			AdapterFactory factory =
				EcoreUtil.getAdapterFactory(
					resourceSet.getAdapterFactories(),
					AbstractPropertyDescriptorAdapter.IPROPERTYDESCRIPTOR_TYPE);
			if (factory != null)
				return (IPropertyDescriptor) factory.adaptNew(sFeature, AbstractPropertyDescriptorAdapter.IPROPERTYDESCRIPTOR_TYPE);
		}

		// No factory found, use default adapter.
		DecoratedPropertyDescriptorAdapter adapter = new DecoratedPropertyDescriptorAdapter();
		sFeature.eAdapters().add(adapter);
		return adapter;
	}

	public Object getEditableValue() {
		return target;
	}

	/**
	 * Helper to avoid casting all over the place.  Maybe should be cached later
	 */
	protected final EObject getEObject() {
		return (EObject) target;
	}

	protected EAnnotation getDecorator(EModelElement model, Class decoratorType) {
		Iterator decorItr = model.getEAnnotations().iterator();
		while (decorItr.hasNext()) {
			EAnnotation o = (EAnnotation) decorItr.next();
			if (decoratorType.isInstance(o))
				return o;
		}
		return null;
	}

	/**
	 * See if this feature should be included. The default is
	 * to get the property descriptor decorator and see if it
	 * is not marked as hidden. Hidden features should not be included.
	 * Also if not hidden, then if it is explicitly NOT a designTimeProperty,
	 * then it should also not be included.
	 *
	 * Setting isDesignTimeProperty to explicit false means that the property
	 * is a runtime only property and is not settable at designtime. Setting it
	 * explicitly true means that it is only a designtime property and can't
	 * be set in the runtime (these are usually extra properties that result
	 * in some special code being generated and is not really a property of
	 * the object). If it is not explicitly set, then that means do the default,
	 * it is both a runtime and designtime property.
	 */
	protected boolean includeFeature(EStructuralFeature sfeature) {
		PropertyDescriptorDecorator decorator = (PropertyDescriptorDecorator) getDecorator(sfeature, PropertyDescriptorDecorator.class);
		if (decorator != null)
			return !decorator.isHidden() && (!decorator.isSetDesigntimeProperty() || decorator.isDesigntimeProperty());
		return true;
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		ArrayList descList = new ArrayList();
		EObject mofTarget = (EObject) target;
		EClass mofClass = mofTarget.eClass();

		Iterator i = getAllFeatures(mofClass).iterator();
		while (i.hasNext()) {
			EStructuralFeature sfeature = (EStructuralFeature) i.next();
			if (!sfeature.isMany() && includeFeature(sfeature)) {
				// TODO Need a method of handling isMany too.
				// Not a many feature, and feature was marked as included.
				descList.add(getPropertyDescriptor(sfeature));
			}
		}

		if (shouldAllowAnnotationRename())
			descList.addAll(AnnotationPolicy.getAnnotationPropertyDescriptors(getTarget(), domain));

		return (IPropertyDescriptor[]) descList.toArray(new IPropertyDescriptor[descList.size()]);
	}
	
	protected boolean shouldAllowAnnotationRename() {
		return domain != null;
	}

	protected List getAllFeatures(EClass cls) {
		return cls.getEAllStructuralFeatures();
	}

	/**
	 * Get the property descriptor.
	 * This can be overridden if necessary to retrieve
	 * from a different location.
	 * 
	 * It first sees if already adapted, if so use that.
	 * Then it checks to see if there is property descriptor information that says
	 * there is specific descriptor that needs to know the source (i.e. not an adapter, which is for the
	 * feature no matter what the source is). If so, it created that. If not, it tries to adapt it.
	 */
	protected IPropertyDescriptor getPropertyDescriptor(EStructuralFeature sfeature) {
		IPropertyDescriptor desc = (IPropertyDescriptor) EcoreUtil.getExistingAdapter(sfeature, AbstractPropertyDescriptorAdapter.IPROPERTYDESCRIPTOR_TYPE);
		if (desc == null)
			desc = createPropertyDescriptorFromInfo(sfeature);
		if (desc == null)
			desc = createPropertyDescriptorAdapter(sfeature);
		return desc;
	}

	/**
	 * Create a property descriptor of the class in the property descriptor info, if the info exists
	 * and says not adapter. It will try two possible constructors. If neither exists then it will
	 * return null. Target as EObject is always sent on the constructor because if the source wasn't
	 * necessary to the descriptor, then a straight adapter would be used.
	 *
	 * isAdapter false means the descriptor is not an adapter, so it needs to be created each time
	 * it is requested. This is used in the case that the descriptor needs to know the source of the property for
	 * it to do its job.
	 */
	protected IPropertyDescriptor createPropertyDescriptorFromInfo(EStructuralFeature sfeature) {
		PropertyDescriptorInformation descInfo =
			(PropertyDescriptorInformation) getDecorator(sfeature, PropertyDescriptorInformation.class);
		if (descInfo != null && !descInfo.isAdapter() && descInfo.getPropertyDescriptorClassname() != null) {
			String cname = descInfo.getPropertyDescriptorClassname();
			try {
				Class c = CDEPlugin.getClassFromString(cname);
				if (c != null) {
					try {
						Constructor ctor = c.getConstructor(new Class[] { EObject.class, EStructuralFeature.class });
						IPropertyDescriptor pd = (IPropertyDescriptor) ctor.newInstance(new Object[] {(EObject) target, sfeature });
						CDEPlugin.setInitializationData(pd, cname, null);
						return pd;
					} catch (NoSuchMethodException e) {
					}
					try {
						Constructor ctor = c.getConstructor(new Class[] { EObject.class });
						IPropertyDescriptor pd = (IPropertyDescriptor) ctor.newInstance(new Object[] {(EObject) target });
						CDEPlugin.setInitializationData(pd, cname, null);
						return pd;
					} catch (NoSuchMethodException e) {
					}
				}
			} catch (Exception e) {
				String msg = MessageFormat.format(CDEMessages.Object_noinstantiate_EXC_, new Object[] { cname }); 
				CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, e));
			}
		}
		return null;
	}

	public Object getPropertyValue(Object feature) {
		try {
			Object value = ((EObject) target).eGet((EStructuralFeature) feature);
			if (value instanceof IPropertySource)
				return value;
			if (value instanceof EObject) {
				Object result = EcoreUtil.getRegisteredAdapter((EObject) value, IPropertySource.class);
				return result != null ? result : value; // If no adapter could be found, use the value.
			}
			return value;
		} catch (Exception exc) {
			CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", exc)); //$NON-NLS-1$
			return null;
		}
	}

	public boolean isAdapterForType(Object type) {
		return IPropertySource.class.equals(type);
	}

	public boolean isPropertySet(Object feature) {
		return ((EObject) target).eIsSet((EStructuralFeature) feature);
	}

	public void resetPropertyValue(Object feature) {
		((EObject) target).eUnset((EStructuralFeature) feature);
	}

	public void setPropertyValue(Object feature, Object val) {
		((EObject) target).eSet((EStructuralFeature) feature, val);
	}
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (getTarget() != null)
			return super.toString() + ':' + getTarget().toString();
		return super.toString();
	}

}
