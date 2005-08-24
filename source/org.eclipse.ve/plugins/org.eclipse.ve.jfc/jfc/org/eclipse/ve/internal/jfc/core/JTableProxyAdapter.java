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
package org.eclipse.ve.internal.jfc.core;
/*
 *  $RCSfile: JTableProxyAdapter.java,v $
 *  $Revision: 1.15 $  $Date: 2005-08-24 23:38:09 $ 
 */

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.jem.internal.instantiation.base.*;
import org.eclipse.jem.internal.proxy.core.*;
import org.eclipse.jem.internal.proxy.core.ExpressionProxy.ProxyEvent;
import org.eclipse.jem.internal.proxy.initParser.tree.ForExpression;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.core.IAllocationProcesser.AllocationException;
/**
 * JTable proxy adapter.
 * 
 * @since 1.1.0
 */
public class JTableProxyAdapter extends ComponentProxyAdapter {

	protected EStructuralFeature sfColumns, sfAutoCreateColumns;

	public JTableProxyAdapter(IBeanProxyDomain domain) {
		super(domain);

		sfColumns = JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain.getEditDomain()), JFCConstants.SF_JTABLE_COLUMNS);
		sfAutoCreateColumns = JavaInstantiation.getSFeature(JavaEditDomainHelper.getResourceSet(domain.getEditDomain()), JFCConstants.SF_JTABLE_AUTOCREATECOLUMNSFROMMODEL);		
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#instantiateAndInitialize(org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected void instantiateAndInitialize(IExpression expression) throws IllegalStateException, AllocationException {
		super.instantiateAndInitialize(expression);
		// Now call the JTable manager to complete the initialization. This guy handles the default model and puts our preview
		// model in instead.
		if (getProxy() != null && expression.isValid()) {
			//   try {
			//     JTableManager.initializeTableModel(jtable);
			expression.createTry();
			try {
				expression.createSimpleMethodInvoke(BeanAwtUtilities.getJTableInitializeTableModel(expression), null, new IProxy[] { getProxy()},
						false);
				//   } catch (Exception e) {
				//     ... send back thru ExpressionProxy to mark an instantiation error ...
				//     throw new BeanInstantiationError(); ... so that when being applied as a setting it can be seen as not valid, but rest of expression can continue.
				//   }				
				ExpressionProxy expProxy = expression.createTryCatchClause(getBeanTypeProxy("java.lang.Exception", expression), true); //$NON-NLS-1$
				expProxy.addProxyListener(new ExpressionProxy.ProxyAdapter() {

					public void proxyResolved(ProxyEvent event) {
						ThrowableProxy throwableProxy = (ThrowableProxy) event.getProxy();
						JavaVEPlugin.log(throwableProxy, Level.INFO);
						processInstantiationError(new BeanExceptionError(throwableProxy, ERROR_SEVERE));
					}
				});
				expression.createThrow();
				expression.createClassInstanceCreation(ForExpression.THROW_OPERAND, getBeanTypeProxy(
						"org.eclipse.ve.internal.java.remotevm.BeanInstantiationException", expression), 0); //$NON-NLS-1$
			} finally {
				if (expression.isValid())
					expression.createTryEnd();
			}
		}
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applySetting(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int, org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected void applySetting(EStructuralFeature feature, final Object value, final int index, IExpression expression) {
		if (feature == sfColumns)
			addColumn((IJavaInstance) value, index, expression);
		else if (feature == sfAutoCreateColumns) {
			super.applySetting(feature, value, index, expression);
			// Now need to do some other stuff to handle autocreate, in case explicit columns too.
			IInternalBeanProxyHost autocreate = getSettingBeanProxyHost((IJavaInstance) value);
			if (autocreate.getProxy() != null) {
				IProxy autocreateProxy = autocreate.getProxy();
				expression.createIfElse(!inInstantiation());
				expression.createProxyExpression(ForExpression.IF_CONDITION, autocreateProxy);
				ExpressionProxy inTrue = expression.createProxyAssignmentExpression(ForExpression.IF_TRUE);
				// Bit of kludge but we want to know it is True, but we want to execute on the IDE. We need an expression proxy as a handle to know this.
				expression.createPrimitiveLiteral(ForExpression.ASSIGNMENT_RIGHT, true);
				inTrue.addProxyListener(new ExpressionProxy.ProxyAdapter() {
					public void proxyResolved(ProxyEvent event) {
						// It has gone true, see if there are columns. Allowed it to be applied, but create a warning.
						if (!((List) getEObject().eGet(sfColumns)).isEmpty()) {
							processPropertyError(new IllegalArgumentException(JFCMessages.JTable_ShouldnotSet_EXC_), sfAutoCreateColumns, value); 
						}						
					}
				});
				if (!inInstantiation()) {
					// Now the false condition. We went false, so we need to reapply the columns to clean up what was there and put the correct set back.
					// This only needs to be done when not in instantiation.
					expression.createBlockBegin();
					reapplyColumns(expression);
					expression.createBlockEnd();
				}
			}
		} else
			super.applySetting(feature, value, index, expression);
	}
	
	/**
	 * Add the colunm at the given index.
	 * @param value
	 * @param index
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	protected void addColumn(IJavaInstance column, int index, IExpression expression) {
		IInternalBeanProxyHost columnProxyHost = getSettingBeanProxyHost(column);
		IProxy columnProxy = instantiateSettingBean(columnProxyHost, expression, sfColumns, column);
		if (columnProxy == null)
			return; // It failed creation, don't go any further.

		IProxy beforeBeanProxy; // The beanproxy to go before, if any.
		if (index != Notification.NO_INDEX) {
			// Need to do +1 because we (columnBeanProxy) are already at that position in the EMF list. So we want to go before next guy.
			beforeBeanProxy = getProxyAt(index + 1, sfColumns); 
		} else
			beforeBeanProxy = null;

		expression.createSimpleMethodInvoke(BeanAwtUtilities.getJTableAddColumnBefore(expression), null,
				new IProxy[] { getProxy(), columnProxy, beforeBeanProxy}, false);
		revalidateBeanProxy();
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applied(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int, boolean, org.eclipse.jem.internal.proxy.core.IExpression, boolean)
	 */
	protected void applied(EStructuralFeature feature, Object value, int index, boolean isTouch, IExpression expression, boolean testValidity) {
		if ((feature == sfColumns || feature == sfAutoCreateColumns) && isTouch)
			return;	// Don't need to apply if touch on columns or autocreate columns. It didn't change.
		
		super.applied(feature, value, index, isTouch, expression, testValidity);
	}
		
	private void reapplyColumns(IExpression expression) {
		if (inInstantiation())
			return;	// Can't do this while instantiating. May not yet have columns.
		removeColumns(expression);
		applyColumns(expression);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.jfc.core.ComponentProxyAdapter#cancelSetting(org.eclipse.emf.ecore.EStructuralFeature, java.lang.Object, int, org.eclipse.jem.internal.proxy.core.IExpression)
	 */
	protected void cancelSetting(EStructuralFeature feature, Object oldValue, int index, IExpression expression) {
		if (feature == sfColumns) {
			IInternalBeanProxyHost columnHost = getSettingBeanProxyHost((IJavaInstance) oldValue);
			expression.createSimpleMethodInvoke(BeanAwtUtilities.getJTableRemoveColumn(expression), getProxy(), new IProxy[] {columnHost.getProxy()}, false);
			if (hasErrorsOfKey(sfAutoCreateColumns) && ((List) getEObject().eGet(sfColumns)).isEmpty()) {
				// We had autocreate as true and we had columns. Now we do not, so we can clear the error and reapply the autocreate columns.
				clearError(sfAutoCreateColumns);
				applySetting(sfAutoCreateColumns, getEObject().eGet(sfAutoCreateColumns), Notification.NO_INDEX, expression);
			}
		} else if (feature == sfAutoCreateColumns) {
			super.cancelSetting(feature, oldValue, index, expression);
			// We canceled auto create. That puts is back to the default of true. So we need to create an warning if we have columns.
			if (!((List) getEObject().eGet(sfColumns)).isEmpty()) {
				processPropertyError(new IllegalArgumentException(JFCMessages.JTable_ShouldnotSet_EXC_), sfAutoCreateColumns, null); 
			}							
		} else
			super.cancelSetting(feature, oldValue, index, expression);
	}

	private void applyColumns(IExpression expression) {
		List columns = (List) ((EObject) getTarget()).eGet(sfColumns);
		Iterator iter = columns.iterator();
		int index = -1;
		while (iter.hasNext()) {
			IJavaObjectInstance column = (IJavaObjectInstance) iter.next();
			IInternalBeanProxyHost columnBeanProxyHost = getSettingBeanProxyHost(column);
			if (columnBeanProxyHost.getProxy() == null)
				continue;	// Don't apply. It has an error already.
			addColumn(column, ++index, expression);
		}
	}
	
	/**
	 * Remove all columns
	 * @param expression
	 * 
	 * @since 1.1.0
	 */
	private void removeColumns(IExpression expression) {
		expression.createSimpleMethodInvoke(BeanAwtUtilities.getJTableRemoveAllColumns(expression), null, new IProxy[] {getProxy()}, false);
	}
	
}
