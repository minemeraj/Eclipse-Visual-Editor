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
 *  $RCSfile: IInternalBeanProxyHost2.java,v $
 *  $Revision: 1.1 $  $Date: 2005-05-11 19:01:20 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.proxy.core.IExpression;
import org.eclipse.jem.internal.proxy.core.IProxy;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;

 

/**
 * This is an internal internal to be used only by the ones that really need it.
 * They are delicate and can break the bean proxy host if not used correctly.
 * <p>
 * However it will only be available on those that implement IBeanProxyHost2.
 * 
 * @since 1.1.0
 */
public interface IInternalBeanProxyHost2 extends IInternalBeanProxyHost, IBeanProxyHost2 {
	
	/**
	 * Get the property value as a expression proxy. It is here only for implicit allocations. It needs to get the value at instantiation through
	 * an expression.
	 * @param aBeanPropertyFeature
	 * @param expression
	 * @param forExpression
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public IProxy getBeanPropertyProxyValue(EStructuralFeature aBeanPropertyFeature, IExpression expression, ForExpression forExpression);
	
	/**
	 * Return the proxy for the bean proxy. It could be either an IBeanProxy if already instantiated or an ExpressionProxy if in instantiation.
	 * It is here for cross-package, but basically internal usage.
	 * 
	 * @return the proxy or <code>null</code> if not instantiated or being instantiated.
	 * 
	 * @since 1.1.0
	 */
	public IProxy getProxy();

	/**
	 * Return whether in instantiation or not.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public boolean inInstantiation();
	
	/**
	 * Answers whether there were any instantiation errors or not.
	 * @return
	 * 
	 * @since 1.1.0
	 */
	public boolean hasInstantiationErrors();

	/**
	 * Being added to the FreeForm. The bean should save this indication and do what it needs to do on the next instantiation
	 * to be on the freeform. 
	 * <p>
	 * This is called whenever the bean is being added to the freeform (either "components" or "thisPart").
	 * At the time it is called the bean should of been released. Before being called the bean will be released. And then after the
	 * call the bean will be instantiated. 
	 * 
	 * @param compositionAdapter the proxy adapter for the composition. This allows them to get a hold of the appropriate freeform hosts.
	 * 
	 * @since 1.1.0
	 */
	public void addToFreeForm(CompositionProxyAdapter compositionAdapter);
	
	/**
	 * Being removed from the FreeForm. The bean should remove the indication. The bean will alwayb have been released before the remove is
	 * called. 
	 * 
	 * @see IInternalBeanProxyHost2#addToFreeForm(CompositionProxyAdapter)
	 * @since 1.1.0
	 */
	public void removeFromFreeForm();
}
