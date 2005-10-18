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
 *  $RCSfile: JListBeanInfo.java,v $
 *  $Revision: 1.10 $  $Date: 2005-10-18 15:32:23 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.common.IBaseBeanInfoConstants;

public class JListBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle JListMessages = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jlist");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JList.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, JListMessages.getString("JList.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, JListMessages.getString("JList.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/list32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/list16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
			listSelectionEventSetDescriptor()
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
	    return loadImage("list32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("list16.gif"); //$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// addSelectionInterval(int,int)
			super.createMethodDescriptor(getBeanClass(),"addSelectionInterval",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("addSelectionInterval(int,int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("addSelectionInterval(int,int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index0", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("addSelectionInterval(int,int).startIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Starting cell",
	      				}
	      			),
	      			createParameterDescriptor("index1", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("addSelectionInterval(int,int).endIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Ending cell",
	      				})
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// clearSelection()
			super.createMethodDescriptor(getBeanClass(),"clearSelection",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("clearSelection().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("clearSelection().Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// ensureIndexIsVisible(int)
			super.createMethodDescriptor(getBeanClass(),"ensureIndexIsVisible",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("ensureIndexIsVisible(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("ensureIndexIsVisible(int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("ensureIndexIsVisible(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of cell",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
			// getAccessibleContext()
			super.createMethodDescriptor(getBeanClass(),"getAccessibleContext",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getAccessibleContext().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the accessible context",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getCellBounds(int,int)
			super.createMethodDescriptor(getBeanClass(),"getCellBounds",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getCellBounds(int,int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the bounding rectangle for the range of items",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index1", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("getCellBounds(int,int).index1.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of first cell",
	      				}),
	      			createParameterDescriptor("index2", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("getCellBounds(int,int).y.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of last cell",
	      				})
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// getCellRenderer()
			super.createMethodDescriptor(getBeanClass(),"getCellRenderer",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getCellRenderer().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the current cell renderer",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getFirstVisibleIndex()
			super.createMethodDescriptor(getBeanClass(),"getFirstVisibleIndex",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getFirstVisibleIndex().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("getFirstVisibleIndex().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getFixedCellHeight()
			super.createMethodDescriptor(getBeanClass(),"getFixedCellHeight",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getFixedCellHeight().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the fixed cell width value",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getFixedCellWidth()
			super.createMethodDescriptor(getBeanClass(),"getFixedCellWidth",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getFixedCellWidth().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the fixed cell width value",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getLastVisibleIndex()
			super.createMethodDescriptor(getBeanClass(),"getLastVisibleIndex",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getLastVisibleIndex().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("getLastVisibleIndex().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getModel()
			super.createMethodDescriptor(getBeanClass(),"getModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getModel().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("getModel().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getPreferredScrollableViewportSize()
			super.createMethodDescriptor(getBeanClass(),"getPreferredScrollableViewportSize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getPreferredScrollableViewportSize().Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	   			OBSCURE, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the preferred viewable size",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectedIndex()
			super.createMethodDescriptor(getBeanClass(),"getSelectedIndex",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getSelectionIndex().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("getSelectionIndex().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectedIndices()
			super.createMethodDescriptor(getBeanClass(),"getSelectedIndices",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getSelectedIndices().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get an array of selected indices",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectedValue()
			super.createMethodDescriptor(getBeanClass(),"getSelectedValue",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getSelectedValue().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("getSelectedValue().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectedValues()
			super.createMethodDescriptor(getBeanClass(),"getSelectedValues",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getSelectedValues().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("getSelectedValues().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionBackground()
			super.createMethodDescriptor(getBeanClass(),"getSelectionBackground",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getSelectionBackground().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("getSelectionBackground().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionForeground()
			super.createMethodDescriptor(getBeanClass(),"getSelectionForeground",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getSelectionForeground().Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("getSelectionForeground().Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionMode()
			super.createMethodDescriptor(getBeanClass(),"getSelectionMode",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getSelectionMode().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the list selection mode",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionModel()
			super.createMethodDescriptor(getBeanClass(),"getSelectionModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getSelectionModel().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the model representing the current selection state",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getUI().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the ListUI object",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE	      		
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getValueIsAdjusting()
			super.createMethodDescriptor(getBeanClass(),"getValueIsAdjusting",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getValueIsAdjusting().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE if the list value is adjusting"
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getVisibleRowCount()
			super.createMethodDescriptor(getBeanClass(),"getVisibleRowCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("getVisibleRowCount().Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Number of cells visible",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// indexToLocation(int)
			super.createMethodDescriptor(getBeanClass(),"indexToLocation",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("indexToLocation(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("indexToLocation(int).Desc"), //$NON-NLS-1$
	      		
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("indexToLocation(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "List cell index",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// isSelectedIndex(int)
			super.createMethodDescriptor(getBeanClass(),"isSelectedIndex",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("isSelectedIndex(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("isSelectedIndex(int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("isSelectedIndex(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of cell",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// isSelectionEmpty
			super.createMethodDescriptor(getBeanClass(),"isSelectionEmpty",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("isSelectionEmpty.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE if the selection is empty",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// removeSelectionInterval(int,int)
			super.createMethodDescriptor(getBeanClass(),"removeSelectionInterval",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("removeSelectionInterval(int,int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("removeSelectionInterval(int,int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index0", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("removeSelectionInterval(int,int).startIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Starting row",
	      				}
	      			),
	      			createParameterDescriptor("index1", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("removeSelectionInterval(int,int).endIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "End row",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// setCellRenderer(ListCellRenderer)
			super.createMethodDescriptor(getBeanClass(),"setCellRenderer",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("setCellRenderer(ListCellRenderer).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("setCellRenderer(ListCellRenderer).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("cellRenderer", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setCellRenderer(ListCellRenderer).cellRenderer.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "List cell renderer",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.ListCellRenderer.class
	      		}		    		
		  	),
			// setFixedCellHeight(int)
			super.createMethodDescriptor(getBeanClass(),"setFixedCellHeight",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("setFixedCellHeight(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the height of each cell in the list",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("height", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setFixedCellHeight(int).height.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Cell height in pixels",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setFixedCellWidth(int)
			super.createMethodDescriptor(getBeanClass(),"setFixedCellWidth",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("setFixedCellWidth(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the width of each cell in the list",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("width", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setFixedCellWidth(int).width.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Cell width in pixels",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setListData(Object[])
			super.createMethodDescriptor(getBeanClass(),"setListData",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("setListData(Object[]).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("setListData(Object[]).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("data", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setListData(Object[]).listData.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "An array of objects",
	      				EXPERT, Boolean.TRUE
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.lang.Object[].class
	      		}		    		
		  	),
		  	// setListData(Vector)
			super.createMethodDescriptor(getBeanClass(),"setListData",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("setListData(Vector).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("setListData(Vector).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("data", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setListData(Vector).listData.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "A vector of items",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.util.Vector.class
	      		}		    		
		  	),
		  	// setModel(ListModel)
			super.createMethodDescriptor(getBeanClass(),"setModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("setModel(ListModel).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the model providing the list items",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("model", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setModel(ListModel).aModel.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "List model",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.ListModel.class
	      		}		    		
		  	),
		  	// setSelectedIndex(int)
			super.createMethodDescriptor(getBeanClass(),"setSelectedIndex",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("setSelectedIndex(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("setSelectedIndex(int).Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setSelectedIndex(int).index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of item",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setSelectedIndices(int[])
			super.createMethodDescriptor(getBeanClass(),"setSelectedIndices",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("setSelectedIndices(int[]).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Select the items at specified indices",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("indices", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setSelectedIndices(int[]).indexArray.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Array of indices",
	      				})
	      		},
	      		new Class[] {
	      			int[].class
	      		}		    		
		  	),
		  	// setSelectionBackground(Color)
			super.createMethodDescriptor(getBeanClass(),"setSelectionBackground",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("setSelectionBackground(Color).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the background color of selected cells",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("color", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setSelectionBackground(Color).color.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Background color",
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Color.class
	      		}		    		
		  	),
		  	// setSelectionForeground(Color)
			super.createMethodDescriptor(getBeanClass(),"setSelectionForeground",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("setSelectionForeground(Color).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the foreground color of selected cells",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("color", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setSelectionForeground(Color).color.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Foreground color",
	      				})
	      		},
	      		new Class[] {
	      			java.awt.Color.class
	      		}		    		
		  	),
		  	// setSelectionInterval(int,int)
			super.createMethodDescriptor(getBeanClass(),"setSelectionInterval",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("setSelectionInterval(int,int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("setSelectionInterval(int,int).Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index0", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setSelectionInterval(int,int).startIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Starting row",
	      				}
	      			),
	      			createParameterDescriptor("index1", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setSelectionInterval(int,int).endIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "End row",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// setSelectionMode(int)
			super.createMethodDescriptor(getBeanClass(),"setSelectionMode",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("setSelectionMode(int).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("setSelectionMode(int).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("mode", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setSelectionMode(int).aModel.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "SINGLE_SELECTION...",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setSelectionModel(ListSelectionModel)
			super.createMethodDescriptor(getBeanClass(),"setSelectionModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("setSelectionModel(ListSelectionModel).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("setSelectionModel(ListSelectionModel).Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("model", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setSelectionModel(ListSelectionModel).aModel.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "ListSelection model",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.ListSelectionModel.class
	      		}		    		
		  	),
		  	// setUI(ListUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("setUI(ListUI).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the list UI",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("ui", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setUI(ListUI).anUI.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "List UI",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.plaf.ListUI.class
	      		}		    		
		  	),
		  	// setValueIsAdjusting(boolean)
			super.createMethodDescriptor(getBeanClass(),"setValueIsAdjusting",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("setValueIsAdjusting(boolean).Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("setValueIsAdjusting(boolean).Desc"), //$NON-NLS-1$
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setValueIsAdjusting.b.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "whether the value is adjusting",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setVisibleRowCount(int)
			super.createMethodDescriptor(getBeanClass(),"setVisibleRowCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("setVisibleRowCount(int).Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the number of cells visible",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("newCount", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, JListMessages.getString("setVisibleRowCount(int).newCount.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "New number of visible cells",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
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
	    	// cellRenderer
	    	super.createPropertyDescriptor(getBeanClass(),"cellRenderer", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("cellRenderer.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("cellRenderer.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	//DESIGNTIMEPROPERTY , Boolean.FALSE,
	      	BOUND, Boolean.TRUE }   
	    	),
	    	// firstVisibleIndex
			super.createPropertyDescriptor(getBeanClass(),"firstVisibleIndex", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("firstVisibleIndex.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("firstVisibleIndex.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// fixedCellHeight
			super.createPropertyDescriptor(getBeanClass(),"fixedCellHeight", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("fixedCellHeight.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("fixedCellHeight.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// fixedCellWidth
			super.createPropertyDescriptor(getBeanClass(),"fixedCellWidth", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("fixedCellWidth.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("fixedCellWidth.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// lastVisibleIndex
			super.createPropertyDescriptor(getBeanClass(),"lastVisibleIndex", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("lastVisibleIndex.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("lastVisibleIndex.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.FALSE
	    		}
	    	),
			// layout - hide it
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// model
			super.createPropertyDescriptor(getBeanClass(),"model", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("model.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("model.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	PREFERRED, Boolean.TRUE,
	    		}
	    	),
	    	// preferredScrollableViewportSize
			super.createPropertyDescriptor(getBeanClass(),"preferredScrollableViewportSize", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("preferredScrollableViewportSize.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("preferredScrollableViewportSize.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	BOUND, Boolean.TRUE,
	      	OBSCURE, Boolean.TRUE	      	
	    		}
	    	),
	    	// selectedIndex
			super.createPropertyDescriptor(getBeanClass(),"selectedIndex", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("selectedIndex.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("selectedIndex.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE	      	
	    		}
	    	),
	    	// selectedIndices
			super.createPropertyDescriptor(getBeanClass(),"selectedIndices", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("selectedIndices.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("selectedIndices.Desc"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY , Boolean.FALSE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// selectionBackground
			super.createPropertyDescriptor(getBeanClass(),"selectionBackground", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("selectionBackground.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("selectionBackground.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// selectionEmpty
			super.createPropertyDescriptor(getBeanClass(),"selectionEmpty", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("selectionEmpty.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("selectionEmpty.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// selectionForeground
			super.createPropertyDescriptor(getBeanClass(),"selectionForeground", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("selectionForeground.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("selectionForeground.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// selectionMode
			super.createPropertyDescriptor(getBeanClass(),"selectionMode", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("selectionMode.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("selectionMode.Desc"), //$NON-NLS-1$
	      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			JListMessages.getString("selectionMode.SINGLE_SELECTION"), new Integer(javax.swing.ListSelectionModel.SINGLE_SELECTION), //$NON-NLS-1$
	      				"javax.swing.ListSelectionModel.SINGLE_SELECTION", //$NON-NLS-1$
					JListMessages.getString("selectionMode.SINGLE_INTERVAL_SELECTION"), new Integer(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION), //$NON-NLS-1$
	      				"javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION", //$NON-NLS-1$
	      			JListMessages.getString("selectionMode.MULTIPLE_INTERVAL_SELECTION"), new Integer(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION), //$NON-NLS-1$
	      				"javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION", //$NON-NLS-1$
	    			}	      	
	    		}
	    	),
	    	// selectionModel
			super.createPropertyDescriptor(getBeanClass(),"selectionModel", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("selectionModel.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("selectionModel.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE
			EXPERT, Boolean.TRUE
	    		}
	    	),
			// selectedValue
			super.createPropertyDescriptor(getBeanClass(),"selectedValue", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("selectedValue.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("selectedValue.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE
	    		}
	    	),
			// valueIsAdjusting
			super.createPropertyDescriptor(getBeanClass(),"valueIsAdjusting", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("valueIsAdjusting.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("valueIsAdjusting.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// visibleRowCount
			super.createPropertyDescriptor(getBeanClass(),"visibleRowCount", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("visibleRowCount.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("visibleRowCount.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,	      	
	    		}
	    	),
	    	// ui
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, JListMessages.getString("ui.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, JListMessages.getString("ui.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
			BOUND, Boolean.TRUE,
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
 * Gets the componentevent set descriptor.
 * @return java.beans.EventSetDescriptor
 */
public EventSetDescriptor listSelectionEventSetDescriptor() {
	EventSetDescriptor aDescriptor = null;
	Class[] paramTypes = { javax.swing.event.ListSelectionEvent.class };
	MethodDescriptor aDescriptorList[] = {
			super.createMethodDescriptor(javax.swing.event.ListSelectionListener.class,
				"valueChanged",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, JListMessages.getString("valueChanged.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, JListMessages.getString("valueChanged.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("event", new Object[] { //$NON-NLS-1$
	   				DISPLAYNAME, JListMessages.getString("valueChanged.listSelectionEvent.Name"), //$NON-NLS-1$
	      			// SHORTDESCRIPTION, "List selection changed event",
	      			})
	      		},
	      		paramTypes
		  	)
		};	
		aDescriptor = super.createEventSetDescriptor(getBeanClass(),
						"listSelection", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, JListMessages.getString("listSelectionEvents.Name"), //$NON-NLS-1$
	      				SHORTDESCRIPTION, JListMessages.getString("listSelectionEvents.Desc"), //$NON-NLS-1$
	      				INDEFAULTEVENTSET, Boolean.TRUE,
	      				}, 
						aDescriptorList, javax.swing.event.ListSelectionListener.class,
						"addListSelectionListener", "removeListSelectionListener"); //$NON-NLS-1$ //$NON-NLS-2$

	return aDescriptor;
}
}
