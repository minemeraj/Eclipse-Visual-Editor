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
 *  $RCSfile: DecoratedPropertyDescriptorAdapter.java,v $
 *  $Revision: 1.11 $  $Date: 2005-08-24 23:12:48 $ 
 */

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.ve.internal.cde.core.*;
import org.eclipse.ve.internal.cde.decorators.*;
import org.eclipse.ve.internal.cde.emf.ClassDecoratorFeatureAccess;
import org.eclipse.ve.internal.cde.emf.ClassDescriptorDecoratorPolicy;
import org.eclipse.ve.internal.cde.utility.AbstractString;
import org.eclipse.ve.internal.propertysheet.*;

/**
 * This is class is used when the decorator is the standard CDE FeatureDescriptorDecorator.
 */
public class DecoratedPropertyDescriptorAdapter extends AbstractPropertyDescriptorAdapter implements IEToolsPropertyDescriptor {
	/*
	 * This is a label provider for types where none was specifically provided. It will find
	 * the default BasePropertyDecorator entry in the current domain. If it doesn't find
	 * one it will use the default ClassDescriptorDecorator to find the default label provider
	 * for the domain (except it won't use the icon from that one).
	 */
	private class TypeLabelProvider extends LabelProvider implements INeedData {
		EditDomain currentDomain;
		ILabelProvider provider;
		boolean doImage;

		public void setData(Object data) {
			if (!(data instanceof EditDomain) || currentDomain == data)
				return;

			currentDomain = (EditDomain) data;
			provider = null;
			doImage = false;
			BasePropertyDecorator baseDecor =
				(BasePropertyDecorator) ClassDescriptorDecoratorPolicy.getPolicy(currentDomain).getDefaultDecorator(
					BasePropertyDecorator.class);
			if (baseDecor != null) {
				if (baseDecor.isSetLabelProviderClassname()) {
					try {
						String classNameAndData = baseDecor.getLabelProviderClassname();
						if (classNameAndData == null)
							return; // Explicitly set to no label provider.
						Class providerClass = CDEPlugin.getClassFromString(classNameAndData);
						provider =
							createLabelProviderInstance(providerClass, classNameAndData, null, DecoratedPropertyDescriptorAdapter.this);
						doImage = true;
						if (provider instanceof INeedData)
							 ((INeedData) provider).setData(currentDomain);
						return;
					} catch (ClassNotFoundException e) {
						// One specified, but incorrect, log it, but continue and see if we can get another way.
						CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", e)); //$NON-NLS-1$
					}
				}
			}

			// Now try for the class.		
			provider = ClassDescriptorDecoratorPolicy.getPolicy(currentDomain).getLabelProvider(null);
			// Get the default class label provider.
			if (provider instanceof INeedData)
				 ((INeedData) provider).setData(currentDomain);
		}

		public String getText(Object element) {
			if (provider == null)
				return super.getText(element);

			return provider.getText(element);
		}

		public Image getImage(Object element) {
			if (!doImage || provider == null)
				return super.getImage(element);

			return provider.getImage(element);
		}

		public void dispose() {
			if (provider != null) {
				provider.dispose();
				provider = null;
			}
		}
	}

	protected FeatureDescriptorDecorator getFeatureDecorator() {
		// Return the FeatureDescriptorDecorator for this feature.
		return (FeatureDescriptorDecorator) CDEUtilities.findDecorator((EModelElement) target, FeatureDescriptorDecorator.class);
	}

	protected PropertyDescriptorDecorator getPropertyDecorator() {
		// Return the PropertyDescriptorDecorator for this feature.
		return (PropertyDescriptorDecorator) CDEUtilities.findDecorator((EModelElement) target, PropertyDescriptorDecorator.class);
	}

	protected BasePropertyDecorator getBaseDecorator() {
		// Return the BasePropertyDecorator for this feature.
		return (BasePropertyDecorator) CDEUtilities.findDecorator((EModelElement) target, BasePropertyDecorator.class);
	}

	public String getDescription() {
		FeatureDescriptorDecorator dec = getFeatureDecorator();
		return (dec != null && dec.getDescriptionString() != null) ? dec.getDescriptionString().getStringValue() : null;
	}

