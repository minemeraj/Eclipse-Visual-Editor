/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: FrameProxyAdapter.java,v $
 *  $Revision: 1.1 $  $Date: 2004-09-09 16:15:14 $ 
 */
package org.eclipse.ve.internal.jfc.core;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.instantiation.base.*;

import org.eclipse.ve.internal.cdm.Annotation;

import org.eclipse.ve.internal.cde.core.AnnotationLinkagePolicy;
import org.eclipse.ve.internal.cde.properties.NameInCompositionPropertyDescriptor;

import org.eclipse.ve.internal.java.core.*;
import org.eclipse.ve.internal.java.rules.RuledCommandBuilder;
 

/**
 * Frame Proxy Adapter.
 * <p>
 * TODO This shouldn't be necessary. The only reason we have it is to put a default title on the 
 * frame. But there are two problems with doing it here. One is that it is done outside of the drop
 * so it adds it outside of the undo of the create. So after dropping a frame, if someone then did an undo, 
 * it would simply undo the change title and not undo the drop of the frame. The second problem is that 
 * if someone removed the title, the next time it comes up it will put the title back and the class will
 * be marked as changed and save would be needed. This means you can never "not" have a title.
 * The better way is to have it done in the creation policy when dropped, but allow it to be unset later.
 * @since 1.0.0
 */
public class FrameProxyAdapter extends WindowProxyAdapter {

	/**
	 * @param domain
	 * 
	 * @since 1.0.0
	 */
	public FrameProxyAdapter(IBeanProxyDomain domain) {
		super(domain);
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.core.BeanProxyAdapter#primInstantiateBeanProxy()
	 */
	protected void primInstantiateBeanProxy() {
		super.primInstantiateBeanProxy();
		if (isBeanProxyInstantiated()) {
			IJavaObjectInstance frame = getJavaObject();
			EReference sf = JavaInstantiation.getReference(frame,JFCConstants.SF_FRAME_TITLE);
			if (!frame.eIsSet(sf)) {
				ResourceSet rset = JavaEditDomainHelper.getResourceSet(getBeanProxyDomain().getEditDomain());
				AnnotationLinkagePolicy policy = getBeanProxyDomain().getEditDomain().getAnnotationLinkagePolicy();
				Annotation ann = policy.getAnnotation(frame);
				String name = null;
				if (ann != null) {
					name = (String) ann.getKeyedValues().get(NameInCompositionPropertyDescriptor.NAME_IN_COMPOSITION_KEY);
				}
				if(name!=null){
					IJavaInstance titleInstance = BeanUtilities.createString(rset, name);
					final RuledCommandBuilder cbld = new RuledCommandBuilder(getBeanProxyDomain().getEditDomain());
					cbld.applyAttributeSetting((EObject) target, sf, titleInstance);
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							// Check to see if still active since this was spawned off.
							if (isBeanProxyInstantiated())
								getBeanProxyDomain().getEditDomain().getCommandStack().execute(cbld.getCommand());
						}
					});
				}
			}
		}

	}

}
