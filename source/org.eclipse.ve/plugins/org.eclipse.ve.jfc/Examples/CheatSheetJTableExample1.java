
/**
 * 
 */
public class CheatSheetJTableExample1 extends javax.swing.JFrame {
	private javax.swing.JPanel jContentPane = null;
    private javax.swing.JScrollPane jScrollPane = null;
    private javax.swing.JTable jTable = null;
	public static void main(String[] args) {
		CheatSheetJTableExample1 example = new CheatSheetJTableExample1();
		example.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
		example.show();
	}
	/**
	 * This is the default constructor
	 */
	public CheatSheetJTableExample1() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setContentPane(getJContentPane());
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new java.awt.BorderLayout());
			jContentPane.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jTable
	 * 
	 * @return javax.swing.JTable
	 */
	private javax.swing.JTable getJTable() {
		if(jTable == null) {
			jTable = new javax.swing.JTable();
		}
		return jTable;
	}
	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private javax.swing.JScrollPane getJScrollPane() {
		if(jScrollPane == null) {
			jScrollPane = new javax.swing.JScrollPane();
			jScrollPane.setViewportView(getJTable());
		}
		return jScrollPane;
	}
}
