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
 *  Created May 27, 2005 by Gili Mendel
 * 
 *  $RCSfile: EditorSettings.java,v $
 *  $Revision: 1.2 $  $Date: 2005-08-24 23:30:47 $ 
 */
package org.eclipse.ve.internal.java.codegen.editorpart;

import java.util.ArrayList;

import org.eclipse.core.resources.IResource;
import org.eclipse.ui.part.FileEditorInput;

import org.eclipse.ve.internal.java.core.JavaVEPlugin;
 
/**
 * EditorSettings holds a set of settings and drive their life cycle.
 * @since 1.1.0
 */
public class EditorSettings {
	
	public final static String SEPERATOR = ";";
	
	
	protected IResource file;
	protected JavaVisualEditorPart editor;	
	protected ArrayList settings = new ArrayList();
	
	
	/**
	 * The ability to apply/monitor/persist some editor setting
	 * It is up to the setting to monitor and persist it setting details.
	 * the apply method will be called when the editor comes up.
	 * @since 1.1.0
	 */
	public interface ISetting {
		public void apply();
		public void dispose();	
		public void setQualifier(String q);
		public void setEditor(JavaVisualEditorPart e);
		public void setResource(IResource r);
	}
	
	protected EditorSettings(IResource f, JavaVisualEditorPart e) {
		file = f;
		editor = e;			
	}
	
	
	public void apply() {
		for (int i = 0; i < settings.size(); i++) {
			((ISetting)settings.get(i)).apply();			
		}
	}
	
	public void addSetting(ISetting s) {
		s.setQualifier(JavaVEPlugin.PLUGIN_ID);
		s.setEditor(editor);
		s.setResource(file);
		settings.add(s);
	}
	
	public void addSettingAndApply(ISetting s) {
		addSetting(s);
		s.apply();
	}
	
	public void dispose() {
		for (int i = 0; i < settings.size(); i++) {
			((ISetting)settings.get(i)).dispose();			
		}
	}
	
	public void setInput(FileEditorInput input) {
		file = input.getFile();
		for (int i = 0; i < settings.size(); i++) {
			((ISetting)settings.get(i)).setResource(file);			
		}		
	}
	
	
	public static EditorSettings getEditorSetting (JavaVisualEditorPart editor) {
		EditorSettings settings = null;
		if (editor!=null) {
		   IResource r = ((FileEditorInput)editor.getEditorInput()).getFile();
		   settings = new EditorSettings(r, editor);
		}
		return settings;				 
	}
	
	public String toString() {
		return file.getName();
	}

}
