package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.IContentProvider;

public interface IElementContentProvider extends IContentProvider {
	
	Object getElement(Object input);

}
