package org.eclipse.ve.internal.cde.emf;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ClassDecoratorIterator.java,v $
 *  $Revision: 1.3 $  $Date: 2005-06-21 21:50:16 $ 
 */

import java.text.MessageFormat;
import java.util.*;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;

/**
 * This is an iterator to walk over the eAnnotations.
 * It walks through the supertype hierarchy starting at the
 * given EModelElement. Subclasses go search on type of EAnnotation, or by the source of the EAnnotation.
 */
public abstract class ClassDecoratorIterator implements Iterator {
	protected ListIterator fSuperItr;
	protected EAnnotation fNext; // We always go one forward to simplify the hasNext().

	/*
	 * subclasses in their ctor must call initialize(startWith) for this to work correctly.
	 */ 
	protected ClassDecoratorIterator(EClassifier startWith) {
		if (startWith instanceof EClass) {
			// The list is backwards, from top to bottom, so we need to walk up from the end.
			List allsupers = ((EClass) startWith).getEAllSuperTypes();
			fSuperItr = allsupers.listIterator(allsupers.size());
		}
	}

	protected void initialize(EClassifier startWith) {
		fNext = findDecorator(startWith);
		if (fNext == null) {
			fNext = findNext(); // Find the next one from the superType iterator
		}
	}

	protected abstract EAnnotation findDecorator(EClassifier eclassifier);

	public boolean hasNext() {
		return fNext != null;
	}

	public Object next() {
		Object result = fNext;
		fNext = findNext();
		return result;
	}

	public void remove() {
		throw new UnsupportedOperationException(MessageFormat.format(CDEEmfMessages.ClassDecoratorIterator_UnsupportedRemove_Object__EXC_, new Object[] { getClass().getName()})); 
	}

	/**
	 * From the current supertype iterator, find the next matching decorator, if any.
	 */
	protected EAnnotation findNext() {
		if (fSuperItr == null)
			return null; // This wasn't a EGeneralizableElement, so there aren't any supertypes.
		if (fSuperItr.hasPrevious()) {
			EAnnotation next = findDecorator((EClassifier) fSuperItr.previous());
			if (next == null)
				return findNext(); // This class didn't have one, go on.
			return next;
		}
		return null;
	}
}