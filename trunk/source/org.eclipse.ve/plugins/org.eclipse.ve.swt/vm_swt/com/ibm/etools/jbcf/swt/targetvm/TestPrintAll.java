package com.ibm.etools.jbcf.swt.targetvm;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class TestPrintAll {
	
	public static void main(String[] args) {
		
		final Frame frame = new Frame("Title");
		MyPanel panel = new MyPanel();
		panel.setLayout(null);
		frame.add(panel);
		JButton button = new JButton("OK");
		button.setLocation(20,20);
		button.setSize(button.getPreferredSize());
		panel.add(button);
		frame.setBounds(20,20,200,200);
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				System.exit(0);
			}
		});

		
		JFrame grabberFrame = new JFrame("Grabber");
		grabberFrame.getContentPane().setLayout(new GridBagLayout());
		JButton grabButton = new JButton("Grab image");
		GridBagConstraints constraint = new GridBagConstraints();
		grabberFrame.getContentPane().add(grabButton,constraint);
		constraint.gridy = 1;
		constraint.weighty = 1.0;
		final JLabel label = new JLabel();
		label.setBackground(java.awt.Color.WHITE);
		grabberFrame.getContentPane().add(label,constraint);
		grabberFrame.setBounds(250,20,250,250);
		grabberFrame.setVisible(true);
		
		grabButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Image image = frame.createImage(frame.getSize().width, frame.getSize().height);				
				Graphics g = image.getGraphics();
				frame.printAll(g);
				// Now display the image in the 
				ImageIcon imageIcon = new ImageIcon(image);
				label.setIcon(imageIcon);
				label.revalidate();
			}
		});		
	}
}