	public ICellEditorValidator getValidator() {

		// Step 1 - See if the base decorator on the feature has it.
		BasePropertyDecorator decorator = getBaseDecorator();
		if (decorator != null && !decorator.getCellEditorValidatorClassnames().isEmpty()) {
			try {
				ICellEditorValidator[] validators = null;
				if (!decorator.getCellEditorValidatorClassnames().isEmpty()) {
					validators = new ICellEditorValidator[decorator.getCellEditorValidatorClassnames().size()];
					Iterator itr = decorator.getCellEditorValidatorClassnames().iterator();
					for (int i = 0; itr.hasNext(); i++)
						validators[i] = (ICellEditorValidator) CDEPlugin.createInstance(null, (String) itr.next());
				}

				return (validators.length == 1 ? validators[0] : new DefaultWrapperedValidator(validators));
			} catch (Exception e) {
				String msg =
					MessageFormat.format(
						CDEMessages.Object_noinstantiate_EXC_, 
						new Object[] { decorator.getCellEditorValidatorClassnames()});
				CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, e));
			}
		}

		// Step 2 - If not on feature, then get it from the type, look for BasePropertyDecorator on the type.
		BasePropertyDecorator bdec =
			(BasePropertyDecorator) ClassDecoratorFeatureAccess.getDecoratorWithFeature(
				((EStructuralFeature) target).getEType(),
				BasePropertyDecorator.class,
				DecoratorsPackage.eINSTANCE.getBasePropertyDecorator_CellEditorValidatorClassnames());
		if (bdec != null && !bdec.getCellEditorValidatorClassnames().isEmpty()) {
			try {
				ICellEditorValidator[] validators = null;
				if (!bdec.getCellEditorValidatorClassnames().isEmpty()) {
					validators = new ICellEditorValidator[bdec.getCellEditorValidatorClassnames().size()];
					Iterator itr = bdec.getCellEditorValidatorClassnames().iterator();
					for (int i = 0; itr.hasNext(); i++)
						validators[i] = (ICellEditorValidator) CDEPlugin.createInstance(null, (String) itr.next());
				}

				return (validators.length == 1 ? validators[0] : new DefaultWrapperedValidator(validators));
			} catch (Exception e) {
				String msg =
					MessageFormat.format(
						CDEMessages.Object_noinstantiate_EXC_, 
						new Object[] { bdec.getCellEditorValidatorClassnames()});
				CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, msg, e));
			}
		}

		return null;
	}

	public String[] getFilterFlags() {
		FeatureDescriptorDecorator dec = getFeatureDecorator();
		if (dec != null) {
			List fs = dec.getFilterFlagStrings();
			if (!fs.isEmpty()) {
				String[] flags = new String[fs.size()];
				Iterator itr = fs.iterator();
				int i = 0;
				while (itr.hasNext())
					flags[i++] = ((AbstractString) itr.next()).getStringValue();
				return flags;
			}
		}
		return null;
	}

	public String getCategory() {
		FeatureDescriptorDecorator dec = getFeatureDecorator();
		return (dec != null && dec.getCategoryString() != null) ? dec.getCategoryString().getStringValue() : null;
	}

	protected String primGetDisplayName() {
		FeatureDescriptorDecorator dec = getFeatureDecorator();
		return (dec != null && dec.getDisplayNameString() != null) ? dec.getDisplayNameString().getStringValue() : ((ENamedElement) target).getName();
	}

	public Object getHelpContextIds() {
		FeatureDescriptorDecorator dec = getFeatureDecorator();
		if (dec != null) {
			List hs = dec.getHelpContextIdsString();
			if (!hs.isEmpty()) {
				String[] hids = new String[hs.size()];
				Iterator itr = hs.iterator();
				int i = 0;
				while (itr.hasNext())
					hids[i++] = ((AbstractString) itr.next()).getStringValue();
				return hids;
			}
		}
		return null;
	}

	public CellEditor createPropertyEditor(Composite parent) {

		if (isReadOnly())
			return null; // The feature is a read-only, so no editor to allow changes

		Class editorClass = null;
		String classNameAndData = ""; //$NON-NLS-1$

		// Step 1 - See if the base decorator on the feature has it.
		BasePropertyDecorator decorator = getBaseDecorator();
		if (decorator != null && decorator.isSetCellEditorClassname()) {
			try {
				classNameAndData = decorator.getCellEditorClassname();
				if (classNameAndData == null)
					return null; // Explicitly set to no cell editor.
				editorClass = CDEPlugin.getClassFromString(classNameAndData);
			} catch (ClassNotFoundException e) {
				// One specified, but incorrect, log it, but continue and see if we can get another way.
				CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", e)); //$NON-NLS-1$
			}
		}

		// Step 2 - If not on feature, then get it from the type, look for BasePropertyDecorator.
		if (editorClass == null) {
			BasePropertyDecorator bdec =
				(BasePropertyDecorator) ClassDecoratorFeatureAccess.getDecoratorWithFeature(
					((EStructuralFeature) target).getEType(),
					BasePropertyDecorator.class,
					DecoratorsPackage.eINSTANCE.getBasePropertyDecorator_CellEditorClassname());
			if (bdec != null && bdec.isSetCellEditorClassname()) {
				try {
					classNameAndData = bdec.getCellEditorClassname();
					if (classNameAndData == null)
						return null; // Explicitly set to no cell editor.
					editorClass = CDEPlugin.getClassFromString(classNameAndData);
				} catch (ClassNotFoundException e) {
					// One specified, but incorrect, log it, but continue and see if we can get another way.
					CDEPlugin.getPlugin().getLog().log(new Status(IStatus.WARNING, CDEPlugin.getPlugin().getPluginID(), 0, "", e)); //$NON-NLS-1$
				}
			}

		}

		if (editorClass == null)
			return null; // Couldn't find one either on the feature or on the type.

		return createCellEditorInstance(editorClass, parent, classNameAndData, null);
	}

	/**
	 * One will be created each time. It is the callers responsibility to dispose
	 * it when no longer needed.
	 */
	public ILabelProvider getLabelProvider() {

		// Step 1 - See if the base decorator on the feature has it.
		BasePropertyDecorator decorator = getBaseDecorator();
		if (decorator != null && decorator.isSetLabelProviderClassname()) {
			return decorator.getLabelProvider(this);
		}

		BasePropertyDecorator bdec =
			(BasePropertyDecorator) ClassDecoratorFeatureAccess.getDecoratorWithFeature(
				((EStructuralFeature) target).getEType(),
				BasePropertyDecorator.class,
				DecoratorsPackage.eINSTANCE.getBasePropertyDecorator_LabelProviderClassname());
		if (bdec != null && bdec.isSetLabelProviderClassname()) {
			return bdec.getLabelProvider(this);
		}

		return new TypeLabelProvider(); // Couldn't find one either on the feature or on the type. Return the default one.

	}

	/**
	 * They are considered compatible if they are identical and the decor says not always incompatible.
	 */
	public boolean isCompatibleWith(IPropertyDescriptor anotherProperty) {
		PropertyDescriptorDecorator decor = getPropertyDecorator();
		return this.equals(anotherProperty) && (decor == null || !decor.isAlwaysIncompatible());
	}

	public boolean isExpandable() {

		// Step 1 - See if the base decorator on the feature has it.
		BasePropertyDecorator decorator = getBaseDecorator();
		if (decorator != null && decorator.isSetEntryExpandable())
			return decorator.isEntryExpandable();

		// Step 2 - If not on feature, then get it from the type, look for BasePropertyDecorator.
		BasePropertyDecorator bdec =
			(BasePropertyDecorator) ClassDecoratorFeatureAccess.getDecoratorWithFeature(
				((EStructuralFeature) target).getEType(),
				BasePropertyDecorator.class,
				DecoratorsPackage.eINSTANCE.getBasePropertyDecorator_EntryExpandable());
		if (bdec != null)
			return bdec.isEntryExpandable();

		return true;
	}

	public boolean areNullsInvalid() {

		// Step 1 - See if the base decorator on the feature has it.
		BasePropertyDecorator decorator = getBaseDecorator();
		if (decorator != null && decorator.isSetNullInvalid())
			return decorator.isNullInvalid();

		// Step 2 - If not on feature, then get it from the type, look for BasePropertyDecorator.
		BasePropertyDecorator bdec =
			(BasePropertyDecorator) ClassDecoratorFeatureAccess.getDecoratorWithFeature(
				((EStructuralFeature) target).getEType(),
				BasePropertyDecorator.class,
				DecoratorsPackage.eINSTANCE.getBasePropertyDecorator_NullInvalid());
		if (bdec != null)
			return bdec.isNullInvalid();

		return false;
	}

	public boolean isReadOnly() {
		return !(((EStructuralFeature) getTarget()).isChangeable());
	}
}
