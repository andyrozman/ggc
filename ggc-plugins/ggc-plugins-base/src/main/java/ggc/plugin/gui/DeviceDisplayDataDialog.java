package ggc.plugin.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.data.DeviceValuesTable;
import ggc.plugin.data.DeviceValuesTableModel;
import ggc.plugin.data.enums.DownloaderFilterType;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.output.*;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.plugin.util.LogEntryType;

/**
 * Application: GGC - GNU Gluco Control 
 * Plug-in: GGC PlugIn Base (base class for all plugins)
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename: DeviceDisplayDataDialog Description: This is dialog for displaying
 * data as it's been downloaded.
 * 
 * Author: Andy {andy@atech-software.com}
 */

public class DeviceDisplayDataDialog extends JDialog implements ActionListener, OutputWriter, HelpCapable
{

    private static final long serialVersionUID = 3365114423740706212L;

    protected DeviceReaderRunner deviceReaderRunner;

    protected DataAccessPlugInBase dataAccess;
    protected I18nControlAbstract i18nControl;
    protected DeviceDataHandler deviceDataHandler;

    protected DeviceValuesTableModel model = null;
    protected DeviceValuesTable table = null;

    protected JButton btnClose, btnImport, btnBreak, btnHelp, btnSelectAll, btnUnselectAll;
    protected JButton btnInfo, btnErrors;
    protected JComboBox cmbFilter;
    protected JTabbedPane tabPane;
    protected JLabel lblStatus, lblComment;
    protected JTextArea taInfo, taLogText;
    protected JFrame parentFrame = null;
    protected JProgressBar progress, progressOld;

    protected boolean indeterminate = false;

    //
    protected DeviceIdentification deviceIdentification;
    protected String subStatus = null;
    protected OutputUtil outputUtil;
    protected int count = 0;
    protected int currentStatus = 0;
    protected boolean deviceShouldBeStopped = false;
    protected int readingStatus = AbstractOutputWriter.STATUS_READY;
    protected String deviceSource;
    protected List<ErrorMessageDto> errorMessageList = new ArrayList<ErrorMessageDto>();


    public DeviceDisplayDataDialog(JFrame parent, DataAccessPlugInBase da, DeviceDataHandler ddh)
    {
        this(parent, da, ddh, true);
    }


    /**
     * Constructor
     * 
     * @param parent 
     * @param da
     * @param ddh
     */
    public DeviceDisplayDataDialog(JFrame parent, DataAccessPlugInBase da, DeviceDataHandler ddh, boolean init)
    {
        super(parent, "", true);

        this.dataAccess = da;
        this.i18nControl = da.getI18nControlInstance();
        this.parentFrame = parent;

        this.deviceDataHandler = ddh;
        if (ddh != null)
        {
            this.deviceDataHandler.dialog_data = this;
        }
        outputUtil = OutputUtil.getInstance(this);

        if (init)
        {
            initReader();

            dialogPreInit();
        }
    }


    /**
     * Constructor (for testing GUI)
     * 
     * @param da
     * @param ddh
     * @param is_debug
     */
    public DeviceDisplayDataDialog(DataAccessPlugInBase da, DeviceDataHandler ddh, boolean is_debug)
    {
        super();

        this.dataAccess = da;
        this.i18nControl = da.getI18nControlInstance();

        this.deviceDataHandler = ddh;
        outputUtil = OutputUtil.getInstance(this);

        dialogPreInit();
    }


    protected void initReader()
    {
        this.deviceReaderRunner = new DeviceReaderRunner(dataAccess, this.deviceDataHandler);
    }


    protected void startReader()
    {
        if (this.deviceReaderRunner != null)
        {
            this.deviceReaderRunner.start();
        }
    }


    protected void dialogPreInit()
    {
        setTitle();

        dataAccess.addComponent(this);

        init();

        startReader();

        this.setVisible(true);
    }


    protected void setTitle()
    {
        if (deviceDataHandler != null)
        {
            setTitle(String.format(i18nControl.getMessage("READ_DEVICE_DATA_TITLE"),
                this.deviceDataHandler.getConfiguredDevice().device_device,
                this.deviceDataHandler.getConfiguredDevice().communication_port));
        }
    }


    /**
     * If we have special status progress defined, by device, we need to set
     * progress, by ourselves, this is done with this method.
     * 
     * @param value
     */
    public void setSpecialProgress(int value)
    {
        if (!this.indeterminate)
        {
            this.progress.setValue(value);
        }
    }


