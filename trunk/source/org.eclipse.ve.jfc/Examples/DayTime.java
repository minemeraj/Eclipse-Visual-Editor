import java.awt.Frame;
public class DayTime extends Frame {

     private com.ibm.calendar.Calendar calendar = null;
     private com.ibm.clock.IClock iClock = null;
     private com.ibm.clock.IClock iClock1 = null;
	/**
	 * This method initializes 
	 * 
	 */
	public DayTime() {
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
        java.awt.GridBagConstraints consGridBagConstraints8 = new java.awt.GridBagConstraints();
        java.awt.GridBagConstraints consGridBagConstraints7 = new java.awt.GridBagConstraints();
        java.awt.GridBagConstraints consGridBagConstraints9 = new java.awt.GridBagConstraints();
        consGridBagConstraints8.gridy = 0;
        consGridBagConstraints8.gridx = 1;
        consGridBagConstraints9.gridy = 1;
        consGridBagConstraints9.gridx = 1;
        consGridBagConstraints9.anchor = java.awt.GridBagConstraints.NORTH;
        consGridBagConstraints9.insets = new java.awt.Insets(5,5,5,5);
        consGridBagConstraints8.fill = java.awt.GridBagConstraints.BOTH;
        consGridBagConstraints8.insets = new java.awt.Insets(5,5,5,5);
        consGridBagConstraints8.weighty = 1.0D;
        consGridBagConstraints8.weightx = 1.0D;
        consGridBagConstraints7.gridy = 0;
        consGridBagConstraints7.gridx = 0;
        consGridBagConstraints7.gridheight = 2;
        consGridBagConstraints7.insets = new java.awt.Insets(5,5,5,5);
        consGridBagConstraints7.anchor = java.awt.GridBagConstraints.NORTH;
        this.setLayout(new java.awt.GridBagLayout());
        this.add(getCalendar(), consGridBagConstraints7);
        this.add(getIClock(), consGridBagConstraints8);
        this.add(getIClock1(), consGridBagConstraints9);
        this.setSize(376, 235);
        this.setTitle("Day-Time");
        this.addWindowListener(new java.awt.event.WindowAdapter() { 
        	public void windowClosed(java.awt.event.WindowEvent e) {    
        		System.exit(0);
        	}
        });
			
	}
	public static void main(String[] args) {
		new DayTime().show();
	}
	/**
	 * This method initializes calendar
	 * 
	 * @return com.ibm.calendar.Calendar
	 */
	private com.ibm.calendar.Calendar getCalendar() {
		if(calendar == null) {
			calendar = new com.ibm.calendar.Calendar();
			calendar.setInputTheme(null);
			calendar.setHolidays("/1/1,/5/1/-1,/11/4/-1");
		}
		return calendar;
	}
	/**
	 * This method initializes iClock
	 * 
	 * @return com.ibm.clock.IClock
	 */
	private com.ibm.clock.IClock getIClock() {
		if(iClock == null) {
			iClock = new com.ibm.clock.IClock();
			iClock.setAnalogNumeralStyle(0);
		}
		return iClock;
	}
	/**
	 * This method initializes iClock1
	 * 
	 * @return com.ibm.clock.IClock
	 */
	private com.ibm.clock.IClock getIClock1() {
		if(iClock1 == null) {
			iClock1 = new com.ibm.clock.IClock();
			iClock1.setDisplayMode(5);
			iClock1.setDigitalDisplayStyle(1);
			iClock1.setDigitalBackGroundColor(java.awt.Color.white);
		}
		return iClock1;
	}
}  //  @jve:visual-info  decl-index=0 visual-constraint="0,0"
