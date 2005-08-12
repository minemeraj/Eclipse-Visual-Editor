package org.eclipse.ve.sweet2;

import org.eclipse.jface.viewers.IContentProvider;

public interface IValueProvider extends IContentProvider , IValue {
	
	boolean canSetValue();

}
