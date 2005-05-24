package org.eclipse.ve.internal.java.core;

import org.eclipse.swt.dnd.*;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.internal.ole.win32.COM;


public class JavaBeanTransfer extends Transfer {
	
	private static final String CF_UNICODETEXT = "CF_UNICODETEXT"; //$NON-NLS-1$
	private static final String CF_TEXT = "CF_TEXT"; //$NON-NLS-1$
	private static final int CF_UNICODETEXTID = COM.CF_UNICODETEXT;
	private static final int CF_TEXTID = COM.CF_TEXT;	

	public TransferData[] getSupportedTypes() {
		return null;
	}

	public boolean isSupportedType(TransferData transferData) {
		// TODO Auto-generated method stub
		return false;
	}


	protected int[] getTypeIds(){
		return new int[] {CF_UNICODETEXTID, CF_TEXTID};
	}

	protected String[] getTypeNames(){
		return new String[] {CF_UNICODETEXT, CF_TEXT};
	}		

	protected void javaToNative(Object object, TransferData transferData) {
		TextTransfer.getInstance().javaToNative(object,transferData);
	}

	protected Object nativeToJava(TransferData transferData) {
		return TextTransfer.getInstance().nativeToJava(transferData);
	}

}
