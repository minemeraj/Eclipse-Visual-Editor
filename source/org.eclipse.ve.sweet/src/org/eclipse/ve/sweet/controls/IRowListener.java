package org.eclipse.ve.sweet.controls;

public interface IRowListener {

	boolean requestRowChange(CompositeTable parent);

	void rowChanged(CompositeTable parent);

}
