package org.eclipse.ve.internal.jfc.core;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: CompositionFreeFormComponentsEditPolicy.java,v $
 *  $Revision: 1.3 $  $Date: 2003-11-12 18:57:07 $ 
 */
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.editpolicies.AbstractEditPolicy;
import org.eclipse.ui.IWorkbenchPage;

import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.java.core.CompositionProxyAdapter;
import org.eclipse.ve.internal.java.core.JavaEditDomainHelper;
import org.eclipse.ve.internal.jcm.BeanSubclassComposition;

/**
 * Composition Graphical EditPolicy for a Bean subclass in a visual environment.
 * Creation date: (06/22/00 4:56:28 PM)
 * @author: Joe Winchester
 */
public class CompositionFreeFormComponentsEditPolicy extends AbstractEditPolicy {

	protected FreeFormComponentsHostAdapter fFreeFormHost;
	protected IWorkbenchPage fWorkbenchPage;

	public CompositionFreeFormComponentsEditPolicy() {
	}

	public void activate() {
		fWorkbenchPage = EditDomain.getEditDomain(getHost()).getEditorPart().getSite().getPage();
		// Make sure that the proxy host for the frame that hosts free form components is instantiated
		activateFreeFormEnvironment();
		super.activate();
	}

	protected void activateFreeFormEnvironment() {
		BeanSubclassComposition composition = (BeanSubclassComposition) getHost().getModel();
		if (composition == null)
			return;
		CompositionProxyAdapter compositionAdapter =
			(CompositionProxyAdapter) EcoreUtil.getExistingAdapter(
				composition,
				CompositionProxyAdapter.BEAN_COMPOSITION_PROXY);
		fFreeFormHost =
			(FreeFormComponentsHostAdapter) EcoreUtil.getExistingAdapter(
				compositionAdapter,
				FreeFormComponentsHostAdapter.FREE_FORM_COMPONENTS_HOST);
		// There is no factory to create this object we create it by hand if required
		if (fFreeFormHost == null) {
			fFreeFormHost =
				new FreeFormComponentsHostAdapter(
					JavaEditDomainHelper.getBeanProxyDomain(EditDomain.getEditDomain(getHost())),
					composition,
					fWorkbenchPage);
			compositionAdapter.eAdapters().add(fFreeFormHost);
			fFreeFormHost.setTarget(compositionAdapter);
		}
		fFreeFormHost.instantiateDialogs();
	}

	/**
	 * deactivate: Clean up and dispose the dialog bean proxy for free form host.
	 */
	public void deactivate() {
		super.deactivate();
		if (fFreeFormHost != null) {
			fFreeFormHost.disposeDialogs();
			fFreeFormHost = null;
		}
	}
}