    protected void addLogText(String s)
    {
        taLogText.append(s + "\n");
    }


    protected void init()
    {
        ATSwingUtils.initLibrary();

        model = this.deviceDataHandler.getDeviceValuesTableModel();
        model.clearData();

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(700, 600);

        JLabel label;

        int wide_add = 0;

        if (dataAccess.isDataDownloadScreenWide())
        {
            wide_add = 200;
        }

        Font normal = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL);
        Font normal_b = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD);

        setBounds(0, 0, 480 + wide_add, 660);

        dataAccess.centerJDialog(this);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(panel, BorderLayout.CENTER);

        // TabControl with two tabs: log and data

        taLogText = new JTextArea(i18nControl.getMessage("LOG__") + ":\n", 8, 35);
        taLogText.setAutoscrolls(true);
        JScrollPane sp = new JScrollPane(taLogText, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        addLogText(i18nControl.getMessage("LOG_IS_CURRENTLY_NOT_IMPLEMENTED"));

        this.table = new DeviceValuesTable(dataAccess, model);

        tabPane = new JTabbedPane();
        tabPane.add(i18nControl.getMessage("DATA"), this.createTablePanel(this.table));
        tabPane.add(i18nControl.getMessage("LOG"), sp);
        tabPane.setBounds(30, 15, 410 + wide_add, 250); // 410
        panel.add(tabPane);

        // Info
        label = new JLabel(String.format(i18nControl.getMessage("DEVICE_INFO"),
            i18nControl.getMessage("DEVICE_NAME_BIG"))
                + ":");
        label.setBounds(30, 310, 310, 25);
        label.setFont(normal_b);
        panel.add(label);

        taInfo = new JTextArea();

        JScrollPane sp3 = new JScrollPane(taInfo);
        sp3.setBounds(30, 345, 410 + wide_add, 70);
        panel.add(sp3);

        taInfo.setText("");

        lblComment = new JLabel("");
        lblComment.setBounds(40, 270, 305 + wide_add, 45);
        lblComment.setFont(normal);
        panel.add(lblComment);

        btnInfo = ATSwingUtils.getIconButton(355 + wide_add, 295, 40, 40, //
            i18nControl.getMessage("GET_DL_INFO"), "document_info.png", 32, 32, //
            "show_info", this, panel, dataAccess);
        btnInfo.setEnabled(false);

        btnErrors = ATSwingUtils.getIconButton(400 + wide_add, 295, 40, 40, //
            i18nControl.getMessage("GET_DL_ERRORS"), "document_warning.png", 31, 31, //
            "show_errors", this, panel, dataAccess);
        btnErrors.setEnabled(false);

        // reading old data
        label = new JLabel(i18nControl.getMessage("READING_OLD_DATA") + ":");
        label.setBounds(30, 425, 250, 25); // 420
        label.setFont(normal_b);
        panel.add(label);

        this.progressOld = new JProgressBar();
        this.progressOld.setBounds(30, 450, 410 + wide_add, 20); // 450
        this.progressOld.setStringPainted(true);
        // this.progress.setIndeterminate(true);
        panel.add(this.progressOld);

        // device status
        label = new JLabel(i18nControl.getMessage("ACTION") + ":");
        label.setBounds(30, 490, 100, 25); // 420
        label.setFont(normal_b);
        panel.add(label);

        lblStatus = new JLabel(i18nControl.getMessage("READY"));
        lblStatus.setBounds(110, 490, 330, 25); // 420
        // lblStatus.setBorder(new LineBorder(Color.red));
        lblStatus.setFont(normal);
        panel.add(lblStatus);

        this.progress = new JProgressBar();
        this.progress.setBounds(30, 520, 410 + wide_add, 20); // 450
        this.progress.setStringPainted(true);
        // this.progress.setIndeterminate(true);
        panel.add(this.progress);

        btnBreak = new JButton(i18nControl.getMessage("BREAK_COMMUNICATION"));
        btnBreak.setBounds(150 + wide_add, 565, 170, 40);
        btnBreak.setActionCommand("break_communication");
        btnBreak.addActionListener(this);
        panel.add(btnBreak);

        btnHelp = ATSwingUtils.createHelpButtonByBounds(30, 565, 110, 40, this, ATSwingUtils.FONT_NORMAL, dataAccess);
        panel.add(btnHelp);

        btnClose = new JButton(i18nControl.getMessage("CLOSE"));
        btnClose.setBounds(330 + wide_add, 565, 110, 40);
        btnClose.setEnabled(false);
        btnClose.setFont(normal);
        btnClose.setActionCommand("close");
        btnClose.addActionListener(this);
        panel.add(btnClose);

        // btnImport = new JButton(i18nControl.getMessage("EXPORT_DATA"));
        // btnImport.setBounds(270 + wide_add, 300, 170, 25); // 270
        // btnImport.setActionCommand("export_data");
        // btnImport.addActionListener(this);
        // btnImport.setEnabled(false);
        //
        // // button.setEnabled(meterDevice.isStatusOK());
        //
        // panel.add(btnImport);

        dataAccess.enableHelp(this);

    }


    protected JPanel createTablePanel(DeviceValuesTable table_in)
    {

        JScrollPane scroller = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // final AddRowAction addRowAction = new AddRowAction(table);
        // final DeleteRowAction deleteRowAction = new DeleteRowAction(table);

        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        toolBar.setFloatable(false);

        JLabel label;

        toolBar.add(label = new JLabel(i18nControl.getMessage("FILTER") + ":   "));
        label.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_P2));
        toolBar.add(cmbFilter = new JComboBox(DownloaderFilterType.values()));
        toolBar.add(new JLabel("    "));
        toolBar.add(btnSelectAll = this.createButton("select_all", //
            i18nControl.getMessage("SELECT_ALL"), "element_selection.png"));
        toolBar.add(new JLabel("  "));
        toolBar.add(btnUnselectAll = this.createButton("deselect_all", //
            i18nControl.getMessage("DESELECT_ALL"), "element_selection_delete.png"));
        toolBar.add(new JLabel("                                      "));
        toolBar.add(btnImport = this.createButton("export_data", //
            i18nControl.getMessage("IMPORT_DATA"), "import1.png"));

        // cmbFilter.setSelectedIndex(DeviceDisplayDataDialog.FILTER_NEW_CHANGED);
        cmbFilter.setSelectedItem(DownloaderFilterType.NewChanged);
        cmbFilter.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_P2));
        cmbFilter.setEnabled(false);

        btnSelectAll.setEnabled(false);
        btnUnselectAll.setEnabled(false);
        btnImport.setEnabled(false);

        cmbFilter.addItemListener(new ItemListener()
        {

            public void itemStateChanged(ItemEvent ev)
            {
                model.setFilter((DownloaderFilterType) cmbFilter.getSelectedItem());
            }
        });

        JPanel container = new JPanel(new BorderLayout());
        container.add(toolBar, "North");
        container.add(scroller, "Center");

        return container;

    }


    private JButton createButton(String command_text, String tooltip, String image_d)
    {
        JButton b = new JButton();
        b.setIcon(ATSwingUtils.getImageIcon(image_d, 25, 25, this, dataAccess));
        b.addActionListener(this);
        b.setActionCommand(command_text);
        b.setToolTipText(tooltip);
        return b;
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("break_communication"))
        {
            this.setStatus(AbstractOutputWriter.STATUS_STOPPED_USER);
            this.setReadingStop();
        }
        else if (action.equals("close"))
        {
            dataAccess.removeComponent(this);
            this.dispose();
        }
        else if (action.equals("select_all"))
        {
            this.model.selectAll();
        }
        else if (action.equals("deselect_all"))
        {
            this.model.deselectAll();
        }
        else if (action.equals("export_data"))
        {
            DeviceExportDialog med = new DeviceExportDialog(dataAccess, this, this.deviceDataHandler);

            if (med.wasAction())
            {
                this.btnImport.setEnabled(false);
            }
        }
        else
        {
            System.out.println("DeviceDisplayDataDialog::Unknown command: " + action);
        }

    }


    /**
     * endOutput
     */
    public void endOutput()
    {
        // System.out.println("endOutput()");
        if (this.indeterminate)
        {
            this.progress.setIndeterminate(false);
            this.progress.setStringPainted(true);
            this.progress.setValue(50);
            this.progress.setValue(100);
            this.progress.setString("100 %");
            this.progress.repaint();
        }
    }


    /**
     * getDeviceIdentification
     */
    public DeviceIdentification getDeviceIdentification()
    {
        if (deviceIdentification == null)
        {
            deviceIdentification = new DeviceIdentification(dataAccess.getI18nControlInstance());
        }

        return deviceIdentification;
    }


    /**
     * Set Sub Status
     * 
     * @see ggc.plugin.output.OutputWriter#setSubStatus(java.lang.String)
     */
    public void setSubStatus(String sub_status)
    {
        this.subStatus = sub_status;
        refreshStatus();
    }


    /**
     * Get Sub Status
     * 
     * @see ggc.plugin.output.OutputWriter#getSubStatus()
     */
    public String getSubStatus()
    {
        return this.subStatus;
    }


    /**
     * getOutputUtil
     */
    public OutputUtil getOutputUtil()
    {
        return this.outputUtil;
    }


    /**
     * setBGOutputType
     */
    public void setBGOutputType(int bg_type)
    {
        // System.out.println("setBGOutput()");
        // this.outputUtil.setBGMeasurmentType(bg_type);
    }


    /**
     * setDeviceIdentification
     */
    public void setDeviceIdentification(DeviceIdentification di)
    {
        this.deviceIdentification = di;
    }


    /**
     * writeDeviceIdentification
     */
    public void writeDeviceIdentification()
    {
        if (this.deviceIdentification.company == null)
        {
            this.deviceIdentification.company = dataAccess.getCompanyNameForSelectedDevice();
        }

        if (this.deviceIdentification.device_selected == null)
        {
            this.deviceIdentification.device_selected = dataAccess.getDeviceNameForSelectedDevice();
        }

        this.taInfo.setText(this.deviceIdentification.getShortInformation());
    }


    /**
     * writeHeader
     */
    public void writeHeader()
    {
    }


    /**
     * writeRawData
     * 
     * @param input
     * @param is_bg_data
     */
    public void writeRawData(String input, boolean is_bg_data)
    {
    }


    /**
     * User can stop readings from his side (if supported)
     */
    public void setReadingStop()
    {
        this.deviceShouldBeStopped = true;
    }


    /**
     * This should be queried by device implementation, to see if it must stop
     * reading
     */
    public boolean isReadingStopped()
    {
        return this.deviceShouldBeStopped;
    }


    /**
     * This is status of device and also of GUI that is reading device (if we
     * have one) This is to set that status to see where we are. Allowed
     * statuses are: 1-Ready, 2-Downloading, 3-Stopped by device, 4-Stoped by
     * user,5-Download finished,6-Reader error
     */
    public void setStatus(int status)
    {
        if (this.readingStatus == AbstractOutputWriter.STATUS_STOPPED_DEVICE
                || this.readingStatus == AbstractOutputWriter.STATUS_STOPPED_USER
                || this.readingStatus == AbstractOutputWriter.STATUS_READER_ERROR)
            return;

        this.readingStatus = status;
        setGUIStatus(status);
    }


    /**
     * Get Status
     * 
     * @see ggc.plugin.output.OutputWriter#getStatus()
     */
    public int getStatus()
    {
        return this.readingStatus;
    }


    /**
     * Refresh Status
     */
    public void refreshStatus()
    {
        setGUIStatus(currentStatus);
    }


    /**
     * Set GUI Status
     * 
     * @param status
     */
    public void setGUIStatus(int status)
    {

        currentStatus = status;

        if (this.subStatus == null || this.subStatus.length() == 0)
        {
            this.lblStatus.setText(this.dataAccess.getReadingStatuses()[status]);
        }
        else
        {
            this.lblStatus.setText(this.dataAccess.getReadingStatuses()[status] + " - "
                    + i18nControl.getMessage(this.subStatus));
        }

        // System.out.println("btnImport:" + btnImport + "  STATUS: " + status);

        switch (status)
        {

            case AbstractOutputWriter.STATUS_DOWNLOADING: // downloading
                {
                    this.btnBreak.setEnabled(true);
                    this.btnClose.setEnabled(false);
                    if (btnImport != null)
                        this.btnImport.setEnabled(false);
                }
                break;

            case AbstractOutputWriter.STATUS_DOWNLOAD_FINISHED: // finished
                {
                    this.btnBreak.setEnabled(false);
                    this.btnClose.setEnabled(true);
                    if (this.btnImport != null)
                    {
                        this.btnImport.setEnabled(true);
                        cmbFilter.setEnabled(true);
                        btnSelectAll.setEnabled(true);
                        btnUnselectAll.setEnabled(true);
                    }
                }
                break;

            case AbstractOutputWriter.STATUS_READER_ERROR: // error
                {
                    this.btnBreak.setEnabled(false);
                    this.btnClose.setEnabled(true);
                    if (this.btnImport != null)
                    {
                        this.btnImport.setEnabled(false);
                        cmbFilter.setEnabled(false);
                        btnSelectAll.setEnabled(false);
                        btnUnselectAll.setEnabled(false);
                    }
                }
                break;

            case AbstractOutputWriter.STATUS_STOPPED_DEVICE: // stopped - device
            case AbstractOutputWriter.STATUS_STOPPED_USER: // stoped - user
                {
                    this.btnBreak.setEnabled(false);
                    this.btnClose.setEnabled(true);
                    if (this.btnImport != null)
                    {
                        this.btnImport.setEnabled(false);
                    }
                }
                break;

            case AbstractOutputWriter.STATUS_READY: // ready
                // case 0: // none
            default:
                {
                    this.btnBreak.setEnabled(false);
                    this.btnClose.setEnabled(false);
                    if (this.btnImport != null)
                    {
                        this.btnImport.setEnabled(false);
                    }
                }
                break;
        }

    }


    /**
     * Set Device Comment
     * 
     * @param text
     */
    public void setDeviceComment(String text)
    {
        if (lblComment != null)
        {
            this.lblComment.setText(i18nControl.getMessage(text));
        }
    }


    /*
     * public static void main(String[] args) { JFrame f = new JFrame();
     * f.setSize(800,600);
     * DataAccessMeter.getInstance().addComponent(f);
     * // MeterReadDialog mrd = new MeterDisplayDataDialog(); // new
     * AscensiaContour("COM12", new // ConsoleOutputWriter())); }
     */

    /**
     * Write Data to OutputWriter
     * 
     * @param data
     */
    public void writeData(OutputWriterData data)
    {
        count++;
        this.model.addEntry((DeviceValuesEntryInterface) data);
    }


    public void writeConfigurationData(OutputWriterConfigData configData)
    {
    }


    public void setPluginName(String pluginName)
    {
    }


    public String getPluginName()
    {
        return null;
    }


    /**
     * Write log entry
     * 
     * @param entryType
     * @param message
     */
    public void writeLog(LogEntryType entryType, String message)
    {
        this.addLogText(message);
    }


    /**
     * Write log entry
     * 
     * @param entryType
     * @param message
     * @param ex
     */
    public void writeLog(LogEntryType entryType, String message, Exception ex)
    {
        this.addLogText(message);
    }


    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    /**
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
        return this.getRootPane();
    }


    /**
     * getHelpButton - get Help button
     */
    public JButton getHelpButton()
    {
        return this.btnHelp;
    }


    /**
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        // return dataAccess.getDeviceConfigurationDefinition().getHelpPrefix()
        // +
        // "Reading_View";
        return "DeviceTool_Reading_View";
    }


    /**
     * Set old data reading progress
     * 
     * @param value
     */
    public void setOldDataReadingProgress(int value)
    {
        if (this.progressOld != null)
            this.progressOld.setValue(value);
    }


    /**
     * Can old data reading be initiated (if module in current running mode
     * supports this, this is intended mostly for usage outside GGC)
     * 
     * @param value
     */
    public void canOldDataReadingBeInitiated(boolean value)
    {
    }


    /**
     * Set Device Source
     * 
     * @param dev
     */
    public void setDeviceSource(String dev)
    {
        this.deviceSource = dev;
    }


    /**
     * Set Device Source
     * 
     * @return
     */
    public String getDeviceSource()
    {
        return this.deviceSource;
    }


    /**
     * setIndeterminateProgress - if we cannot trace progress, we set this and
     * JProgressBar will go into indeterminate mode
     */
    public void setIndeterminateProgress()
    {
        indeterminate = true;
        this.progress.setIndeterminate(true);
        this.progress.setStringPainted(false);
    }


    public void addErrorMessage(ErrorMessageDto msg)
    {
        errorMessageList.add(msg);

        if (btnErrors != null)
        {
            // TODO btnErrors.setEnabled(true);
        }
    }


    public int getErrorMessageCount()
    {
        return errorMessageList.size();
    }


    public List<ErrorMessageDto> getErrorMessages()
    {
        return errorMessageList;
    }


    public void setSpecialNote(int noteType, String note)
    {
        System.out.println("Special Note [" + noteType + "]: " + note);
    }

}
