/**
 * 
 */
package org.eclipse.ve.sweet2;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class ListContentProvider extends AbstractObjectContentProvider implements IStructuredContentProvider {
	
	public ListContentProvider(){
		super(null);
	}

	public ListContentProvider(String aPropertyName) {
		super(aPropertyName);
	}

	public Object[] getElements(Object inputElement) {		
		if(inputElement instanceof List){
			return ((List)inputElement).toArray();
		} else {
			Object result = lookupPropertyValue(inputElement);
			if (result==null)
				return new Object[0];
			if(result instanceof List){
				return ((List)result).toArray();
			} else {
				return (Object[])result;
			}
		}
	}

	
	
}