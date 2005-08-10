package org.eclipse.ve.sweet2;

public interface Editor {
	
	public final int COMMIT_MODIFY = 0;
	public final int COMMIT_FOCUS = 1;	
	public final int COMMIT_EXPLICIT = 2;	
	
	void setUpdatePolicy(int updatePolicy);

}
