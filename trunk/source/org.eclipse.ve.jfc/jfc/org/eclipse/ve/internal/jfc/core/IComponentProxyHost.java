package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IComponentProxyHost.java,v $
 *  $Revision: 1.2 $  $Date: 2005-02-15 23:42:05 $ 
 */

import org.eclipse.ve.internal.java.core.IBeanProxyHost;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
/**
 * Interface for a proxy host that is a component
 */
public interface IComponentProxyHost  {
/**
 * Dispost the bean proxy giving it the dialog that is used to host
 * components for the free form
 */
void disposeOnFreeForm(IBeanProxy freeFormDialog);
/**
 * Return the parent component proxy adaptor
 */
IComponentProxyHost getParentComponentProxyHost();
/**
 * Return a bean proxy to the component
 */
IBeanProxy getVisualComponentBeanProxy();
/**
 * Answer whether or not we have image listeners attached to the component image
 */
boolean hasImageListeners();
/**
 * Instantiate the bean proxy giving it the dialog that is used to host
 * components for the free form
 */
IBeanProxy instantiateOnFreeForm(IBeanProxy freeFormDialog);
/**
 * Reinstantiate the child proxy.
 */
public void reinstantiateChild(IBeanProxyHost aChildProxy);
/**
 * Invalidate any image we may have on behalf of the component
 */
void invalidateImage();
/**
 * Refresh the image
 */
void refreshImage();

/**
 * child has been invalidated. Called when a direct has been invalidated.
 * Implementor may decide whether to pass on up to parent or not.
 */
void childInvalidated(IComponentProxyHost childProxy);

/**
 * child has been validated. Called when a direct has been validated.
 * Implementor may decide whether to pass on up to parent or not.
 */
void childValidated(IComponentProxyHost childProxy);

/**
 * Set the parent component proxy adaptor
 */
void setParentComponentProxyHost(IComponentProxyHost aParentComponent);

/**
 * Apply null layout constraints. This is called when added to a parent
 * so it should now apply the null layout constraints, i.e. bounds/size/location.
 */
void applyNullLayoutConstraints();
}