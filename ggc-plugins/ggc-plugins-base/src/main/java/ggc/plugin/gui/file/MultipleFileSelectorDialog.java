package ggc.plugin.gui.file;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.*;

import com.atech.utils.ATSwingUtils;

import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.util.DataAccessPlugInBase;

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

    List<GGCPlugInFileReaderContext> fileReaderContexts;
    JComboBox cb_contexts;


    /**
     * Constructor
     * 
     * @param da
     * @param dialog
     * @param ddh
     */
    public MultipleFileSelectorDialog(DataAccessPlugInBase da, JDialog dialog, DeviceDataHandler ddh,
            DownloadSupportType downloadSupportType)
    {
        super(da, ddh, dialog, downloadSupportType);
    }


    @Override
    public void init()
    {
        ATSwingUtils.initLibrary();
        this.setLayout(null);

        JLabel l = ATSwingUtils.getTitleLabel(this.m_ic.getMessage("Multiple Import Selector"), 50, 30, 300, 30, this,
            ATSwingUtils.FONT_BIG_BOLD);
        l.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel r = new JPanel();
        r.setBounds(50, 30, 300, 30);
        r.setBackground(Color.red);
        // this.add(r);

        ATSwingUtils.getLabel(m_ic.getMessage("MULTIPLE_IMPORT_SELECTOR_DESC"), 50, 60, 300, 120, this,
            ATSwingUtils.FONT_NORMAL);

        fileReaderContexts = this.deviceDataHandler.getFileDownloadTypes(downloadSupportType);

        cb_contexts = ATSwingUtils.getComboBox(getFileReaderContexts(), 50, 180, 300, 25, this,
            ATSwingUtils.FONT_NORMAL);

        this.helpButton = ATSwingUtils.createHelpIconByBounds(50, 230, 60, 25, this, ATSwingUtils.FONT_NORMAL, m_da);
        this.add(helpButton); // 60, 25

        ATSwingUtils.getButton("" /* i18nControl.getMessage("CANCEL") */, 120, 230, 60, 25, this, ATSwingUtils.FONT_NORMAL,
            "cancel.png", "cancel", this, m_da);

        ATSwingUtils.getButton("" /* i18nControl.getMessage("NEXT") */, 290, 230, 60, 25, this, ATSwingUtils.FONT_NORMAL,
            "nav_right_blue.png", "next", this, m_da);

        this.setBounds(0, 0, 400, 320);

        this.m_da.enableHelp(this);
    }


    public Vector<?> getFileReaderContexts()
    {
        Vector<GGCPlugInFileReaderContext> vector = new Vector<GGCPlugInFileReaderContext>();

        for (GGCPlugInFileReaderContext ctx : fileReaderContexts)
        {
            vector.add(ctx);
        }

        return vector;
    }


    public void actionPerformed(ActionEvent ae)
    {
        String action = ae.getActionCommand();

        if (action.equals("cancel"))
        {
            this.dialogParent.dispose();
        }
        else if (action.equals("next"))
        {
            deviceDataHandler.selected_file_context = fileReaderContexts.get(this.cb_contexts.getSelectedIndex());
            this.dispose();
            new ImportFileSelectorDialog(m_da, this.dialogParent, deviceDataHandler, downloadSupportType);
        }
    }


    public String getHelpId()
    {
        return "DeviceTool_File_Import_Type";
    }


    @Override
    public Dimension getSize()
    {
        return new Dimension(400, 320);
    }

}
