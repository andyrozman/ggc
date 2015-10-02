package ggc.plugin.cfg;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.ATSwingUtils;
import com.atech.utils.data.TimeZoneUtil;

import ggc.plugin.data.enums.DevicePortParameterType;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;
import ggc.plugin.protocol.DeviceConnectionProtocol;
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
 *  Filename:      DeviceConfigurationDialog  
 *  Description:   Configuration dialog for plug-in devices
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DeviceConfigurationDialog extends JDialog implements ActionListener, ChangeListener, ItemListener,
        HelpCapable
{

    private static final long serialVersionUID = -947278207985014744L;

    private static final Log LOG = LogFactory.getLog(DeviceConfigurationDialog.class);
    private I18nControlAbstract m_ic = null; // I18nControl.getInstance();
    private DataAccessPlugInBase m_da; // = DataAccessMeter.getInstance();

    // private JPanel prefPane;

    // protected static Vector<String> time_zones_vector = null;
    // protected static Hashtable<String, String> time_zones = null;
    JComboBox cb_timezone, cb_winter_fix, cb_summer_fix;
    JComboBox cb_entry;
    JCheckBox chb_fix;
    JTextField tf_name;
    JButton help_button;
    JLabel lbl_company, lbl_device;
    // CommunicationPortComponent comm_port_comp;

    JButton buttons[] = null;
    JPanel main_panel;
    JPanel pan_tzfix;

    // DeviceConfiguration dev_config = null;

    DeviceConfigurationDefinition dcd = null;
    Hashtable<String, DeviceConfigEntry> data;
    DeviceConfigEntry current_entry;
    int current_index = 0;
    String current_index_object = "";
    String first_selected = "";
    CommunicationSettingsPanel comm_settings;

    DeviceInterface currentDeviceV1 = null;
    DeviceInstanceWithHandler currentDeviceV2 = null;

    TimeZoneUtil timeZoneUtil = TimeZoneUtil.getInstance();


    // DeviceInterface current_device = null;

    /**
     * Constructor
     * 
     * @param parent
     * @param da
     */
    public DeviceConfigurationDialog(JFrame parent, ATDataAccessAbstract da)
    {
        super(parent, "", true);

        m_da = (DataAccessPlugInBase) da;
        m_ic = m_da.getI18nControlInstance();
        this.dcd = m_da.getDeviceConfigurationDefinition();

        m_da.addComponent(this);

        setTitle(String.format(m_ic.getMessage("DEVICE_CONFIGURATION"), m_ic.getMessage("DEVICE_NAME_BIG")));
        ATSwingUtils.centerJDialog(this, m_da.getCurrentComponentParent());

        // if (DeviceConfigurationDialog.time_zones_vector == null)
        // {
        // this.loadTimeZones();
        // }

        ATSwingUtils.initLibrary();

        loadData();

        init();

        this.setResizable(false);
        ATSwingUtils.centerJDialog(this, parent);

        this.setVisible(true);
    }


    private void loadData()
    {
        this.data = this.m_da.getDeviceConfiguration().getConfigDataCopy();
        this.current_entry = this.data.get(this.m_da.getDeviceConfiguration().getSelectedDeviceIndex());

        if (current_entry != null)
        {
            first_selected = this.m_da.getDeviceConfiguration().getSelectedDeviceIndex() + " - "
                    + this.current_entry.name;
        }
        else
        {
            first_selected = null;
        }
    }


    private void saveData()
    {
        this.saveItemData();

        DeviceConfiguration dc = m_da.getDeviceConfiguration();
        dc.setNewConfigData(data);
        dc.setSelectedDeviceFromConfigurator(this.current_index_object);
        // dc.setSelectedDevice(this.cb_entry.getSelectedIndex()+1);

        // System.out.println("Index: " + (this.cb_entry.getSelectedItem()));

        dc.writeConfigData();
    }


    private void resetComboBox()
    {
        this.cb_entry.removeAllItems();

        Object[] o = getComboEntriesFromConfiguration();

        for (Object element : o)
        {
            this.cb_entry.addItem(element);
        }

    }


    private void resetEntry()
    {
        this.currentDeviceV1 = null;
        this.currentDeviceV2 = null;

        showDevice();

        // FIXME
        // this.comm_port_comp.setCommunicationPort(m_ic.getMessage("NOT_SET"));
        this.comm_settings.setParameters(null);

        if (!dcd.doesDeviceSupportTimeFix())
            return;

        this.cb_timezone.setSelectedItem("(GMT+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague");

        this.chb_fix.setSelected(false);
        this.cb_summer_fix.setSelectedItem("0");
        this.cb_winter_fix.setSelectedItem("0");

    }


    private Object[] getComboEntriesFromConfiguration()
    {
        ArrayList<String> lst = new ArrayList<String>();

        String new_entry = null;

        for (int i = 1; i < 21; i++)
        {
            if (this.data.containsKey("" + i))
            {
                lst.add(i + " - " + this.data.get("" + i).name);
            }
            else
            {
                if (new_entry == null)
                {
                    new_entry = "" + i;
                }
            }
        }

        lst.add(m_ic.getMessage("NEW__") + " [" + new_entry + "]");

        return lst.toArray();

    }


    private void init()
    {
        this.setLayout(null);

        main_panel = ATSwingUtils.getPanel(0, 0, 450, 600, null, null, null);

        ATSwingUtils.getTitleLabel(
            String.format(m_ic.getMessage("DEVICE_CONFIGURATION"), m_ic.getMessage("DEVICE_NAME_BIG")), 0, 20, 450, 30,
            main_panel, ATSwingUtils.FONT_BIG_BOLD);

        // select
        JPanel pan_sel = ATSwingUtils.getPanel(20, 75, 410, 60, null,
            new TitledBorder(String.format(m_ic.getMessage("SELECT_X_DEVICE"), m_ic.getMessage("DEVICE_NAME_BIG"))),
            main_panel);

        ATSwingUtils.getLabel(String.format(m_ic.getMessage("SELECTED_X_DEVICE"), m_ic.getMessage("DEVICE_NAME_BIG"))
                + ":", 25, 25, 200, 25, pan_sel, ATSwingUtils.FONT_NORMAL_BOLD);

        cb_entry = ATSwingUtils.getComboBox(getComboEntriesFromConfiguration(), 205, 25, 190, 25, pan_sel,
            ATSwingUtils.FONT_NORMAL);

        if (first_selected != null)
        {
            cb_entry.setSelectedItem(this.first_selected);
            // else
            // cb_entry.setSelectedIndex(0);
        }

        cb_entry.addItemListener(this);

        // device configuration
        JPanel pan_meter = ATSwingUtils
                .getPanel(
                    20,
                    140,
                    410,
                    125,
                    null,
                    new TitledBorder(String.format(m_ic.getMessage("DEVICE_CONFIGURATION"),
                        m_ic.getMessage("DEVICE_NAME_BIG"))), main_panel);

        ATSwingUtils.getLabel(String.format(m_ic.getMessage("CUSTOM_NAME"), m_ic.getMessage("DEVICE_NAME_BIG")) + ":",
            25, 25, 150, 25, pan_meter, ATSwingUtils.FONT_NORMAL_BOLD);
        this.tf_name = ATSwingUtils.getTextField("", 170, 25, 225, 25, pan_meter);

        ATSwingUtils.getLabel(m_ic.getMessage("DEVICE_COMPANY") + ":", 25, 55, 150, 25, pan_meter,
            ATSwingUtils.FONT_NORMAL_BOLD);
        this.lbl_company = ATSwingUtils.getLabel(m_ic.getMessage("NO_COMPANY_SELECTED"), 110, 55, 250, 25, pan_meter,
            ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(m_ic.getMessage("DEVICE_DEVICE") + ":", 25, 85, 450, 25, pan_meter,
            ATSwingUtils.FONT_NORMAL_BOLD);
        this.lbl_device = ATSwingUtils.getLabel(m_ic.getMessage("NO_DEVICE_SELECTED"), 110, 85, 250, 25, pan_meter,
            ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getButton(m_ic.getMessage("SELECT"), 295, 55, 100, 55, pan_meter, ATSwingUtils.FONT_NORMAL, null,
            "device_selector", this, m_da);

        comm_settings = new CommunicationSettingsPanel(20, 270, m_da, this);
        main_panel.add(this.comm_settings);

        /*
         * JPanel pan_comm_settings = ATSwingUtils.getPanel(20, 250, 410, 80,
         * null,
         * new TitledBorder(m_ic.getMessage("COMMUNICATION_SETTINGS")),
         * main_panel);
         * this.comm_port_comp = new CommunicationPortComponent(dataAccess,
         * this);
         * pan_comm_settings.add(this.comm_port_comp);
         */

        int start_y = 270 + 5 + comm_settings.getHeight();

        if (this.dcd.doesDeviceSupportTimeFix())
        {

            // timezone fix panel
            pan_tzfix = ATSwingUtils.getPanel(20, start_y, 410, 200, null,
                new TitledBorder(m_ic.getMessage("TIMEZONE_CONFIGURATION")), main_panel);

            ATSwingUtils.getLabel(m_ic.getMessage("SELECT_TIMEZONE_LIST") + ":", 25, 25, 450, 25, pan_tzfix,
                ATSwingUtils.FONT_NORMAL_BOLD);

            cb_timezone = ATSwingUtils.getComboBox(this.timeZoneUtil.getTimezonesAsVector(), 25, 50, 370, 25,
                pan_tzfix, ATSwingUtils.FONT_NORMAL);

            chb_fix = ATSwingUtils.getCheckBox("  " + m_ic.getMessage("NEED_DAYLIGHTSAVING_FIX"), 25, 90, 380, 25,
                pan_tzfix, ATSwingUtils.FONT_NORMAL_BOLD);
            chb_fix.addChangeListener(this);
            chb_fix.setSelected(false);

            String[] changes = { "-1", "0", "+1" };

            ATSwingUtils.getLabel(m_ic.getMessage("WINTERTIME_FIX") + ":", 40, 120, 200, 25, pan_tzfix,
                ATSwingUtils.FONT_NORMAL_BOLD);

            this.cb_winter_fix = ATSwingUtils.getComboBox(changes, 240, 120, 80, 25, pan_tzfix,
                ATSwingUtils.FONT_NORMAL);
            this.cb_winter_fix.setSelectedIndex(1);

            ATSwingUtils.getLabel(m_ic.getMessage("SUMMERTIME_FIX") + ":", 40, 155, 200, 25, pan_tzfix,
                ATSwingUtils.FONT_NORMAL_BOLD);

            this.cb_summer_fix = ATSwingUtils.getComboBox(changes, 240, 155, 80, 25, pan_tzfix,
                ATSwingUtils.FONT_NORMAL);
            this.cb_summer_fix.setSelectedIndex(1);

            // start_y = 510;
            start_y += 210;

            enableDisableFix(false);
        }

        buttons = new JButton[3];

        buttons[0] = ATSwingUtils.getButton("  " + m_ic.getMessage("OK"), 50, start_y + 10, 110, 25, main_panel,
            ATSwingUtils.FONT_NORMAL, "ok.png", "ok", this, m_da);

        buttons[1] = ATSwingUtils.getButton("  " + m_ic.getMessage("CANCEL"), 170, start_y + 10, 110, 25, main_panel,
            ATSwingUtils.FONT_NORMAL, "cancel.png", "cancel", this, m_da);

        help_button = ATSwingUtils.createHelpButtonByBounds(290, start_y + 10, 110, 25, this, ATSwingUtils.FONT_NORMAL,
            m_da.getImagesRoot(), m_ic); // ATDataAccessAbstract.FONT_NORMAL);
        main_panel.add(help_button);
        buttons[2] = help_button;

        // this.cb_entry.setSelectedItem(this.first_selected);

        this.comm_settings.setProtocol(DeviceConnectionProtocol.None);
        // comm_port_comp.setProtocol(0);
        current_index = this.cb_entry.getSelectedIndex();
        this.current_index_object = (String) this.cb_entry.getSelectedItem();
        this.loadItemData();

        if (this.current_entry == null)
        {
            this.tf_name.setText(m_ic.getMessage("MY__") + " " + m_ic.getMessage("DEVICE_NAME_BIG") + " #1");
        }

        setSize(450, start_y + 100);
        main_panel.setBounds(0, 0, 450, start_y + 100);
        getContentPane().add(main_panel, null);

        showDevice();
    }


    private void enableDisableFix(boolean enable)
    {
        this.cb_summer_fix.setEnabled(enable);
        this.cb_winter_fix.setEnabled(enable);
    }


    private void refreshCommunicationSettings()
    {
        int start_y = 275;

        // System.out.println("this.current_device: " + this.current_device);

        if (this.currentDeviceV1 != null
                && (this.currentDeviceV1.hasDefaultParameter() || this.currentDeviceV1.hasSpecialConfig()))
        {
            this.comm_settings.setCurrentDevice(this.currentDeviceV1);
            start_y += comm_settings.getHeight(); // .getBounds().height;
            this.comm_settings.setVisible(true);
        }
        else if (this.currentDeviceV2 != null
                && (this.currentDeviceV2.getDevicePortParameterType() == DevicePortParameterType.SimpleParameter || this.currentDeviceV2
                        .hasSpecialConfig()))
        {
            this.comm_settings.setCurrentDevice(this.currentDeviceV2);

            start_y += comm_settings.getHeight(); // .getBounds().height;
            this.comm_settings.setVisible(true);
        }
        else
        {
            // System.out.println("No parameters used !");
            comm_settings.resetDevices();
            this.comm_settings.setVisible(false);
        }

        if (this.dcd.doesDeviceSupportTimeFix())
        {
            this.pan_tzfix.setBounds(20, start_y, 410, 200);
            start_y += 210;
        }

        buttons[0].setBounds(50, start_y + 10, 110, 25);
        buttons[1].setBounds(170, start_y + 10, 110, 25);
        buttons[2].setBounds(290, start_y + 10, 110, 25);

        setSize(450, start_y + 80);
        main_panel.setBounds(0, 0, 450, start_y + 80);

    }


    private void showDevice()
    {
        if ((this.currentDeviceV1 == null) && (this.currentDeviceV2 == null))
        {
            // System.out.println("current device: " + this.current_device);
            this.lbl_company.setText(m_ic.getMessage("NO_COMPANY_SELECTED"));
            this.lbl_device.setText(m_ic.getMessage("NO_DEVICE_SELECTED"));
            // this.comm_port_comp.setProtocol(0);
            this.comm_settings.setProtocol(DeviceConnectionProtocol.None);
            this.comm_settings.setParameters(null);

            this.refreshCommunicationSettings();
        }
        else
        {
            this.lbl_company.setText(getParameterValue(1));
            this.lbl_device.setText(getParameterValue(2));
            // this.comm_port_comp.setProtocol(this.current_device.getConnectionProtocol());
            // System.out.println("current device: " + this.current_device);

            this.refreshCommunicationSettings();

            if (this.comm_settings != null)
            {

                if (this.currentDeviceV1 != null)
                {
                    this.comm_settings.setCurrentDevice(this.currentDeviceV1);
                }
                else
                {
                    this.comm_settings.setCurrentDevice(this.currentDeviceV2);
                }

                if (this.current_entry != null)
                {
                    this.comm_settings.setParameters(this.current_entry.communication_port_raw);
                }
                else
                {
                    this.comm_settings.setParameters(null);
                }

                // this.comm_settings.setParameters(this.current_device.)
                // x this.refreshCommunicationSettings();
            }

        }

    }

    private static final int PARAMETER_COMPANY = 1;
    private static final int PARAMETER_DEVICE_NAME = 2;


    private String getParameterValue(int parameter)
    {
        if (this.currentDeviceV2 != null)
        {
            return this.currentDeviceV2.getColumnValue(parameter);
        }
        else
        {
            return this.currentDeviceV1.getColumnValue(parameter);
        }
    }

    // ---
    // --- End
    // ---

    private boolean action_was = false;


    /**
     * Was Action
     * 
     * @return
     */
    public boolean wasAction()
    {
        return action_was;
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("ok"))
        {

            if ((this.currentDeviceV1 == null && this.currentDeviceV2 == null))
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("CONFIG_ERROR_NO_DEVICE_OR_PARAMETERS"),
                    m_ic.getMessage("WARNING"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!this.comm_settings.areParametersSet())
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("CONFIG_ERROR_NO_DEVICE_OR_PARAMETERS"),
                    m_ic.getMessage("WARNING"), JOptionPane.ERROR_MESSAGE);
                return;
            }

            action_was = true;

            this.saveData();

            this.m_da.resetSelectedDeviceInstance();

            this.dispose();
            m_da.removeComponent(this);
        }
        else if (action.equals("cancel"))
        {
            action_was = false;
            this.dispose();
            m_da.removeComponent(this);
        }
        else if (action.equals("device_selector"))
        {
            DeviceSelectorDialog dsd = new DeviceSelectorDialog(this, m_da);

            if (dsd.wasAction())
            {
                setSelectedDeviceFromObject(dsd.getSelectedObject());

                // this.current_device = (DeviceInterface)
                // dsd.getSelectedObject();
                this.current_entry = new DeviceConfigEntry(m_ic);
                this.refreshCommunicationSettings();
                this.comm_settings.setParameters(null);

                System.out.println("Device V1 " + currentDeviceV1 + ", Device V2 " + currentDeviceV2);

                showDevice();
            }

        }
        else
        {
            LOG.error("DeviceConfigurationDialog: Unknown command: " + action);
        }

    }


    private void setSelectedDeviceFromObject(Object selected)
    {

        if (selected instanceof DeviceInstanceWithHandler)
        {
            this.currentDeviceV2 = (DeviceInstanceWithHandler) selected;
            this.currentDeviceV1 = null;
        }
        else
        {
            this.currentDeviceV1 = (DeviceInterface) selected;
            this.currentDeviceV2 = null;
        }
    }


    /*
     * public String getData()
     * {
     * StringBuilder sb = new StringBuilder();
     * String tz = (String)this.cb_timezone.getSelectedItem();
     * sb.append("METER_X_TIMEZONE=" +
     * DeviceConfigurationDialog.time_zones.get(tz) + "\n");
     * if (this.chb_fix.isSelected())
     * {
     * sb.append("METER_X_DAYLIGHTSAVING_FIX=YES\n");
     * sb.append("METER_X_WINTER_FIX=" + this.cb_winter_fix.getSelectedItem() +
     * "\n");
     * sb.append("METER_X_SUMMER_FIX=" + this.cb_summer_fix.getSelectedItem() +
     * "\n");
     * }
     * else
     * {
     * sb.append("METER_X_DAYLIGHTSAVING_FIX=NO\n");
     * sb.append("METER_X_WINTER_FIX=0\n");
     * sb.append("METER_X_SUMMER_FIX=0\n");
     * }
     * return sb.toString();
     * }
     */

    /** 
     * stateChanged
     */
    public void stateChanged(ChangeEvent e)
    {
        enableDisableFix(this.chb_fix.isSelected());
    }


    private void loadItemData()
    {
        if (this.current_entry == null)
            return;

        findAndSetDevice();
        // this.current_entry = this.data.get(en);
        // loadItemData();

        this.showDevice();

        this.tf_name.setText(this.current_entry.name);

        if (!dcd.doesDeviceSupportTimeFix())
            return;

        this.cb_timezone.setSelectedItem(this.current_entry.ds_area_long);

        this.chb_fix.setSelected(this.current_entry.ds_fix);
        this.cb_summer_fix.setSelectedItem(getTimeChange(this.current_entry.ds_summer_change));
        this.cb_winter_fix.setSelectedItem(getTimeChange(this.current_entry.ds_winter_change));
    }


    private String getTimeChange(int change)
    {
        if (change == 0)
            return "0";
        else if (change == -1)
            return "-1";
        else
            // if (change==+1)
            return "+1";

    }


    /**
     * Save Item Data
     */
    public void saveItemData()
    {
        DeviceConfigEntry dce = new DeviceConfigEntry(m_ic);

        dce.name = this.tf_name.getText();
        dce.device_company = this.lbl_company.getText();
        dce.device_device = this.lbl_device.getText();

        // if (is_new)
        // dce.communication_port_raw = "";
        // else
        dce.communication_port_raw = this.comm_settings.getParameters(); // .comm_port_comp.getCommunicationPort();

        if (this.comm_settings.getParameters().equals(m_ic.getMessage("NOT_SET")))
        {
            dce.communication_port_raw = "";
        }

        dce.processCommunicationSettings();

        if (dcd.doesDeviceSupportTimeFix())
        {
            dce.ds_area_long = (String) this.cb_timezone.getSelectedItem();
            dce.ds_area = this.timeZoneUtil.getTimezoneDescription(dce.ds_area_long);
            dce.ds_fix = this.chb_fix.isSelected();

            dce.ds_summer_change = getNumber((String) this.cb_summer_fix.getSelectedItem());
            dce.ds_winter_change = getNumber((String) this.cb_winter_fix.getSelectedItem());
        }

        if (this.current_index_object.startsWith(m_ic.getMessage("NEW__")))
        {
            String en_num = this.current_index_object.substring(this.current_index_object.indexOf("[") + 1,
                this.current_index_object.indexOf("]"));
            this.data.put(en_num, dce);

            resetComboBox();
            this.cb_entry.setSelectedIndex(m_da.getIntValueFromString(en_num) - 1);
        }
        else
        {
            String id = this.current_index_object.substring(0, this.current_index_object.indexOf(" ")).trim();

            DeviceConfigEntry dce_old = this.data.get(id);

            this.data.remove(dce_old);
            this.data.put(id, dce);
        }

    }


    private int getNumber(String item)
    {
        if (item.equals("+1"))
            return 1;
        else if (item.equals("0"))
            return 0;
        else
            return -1;
    }


    private void findAndSetDevice()
    {
        if (this.current_entry == null)
            return;

        Object selectedObject = this.m_da.getDeviceConfigurationDefinition().getSpecificDeviceInstance(
            this.current_entry.device_company, this.current_entry.device_device);

        if (selectedObject != null)
        {
            setSelectedDeviceFromObject(selectedObject);
        }
        else
        {
            LOG.error("Configured device not found: " + this.current_entry.device_company + " "
                    + this.current_entry.device_device);
        }
    }

    boolean act = false;


    /**
     * Item State Changed
     * 
     * @see java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
     */
    public void itemStateChanged(ItemEvent arg0)
    {
        if (act)
            return;

        if (this.current_index == this.cb_entry.getSelectedIndex())
            return;

        act = true;
        saveItemData();
        String en = (String) this.cb_entry.getSelectedItem();

        if (en.startsWith(m_ic.getMessage("NEW__")))
        {
            String en_num = en.substring(en.indexOf("[") + 1, en.indexOf("]"));
            this.tf_name.setText(m_ic.getMessage("MY__") + " " + m_ic.getMessage("DEVICE_NAME_BIG") + " #" + en_num);
            resetEntry();
        }
        else
        {
            en = en.substring(0, en.indexOf(" "));
            this.current_entry = this.data.get(en);
            loadItemData();
        }

        // System.out.println("item changed: " +
        // this.cb_entry.getSelectedItem());
        this.current_index = this.cb_entry.getSelectedIndex();
        this.current_index_object = (String) this.cb_entry.getSelectedItem();
        act = false;
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
        return this.help_button;
    }


    /** 
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        // return
        // this.dataAccess.getDeviceConfigurationDefinition().getHelpPrefix() +
        // "Configuration";
        return "DeviceTool_Configuration";
    }

}
