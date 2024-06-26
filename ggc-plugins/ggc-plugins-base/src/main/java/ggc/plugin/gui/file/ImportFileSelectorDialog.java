package ggc.plugin.gui.file;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.*;

import com.atech.utils.ATSwingUtils;

import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.gui.DeviceDisplayConfigDialog;
import ggc.plugin.gui.DeviceDisplayDataDialog;
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

public class ImportFileSelectorDialog extends AbstractFileSelectorDialog
{

    private static final long serialVersionUID = -4620972246237384499L;
    // DataAccessPlugInBase dataAccess = null;
    // I18nControlAbstract i18nControl = null;
    // JDialog dialogParent = null;
    // JPanel previous = null;

    JLabel label_type;
    JButton b_prev, b_next;
    JTextField tf_file;


    // FileReaderContext m_frc;

    /**
     * Constructor
     * 
     * @param da
     * @param previous_parent
     * @param ddh
     */
    public ImportFileSelectorDialog(DataAccessPlugInBase da, JDialog previous_parent, DeviceDataHandler ddh,
            DownloadSupportType downloadSupportType)
    {
        super(da, ddh, previous_parent, downloadSupportType);
        // dataAccess = da;
        // i18nControl = da.getI18nControlInstance();
        // this.dialogParent = dialog;
        // this.previous = previous_panel;
        // this.m_frc = frc;
        // init();
    }


    @Override
    public void init()
    {
        ATSwingUtils.initLibrary();
        this.setLayout(null);
        dataAccess.addComponent(this);

        JLabel l = ATSwingUtils.getTitleLabel(i18nControl.getMessage("IMPORT_FILE_SELECTOR"), 50, 30, 300, 30, this,
            ATSwingUtils.FONT_BIG_BOLD);
        l.setHorizontalAlignment(SwingConstants.CENTER);

        ATSwingUtils.getLabel(i18nControl.getMessage("IMPORT_FILE_SELECTOR_DESC"), 50, 60, 300, 60, this,
            ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel("Selected type:", 50, 95, 300, 60, this, ATSwingUtils.FONT_NORMAL_BOLD);

        label_type = ATSwingUtils.getLabel(this.deviceDataHandler.selected_file_context.getFullFileDescription(), 50,
            115, 300, 60, this, ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(this.i18nControl.getMessage("SELECT_FILE"), 50, 150, 300, 60, this,
            ATSwingUtils.FONT_NORMAL_BOLD);

        tf_file = ATSwingUtils.getTextField("", 50, 195, 300, 25, this);
        tf_file.setEnabled(false);

        /* JButton b = */
        ATSwingUtils.getButton(i18nControl.getMessage("BROWSE"), 250, 170, 100, 20, this, ATSwingUtils.FONT_NORMAL,
            null, "browse", this, dataAccess);

        this.helpButton = ATSwingUtils.createHelpIconByBounds(50, 250, 60, 25, this, ATSwingUtils.FONT_NORMAL,
            dataAccess);
        // helpButton.setFont(normal);
        this.add(helpButton);

        ATSwingUtils.getButton("", 115, 250, 60, 25, this, ATSwingUtils.FONT_NORMAL, "cancel.png", "cancel", this,
            dataAccess);

        b_prev = ATSwingUtils.getButton("", 220, 250, 60, 25, this, ATSwingUtils.FONT_NORMAL, "nav_left_blue.png",
            "prev", this, dataAccess);
        b_prev.setEnabled(false);
        b_prev.setVisible(false);

        b_next = ATSwingUtils.getButton("", 290, 250, 60, 25, this, ATSwingUtils.FONT_NORMAL, "nav_right_blue.png",
            "next", this, dataAccess);
        b_next.setEnabled(false);

        this.setBounds(0, 0, 400, 320);

        List<GGCPlugInFileReaderContext> downloadTypes = this.deviceDataHandler
                .getFileDownloadTypes(downloadSupportType);

        if (downloadTypes.size() > 1)
        {
            b_prev.setEnabled(true);
            b_prev.setVisible(true);
        }

        dataAccess.enableHelp(this);

    }


    public void actionPerformed(ActionEvent ae)
    {
        String action = ae.getActionCommand();

        if (action.equals("cancel"))
        {
            this.dialogParent.dispose();
        }
        else if (action.equals("browse"))
        {

            JFileChooser file_chooser = new JFileChooser();
            // file_chooser.setDialogTitle(i18nControlAbstract.getMessage("SELECT_FILE_TO_RESTORE"));
            file_chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            file_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            file_chooser.setMultiSelectionEnabled(false);
            file_chooser.setCurrentDirectory(new File("."));
            file_chooser.setFileFilter(this.deviceDataHandler.selected_file_context.getFileFilter());

            int returnVal = file_chooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = file_chooser.getSelectedFile();
                this.tf_file.setText(file.getAbsolutePath());
                this.deviceDataHandler.selected_file = file.getAbsolutePath();
                this.b_next.setEnabled(true);
            }

        }
        else if (action.equals("next"))
        {
            if (this.deviceDataHandler.selected_file_context.hasSpecialSelectorDialog())
            {
                this.deviceDataHandler.selected_file_context.goToNextDialog(this);
            }
            else
            {
                this.dispose();
                dataAccess.removeComponent(this);

                if (downloadSupportType == DownloadSupportType.DownloadDataFile)
                {
                    new DeviceDisplayDataDialog(dataAccess.getMainParent(), dataAccess, deviceDataHandler);
                }
                else
                {
                    new DeviceDisplayConfigDialog(dataAccess.getMainParent(), dataAccess, deviceDataHandler);
                }
            }
        }
        else if (action.equals("prev"))
        {
            dataAccess.removeComponent(this);
            this.dispose();
            new MultipleFileSelectorDialog(dataAccess, this.dialogParent, deviceDataHandler, downloadSupportType);
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
