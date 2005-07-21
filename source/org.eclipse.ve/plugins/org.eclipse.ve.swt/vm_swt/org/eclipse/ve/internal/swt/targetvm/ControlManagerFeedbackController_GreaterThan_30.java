/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ControlManagerFeedbackController_GreaterThan_30.java,v $
 *  $Revision: 1.2 $  $Date: 2005-07-21 19:37:57 $ 
 */
package org.eclipse.ve.internal.swt.targetvm;

import java.util.*;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.swt.widgets.*;

import org.eclipse.ve.internal.swt.targetvm.ControlManager.ControlManagerFeedbackController;
 

/**
 * This is the version for SWT 3.1 or greater.
 * @since 1.1.0
 */
public class ControlManagerFeedbackController_GreaterThan_30 extends ControlManagerFeedbackController {

	/**
	 * Construct with environment.
	 * @param environment
	 * 
	 * @since 1.1.0
	 */
	public ControlManagerFeedbackController_GreaterThan_30(Environment environment) {
		super(environment);
	}

	
	protected Collection validateControls(Map invalidControls) {
		// This will be the set of controls and parents of the controls, up to the shells, that are invalid and need a new image.
		Set invalidatedControls = new HashSet();
		Shell freeformShell = environment.getFreeFormHost();
		Iterator shellEntries = invalidControls.entrySet().iterator();
		while (shellEntries.hasNext()) {
			Map.Entry shellEntry = (Entry) shellEntries.next();
			Shell shell = (Shell) shellEntry.getKey();
			
			// Now go through the collection of invalid controls for this shell, collecting those that are not disposed,
			// adding them to the list of controls that need validation and getting all of their parents into the invalidatedControls list.
			Collection invalids = (Collection) shellEntry.getValue();
			List invalidsForShell = new ArrayList(invalids.size());
			Iterator invalidsItr = (invalids).iterator();
			while (invalidsItr.hasNext()) {
				Control control = (Control) invalidsItr.next();
				if (control != shell)
					invalidsForShell.add(control);	// Only the explicit invalid control will participate in the layout later.
				// But add the control and all of its parents to the collection of invalidated controls so that they can have their images refreshed.
				while(control != null) {
					if (invalidatedControls.add(control))
						sendAboutToValidateToManager(control);
					control = control.getParent();
				}
			}
			
			try {
				// Now actually relayout the controls.
				// Note: If it is the shell itself that is invalid (such as change of layout), the
				// list of invalids for shell will be empty and it won't do anything. Need to check
				// for this and when just shell is invalid, then do a layout on shell directly.
				if (!invalidsForShell.isEmpty()) {
					Iterator controls = invalidsForShell.iterator();
					while(controls.hasNext()){
						try{
							((Composite)controls.next()).layout(true,true);
						} catch (ClassCastException exc){
						}
					}
					shell.layout((Control[]) invalidsForShell.toArray(new Control[invalidsForShell.size()]));
				}else {
					shell.layout(true, false);
				}
				if (shell == freeformShell)
					shell.pack(); // Also need to pack it for the freeform host.
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
		}
		return invalidatedControls;
	}

}
