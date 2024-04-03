package ggc.plugin.gui;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.TableColumn;

import com.atech.utils.ATSwingUtils;

import ggc.core.util.DataAccess;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValueConfigEntry;
import ggc.plugin.data.DeviceValuesConfigTable;
import ggc.plugin.data.DeviceValuesConfigTableModel;
import ggc.plugin.data.enums.DeviceConfigurationGroup;
import ggc.plugin.output.OutputWriterConfigData;
import ggc.plugin.output.OutputWriterData;
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

public class DeviceDisplayConfigDialog extends DeviceDisplayDataDialog
{

    private static final long serialVersionUID = -3480394909832672125L;

    // private DataAccessPlugInBase dataAccess; // = DataAccessMeter.getInstance();
    // I18nControlAbstract i18nControl; // = dataAccess.getI18nControlInstance();
    // DeviceReaderRunner deviceReaderRunner;

    // public JProgressBar progress = null;
    // private JProgressBar progressOld = null;

    protected DeviceValuesConfigTableModel modelConfig = null;
    protected DeviceValuesConfigTable tableConfig = null;

    // private JButton btnClose, btnImport, btnBreak;
    // private JTabbedPane tabPane;

    // JLabel lblStatus, lblComment;
    // JTextArea taInfo = null;
    // JTextArea taLogText = null;
    // JButton btnHelp;
    // DeviceDataHandler deviceDataHandler;
    // JFrame m_parent = null;

    protected Map<DeviceConfigurationGroup, Integer> groupCounter = new HashMap<DeviceConfigurationGroup, Integer>();


    /**
     * Constructor
     * 
     * @param parent 
     * @param da 
     * @param ddh 
     */
    public DeviceDisplayConfigDialog(JFrame parent, DataAccessPlugInBase da, DeviceDataHandler ddh)
    {
        super(parent, da, ddh);
    }


    public DeviceDisplayConfigDialog(JFrame parent, DataAccessPlugInBase da, DeviceDataHandler ddh, boolean doInit)
    {
        super(parent, da, ddh, doInit);
    }

    // /**
    // * Constructor
    // *
    // * @param parent
    // * @param da
    // * @param ddh
    // */
    // public DeviceDisplayConfigDialog(JFrame parent, DataAccessPlugInBase da, DeviceDataHandler
    // ddh,
    // DeviceReaderRunner runner)
    // {
    // super(parent, "", true);
    //
    // this.dataAccess = da;
    // this.i18nControl = da.getI18nControlInstance();
    // this.m_parent = parent;
    // this.deviceDataHandler = ddh;
    // this.deviceDataHandler.dialog_config = this;
    // this.deviceReaderRunner = runner;
    //
    // dialogPreInit();
    // }

    /**
     * Constructor (for testing GUI)
     *  
     * @param da 
     * @param ddh 
     * @param is_debug 
     */
    public DeviceDisplayConfigDialog(DataAccessPlugInBase da, DeviceDataHandler ddh, boolean is_debug)
    {
        super(da, ddh, is_debug);
    }


    protected void setTitle()
    {
        if (deviceDataHandler != null)
        {
            setTitle(String.format(i18nControl.getMessage("READ_DEVICE_CONFIG_TITLE"),
                this.deviceDataHandler.getConfiguredDevice().device_device,
                this.deviceDataHandler.getConfiguredDevice().communication_port));
        }
    }


