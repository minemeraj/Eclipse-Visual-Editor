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
 *  $RCSfile: AnonymousEventSrcGenerator.java,v $
 *  $Revision: 1.3 $  $Date: 2004-03-22 23:49:37 $ 
 */
package org.eclipse.ve.internal.java.codegen.util;

import java.util.List;

import org.eclipse.jem.internal.beaninfo.EventSetDecorator;
import org.eclipse.ve.internal.jcm.*;
import org.eclipse.ve.internal.jcm.EventInvocation;
import org.eclipse.ve.internal.jcm.Listener;
import org.eclipse.ve.internal.java.vce.templates.*;

/**
 * @author Gili Mendel
 *
 */
public class AnonymousEventSrcGenerator  extends AbstractEventSrcGenerator {
	
   public final static  String BASE_PLUGIN = "org.eclipse.ve.java.core"; //$NON-NLS-1$
   public final static  String TEMPLATE_PATH = "templates/org/eclipse/ve/internal/java/codegen/jjet/util" ; //$NON-NLS-1$
   public final static  String EVENT_TEMPLATE_CLASS_NAME = "AnonymousEventTemplate" ;   //$NON-NLS-1$
   public final static  String EVENT_METHOD_TEMPLATE_CLASS_NAME = "AnonymousEventMethodTemplate" ;   //$NON-NLS-1$
   public final static  String EVENT_TEMPLATE_NAME = EVENT_TEMPLATE_CLASS_NAME+JAVAJET_EXT ;  
   public final static  String EVENT_METHOD_TEMPLATE_NAME = EVENT_METHOD_TEMPLATE_CLASS_NAME+JAVAJET_EXT ;

//		    ivjJTextField1.addMouseListener(new java.awt.event.MouseListener() {
//				public void mouseClicked(java.awt.event.MouseEvent e) {
//					;
//				}
//				public void mousePressed(java.awt.event.MouseEvent e) {
//					;
//				}
//				public void mouseReleased(java.awt.event.MouseEvent e) {
//					;
//				}
//				public void mouseEntered(java.awt.event.MouseEvent e) {
//					;
//				}
//				public void mouseExited(java.awt.event.MouseEvent e) {
//					;
//				}
//			});   

	

   IEventTemplate       fEventTemplate = null ;
   IEventTemplate		fEventMethodTemplate = null ;



public AnonymousEventSrcGenerator (AbstractEventInvocation ee, Listener l, String rec) {
   	super(ee,l,rec) ;
}  

protected IEventTemplate getEventTemplate() {
 	if (fEventTemplate != null) return fEventTemplate ;
 	
	try {
		List list = TemplateUtil.getPluginAndPreReqJarPath(BASE_PLUGIN);
		list.addAll(TemplateUtil.getPlatformJREPath());
		String[] classPath = (String[]) list.toArray(new String[list.size()]);
		String   templatePath = TemplateUtil.getPluginInstallPath(BASE_PLUGIN, TEMPLATE_PATH) ;
		
		fEventTemplate = (IEventTemplate)
		             TemplateObjectFactory.getClassInstance(classPath, new String[] {templatePath}, 
		                                                    EVENT_TEMPLATE_NAME, TemplateUtil.getClassLoader(BASE_PLUGIN),
		                                                    EVENT_TEMPLATE_CLASS_NAME,null) ;
	}
	catch (TemplatesException e) {
		org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
	} 	                                                            	
 	return fEventTemplate ; 	
}

protected IEventTemplate getEventMethodTemplate() {
	if (fEventMethodTemplate != null) return fEventMethodTemplate ;
 	
	try {
		List list = TemplateUtil.getPluginAndPreReqJarPath(BASE_PLUGIN);
		list.addAll(TemplateUtil.getPlatformJREPath());
		String[] classPath = (String[]) list.toArray(new String[list.size()]);
		String   templatePath = TemplateUtil.getPluginInstallPath(BASE_PLUGIN, TEMPLATE_PATH) ;
		
		fEventMethodTemplate = (IEventTemplate)
					 TemplateObjectFactory.getClassInstance(classPath, new String[] {templatePath}, 
															EVENT_METHOD_TEMPLATE_NAME, TemplateUtil.getClassLoader(BASE_PLUGIN),
															EVENT_METHOD_TEMPLATE_CLASS_NAME,null) ;
	}
	catch (TemplatesException e) {
		org.eclipse.ve.internal.java.core.JavaVEPlugin.log(e) ;
	} 	                                                            	
	return fEventMethodTemplate ; 	
}

public String generateEvent() {	
	return getEventTemplate().generateEvent(getInfo()) ;
}
     
  


	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.IEventSrcGenerator#generateEventMethod(java.lang.String)
	 */
	public String generateEventMethod(Callback[] callbacks) {
		EventInfo info = new EventInfo(callbacks) ;
		return getEventMethodTemplate().generateEvent(info) ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.util.AbstractEventSrcGenerator#getListenerMethod()
	 */
	protected String getAddListenerMethod() {
		EventSetDecorator ed = org.eclipse.jem.internal.beaninfo.core.Utilities.getEventSetDecorator(((EventInvocation)fEE).getEvent()) ;    
        return ed.getAddListenerMethod().getName() ;

	}

}
