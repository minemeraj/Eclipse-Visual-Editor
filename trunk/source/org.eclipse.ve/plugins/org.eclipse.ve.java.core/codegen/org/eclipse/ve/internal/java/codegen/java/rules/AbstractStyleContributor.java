/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: AbstractStyleContributor.java,v $
 *  $Revision: 1.4 $  $Date: 2005-09-20 22:04:42 $ 
 */
package org.eclipse.ve.internal.java.codegen.java.rules;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

import org.eclipse.ve.internal.java.vce.VCEPreferences;
import org.eclipse.ve.internal.java.vce.rules.IEditorStylePrefUI;



/**
 * @author Gili Mendel
 * 
 *  This contributor works with the VCE style
 *
 */
public abstract class AbstractStyleContributor implements IEditorStylePrefUI{

	private Control fRootControl = null ;
	private FontMetrics fFontMetrics = null ;
	private Preferences fStore = null ;
	
	
	protected FontMetrics getFontMetrics(Control c) {
		if (fFontMetrics != null)
			return fFontMetrics;
		if (c == null || c.isDisposed())
			return null;

		GC gc = new GC(c);
		gc.setFont(c.getFont());
		fFontMetrics = gc.getFontMetrics();
		gc.dispose();
		return fFontMetrics;
	}


	protected Label createLabel(Composite group, String aLabelString, Image aLabelImage) {
		Label label = new Label(group, SWT.LEFT);
		if (aLabelImage != null) {
			label.setImage(aLabelImage);
		}
		else {
			label.setText(aLabelString);
		}
		GridData data = new GridData();
		label.setLayoutData(data);
		return label;
	}	
	
	protected Button createButton(Composite parent, String label, int style) {
		Button button = new Button(parent, style);
		if (label != null)
			button.setText(label);
		GridData data = new GridData();
		button.setData(data);
		return button;
	}
	
	protected Button createCheckBox(Composite parent, String label, int style) {
		if ((style & SWT.RADIO) ==0)
		   style |= SWT.CHECK ;
		Button button = createButton(parent, label, style);
		return button;
	}
	
	public final Control createUI(Composite parent) {
		fRootControl = buildUI (parent) ;
		fRootControl.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				disposed();
			}
		});	    	    				   
		return fRootControl ;
	}
	protected abstract Control buildUI(Composite parent) ;
	
	protected Preferences getPrefStore() {
		if (fStore != null) return fStore ;
		fStore = primGetPrefStore() ;
		return fStore ;
	}
	
	public static Preferences primGetPrefStore() {
		return VCEPreferences.getPlugin().getPluginPreferences();
	}
		
	
	protected void saveStore() {
		VCEPreferences.getPlugin().savePluginPreferences() ;
	}
	
	/**
	 * Called whenever the control has been disposed. Remember
	 * to call super.disposed() at the end.
	 * 
	 * This method is not intended to be called by any subclass other
	 * than by overrides to the disposed() method.
	 */
	protected void disposed() {
		fFontMetrics = null ;
		fRootControl = null ;
		fStore = null;		
	}
}
