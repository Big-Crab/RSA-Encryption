package main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.UIManager;

public class JMinimalistCheckbox extends JCheckBox implements ActionListener{
	public JMinimalistCheckbox(){
		super();
		setForeground(new Color(30,30,30));
		addActionListener(this);
		setFocusable(false);
		setBorderPainted(false);
		try{
			String imgName = "/assets/cbox-icon.png"; 
			InputStream in = getClass().getResourceAsStream(imgName); 
			ImageIcon img = new ImageIcon(ImageIO.read(in));
			setIcon(img);	

			imgName = "/assets/cbox-sel.png"; 
			in = getClass().getResourceAsStream(imgName); 
			img = new ImageIcon(ImageIO.read(in));
			setSelectedIcon(img);
		} catch (IOException ioe){
			
		}
		
	}
	@Override
	public void actionPerformed (ActionEvent ev) {
		System.out.println("Minimalist Checkbox Event");
	}
}
