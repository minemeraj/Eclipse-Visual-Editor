/*******************************************************************************
 * Copyright (c) 2001, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ve.internal.jfc.beaninfo;
/*
 *  $RCSfile: TextComponentBeanInfo.java,v $
 *  $Revision: 1.3 $  $Date: 2005-08-24 23:38:12 $ 
 */

import java.beans.*;

public class TextComponentBeanInfo extends IvjBeanInfo {
	private static java.util.ResourceBundle restextcomponent = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.textcomponent");  //$NON-NLS-1$
	
	
public Class getBeanClass() {
	return java.awt.TextComponent.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	try {
		/* Create and return the TextComponentBeanInfobean descriptor. */
		aDescriptor = new java.beans.BeanDescriptor(java.awt.TextComponent.class);
		aDescriptor.setDisplayName(restextcomponent.getString("TextComponentDN")); //$NON-NLS-1$
		aDescriptor.setShortDescription(restextcomponent.getString("TextComponentSD")); //$NON-NLS-1$
	} catch (Throwable exception) {
	};
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	try {
		java.beans.EventSetDescriptor aDescriptorList[] = {
			textEventSetDescriptor()
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		java.beans.MethodDescriptor aDescriptorList[] = {
			// getCaretPosition()
			super.createMethodDescriptor(getBeanClass(),"getCaretPosition", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getCaretPosition()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextcomponent.getString("getCaretPosition()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getSelectedText()
			super.createMethodDescriptor(getBeanClass(),"getSelectedText", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getSelectedText()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextcomponent.getString("getSelectedText()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getSelectionEnd()
			super.createMethodDescriptor(getBeanClass(),"getSelectionEnd", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getSelectionEnd()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextcomponent.getString("getSelectionEnd()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getSelectionStart()
			super.createMethodDescriptor(getBeanClass(),"getSelectionStart", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getSelectionStart()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextcomponent.getString("getSelectionStart()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// getText()
			super.createMethodDescriptor(getBeanClass(),"getText", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "getText()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextcomponent.getString("getText()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// isEditable()
			super.createMethodDescriptor(getBeanClass(),"isEditable", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "isEditable()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextcomponent.getString("isEditable()SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// removeNotify()
			super.createMethodDescriptor(getBeanClass(),"removeNotify", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "removeNotify()",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Remove the peer",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// select(int,int)
			super.createMethodDescriptor(getBeanClass(),"select", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "select(int,int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextcomponent.getString("select(int,int)SD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextcomponent.getString("selectionStartParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Start position",
	      			}
	      		),
	      		createParameterDescriptor("arg2", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextcomponent.getString("selectionEndParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "End position",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class, int.class 
	      	}	    		
		  	),
			// selectAll()
			super.createMethodDescriptor(getBeanClass(),"selectAll", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "selectAll()",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextcomponent.getString("selectAll()SD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      	new Class[] {}		    		
		  	),
			// setCaretPosition(int)
			super.createMethodDescriptor(getBeanClass(),"setCaretPosition", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setCaretPosition(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextcomponent.getString("setCaretPosition(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextcomponent.getString("positionParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "caret position",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
			// setEditable(boolean)
			super.createMethodDescriptor(getBeanClass(),"setEditable", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setEditable(boolean)",//$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Make the text component editable or read-only",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextcomponent.getString("booleanParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "TRUE for editable",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		boolean.class 
	      	}	    		
		  	),
			// setSelectionEnd(int)
			super.createMethodDescriptor(getBeanClass(),"setSelectionEnd", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setSelectionEnd(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextcomponent.getString("setSelectionEnd(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextcomponent.getString("selectionEndParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "selection end position",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
			// setSelectionStart(int)
			super.createMethodDescriptor(getBeanClass(),"setSelectionStart", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setSelectionStart(int)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextcomponent.getString("setSelectionStart(int)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextcomponent.getString("selectionStartParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "selection start position",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		int.class 
	      	}	    		
		  	),
			// setText(String)
			super.createMethodDescriptor(getBeanClass(),"setText", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, "setText(String)",//$NON-NLS-1$
	      		SHORTDESCRIPTION, restextcomponent.getString("setText(String)SD"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("arg1", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextcomponent.getString("textParmDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "text",
	      			}
	      		)
	      	},
	      	new Class[] { 
	      		String.class 
	      	}	    		
		  	)
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
/**
 * Return the property descriptors for this bean.
 * @return java.beans.PropertyDescriptor[]
 */
public java.beans.PropertyDescriptor[] getPropertyDescriptors() {
	try {
		java.beans.PropertyDescriptor aDescriptorList[] = {
			// caretPosition
			super.createPropertyDescriptor(getBeanClass(),"caretPosition", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, restextcomponent.getString("caretPositionDN"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, restextcomponent.getString("caretPositionSD"), //$NON-NLS-1$
	    		}
	    	),			
			// editable
			super.createPropertyDescriptor(getBeanClass(),"editable", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, restextcomponent.getString("editableDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, restextcomponent.getString("editableSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			// selectedText
			super.createPropertyDescriptor(getBeanClass(),"selectedText", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, restextcomponent.getString("selectedTextDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, restextcomponent.getString("selectedTextSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			// selectionEnd
			super.createPropertyDescriptor(getBeanClass(),"selectionEnd", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, restextcomponent.getString("selectionEndDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, restextcomponent.getString("selectionEndSD"), //$NON-NLS-1$
	    		}
	    	),
			// selectionStart
			super.createPropertyDescriptor(getBeanClass(),"selectionStart", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, restextcomponent.getString("selectionStartDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, restextcomponent.getString("selectionStartSD"), //$NON-NLS-1$
	    		}
	    	),
			// text
			super.createPropertyDescriptor(getBeanClass(),"text", new Object[] {//$NON-NLS-1$
			DISPLAYNAME, restextcomponent.getString("textDN"),				 //$NON-NLS-1$
	      	SHORTDESCRIPTION, restextcomponent.getString("textSD"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	)
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
/**
 * Gets the textevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor textEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.TextEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.TextListener.class,
				"textValueChanged", //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, restextcomponent.getString("textValueChangedDN"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, restextcomponent.getString("textValueChangedSD"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("textEvent", new Object[] {//$NON-NLS-1$
	   				DISPLAYNAME, restextcomponent.getString("textEventDN"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "Event on changing text",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"text", new Object[] {//$NON-NLS-1$
						DISPLAYNAME, restextcomponent.getString("textEventsDN"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, restextcomponent.getString("textEventsSD"), //$NON-NLS-1$
	      				INDEFAULTEVENTSET, Boolean.TRUE,
	      			}, 
						aDescriptorList, java.awt.event.TextListener.class,
						"addTextListener", "removeTextListener");//$NON-NLS-2$//$NON-NLS-1$

	return aDescriptor;
}
}
