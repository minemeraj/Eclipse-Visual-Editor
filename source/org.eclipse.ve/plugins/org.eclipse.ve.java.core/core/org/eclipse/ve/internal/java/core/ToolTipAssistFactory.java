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
 *  $RCSfile: ToolTipAssistFactory.java,v $
 *  $Revision: 1.17 $  $Date: 2005-10-20 19:34:42 $ 
 */
package org.eclipse.ve.internal.java.core;

import java.util.Iterator;

import org.eclipse.draw2d.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jem.internal.instantiation.base.IJavaInstance;
import org.eclipse.jem.internal.proxy.core.*;

import org.eclipse.ve.internal.cde.core.*;


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
	
	static  class NullTTAdapter implements IJavaToolTipProposalAdapter {
		final static Label l = new Label(JavaMessages.ToolTipAssistFactory_ToolTip_not_available_1) ; 
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

    static class DefaultInstanceProcessor implements ToolTipProcessor {
    	
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
			return getTTAdapter().getInstanceDisplayInformation();
		}

		public void activate() {
			// TODO in the future, if rename ever gets fixed such that it doesn't do an remove/add under the covers, this should reget the
			// display info because it could of changed. Need to handle removing the old display info, or changing
			// such that the figure is another figure which simply has the instancedisplayinfo as a child. That way we can easily
			// remove the old info and add in new info in its place.
		}

		public void deactivate() {
		}
    }
    
       /** 
     * Create a getter processor that at this time generates a single getter proposal.
     */
	static class DefaultMethodProcessor implements ToolTipProcessor {
    	
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
			public void activate() {
				// TODO in the future, if rename ever gets fixed such that it doesn't do an remove/add under the covers, this should reget the
				// display info because it could of changed. Need to handle removing the old display info, or changing
				// such that the figure is another figure which simply has the returndisplayinfo as a child. That way we can easily
				// remove the old info and add in new info in its place.
			}
			
			public void deactivate() {
				
			}
		}
	
	static class ErrorProcessor implements ToolTipProcessor {
		
		private Figure figure;
		private IErrorNotifier errNotifier;
		private Display display;
		
		/**
		 * Construct with a given notifier.
		 * @param notifier
		 * 
		 * @since 1.1.0
		 */
		public ErrorProcessor(IErrorNotifier notifier){
			errNotifier = notifier;
		}
		
		private IErrorNotifier.ErrorListener errorListener = new IErrorNotifier.ErrorListenerAdapter(){
			public void errorStatusChanged() {	
				display.asyncExec(new Runnable(){
					public void run(){
						updateFigure();
					}
				});
			}				
		};
		
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
			updateFigure();
			// The error severity could change, in which case we must refresh our figure
			errNotifier.addErrorListener(errorListener);
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

		public void activate() {
			if (figure != null) {
				// We've been created at least once, so just update and add errNotifier listening.
				// Get the errors
				updateFigure();
				// The error severity could change, in which case we must refresh our figure
				errNotifier.addErrorListener(errorListener);				
			}
		}

		public void deactivate() {
			errNotifier.removeErrorListener(errorListener);
		}
	}
	/**
	 * Construct with a beanproxy and a get method name
	 * If the get method throws an exception then show this in a label
	 */
	public static class GetMethodErrorProcessor implements ToolTipProcessor{
		
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

		public void activate() {
			// TODO in the future, if rename ever gets fixed such that it doesn't do an remove/add under the covers, this should reget the
			// display info because it could of changed. Need to handle removing the old display info, or changing
			// such that the figure is another figure which simply has the get displayinfo as a child. That way we can easily
			// remove the old info and add in new info in its place.
		}

		public void deactivate() {
		}
	}
        
    public static ToolTipProcessor[] createToolTipProcessors (IJavaInstance javaInstance, IErrorNotifier errorNotifier) {
    	ToolTipProcessor errorProcessor = new ErrorProcessor(errorNotifier);    	
    	ToolTipProcessor instanceProcessor = new DefaultInstanceProcessor(javaInstance);
    	ToolTipProcessor methodProcessor = new DefaultMethodProcessor(javaInstance);
		return new ToolTipProcessor[] { errorProcessor , methodProcessor, instanceProcessor } ;		
    }


}
