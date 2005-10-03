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
package org.eclipse.ve.internal.cde.emf;
/*
 *  $RCSfile: DefaultModelAdapterFactory.java,v $
 *  $Revision: 1.8 $  $Date: 2005-10-03 19:21:04 $ 
 */

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.*;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;

import org.eclipse.ve.internal.cde.core.*;

/**
 * Base editpart factory. It is used to create editparts on a 
 * factory basis. It is used in the EMF environment.
 * 
 * The default procedure, unless the factory is subclassed, is:
 * 1) Is the modelObject itself an instance of the adapterClass, if so, return it.
 * 2) If the modelObject is IAdaptable, see if it can create the requested class.
 * 3) Go to the ClassDescriptorDecoratorPolicy that the factory was created with,
 *    use the modelAdapterClassname from the policy, if it has one.
 * 4) If the returned adapter is instanceof the adapterclass, return it.
 * 5) Finally, for the special case of IConstraintHandler, create an appropriate
 *    graphical EditPart and ask it for the adapter class through getAdapter.
 */

public class DefaultModelAdapterFactory implements IModelAdapterFactory {
	protected ClassDescriptorDecoratorPolicy policy;
	protected Map classStringToConstructor = new HashMap();	// Map so we don't keep looking them up. 

	public DefaultModelAdapterFactory(ClassDescriptorDecoratorPolicy policy) {
		this.policy = policy;
	}

	public IModelAdapter getAdapter(Object modelObject, Class adapterClass) {
		if (adapterClass.isInstance(modelObject))
			return (IModelAdapter) modelObject;
		
		if (modelObject instanceof IAdaptable) {
			IModelAdapter adapter = (IModelAdapter) ((IAdaptable) modelObject).getAdapter(adapterClass);
			if (adapter != null)
				return adapter;
		}

		
		IModelAdapter adapter = createAdapter(modelObject, adapterClass);
		if (adapter != null)
			return adapter;


		// Else one fallback for IConstraintHandler
		if (adapterClass == IConstraintHandler.class) {
			EditPart ep = createEditPart(modelObject);
			if (ep != null)
				return (IModelAdapter) ((IAdaptable) ep).getAdapter(adapterClass);
		}

		return null;
	}

	public boolean typeHasAdapter(Object type, Class adapter) {
		return (type instanceof EClassifier) && policy.getModelAdapterClassname((EClassifier) type) != null;
	}
	
	public IModelAdapter getSuperAdapter(Object superType, Object modelObject, Class adapter) {
		if (superType instanceof EClassifier) {
			
		}
		return null;
	}
	
	/*
	 * This is an internal method to create the model adapter.
	 * The model adapter must have a constructor that takes an Object, this
	 * will be the model the adapter is wrappering.
	 */
	protected IModelAdapter createAdapter(Object modelObject, Class adaptTo) {
		if (!(modelObject instanceof EObject))
			return null;
		return createAdapter(((EObject) modelObject).eClass(), modelObject, adaptTo);
	}

	/**
	 * @param modelObject
	 * @param adaptTo
	 * @return
	 * 
	 * @since 1.2.0
	 */
	private IModelAdapter createAdapter(EClassifier type, Object modelObject, Class adaptTo) {
		String classString = policy.getModelAdapterClassname(type);
		if (classString == null)
			return null;
		try {
			try {
				Object ctorOrClass = getConstructor(classString);
				IModelAdapter adapter;
				if (ctorOrClass instanceof Constructor) {
					Constructor constructor = (Constructor) ctorOrClass;
					if (adaptTo.isAssignableFrom(constructor.getDeclaringClass()))
						adapter = (IModelAdapter) constructor.newInstance(new Object[] {modelObject});
					else
						return null;	// Doesn't convert to desired type.
				} else {
					Class clazz = (Class) ctorOrClass;
					if (adaptTo.isAssignableFrom(clazz))
						adapter = (IModelAdapter) clazz.newInstance();
					else
						return null;	// Doesn't convert to desired type.
				}
				CDEPlugin.setInitializationData(adapter, classString, null);
				return adapter;
			} catch (NoSuchMethodException e) {
				return null;	// Already handled msg.
			}
		} catch (Exception e) {
			String message =
				java.text.MessageFormat.format(CDEMessages.Object_noinstantiate_EXC_, new Object[] { classString }); 
			Status s = new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, message, e);
			CDEPlugin.getPlugin().getLog().log(s);
			return null;
		}
	}
	
	/**
	 * Return a constructor for this class string, either one that takes only one argument of type Object, or one that takes
	 * no arguments.
	 * @param classString
	 * @return Constructor if it is the one argument constructor, or Class if it is the no argument constructor (so use class.newInstance() then).
	 * @throws NoSuchMethodException if can't find appropriate constructor. This has already been logged. No need to log again.
	 * 
	 * @since 1.2.0
	 */
	protected Object getConstructor(String classString) throws NoSuchMethodException {
		if (classStringToConstructor.containsKey(classString)) {
			Object ctorOrClass = classStringToConstructor.get(classString);
			if (ctorOrClass == null)
				throw new NoSuchMethodException(classString);	// We had explicity set "null" to indicate we tried and failed.
			return ctorOrClass;
		} else {
			try {
				Class adapterClass = CDEPlugin.getClassFromString(classString);
				Object ctorOrClass = null;
				try {
					ctorOrClass = adapterClass.getConstructor(new Class[] { Object.class});
				} catch (NoSuchMethodException e) {
					try {
						adapterClass.getConstructor(null); // See if there is a default constructor available.
						ctorOrClass = adapterClass;
					} catch (NoSuchMethodException e1) {
						// Could not find one arg or default. 
						String message = java.text.MessageFormat.format(CDEMessages.Object_noinstantiate_EXC_, new Object[] { classString});
						Status s = new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, message, e1);
						CDEPlugin.getPlugin().getLog().log(s);
						classStringToConstructor.put(classString, null); // Put a null out so we don't try again.
						throw new NoSuchMethodException(classString);
					}
				}
				classStringToConstructor.put(classString, ctorOrClass);
				return ctorOrClass;
			} catch (ClassNotFoundException e) {
				// Could find class.
				String message = java.text.MessageFormat.format(CDEMessages.Object_noinstantiate_EXC_, new Object[] { classString});
				Status s = new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, message, e);
				CDEPlugin.getPlugin().getLog().log(s);
				classStringToConstructor.put(classString, null); // Put a null out so we don't try again.
				throw new NoSuchMethodException(classString);
			}
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
			try {
				Object ctorOrClass = getConstructor(epClassString);
				EditPart editPart = null;
				if (ctorOrClass instanceof Constructor)
					editPart = (EditPart) ((Constructor) ctorOrClass).newInstance(new Object[] { modelObject});
				else {
					editPart = (EditPart) ((Class) ctorOrClass).newInstance();
					editPart.setModel(modelObject);
				}
				CDEPlugin.setInitializationData(editPart, epClassString, null);
				return editPart;
			} catch (NoSuchMethodException e) {
				// Already handled msg.
				return null;
			}
		} catch (Exception e) {
			String message =
				java.text.MessageFormat.format(CDEMessages.Object_noinstantiate_EXC_, new Object[] { epClassString }); 
			Status s = new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, message, e);
			CDEPlugin.getPlugin().getLog().log(s);
			return null;
		}
	}
}
