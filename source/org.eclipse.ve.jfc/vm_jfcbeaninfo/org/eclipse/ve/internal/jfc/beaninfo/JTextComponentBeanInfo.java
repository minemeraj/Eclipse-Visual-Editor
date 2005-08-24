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
 *  $RCSfile: JTextComponentBeanInfo.java,v $
 *  $Revision: 1.11 $  $Date: 2005-08-24 23:38:12 $ 
 */

import java.beans.*;

import javax.swing.text.NavigationFilter;

public class JTextComponentBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JTextComponentMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jtextcomponent");  //$NON-NLS-1$

/**
 * Gets the itemevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public java.beans.EventSetDescriptor caretEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { javax.swing.event.CaretEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(javax.swing.event.CaretListener.class,
				"caretUpdate",  //$NON-NLS-1$
				new Object[] {
	   				DISPLAYNAME, JTextComponentMessages.getString("caretUpdate.Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JTextComponentMessages.getString("caretUpdate.Desc"), //$NON-NLS-1$
	      			PREFERRED, Boolean.TRUE
	    			}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("caretEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JTextComponentMessages.getString("caretEvent.Name"), //$NON-NLS-1$
	      			SHORTDESCRIPTION, JTextComponentMessages.getString("caretEvent.Desc"), //$NON-NLS-1$
	      			})
	      		},
	      		paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"caret", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JTextComponentMessages.getString("caretEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JTextComponentMessages.getString("caretEvents.Desc"), //$NON-NLS-1$
	      			}, 
						aDescriptorList, javax.swing.event.CaretListener.class,
						"addCaretListener", "removeCaretListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.text.JTextComponent.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JTextComponentMessages.getString("JTextComponent.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JTextComponentMessages.getString("JTextComponent.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/txtfld32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/txtfld16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	try {
		EventSetDescriptor aDescriptorList[] = {
			keyEventSetDescriptor(),
			caretEventSetDescriptor()
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
/**
  * @return an icon of the specified kind for JButton
  */
