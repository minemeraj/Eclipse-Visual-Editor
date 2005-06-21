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
package org.eclipse.ve.internal.cde.emf;
/*
 *  $RCSfile: DefaultModelAdapterFactory.java,v $
 *  $Revision: 1.5 $  $Date: 2005-06-21 21:43:42 $ 
 */

import java.lang.reflect.Constructor;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.gef.EditPart;

/**
 * Base editpart factory. It is used to create editparts on a 
 * factory basis. It is used in the EMF environment.
 * 
 * The default procedure, unless the factory is subclassed, is:
 * 1) Is the modelObject itself an instance of the adapterClass, if so, return it.
 * 2) Go to the ClassDescriptorDecoratorPolicy that the factory was created with,
 *    use the modelAdapterClassname from the policy, if it has one.
 * 3) If the returned adapter is instanceof the adapterclass, return it.
 * 4) Else, if the adapter itself is IAdaptable, ask the adapter for the
 *    adapterClass.
 * 5) Finally, for the special case of IConstraintHandler, create an appropriate
 *    graphical EditPart and ask it for the adapter class through getAdapter.
 */

public class DefaultModelAdapterFactory implements IModelAdapterFactory {
	protected ClassDescriptorDecoratorPolicy policy;

	public DefaultModelAdapterFactory(ClassDescriptorDecoratorPolicy policy) {
		this.policy = policy;
	}

	public Object getAdapter(Object modelObject, Class adapterClass) {
		if (adapterClass.isInstance(modelObject))
			return modelObject;

		Object adapter = createAdapter(modelObject);
		if (adapterClass.isInstance(adapter))
			return adapter;

		if (adapter instanceof IAdaptable)
			return ((IAdaptable) adapter).getAdapter(adapterClass);

		// Else one fallback for IConstraintHandler
		if (adapterClass == IConstraintHandler.class) {
			EditPart ep = createEditPart(modelObject);
			if (ep != null)
				return ((IAdaptable) ep).getAdapter(adapterClass);
		}

		return null;
	}

	/*
	 * This is an internal method to create the model adapter.
	 * The model adapter must have a constructor that takes an Object, this
	 * will be the model the adapter is wrappering.
	 */
	protected Object createAdapter(Object modelObject) {
		if (!(modelObject instanceof EObject))
			return null;
		String classString = policy.getModelAdapterClassname(((EObject) modelObject).eClass());
		if (classString == null)
			return null;
		try {
			Class adapterClass = CDEPlugin.getClassFromString(classString);
			Object adapter = null;
			try {
				Constructor ctor = adapterClass.getConstructor(new Class[] { Object.class });
				adapter = ctor.newInstance(new Object[] { modelObject });
			} catch (NoSuchMethodException e) {
				adapter = adapterClass.newInstance();
			}
			CDEPlugin.setInitializationData(adapter, classString, null);
			return adapter;
		} catch (Exception e) {
			String message =
				java.text.MessageFormat.format(CDEMessages.Object_noinstantiate_EXC_, new Object[] { classString }); 
			Status s = new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, message, e);
			CDEPlugin.getPlugin().getLog().log(s);
			return null;
		}
	}

	/*
	 * This is an internal method to create the default editpart.
	 * It is used because for IConstraintHandler the default graphical editpart
	 * would be a good choice for fallback.
	 *
	 * The modelObject will be set into the editpart after it is created.
	 */
	protected EditPart createEditPart(Object modelObject) {
		String epClassString = policy.getGraphViewClassname(((EObject) modelObject).eClass());
		if (epClassString == null)
			return null;
		try {
			Class editpartClass = CDEPlugin.getClassFromString(epClassString);
			EditPart editpart = null;
			try {
				Constructor ctor = editpartClass.getConstructor(new Class[] { Object.class });
				editpart = (EditPart) ctor.newInstance(new Object[] { modelObject });
			} catch (NoSuchMethodException e) {
				// Use default ctor instead.
				editpart = (EditPart) editpartClass.newInstance();
				editpart.setModel(modelObject);
			}
			CDEPlugin.setInitializationData(editpart, epClassString, null);
			return editpart;
		} catch (Exception e) {
			String message =
				java.text.MessageFormat.format(CDEMessages.Object_noinstantiate_EXC_, new Object[] { epClassString }); 
			Status s = new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, message, e);
			CDEPlugin.getPlugin().getLog().log(s);
			return null;
		}
	}
}
