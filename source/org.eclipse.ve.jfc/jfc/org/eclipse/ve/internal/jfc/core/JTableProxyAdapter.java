package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: JTableProxyAdapter.java,v $
 *  $Revision: 1.3 $  $Date: 2004-02-20 00:43:58 $ 
 */

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.java.core.*;
public class JTableProxyAdapter extends ComponentProxyAdapter {

	protected IMethodProxy fAddColumnMethodProxy;
	protected IMethodProxy fRemoveAllColumnsMethodProxy;
	protected EStructuralFeature sfColumns, sfAutoCreateColumns;

	public JTableProxyAdapter(IBeanProxyDomain domain) {
		super(domain);

		sfColumns = JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain.getEditDomain()), JFCConstants.SF_JTABLE_COLUMNS);
		sfAutoCreateColumns = JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain.getEditDomain()), JFCConstants.SF_JTABLE_AUTOCREATECOLUMNSFROMMODEL);		
	}
	/*
	 * Create the bean proxy. We override because we put a table model there
	 * if not is not present so that preview data can be shown in the columns
	 */
	protected void primInstantiateBeanProxy() {
		super.primInstantiateBeanProxy();
		if (isBeanProxyInstantiated()) {
			// Creating proxy worked.
			// Query the current table model - If it is the Default then replace it with our one
			// That has preview data
			IBeanProxy tableModelProxy = getBeanProxy().getTypeProxy().getMethodProxy("getModel").invokeCatchThrowableExceptions(getBeanProxy()); //$NON-NLS-1$
			if (tableModelProxy.getTypeProxy().getTypeName().equals("javax.swing.table.DefaultTableModel")) { //$NON-NLS-1$
				// Set the table model to be a special one we have on the target VM
				IBeanTypeProxy previewTypeProxy =
					getBeanProxy().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.PreviewTableModel");	//$NON-NLS-1$
				IMethodProxy setModelProxy = getBeanProxy().getTypeProxy().getMethodProxy("setModel", "javax.swing.table.TableModel");	//$NON-NLS-1$ //$NON-NLS-2$
				try {
					setModelProxy.invoke(getBeanProxy(), previewTypeProxy.newInstance());
				} catch (ThrowableProxy exc) {
					JavaVEPlugin.log(exc, Level.WARNING);
				}
			}
		}
	}
	protected void applied(EStructuralFeature as, Object newValue, int position) {

		if (!isBeanProxyInstantiated())
			return;
		else if (as == sfColumns) {
			reapplyColumns();
		} else {
			super.applied(as, newValue, position);
			if (getErrorStatus() != ERROR_SEVERE && as == sfAutoCreateColumns) {
				IBeanProxy newProxy = BeanProxyUtilities.getBeanProxy((IJavaInstance) newValue);
				if (newProxy instanceof IBooleanBeanProxy) {
					if (!((IBooleanBeanProxy) newProxy).booleanValue()) {
						// It has gone false, need to revamp the columns.
						reapplyColumns();
					} else {
						// It has gone true, see if there are columns. Allowed it to be applied, but create a warning.
						if (!((List) getEObject().eGet(sfColumns)).isEmpty()) {
							processError(sfAutoCreateColumns, new IllegalArgumentException(VisualMessages.getString("JTable_ShouldnotSet_EXC_"))); //$NON-NLS-1$
						}
						revalidateBeanProxy();						
					}
				}
			}			
		}

	}
	public void reapplyColumns() {
		removeColumns();
		applyColumns();
		revalidateBeanProxy();
	}
	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {

		if (!isBeanProxyInstantiated())
			return;
		else if (sf == sfColumns) {
			if (!isValidFeature(sfAutoCreateColumns) && ((List) getEObject().eGet(sfColumns)).isEmpty()) {
				// We had made it true, but now that we've removed all columns, we should reinstantiate to see if it goes good.
				clearError(sfAutoCreateColumns);	// Clear it so we retry to apply it.
				reinstantiateBeanProxy();
			} else
				reapplyColumns();
			IBeanProxyHost oldHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) oldValue);
			if (oldHost != null)
				oldHost.releaseBeanProxy();
		} else {
			super.canceled(sf, oldValue, position);
			if (getErrorStatus() != ERROR_SEVERE && sf == sfAutoCreateColumns) {	
				if (!((List) getEObject().eGet(sfColumns)).isEmpty()) {
					processError(sfAutoCreateColumns, new IllegalArgumentException(VisualMessages.getString("JTable_ShouldnotSet_EXC_"))); //$NON-NLS-1$
				}				
				revalidateBeanProxy();	// Because table size/position has changed						
			}
		}

	}
	protected void appliedList(EStructuralFeature as, List newValues, int position, boolean testValidity) {
		if (as == sfColumns)
			reapplyColumns();		
		else {
			super.appliedList(as, newValues, position, testValidity);
		}
	}
	protected void canceledList(EStructuralFeature as, List oldValues, int position) {
		if (as == sfColumns) {
			// This is overridden so that we only need to do remove/apply once for this entire list.
			reapplyColumns();
			Iterator itr = oldValues.iterator();
			while (itr.hasNext()) {
				IBeanProxyHost oldHost = BeanProxyUtilities.getBeanProxyHost((IJavaInstance) itr.next());
				if (oldHost != null)
					oldHost.releaseBeanProxy();
			}

		} else {
			super.canceledList(as, oldValues, position);
		}
	}

	/** 
	 * Iterate over the table columns.  These are added to the live bean with the
	 * method addColumn(javax.swing.TableColumn)
	 */
	protected void applyColumns() {

		boolean apply = getErrorStatus() != IBeanProxyHost.ERROR_SEVERE;
			
		List columns = (List) ((EObject) getTarget()).eGet(sfColumns);
		Iterator iter = columns.iterator();
		while (iter.hasNext()) {
			IJavaObjectInstance column = (IJavaObjectInstance) iter.next();
			IBeanProxyHost columnBeanProxyHost = BeanProxyUtilities.getBeanProxyHost(column);
			columnBeanProxyHost.instantiateBeanProxy();
			if (apply && columnBeanProxyHost.getErrorStatus() != IBeanProxyHost.ERROR_SEVERE)
				getAddColumnMethodProxy().invokeCatchThrowableExceptions(getBeanProxy(), columnBeanProxyHost.getBeanProxy());
		}
	}
	/* 
	 * Go to the JTableManager and remove all of the columns. 
	 */
	protected void removeColumns() {
		if (getErrorStatus() == IBeanProxyHost.ERROR_SEVERE)
			return;
					
		getRemoveAllColumnsMethodProxy().invokeCatchThrowableExceptions(null, getBeanProxy());
	}
	protected IMethodProxy getAddColumnMethodProxy() {
		if (fAddColumnMethodProxy == null) {
			fAddColumnMethodProxy = getBeanProxy().getTypeProxy().getMethodProxy("addColumn", "javax.swing.table.TableColumn");	//$NON-NLS-1$ //$NON-NLS-2$
		}
		return fAddColumnMethodProxy;
	}
	protected IMethodProxy getRemoveAllColumnsMethodProxy() {
		if (fRemoveAllColumnsMethodProxy == null) {
			fRemoveAllColumnsMethodProxy =
				getBeanProxy()
					.getProxyFactoryRegistry()
					.getBeanTypeProxyFactory()
					.getBeanTypeProxy("org.eclipse.ve.internal.jfc.vm.JTableManager")	//$NON-NLS-1$
					.getMethodProxy("removeAllColumns", "javax.swing.JTable");	//$NON-NLS-1$ //$NON-NLS-2$
		}
		return fRemoveAllColumnsMethodProxy;
	}
	/**
	 * @see org.eclipse.ve.internal.java.core.IBeanProxyHost#releaseBeanProxy()
	 */
	public void releaseBeanProxy() {
		super.releaseBeanProxy();
		
		if (fRemoveAllColumnsMethodProxy != null) {
			fRemoveAllColumnsMethodProxy.getProxyFactoryRegistry().releaseProxy(fRemoveAllColumnsMethodProxy);
			fRemoveAllColumnsMethodProxy = null;
		}
		
		if (fAddColumnMethodProxy != null) {
			fAddColumnMethodProxy.getProxyFactoryRegistry().releaseProxy(fAddColumnMethodProxy);
			fAddColumnMethodProxy = null;
		}		
	}

}