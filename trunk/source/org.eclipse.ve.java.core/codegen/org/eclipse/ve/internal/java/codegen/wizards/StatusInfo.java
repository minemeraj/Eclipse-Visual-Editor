/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/**
 * 
 */
package org.eclipse.ve.internal.java.codegen.wizards;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class StatusInfo implements IStatus{
	private String fMessage;
	private int fSeverity;
	private String fPluginID;
	public static final StatusInfo OK_STATUS = new StatusInfo();
	
	public StatusInfo(){
		this(OK, null,JavaVEPlugin.PLUGIN_ID);		 
	}
	/**
	 * Creates a status .
	 * @param severity The status severity: ERROR, WARNING, INFO and OK.
	 * @param message The message of the status. Applies only for ERROR,
	 * WARNING and INFO.
	 */	
	public StatusInfo(int severity, String message, String pluginID) {
		fMessage= message;
		fSeverity= severity;
		fPluginID = pluginID; 
	}			

	public IStatus[] getChildren() {
		return null;
	}
	public int getCode() {
		return 0;
	}
	public Throwable getException() {
		return null;
	}
	public String getMessage() {
		return fMessage;
	}
	public String getPlugin() {
		return fPluginID;
	}
	public void setPlugin(String aPluginID){
		fPluginID = aPluginID;
	}
	public int getSeverity() {
		return fSeverity;
	}
	public boolean isMultiStatus() {
		return false;
	}
	public boolean isOK() {
		return fSeverity == IStatus.OK;
	}
	public boolean matches(int severityMask) {
		return (fSeverity & severityMask) != 0;
	}
}