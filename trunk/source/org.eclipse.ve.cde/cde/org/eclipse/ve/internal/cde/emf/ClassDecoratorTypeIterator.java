package org.eclipse.ve.internal.cde.emf;
/*******************************************************************************
 * Copyright (c) 2001, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
/*
 *  $RCSfile: ClassDecoratorTypeIterator.java,v $
 *  $Revision: 1.1 $  $Date: 2003-10-27 17:37:07 $ 
 */

import java.util.Iterator;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClassifier;

/**
 * This is an iterator to walk over the eAnnotations.
 * It walks through the supertype hierarchy starting at the
 * given EModelElement. Subclasses go search on java.class type of EAnnotation.
 */
public class ClassDecoratorTypeIterator extends ClassDecoratorIterator {
	protected Class fDecoratorType;

	public ClassDecoratorTypeIterator(EClassifier startWith, Class decoratorType) {
		super(startWith);
		fDecoratorType = decoratorType;
		initialize(startWith);
	}

	protected EAnnotation findDecorator(EClassifier eClassifier) {
		Iterator decorItr = eClassifier.getEAnnotations().iterator();
		while (decorItr.hasNext()) {
			EAnnotation decor = (EAnnotation) decorItr.next();
			if (fDecoratorType.isInstance(decor))
				return decor;
		}
		return null;
	}
}