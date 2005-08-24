/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.java.codegen.java;
/*
 *  $RCSfile: ExpressionDecoderAdapter.java,v $
 *  $Revision: 1.9 $  $Date: 2005-08-24 23:30:45 $ 
 */

import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.jem.internal.instantiation.base.IJavaObjectInstance;

import org.eclipse.ve.internal.cde.core.CDEUtilities;

import org.eclipse.ve.internal.jcm.JCMPackage;

import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.codegen.util.CodeGenRuntimeException;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

public class ExpressionDecoderAdapter implements ICodeGenAdapter {

	
	protected IExpressionDecoder fDecoder = null;

	public ExpressionDecoderAdapter(IExpressionDecoder decoder) {
		fDecoder = decoder;
	}

	/**
	 * isAdaptorForType.
	 */
	public boolean isAdapterForType(Object type) {
		return JVE_CODE_GEN_TYPE.equals(type)
			|| JVE_CODEGEN_EXPRESSION_SOURCE_RANGE.equals(type)
			|| JVE_CODEGEN_EXPRESSION_ADAPTER.equals(type);
	}


	/**
	 * 	If an object is promoted into a full member, CodeGen will not
	 *  act on it up front, when it is moved from a property to a member.  This
	 *  is since as the object may refer to other objects that were not place in the 
	 *  model yet.  But, at the end of the promotion, a touch (snooze) will be given by
	 *  the PropertySheet or EditParts.
	 *  
	 * 
	 * @param msg
	 */
	protected boolean processPromotionIfNeeded(Notification msg) {
		
		boolean needPromotion = false;
		if (msg.isTouch()) {
			Object val = msg.getNewValue();
			if (val!=null && val instanceof IJavaObjectInstance) {
				IJavaObjectInstance obj = (IJavaObjectInstance)val;
				if (obj.eContainmentFeature()==JCMPackage.eINSTANCE.getMemberContainer_Members()) {
					// The value is a full bean part and should be in the BDM
					// If we ask the BDM, it will be generated on the fly
					// if it was marked as lazyCreate.  Before we lazy 
					// create, we will need to clean up.
					BeanDecoderAdapter bAdapter = (BeanDecoderAdapter) EcoreUtil.getExistingAdapter(obj, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
					if (bAdapter == null) { 
						needPromotion=true;
						// force the lazy creation
				        fDecoder.getBeanModel().getABean(obj); 
					}
				}
			}
		}
//		else {		
//			// Nested attribute
//			if (!fDecoder.getSF().equals(msg.getFeature())) {
//				// Is the current value of the SF (arg) now notifies us on its own new attributes ?
//				Object val = fDecoder.getBeanPart().getEObject().eGet(fDecoder.getSF());
//				if (val != null && val instanceof EObject) {
//					EObject curVal = (EObject) val;
//					if (curVal.equals(msg.getNotifier()))
//						needPromotion=true;
//				}
//			}
//		}

		if (needPromotion) {
			// New value is promoted, but we may need to clean up
			// the current expression that is refering to the new object
			EObject bInstance = fDecoder.getBeanPart().getEObject();
			EStructuralFeature sf = fDecoder.getSF();
			fDecoder.deleteFromSrc();
			BeanDecoderAdapter bAdapter =
				(BeanDecoderAdapter) EcoreUtil.getExistingAdapter(bInstance, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
			Notification nMsg = new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.SET, sf, null, msg.getNotifier(), true);
			bAdapter.notifyChanged(nMsg);
		}
		return needPromotion;
	}


	/**
	 * applied: A setting has been applied to the mof object,
	 * notify the decoder
	 */
	public void notifyChanged(Notification msg) {

		try {

			if (fDecoder.getBeanModel() == null
				|| fDecoder.getBeanPart().getModel().isStateSet(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL)
				|| fDecoder.getBeanPart().getModel().isStateSet(IBeanDeclModel.BDM_STATE_SNIPPET))
				return;

			switch (msg.getEventType()) {
				case Notification.SET :
					if (!CDEUtilities.isUnset(msg)) {
						if (processPromotionIfNeeded(msg))
							return;
					} // Else flow into unset since really an unset.
				case Notification.UNSET :
				case Notification.REMOVE :
				case Notification.MOVE :
					if (fDecoder.isRelevantFeature((EStructuralFeature) msg.getFeature()))
						if(fDecoder.canReflectMOFChange())
							fDecoder.reflectMOFchange();
						else
							throw new CodeGenRuntimeException("Decoder " +fDecoder.getClass().toString()+" cannot reflect the MOF Model for expression "+fDecoder.getCurrentExpression()); //$NON-NLS-1$ //$NON-NLS-2$
					break;
			}
		} catch( CodeGenRuntimeException e){
			if (JavaVEPlugin.isLoggingLevel(Level.FINE))
				JavaVEPlugin.log(e.getMessage(), Level.FINE);
			throw e;
		} catch (Throwable t) {
			JavaVEPlugin.log(t, Level.WARNING);
		}
	}

	public IExpressionDecoder getDecoder() {
		return fDecoder;
	}

	/**
	 * Returns a String that represents the value of this object.
	 * @return a string representation of the receiver
	 */
	public String toString() {
		// Insert code to print the receiver here.
		// This implementation forwards the message to super.
		// You may replace or supplement this.
		return "\tExpressionDecoderAdapter:" + fDecoder; //$NON-NLS-1$
	}

	public ICodeGenSourceRange getJavaSourceRange() throws CodeGenException {
		return fDecoder.getExprRef().getTargetSourceRange();
	}

	/**
	 * @see ICodeGenAdapter#getBDMSourceRange()
	 */
	public ICodeGenSourceRange getBDMSourceRange() throws CodeGenException {
		return new CodeGenSourceRange(fDecoder.getExprRef().getOffset(), fDecoder.getExprRef().getLen());
	}

	/**
	 * @see org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter#getHighlightSourceRange()
	 */
	public ICodeGenSourceRange getHighlightSourceRange() throws CodeGenException {
		return getJavaSourceRange();
	}

	public ICodeGenAdapter getShadowSourceRangeAdapter() {

		return new ICodeGenAdapter() {

			public ICodeGenSourceRange getHighlightSourceRange() throws org.eclipse.ve.internal.java.codegen.util.CodeGenException {
				return ExpressionDecoderAdapter.this.getHighlightSourceRange();
			}

			public ICodeGenSourceRange getJavaSourceRange() throws org.eclipse.ve.internal.java.codegen.util.CodeGenException {
				return ExpressionDecoderAdapter.this.getJavaSourceRange();
			}

			public ICodeGenSourceRange getBDMSourceRange() throws org.eclipse.ve.internal.java.codegen.util.CodeGenException {
				return ExpressionDecoderAdapter.this.getBDMSourceRange();
			}

			public void notifyChanged(Notification msg) {
				if (JavaVEPlugin.isLoggingLevel(Level.FINE))
					JavaVEPlugin.log("SourceRangeExpressionDecoderAdapter Shadow: msgType=: " + msg.getEventType(), Level.FINE); //$NON-NLS-1$
			}

			public org.eclipse.emf.common.notify.Notifier getTarget() {
				return null;
			}

			public boolean isAdapterForType(java.lang.Object type) {
				return JVE_CODEGEN_EXPRESSION_SOURCE_RANGE.equals(type);
			}
			public void setTarget(org.eclipse.emf.common.notify.Notifier newTarget) {
			}
		};

	}

	/**
	 * @see org.eclipse.emf.common.notify.Adapter#getTarget()
	 */
	public Notifier getTarget() {
		return null;
	}

	/**
	 * @see org.eclipse.emf.common.notify.Adapter#setTarget(Notifier)
	 */
	public void setTarget(Notifier newTarget) {
	}

}
