/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ToolTipAssistFactory.java,v $
 *  $Revision: 1.9 $  $Date: 2004-08-27 15:34:10 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.Iterator;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.IBeanProxy;
import org.eclipse.jem.internal.proxy.core.IMethodProxy;
import org.eclipse.jem.internal.proxy.core.ThrowableProxy;

import org.eclipse.ve.internal.cde.core.CDEUtilities;


/**
 * @author gmendel
 * 
 * This is a temporary implementation of hardcoded ToolTip Assistants.
 * We need to provide an extendible mechanism for these
 */
public class ToolTipAssistFactory {
	
	public static class LineFigure extends Figure {
		public LineFigure() {
			setPreferredSize(100, 2);
		}
		protected void paintFigure(Graphics g) {
			g.setLineStyle(Graphics.LINE_DOT);
			g.setXORMode(true);
			g.setForegroundColor(ColorConstants.darkGray);
			if (bounds.width > bounds.height) {
				g.drawLine(bounds.x, bounds.y, bounds.right(), bounds.y);
				g.drawLine(bounds.x + 2, bounds.y, bounds.right(), bounds.y);
			} else {
				g.drawLine(bounds.x, bounds.y, bounds.x, bounds.bottom());
				g.drawLine(bounds.x, bounds.y + 2, bounds.x, bounds.bottom());
			}
		}
	}
	
	/**
	 * ToolTipDetails are able to create a figure
	 * @since 1.0.0
	 */
	public static interface TooltipDetails{
		IFigure createFigure();
	}	
		
	static  class NullTTAdapter implements IJavaToolTipProposalAdapter {
		final static Label l = new Label(JavaMessages.getString("ToolTipAssistFactory.ToolTip_not_available_1")) ; //$NON-NLS-1$
		final static Label l2 = new Label() ;
				
		public java.lang.String getInitMethodName() {		
		   return null;
	    }
		public Label getInstanceDisplayInformation() {			
			return l;
		}
		public String getInstanceName() {
			return null;
		}
		public Label getReturnMethodDisplayInformation() {
			return l2;
		}
	}
	static final NullTTAdapter fNullTTAdapter = new NullTTAdapter() ; 
	
	private static IJavaToolTipProposalAdapter getToolTipAdapter(IJavaInstance javaInstance) {
		return (IJavaToolTipProposalAdapter) EcoreUtil.getExistingAdapter(javaInstance, IJavaToolTipProposalAdapter.JAVA_ToolTip_Proposal_TYPE);		
	}	

    static class DefaultInstanceProcessor implements ToolTipAssistFactory.TooltipDetails {
    	
    	IJavaInstance					javaInstance ;
    	IJavaToolTipProposalAdapter fTTadapter = null ;
    	
    	/**
    	 * Create an instance proposal generator, which at this time generates a single 
    	 * proposal: instance name.
    	 */
		public DefaultInstanceProcessor (IJavaInstance aJavaInstance) {
			javaInstance = aJavaInstance;
		}
		
		IJavaToolTipProposalAdapter  getTTAdapter() {
			if (fTTadapter == null) fTTadapter = getToolTipAdapter(javaInstance) ;
			return fTTadapter!= null ? fTTadapter : fNullTTAdapter ;
			
		}

		/* 
		 * Return a draw2D figure
		 */
		public IFigure createFigure() {
			// TODO
			return getTTAdapter().getInstanceDisplayInformation();
		}
    }
    
       /** 
     * Create a getter processor that at this time generates a single getter proposal.
     */
	static class DefaultMethodProcessor implements ToolTipAssistFactory.TooltipDetails {
    	
