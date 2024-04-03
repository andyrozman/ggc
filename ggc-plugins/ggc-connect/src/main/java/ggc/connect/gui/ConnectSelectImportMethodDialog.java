package ggc.connect.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.ATSwingUtils;

import ggc.connect.data.ConnectHandlerParameters;
import ggc.connect.defs.ConnectHandler;
import ggc.connect.enums.ConnectConfigType;
import ggc.connect.enums.ConnectHandlerConfiguration;
import ggc.connect.util.DataAccessConnect;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.mgr.DeviceHandlerManager;
import ggc.plugin.gui.file.AbstractFileSelectorDialog;

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

public class ConnectSelectImportMethodDialog extends AbstractFileSelectorDialog
{

    private static final long serialVersionUID = -4620972246237384499L;
    private static final Logger LOG = LoggerFactory.getLogger(ConnectSelectImportMethodDialog.class);

    DeviceHandlerType deviceHandlerType;
    ConnectHandler connectHandler;
    ConnectHandlerConfiguration configuration;

    JButton buttonNext;
    JTextField fileNameTextField;
    int posY = 0;


    /**
     * Constructor
     */
    public ConnectSelectImportMethodDialog(JFrame parent, DeviceHandlerType deviceHandlerType,
            ConnectHandlerConfiguration configuration)
    {
        super(DataAccessConnect.getInstance(), null, parent, null, false);

        this.deviceHandlerType = deviceHandlerType;
        this.connectHandler = (ConnectHandler) DeviceHandlerManager.getInstance()
                .getDeviceHandler(this.deviceHandlerType);
        this.connectHandler.checkIfDataAccessSet();
        this.configuration = configuration;

        doInit();
    }


    public ConnectSelectImportMethodDialog(JFrame parent, DeviceHandlerType deviceHandlerType,
            ConnectHandlerConfiguration configuration, ConnectHandlerParameters parameters)
    {
        super(DataAccessConnect.getInstance(), null, parent, null, false);

        this.deviceHandlerType = deviceHandlerType;
        this.connectHandler = (ConnectHandler) DeviceHandlerManager.getInstance()
                .getDeviceHandler(this.deviceHandlerType);
        this.connectHandler.checkIfDataAccessSet();
        this.configuration = configuration;

        doInit(parameters);
    }


    public void doInit(ConnectHandlerParameters parameters)
    {
        init();
        setParameters(parameters);
        setSize(getSize());
        ATSwingUtils.centerJDialog(this, this.dialogParent != null ? this.dialogParent : this.frameParent);
        this.setVisible(true);
    }


    @Override
    public void init()
    {
        ATSwingUtils.initLibrary();
        this.setLayout(null);
        dataAccess.addComponent(this);

        this.setTitle(i18nControl.getMessage(configuration.getName()) + //
                " - " + i18nControl.getMessage(configuration.getConfigType().getDescription()) + //
                " - " + i18nControl.getMessage("STEP") + " 1");

        ImageIcon imageIcon = ATSwingUtils.getImageIcon(this.configuration.getIconName(),
                this, dataAccess);

        posY += 10 + imageIcon.getIconHeight() + 30;

        JPanel panelLogo = new JPanel();
        panelLogo.setBounds(10, 10, 520, imageIcon.getIconHeight() + 20);
        panelLogo.setLayout(new BorderLayout());

        JLabel label = new JLabel(imageIcon);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);
        panelLogo.add(label);
        this.add(panelLogo);

        this.add(createLabel(this.configuration.getDescription(), "DESCRIPTION"));

        if (configuration.getSpecialDescription() != null)
        {
            this.add(createLabel(this.configuration.getSpecialDescription(), "SPECIAL_DESCRIPTION"));
        }

        if (configuration.getConfigType() == ConnectConfigType.File)
        {
            JPanel panelFileSelector = new JPanel();
            panelFileSelector.setBorder(new TitledBorder(i18nControl.getMessage(configuration.getConfigTypeValue())));
            panelFileSelector.setBounds(10, posY, 520, 80);
            panelFileSelector.setLayout(null);

            fileNameTextField = ATSwingUtils.getTextField("", 30, 30, 330, 30, panelFileSelector,
                ATSwingUtils.FONT_NORMAL);

            ATSwingUtils.getButton(i18nControl.getMessage("BROWSE"), 390, 30, 100, 30, panelFileSelector,
                ATSwingUtils.FONT_NORMAL, null, "browse", this, dataAccess);

            this.add(panelFileSelector);

            posY += 100;
        }
        else
        {
            LOG.error("Config Type {} not supported.", configuration.getConfigType());
        }

