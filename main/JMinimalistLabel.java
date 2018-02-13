package main;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;

public class JMinimalistLabel extends JLabel{
	public JMinimalistLabel(String str){
		super();
		Border empty;
		empty = BorderFactory.createEmptyBorder();
		setBorder(empty);
		setVerticalAlignment(SwingConstants.CENTER);
		setHorizontalAlignment(SwingConstants.CENTER);
		
		setText(str);
	}
}
