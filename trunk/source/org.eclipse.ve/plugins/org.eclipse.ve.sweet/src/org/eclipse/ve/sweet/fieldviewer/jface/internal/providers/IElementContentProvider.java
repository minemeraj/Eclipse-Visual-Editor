package org.eclipse.ve.sweet.fieldviewer.jface.internal.providers;

import org.eclipse.jface.viewers.IContentProvider;

public interface IElementContentProvider extends IContentProvider {
	
	Object getElement(Object input);

}
