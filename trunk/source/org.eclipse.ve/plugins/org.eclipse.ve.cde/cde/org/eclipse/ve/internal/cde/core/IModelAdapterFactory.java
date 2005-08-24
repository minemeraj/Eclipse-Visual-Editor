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
package org.eclipse.ve.internal.cde.core;
/*
 *  $RCSfile: IModelAdapterFactory.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:49 $ 
 */

/**
 * Interface for the model related objects factory.
 * There are different objects required by the CDE
 * relating to the model, and not specifically any viewer.
 * This factory return those various adapters. It is used
 * because model objects are usually not IAdaptable, and
 * even if they are, they wouldn't be returning the CDE
 * specific adapters since they aren't needed for the model
 * itself, only the running within the CDE.
 *
 * The model factory, if being used for the current domain,
 * will be stored in the EditDomain using the
 * key MODEL_FACTORY_KEY defined here.
 *
 * A model factory does not need to be used, the customization
 * of CDE that the developer is creating determines if they wish
 * to use it or not.
 *
 * There is a static method on CDEUtilities to retrieve the model factory.
 */

public interface IModelAdapterFactory {

	public static final String MODEL_FACTORY_KEY = "com.ibm.etools.ide.modelfactorykey"; //$NON-NLS-1$

	/**
	 * getAdapter
	 *
	 * Return the requested adapter for the model object, if one is defined for the model object.
	 * CDE defined adapters are:
	 *   IConstraintHandler - used when dropping a child to determine if it is resizeable or not.
	 *   IContainmentHandler - used when dropping a child to determine if the parent is viable.
	 *
	 * Developers may add there own adapters too.
	 * Adapters should be lightweight because they are created and released frequently.
	 * They shouldn't hold onto data other than the model.
	 */
	public Object getAdapter(Object modelObject, Class adapter);	
}
