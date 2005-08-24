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
package org.eclipse.ve.internal.cde.properties;
/*
 *  $RCSfile: NameInCompositionPropertyDescriptor.java,v $
 *  $Revision: 1.10 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.util.*;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import org.eclipse.ve.internal.cde.core.CDEMessages;
import org.eclipse.ve.internal.cde.core.EditDomain;
import org.eclipse.ve.internal.cdm.Annotation;
import org.eclipse.ve.internal.propertysheet.*;
/**
 * Property Descriptor for Name in Composition.
 */
public class NameInCompositionPropertyDescriptor extends AbstractAnnotationPropertyDescriptor {

	public static final String NAME_IN_COMPOSITION_KEY = "org.eclipse.ve.internal.cde.core.nameincomposition"; // Key for KeyedString for name in composition. //$NON-NLS-1$

	public NameInCompositionPropertyDescriptor() {
		this(CDEMessages.PropertyDescriptor_NameInComposition_DisplayName); 
	}

	public NameInCompositionPropertyDescriptor(String displayNameToUse) {
		this(displayNameToUse, null);
	}

	public NameInCompositionPropertyDescriptor(String displayNameToUse, ICellEditorValidator additionalValidator) {
		super(NAME_IN_COMPOSITION_KEY, displayNameToUse);
		setValidator(additionalValidator);
	}
	/* (non-Javadoc)
	 * @see org.eclipse.ve.internal.propertysheet.INeedData#setData(java.lang.Object)
	 */
	public void setData(Object data) {
		super.setData(data);
		if (!getAlwaysIncompatible()) {
			// We haven't yet set always incompatible, so we haven't set out validator yet.
			setValidator(
					getValidator() != null
						? (ICellEditorValidator) new DefaultWrapperedValidator(new ICellEditorValidator[] { getNameValidator(),
							getValidator()})
						: getNameValidator());
			// Can only be one. Doesn't make sense to allow changing the name of more than one to the same name, since the validator needs to make it unique.		
			setAlwaysIncompatible(true);
		}
		
	}
	
	/**
	 * Get the name validator to use. Subclasses should override and return thier own. 
	 * The default one makes it unique within entire compostion.
	 * 
	 * @return name validator to use.
	 * 
	 * @since 1.0.0
	 */
	protected NameValidator getNameValidator() {
		return new NameValidator();
	}

	protected Object getKeyedValue(BasicEMap.Entry kv) {
		return kv.getValue();
	}

	protected boolean isSetKeyedValue(BasicEMap.Entry kv) {
		return kv.getValue() != null;
	}

