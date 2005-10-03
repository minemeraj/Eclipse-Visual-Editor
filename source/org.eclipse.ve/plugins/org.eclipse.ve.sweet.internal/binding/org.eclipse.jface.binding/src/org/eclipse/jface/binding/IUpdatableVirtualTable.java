package org.eclipse.jface.binding;

public interface IUpdatableVirtualTable extends IUpdatable {

	public static interface IValuesCallback {
		public void valuesRequired(int fromIndex, int toIndex);
	}

	public int getSize();

	public void setSize();

	public Class[] getColumnTypes();

	public void clear(int fromIndex, int toIndex);

	public void add(int index);
	
	public void remove(int index);

	public Object[] getValues(int index);

	public void setValues(int index, Object[] values);

	public void addValueCallback(IValuesCallback callback);
}
