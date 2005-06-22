/*****************************************************************************************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others. All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************************************************************************************/
/*
 *  $RCSfile: TableColumnProxyAdapter.java,v $
 *  $Revision: 1.12 $  $Date: 2005-06-22 21:05:25 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.ResourceSet;

import org.eclipse.jem.internal.beaninfo.PropertyDecorator;
import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.emf.InverseMaintenanceAdapter;

import org.eclipse.ve.internal.java.core.*;

/**
 * Proxy Adapter for swing TableColumn.
 * 
 * @since 1.0.0
 */
public class TableColumnProxyAdapter extends BeanProxyAdapter {

	protected EStructuralFeature sfModelIndex, sfHeaderValue;

	protected EReference sfTableColumns;

	public TableColumnProxyAdapter(IBeanProxyDomain aDomain) {
		super(aDomain);
		ResourceSet rset = JavaEditDomainHelper.getResourceSet(getBeanProxyDomain().getEditDomain());
		sfModelIndex = JavaInstantiation.getSFeature(rset, JFCConstants.SF_TABLECOLUMN_MODELINDEX);
		sfHeaderValue = JavaInstantiation.getSFeature(rset, JFCConstants.SF_TABLECOLUMN_HEADERVALUE);
		sfTableColumns = JavaInstantiation.getReference(rset, JFCConstants.SF_JTABLE_COLUMNS);

	}
	
	public void revalidateBeanProxy() {
		//  When we invalidate we need to revalidate the JTable as well to cause the image to refresh.
		EObject table = InverseMaintenanceAdapter.getFirstReferencedBy(getTarget(), sfTableColumns);
		// If we are on the freeform then container will not be an instance of table
		if (table != null) {
			IBeanProxyHost tableProxyHost = getSettingBeanProxyHost((IJavaInstance) table);
			// The table is a Component so this should refresh the shell and the image
			tableProxyHost.revalidateBeanProxy();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applyBeanProperty(org.eclipse.jem.internal.beaninfo.PropertyDecorator,
	 *      org.eclipse.jem.internal.proxy.core.IProxy, org.eclipse.jem.internal.proxy.core.IExpression, boolean)
	 */
	protected IProxy applyBeanProperty(PropertyDecorator propertyDecorator, IProxy settingProxy, IExpression expression, boolean getOriginalValue)
			throws NoSuchMethodException, NoSuchFieldException {
		IProxy result = super.applyBeanProperty(propertyDecorator, settingProxy, expression, getOriginalValue);
		if (!inInstantiation() && propertyDecorator.getEModelElement() == sfModelIndex && !getEObject().eIsSet(sfHeaderValue)) {
			// We changed the model index, need to reset the header value to the default since we've changed the index and have no specific header
			// value.
			// If we don't do the reset it won't reflect the changed index in the headers.
			IJavaObjectInstance table = (IJavaObjectInstance) InverseMaintenanceAdapter.getFirstReferencedBy(getEObject(), sfTableColumns);
			// Not sure if this would occur - perhaps we could be on the free form although this
			// is unlikely
			if (table != null) {
				IInternalBeanProxyHost tableProxyHost = getSettingBeanProxyHost(table);
				if (tableProxyHost.getProxy() != null) {
					IProxyMethod resetHeader = getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.JTableManager", expression).getMethodProxy(expression, "resetHeaderValue", //$NON-NLS-1$ //$NON-NLS-2$
							new String[] { "javax.swing.JTable", "javax.swing.table.TableColumn"}); //$NON-NLS-1$ //$NON-NLS-2$
					expression.createSimpleMethodInvoke(resetHeader, null, new IProxy[] { tableProxyHost.getProxy(), getProxy()}, false);
				}
			}
		}
		return result;
	}
}