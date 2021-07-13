package ggc.connect.gui;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.connect.data.ConnectConfigEntry;
import ggc.connect.data.ConnectHandlerParameters;
import ggc.connect.data.retrieval.ConnectDataRetriever;
import ggc.connect.defs.ConnectHandler;
import ggc.connect.util.DataAccessConnect;
import ggc.plugin.data.DeviceValuesConfigTable;
import ggc.plugin.data.DeviceValuesConfigTableModel;
import ggc.plugin.gui.DeviceDisplayConfigDialog;
import ggc.plugin.output.OutputWriterConfigData;
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
 *  Filename:      DeviceDisplayDataDialog
 *  Description:   This is dialog for displaying data as it's been downloaded. 
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// FIXME
// This needs to work with new configuration type thing and groups (Animas
// Pump/CGMS for testing)

public class ConnectDisplayConfigDialog extends DeviceDisplayConfigDialog implements ActionListener
{

    private static final long serialVersionUID = -3480394909832672125L;

    private DataAccessPlugInBase dataAccess; // = DataAccessMeter.getInstance();
    I18nControlAbstract i18nControl; // = dataAccess.getI18nControlInstance();
    ConnectDataRetriever deviceReaderRunner;

    public JProgressBar progress = null;
    // private JProgressBar progressOld = null;

    //private DeviceValuesConfigTableModel model = null;
    //private DeviceValuesConfigTable table = null;

    //private JButton bt_close, bt_import, bt_break;
    //private JTabbedPane tabPane;

    //JLabel lbl_status, lbl_comment;
    //JTextArea ta_info = null;
    //JTextArea logText = null;
    //JButton help_button;
    // DeviceDataHandler deviceDataHandler;
    //JFrame m_parent = null;

    // new
    ConnectHandlerParameters parameters;
    ConnectHandler connectHandler;


    /**
     * Constructor
     *
     * @param parent
    
     */
    public ConnectDisplayConfigDialog(JFrame parent, ConnectHandlerParameters parameters, ConnectHandler connectHandler)
    {
        super(parent, DataAccessConnect.getInstance(), null, false);

        this.parameters = parameters;
        this.connectHandler = connectHandler;

        initReader();

        dialogPreInit();
    }

    protected void initReader()
    {
        this.deviceDataHandler = dataAccess.getDeviceDataHandler();
        this.deviceReaderRunner = new ConnectDataRetriever(this, this.parameters, this.connectHandler);
    }


    protected void startReader()
    {
        addLogText(i18nControl.getMessage("READ_DEVICE_CONFIG") + " - "
                + i18nControl.getMessage(this.parameters.getHandlerConfiguration().getName())
                + //
                "\n    " + i18nControl.getMessage("CFG_BASE_FROM").toLowerCase() + " "
                + this.parameters.getHandlerTarget());

        if (this.deviceReaderRunner != null)
        {
            this.deviceReaderRunner.start();
        }
    }


    protected void setTitle()
    {
        setTitle(String.format(i18nControl.getMessage("READ_DEVICE_CONFIG_TITLE"),
                i18nControl.getMessage(this.parameters.getHandlerConfiguration().getName())));
    }



    protected void init()
    {
        ATSwingUtils.initLibrary();

        modelConfig = new DeviceValuesConfigTableModel(dataAccess, ConnectConfigEntry.class); // ,
        // dataAccess.getSourceDevice());

        modelConfig.clearData();

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(600, 450);

        JLabel label;

        int wide_add = 0;

        if (dataAccess.isDataDownloadScreenWide())
        {
            wide_add = 100;
        }

        Font normal = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL);
        Font normal_b = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD);

        setBounds(0, 0, 480 + wide_add, 460);

        dataAccess.centerJDialog(this);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(panel, BorderLayout.CENTER);

        // TabControl with two tabs: log and data

        taLogText = new JTextArea(i18nControl.getMessage("LOG__") + ":\n", 8, 35);
        taLogText.setAutoscrolls(true);
        JScrollPane sp = new JScrollPane(taLogText, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        addLogText(i18nControl.getMessage("LOG_IS_CURRENTLY_NOT_IMPLEMENTED"));

        this.tableConfig = new DeviceValuesConfigTable(dataAccess, modelConfig);

        tabPane = new JTabbedPane();
        tabPane.add(i18nControl.getMessage("DATA"), this.createTablePanel(this.table));
        tabPane.add(i18nControl.getMessage("LOG"), sp);
        tabPane.setBounds(30, 15, 410 + wide_add, 250); // 410
        panel.add(tabPane);

        // Info



        // device status
        label = new JLabel(i18nControl.getMessage("ACTION") + ":");
        label.setBounds(30, 290, 100, 25); // 420
        label.setFont(normal_b);
        panel.add(label);

        lblStatus = new JLabel(i18nControl.getMessage("READY"));
        lblStatus.setBounds(110, 290, 330, 25); // 420
        lblStatus.setFont(normal);
        panel.add(lblStatus);

        this.progress = new JProgressBar();
        this.progress.setBounds(30, 330, 410 + wide_add, 20); // 450
        this.progress.setStringPainted(true);
        panel.add(this.progress);


        btnBreak = ATSwingUtils.getButton(i18nControl.getMessage("BREAK_COMMUNICATION"), //
                150 + wide_add, 380, 170, 25, panel, ATSwingUtils.FONT_NORMAL, //
                null, "break_communication", this, dataAccess);


        btnHelp = ATSwingUtils.createHelpButtonByBounds(30, 380, 110, 25, this, //
                ATSwingUtils.FONT_NORMAL, dataAccess);
        panel.add(btnHelp);


        btnClose = ATSwingUtils.getButton(i18nControl.getMessage("CLOSE"), //
                330 + wide_add, 380, 110, 25, panel, ATSwingUtils.FONT_NORMAL, //
                null, "close", this, dataAccess);
        btnClose.setEnabled(false);


        dataAccess.enableHelp(this);

    }


    public void writeConfigurationData(OutputWriterConfigData configData)
    {
        count++;

        ConnectConfigEntry dta = (ConnectConfigEntry) configData;

        if (!groupCounter.containsKey(dta.getGroup()))
        {
            groupCounter.put(dta.getGroup(), 0);
            this.modelConfig.addEntry(new ConnectConfigEntry(dta.getGroup(), dta.getSource(), dta.getSourceSubItem()));
        }

        this.modelConfig.addEntry(dta);
    }



    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************


    /** 
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return "DeviceTool_Reading_Config_View";
    }


    public void setSpecialNote(int noteType, String note)
    {
        System.out.println("Special Note [" + noteType + "]: " + note);
    }

}
