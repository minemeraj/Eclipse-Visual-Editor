/*
 * Created on May 23, 2003
 * by gmendel
 *
*******************************************************************************
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
 *  $RCSfile: ToolTipAssistFactory.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:48:30 $ 
 */
package org.eclipse.ve.internal.java.core;

import org.eclipse.draw2d.Label;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.*;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;


/**
 * @author gmendel
 * 
 * This is a temporary implementation of hardcoded ToolTip Assistants.
 * We need to provide an extendible mechanism for these
 */
public class ToolTipAssistFactory {
		
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
	
	private static IJavaToolTipProposalAdapter getToolTipAdapter(EditPart ep) {
		IJavaToolTipProposalAdapter result = null ;
		if (ep.getModel() instanceof IJavaObjectInstance) {
			IJavaObjectInstance o = (IJavaObjectInstance) ep.getModel() ;
			result = (IJavaToolTipProposalAdapter) EcoreUtil.getExistingAdapter(o, IJavaToolTipProposalAdapter.JAVA_ToolTip_Proposal_TYPE);
		}		
		return result ;		
	}

    static class DefaultInstanceProcessor implements IContentAssistProcessor {
    	
    	EditPart					fEditPart ;
    	IJavaToolTipProposalAdapter fTTadapter = null ;
    	
    	
    	/** 
    	 * Create an instance proposal generator, which at this time generates a single 
    	 * proposal: instance name.
    	 */
		public DefaultInstanceProcessor (EditPart ep) {
			fEditPart = ep ;
		}
		
		IJavaToolTipProposalAdapter  getTTAdapter() {
			if (fTTadapter == null) 
			   fTTadapter = getToolTipAdapter(fEditPart) ;
			return fTTadapter!= null ? fTTadapter : fNullTTAdapter ;
			
		}
		/** 
		 * Create a getter processor that at this time generates a single getter proposal.
		 */
		public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int documentOffset) {
			ICompletionProposal p = new ICompletionProposal() {
				Label fResult = null;
				private Label getResult() {
					// Try to be more effient on calling getTTAdapter().
					// getResult is going to be called twice.  Once for text, and one for image.
					if (fResult == null) {
						fResult = getTTAdapter().getInstanceDisplayInformation();
						return fResult;
					}
					else {
						Label l = fResult;
						fResult = null;
						return l;
					}
				}
				public void apply(org.eclipse.jface.text.IDocument document) {}

				public org.eclipse.swt.graphics.Point getSelection(org.eclipse.jface.text.IDocument document) {
					return null ;
				}
				public java.lang.String getAdditionalProposalInfo() {
					return null;
				}
				public java.lang.String getDisplayString() {			
					try {		
					   return getResult().getText();
					}
					catch (Exception e) {
					  return "" ; //$NON-NLS-1$
					}
				}
				public org.eclipse.swt.graphics.Image getImage() {
					return getResult().getIcon();
				}

				public org.eclipse.jface.text.contentassist.IContextInformation getContextInformation() {
					return null;
				}
			};
			return new ICompletionProposal[] { p } ;						
		}
		public IContextInformation[] computeContextInformation(ITextViewer viewer, int documentOffset) {
			return null;
		}
		public char[] getCompletionProposalAutoActivationCharacters() {
			return null;
		}
		public char[] getContextInformationAutoActivationCharacters() {
			return null;
		}
		public IContextInformationValidator getContextInformationValidator() {
			return null;
		}
		public String getErrorMessage() {
			return null;
		}
    }
    
    /** 
     * Create a getter processor that at this time generates a single getter proposal.
     */
	static class DefaultMethodProcessor implements IContentAssistProcessor {
    	
			EditPart	fEditPart ;
		    IJavaToolTipProposalAdapter fTTadapter = null ;
    	
			public DefaultMethodProcessor (EditPart ep) {
				fEditPart = ep ;
			}
		    IJavaToolTipProposalAdapter  getTTAdapter() {
				if (fTTadapter == null) 
					 fTTadapter = getToolTipAdapter(fEditPart) ;								   
				return fTTadapter!= null ? fTTadapter : fNullTTAdapter ;			
			}
    	  
			public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int documentOffset) {
				ICompletionProposal p = new ICompletionProposal() {
					Label fResult = null ;
					private Label getResult() {
						// Try to be more effient on calling getTTAdapter().
						// getResult is going to be called twice.  Once for text, and one for image.
						if (fResult == null) {
						   fResult = getTTAdapter().getReturnMethodDisplayInformation() ;
						   return fResult ;
						}
						else {
							Label l = fResult ;
							fResult = null ;
							return l ;
						}						
					}
					public void apply(org.eclipse.jface.text.IDocument document) {}
					public org.eclipse.swt.graphics.Point getSelection(org.eclipse.jface.text.IDocument document) {
						return null ;
					}
					public java.lang.String getAdditionalProposalInfo() {
						return null;
					}
					public java.lang.String getDisplayString() {
						try {
							return getResult().getText();
						}
						catch (Exception e) {
							return ""; //$NON-NLS-1$
						}
					}
					public org.eclipse.swt.graphics.Image getImage() {
						return  getResult().getIcon();
					}
					public org.eclipse.jface.text.contentassist.IContextInformation getContextInformation() {
						return null;
					}
				};
				return new ICompletionProposal[] { p } ;						
			}
			public IContextInformation[] computeContextInformation(ITextViewer viewer, int documentOffset) {
				return null;
			}
			public char[] getCompletionProposalAutoActivationCharacters() {
				return null;
			}
			public char[] getContextInformationAutoActivationCharacters() {
				return null;
			}
			public IContextInformationValidator getContextInformationValidator() {
				return null;
			}
			public String getErrorMessage() {
				return null;
			}
		}
    
    
    public static IContentAssistProcessor[] createToolTipProcessors (EditPart ep) {
		IContentAssistProcessor instanceProcessor = new DefaultInstanceProcessor(ep) ;
		IContentAssistProcessor methodProcessor = new DefaultMethodProcessor(ep) ;
		return new IContentAssistProcessor[] { methodProcessor, instanceProcessor } ;		
    }


}
