package org.eclipse.ve.sweet.fieldviewer.jface.internal;

public class EditorUtilities {
	
	public static String getMethodName(String propertyName){
		//TODO: <gm> need to deal with BeanInfo overrides </gm>
		StringBuffer getMethodName = new StringBuffer();
		getMethodName.append("get");
		getMethodName.append(propertyName.substring(0,1).toUpperCase());
		getMethodName.append(propertyName.substring(1));
		return getMethodName.toString();
	}
	
	public static String setMethodName(String propertyName){
		//TODO: <gm> need to deal with BeanInfo overrides </gm>
		StringBuffer getMethodName = new StringBuffer();
		getMethodName.append("set");
		getMethodName.append(propertyName.substring(0,1).toUpperCase());
		getMethodName.append(propertyName.substring(1));
		return getMethodName.toString();
	}	

}
