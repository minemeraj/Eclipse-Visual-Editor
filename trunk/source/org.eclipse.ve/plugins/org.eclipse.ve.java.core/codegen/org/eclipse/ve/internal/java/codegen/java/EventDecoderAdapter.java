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
 *  $RCSfile: EventDecoderAdapter.java,v $
 *  $Revision: 1.5 $  $Date: 2005-08-24 23:30:45 $ 
 */
package org.eclipse.ve.internal.java.codegen.java;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.ve.internal.jcm.*;

import org.eclipse.ve.internal.java.codegen.model.IBeanDeclModel;
import org.eclipse.ve.internal.java.codegen.util.CodeGenException;
import org.eclipse.ve.internal.java.core.JavaVEPlugin;

/**
 * @author gmendel
 */
public class EventDecoderAdapter implements ICodeGenAdapter {
	
	AbstractEventInvocation  fTarget = null ;
	IEventDecoder    fDecoder ;


    public EventDecoderAdapter (IEventDecoder decoder) {
    	fDecoder = decoder ;
    }
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter#getBDMSourceRange()
	 */
	public ICodeGenSourceRange getBDMSourceRange() throws CodeGenException {
		return new CodeGenSourceRange(fDecoder.getExprRef().getOffset(), fDecoder.getExprRef().getLen());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter#getHighlightSourceRange()
	 */
	public ICodeGenSourceRange getHighlightSourceRange() throws CodeGenException {
		return getJavaSourceRange();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.java.codegen.java.ICodeGenAdapter#getJavaSourceRange()
	 */
	public ICodeGenSourceRange getJavaSourceRange() throws CodeGenException {
		return fDecoder.getExprRef().getTargetSourceRange();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#getTarget()
	 */
	public Notifier getTarget() {
		return fTarget ;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#isAdapterForType(java.lang.Object)
	 */
	public boolean isAdapterForType(Object type) {
		return JVE_CODE_GEN_TYPE.equals(type) || JVE_CODEGEN_EXPRESSION_SOURCE_RANGE.equals(type) || JVE_CODEGEN_EVENT_ADAPTER.equals(type);
	}

	protected void processMany(Notification msg) {
		if (msg.getEventType() == Notification.ADD_MANY) {
			List al = (List) msg.getNewValue();
			int i = 0;
			Iterator aitr = al.iterator();
			int pos = msg.getPosition();
			while (aitr.hasNext()) {
				int mpos = pos < 0 ? pos : pos + i++;
				Object o = aitr.next();
				notifyChanged(new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.ADD, (EStructuralFeature) msg.getFeature(), null, o, mpos));
			}
		}
		else {  // REMOVE_MANY
			List rl = (List) msg.getOldValue();
			Iterator ritr = rl.iterator();
			int pos = msg.getPosition();
			while (ritr.hasNext()) {
				Object o = ritr.next();
				notifyChanged(new ENotificationImpl((InternalEObject) msg.getNotifier(), Notification.REMOVE, (EStructuralFeature) msg.getFeature(), o, null, pos));
			}
		}
	}
	/* 
	 * @see org.eclipse.emf.common.notify.Adapter#notifyChanged(org.eclipse.emf.common.notify.Notification)
	 */
	public void notifyChanged(Notification msg) {
		if (fDecoder.getBeanModel() == null
			|| fDecoder.getBeanPart().getModel().isStateSet(IBeanDeclModel.BDM_STATE_UPDATING_JVE_MODEL)
			|| fDecoder.getBeanPart().getModel().isStateSet(IBeanDeclModel.BDM_STATE_SNIPPET)||
			msg.getEventType() == Notification.REMOVING_ADAPTER)
			return;
			
		switch (msg.getEventType()) {
			case Notification.ADD_MANY :
			case Notification.REMOVE_MANY :
				processMany(msg);
				return;
		}

		try {
			EObject val = (EObject) (msg.getNewValue() == null ? msg.getOldValue() : msg.getNewValue());
			if (val == null)
				return; // Should never be the case

			switch (msg.getEventType()) {

				case Notification.REMOVE :
				case Notification.ADD :

					if (val instanceof Callback) {
						if (msg.getEventType() == Notification.REMOVE)
							fDecoder.removeCallBack((Callback) msg.getOldValue());
						else if (msg.getEventType() == Notification.ADD)
							fDecoder.addCallBack((Callback) msg.getNewValue());
					}
					else if (val instanceof PropertyEvent){
						if (msg.getEventType() == Notification.REMOVE)
							fDecoder.removePropertyEvent((PropertyEvent) msg.getOldValue());
						else if (msg.getEventType() == Notification.ADD)
							fDecoder.addPropertyEvent((PropertyEvent) msg.getNewValue());
					}
					else
						fDecoder.reflectMOFchange();
					break;
				case Notification.UNSET :
				case Notification.SET :
				case Notification.MOVE :
					fDecoder.reflectMOFchange();
					break;

			}

		}
		catch (Throwable t) {
			JavaVEPlugin.log(t, Level.WARNING);
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.emf.common.notify.Adapter#setTarget(org.eclipse.emf.common.notify.Notifier)
	 */
	public void setTarget(Notifier arg0) {
		fTarget = (AbstractEventInvocation)arg0 ;
	}
	
	public IEventDecoder getDecoder() {
		return fDecoder;
	}
	/**
	 * Returns a String that represents the value of this object.
	 * @return a string representation of the receiver
	 */
	public String toString() {
		return "\tEventDecoderAdapter:" + fDecoder; //$NON-NLS-1$
	}
	
	public ICodeGenAdapter getCallBackSourceRangeAdapter(final Callback cb) {
		return new ICodeGenAdapter() {								
			public ICodeGenSourceRange getHighlightSourceRange() throws org.eclipse.ve.internal.java.codegen.util.CodeGenException {
				return getJavaSourceRange();
			}
			public org.eclipse.ve.internal.java.codegen.java.ICodeGenSourceRange getJavaSourceRange() throws org.eclipse.ve.internal.java.codegen.util.CodeGenException {
				return fDecoder.getCallBackSourceRange(cb) ;
				// Relative offset to the main expression				
			}
			public org.eclipse.ve.internal.java.codegen.java.ICodeGenSourceRange getBDMSourceRange() throws org.eclipse.ve.internal.java.codegen.util.CodeGenException {
				return null;
			}
			public void notifyChanged(org.eclipse.emf.common.notify.Notification notification) {}
			public org.eclipse.emf.common.notify.Notifier getTarget() {return this.getTarget();}
			public void setTarget(org.eclipse.emf.common.notify.Notifier newTarget) {}

			public boolean isAdapterForType(java.lang.Object type) { 
				return JVE_CODE_GEN_TYPE.equals(type) ;
			}
		}; 
	}
	
	public ICodeGenAdapter getPropertyEventSourceRangeAdapter(final PropertyEvent pe) {
			return new ICodeGenAdapter() {								
				public ICodeGenSourceRange getHighlightSourceRange() throws org.eclipse.ve.internal.java.codegen.util.CodeGenException {
					return getJavaSourceRange();
				}
				public org.eclipse.ve.internal.java.codegen.java.ICodeGenSourceRange getJavaSourceRange() throws org.eclipse.ve.internal.java.codegen.util.CodeGenException {
					return fDecoder.getPropertyEventSourceRange(pe) ;
					// Relative offset to the main expression				
				}
				public org.eclipse.ve.internal.java.codegen.java.ICodeGenSourceRange getBDMSourceRange() throws org.eclipse.ve.internal.java.codegen.util.CodeGenException {
					return null;
				}
				public void notifyChanged(org.eclipse.emf.common.notify.Notification notification) {}
				public org.eclipse.emf.common.notify.Notifier getTarget() {return this.getTarget();}
				public void setTarget(org.eclipse.emf.common.notify.Notifier newTarget) {}

				public boolean isAdapterForType(java.lang.Object type) { 
					return JVE_CODE_GEN_TYPE.equals(type) ;
				}
			}; 
		}

}