			IJavaInstance javaInstance;
		    IJavaToolTipProposalAdapter fTTadapter = null ;
   			public DefaultMethodProcessor (IJavaInstance aJavaInstance) {
				javaInstance = aJavaInstance ;
			}
		    private IJavaToolTipProposalAdapter  getTTAdapter() {
				if (fTTadapter == null) fTTadapter = getToolTipAdapter(javaInstance) ;								   
				return fTTadapter!= null ? fTTadapter : fNullTTAdapter ;			
			}    	  
			/* 
			 * Return a draw2D label with the icon and method name on the right
			 */
			public IFigure createFigure() {
				// TODO - This is incomplete
				return getTTAdapter().getReturnMethodDisplayInformation();				
			}
		}
	
	static class ErrorProcessor implements ToolTipAssistFactory.TooltipDetails {
		
		IJavaInstance javaInstance;
		private Figure figure;
		private IErrorNotifier errNotifier;
		private Display display;
		
		public ErrorProcessor(IJavaInstance aJavaInstance){
			javaInstance = aJavaInstance;
		}
		/* 
		 * Iterate over the errors creating a composite figure that arranges them all
		 */
		public IFigure createFigure() {
			display = Display.getCurrent();
			figure = new Panel();
			FlowLayout layout = new FlowLayout(false);
			layout.setMajorSpacing(0);
			layout.setMinorSpacing(0);						
			figure.setLayoutManager(layout);
			// Get the errors
			errNotifier =(IErrorNotifier) EcoreUtil.getExistingAdapter(javaInstance, IErrorNotifier.ERROR_NOTIFIER_TYPE);
			updateFigure();
			// The error severity could change, in which case we must refresh our figure
			errNotifier.addErrorListener(new IErrorNotifier.ErrorListenerAdapter(){
				public void errorStatusChanged() {	
					CDEUtilities.displayExec(display,new Runnable(){
						public void run(){
							updateFigure();
						}
					});
				}				
			});
			return figure;
		}
		private void updateFigure(){
			Iterator errors = errNotifier.getErrors().iterator();
			figure.removeAll();
			while(errors.hasNext()){
				IErrorHolder.ErrorType error = (IErrorHolder.ErrorType) errors.next();
				Label l = new Label();
				l.setIcon(error.getImage());
				l.setText(error.getMessage());
				figure.add(l);
			}
		}
	}
	/**
	 * Construct with a beanproxy and a get method name
	 * If the get method throws an exception then show this in a label
	 */
	public static class GetMethodErrorProcessor implements ToolTipAssistFactory.TooltipDetails{
		
		private IBeanProxy beanProxy;
		private String methodName;
		private IMethodProxy methodProxy;

		public GetMethodErrorProcessor(IBeanProxy aBeanProxy, String aMethodName){
			beanProxy = aBeanProxy;
			methodName = aMethodName;
		}

		public IFigure createFigure() {
			Label label = new Label();
			if(beanProxy == null || !beanProxy.isValid()) return null;
			// Get the error message from calling the get method
			if(methodProxy == null){
				methodProxy = beanProxy.getTypeProxy().getMethodProxy(methodName);
			}
			String errorMessage = null;
			try {
				methodProxy.invoke(beanProxy);
			} catch (ThrowableProxy e) {
				errorMessage = e.toBeanString();
			}
			if(errorMessage != null){
				label.setIcon(IErrorHolder.ErrorType.getWarningErrorImage());
				label.setText(errorMessage);
				return label;				
			} else {
				return null;
			}
		}
	}
        
    public static ToolTipAssistFactory.TooltipDetails[] createToolTipProcessors (IJavaInstance javaInstance) {
    	ToolTipAssistFactory.TooltipDetails errorProcessor = new ErrorProcessor(javaInstance);    	
    	ToolTipAssistFactory.TooltipDetails instanceProcessor = new DefaultInstanceProcessor(javaInstance);
    	ToolTipAssistFactory.TooltipDetails methodProcessor = new DefaultMethodProcessor(javaInstance);
		return new ToolTipAssistFactory.TooltipDetails[] { errorProcessor , methodProcessor, instanceProcessor } ;		
    }


}
