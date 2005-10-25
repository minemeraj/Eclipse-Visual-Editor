package org.eclipse.jface.examples.binding.emf;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jface.binding.IColumn;
 
public class EMFColumn implements IColumn {
	
	String attributeName;
	private EStructuralFeature attribute;
	
	public EMFColumn(String attributeName){
		this.attributeName = attributeName;
	}

	public void setValue(Object row, Object value) {
		if(attribute == null){
			attribute = ((EObject)row).eClass().getEStructuralFeature(attributeName);
		}
		((EObject)row).eSet(attribute,value);
	}

	public Object getValue(Object row) {
		if(attribute == null){
			attribute = ((EObject)row).eClass().getEStructuralFeature(attributeName);
		}
		return ((EObject)row).eGet(attribute);
	}

	public Class getType(Object rowClass) {
		if(attribute == null){
			attribute = ((EClass)rowClass).getEStructuralFeature(attributeName);
		}		
		return attribute.getEType().getInstanceClass();
	}
}
