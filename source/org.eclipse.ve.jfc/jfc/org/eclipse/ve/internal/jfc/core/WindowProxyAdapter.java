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
 *  $RCSfile: WindowProxyAdapter.java,v $
 *  $Revision: 1.11 $  $Date: 2005-03-18 18:27:48 $ 
 */

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.awt.IDimensionBeanProxy;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
import org.eclipse.ve.internal.java.visual.DimensionJavaClassCellEditor;
/**
 * Windows need explicit disposing when the proxy is disposed
 */
public class WindowProxyAdapter extends ContainerProxyAdapter {

	public WindowProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	/**
	 * Windows need to be positioned off screen and also made visible
	 * even when the MOF model says they are mot
	 */
	public IBeanProxy instantiateOnFreeForm(IBeanProxy aFreeFormDialogHost) {

		// Need to make sure location/visibility are good before we instantiate so that it won't flash on the screen.
		applyLocation(false, BeanAwtUtilities.getOffScreenLocation());
		applyVisibility(false, Boolean.TRUE);

		if (!isBeanProxyInstantiated() && getErrorStatus() != ERROR_SEVERE)
			instantiateBeanProxy();
		// If not already instantiated, and not errors, try again. If already instantiated w/severe, don't waste time

		if (getErrorStatus() == ERROR_SEVERE)
			return null; // It is bad, don't go on.

		// Having done this the frame on the target VM will now possibly be visible
		// We should attempt to restore focus to the IDE
		// Update:	Following lines were commented out becuase this code could be executed 
		// 			in the background thread, which would cause a NPE
		// TODO: Check to see if a window really causes a loss of focus in other systems (Ex:linux)
//		if (Display.getCurrent().getActiveShell() != null)
//			Display.getCurrent().getActiveShell().setFocus();
//			// Force the focus back to the editor part so that knows it has re-gained focus
//			getBeanProxyDomain().getEditDomain().getEditorPart().setFocus();

		return getBeanProxy();

	}
	/**
	 * releaseBeanProxy: Get rid of the bean proxy being held.
	 * This allows for recreation if needed.
	 * java.awt.Window objects (e.g. Frame) require an explicit
	 * dispose to be called to get rid of it. Will only do it
	 * if we own it.
	 */
	public void releaseBeanProxy() {
		if (fOwnsProxy && isBeanProxyInstantiated()) {
			// Invoke a method to dispose of the window.
			BeanAwtUtilities.invoke_dispose(getBeanProxy());
		};
		super.releaseBeanProxy();
	}

	public void disposeOnFreeForm(IBeanProxy aFreeFormDialogHost) {
		if (fOwnsProxy && isBeanProxyInstantiated()) {
			// Invoke a method to dispose of the window.
			BeanAwtUtilities.invoke_dispose(getBeanProxy());
		};
	}

	/**
	 * @see org.eclipse.ve.internal.jfc.core.IComponentProxyHost#childValidated(IComponentProxyHost)
	 */
	public void childValidated(IComponentProxyHost childProxy) {
		super.childValidated(childProxy);
		// See if we are not explicitly setting size. If we aren't, then
		// we need to do a pack.
		EObject rtarget = getEObject();
		if (isBeanProxyInstantiated() && !rtarget.eIsSet(sfComponentBounds) && !rtarget.eIsSet(sfComponentSize))
			BeanAwtUtilities.invoke_window_pack(getBeanProxy());
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
				final IJavaInstance dimensionBean = BeanUtilities.createJavaObject(JFCConstants.DIMENSION_CLASS_NAME, ((EObject) target).eResource().getResourceSet(), initString);
				Display.getDefault().asyncExec(new Runnable() {
					/**
					 * @see java.lang.Runnable#run()
					 */
					public void run() {
						// We may not be within the context of a change control, so we need to get a controller to handle the change.
						getModelChangeController().doModelChanges(new Runnable() {
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

}
