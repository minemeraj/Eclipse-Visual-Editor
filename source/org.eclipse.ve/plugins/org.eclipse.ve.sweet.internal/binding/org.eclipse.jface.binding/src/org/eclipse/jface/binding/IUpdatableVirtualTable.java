package org.eclipse.jface.binding;

public interface IUpdatableVirtualTable {

	public int getSize();
	
	public void setSize();
	
	public int getColumnCount();
	
	public void addColumn(int index);
	public void removeColumn(int index);
	
	public void clear(int index);

	public void clear(int fromIndex, int toIndex);

	public void clearAll();
	
	public void remove(int index);
	
	public void add(int index);
	
	public Object[] getValues(int index);
	
	public void setValues(int index, Object[] values);
	
	// TODO public void addValuesNeededListener(ISetValuesListener listener);
}