    protected void init()
    {
        ATSwingUtils.initLibrary();

        modelConfig = new DeviceValuesConfigTableModel(dataAccess, DeviceValueConfigEntry.class); // ,
        modelConfig.clearData();

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(600, 450);

        JLabel label;

        int wide_add = 100;

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
        DataAccess.getSkinManager().reSkinifyComponent(tableConfig);

        tabPane = new JTabbedPane();
        tabPane.add(i18nControl.getMessage("DATA"), this.createTablePanel(this.tableConfig));
        tabPane.add(i18nControl.getMessage("LOG"), sp);
        tabPane.setBounds(30, 15, 410 + wide_add, 250); // 410
        panel.add(tabPane);

        // device status
        label = new JLabel(i18nControl.getMessage("ACTION") + ":");
        label.setBounds(30, 290, 100, 25); // 420
        label.setFont(normal_b);
        panel.add(label);

        lblStatus = new JLabel(i18nControl.getMessage("READY"));
        lblStatus.setBounds(110, 290, 330, 25); // 420
        // lblStatus.setBorder(new LineBorder(Color.red));
        lblStatus.setFont(normal);
        panel.add(lblStatus);

        this.progress = new JProgressBar();
        this.progress.setBounds(30, 330, 410 + wide_add, 20); // 450
        this.progress.setStringPainted(true);
        // this.progress.setIndeterminate(true);
        panel.add(this.progress);

        btnBreak = new JButton(i18nControl.getMessage("BREAK_COMMUNICATION"));
        btnBreak.setBounds(150 + wide_add, 380, 170, 25);
        // btnBreak.setEnabled(this.m_mim.isStatusOK());
        btnBreak.setActionCommand("break_communication");
        btnBreak.addActionListener(this);
        panel.add(btnBreak);

        btnHelp = ATSwingUtils.createHelpButtonByBounds(30, 380, 110, 25, this, ATSwingUtils.FONT_NORMAL, dataAccess);
        panel.add(btnHelp);

        btnClose = new JButton(i18nControl.getMessage("CLOSE"));
        btnClose.setBounds(330 + wide_add, 380, 110, 25);
        btnClose.setEnabled(false);
        btnClose.setActionCommand("close");
        btnClose.addActionListener(this);
        panel.add(btnClose);

        dataAccess.enableHelp(this);

        this.setResizable(false);

    }


    protected JPanel createTablePanel(DeviceValuesConfigTable tableIn)
    {

        JScrollPane scroller = new JScrollPane(tableIn, //
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, //
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // final AddRowAction addRowAction = new AddRowAction(table);
        // final DeleteRowAction deleteRowAction = new DeleteRowAction(table);

        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        toolBar.setFloatable(false);

        // int[] cw = { 110, 80, 70, 80, 30 };

        TableColumn column = null;
        for (int i = 0; i < 2; i++)
        {
            column = tableIn.getColumnModel().getColumn(i);
            column.setPreferredWidth(this.model.getColumnWidth(i, tableIn.getWidth()));
        }

        JPanel container = new JPanel(new BorderLayout());
        container.add(toolBar, "North");
        container.add(scroller, "Center");

        return container;

    }


    /**
     * endOutput
     */
    public void endOutput()
    {
    }


    /**
     * Write Data to OutputWriter
     * 
     * @param data
     */
    public void writeData(OutputWriterData data)
    {
    }


    public void writeConfigurationData(OutputWriterConfigData configData)
    {
        count++;

        DeviceValueConfigEntry dta = (DeviceValueConfigEntry) configData;

        if (!groupCounter.containsKey(dta.getGroup()))
        {
            groupCounter.put(dta.getGroup(), 0);
            this.modelConfig.addEntry(new DeviceValueConfigEntry(dta.getGroup()));
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


    /**
     * Set old data reading progress
     * 
     * @param value
     */
    public void setOldDataReadingProgress(int value)
    {
        // this.progressOld.setValue(value);
    }


    /**
     * Can old data reading be initiated (if module in current running mode supports this, this is
     * intended mostly for usage outside GGC)
     * 
     * @param value
     */
    public void canOldDataReadingBeInitiated(boolean value)
    {
    }


    /**
     * setIndeterminateProgress - if we cannot trace progress, we set this and JProgressBar will go
     *    into indeterminate mode
     */
    public void setIndeterminateProgress()
    {
    }





    public void setSpecialNote(int noteType, String note)
    {
        System.out.println("Special Note [" + noteType + "]: " + note);
    }

}
