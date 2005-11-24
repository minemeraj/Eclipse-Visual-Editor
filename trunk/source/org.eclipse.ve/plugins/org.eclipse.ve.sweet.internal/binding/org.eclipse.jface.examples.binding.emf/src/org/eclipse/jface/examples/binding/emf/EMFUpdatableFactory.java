/**
 * 
 */
package org.eclipse.jface.examples.binding.emf;

import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.databinding.IDataBindingContext;
import org.eclipse.jface.databinding.IUpdatable;
import org.eclipse.jface.databinding.IUpdatableFactory;
import org.eclipse.jface.databinding.Property;
import org.eclipse.jface.examples.binding.emf.internal.EMFUpdatableCollection;
import org.eclipse.jface.examples.binding.emf.internal.EMFUpdatableEList;
import org.eclipse.jface.examples.binding.emf.internal.EMFUpdatableValue;

public class EMFUpdatableFactory implements IUpdatableFactory {
	public IUpdatable createUpdatable(Map properties,
			Object description, IDataBindingContext bindingContext) {
		if (description instanceof Property) {
			Property propertyDescription = (Property) description;
			if (propertyDescription.getObject() instanceof EObject) {
				EObject eObject = (EObject) propertyDescription
						.getObject();
				EStructuralFeature attr;
				if (propertyDescription.getPropertyID() instanceof EStructuralFeature)
					attr = (EStructuralFeature) propertyDescription
							.getPropertyID();
				else
					attr = eObject.eClass().getEStructuralFeature(
							(String) propertyDescription
									.getPropertyID());
				if (attr.isMany()) {
					return new EMFUpdatableCollection(eObject, attr,
							!attr.isChangeable());
				} else
					return new EMFUpdatableValue(eObject, attr, !attr
							.isChangeable());
			}
			if (propertyDescription.getObject() instanceof EList) {
				return new EMFUpdatableEList(
						(EList) propertyDescription.getObject(), true);
			}
		}
		return null;
	}
}