/**
 * This JavaBean throws an error on its null constructor and is designed
 * to test dropping and extending it with the JVE
 */
public class CannotInstantiate {
	private String fName;
	
	public CannotInstantiate(){
		throw new RuntimeException("Can't create me !!");
	}
	
	public String getName(){
		return fName;
	}
	public void setName(String aName){
		fName = aName;
	}

}