	protected BasicEMap.Entry getSetKeyedValue(BasicEMap.Entry oldKV, Object setValue) {
		Object oldValue = oldKV.getValue();
		if (oldValue == setValue || (oldValue != null && oldValue.equals(setValue)))
			return oldKV; // They haven't changed

		EStringToStringMapEntryImpl ks =
			(EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
		ks.setKey(NAME_IN_COMPOSITION_KEY);
		if (setValue != null && ((String) setValue).length() > 0)
			ks.setValue(setValue);
		return ks;
	}

	protected BasicEMap.Entry getUnsetKeyedValue() {
		EStringToStringMapEntryImpl ks =
			(EStringToStringMapEntryImpl) EcoreFactory.eINSTANCE.create(EcorePackage.eINSTANCE.getEStringToStringMapEntry());
		ks.setKey(NAME_IN_COMPOSITION_KEY);
		return ks;
	}

	/**
	 * Get a unique name in composition using the given base name.
	 */
	public static String getUniqueNameInComposition(EditDomain domain, String name) {
		return getUniqueNameInComposition(domain, name, null);
	}
	
	/**
	 * Get a unique name in composition using the given base name. It will also
	 * look in the Set of other names if the set is not null. This allows for checking
	 * for an add of group at once, so that they also don't duplicate themselves.
	 */
	public static String getUniqueNameInComposition(EditDomain domain, String name, Set otherNames) {
		String baseName = null;
		if (name != null)
			baseName = name;
		else
			baseName = CDEMessages.PropertyDescriptor_NameInComposition_Default; // Use a default. //$NON-NLS-1$
		String componentName = baseName;
		int incr = 0;
		main : while (true) {
			if (otherNames != null && otherNames.contains(componentName)) {
				componentName = baseName + ++incr;
				continue;
			}
			Iterator itr = domain.getDiagramData().getAnnotations().iterator();
			while (itr.hasNext()) {
				Annotation an = (Annotation) itr.next();
				BasicEMap.Entry ks = getMapEntry(an, NAME_IN_COMPOSITION_KEY);
				if (ks != null && componentName.equals(ks.getValue())) {
					componentName = baseName + ++incr;
					continue main;
				}
			}
			break;
		}

		return componentName;
	}

	public CellEditor createPropertyEditor(Composite parent) {
		if (!isReadOnly()) {
			CellEditor editor = new StringCellEditor(parent);
			ICellEditorValidator v = getValidator();
			if (v != null)
				editor.setValidator(v);
			return editor;
		} else
			return null;
	}

	public static class NameValidator implements ICellEditorValidator, INeedData, ISourced {
		protected EditDomain domain;
		protected IPropertySource[] pos;
		protected IPropertyDescriptor[] des;

		public void setData(Object data) {
			domain = (EditDomain) data;
		}

		// Is the name valid. It is valid if the name is unique in the composition.
		public String isValid(Object value) {
			if (value == null)
				return null; // Null is valid, it means not set.

			String name = (String) value;

			if (name.equals(getCurrentName()))
				return null; // The current name is considered valid.

			String newName = getSuggestedName(name);
			if (newName.equals(name))
				return null; // The name didn't change, so it is valid.

			return CDEMessages.PropertyDescriptor_NameInComposition_NonUnique_INFO_; 
		}

		/**
		 * Get the suggested name. Overrides may come up with a different one.
		 * This default implementation makes the name unique in the entire composition.
		 * 
		 * @param name
		 * @return the name to use based upon input name.
		 * 
		 * @since 1.0.0
		 */
		protected String getSuggestedName(String name) {
			return getUniqueNameInComposition(domain, name);
		}

		private String getCurrentName() {
			// Some may use special ways of getting the name in composition for the current source, so we will
			// use the property source and the property descriptor to get the name.
			// There will only be one, can't handle more than one source, it doesn't make sense because since
			// they need to be unique, we can't set more than one to the same name.
			if (des[0] instanceof ISourcedPropertyDescriptor)
				return (String) ((ISourcedPropertyDescriptor) des[0]).getValue(pos[0]);
			else
				return (String) pos[0].getPropertyValue(des[0]);
		}

		public void setSources(Object[] sources, IPropertySource[] pos, IPropertyDescriptor[] des) {
			this.pos = pos;
			this.des = des;
		}
	}

	/**
	 * Returns the list of unique names for names and annotations passed in.
	 * 
	 * @param domain
	 * @param names  List of names of the passed in annotations
	 * @param annotations  List of annotations
	 * @return  unique names calculated from the names passed in
	 * 
	 * @since 1.1
	 */
	public String[] getUniqueNamesInComposition(EditDomain domain, String[] names, Annotation[] annotations) {
		HashSet addedNames = new HashSet(names==null?0:names.length); //store already added names
		String uniqueNames[] = new String[names==null?0:names.length];
		for (int ac = 0; ac < uniqueNames.length; ac++) {
			uniqueNames[ac] = getUniqueNameInComposition(domain, names[ac], addedNames);
		}
		return uniqueNames;
	}

	public boolean isPropertyResettable(IPropertySource source) {
		return false;	// Name is never resettable. It doesn't make sense.
	}
}
