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
package org.eclipse.ve.internal.cde.emf;
/*
 *  $RCSfile: ClassDecoratorSourceIterator.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.util.Iterator;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClassifier;

/**
 * This is an iterator to walk over the decorators of a specific kind, using the Annotation.getSource() as the key.
 * It walks through the supertype hierarchy starting at the
 * given EModelElement.
 */
public class ClassDecoratorSourceIterator extends ClassDecoratorIterator {
	protected String source;

	public ClassDecoratorSourceIterator(EClassifier startWith, String source) {
		super(startWith);
		this.source = source;
		initialize(startWith);
	}

	protected EAnnotation findDecorator(EClassifier eClassifier) {
		Iterator decorItr = eClassifier.getEAnnotations().iterator();
		while (decorItr.hasNext()) {
			EAnnotation decor = (EAnnotation) decorItr.next();
			if (source.equals(decor.getSource()))
				return decor;
		}
		return null;
	}
}
