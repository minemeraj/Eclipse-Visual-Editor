/**
 * This JavaBean has a String property name
 * If this property is longer than one character then it throws
 * an exception
 */
public class CannotApplyName {
	
	public CannotApplyName(){
	}

	protected String fName;
	protected String fOtherName;

	public String getName(){
		return fName;
	}
	
	public void setName(String aName){
		if ( aName != null && aName.length() > 1){
			throw new RuntimeException("Name length cannot be larger than one character");
		} else {
			fName = aName;
		}
	}
	
	public void setOtherName(String otherName){
		if ( otherName != null && otherName.length() > 1){
			throw new RuntimeException("OtherName length cannot be larger than one character");
		} else {
			fOtherName = otherName;
		}		
	}
	
	public String getOtherName(){
		return fOtherName;
	}

}