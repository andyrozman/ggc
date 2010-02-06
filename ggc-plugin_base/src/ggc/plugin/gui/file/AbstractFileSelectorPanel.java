package ggc.plugin.gui.file;

import java.awt.Component;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.atech.help.HelpCapable;

public abstract class AbstractFileSelectorPanel extends JPanel implements ActionListener, HelpCapable 
{

	private static final long serialVersionUID = -7597376030031612506L;
	JButton help_button = null;
	
	public abstract int[] getPanelSizes();
	
	
	public Component getComponent() 
	{
		return this;
	}

	public JButton getHelpButton() 
	{
		return this.help_button;
	}
	
	
}
