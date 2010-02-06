package ggc.plugin.gui.file;

import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

public class ImportFileSelectorPanel extends AbstractFileSelectorPanel 
{
	private static final long serialVersionUID = -4620972246237384499L;
	DataAccessPlugInBase m_da = null;
	I18nControlAbstract m_ic = null;
	JDialog dialog_parent = null;
	JPanel previous = null;
	
	
	
	public ImportFileSelectorPanel(DataAccessPlugInBase da, JDialog dialog, JPanel previous_panel)
	{
		super();
		m_da = da;
		m_ic = da.getI18nControlInstance();
		this.dialog_parent = dialog;
		this.previous = previous_panel;
		init();
	}

	
	public void init()
	{
		ATSwingUtils.initLibrary();
		this.setLayout(null);
		
		JLabel l = ATSwingUtils.getTitleLabel("Import File Selector", 50, 30, 300, 30, this, ATSwingUtils.FONT_BIG_BOLD);
		l.setHorizontalAlignment(JLabel.CENTER);
		
		
		ATSwingUtils.getLabel("<html><body>" +
				"Please select import file of selected type. " +
				"</body></html>", 
							  50, 60, 300, 60, this, ATSwingUtils.FONT_NORMAL);
		
		ATSwingUtils.getLabel("Selected type:", 
							  50, 95, 300, 60, this, ATSwingUtils.FONT_NORMAL_BOLD);
		
		ATSwingUtils.getLabel("TTXZ", 
				  50, 115, 300, 60, this, ATSwingUtils.FONT_NORMAL);

		
		ATSwingUtils.getLabel("Select file:", 
				  50, 150, 300, 60, this, ATSwingUtils.FONT_NORMAL_BOLD);
		
		JTextField tf = ATSwingUtils.getTextField("", 50, 195, 300, 25, this);
		tf.setEnabled(false);
		
		
		/*JButton b =*/ 
		ATSwingUtils.getButton("Browse", 250, 170, 100, 20, this, ATSwingUtils.FONT_NORMAL, null, "browse", this, m_da);
		
		//String[] fd = { "Test 1", "Test 2" };
		
		//ATSwingUtils.getComboBox(fd, 50, 180, 300, 25, this, ATSwingUtils.FONT_NORMAL);
	
		/*
		ATSwingUtils.getButton("NEXT", 
				x, y, width, height, 
				cont, font, icon_name, 
				action_cmd, al, da, 
				icon_size);
		*/
		
        this.help_button = m_da.createHelpIconByBounds(50, 250, 60, 25, this);
        //help_button.setFont(normal);
        this.add(help_button);
       
        //m_da.enableHelp(this);
        
        ATSwingUtils.getButton("" /*m_ic.getMessage("CANCEL")*/, 120, 250, 60, 25, this, 
            ATSwingUtils.FONT_NORMAL, "cancel.png", "cancel", this, m_da);
        

        ATSwingUtils.getButton("" /*m_ic.getMessage("NEXT")*/, 290, 250, 60, 25, this, 
            ATSwingUtils.FONT_NORMAL, "nav_right_blue.png", "next", this, m_da);
		
		this.setBounds(0, 0, 400, 320);

	}
	
	public int[] getMinSize()
	{
		int[] sizes = new int[2];
		sizes[0] = 400;
		sizes[1] = 340;
		
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
