package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JTextComponentProxyAdapter.java,v $
 *  $Revision: 1.3 $  $Date: 2005-04-05 21:53:36 $ 
 */
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.IBeanProxyDomain;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.jfc.core.ComponentProxyAdapter;

/*
 * Created on Jun 23, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

/**
 * @author pwalker
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class JTextComponentProxyAdapter extends ComponentProxyAdapter {
	protected EStructuralFeature sfSelectionStart, sfSelectionEnd;

	/**
	 * @param domain
	 */
	public JTextComponentProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(domain.getEditDomain());
		sfSelectionStart = JavaInstantiation.getSFeature(rset, URI.createURI("java:/javax.swing.text#JTextComponent/selectionStart")); //$NON-NLS-1$
		sfSelectionEnd = JavaInstantiation.getSFeature(rset, URI.createURI("java:/javax.swing.text#JTextComponent/selectionEnd")); //$NON-NLS-1$
	}
	/* (non-Javadoc)
	 * Need to reinstanciate the bean when the selectionStart or selectionEnd is reset
	 */
	protected void canceled(EStructuralFeature as, Object oldValue, int position) {
		if ((as == sfSelectionStart || as == sfSelectionEnd) && !inInstantiation())
			throw new ReinstantiationNeeded();
		super.canceled(as, oldValue, position);
	}

}
