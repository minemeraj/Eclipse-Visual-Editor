import javax.swing.table.AbstractTableModel;

/**
 * @author JoeWin
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ToDoListModel extends AbstractTableModel {
	
     String[][] data = { {"",""}, {"",""}, {"",""}, {"",""},
          {"",""}, {"",""}, {"",""}, {"",""},
          {"",""}, {"",""}, {"",""}, {"",""},
          {"",""}, {"",""}, {"",""}, {"",""},
          {"",""}, {"",""}, {"",""}, {"",""},
          {"",""}, {"",""}, {"",""}, {"",""},
          {"",""}, {"",""}, {"",""}, {"",""},
          {"",""}, {"",""}, {"",""}, {"",""} };	

	public Object getValueAt(int rowIndex, int columnIndex) {
		return data[rowIndex][columnIndex];
	}

	public int getColumnCount() {
		return 0;
	}

	public int getRowCount() {
		return data.length;
	}

    public String getColumnName( int c ) {
    	return( "" ); 
    }                 
    
    public void setValueAt(Object arg0,int arg1,int arg2) {                   	
    	data[ arg1 ][ arg2 ] = ( String ) arg0;
        fireTableDataChanged();
    }
}
