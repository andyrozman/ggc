package ggc.plugin.gui.file;

import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.gui.DeviceDisplayDataDialog;
import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.atech.utils.ATSwingUtils;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     ----  
 *  Description:  ----
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class ImportFileSelectorDialog extends AbstractFileSelectorDialog 
{
	private static final long serialVersionUID = -4620972246237384499L;
	//DataAccessPlugInBase m_da = null;
	//I18nControlAbstract m_ic = null;
	//JDialog dialog_parent = null;
	//JPanel previous = null;
	
	JLabel label_type;
	JButton b_prev, b_next;
	JTextField tf_file;
	//FileReaderContext m_frc;
	
	
	/**
	 * Constructor
	 * 
	 * @param da
	 * @param previous_parent
	 * @param ddh
	 */
	public ImportFileSelectorDialog(DataAccessPlugInBase da, JDialog previous_parent, DeviceDataHandler ddh)
	{
		super(da, ddh, previous_parent);
		//m_da = da;
		//m_ic = da.getI18nControlInstance();
		//this.dialog_parent = dialog;
		//this.previous = previous_panel;
		//this.m_frc = frc;
		//init();
	}

	
	public void init()
	{
		ATSwingUtils.initLibrary();
		this.setLayout(null);
        m_da.addComponent(this);
		
		JLabel l = ATSwingUtils.getTitleLabel(m_ic.getMessage("IMPORT_FILE_SELECTOR"), 50, 30, 300, 30, this, ATSwingUtils.FONT_BIG_BOLD);
		l.setHorizontalAlignment(JLabel.CENTER);
		
		
		ATSwingUtils.getLabel(m_ic.getMessage("IMPORT_FILE_SELECTOR_DESC"), 
							  50, 60, 300, 60, this, ATSwingUtils.FONT_NORMAL);
		
		ATSwingUtils.getLabel("Selected type:", 
							  50, 95, 300, 60, this, ATSwingUtils.FONT_NORMAL_BOLD);
		
		label_type = ATSwingUtils.getLabel(this.m_ddh.selected_file_context.getFullFileDescription(), 
				  50, 115, 300, 60, this, ATSwingUtils.FONT_NORMAL);

		
		ATSwingUtils.getLabel(this.m_ic.getMessage("SELECT_FILE"), 
				  50, 150, 300, 60, this, ATSwingUtils.FONT_NORMAL_BOLD);
		
		tf_file = ATSwingUtils.getTextField("", 50, 195, 300, 25, this);
		tf_file.setEnabled(false);
		
		
		/*JButton b =*/ 
		ATSwingUtils.getButton(m_ic.getMessage("BROWSE"), 250, 170, 100, 20, this, ATSwingUtils.FONT_NORMAL, null, "browse", this, m_da);
		
		
		
        this.help_button = m_da.createHelpIconByBounds(50, 250, 60, 25, this);
        //help_button.setFont(normal);
        this.add(help_button);
       
        ATSwingUtils.getButton("", 115, 250, 60, 25, this, 
            ATSwingUtils.FONT_NORMAL, "cancel.png", "cancel", this, m_da);

        b_prev = ATSwingUtils.getButton("", 220, 250, 60, 25, this, 
            ATSwingUtils.FONT_NORMAL, "nav_left_blue.png", "prev", this, m_da);
        b_prev.setEnabled(false);
        b_prev.setVisible(false);

        b_next = ATSwingUtils.getButton("", 290, 250, 60, 25, this, 
            ATSwingUtils.FONT_NORMAL, "nav_right_blue.png", "next", this, m_da);
        b_next.setEnabled(false);
        
		this.setBounds(0, 0, 400, 320);

		if (this.m_ddh.getDeviceInterface().getFileDownloadTypes().length>1)
		{
	        b_prev.setEnabled(true);
	        b_prev.setVisible(true);
		}
		
        m_da.enableHelp(this);
		
		
	}
	

	public void actionPerformed(ActionEvent ae) 
	{
		String action = ae.getActionCommand();
		
		if (action.equals("cancel"))
		{
			this.dialog_parent.dispose();
		}
		else if (action.equals("browse"))
		{
		    
            JFileChooser file_chooser = new JFileChooser();
            //file_chooser.setDialogTitle(ic.getMessage("SELECT_FILE_TO_RESTORE"));
            file_chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            file_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            file_chooser.setMultiSelectionEnabled(false);
            file_chooser.setCurrentDirectory(new File("."));
            file_chooser.setFileFilter(this.m_ddh.selected_file_context.getFileFilter());
		    
		    int returnVal = file_chooser.showOpenDialog(this);		    
		    
	        if (returnVal == JFileChooser.APPROVE_OPTION) 
	        {
	            File file = file_chooser.getSelectedFile();
	            this.tf_file.setText(file.getAbsolutePath());
	            this.m_ddh.selected_file = file.getAbsolutePath();
	            this.b_next.setEnabled(true);
	        } 
		    
		}
		else if (action.equals("next"))
        {
		    if (this.m_ddh.selected_file_context.hasSpecialSelectorDialog())
		    {
		        this.m_ddh.selected_file_context.goToNextDialog(this);
		    }
		    else
		    {
		        this.dispose();
		        m_da.removeComponent(this);
                new DeviceDisplayDataDialog(m_da.getMainParent(), m_da, m_ddh);		        
		    }
        }
		else if (action.equals("prev"))
		{
            m_da.removeComponent(this);
            this.dispose();
            new MultipleFileSelectorDialog(m_da, this.dialog_parent, m_ddh);
		}
	}

	
	
	
	public String getHelpId() 
	{
		return "DeviceTool_File_Import";
	}


    @Override
    public Dimension getSize()
    {
        return new Dimension(400, 340);
    }
	
	

}
