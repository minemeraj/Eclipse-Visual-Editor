/**
 * This JavaBean throws an error on its null constructor and is designed
 * to test dropping and extending it with the JVE
 * It is a java.awt.Button subclass to test dropping it onto a container
 */
public class CannotInstantiateButton extends java.awt.Button {
	
	public CannotInstantiateButton(){
		throw new RuntimeException("Can't create me !!");
	}

}

