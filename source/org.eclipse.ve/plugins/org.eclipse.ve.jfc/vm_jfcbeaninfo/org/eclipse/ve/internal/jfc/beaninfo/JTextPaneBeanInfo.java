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
 *  $RCSfile: JTextPaneBeanInfo.java,v $
 *  $Revision: 1.6 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

public class JTextPaneBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JTextPaneMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jtextpane");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JTextPane.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JTextPaneMessages.getString("JTextPane.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JTextPaneMessages.getString("JTextPane.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/jtxtpn32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/jtxtpn16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
	} catch (Throwable exception) {
		handleException(exception);
	};
	return aDescriptor;
}
/**
 * Return the event set descriptors for this bean.
 * @return java.beans.EventSetDescriptor[]
 */
public java.beans.EventSetDescriptor[] getEventSetDescriptors() {
	return (new EventSetDescriptor[0]);
}
/**
  * @return an icon of the specified kind for JButton
  */
public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("jtxtpn32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("jtxtpn16.gif"); //$NON-NLS-1$
   	return super.getIcon(kind);
}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// addStyle(String,Style)
			super.createMethodDescriptor(getBeanClass(),"addStyle",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextPaneMessages.getString("addStyle(String,Style).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Add a new style",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("name", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextPaneMessages.getString("addStyle(String,Style).name.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Name for the style",
	      				}),
	      			createParameterDescriptor("style", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextPaneMessages.getString("addStyle(String,Style).style.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Parent style",
	      				})
	      		},
	      		new Class[] {
	      			String.class, javax.swing.text.Style.class
	      		}		    		
		  	),
		  	// getLogicalStyle()
			super.createMethodDescriptor(getBeanClass(),"getLogicalStyle",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextPaneMessages.getString("getLogicalStyle().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextPaneMessages.getString("getLogicalStyle().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getStyle(String)
			super.createMethodDescriptor(getBeanClass(),"getStyle",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextPaneMessages.getString("getStyle(String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get a named style previously added",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("name", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextPaneMessages.getString("getStyle(String).name.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Name for the style",
	      				})
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
			// getStyledDocument()
			super.createMethodDescriptor(getBeanClass(),"getStyledDocument",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextPaneMessages.getString("getStyledDocument().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextPaneMessages.getString("getStyledDocument().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// insertComponent(Component)
			super.createMethodDescriptor(getBeanClass(),"insertComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextPaneMessages.getString("insertComponent(String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Insert a component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("c", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextPaneMessages.getString("insertComponent(String).aComponent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Component to insert",
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Component.class
	      		}		    		
		  	),
		  	// insertIcon(Icon)
			super.createMethodDescriptor(getBeanClass(),"insertIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextPaneMessages.getString("insertIcon(Icon).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Insert an icon",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("g", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextPaneMessages.getString("insertIcon(Icon).anIcon.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Icon to insert",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.Icon.class
	      		}		    		
		  	),
		  	// removeStyle(String)
			super.createMethodDescriptor(getBeanClass(),"removeStyle",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextPaneMessages.getString("removeStyle(String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Remove a named style previously added",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("name", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextPaneMessages.getString("removeStyle(String).name.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Name for the style",
	      				})
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
			// setEditorKit(EditorKit)
			super.createMethodDescriptor(getBeanClass(),"setEditorKit",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextPaneMessages.getString("setEditorKit(EditorKit).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextPaneMessages.getString("setEditorKit(EditorKit).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("kit", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextPaneMessages.getString("setEditorKit(EditorKit).kit.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Editor kit",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.text.EditorKit.class
	      		}		    		
		  	),
			// setLogicalStyle(Style)
			super.createMethodDescriptor(getBeanClass(),"setLogicalStyle",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextPaneMessages.getString("setLogicalStyle(Style).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the logical style of the current paragraph",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	      			createParameterDescriptor("style", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextPaneMessages.getString("setLogicalStyle(Style).style.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "A named style",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.text.Style.class
	      		}		    		
		  	),
		  	// setStyledDocument(StyledDocument)
			super.createMethodDescriptor(getBeanClass(),"setStyledDocument",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextPaneMessages.getString("setStyledDocument(StyledDocument).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Associate the editor with a text document",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	      			createParameterDescriptor("doc", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextPaneMessages.getString("setStyledDocument(StyledDocument).doc.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "A text document",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.text.StyledDocument.class
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
		PropertyDescriptor aDescriptorList[] = {
			// editorKit
			super.createPropertyDescriptor(getBeanClass(),"editorKit", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextPaneMessages.getString("editorKit.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextPaneMessages.getString("editorKit.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	    		}
	    	),
			// logicalStyle
			super.createPropertyDescriptor(getBeanClass(),"logicalStyle", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JTextPaneMessages.getString("logicalStyle.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextPaneMessages.getString("logicalStyle.Desc"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	PREFERRED, Boolean.TRUE	      	
	    		}
	    	),
	    	// styledDocument
			super.createPropertyDescriptor(getBeanClass(),"styledDocument", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextPaneMessages.getString("styledDocument.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextPaneMessages.getString("styledDocument.Desc"), //$NON-NLS-1$
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE,
			EXPERT, Boolean.TRUE
	    		}
	    	)
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
}
