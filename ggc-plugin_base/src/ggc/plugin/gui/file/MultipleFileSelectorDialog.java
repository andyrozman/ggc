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


public class MultipleFileSelectorDialog extends AbstractFileSelectorDialog 
{
	private static final long serialVersionUID = -876338378352653634L;
	FileReaderContext[] file_contexts;
	JComboBox cb_contexts;
	
	/**
	 * Constructor
	 * 
	 * @param da
	 * @param dialog
	 * @param ddh
	 */
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
