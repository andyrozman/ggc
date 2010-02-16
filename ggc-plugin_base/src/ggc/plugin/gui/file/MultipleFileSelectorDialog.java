package ggc.plugin.gui.file;

import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.atech.utils.ATSwingUtils;
import com.atech.utils.file.FileReaderContext;

public class MultipleFileSelectorDialog extends AbstractFileSelectorDialog 
{
	private static final long serialVersionUID = -876338378352653634L;
	FileReaderContext[] file_contexts;
	JComboBox cb_contexts;
	
	public MultipleFileSelectorDialog(DataAccessPlugInBase da, JDialog dialog, DeviceDataHandler ddh)
	{
		super(da, ddh, dialog);
	}
	
	public void init()
	{
		ATSwingUtils.initLibrary();
		this.setLayout(null);
		
		JLabel l = ATSwingUtils.getTitleLabel(this.m_ic.getMessage("Multiple Import Selector"), 50, 30, 300, 30, this, ATSwingUtils.FONT_BIG_BOLD);
		l.setHorizontalAlignment(JLabel.CENTER);
		
		JPanel r = new JPanel();
		r.setBounds(50, 30, 300, 30);
		r.setBackground(Color.red);
		//this.add(r);
		
		
		ATSwingUtils.getLabel(m_ic.getMessage("MULTIPLE_IMPORT_SELECTOR_DESC"), 
							  50, 60, 300, 120, this, ATSwingUtils.FONT_NORMAL);
		
		file_contexts = this.m_ddh.getDeviceInterface().getFileDownloadTypes();
		
		
		cb_contexts = ATSwingUtils.getComboBox(file_contexts, 50, 180, 300, 25, this, ATSwingUtils.FONT_NORMAL);
	
        this.help_button = m_da.createHelpIconByBounds(50, 230, 60, 25, this);
        this.add(help_button);
       
        
        ATSwingUtils.getButton("" /*m_ic.getMessage("CANCEL")*/, 120, 230, 60, 25, this, 
            ATSwingUtils.FONT_NORMAL, "cancel.png", "cancel", this, m_da);
        

        ATSwingUtils.getButton("" /*m_ic.getMessage("NEXT")*/, 290, 230, 60, 25, this, 
            ATSwingUtils.FONT_NORMAL, "nav_right_blue.png", "next", this, m_da);
		
		this.setBounds(0, 0, 400, 320);

	}
	
	
	

	public void actionPerformed(ActionEvent ae) 
	{
		String action = ae.getActionCommand();
		
		if (action.equals("cancel"))
		{
			this.dialog_parent.dispose();
		}
		else if (action.equals("next"))
		{
		    m_ddh.selected_file_context = file_contexts[this.cb_contexts.getSelectedIndex()];
		    this.dispose();
		    new ImportFileSelectorDialog(m_da, this.dialog_parent, m_ddh);
		}
	}

	
	
	public String getHelpId() 
	{
		return null;
	}

	
    @Override
    public Dimension getSize()
    {
        return new Dimension(400, 320);
    }
	
	
	

}
