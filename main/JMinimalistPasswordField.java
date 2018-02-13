package main;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;

public class JMinimalistPasswordField extends JPasswordField{
	public JMinimalistPasswordField(){
		super();
		Border empty;
		empty = BorderFactory.createEmptyBorder();
		setBorder(empty);
	}
}
