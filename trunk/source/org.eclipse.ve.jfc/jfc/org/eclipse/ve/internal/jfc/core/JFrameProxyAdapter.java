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
 *  $RCSfile: JFrameProxyAdapter.java,v $
 *  $Revision: 1.8 $  $Date: 2005-02-15 23:42:05 $ 
 */

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.awt.IDimensionBeanProxy;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.IModelChangeController;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.DimensionJavaClassCellEditor;

/**
 * Proxy adapter for javax.swing.JFrame.
 * Need to setDefaultCloseOperation to WindowConstants.DO_NOTHING_ON_CLOSE 
 * so that the live window can't be closed if the user tries to close it from
 * the task bar or from the window itself.
 */
public class JFrameProxyAdapter extends FrameProxyAdapter {

	/**
	 * Constructor for JFrameProxyAdapter.
	 * @param domain
	 */
	public JFrameProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}

	/*
	 * Create the bean proxy. We override because we want to set the default close operation 
	 * such that it can't be closed from the task bar.
	 */
	protected void primInstantiateBeanProxy() {
		super.primInstantiateBeanProxy();
		if (isBeanProxyInstantiated()) {
			IBeanTypeProxy intType = getBeanProxy().getProxyFactoryRegistry().getBeanTypeProxyFactory().getBeanTypeProxy("int"); //$NON-NLS-1$
			try {
				IIntegerBeanProxy intBeanProxy = (IIntegerBeanProxy) intType.newInstance("javax.swing.JFrame.DO_NOTHING_ON_CLOSE"); //$NON-NLS-1$
				if (intBeanProxy != null) {
					IInvokable setDefaultCloseOperationInvokable = getBeanProxy().getTypeProxy().getInvokable("setDefaultCloseOperation", "int"); //$NON-NLS-1$ //$NON-NLS-2$
					setDefaultCloseOperationInvokable.invokeCatchThrowableExceptions(getBeanProxy(), intBeanProxy);
				}
			} catch (ThrowableProxy e) {
			} catch (InstantiationException e) {
			}
		}
	}
	/**
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#applied(EStructuralFeature, Object, int)
	 */
	protected void applied(EStructuralFeature sf, Object newValue, int position) {
		// We never apply defaultCloseOperation, we override to always be do nothing on close.
		if (!("defaultCloseOperation".equals(sf.getName()))) //$NON-NLS-1$
			super.applied(sf, newValue, position);
	}

	/**
	 * @see org.eclipse.ve.internal.jfc.core.ComponentProxyAdapter#appliedSize(EStructuralFeature, Object, int)
	 */
	protected void appliedSize(final EStructuralFeature as, Object newValue, int position) {
		IDimensionBeanProxy dim = (IDimensionBeanProxy) BeanProxyUtilities.getBeanProxy((IJavaInstance) newValue);
		if (dim != null && (dim.getWidth() == -1 || dim.getHeight() == -1)) {
			// If the either width or height is -1, then we need to adjust to preferred size.
			// This will do an actual set of the setting in the EMF object. This is OK because
			// -1 is not a valid setting anyway, and doing an undo followed by redo will come back
			// with -1, and then at this point in time it would reconstruct the setting.
			Dimension size = new Dimension(dim.getWidth(), dim.getHeight());
			if (NullLayoutEditPolicy.adjustForPreferredSize(getBeanProxy(), size)) {
				String initString = DimensionJavaClassCellEditor.getJavaInitializationString(size.width, size.height, JFCConstants.DIMENSION_CLASS_NAME);
				final IJavaInstance dimensionBean = BeanUtilities.createJavaObject("java.awt.Dimension", ((EObject) target).eResource().getResourceSet(), initString); //$NON-NLS-1$
				Display.getDefault().asyncExec(new Runnable() {
					/**
					 * @see java.lang.Runnable#run()
					 */
					public void run() {
						// We may not be within the context of a change control, so we need to get a controller to handle the change.
						IModelChangeController controller =
							(IModelChangeController) getBeanProxyDomain().getEditDomain().getData(
								IModelChangeController.MODEL_CHANGE_CONTROLLER_KEY);
						controller.run(new Runnable() {
							public void run() {
								// Set the constraints on the component bean.  This will change the size of the component
								// Because we will be called back with notify and apply the constraints rectangle to the live bean
								//
								// Note: Need to use RuledCommandBuilder.
								RuledCommandBuilder cbld = new RuledCommandBuilder(getBeanProxyDomain().getEditDomain());
								cbld.applyAttributeSetting((EObject) target, as, dimensionBean);
								cbld.getCommand().execute();

							}
						}, true);
					}
				});
				return; // Let the notify back from the set here do the actual apply.
			}
		}

		super.appliedSize(as, newValue, position);
	}

	/**
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#canceled(EStructuralFeature, Object, int)
	 */
	protected void canceled(EStructuralFeature sf, Object oldValue, int position) {
		// We never apply defaultCloseOperation, we override to always be do nothing on close.
		if (!("defaultCloseOperation".equals(sf.getName()))) //$NON-NLS-1$
			super.canceled(sf, oldValue, position);
	}

}
