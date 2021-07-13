package ggc.plugin.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.atech.db.DbDataReadingFinishedInterface;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;
import com.atech.utils.data.TimeZoneUtil;

import ggc.plugin.DevicePlugInServer;
import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceInterfaceVersion;
import ggc.plugin.device.DeviceAbstract;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;
import ggc.plugin.gui.file.ImportFileSelectorDialog;
import ggc.plugin.gui.file.MultipleFileSelectorDialog;
import ggc.plugin.output.ConsoleOutputWriter;
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
 *  Filename:     DeviceInstructionsDialog
 *  Description:  Dialog showing us device, and instructions on how to connect and download data.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DeviceInstructionsDialog extends JDialog implements ActionListener, DbDataReadingFinishedInterface,
        HelpCapable
{

    private static final long serialVersionUID = -1199131576461686895L;
    private DataAccessPlugInBase dataAccess;
    I18nControlAbstract i18nControl = null;

    DeviceConfigEntry configured_device;
    DeviceDataHandler deviceDataHandler;
    JFrame m_parent = null;
    JButton btnStart, btnHelp;
    JLabel label_waiting;
    boolean reading_old_done = false;
    boolean is_preinit_done = true;
    private String unknownTranslation;

    int continuing_type = DeviceDataHandler.TRANSFER_READ_DATA;

    /**
     * Constructor
     *
     * @param da
     * @param parent 
     * @param server
     * @param _continued_type 
     */
    public DeviceInstructionsDialog(Container parent, DataAccessPlugInBase da, DevicePlugInServer server,
            int _continued_type)
    {
        super((JFrame) parent, "", true);

        this.dataAccess = da;
        this.i18nControl = da.getI18nControlInstance();
        this.m_parent = (JFrame) parent;

        unknownTranslation = i18nControl.getMessage("UNKNOWN");

        this.deviceDataHandler = dataAccess.getDeviceDataHandler();
        this.deviceDataHandler.setDevicePlugInServer(server);
        // this.deviceDataHandler.setDbDataReader(reader);
        this.checkReading(this.deviceDataHandler.isOldDataReadingFinished());
        this.deviceDataHandler.setReadingFinishedObject(this);
        dataAccess.addComponent(this);

        this.continuing_type = _continued_type;
        this.deviceDataHandler.setTransferType(this.continuing_type);

        btnStart = new JButton(i18nControl.getMessage("START_DOWNLOAD"));

        if (!loadConfiguration())
        {
            notConfigured();
            return;
        }

        DownloadSupportType downloadSupportTypes = getDownloadSupportType();

        if (this.continuing_type == DeviceDataHandler.TRANSFER_READ_DATA)
        {
            if (!DownloadSupportType.isOptionSet(downloadSupportTypes, DownloadSupportType.DownloadData))
            {
                boolean warned = showNoSupportDialog(downloadSupportTypes);

                if (!warned)
                {
                    String msg = String.format(i18nControl.getMessage("DEVICE_HAS_NO_DEVICE_DOWNLOAD_SUPPORT"),
                            this.configured_device.device_device, this.configured_device.device_company);
                    this.showWarningDialog(msg);
                }

                return;
            }
        }
        else if (this.continuing_type == DeviceDataHandler.TRANSFER_READ_DATA_FILE)
        {
            // FIXME add read_file_config
            if (!DownloadSupportType.isOptionSet(downloadSupportTypes, DownloadSupportType.DownloadDataFile))
            {
                boolean warned = showNoSupportDialog(downloadSupportTypes);

                if (!warned)
                {
                    String msg = String.format(i18nControl.getMessage("DEVICE_HAS_NO_FILE_DOWNLOAD_SUPPORT"),
                            this.configured_device.device_device, this.configured_device.device_company);
                    this.showWarningDialog(msg);
                }

                return;
            }

        }
        else if (this.continuing_type == DeviceDataHandler.TRANSFER_READ_CONFIGURATION)
        {
            if (!DownloadSupportType.isOptionSet(downloadSupportTypes, DownloadSupportType.DownloadConfig))
            {
                boolean warned = showNoSupportDialog(downloadSupportTypes);

                if (!warned)
                {
                    String msg = String.format(i18nControl.getMessage("DEVICE_HAS_NO_CONFIG_DOWNLOAD_SUPPORT"),
                            this.configured_device.device_device, this.configured_device.device_company);
                    this.showWarningDialog(msg);
                }

                return;
            }

        }
        else
        {
            System.out.println("System error: This option is not supported!");
        }

        init();

        ATSwingUtils.centerJDialog(this, parent);
        this.setResizable(false);
        this.setVisible(true);
    }


    private boolean showNoSupportDialog(DownloadSupportType status)
    {

        // if (DownloadSupportType.isOptionSet(status,
        // DownloadSupportType.DOWNLOAD_SUPPORT_NA_DEVICE))
        // {
        // String msg =
        // String.format(i18nControl.getMessage("DEVICE_HAS_NO_DOWNLOAD_SUPPORT"),
        // this.configured_device.device_device,
        // this.configured_device.device_company);
        // showWarningDialog(msg);
        // return true;
        // }
        // else
        if (DownloadSupportType.isOptionSet(status, DownloadSupportType.NotSupportedByGGC))
        {
            String msg = String.format(i18nControl.getMessage("DEVICE_DOWNLOAD_NOT_SUPPORTED_BY_GGC"),
                    this.configured_device.device_device, this.configured_device.device_company);
            showWarningDialog(msg);
            return true;
        }
        // else if (DownloadSupportType.isOptionSet(status,
        // DownloadSupportType.DOWNLOAD_SUPPORT_NA_GENERIC_DEVICE))
        // {
        // showWarningDialog(i18nControl.getMessage("DEVICE_DOWNLOAD_NOT_SUPPORTED_GENERIC"));
        // return true;
        // }

        return false;

    }


    private void showWarningDialog(String msg)
    {
        JOptionPane.showMessageDialog(dataAccess.getCurrentComponentParent(), msg, i18nControl.getMessage("WARNING"),
            JOptionPane.WARNING_MESSAGE);
    }


    private void notConfigured()
    {
        String dv = i18nControl.getMessage("DEVICE_NAME_NORMAL");
        String msg = String.format(i18nControl.getMessage("OOPS_DEVICE_NOT_CONFIGURED"), dv, dv, dv);
        JOptionPane.showMessageDialog(this, msg, i18nControl.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
    }


    private boolean loadConfiguration()
    {
        // System.out.println("Load configuration");

        this.configured_device = this.dataAccess.getDeviceConfiguration().getSelectedDeviceInstance();

        // System.out.println("Cfg: " + this.configured_device);

        if (this.configured_device == null)
            return false;

        // this.m_dtd.configured_device = configured_device;
        this.deviceDataHandler.setConfiguredDevice(configured_device);

        return createDeviceInstance();
    }


    private boolean createDeviceInstance()
    {
        boolean configured = false;

        DeviceInstanceWithHandler diwh = dataAccess.getManager().getDeviceV2(this.configured_device.device_company,
            this.configured_device.device_device);

        boolean doPreInit = false;

        if (diwh != null)
        {
            this.deviceDataHandler.setDeviceInterfaceVersion(DeviceInterfaceVersion.DeviceInterfaceVersion2);
            this.deviceDataHandler.setDeviceInterfaceV2(diwh);

            if (diwh.hasPreInit())
            {
                doPreInit = true;
            }

            configured = true;
        }
        else
        {

            this.deviceDataHandler.setDeviceInterfaceVersion(DeviceInterfaceVersion.DeviceInterfaceVersion1);

            DeviceInterface deviceV1 = dataAccess.getManager().getDeviceV1(this.configured_device.device_company,
                this.configured_device.device_device);

            if (deviceV1 != null)
            {
                deviceV1.setConnectionParameters(this.configured_device.communication_port_raw);

                this.deviceDataHandler.setDeviceInterfaceV1(deviceV1);

                DeviceAbstract dva = (DeviceAbstract) deviceV1;
                dva.preInitInit(this.configured_device.communication_port_raw, new ConsoleOutputWriter(), dataAccess);

                if (deviceV1.hasPreInit())
                {
                    is_preinit_done = false;
                    this.btnStart.setEnabled(false);
                    dva.setDataAccessInstance(dataAccess);
                    dva.setOutputWriter(new ConsoleOutputWriter());

                    doPreInit = true;
                }

                configured = true;
            }
        }

        if (doPreInit)
        {
            DevicePreInitRunner dpir = new DevicePreInitRunner(this.deviceDataHandler, this);
            dpir.start();
        }

        return configured;

    }


    private DownloadSupportType getDownloadSupportType()
    {
        if (deviceDataHandler.getDeviceInterfaceV1() != null)
        {
            return deviceDataHandler.getDeviceInterfaceV1().getDownloadSupportType();
        }
        else if (deviceDataHandler.getDeviceInterfaceV2() != null)
        {
            return deviceDataHandler.getDeviceInterfaceV2().getDownloadSupportType();
        }
        else
        {
            return DownloadSupportType.NotSupportedByGGC;
        }
    }


    private ImageIcon getDeviceIcon()
    {

        String root = dataAccess.getDeviceImagesRoot();

        if (this.configured_device == null)
            return dataAccess.getImageIcon(root, "no_device.gif");
        else
        {
            return dataAccess.getImageIcon(root, getDeviceInterfaceParameter(DEVICE_INTERFACE_PARAM_ICON_NAME));
        }
    }

    private static final int DEVICE_INTERFACE_PARAM_CONNECTION_TYPE = 1;
    private static final int DEVICE_INTERFACE_PARAM_STATUS = 2;
    private static final int DEVICE_INTERFACE_PARAM_INSTRUCTIONS = 3;
    private static final int DEVICE_INTERFACE_PARAM_ICON_NAME = 4;


    private String getDeviceInterfaceParameter(int param)
    {
        String value = unknownTranslation;

        switch (param)
        {
            case DeviceInstructionsDialog.DEVICE_INTERFACE_PARAM_CONNECTION_TYPE:
                {
                    if (deviceDataHandler.getDeviceInterfaceV2() != null)
                    {
                        value = i18nControl.getMessage(deviceDataHandler.getDeviceInterfaceV2().getConnectionProtocol()
                                .getI18nKey());
                    }
                    else if (deviceDataHandler.getDeviceInterfaceV1() != null)
                    {
                        value = i18nControl.getMessage(deviceDataHandler.getDeviceInterfaceV1().getConnectionProtocol()
                                .getI18nKey());
                    }

                    return value;
                }

            case DeviceInstructionsDialog.DEVICE_INTERFACE_PARAM_STATUS:
                {
                    value = i18nControl.getMessage("ERROR_IN_CONFIG");

                    if ((deviceDataHandler.getDeviceInterfaceV1() != null)
                            || (deviceDataHandler.getDeviceInterfaceV2() != null))
                    {
                        value = i18nControl.getMessage("READY");
                    }

                    return value;
                }

            case DeviceInstructionsDialog.DEVICE_INTERFACE_PARAM_INSTRUCTIONS:
                {
                    if (deviceDataHandler.getDeviceInterfaceV2() != null)
                    {
                        value = i18nControl.getMessage(deviceDataHandler.getDeviceInterfaceV2().getInstructions());
                    }
                    else if (deviceDataHandler.getDeviceInterfaceV1() != null)
                    {
                        value = i18nControl.getMessage(deviceDataHandler.getDeviceInterfaceV1().getInstructions());
                    }

                    return value;
                }

            case DeviceInstructionsDialog.DEVICE_INTERFACE_PARAM_ICON_NAME:
                {
                    if (deviceDataHandler.getDeviceInterfaceV2() != null)
                    {
                        value = deviceDataHandler.getDeviceInterfaceV2().getIconName();
                    }
                    else if (deviceDataHandler.getDeviceInterfaceV1() != null)
                    {
                        value = deviceDataHandler.getDeviceInterfaceV1().getIconName();
                    }
                    else
                    {
                        value = "no_device.gif";
                    }

                    return value;
                }

            default:
                return value;
        }
    }


    protected void init()
    {
        ATSwingUtils.initLibrary();

        setTitle(String.format(i18nControl.getMessage("CONFIGURED_DEVICE_INSTRUCTIONS"), i18nControl.getMessage("DEVICE_NAME_BIG")));

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(600, 700);

        JLabel label;

        Font normal = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL);

        setBounds(0, 0, 650, 600);
        dataAccess.centerJDialog(this);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().add(panel, BorderLayout.CENTER);

        // panel icon
        JPanel panel_icon = ATSwingUtils.getPanel(10, 10, 280, 380, null,
            new TitledBorder(String.format(i18nControl.getMessage("DEVICE_ICON"), i18nControl.getMessage("DEVICE_NAME_BIG"))), panel);

        label = new JLabel(this.getDeviceIcon());
        label.setBounds(10, 20, 260, 350);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        panel_icon.add(label);

        // panel configured device
        JPanel panel_device = ATSwingUtils.getPanel(300, 10, 330, 170, null,
            new TitledBorder(i18nControl.getMessage("CONFIGURED_DEVICE")), panel);

        ATSwingUtils.getLabel(i18nControl.getMessage("MY_DEVICE_NAME") + ":", 15, 20, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL_BOLD);

        ATSwingUtils.getLabel(this.configured_device.name, 130, 20, 320, 25, panel_device, ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(i18nControl.getMessage("DEVICE_COMPANY") + ":", 15, 40, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL_BOLD);

        ATSwingUtils.getLabel(this.configured_device.device_company, 130, 40, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(i18nControl.getMessage("DEVICE_NAME_") + ":", 15, 60, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL_BOLD);

        ATSwingUtils.getLabel(this.configured_device.device_device, 130, 60, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(i18nControl.getMessage("CONNECTION_TYPE") + ":", 15, 80, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL_BOLD);

        ATSwingUtils.getLabel(this.getDeviceInterfaceParameter(DEVICE_INTERFACE_PARAM_CONNECTION_TYPE), 130, 80, 320,
            25, panel_device, ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(i18nControl.getMessage("CONNECTION_PARAMETER") + ":", 15, 100, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL_BOLD);

        ATSwingUtils.getLabel(this.configured_device.communication_port, 130, 100, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(i18nControl.getMessage("STATUS") + ":", 15, 120, 320, 25, panel_device,
            ATSwingUtils.FONT_NORMAL_BOLD);

        label = ATSwingUtils.getLabel(this.getDeviceInterfaceParameter(DEVICE_INTERFACE_PARAM_STATUS), 130, 120, 320,
            25, panel_device, ATSwingUtils.FONT_NORMAL);

        JLabel dsFixName = ATSwingUtils.getLabel(i18nControl.getMessage("DAYLIGHTSAVINGS_FIX") + ":", 15, 140, 320, 25,
            panel_device, ATSwingUtils.FONT_NORMAL_BOLD);

        label = ATSwingUtils.getLabel("", 130, 140, 320, 25, panel_device, ATSwingUtils.FONT_NORMAL);

        if (dataAccess.getDeviceConfigurationDefinition().doesDeviceSupportTimeFix())
        {
            if (this.configured_device.ds_fix)
            {
                label.setText(this.configured_device.getDayLightFix());
                label.setToolTipText(this.configured_device.getDayLightFixLong());
            }
            else
            {
                label.setText(this.configured_device.getDayLightFix());
            }
        }
        else
        {
            // label.setText(i18nControl.getMessage("DEVICE_DOESNT_SUPPORT_DS_FIX"));
            // label.setToolTipText(i18nControl.getMessage("DEVICE_DOESNT_SUPPORT_DS_FIX"));
            label.setVisible(false);
            dsFixName.setVisible(false);
        }

        // device instructions
        JPanel panel_instruct = ATSwingUtils.getPanel(300, 190, 330, 200, new FlowLayout(),
            new TitledBorder(i18nControl.getMessage("INSTRUCTIONS")), panel);

        label = ATSwingUtils.getLabel(
            i18nControl.getMessage(getDeviceInterfaceParameter(DEVICE_INTERFACE_PARAM_INSTRUCTIONS)), 5, 0, 280, 180,
            panel_instruct, ATSwingUtils.FONT_NORMAL_SMALLER);
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setHorizontalAlignment(SwingConstants.LEFT);

        // bottom
        label = ATSwingUtils.getLabel(i18nControl.getMessage("INSTRUCTIONS_DESC"), 20, 400, 590, 120, panel,
            ATSwingUtils.FONT_NORMAL);
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        btnHelp = ATSwingUtils.createHelpButtonByBounds(30, 510, 130, 35, this, ATSwingUtils.FONT_NORMAL, dataAccess);
        btnHelp.setFont(normal);
        panel.add(btnHelp);

        dataAccess.enableHelp(this);

        ATSwingUtils.getButton("   " + i18nControl.getMessage("CANCEL"), 170, 510, 130, 35, panel, ATSwingUtils.FONT_NORMAL, "cancel.png",
            "cancel", this, dataAccess);

        label_waiting = ATSwingUtils.getLabel(i18nControl.getMessage("WAIT_UNTIL_DEVICE_PREINIT"), 310, 498, 300, 25, panel,
            ATSwingUtils.FONT_NORMAL);
        label_waiting.setVerticalAlignment(SwingConstants.TOP);
        label_waiting.setHorizontalAlignment(SwingConstants.RIGHT);

        btnStart.setBounds(370, 510, 240, 35);
        btnStart.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        btnStart.setIcon(ATSwingUtils.getImageIcon("arrow_right_blue.png", this, dataAccess));
        btnStart.setHorizontalTextPosition(SwingConstants.LEFT);
        btnStart.setActionCommand("start_download");
        btnStart.addActionListener(this);
        // btnStart.setEnabled(true);
        panel.add(btnStart);

        this.preInitDone(false);
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("cancel"))
        {
            dataAccess.removeComponent(this);
            this.dispose();
        }
        else if (action.equals("start_download"))
        {
            if (this.dataAccess.getDeviceConfigurationDefinition().doesDeviceSupportTimeFix()
                    && this.configured_device.ds_fix)
            {
                TimeZoneUtil tzu = TimeZoneUtil.getInstance();

                tzu.setTimeZone(this.configured_device.ds_area);
                tzu.setWinterTimeChange(this.configured_device.ds_winter_change);
                tzu.setSummerTimeChange(this.configured_device.ds_summer_change);
            }

            this.dispose();

            if (this.continuing_type == DeviceDataHandler.TRANSFER_READ_DATA)
            {
                dataAccess.removeComponent(this);
                new DeviceDisplayDataDialog(m_parent, dataAccess, deviceDataHandler);
            }
            else if (this.continuing_type == DeviceDataHandler.TRANSFER_READ_CONFIGURATION)
            {
                dataAccess.removeComponent(this);
                new DeviceDisplayConfigDialog(m_parent, dataAccess, deviceDataHandler);
            }
            else if (this.continuing_type == DeviceDataHandler.TRANSFER_READ_DATA_FILE)
            {
                readFile(DownloadSupportType.DownloadDataFile);
            }
            else if (this.continuing_type == DeviceDataHandler.TRANSFER_READ_CONFIGURATION_FILE)
            {
                readFile(DownloadSupportType.DownloadConfigFile);
            }

        }
        else
        {
            System.out.println("DeviceInstructionsDialog::Unknown command: " + action);
        }

    }


    private void readFile(DownloadSupportType downloadSupportType)
    {
        List<GGCPlugInFileReaderContext> downloadTypes = this.deviceDataHandler
                .getFileDownloadTypes(downloadSupportType);

        if (downloadTypes == null)
        {
            showWarningDialog(this.i18nControl.getMessage("INTERNAL_CONFIGURATION_ERROR"));
            dataAccess.removeComponent(this);
        }
        else if (downloadTypes.size() == 1)
        {
            deviceDataHandler.selected_file_context = downloadTypes.get(0);
            dataAccess.removeComponent(this);
            new ImportFileSelectorDialog(dataAccess, this, deviceDataHandler, downloadSupportType);
        }
        else
        {
            dataAccess.removeComponent(this);
            new MultipleFileSelectorDialog(dataAccess, this, deviceDataHandler, downloadSupportType);
        }

    }


    /** 
     * readingFinished
     */
    public void readingFinished()
    {
        /*
         * checkReading(true);
         * //System.out.println("DeviceInstructionDialog:readingFinished");
         * if (this.btnStart!=null)
         * {
         * this.btnStart.setEnabled(true);
         * this.label_waiting.setText("");
         * }
         */
    }


    private synchronized void checkReading(boolean status)
    {
        // System.out.println("CheckReading: " + status);
        reading_old_done = status;
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
        // "Read_Instruction";
        return "DeviceTool_Read_Instruction";
    }


    /**
     * Preinit Done
     * 
     * @param set 
     */
    public void preInitDone(boolean set)
    {
        if (set)
        {
            this.is_preinit_done = true;
        }

        if (this.is_preinit_done)
        {
            if (this.label_waiting != null)
            {
                this.btnStart.setEnabled(true);
                this.label_waiting.setVisible(false);
            }
            else
            {
                this.btnStart.setEnabled(true);
            }

        }

    }

}
