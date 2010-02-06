package ggc.plugin.gui.file;

import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

public class MultipleFileSelectorPanel extends AbstractFileSelectorPanel 
{
	private static final long serialVersionUID = -876338378352653634L;
	DataAccessPlugInBase m_da = null;
	I18nControlAbstract m_ic = null;
	JDialog dialog_parent = null;
	
	public MultipleFileSelectorPanel(DataAccessPlugInBase da, JDialog dialog)
	{
		super();
		m_da = da;
		m_ic = da.getI18nControlInstance();
		this.dialog_parent = dialog;
		init();
	}
	
	public void init()
	{
		ATSwingUtils.initLibrary();
		this.setLayout(null);
		
		JLabel l = ATSwingUtils.getTitleLabel("Multiple Import Selector", 50, 30, 300, 30, this, ATSwingUtils.FONT_BIG_BOLD);
		l.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel r = new JPanel();
		r.setBounds(50, 30, 300, 30);
		r.setBackground(Color.red);
		//this.add(r);
		
		
		ATSwingUtils.getLabel("<html><body>" +
				"This device has several possible import types. " +
				"Please select correct import type, for import " +
				"type you have available. " +
				"</body></html>", 
							  50, 60, 300, 120, this, ATSwingUtils.FONT_NORMAL);
		
		String[] fd = { "Test 1", "Test 2" };
		
		ATSwingUtils.getComboBox(fd, 50, 180, 300, 25, this, ATSwingUtils.FONT_NORMAL);
	
		/*
		ATSwingUtils.getButton("NEXT", 
				x, y, width, height, 
				cont, font, icon_name, 
				action_cmd, al, da, 
				icon_size);
		*/
		
        this.help_button = m_da.createHelpIconByBounds(50, 230, 60, 25, this);
        //help_button.setFont(normal);
        this.add(help_button);
       
        //m_da.enableHelp(this);
        
        ATSwingUtils.getButton("" /*m_ic.getMessage("CANCEL")*/, 120, 230, 60, 25, this, 
            ATSwingUtils.FONT_NORMAL, "cancel.png", "cancel", this, m_da);
        

        ATSwingUtils.getButton("" /*m_ic.getMessage("NEXT")*/, 290, 230, 60, 25, this, 
            ATSwingUtils.FONT_NORMAL, "nav_right_blue.png", "next", this, m_da);
		
		this.setBounds(0, 0, 400, 320);

	}
	
	public int[] getMinSize()
	{
		int[] sizes = new int[2];
		sizes[0] = 400;
		sizes[1] = 320;
		
		return sizes; 
	}
	
	
	
/*	
	public static void main(String[] args)
	{
		
		JDialog d = new JDialog();
		MultipleFileSelectorPanel p = new MultipleFileSelectorPanel();
		
		d.getContentPane().add(p);
		
		d.setBounds(20, 20, p.getMinSize()[0], p.getMinSize()[1]);
		d.setVisible(true);
		
		
		
		
		
	}*/

	public void actionPerformed(ActionEvent ae) 
	{
		// TODO Auto-generated method stub
		
		String action = ae.getActionCommand();
		
		if (action.equals("cancel"))
		{
			this.dialog_parent.dispose();
		}
		
		
		
		
	}

	@Override
	public int[] getPanelSizes() 
	{
		return null;
	}
	
	
	public String getHelpId() 
	{
		return null;
	}
	
	
	

}