        this.helpButton = ATSwingUtils.createHelpIconByBounds(30, posY, 50, 40, this, ATSwingUtils.FONT_NORMAL,
            dataAccess);
        this.add(helpButton);

        int[] size = new int[] { 33, 33 };

        ATSwingUtils.getButton("   " + i18nControl.getMessage("CANCEL"), 90, posY, 120, 40, this,
            ATSwingUtils.FONT_NORMAL, "cancel.png", "cancel", this, dataAccess, size);

        buttonNext = ATSwingUtils.getButton("", 420, posY, 70, 40, this, ATSwingUtils.FONT_NORMAL,
            "arrow_right_blue.png", "next", this, dataAccess, size);
        buttonNext.setEnabled(false);

        posY += 50 + 35;

        this.setBounds(0, 0, 550, posY);

        dataAccess.enableHelp(this);

    }


    private JLabel createLabel(String inputText, String borderTitle)
    {
        String text = i18nControl.getMessage(inputText);

        String textWidth = ATSwingUtils.createHtmlTextWithWidth(text, 510, 15, 15);

        JLabel label = new JLabel(textWidth);
        label.setBorder(new TitledBorder("  " + i18nControl.getMessage(borderTitle) + "  "));
        label.setVerticalAlignment(JLabel.TOP);

        Dimension dimension = ATSwingUtils.calculateJLabelSizeWithText(label, textWidth);

        label.setBounds(10, posY, 520, (int) dimension.getHeight());

        posY += (int) dimension.getHeight() + 10;

        return label;
    }


    public void actionPerformed(ActionEvent ae)
    {
        String action = ae.getActionCommand();

        if (action.equals("cancel"))
        {
            this.dispose();
            dataAccess.removeComponent(this);
        }
        else if (action.equals("browse"))
        {
            Boolean old = UIManager.getBoolean("FileChooser.readOnly");
            UIManager.put("FileChooser.readOnly", Boolean.TRUE);

            JFileChooser file_chooser = new JFileChooser();
            // file_chooser.setDialogTitle(i18nControlAbstract.getMessage("SELECT_FILE_TO_RESTORE"));
            file_chooser.setDialogType(JFileChooser.OPEN_DIALOG);
            file_chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            file_chooser.setMultiSelectionEnabled(false);
            file_chooser.setCurrentDirectory(new File("."));
            file_chooser.setAcceptAllFileFilterUsed(false);

            String fileName = fileNameTextField.getText();
            FileFilter[] fileFilters = connectHandler.getFileFilters(this.configuration);

            if (StringUtils.isNotBlank(fileName))
            {
                File cur = new File(fileName);

                if (cur.exists())
                {
                    for (FileFilter filter : fileFilters)
                    {
                        if (filter.accept(cur))
                        {
                            file_chooser.setSelectedFile(cur);
                            break;
                        }
                    }
                }
            }

            for (FileFilter filter : fileFilters)
            {
                file_chooser.addChoosableFileFilter(filter);
            }

            int returnVal = file_chooser.showOpenDialog(this);

            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = file_chooser.getSelectedFile();
                this.fileNameTextField.setText(file.getAbsolutePath());
                this.buttonNext.setEnabled(true);
                this.fileNameTextField.setEditable(false);
            }

            UIManager.put("FileChooser.readOnly", old);

        }
        else if (action.equals("next"))
        {
            this.dispose();
            dataAccess.removeComponent(this);

            new ConnectShowSummaryDialog(frameParent, getParameters());
        }
    }


    public String getHelpId()
    {
        // FIXME
        return "ConnectPlugin_Select_Import_Method";
    }


    @Override
    public Dimension getSize()
    {
        return new Dimension(550, posY);
    }


    public void setParameters(ConnectHandlerParameters parameters)
    {
        if (configuration.getConfigType() == ConnectConfigType.File)
        {
            this.fileNameTextField.setText(parameters.getFileName());
            this.fileNameTextField.setEditable(false);
            this.buttonNext.setEnabled(true);
        }

    }


    public ConnectHandlerParameters getParameters()
    {
        ConnectHandlerParameters parameters = new ConnectHandlerParameters();

        parameters.setDeviceHandlerType(deviceHandlerType);
        parameters.setHandlerConfiguration(configuration);

        if (configuration.getConfigType() == ConnectConfigType.File)
        {
            parameters.setFileName(this.fileNameTextField.getText());
        }

        return parameters;
    }

}
