/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IAdapterContributor.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:30:47 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;
 

/**
 * The extension point "org.eclipse.ve.java.core.editorpartadapters" defines instances of IAdapterContributor
 * These allow plugins to contribute code to the visual editor, so that when it is asked for an adapter (typically done by viewers)
 * the adapterContributor is able to respond
 * The contributor has a lifecycle that gives it knowledge of the edit domain as well as when to dispose any local resources
 * @since 1.0.0
 */
public interface IAdapterContributor {
	
	public Class getAdapterKey();

}
