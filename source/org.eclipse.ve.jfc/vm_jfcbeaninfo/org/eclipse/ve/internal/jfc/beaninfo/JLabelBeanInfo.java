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
 *  $RCSfile: JLabelBeanInfo.java,v $
 *  $Revision: 1.7 $  $Date: 2005-08-24 23:38:11 $ 
 */

import java.beans.*;

public class JLabelBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JLabelMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jlabel");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JLabel.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JLabelMessages.getString("JLabel.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JLabelMessages.getString("JLabel.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/label32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/label16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	return( new EventSetDescriptor[0]);
}
/**
  * @return an icon of the specified kind for JButton
  */
public java.awt.Image getIcon(int kind) 
{
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("label32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("label16.gif"); //$NON-NLS-1$
   	return super.getIcon(kind);
}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
		  	// getAccessibleContext()
			super.createMethodDescriptor(getBeanClass(),"getAccessibleContext",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible context",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDisabledIcon()
			super.createMethodDescriptor(getBeanClass(),"getDisabledIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("getDisabledIcon().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the disabled icon",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDisplayedMnemonic()
			super.createMethodDescriptor(getBeanClass(),"getDisplayedMnemonic",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("getDisplayedMnemonic().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the mnemonic for associated component",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getHorizontalAlignment()
			super.createMethodDescriptor(getBeanClass(),"getHorizontalAlignment",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("getHorizontalAlignment().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JLabelMessages.getString("getHorizontalAlignment().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getHorizontalTextPosition()
			super.createMethodDescriptor(getBeanClass(),"getHorizontalTextPosition",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("getHorizontalTextPosition().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JLabelMessages.getString("getHorizontalTextPosition().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getIcon()
			super.createMethodDescriptor(getBeanClass(),"getIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("getIcon().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the icon image displayed",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getIconTextGap()
			super.createMethodDescriptor(getBeanClass(),"getIconTextGap",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("getIconTextGap().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the space between icon and text",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getLabelFor()
// can't handle this in MOF for now so just comment out
//			super.createMethodDescriptor(getBeanClass(),"getLabelFor",  //$NON-NLS-1$
//				new Object[] {
//	   			DISPLAYNAME, JLabelMessages.getString("getLabelFor().Name"), //$NON-NLS-1$
//	      		SHORTDESCRIPTION, JLabelMessages.getString("getLabelFor().Desc"), //$NON-NLS-1$
//		      	HIDDEN, Boolean.TRUE
//	    		}, 
//	    		new ParameterDescriptor[] {},
//	      		new Class[] {}		    		
//		  	),
		  	// getText()
			super.createMethodDescriptor(getBeanClass(),"getText",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("getText().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the text",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("getUI().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the LabelUI object",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE	      		
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getVerticalAlignment()
			super.createMethodDescriptor(getBeanClass(),"getVerticalAlignment",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("getVerticalAlignment().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JLabelMessages.getString("getVerticalAlignment().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getVerticalTextPosition()
			super.createMethodDescriptor(getBeanClass(),"getVerticalTextPosition",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("getVerticalTextPosition().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JLabelMessages.getString("getVerticalTextPosition().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setDisabledIcon(Icon)
			super.createMethodDescriptor(getBeanClass(),"setDisabledIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("setDisabledIcon(Icon).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the icon for the disabled state",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("icon", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JLabelMessages.getString("setDisabledIcon(Icon).icon.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Disabled icon",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.Icon.class
	      		}		    		
		  	),
		  	// setDisplayedMnemonic(char)
			super.createMethodDescriptor(getBeanClass(),"setDisplayedMnemonic",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("setDisplayedMnemonic(char).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JLabelMessages.getString("setDisplayedMnemonic(char).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("mnemonic", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JLabelMessages.getString("setDisplayedMnemonic(char).mnemonic.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Mnemonic key",
	      				})
	      		},
	      		new Class[] {
	      			char.class
	      		}		    		
		  	),
		  	// setHorizontalAlignment(int)
			super.createMethodDescriptor(getBeanClass(),"setHorizontalAlignment",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("setHorizontalAlignment(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JLabelMessages.getString("setHorizontalAlignment(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("alignment", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JLabelMessages.getString("setHorizontalAlignment(int).alignment.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "CENTER, LEFT or RIGHT",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setHorizontalTextPosition(int)
			super.createMethodDescriptor(getBeanClass(),"setHorizontalTextPosition",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("setHorizontalTextPosition(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JLabelMessages.getString("setHorizontalTextPosition(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("position", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JLabelMessages.getString("setHorizontalTextPosition(int).textPosition.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "CENTER, LEFT or RIGHT",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setIcon(Icon)
			super.createMethodDescriptor(getBeanClass(),"setIcon",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("setIcon(Icon).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the icon to display",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("icon", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JLabelMessages.getString("setIcon(Icon).icon.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Image icon",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.Icon.class
	      		}		    		
		  	),
		  	// setIconTextGap(int)
			super.createMethodDescriptor(getBeanClass(),"setIconTextGap",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("setIconTextGap(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set to space between icon and text",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("gap", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JLabelMessages.getString("setIconTextGap(int).iconTextGap.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Space in pixels",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setLabelFor(Component)
// can't handle this in MOF for now so just comment out
//			super.createMethodDescriptor(getBeanClass(),"setLabelFor",  //$NON-NLS-1$
//				new Object[] {
//	   			DISPLAYNAME, JLabelMessages.getString("setLabelFor(Component).Name"), //$NON-NLS-1$
//	      		SHORTDESCRIPTION, JLabelMessages.getString("setLabelFor(Component).Desc"), //$NON-NLS-1$
//		      	HIDDEN, Boolean.TRUE
//	    		}, 
//	    		new ParameterDescriptor[] {
//	    			createParameterDescriptor("component", new Object[] { //$NON-NLS-1$
//	   					DISPLAYNAME, JLabelMessages.getString("setLabelFor(Component).aComponent.Name"), //$NON-NLS-1$
//	      				// SHORTDESCRIPTION, "Component for mnemonic on label",
//	      				})
//	      		},
//	      		new Class[] {
//	      			java.awt.Component.class
//	      		}		    		
//		  	),
		  	// setText(String)
			super.createMethodDescriptor(getBeanClass(),"setText",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("setText(String).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the text",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("text", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JLabelMessages.getString("setText(String).text.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Text",
	      				})
	      		},
	      		new Class[] {
	      			String.class
	      		}		    		
		  	),
		  	// setUI(LabelUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("setUI(LabelUI).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the Label UI",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("ui", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JLabelMessages.getString("setUI(LabelUI).anUI.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Button UI",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.plaf.LabelUI.class
	      		}		    		
		  	),
		  	// setVerticalAlignment(int)
			super.createMethodDescriptor(getBeanClass(),"setVerticalAlignment",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("setVerticalAlignment(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JLabelMessages.getString("setVerticalAlignment(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("alignment", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JLabelMessages.getString("setVerticalAlignment(int).alignment.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "CENTER, LEFT or RIGHT",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setVerticalTextPosition(int)
			super.createMethodDescriptor(getBeanClass(),"setVerticalTextPosition",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JLabelMessages.getString("setVerticalTextPosition(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JLabelMessages.getString("setVerticalTextPosition(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("position", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JLabelMessages.getString("setVerticalTextPosition(int).textPosition.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "CENTER, LEFT or RIGHT",
	      				})
	      		},
	      		new Class[] {
	      			int.class
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
	    	// disabledIcon
			super.createPropertyDescriptor(getBeanClass(),"disabledIcon", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, JLabelMessages.getString("disabledIcon.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JLabelMessages.getString("disabledIcon.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE
	    		}
	    	),
	    	// displayedMnemonic
			super.createPropertyDescriptor(getBeanClass(),"displayedMnemonic", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JLabelMessages.getString("displayedMnemonic.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JLabelMessages.getString("displayedMnemonic.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	  //    	PROPERTYEDITORCLASS, com.ibm.uvm.abt.edit.MnemonicEditor.class
	    		}
	    	),
	    	// horizontalAlignment
			super.createPropertyDescriptor(getBeanClass(),"horizontalAlignment", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JLabelMessages.getString("horizontalAlignment.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JLabelMessages.getString("horizontalAlignment.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	ENUMERATIONVALUES, new Object[] {
	      			JLabelMessages.getString("Alignment.LEFT"), new Integer(javax.swing.SwingConstants.LEFT), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.LEFT", //$NON-NLS-1$
	      			JLabelMessages.getString("Alignment.CENTER"), new Integer(javax.swing.SwingConstants.CENTER), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.CENTER", //$NON-NLS-1$
	      			JLabelMessages.getString("Alignment.RIGHT"), new Integer(javax.swing.SwingConstants.RIGHT), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.RIGHT", //$NON-NLS-1$
	      			JLabelMessages.getString("Alignment.LEADING") , new Integer(javax.swing.SwingConstants.LEADING), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.LEADING", //$NON-NLS-1$
	      			JLabelMessages.getString("Alignment.TRAILING") , new Integer(javax.swing.SwingConstants.TRAILING), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.TRAILING"	      			 //$NON-NLS-1$
	    		}
	    	}
	    	),
	    	// horizontalTextPosition
			super.createPropertyDescriptor(getBeanClass(),"horizontalTextPosition", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JLabelMessages.getString("horizontalTextPosition.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JLabelMessages.getString("horizontalTextPosition.Desc"), //$NON-NLS-1$
	      	ENUMERATIONVALUES, new Object[] {
	      			JLabelMessages.getString("Alignment.LEFT"), new Integer(javax.swing.SwingConstants.LEFT), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.LEFT", //$NON-NLS-1$
	      			JLabelMessages.getString("Alignment.CENTER"), new Integer(javax.swing.SwingConstants.CENTER), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.CENTER", //$NON-NLS-1$
	      			JLabelMessages.getString("Alignment.RIGHT"), new Integer(javax.swing.SwingConstants.RIGHT), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.RIGHT", //$NON-NLS-1$
					JLabelMessages.getString("Alignment.LEADING") , new Integer(javax.swing.SwingConstants.LEADING),		 //$NON-NLS-1$
	      				"javax.swing.SwingConstants.LEADING", //$NON-NLS-1$
	      			JLabelMessages.getString("Alignment.TRAILING") , new Integer(javax.swing.SwingConstants.TRAILING), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.TRAILING"		      					      			 //$NON-NLS-1$
	    		}
    		}
	    	),
	    	// icon
			super.createPropertyDescriptor(getBeanClass(),"icon", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JLabelMessages.getString("icon.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JLabelMessages.getString("icon.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	    		}
	    	),
	    	// iconTextGap
			super.createPropertyDescriptor(getBeanClass(),"iconTextGap", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JLabelMessages.getString("iconTextGap.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JLabelMessages.getString("iconTextGap.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// labelFor
			super.createPropertyDescriptor(getBeanClass(),"labelFor", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JLabelMessages.getString("labelFor.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JLabelMessages.getString("labelFor.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
			// layout - hide it
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// text
			super.createPropertyDescriptor(getBeanClass(),"text", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JLabelMessages.getString("text.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JLabelMessages.getString("text.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	PREFERRED, Boolean.TRUE	      	
	    		}
	    	),
	    	// verticalAlignment
			super.createPropertyDescriptor(getBeanClass(),"verticalAlignment", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JLabelMessages.getString("verticalAlignment.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JLabelMessages.getString("verticalAlignment.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	ENUMERATIONVALUES, new Object[] {
	      			JLabelMessages.getString("Alignment.TOP"), new Integer(javax.swing.SwingConstants.TOP), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.TOP", //$NON-NLS-1$
	      			JLabelMessages.getString("Alignment.CENTER"), new Integer(javax.swing.SwingConstants.CENTER), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.CENTER", //$NON-NLS-1$
	      			JLabelMessages.getString("Alignment.BOTTOM"), new Integer(javax.swing.SwingConstants.BOTTOM), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.BOTTOM"	      			 //$NON-NLS-1$
	    		}
	      	
	    		}
	    	),
	    	// verticalTextPosition
			super.createPropertyDescriptor(getBeanClass(),"verticalTextPosition", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JLabelMessages.getString("verticalTextPosition.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JLabelMessages.getString("verticalTextPosition.Desc"), //$NON-NLS-1$
	      	ENUMERATIONVALUES, new Object[] {
	      			JLabelMessages.getString("Alignment.TOP"), new Integer(javax.swing.SwingConstants.TOP), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.TOP", //$NON-NLS-1$
	      			JLabelMessages.getString("Alignment.CENTER"), new Integer(javax.swing.SwingConstants.CENTER), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.CENTER", //$NON-NLS-1$
	      			JLabelMessages.getString("Alignment.BOTTOM"), new Integer(javax.swing.SwingConstants.BOTTOM), //$NON-NLS-1$
	      				"javax.swing.SwingConstants.BOTTOM"	      			 //$NON-NLS-1$
	    		}
	    		}
	    	),
	    	// ui
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JLabelMessages.getString("ui.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JLabelMessages.getString("ui.Desc"), //$NON-NLS-1$
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
}