public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("txtfld32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("txtfld16.gif"); //$NON-NLS-1$
   	return super.getIcon(kind);
}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
		  	// addKeymap(String,Keymap)
			super.createMethodDescriptor(getBeanClass(),"addKeymap",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("addKeymap(String,Keymap).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Add a new keymap",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("name", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("addKeymap(String,Keymap).keymapName.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Name of keymap"
	      				}
	      			),
	    			createParameterDescriptor("parent", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("addKeymap(String,Keymap).parent.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "parent keymap",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			String.class,
	      			javax.swing.text.Keymap.class
	      		}		    		
		  	),
		  	// copy()
			super.createMethodDescriptor(getBeanClass(),"copy",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("copy().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextComponentMessages.getString("copy().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// cut()
			super.createMethodDescriptor(getBeanClass(),"cut",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("cut().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextComponentMessages.getString("cut().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getActions()
			super.createMethodDescriptor(getBeanClass(),"getActions",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getActions().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextComponentMessages.getString("getActions().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getCaret()
			super.createMethodDescriptor(getBeanClass(),"getCaret",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getCaret().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the text insertion caret",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getCaretColor()
			super.createMethodDescriptor(getBeanClass(),"getCaretColor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getCaretColor().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the caret color",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getCaretPosition()
			super.createMethodDescriptor(getBeanClass(),"getCaretPosition",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getCaretPosition().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextComponentMessages.getString("getCaretPosition().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDisabledTextColor()
			super.createMethodDescriptor(getBeanClass(),"getDisabledTextColor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getDisabledTextColor().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the color for disabled text",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDocument()
			super.createMethodDescriptor(getBeanClass(),"getDocument",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getDocument().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextComponentMessages.getString("getDocument().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getFocusAccelerator()
			super.createMethodDescriptor(getBeanClass(),"getFocusAccelerator",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getFocusAccelerator().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accelerator key to get the focus",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getHighlighter()
			super.createMethodDescriptor(getBeanClass(),"getHighlighter",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getHighlighter().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the delegate responsible for highlighting",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getKeymap()
			super.createMethodDescriptor(getBeanClass(),"getKeymap",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getKeymap().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the keymap",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getKeymap(String)
			super.createMethodDescriptor(getBeanClass(),"getKeymap",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getKeymap(String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the keymap at specified name"
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("name", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("getKeymap(String).name.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "name of keymap",
	      				})
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
		  	// getMargin()
			super.createMethodDescriptor(getBeanClass(),"getMargin",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getMargin().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextComponentMessages.getString("getMargin().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectedText()
			super.createMethodDescriptor(getBeanClass(),"getSelectedText",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getSelectedText().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the selected text",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectedTextColor()
			super.createMethodDescriptor(getBeanClass(),"getSelectedTextColor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getSelectedTextColor().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the color used for selected text",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionColor()
			super.createMethodDescriptor(getBeanClass(),"getSelectionColor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getSelectionColor().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the background color used for selection",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionEnd()
			super.createMethodDescriptor(getBeanClass(),"getSelectionEnd",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getSelectionEnd().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the end position of the selection",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionStart()
			super.createMethodDescriptor(getBeanClass(),"getSelectionStart",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getSelectionStart().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the start position of the selection",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getText()
			super.createMethodDescriptor(getBeanClass(),"getText",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getText().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the text",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getUI().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the ComboBoxUI object",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE	      		
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isEditable
			super.createMethodDescriptor(getBeanClass(),"isEditable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("isEditable.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextComponentMessages.getString("isEditable.Desc")	    		},  //$NON-NLS-1$
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// moveCaretPosition(int)
			super.createMethodDescriptor(getBeanClass(),"moveCaretPosition",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("moveCaretPosition(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Move caret to new position",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("pos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("moveCaretPosition(int).position.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Position in document",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// paste()
			super.createMethodDescriptor(getBeanClass(),"paste",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("paste().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextComponentMessages.getString("paste().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// read(Reader,Object)
			super.createMethodDescriptor(getBeanClass(),"read",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("read(Reader,Object).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Initialize model document from stream",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("reader", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("read(Reader,Object).reader.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Stream to read from"
	      				}),
	    			createParameterDescriptor("desc", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("read(Reader,Object).desc.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Object describing stream",
	      				})
	      		},
	      		new Class[] {
	      			java.io.Reader.class,
	      			java.lang.Object.class
	      		}		    		
		  	),
		  	// removeKeymap(String)
			super.createMethodDescriptor(getBeanClass(),"removeKeymap",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("removeKeymap(String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Remove the specified keymap",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("name", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("removeKeymap(String).keymapName.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Name of keymap"
	      				}),
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
		  	// replaceSelection(String)
			super.createMethodDescriptor(getBeanClass(),"replaceSelection",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("replaceSelection(String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Replace selection with specified text",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("text", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("replaceSelection(String).text.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "New text to replace selection"
	      				}),
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
		  	// select(int,int)
			super.createMethodDescriptor(getBeanClass(),"select",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("select(int,int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Select the text in specified interval",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("startPos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("select(int,int).startPosition.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Select start position",
	      				}),
	      			createParameterDescriptor("endPos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("select(int,int).endPosition.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Select end position",
	      				})
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// selectAll()
			super.createMethodDescriptor(getBeanClass(),"selectAll",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("selectAll().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextComponentMessages.getString("selectAll().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setCaret(Caret)
			super.createMethodDescriptor(getBeanClass(),"setCaret",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("setCaret(Caret).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the caret",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("caret", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("setCaret(Caret).caret.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Caret",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.text.Caret.class
	      		}		    		
		  	),
		  	// setCaretColor(Color)
			super.createMethodDescriptor(getBeanClass(),"setCaretColor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("setCaretColor(Color).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the caret color",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("color", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("setCaretColor(Color).color.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Caret color",
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Color.class
	      		}		    		
		  	),
		  	// setCaretPosition(int)
			super.createMethodDescriptor(getBeanClass(),"setCaretPosition",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("setCaretPosition(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set caret to new position",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("pos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("setCaretPosition(int).position.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Position in document",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setDisabledTextColor(Color)
			super.createMethodDescriptor(getBeanClass(),"setDisabledTextColor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("setDisabledTextColor(Color).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the disabled text color",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("color", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("setDisabledTextColor(Color).color.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Disabled text color",
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Color.class
	      		}		    		
		  	),
		  	// setDocument(Document)
			super.createMethodDescriptor(getBeanClass(),"setDocument",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("setDocument(Document).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the document",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("doc", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("setDocument(Document).document.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Text document",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.text.Document.class
	      		}		    		
		  	),
		  	// setFocusAccelerator(char)
			super.createMethodDescriptor(getBeanClass(),"setFocusAccelerator",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("setFocusAccelerator(char).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the ALT accelerator key used to get focus",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("char", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("setFocusAccelerator(char).char.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "ALT char for focus",
	      				})
	      		},
	      		new Class[] {
	      			char.class
	      		}		    		
		  	),
		  	// setHighlighter(Highlighter)
			super.createMethodDescriptor(getBeanClass(),"setHighlighter",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("setHighlighter(Highlighter).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the highlighter",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("h", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("setHighlighter(Highlighter).highlighter.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Highlighter object",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.text.Highlighter.class
	      		}		    		
		  	),
		  	// setMargin(Insets)
			super.createMethodDescriptor(getBeanClass(),"setMargin",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("setMargin(Insets).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the margin space between border and text",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("insets", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("setMargin(Insets).insets.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Insets",
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Insets.class
	      		}		    		
		  	),
		  	// setKeymap(Keymap)
			super.createMethodDescriptor(getBeanClass(),"setKeymap",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("setKeymap(Keymap).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the keymap",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("keymap", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("setKeymap(Keymap).keymap.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Keymap",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.text.Keymap.class
	      		}		    		
		  	),
		  	// setSelectedTextColor(Color)
			super.createMethodDescriptor(getBeanClass(),"setSelectedTextColor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("setSelectedTextColor(Color).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the selected text color",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("color", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("setSelectedTextColor(Color).color.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Text color",
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Color.class
	      		}		    		
		  	),
		  	// setSelectionColor(Color)
			super.createMethodDescriptor(getBeanClass(),"setSelectionColor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("setSelectionColor(Color).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextComponentMessages.getString("setSelectionColor(Color).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("color", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("setSelectionColor(Color).color.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Text color",
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Color.class
	      		}		    		
		  	),
			// setSelectionEnd(int)
			super.createMethodDescriptor(getBeanClass(),"setSelectionEnd",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("setSelectionEnd(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set selection end position",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("pos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("setSelectionEnd(int).position.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Position in document",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setSelectionStart(int)
			super.createMethodDescriptor(getBeanClass(),"setSelectionStart",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("setSelectionStart(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set selection start position",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("pos", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("setSelectionStart(int).position.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Position in document",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setText(String)
			super.createMethodDescriptor(getBeanClass(),"setText",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("setText(String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the text",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("string", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("setText(String).string.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Text for component"
	      				}),
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
		  	// setUI(TextUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("setUI(TextUI).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the text UI",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("ui", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("setUI(TextUI).anUI.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Text UI",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.plaf.TextUI.class
	      		}		    		
		  	),
		  	// write(Writer)
			super.createMethodDescriptor(getBeanClass(),"write",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("write(Writer).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Store contents into given stream",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("out", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("write(Writer).out.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Stream to write to"
	      				})
	      		},
	      		new Class[] {
	      			java.io.Writer.class
	      		}		    		
		  	),
		  	// getNavigationFilter()
			super.createMethodDescriptor(getBeanClass(),"getNavigationFilter",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("getNavigationFilter().Name"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setNavigationFilter(NavigationFilter)
			super.createMethodDescriptor(getBeanClass(),"setNavigationFilter",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("setNavigationFilter(NavigationFilter).Name"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("navigationFilter", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JTextComponentMessages.getString("setNavigationFilter(NavigationFilter).navigationFilter.Name"), //$NON-NLS-1$
	      				})
	      		},
	      		new Class[] {
					NavigationFilter.class
	      		}		    		
		  	),
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
			// accessibleContext
			super.createPropertyDescriptor(getBeanClass(),"accessibleContext", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("acessibleContext.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("acessibleContext.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	OBSCURE, Boolean.TRUE
	    		}
	    	),
			// actions
			super.createPropertyDescriptor(getBeanClass(),"actions", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("actions.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("actions.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// caret
			super.createPropertyDescriptor(getBeanClass(),"caret", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("caret.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("caret.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// caretColor
			super.createPropertyDescriptor(getBeanClass(),"caretColor", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("caretColor.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("caretColor.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// caretPosition
			super.createPropertyDescriptor(getBeanClass(),"caretPosition", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("caretPosition.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("caretPosition.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// disabledTextColor
			super.createPropertyDescriptor(getBeanClass(),"disabledTextColor", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("disabledTextColor.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("disabledTextColor.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// document
			super.createPropertyDescriptor(getBeanClass(),"document", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("document.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("document.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
			// editable
			super.createPropertyDescriptor(getBeanClass(),"editable", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("editable.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("editable.Desc"), //$NON-NLS-1$
	    		}
	    	),
			// focusAccelerator
			super.createPropertyDescriptor(getBeanClass(),"focusAccelerator", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("focusAccelerator.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("focusAccelerator.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// focusTraversable
			super.createPropertyDescriptor(getBeanClass(),"focusTraversable", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("focusTraversable.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("focusTraversable.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// highlighter
			super.createPropertyDescriptor(getBeanClass(),"highlighter", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("highlighter.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("highlighter.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// keymap
			super.createPropertyDescriptor(getBeanClass(),"keymap", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("keymap.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("keymap.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
			// layout - hide it
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// margin
			super.createPropertyDescriptor(getBeanClass(),"margin", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("margin.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("margin.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// selectedText
			super.createPropertyDescriptor(getBeanClass(),"selectedText", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("selectedText.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("selectedText.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE	      	
	    		}
	    	),
	    	// selectedTextColor
			super.createPropertyDescriptor(getBeanClass(),"selectedTextColor", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("selectedTextColor.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("selectedTextColor.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// selectionColor
			super.createPropertyDescriptor(getBeanClass(),"selectionColor", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("selectionColor.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("selectionColor.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE	      	
	    		}
	    	),
	    	// selectionEnd
			super.createPropertyDescriptor(getBeanClass(),"selectionEnd", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("selectionEnd.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("selectionEnd.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// selectionStart
			super.createPropertyDescriptor(getBeanClass(),"selectionStart", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("selectionStart.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("selectionStart.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// text
			super.createPropertyDescriptor(getBeanClass(),"text", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("text.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("text.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
	    	// navigationFilter
			super.createPropertyDescriptor(getBeanClass(),"navigationFilter", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("navigationFilter.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("navigationFilter.Desc"), //$NON-NLS-1$
			EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// ui
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JTextComponentMessages.getString("ui.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JTextComponentMessages.getString("ui.Desc"), //$NON-NLS-1$
			EXPERT, Boolean.TRUE,
			BOUND, Boolean.TRUE
	    		}
	    	),
		};
		return aDescriptorList;
	} catch (Throwable exception) {
		handleException(exception);
	};
	return null;
}
/**
 * Gets the keyevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor keyEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { java.awt.event.KeyEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(java.awt.event.KeyListener.class,
				"keyTyped",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("keyTyped.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextComponentMessages.getString("keyTyped.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("keyEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JTextComponentMessages.getString("keyTyped.keyEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "key typed event",
	      			}
	      		)
	      	},
	      	paramTypes	    		
		  	),
			super.createMethodDescriptor(java.awt.event.KeyListener.class,
				"keyPressed",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("keyPressed.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextComponentMessages.getString("keyPressed.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("keyEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JTextComponentMessages.getString("keyPressed.keyEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "key pressed event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	),
		  	super.createMethodDescriptor(java.awt.event.KeyListener.class,
				"keyReleased",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JTextComponentMessages.getString("keyReleased.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JTextComponentMessages.getString("keyReleased.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("keyEvent", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JTextComponentMessages.getString("keyReleased.keyEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "key released event",
	      			}
	      		)
	      	},
	      	paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"key", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JTextComponentMessages.getString("keyEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JTextComponentMessages.getString("keyEvents.Desc"), //$NON-NLS-1$
	      				INDEFAULTEVENTSET, Boolean.TRUE,
	      			}, 
						aDescriptorList, java.awt.event.KeyListener.class,
						"addKeyListener", "removeKeyListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
}
