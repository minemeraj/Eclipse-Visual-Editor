package org.eclipse.jem.internal.proxy.swt;
/*
 * Licensed Material - Property of IBM 
 * (C) Copyright IBM Corp. 2001, 2002 - All Rights Reserved. 
 * US Government Users Restricted Rights - Use, duplication or disclosure 
 * restricted by GSA ADP Schedule Contract with IBM Corp. 
 */
import org.eclipse.jem.internal.proxy.awt.*;
import org.eclipse.jem.internal.proxy.core.*;
/**
 * The Standard awt bean proxy factory.
 * This is the Interface that the desktop will talk
 * to.
 * Creation date: (12/3/99 11:52:09 AM)
 * @author: Joe Winchester
 */
public interface IStandardSWTBeanProxyFactory extends IBeanProxyFactory {
	public static final String REGISTRY_KEY = "standard-java.SWT"; //$NON-NLS-1$

/**
 * Return a new bean proxy for the point argument
 * Creation date: (12/3/99 11:52:20 AM)
 * @author Joe Winchester
 */
public IPointBeanProxy createPointBeanProxyWith(int x, int y);
/**
 * Return a new bean proxy for the rectangle argument
 * Creation date: (12/3/99 11:52:20 AM)
 * @author Joe Winchester
 */
public IRectangleBeanProxy createBeanProxyWith(int x, int y, int width, int height);
}
