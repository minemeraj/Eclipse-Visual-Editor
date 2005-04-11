/*
 * Created on Jun 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.eclipse.ve.internal.java.codegen.wizards;
/*******************************************************************************
 * Copyright (c)  2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: IVisualClassCreationSourceContributor.java,v $
 *  $Revision: 1.3 $  $Date: 2005-04-11 22:17:55 $ 
 */

import java.net.URL;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public interface IVisualClassCreationSourceContributor {
	
	public static StatusInfo OK_STATUS = new StatusInfo();
	
	public class StatusInfo implements IStatus{
		private String fMessage;
		private int fSeverity;
		private String fPluginID;
		
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
		
	
	public boolean needsFormatting();
	public URL getTemplateLocation();
	/** 
	 * @param resource Representing the container that the template is requesting to create itself into
	 * @return String.  If the contributor is valid then return null, otherwise a status reason as to why it is invalid
	 * This could be because the project build path is incorrect or the wrong type
	 */
	public IStatus getStatus(IResource resource);
}
