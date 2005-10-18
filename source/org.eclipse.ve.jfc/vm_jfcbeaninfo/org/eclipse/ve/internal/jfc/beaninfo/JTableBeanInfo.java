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
 *  $RCSfile: JTableBeanInfo.java,v $
 *  $Revision: 1.10 $  $Date: 2005-10-18 15:32:23 $ 
 */

import java.beans.*;

import org.eclipse.jem.beaninfo.common.IBaseBeanInfoConstants;

public class JTableBeanInfo extends IvjBeanInfo {
		
private static java.util.ResourceBundle resJTable = java.util.ResourceBundle.getBundle("org.eclipse.ve.internal.jfc.beaninfo.jtable");  //$NON-NLS-1$

/**
 * Gets the bean class.
 * @return java.lang.Class
 */
public Class getBeanClass() {
	return javax.swing.JTable.class;
}
public java.beans.BeanDescriptor getBeanDescriptor() {
	java.beans.BeanDescriptor aDescriptor = null;
	/* Create and return the bean descriptor. */
	try {
		aDescriptor = createBeanDescriptor(getBeanClass(), new Object[] {
	               		DISPLAYNAME, resJTable.getString("BeanDesc.Name"), //$NON-NLS-1$
	        			SHORTDESCRIPTION, resJTable.getString("BeanDesc.Desc") //$NON-NLS-1$
						}			    
				  	  );
		aDescriptor.setValue("ICON_COLOR_32x32", "icons/jtable32.gif"); //$NON-NLS-1$ //$NON-NLS-2$
		aDescriptor.setValue("ICON_COLOR_16x16", "icons/jtable16.gif"); //$NON-NLS-1$ //$NON-NLS-2$
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
	public java.awt.Image getIcon(int kind) {
	if (kind == ICON_COLOR_32x32) 
	    return loadImage("jtable32.gif"); //$NON-NLS-1$
	if (kind == ICON_COLOR_16x16) 
	    return loadImage("jtable16.gif"); //$NON-NLS-1$
   return super.getIcon(kind);
	}
/**
 * Return the method descriptors for this bean.
 * @return java.beans.MethodDescriptor[]
 */
public java.beans.MethodDescriptor[] getMethodDescriptors() {
	try {
		MethodDescriptor aDescriptorList[] = {
			// addColumn(TableColumn)
			super.createMethodDescriptor(getBeanClass(),"addColumn",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("MthdDesc.AddColumn.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("MthdDesc.AddColumn.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	      			createParameterDescriptor("aColumn", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("ParamDesc.AddColumn.aColumn.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TableColumn to add",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.table.TableColumn.class
	      		}		    		
		  	),
		  	// addColumnSelectionInterval(int,int)
			super.createMethodDescriptor(getBeanClass(),"addColumnSelectionInterval",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("MthdDesc.AddColumnSelectionInterval.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("MthdDesc.AddColumnSelectionInterval.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index0", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("ParamDesc.AddColumnSelectionInterval.index0.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Starting column",
	      				}
	      			),
	      			createParameterDescriptor("index1", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("ParamDesc.AddColumnSelectionInterval.index1.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "End column",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
			// addRowSelectionInterval(int,int)
			super.createMethodDescriptor(getBeanClass(),"addRowSelectionInterval",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("MthdDesc.AddRowSelectionInterval.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("MthdDesc.AddRowSelectionInterval.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index0", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("ParamDesc.AddRowSelectionInterval.index0.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Starting row",
	      				}
	      			),
	      			createParameterDescriptor("index1", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("ParamDesc.AddRowSelectionInterval.index1.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "End row",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// clearSelection()
			super.createMethodDescriptor(getBeanClass(),"clearSelection",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("MthdDesc.ClearSelection.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("MthdDesc.ClearSelection.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// columnAtPoint(Point)
			super.createMethodDescriptor(getBeanClass(),"columnAtPoint",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("MthdDesc.ColumnAtPoint.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Index of column that point lies in",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("point", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("ParamDesc.ColumnAtPoint.point.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Point",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Point.class
	      		}		    		
		  	),
		  	
		  	// columnMarginChanged(ChangeEvent) - discard
		  	// columnMoved(TableColumnModelEvent) - discard
		  	// columnRemoved(TableColumnModelEvent) - discard
		  	// columnSelectionChanged(ListSelectionModel) - discard
		  	
		  	// convertColumnIndexToModel(int)
			super.createMethodDescriptor(getBeanClass(),"convertColumnIndexToModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("MthdDesc.ConvertColumnIndexToModel.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the index of column in model for given column",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("ParamDesc.ConvertColumnIndexToModel.index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of column",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// convertColumnIndexToView(int) - discard		  	
			// createDefaultColumnsFromModel()
			super.createMethodDescriptor(getBeanClass(),"createDefaultColumnsFromModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("MthdDesc.CreateDefaultColumnsFromModel.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Create default columns from data model",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// createScrollPaneForTable(JTable)  - discard
		  	// editCellAt(int,int)
			super.createMethodDescriptor(getBeanClass(),"editCellAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("EditCellAt.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("EditCellAt.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("EditCellAt.row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Row",
	      				}
	      			),
	      			createParameterDescriptor("col", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("EditCellAt.column.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Column",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	
		  	// editCellAt(int,int,EventObject) - discard
		  	// editingCanceled(ChangeEvent) - discard
		  	// editingStopped(ChangeEvent) - discard
		  	
		  	// getAutoCreateColumnsFromModel()
			super.createMethodDescriptor(getBeanClass(),"getAutoCreateColumnsFromModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetAutoCreateColumnsFromModel.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Whether table will create default columns from model",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getAutoResizeMode()
			super.createMethodDescriptor(getBeanClass(),"getAutoResizeMode",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetAutoResizeMode.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetAutoResizeMode.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getCellEditor()
			super.createMethodDescriptor(getBeanClass(),"getCellEditor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetCellEditor.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the editor used to edit cells",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getCellRect(int,int,boolean) - discard

		  	// getCellSelectionEnabled()
			super.createMethodDescriptor(getBeanClass(),"getCellSelectionEnabled",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GgetCellSelectionEnabled.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GgetCellSelectionEnabled.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getColumn(Object)
			super.createMethodDescriptor(getBeanClass(),"getColumn",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetColumn.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the column with matching identifier",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("id", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("GetColumn.identifier.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Identifier",
	      				})
	      		},
	      		new Class[] {
	      			java.lang.Object.class
	      		}		    		
		  	),
		  	// getColumnClass(int)
			super.createMethodDescriptor(getBeanClass(),"getColumnClass",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetColumnClass1.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetColumnClass1.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("column", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("GetColumnClass1.column.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of column",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getColumnCount()
			super.createMethodDescriptor(getBeanClass(),"getColumnCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetColumnCount.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetColumnCount.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getColumnModel()
			super.createMethodDescriptor(getBeanClass(),"getColumnModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetColumnModel.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the model providing the data",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getColumnName(int)
			super.createMethodDescriptor(getBeanClass(),"getColumnClass",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetColumnClass2.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetColumnClass2.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("column", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("GetColumnClass2.column.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of column",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// getColumnSelectionAllowed()
			super.createMethodDescriptor(getBeanClass(),"getColumnSelectionAllowed",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetColumnSelectionAllowed.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetColumnSelectionAllowed.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getDefaultEditor(Class)
			super.createMethodDescriptor(getBeanClass(),"getDefaultEditor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetDefaultEditor.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetDefaultEditor.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("columnClass", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("GetDefaultEditor.columnClass.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Class of data in column",
	      				})
	      		},
	      		new Class[] {
	      			java.lang.Class.class
	      		}		    		
		  	),
		  	// getDefaultRenderer(Class)
			super.createMethodDescriptor(getBeanClass(),"getDefaultRenderer",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetDefaultRenderer.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetDefaultRenderer.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("columnClass", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("GetDefaultRenderer.columnClass.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Class of data in column",
	      				})
	      		},
	      		new Class[] {
	      			java.lang.Class.class
	      		}		    		
		  	),
			// getEditingColumn()
			super.createMethodDescriptor(getBeanClass(),"getEditingColumn",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetEditingColumn.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetEditingColumn.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getEditingRow()
			super.createMethodDescriptor(getBeanClass(),"getEditingRow",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetEditingRow.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetEditingRow.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getEditorComponent()
			super.createMethodDescriptor(getBeanClass(),"getEditorComponent",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetEditorComponent.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get component handling edit session",
	      		EXPERT, Boolean.TRUE,
	   			OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getGridColor()
			super.createMethodDescriptor(getBeanClass(),"getGridColor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetGridColor.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetGridColor.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getIntercellSpacing()
			super.createMethodDescriptor(getBeanClass(),"getIntercellSpacing",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetIntercellSpacing.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetIntercellSpacing.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getModel()
			super.createMethodDescriptor(getBeanClass(),"getModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetModel.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetModel.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getPreferredScrollableViewportSize()
			super.createMethodDescriptor(getBeanClass(),"getPreferredScrollableViewportSize",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetPreferredScrollableViewportSize.Name"), //$NON-NLS-1$
	   			EXPERT, Boolean.TRUE,
	   			// OBSCURE, Boolean.TRUE,
	      		// SHORTDESCRIPTION, "Get the preferred size of the viewport",
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getRowCount()
			super.createMethodDescriptor(getBeanClass(),"getRowCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetRowCount.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetRowCount.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getRowHeight()
			super.createMethodDescriptor(getBeanClass(),"getRowHeight",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetRowHeight.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetRowHeight.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getRowSelectionAllowed()
			super.createMethodDescriptor(getBeanClass(),"getRowSelectionAllowed",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetRowSelectionAllowed.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetRowSelectionAllowed.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectedColumn()
			super.createMethodDescriptor(getBeanClass(),"getSelectedColumn",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetSelectedColumn.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetSelectedColumn.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectedColumnCount()
			super.createMethodDescriptor(getBeanClass(),"getSelectedColumnCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetSelectedColumnCount.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetSelectedColumnCount.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getSelectedColumns()
			super.createMethodDescriptor(getBeanClass(),"getSelectedColumns",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetSelectedColumns.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetSelectedColumns.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectedRow()
			super.createMethodDescriptor(getBeanClass(),"getSelectedRow",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetSelectedRow.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetSelectedRow.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectedRowCount()
			super.createMethodDescriptor(getBeanClass(),"getSelectedRowCount",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetSelectedRowCount.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetSelectedRowCount.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getSelectedRows()
			super.createMethodDescriptor(getBeanClass(),"getSelectedRows",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetSelectedRows.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetSelectedRows.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionBackground()
			super.createMethodDescriptor(getBeanClass(),"getSelectionBackground",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetSelectionBackground.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetSelectionBackground.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionForeground()
			super.createMethodDescriptor(getBeanClass(),"getSelectionForeground",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetSelectionForeground.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetSelectionForeground.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getSelectionModel()
			super.createMethodDescriptor(getBeanClass(),"getSelectionModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetSelectionModel.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetSelectionModel.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getShowHorizontalLines()
			super.createMethodDescriptor(getBeanClass(),"getShowHorizontalLines",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetShowHorizontalLines.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetShowHorizontalLines.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getShowVerticalLines()
			super.createMethodDescriptor(getBeanClass(),"getShowVerticalLines",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetShowVerticalLines.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetShowVerticalLines.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getTableHeader()
			super.createMethodDescriptor(getBeanClass(),"getTableHeader",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetTableHeader.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the table header object",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// getUI()
			super.createMethodDescriptor(getBeanClass(),"getUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetUI.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the TableUI object",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE	      		
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
			// getValueAt(int,int)
			super.createMethodDescriptor(getBeanClass(),"getValueAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("GetValueAt.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("GetValueAt.Desc"), //$NON-NLS-1$
	      		PREFERRED, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("GetValueAt.row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Row index",
	      				}
	      			),
	      			createParameterDescriptor("column", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("GetValueAt.column.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Column index",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// isCellEditable(int,int)
			super.createMethodDescriptor(getBeanClass(),"isCellEditable",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("IsCellEditable.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("IsCellEditable.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("IsCellEditable.row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Row index",
	      				}
	      			),
	      			createParameterDescriptor("column", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("IsCellEditable.column.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Column index",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// isCellSelected(int,int)
			super.createMethodDescriptor(getBeanClass(),"isCellSelected",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("IsCellSelected.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("IsCellSelected.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("IsCellSelected.row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Row index",
	      				}
	      			),
	      			createParameterDescriptor("column", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("IsCellSelected.column.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Column index",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// isColumnSelected(int)
			super.createMethodDescriptor(getBeanClass(),"isColumnSelected",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("IsColumnSelected.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("IsColumnSelected.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	      			createParameterDescriptor("column", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("IsColumnSelected.column.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Column index",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// isEditing
			super.createMethodDescriptor(getBeanClass(),"isEditing",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("IsEditing.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("IsEditing.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// isRowSelected(int)
			super.createMethodDescriptor(getBeanClass(),"isRowSelected",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("IsRowSelected.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("IsRowSelected.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("IsRowSelected.row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Tabke row index",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
			// moveColumn(int,int)
			super.createMethodDescriptor(getBeanClass(),"moveColumn",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("MoveColumn.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("MoveColumn.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index0", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("MoveColumn.sourceIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Current column index",
	      				}
	      			),
	      			createParameterDescriptor("index1", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("MoveColumn.targetIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Target column index",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// prepareEditor(TableEditor,int,int) - discard
		  	// removeColumn(TableColumn)
			super.createMethodDescriptor(getBeanClass(),"removeColumn",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("RemoveColumn.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("RemoveColumn.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("column", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("RemoveColumn.column.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Column to remove",
	      				})
	      		},
	      		new Class[] {
	      			javax.swing.table.TableColumn.class
	      		}		    		
		  	),
		  	// removeColumnSelectionInterval(int,int)
			super.createMethodDescriptor(getBeanClass(),"removeColumnSelectionInterval",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("RemoveColumnSelectionInterva.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("RemoveColumnSelectionInterva.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index0", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("RemoveColumnSelectionInterva.startIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Starting index",
	      				}
	      			),
	      			createParameterDescriptor("index1", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("RemoveColumnSelectionInterva.endIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "End index",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
			// removeEditor - discard
		  	// removeRowSelectionInterval(int,int)
			super.createMethodDescriptor(getBeanClass(),"removeRowSelectionInterval",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("RemoveRowSelectionInterval.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("RemoveRowSelectionInterval.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index0", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("RemoveRowSelectionInterval.startIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Starting index",
	      				}),
	      			createParameterDescriptor("index1", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("RemoveRowSelectionInterval.endIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "End index",
	      				})
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
			// rowAtPoint(Point)
			super.createMethodDescriptor(getBeanClass(),"rowAtPoint",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("RowAtPoint.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Get the index of row that point lies in",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("point", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("RowAtPoint.aPoint.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Point",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Point.class
	      		}		    		
		  	),
		  	// selectAll()
			super.createMethodDescriptor(getBeanClass(),"selectAll",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SelectAll.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("SelectAll.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {},
	      		new Class[] {}		    		
		  	),
		  	// setAutoCreateColumnsFromModel(boolean)
			super.createMethodDescriptor(getBeanClass(),"setAutoCreateColumnsFromModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetAutoCreateColumnsFromModel.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("SetAutoCreateColumnsFromModel.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetAutoCreateColumnsFromModel.aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE to create columns from model",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setAutoResizeMode(int)
			super.createMethodDescriptor(getBeanClass(),"setAutoResizeMode",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetAutoResizeMode.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("SetAutoResizeMode.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("int", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetAutoResizeMode.newMode.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "AUTO_RESIZE_ALL_COLUMNS...",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setCellEditor(TableCellEditor)
			super.createMethodDescriptor(getBeanClass(),"setCellEditor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetCellEditor.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the cell editor",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("cellEditor", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetCellEditor.cellEditor.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Table cell editor",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.table.TableCellEditor.class
	      		}		    		
		  	),
		  	// setCellSelectionEnabled(boolean)
			super.createMethodDescriptor(getBeanClass(),"setCellSelectionEnabled",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetCellSelectionEnabled.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("SetCellSelectionEnabled.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetCellSelectionEnabled.aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE for simultaneous row/column selection",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setColumnModel(TableColumnModel)
			super.createMethodDescriptor(getBeanClass(),"setColumnModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetColumnModel.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("SetColumnModel.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("model", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetColumnModel.aModel.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Table column model",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.table.TableColumnModel.class
	      		}		    		
		  	),
		  	// setColumnSelectionAllowed(boolean)
			super.createMethodDescriptor(getBeanClass(),"setColumnSelectionAllowed",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetColumnSelectionAllowed.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("SetColumnSelectionAllowed.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetColumnSelectionAllowed.aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE for column selection",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setColumnSelectionInterval(int,int)
			super.createMethodDescriptor(getBeanClass(),"setColumnSelectionInterval",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetColumnSelectionInterval.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("SetColumnSelectionInterval.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index0", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetColumnSelectionInterval.startIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Starting index",
	      				}),
	      			createParameterDescriptor("index1", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetColumnSelectionInterval.endIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Ending index",
	      				})
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// setDefaultEditor(Class,TableCellEditor)
			super.createMethodDescriptor(getBeanClass(),"setDefaultEditor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetDefaultEditor.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the default editor for a data type",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("class", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetDefaultEditor.columnClass.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "data class",
	      				}),
	      			createParameterDescriptor("editor", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetDefaultEditor.Editor.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Table cell editor",
	      				})
	      		},
	      		new Class[] {
	      			java.lang.Class.class, 
	      			javax.swing.table.TableCellEditor.class
	      		}		    		
		  	),
		  	// setDefaultRenderer(Class,TableCellRenderer)
			super.createMethodDescriptor(getBeanClass(),"setDefaultRenderer",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetDefaultRenderer.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the default renderer for a data type",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("class", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetDefaultRenderer.columnClass.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "data class",
	      				}),
	      			createParameterDescriptor("renderer", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetDefaultRenderer.renderer.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Table cell renderer",
	      				})
	      		},
	      		new Class[] {
	      			java.lang.Class.class, 
	      			javax.swing.table.TableCellRenderer.class
	      		}		    		
		  	),
		  	// setEditingColumn(int)
			super.createMethodDescriptor(getBeanClass(),"setEditingColumn",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetEditingColumn.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the edited column",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetEditingColumn.index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of column",
	      				})
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setEditingRow(int)
			super.createMethodDescriptor(getBeanClass(),"setEditingRow",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetEditingRow.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the edited row",
	      		EXPERT, Boolean.TRUE,
	      		OBSCURE, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetEditingRow.index.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Index of row",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setGridColor(Color)
			super.createMethodDescriptor(getBeanClass(),"setGridColor",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetGridColor.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the color of grid lines",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("color", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetGridColor.color.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Color",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Color.class
	      		}		    		
		  	),
		  	// setIntercellSpacing(Dimension)
			super.createMethodDescriptor(getBeanClass(),"setIntercellSpacing",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetIntercellSpacing.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the width and height between cells",
	      		EXPERT, Boolean.TRUE,
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("dim", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetIntercellSpacing.dimension.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Dimensions",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Dimension.class
	      		}		    		
		  	),
		  	// setModel(TableModel)
			super.createMethodDescriptor(getBeanClass(),"setModel",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetModel.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("SetModel.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("model", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetModel.aModel.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Table model",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.table.TableModel.class
	      		}		    		
		  	),
			// setPreferredScrollableViewportSize(Dimension)
			super.createMethodDescriptor(getBeanClass(),"setPreferredScrollableViewportSize",  //$NON-NLS-1$
				new Object[] {
				DISPLAYNAME, resJTable.getString("SetPreferredScrollableViewportSize.Name"), //$NON-NLS-1$
				// SHORTDESCRIPTION, "Set the width and height between cells",
				EXPERT, Boolean.TRUE,
				}, 
				new ParameterDescriptor[] {
					createParameterDescriptor("dim", new Object[] { //$NON-NLS-1$
						DISPLAYNAME, resJTable.getString("SetPreferredScrollableViewportSize.dimension.Name"), //$NON-NLS-1$
						// SHORTDESCRIPTION, "Dimensions",
						}
					)
				},
				new Class[] {
					java.awt.Dimension.class
				}		    		
			),
		  	// setRowHeight(int)
			super.createMethodDescriptor(getBeanClass(),"setRowHeight",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetRowHeight.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the height of each row",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("height", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetRowHeight.height.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Row height in pixels",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			int.class
	      		}		    		
		  	),
		  	// setRowSelectionAllowed(boolean)
			super.createMethodDescriptor(getBeanClass(),"setRowSelectionAllowed",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetRowSelectionAllowed.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("SetRowSelectionAllowed.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetRowSelectionAllowed.aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE for row selection",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setRowSelectionInterval(int,int)
			super.createMethodDescriptor(getBeanClass(),"setRowSelectionInterval",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetRowSelectionInterval.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("SetRowSelectionInterval.Desc") //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("index0", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetRowSelectionInterval.startIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Starting index",
	      				}),
	      			createParameterDescriptor("index1", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetRowSelectionInterval.endIndex.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Ending index",
	      				})
	      		},
	      		new Class[] {
	      			int.class, int.class
	      		}		    		
		  	),
		  	// setSelectionBackground(Color)
			super.createMethodDescriptor(getBeanClass(),"setSelectionBackground",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetSelectionBackground.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("SetSelectionBackground.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("color", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetSelectionBackground.color.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Color",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Color.class
	      		}		    		
		  	),
		  	// setSelectionForeground(Color)
			super.createMethodDescriptor(getBeanClass(),"setSelectionForeground",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetSelectionForeground.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the foreground color of selected cells",
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("color", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetSelectionForeground.color.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Color",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.awt.Color.class
	      		}		    		
		  	),
		  	// setSelectionMode(int)
			super.createMethodDescriptor(getBeanClass(),"setSelectionMode",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetSelectionMode.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the selection mode",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("mode", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetSelectionMode.mode.Name"), //$NON-NLS-1$
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
	   			DISPLAYNAME, resJTable.getString("SetSelectionModel.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the row selection model",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("model", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetSelectionModel.aModel.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "List selection model",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.ListSelectionModel.class
	      		}		    		
		  	),
		  	// setShowGrid(boolean)
			super.createMethodDescriptor(getBeanClass(),"setShowGrid",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetShowGrid.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("SetShowGrid.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetShowGrid.aBoo.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE for grid lines",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setShowHorizontalLines(boolean)
			super.createMethodDescriptor(getBeanClass(),"setShowHorizontalLines",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetShowHorizontalLines.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE to show horizontal grid lines",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetShowHorizontalLines.aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE for horizontal grid lines",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setShowVerticalLines(boolean)
			super.createMethodDescriptor(getBeanClass(),"setShowVerticalLines",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetShowVerticalLines.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE to show vertical grid lines",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetShowVerticalLines.aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE for vertical grid lines",
	      				})
	      		},
	      		new Class[] {
	      			boolean.class
	      		}		    		
		  	),
		  	// setTableHeader(JTableHeader)
			super.createMethodDescriptor(getBeanClass(),"setTableHeader",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetTableHeader.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the table header for rendering column headers",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("newHeader", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetTableHeader.aHeader.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Table header",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.table.JTableHeader.class
	      		}		    		
		  	),
		  	// setUI(TableUI)
			super.createMethodDescriptor(getBeanClass(),"setUI",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetUI.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "Set the table UI",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("ui", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetUI.aUI.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Table UI",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			javax.swing.plaf.TableUI.class
	      		}		    		
		  	),
		  	// setValueAt(Object,int,int)
			super.createMethodDescriptor(getBeanClass(),"setValueAt",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SetValueAt.Name"), //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("SetValueAt.Desc"), //$NON-NLS-1$
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("aValue", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetValueAt.aValue.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Cell value",
	      				}
	      			),
	    			createParameterDescriptor("row", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetValueAt.row.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Row index",
	      				}
	      			),
	      			createParameterDescriptor("column", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SetValueAt.column.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "Column index",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			java.lang.Object.class,int.class, int.class
	      		}		    		
		  	),
		  	// sizeColumnsToFit(boolean)
			super.createMethodDescriptor(getBeanClass(),"sizeColumnsToFit",  //$NON-NLS-1$
				new Object[] {
	   			DISPLAYNAME, resJTable.getString("SizeColumnsToFit.Name"), //$NON-NLS-1$
	      		// SHORTDESCRIPTION, "TRUE to resize columns to fit table width",
	      		EXPERT, Boolean.TRUE
	    		}, 
	    		new ParameterDescriptor[] {
	    			createParameterDescriptor("b", new Object[] { //$NON-NLS-1$
	   					DISPLAYNAME, resJTable.getString("SizeColumnsToFit.aBool.Name"), //$NON-NLS-1$
	      				// SHORTDESCRIPTION, "TRUE for resize columns",
	      				}
	      			)
	      		},
	      		new Class[] {
	      			boolean.class
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
	    	// autoCreateColumnsFromModel
			super.createPropertyDescriptor(getBeanClass(),"autoCreateColumnsFromModel", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("autoCreateColumnsFromModel.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("AutoCreateColumnsFromModel.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// autoResizeMode
			super.createPropertyDescriptor(getBeanClass(),"autoResizeMode", new Object[] { //$NON-NLS-1$
	      		SHORTDESCRIPTION, resJTable.getString("AutoCreateColumnsFromModel.autoResizeMode.Desc"), //$NON-NLS-1$
	      		IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			resJTable.getString("AutoResize.Off"), new Integer(javax.swing.JTable.AUTO_RESIZE_OFF), //$NON-NLS-1$
	      				"javax.swing.JTable.AUTO_RESIZE_OFF", //$NON-NLS-1$
	      			resJTable.getString("AutoResize.Next_Column"), new Integer(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN), //$NON-NLS-1$
	      				"javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN", //$NON-NLS-1$
	      			resJTable.getString("AutoResize.Subsequent_Columns"), new Integer(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS), //$NON-NLS-1$
	      				"javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS", //$NON-NLS-1$
	      			resJTable.getString("AutoResize.Last_Column"), new Integer(javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN), //$NON-NLS-1$
	      				"javax.swing.JTable.AUTO_RESIZE_LAST_COLUMN", //$NON-NLS-1$
	      			resJTable.getString("AutoResize.All_Columns"), new Integer(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS), //$NON-NLS-1$
	      				"javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS" //$NON-NLS-1$
	    			}
	      		}
	    	),
			// cellEditor
			super.createPropertyDescriptor(getBeanClass(),"cellEditor", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("cellEditor.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("CellEditor.Desc"), //$NON-NLS-1$
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE
			EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// cellSelectionEnabled
	    	super.createPropertyDescriptor(getBeanClass(),"cellSelectionEnabled", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("cellSelectionEnabled.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("CellSelectionEnabled.Desc"), //$NON-NLS-1$
				}   
	    	),
	    	// columnCount
	    	super.createPropertyDescriptor(getBeanClass(),"columnCount", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("columnCount.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("ColumnCount.Desc"), //$NON-NLS-1$
	      		}   
	    	),
	    	// columnModel
	    	super.createPropertyDescriptor(getBeanClass(),"columnModel", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("columnModel.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("columnModel.Desc"), //$NON-NLS-1$
			DESIGNTIMEPROPERTY, Boolean.FALSE,
	      		}   
	    	),
	    	// columnSelectionAllowed
	    	super.createPropertyDescriptor(getBeanClass(),"columnSelectionAllowed", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("columnSelectionAllowed.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("ColumnSelectionAllowed.Desc"), //$NON-NLS-1$
	      		}   
	    	),
			// editing
			super.createPropertyDescriptor(getBeanClass(),"editing", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("editing.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("Editing.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE
	    		}
	    	),
	    	// editingColumn
			super.createPropertyDescriptor(getBeanClass(),"editingColumn", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("editingColumn.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("editingColumn.Desc"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// editingRow
			super.createPropertyDescriptor(getBeanClass(),"editingRow", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("editingRow.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("editingRow.Desc"), //$NON-NLS-1$
	      	DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// editorComponent
			super.createPropertyDescriptor(getBeanClass(),"editorComponent", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("editorComponent.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("editorComponent.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	OBSCURE, Boolean.TRUE
	    		}
	    	),
	    	// gridColor
			super.createPropertyDescriptor(getBeanClass(),"gridColor", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("gridColor.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("GridColor.Desc"), //$NON-NLS-1$
	    		}
	    	),
	    	// intercellSpacing
			super.createPropertyDescriptor(getBeanClass(),"intercellSpacing", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("intercellSpacing.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("IntercellSpacing.Desc"), //$NON-NLS-1$
	    		}
	    	),
			// layout - hide it
			super.createPropertyDescriptor(getBeanClass(),"layout", new Object[] { //$NON-NLS-1$
	      	HIDDEN, Boolean.TRUE
	    		}
	    	),
	    	// model
			super.createPropertyDescriptor(getBeanClass(),"model", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("model.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("Model.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	PREFERRED, Boolean.TRUE,
	    		}
	    	),
	    	// preferredScrollableViewportSize
			super.createPropertyDescriptor(getBeanClass(),"preferredScrollableViewportSize", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("preferredScrollableViewportSize.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("preferredScrollableViewportSize.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,      	
	    		}
	    	),
	    	// rowCount
			super.createPropertyDescriptor(getBeanClass(),"rowCount", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("rowCount.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("RowCount.Desc"),      	 //$NON-NLS-1$
	    		}
	    	),
	    	// rowHeight
			super.createPropertyDescriptor(getBeanClass(),"rowHeight", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("rowHeight.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("RowHeight.Desc"),	      	 //$NON-NLS-1$
	    		}
	    	),
	    	// rowSelectionAllowed
	    	super.createPropertyDescriptor(getBeanClass(),"rowSelectionAllowed", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("rowSelectionAllowed.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("RowSelectionAllowed.Desc"), //$NON-NLS-1$
	      		}   
	    	),
	    	// scrollableTracksViewportHeight
			super.createPropertyDescriptor(getBeanClass(),"scrollableTracksViewportHeight", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("scrollableTracksViewportHeight.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("scrollableTracksViewportHeight.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	OBSCURE, Boolean.TRUE	      	
	    		}
	    	),
	    	// scrollableTracksViewportWidth
			super.createPropertyDescriptor(getBeanClass(),"scrollableTracksViewportWidth", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("scrollableTracksViewportWidth.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("scrollableTracksViewportWidth.Desc"), //$NON-NLS-1$
	      	EXPERT, Boolean.TRUE,
	      	OBSCURE, Boolean.TRUE	      	
	    		}
	    	),
	    	// selectedColumn
			super.createPropertyDescriptor(getBeanClass(),"selectedColumn", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("selectedColumn.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("selectedColumn.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE	      	
	    		}
	    	),
	    	// selectedColumnCount
			super.createPropertyDescriptor(getBeanClass(),"selectedColumnCount", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("selectedColumnCount.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("SelectedColumnCount.Desc"),	      	 //$NON-NLS-1$
	    		}
	    	),
	    	// selectedColumns
			super.createPropertyDescriptor(getBeanClass(),"selectedColumns", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("selectedColumns.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("SelectedColumns.Desc"),     	 //$NON-NLS-1$
	    		}
	    	),
	    	// selectedRow
			super.createPropertyDescriptor(getBeanClass(),"selectedRow", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("selectedRow.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("SelectedRow.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE	      	
	    		}
	    	),
	    	// selectedRowCount
			super.createPropertyDescriptor(getBeanClass(),"selectedRowCount", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("selectedRowCount.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("SelectedRowCount.Desc"),	      	 //$NON-NLS-1$
	    		}
	    	),
	    	// selectedRows
			super.createPropertyDescriptor(getBeanClass(),"selectedRows", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("selectedRows.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("SelectedRows.Desc"),      	 //$NON-NLS-1$
	    		}
	    	),
	    	// selectionBackground
			super.createPropertyDescriptor(getBeanClass(),"selectionBackground", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("selectionBackground.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("SelectionBackground.Desc"),	      	 //$NON-NLS-1$
	    		}
	    	),
	    	// selectionForeground
			super.createPropertyDescriptor(getBeanClass(),"selectionForeground", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("selectionforeground.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("selectionforeground.Desc"),	 //$NON-NLS-1$
	    		}
	    	),
	    	// selectionMode
			super.createPropertyDescriptor(getBeanClass(),"selectionMode", new Object[] { //$NON-NLS-1$
			DISPLAYNAME, resJTable.getString("selectionMode.Desc"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("SelectionMode.Desc"), //$NON-NLS-1$
	      	IBaseBeanInfoConstants.ENUMERATIONVALUES, new Object[] {
	      			resJTable.getString("SelectionMode.SINGLE_SELECTION"), new Integer(javax.swing.ListSelectionModel.SINGLE_SELECTION), //$NON-NLS-1$
	      				"javax.swing.ListSelectionModel.SINGLE_SELECTION", //$NON-NLS-1$
					resJTable.getString("SelectionMode.SINGLE_INTERVAL_SELECTION"), new Integer(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION), //$NON-NLS-1$
	      				"javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION", //$NON-NLS-1$
	      			resJTable.getString("SelectionMode.MULTIPLE_INTERVAL_SELECTION"), new Integer(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION), //$NON-NLS-1$
	      				"javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION", //$NON-NLS-1$
	    			}	      	
	    		}
	    	),
	    	// selectionModel
			super.createPropertyDescriptor(getBeanClass(),"selectionModel", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("selectionModel.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("SelectionModel.Desc"), //$NON-NLS-1$
	      	BOUND, Boolean.TRUE,
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE,
			EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// showGrid
			super.createPropertyDescriptor(getBeanClass(),"showGrid", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("showGrid.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("ShowGrid.Desc"), //$NON-NLS-1$
	      	PREFERRED, Boolean.TRUE	      	
	    		}
	    	),
	    	// showHorizontalLines
			super.createPropertyDescriptor(getBeanClass(),"showHorizontalLines", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("showHorizontalLines.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("ShowHorizontalRules.Desc"),     	 //$NON-NLS-1$
	    		}
	    	),
	    	// showVerticalLines
			super.createPropertyDescriptor(getBeanClass(),"showVerticalLines", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("showVerticalLines.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("ShowVerticalRules.Desc"),      	 //$NON-NLS-1$
	    		}
	    	),
	    	// tableHeader
			super.createPropertyDescriptor(getBeanClass(),"tableHeader", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("tableHeader.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("tableHeader.Desc"), //$NON-NLS-1$
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE,
	      	EXPERT, Boolean.TRUE
	    		}
	    	),
	    	// ui
			super.createPropertyDescriptor(getBeanClass(),"UI", new Object[] { //$NON-NLS-1$
	      	DISPLAYNAME, resJTable.getString("ui.Name"), //$NON-NLS-1$
	      	SHORTDESCRIPTION, resJTable.getString("ui.Desc"), //$NON-NLS-1$
	      	//DESIGNTIMEPROPERTY, Boolean.FALSE,
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
