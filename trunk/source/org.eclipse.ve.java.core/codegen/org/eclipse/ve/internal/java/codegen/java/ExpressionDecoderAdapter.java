/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
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
 *  $Revision: 1.7 $  $Date: 2005-02-15 23:28:35 $ 
 */

import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.ve.internal.cde.core.CDEUtilities;

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

	protected void primPromoteAttribute(Notification msg, EObject bInstance, EStructuralFeature sf) {

		BeanDecoderAdapter bAdapter =
			(BeanDecoderAdapter) EcoreUtil.getExistingAdapter(bInstance, ICodeGenAdapter.JVE_CODEGEN_BEAN_PART_ADAPTER);
		Notification nMsg = new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.SET, sf, null, msg.getNotifier(), true);
		bAdapter.notifyChanged(nMsg);
	}

	/**
	 * If an event occur which adds attributes to the instance
	 * of "this" attribute, we need to promote the attribute to
	 * a full plagged BeanPart instance.
	 */
	protected boolean processPromotionIfNeeded(Notification msg) {
		if (fDecoder.getSF().equals(msg.getFeature()))
			return false;
		// Is the current value of the SF (arg) now notifies us on its own new attributes ?
		Object val = fDecoder.getBeanPart().getEObject().eGet(fDecoder.getSF());
		if (!(val instanceof EObject))
			return false;
		EObject curVal = (EObject) val;
		if (curVal == null)
			return false;
		if (!curVal.equals(msg.getNotifier()))
			return false;

		EObject bInstance = fDecoder.getBeanPart().getEObject();
		EStructuralFeature sf = fDecoder.getSF();
		fDecoder.deleteFromSrc();
		// A new Expression will be generated by default
		primPromoteAttribute(msg, bInstance, sf);
		return true;
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
						if (msg.isTouch())
							break; // Dont' process touch
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
	 * @see org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter#getSelectionSourceRange()
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
