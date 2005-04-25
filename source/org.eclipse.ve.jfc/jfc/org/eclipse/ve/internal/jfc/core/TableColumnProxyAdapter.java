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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: TableColumnProxyAdapter.java,v $
 *  $Revision: 1.8 $  $Date: 2005-04-25 16:09:11 $ 
 */

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;
import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;
import org.eclipse.jem.internal.instantiation.base.JavaInstantiation;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IMethodProxy;

public class TableColumnProxyAdapter extends BeanProxyAdapter {

	
	protected EStructuralFeature sfModelIndex, sfHeaderValue, sfPreferredWidth;
	protected EReference sfTableColumns;
	
	public TableColumnProxyAdapter(IBeanProxyDomain aDomain) {
		super(aDomain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(getBeanProxyDomain().getEditDomain());
		sfModelIndex = JavaInstantiation.getSFeature(rset, JFCConstants.SF_TABLECOLUMN_MODELINDEX);		
		sfHeaderValue = JavaInstantiation.getSFeature(rset, JFCConstants.SF_TABLECOLUMN_HEADERVALUE);
		sfPreferredWidth = JavaInstantiation.getReference(rset, JFCConstants.SF_TABLECOLUMN_PREFERREDWIDTH);
		sfTableColumns = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABLE_COLUMNS);
		
	}
	
	/**
	 * When we validate we need to revalidate the JTable as well to cause the image to
	 * refresh.
	 */
	public void validateBeanProxy() {
		EObject table = InverseMaintenanceAdapter.getFirstReferencedBy(getTarget(),sfTableColumns);
		// If we are on the freeform then container will not be an instance of table
		if (table != null) {
			IBeanProxyHost tableProxyHost = BeanProxyUtilities.getBeanProxyHost((IJavaObjectInstance) table);
			// The table is a Component so this should refresh the shell and the image
			tableProxyHost.revalidateBeanProxy();
		}
	
	}
	/**
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applied(EStructuralFeature, Object, int)
	 */
	protected void applied(EStructuralFeature sf, Object newValue, int position) {
		super.applied(sf, newValue, position);	
		if (sf == sfPreferredWidth) {
			// The width of the column changed, so update the table's image
			validateBeanProxy();
		} else {
			modelIndexChanged(sf);
		}
	}

	protected void modelIndexChanged(EStructuralFeature sf) {
		if (sf == sfModelIndex && getErrorStatus() != ERROR_SEVERE && !getEObject().eIsSet(sfHeaderValue)) {
			IJavaObjectInstance table = (IJavaObjectInstance) InverseMaintenanceAdapter.getFirstReferencedBy((EObject) getTarget(), sfTableColumns);
			// Not sure if this would occur - perhaps we could be on the free form although this
			// is unlikely
			if (table != null) {
				IBeanProxyHost tableProxyHost = BeanProxyUtilities.getBeanProxyHost(table);
				if (tableProxyHost.getErrorStatus() != ERROR_SEVERE) {		
					// Ask the JTableManager to reset the header value to the default since we've changed the index and have no header value set.
					getResetHeaderValueProxy().invokeCatchThrowableExceptions(null, new IBeanProxy[] {tableProxyHost.getBeanProxy(), getBeanProxy()});
					tableProxyHost.revalidateBeanProxy();
				}
			}
		}
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#canceled(EStructuralFeature, Object, int)
	 */
	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {
		super.canceled(sf, oldValue, position);
		
		modelIndexChanged(sf);
	}
	
	protected IMethodProxy getResetHeaderValueProxy() {
		return getBeanProxy()
				.getProxyFactoryRegistry()
				.getBeanTypeProxyFactory()
				.getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.JTableManager")	//$NON-NLS-1$
				.getMethodProxy("resetHeaderValue", new String[] {"javax.swing.JTable", "javax.swing.table.TableColumn"});	//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}	

	/**
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#releaseBeanProxy()
	 */
	public void releaseBeanProxy() {
		super.releaseBeanProxy();
	}

}
