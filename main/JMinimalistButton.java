package main;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class JMinimalistButton extends JButton implements ActionListener{

    public JMinimalistButton() {
    	super();
    	setFont(new Font("Arial", Font.BOLD, 12));
    	setForeground(new Color(30,30,30));
        //setContentAreaFilled(false);
        addActionListener(this);
        setFocusable(false);
        setBorderPainted(false);
        UIManager.put("Button.select", new Color(200,200,200));
        
        setBackground(new Color(250,250,250));
        
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(new Color(224,224,224));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(new Color(250,250,250));
            }
        });
    }

    @Override
    public void actionPerformed (ActionEvent ev) {
        System.out.println("Minimalist Button Event");
    }
}
