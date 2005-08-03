package org.eclipse.ve.sweet2;


public interface IObjectBinder {

	public final int COMMIT_MODIFY = 0;
	public final int COMMIT_FOCUS = 1;	
	public final int COMMIT_EXPLICIT = 2;

	IPropertyProvider getPropertyProvider(String propertyName);
	void setCommitPolicy(int commitPolicy);
	int getCommitPolicy();
	void commit();
	void setSource(Object aSource);

}
