package main;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;

public class JMinimalistField extends JTextField{
	public JMinimalistField(){
		super();
		Border empty;
		empty = BorderFactory.createEmptyBorder();
		setBorder(empty);
	}
}
