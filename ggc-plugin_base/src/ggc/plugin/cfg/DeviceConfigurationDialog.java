package ggc.plugin.cfg;

import ggc.plugin.device.DeviceInterface;
import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;
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
 *  Filename:      DeviceConfigurationDialog  
 *  Description:   Configuration dialog for plug-in devices
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class DeviceConfigurationDialog extends JDialog implements ActionListener, ChangeListener, ItemListener, HelpCapable
{

    private static final long serialVersionUID = -947278207985014744L;

    private I18nControlAbstract m_ic = null; //I18nControl.getInstance();        
    private DataAccessPlugInBase m_da; // = DataAccessMeter.getInstance();

    //private JPanel prefPane;
    
    protected static Vector<String> time_zones_vector = null;
    protected static Hashtable<String,String> time_zones = null;
    JComboBox cb_timezone, cb_winter_fix, cb_summer_fix;
    JComboBox cb_entry;
    JCheckBox chb_fix;
    JTextField tf_name;
    JButton help_button;
    JLabel lbl_company, lbl_device;
    CommunicationPortComponent comm_port_comp;
    
    //DeviceConfiguration dev_config = null;

    DeviceConfigurationDefinition dcd = null;
    DeviceInterface current_device = null;
    Hashtable<String,DeviceConfigEntry> data;
    DeviceConfigEntry current_entry;
    int current_index = 0;
    String current_index_object = "";
    String first_selected = "";

    /**
     * Constructor
     * 
     * @param parent
     * @param da
     */
    public DeviceConfigurationDialog(JFrame parent, ATDataAccessAbstract da)
    {
        super(parent, "", true);
        
        m_da = (DataAccessPlugInBase)da;
        m_ic = m_da.getI18nControlInstance();
        this.dcd = m_da.getDeviceConfigurationDefinition();
        
        m_da.addComponent(this);

        setSize(450,600);
        setTitle(String.format(m_ic.getMessage("DEVICE_CONFIGURATION"), m_ic.getMessage("DEVICE_NAME_BIG")));
        m_da.centerJDialog(this, m_da.getCurrentComponentParent());

        if (DeviceConfigurationDialog.time_zones_vector==null)
            this.loadTimeZones();
        
        ATSwingUtils.initLibrary();
        
        loadData();

        init();
        
        this.setResizable(false);
        this.m_da.centerJDialog(this, parent);
        
        this.setVisible(true);
    }

    
    private void loadData()
    {
        this.data = this.m_da.getDeviceConfiguration().getConfigDataCopy();
        this.current_entry = this.data.get(this.m_da.getDeviceConfiguration().getSelectedDeviceIndex());
        
        if (current_entry!=null)
            first_selected = this.m_da.getDeviceConfiguration().getSelectedDeviceIndex() + " - " + this.current_entry.name;
        else 
            first_selected = null;
    }
    
    
    private void saveData()
    {
        this.saveItemData();
        
        DeviceConfiguration dc = m_da.getDeviceConfiguration();
        dc.setNewConfigData(data);
        dc.setSelectedDeviceFromConfigurator(this.current_index_object);
        
        dc.writeConfigData();
    }
    

    private void resetComboBox()
    {
        this.cb_entry.removeAllItems();
        
        Object[] o = getComboEntriesFromConfiguration();

        for(int i=0; i<o.length; i++)
        {
            this.cb_entry.addItem(o[i]);
        }
        
    }
    
    
    private void resetEntry()
    {
        this.current_device = null;
        showDevice();
        
        this.comm_port_comp.setCommunicationPort(m_ic.getMessage("NOT_SET"));
        
        
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
        
        //System.out.println("Data: " + this.data);
        
        String new_entry = null;
        
        
        for(int i=1; i<21; i++)
        {
            if (this.data.containsKey("" + i))
            {
                lst.add(i + " - " + this.data.get(""+i).name);
            }
            else
            {
                if (new_entry==null)
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

        JPanel main_panel = ATSwingUtils.getPanel(0,0,450,600, null, null, null); 

        ATSwingUtils.getTitleLabel(String.format(m_ic.getMessage("DEVICE_CONFIGURATION"), m_ic.getMessage("DEVICE_NAME_BIG")), 0,20, 450, 30, main_panel, ATSwingUtils.FONT_BIG_BOLD); 

        // select 
        JPanel pan_sel = ATSwingUtils.getPanel(20, 75, 410, 60, null, new TitledBorder(String.format(m_ic.getMessage("SELECT_X_DEVICE"), m_ic.getMessage("DEVICE_NAME_BIG"))), main_panel); 

        ATSwingUtils.getLabel(String.format(m_ic.getMessage("SELECTED_X_DEVICE"), m_ic.getMessage("DEVICE_NAME_BIG")) + ":", 25,25, 200, 25, pan_sel, ATSwingUtils.FONT_NORMAL_BOLD);

        cb_entry = ATSwingUtils.getComboBox(getComboEntriesFromConfiguration(), 205, 25, 190, 25, pan_sel, ATSwingUtils.FONT_NORMAL);
        
        if (first_selected!=null)
            cb_entry.setSelectedItem(this.first_selected);
        //else
        //    cb_entry.setSelectedIndex(0);
        
        cb_entry.addItemListener(this);

        // device configuration
        JPanel pan_meter = ATSwingUtils.getPanel(20, 140, 410, 150, 
            null, 
            new TitledBorder(String.format(m_ic.getMessage("DEVICE_CONFIGURATION"), m_ic.getMessage("DEVICE_NAME_BIG"))), 
            main_panel);
        
        ATSwingUtils.getLabel(String.format(m_ic.getMessage("DEVICE_CUSTOM_NAME"), m_ic.getMessage("DEVICE_NAME_BIG")) + ":", 25, 25, 150, 25, pan_meter, ATSwingUtils.FONT_NORMAL_BOLD);
        this.tf_name = ATSwingUtils.getTextField("", 170, 25, 225, 25, pan_meter);
        
        ATSwingUtils.getLabel(m_ic.getMessage("DEVICE_COMPANY") + ":", 25, 55, 150, 25, pan_meter, ATSwingUtils.FONT_NORMAL_BOLD);
        this.lbl_company = ATSwingUtils.getLabel(m_ic.getMessage("NO_COMPANY_SELECTED"), 110, 55, 250, 25, pan_meter, ATSwingUtils.FONT_NORMAL);
        
        ATSwingUtils.getLabel(m_ic.getMessage("DEVICE_DEVICE") + ":", 25,85, 450, 25, pan_meter, ATSwingUtils.FONT_NORMAL_BOLD);
        this.lbl_device = ATSwingUtils.getLabel(m_ic.getMessage("NO_DEVICE_SELECTED"), 110, 85, 250, 25, pan_meter, ATSwingUtils.FONT_NORMAL);
        
        ATSwingUtils.getButton(m_ic.getMessage("SELECT"), 295, 55, 100, 55, 
            pan_meter, ATSwingUtils.FONT_NORMAL, null, "device_selector", this, m_da);
        
        this.comm_port_comp = new CommunicationPortComponent(m_da, this);
        pan_meter.add(this.comm_port_comp);
        

        // timezone fix panel
        JPanel pan_tzfix = ATSwingUtils.getPanel(20, 295, 410, 200, null, new TitledBorder(m_ic.getMessage("TIMEZONE_CONFIGURATION")), main_panel); 
        
        ATSwingUtils.getLabel(m_ic.getMessage("SELECT_TIMEZONE_LIST") + ":", 25,25, 450, 25, pan_tzfix, ATSwingUtils.FONT_NORMAL_BOLD);
        
        cb_timezone = ATSwingUtils.getComboBox(DeviceConfigurationDialog.time_zones_vector, 25, 50, 370, 25, pan_tzfix, ATSwingUtils.FONT_NORMAL); 

        chb_fix = ATSwingUtils.getCheckBox("  " + m_ic.getMessage("NEED_DAYLIGHTSAVING_FIX"), 25, 90, 380, 25, pan_tzfix, ATSwingUtils.FONT_NORMAL_BOLD);
        chb_fix.addChangeListener(this);
        chb_fix.setSelected(false);

        String[] changes = { "-1", "0", "+1" }; 
        
        ATSwingUtils.getLabel(m_ic.getMessage("WINTERTIME_FIX") + ":", 40,120, 200, 25, pan_tzfix, ATSwingUtils.FONT_NORMAL_BOLD);
        
        this.cb_winter_fix = ATSwingUtils.getComboBox(changes, 240, 120, 80, 25, pan_tzfix, ATSwingUtils.FONT_NORMAL); 
        this.cb_winter_fix.setSelectedIndex(1);

        ATSwingUtils.getLabel(m_ic.getMessage("SUMMERTIME_FIX") + ":", 40, 155, 200, 25, pan_tzfix, ATSwingUtils.FONT_NORMAL_BOLD);

        this.cb_summer_fix = ATSwingUtils.getComboBox(changes, 240, 155, 80, 25, pan_tzfix, ATSwingUtils.FONT_NORMAL); 
        this.cb_summer_fix.setSelectedIndex(1);
        
        ATSwingUtils.getButton("  " + m_ic.getMessage("OK"), 50, 510, 110, 25, 
                                main_panel, ATSwingUtils.FONT_NORMAL, "ok.png", "ok", this, m_da);

        ATSwingUtils.getButton("  " + m_ic.getMessage("CANCEL"), 170, 510, 110, 25, 
            main_panel, ATSwingUtils.FONT_NORMAL, "cancel.png", "cancel", this, m_da);
        
        help_button = m_da.createHelpButtonByBounds(290, 510, 110, 25, this, ATSwingUtils.FONT_NORMAL); //ATDataAccessAbstract.FONT_NORMAL); 
        main_panel.add(help_button);

        enableDisableFix(false);
        
        //this.cb_entry.setSelectedItem(this.first_selected);
        
        this.comm_port_comp.setProtocol(0);
        current_index = this.cb_entry.getSelectedIndex();
        this.current_index_object = (String)this.cb_entry.getSelectedItem();
        this.loadItemData();
        
        if (this.current_entry==null)
        {
            this.tf_name.setText("My " + m_ic.getMessage("DEVICE_NAME_BIG") + " #1");
        }
        
        getContentPane().add(main_panel, null);
    }


    private void enableDisableFix(boolean enable)
    {
        this.cb_summer_fix.setEnabled(enable);
        this.cb_winter_fix.setEnabled(enable);
    }
    

    private void showDevice()
    {
        if (this.current_device==null)
        {
            this.lbl_company.setText(m_ic.getMessage("NO_COMPANY_SELECTED")); 
            this.lbl_device.setText(m_ic.getMessage("NO_DEVICE_SELECTED"));
            this.comm_port_comp.setProtocol(0);
        }
        else
        {
            this.lbl_company.setText(this.current_device.getColumnValue(1)); 
            this.lbl_device.setText(this.current_device.getColumnValue(2));
            this.comm_port_comp.setProtocol(this.current_device.getConnectionProtocol());
        }
    }
    

    // ---
    // ---  End
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
            action_was = true;
            
            this.saveData();
            
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
                this.current_device = (DeviceInterface)dsd.getSelectedObject();
                showDevice();
            }
            
        }
        else
            System.out.println("DeviceConfigurationDialog: Unknown command: " + action);

    }

    
/*    
    public String getData()
    {
        StringBuilder sb = new StringBuilder();
        
        String tz = (String)this.cb_timezone.getSelectedItem();
        
        sb.append("METER_X_TIMEZONE=" +  DeviceConfigurationDialog.time_zones.get(tz) + "\n");
        
        if (this.chb_fix.isSelected())
        {
            sb.append("METER_X_DAYLIGHTSAVING_FIX=YES\n");
            sb.append("METER_X_WINTER_FIX=" + this.cb_winter_fix.getSelectedItem() + "\n");
            sb.append("METER_X_SUMMER_FIX=" + this.cb_summer_fix.getSelectedItem() + "\n");
        }
        else
        {
            sb.append("METER_X_DAYLIGHTSAVING_FIX=NO\n");
            sb.append("METER_X_WINTER_FIX=0\n");
            sb.append("METER_X_SUMMER_FIX=0\n");
        }
        
        return sb.toString();
        
    }
*/


    
    /** 
     * stateChanged
     */
    public void stateChanged(ChangeEvent e)
    {
        enableDisableFix(this.chb_fix.isSelected());
    }

    
    
    /**
     * Load Time Zones
     */
    public void loadTimeZones()
    {
        DeviceConfigurationDialog.time_zones = new Hashtable<String,String>();
        DeviceConfigurationDialog.time_zones_vector = new Vector<String>();

        // Posible needed enchancment. We should probably list all ID's as values. On windows default ID can be different 
        // as in this table. We should add this names, if we encounter problems.

        addTimeZoneEntry("(GMT+13:00) Nuku'alofa", "Pacific/Tongatapu");
        addTimeZoneEntry("(GMT+12:00) Fiji, Kamchatka, Marshall Is.", "Pacific/Fiji");
        addTimeZoneEntry("(GMT+12:00) Auckland, Wellington", "Pacific/Auckland");
        addTimeZoneEntry("(GMT+11:00) Magadan, Solomon Is., New Caledonia", "Asia/Magadan");
        addTimeZoneEntry("(GMT+10:00) Vladivostok", "Asia/Vladivostok");
        addTimeZoneEntry("(GMT+10:00) Hobart", "Australia/Hobart");
        addTimeZoneEntry("(GMT+10:00) Guam, Port Moresby", "Pacific/Guam");
        addTimeZoneEntry("(GMT+10:00) Canberra, Melbourne, Sydney", "Australia/Sydney");
        addTimeZoneEntry("(GMT+10:00) Brisbane", "Australia/Brisbane");
        addTimeZoneEntry("(GMT+09:30) Adelaide", "Australia/Adelaide");
        addTimeZoneEntry("(GMT+09:00) Yakutsk", "Asia/Yakutsk");
        addTimeZoneEntry("(GMT+09:00) Seoul", "Asia/Seoul");
        addTimeZoneEntry("(GMT+09:00) Osaka, Sapporo, Tokyo", "Asia/Tokyo");
        addTimeZoneEntry("(GMT+08:00) Taipei", "Asia/Taipei");
        addTimeZoneEntry("(GMT+08:00) Perth", "Australia/Perth");
        addTimeZoneEntry("(GMT+08:00) Kuala Lumpur, Singapore", "Asia/Kuala_Lumpur");
        addTimeZoneEntry("(GMT+08:00) Irkutsk, Ulaan Bataar", "Asia/Irkutsk");
        addTimeZoneEntry("(GMT+08:00) Beijing, Chongqing, Hong Kong, Urumqi", "Asia/Hong_Kong");
        addTimeZoneEntry("(GMT+07:00) Krasnoyarsk", "Asia/Krasnoyarsk");
        addTimeZoneEntry("(GMT+07:00) Bangkok, Hanoi, Jakarta", "Asia/Bangkok");
        addTimeZoneEntry("(GMT+06:30) Rangoon", "Asia/Rangoon");
        addTimeZoneEntry("(GMT+06:00) Sri Jayawardenepura", "Asia/Colombo");
        addTimeZoneEntry("(GMT+06:00) Astana, Dhaka", "Asia/Dhaka");
        addTimeZoneEntry("(GMT+06:00) Almaty, Novosibirsk", "Asia/Almaty");
        addTimeZoneEntry("(GMT+05:45) Kathmandu", "Asia/Katmandu");
        addTimeZoneEntry("(GMT+05:30) Chennai, Kolkata, Mumbai, New Delhi", "Asia/Calcutta");
        addTimeZoneEntry("(GMT+05:00) Islamabad, Karachi, Tashkent", "Asia/Karachi");
        addTimeZoneEntry("(GMT+05:00) Ekaterinburg", "Asia/Yekaterinburg");
        addTimeZoneEntry("(GMT+04:30) Kabul", "Asia/Kabul");
        addTimeZoneEntry("(GMT+04:00) Baku, Tbilisi, Yerevan", "Asia/Baku");
        addTimeZoneEntry("(GMT+04:00) Abu Dhabi, Muscat", "Asia/Dubai");
        addTimeZoneEntry("(GMT+03:30) Tehran", "Asia/Tehran");
        addTimeZoneEntry("(GMT+03:00) Nairobi", "Africa/Nairobi");
        addTimeZoneEntry("(GMT+03:00) Moscow, St. Petersburg, Volgograd", "Europe/Moscow");
        addTimeZoneEntry("(GMT+03:00) Kuwait, Riyadh", "Asia/Kuwait");
        addTimeZoneEntry("(GMT+03:00) Baghdad", "Asia/Baghdad");
        addTimeZoneEntry("(GMT+02:00) Jerusalem", "Asia/Jerusalem");
        addTimeZoneEntry("(GMT+02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius", "Europe/Helsinki");
        addTimeZoneEntry("(GMT+02:00) Harare, Pretoria", "Africa/Harare");
        addTimeZoneEntry("(GMT+02:00) Cairo", "Africa/Cairo");
        addTimeZoneEntry("(GMT+02:00) Bucharest", "Europe/Bucharest");
        addTimeZoneEntry("(GMT+02:00) Athens, Istanbul, Minsk", "Europe/Athens");
        addTimeZoneEntry("(GMT+01:00) West Central Africa", "Africa/Lagos");
        addTimeZoneEntry("(GMT+01:00) Sarajevo, Skopje, Warsaw, Zagreb", "Europe/Warsaw");
        addTimeZoneEntry("(GMT+01:00) Brussels, Copenhagen, Madrid, Paris", "Europe/Brussels");
        addTimeZoneEntry("(GMT+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague", "Europe/Prague");
        addTimeZoneEntry("(GMT+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna", "Europe/Amsterdam");
        addTimeZoneEntry("(GMT) Casablanca, Monrovia", "Africa/Casablanca");
        addTimeZoneEntry("(GMT) Greenwich Mean Time : Dublin, Edinburgh, Lisbon, London", "Europe/Dublin");
        addTimeZoneEntry("(GMT-01:00) Azores", "Atlantic/Azores");
        addTimeZoneEntry("(GMT-01:00) Cape Verde Is.", "Atlantic/Cape_Verde");
        addTimeZoneEntry("(GMT-02:00) Mid-Atlantic", "Atlantic/South_Georgia");
        addTimeZoneEntry("(GMT-03:00) Brasilia", "America/Sao_Paulo");
        addTimeZoneEntry("(GMT-03:00) Buenos Aires, Georgetown", "America/Buenos_Aires");
        addTimeZoneEntry("(GMT-03:00) Greenland", "America/Thule");
        addTimeZoneEntry("(GMT-03:30) Newfoundland", "America/St_Johns");
        addTimeZoneEntry("(GMT-04:00) Atlantic Time (Canada)", "America/Halifax");
        addTimeZoneEntry("(GMT-04:00) Caracas, La Paz", "America/Caracas");
        addTimeZoneEntry("(GMT-04:00) Santiago", "America/Santiago");
        addTimeZoneEntry("(GMT-05:00) Bogota, Lima, Quito", "America/Bogota");
        addTimeZoneEntry("(GMT-05:00) Eastern Time (US & Canada)", " America/New_York");
        addTimeZoneEntry("(GMT-05:00) Indiana (East)", "America/Indianapolis");
        addTimeZoneEntry("(GMT-06:00) Central America", "America/Costa_Rica");
        addTimeZoneEntry("(GMT-06:00) Central Time (US & Canada)", "America/Chicago");
        addTimeZoneEntry("(GMT-06:00) Guadalajara, Mexico City, Monterrey", "America/Mexico_City");
        addTimeZoneEntry("(GMT-06:00) Saskatchewan", "America/Winnipeg");
        addTimeZoneEntry("(GMT-07:00) Arizona", "America/Phoenix");
        addTimeZoneEntry("(GMT-07:00) Chihuahua, La Paz, Mazatlan", "America/Tegucigalpa");
        addTimeZoneEntry("(GMT-07:00) Mountain Time (US & Canada)", "America/Denver");
        addTimeZoneEntry("(GMT-08:00) Pacific Time (US & Canada); Tijuana", "America/Los_Angeles");
        addTimeZoneEntry("(GMT-09:00) Alaska", "America/Anchorage");
        addTimeZoneEntry("(GMT-10:00) Hawaii", "Pacific/Honolulu");
        addTimeZoneEntry("(GMT-11:00) Midway Island, Samoa", "Pacific/Apia");
        addTimeZoneEntry("(GMT-12:00) International Date Line West", "MIT");

    }
    
    
    /**
     * Add Time Zone Entry
     * 
     * @param long_desc
     * @param keycode
     */
    public void addTimeZoneEntry(String long_desc, String keycode)
    {
        DeviceConfigurationDialog.time_zones.put(long_desc, keycode);
        DeviceConfigurationDialog.time_zones_vector.add(long_desc);
    }
    
    
    

    private void loadItemData()
    {
        if (this.current_entry == null)
            return;
        
        
        this.current_device = findDevice();
//        this.current_entry = this.data.get(en);
//        loadItemData();
        
        this.showDevice();
        
        this.tf_name.setText(this.current_entry.name);
        
        //if m_da.
        
        this.comm_port_comp.setCommunicationPort(this.current_entry.communication_port);
        
        
        if (!dcd.doesDeviceSupportTimeFix())
            return;
        
        this.cb_timezone.setSelectedItem(this.current_entry.ds_area_long);
        
        this.chb_fix.setSelected(this.current_entry.ds_fix);
        this.cb_summer_fix.setSelectedItem(getTimeChange(this.current_entry.ds_summer_change));
        this.cb_winter_fix.setSelectedItem(getTimeChange(this.current_entry.ds_winter_change));
    }

    private String getTimeChange(int change)
    {
        if (change==0)
            return "0";
        else if (change==-1)
            return "-1";
        else //if (change==+1)
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
        dce.communication_port = this.comm_port_comp.getCommunicationPort();
      
        if (!dcd.doesDeviceSupportTimeFix())
            return;
      
        dce.ds_area_long = (String)this.cb_timezone.getSelectedItem();
        dce.ds_area = DeviceConfigurationDialog.time_zones.get((String)this.cb_timezone.getSelectedItem());
        dce.ds_fix = this.chb_fix.isSelected();
        
        dce.ds_summer_change = getNumber((String)this.cb_summer_fix.getSelectedItem()); 
        dce.ds_winter_change = getNumber((String)this.cb_winter_fix.getSelectedItem()); 

        if (this.current_index_object.startsWith(m_ic.getMessage("NEW__")))
        {
            String en_num = this.current_index_object.substring(this.current_index_object.indexOf("[")+1, this.current_index_object.indexOf("]"));
            this.data.put(en_num, dce);
            
            resetComboBox();
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
    
    @SuppressWarnings("unchecked")
    private DeviceInterface findDevice()
    {
        if (this.current_entry==null)
            return null;
        
        Vector<SelectableInterface> vct = (Vector<SelectableInterface>)this.m_da.getDeviceConfigurationDefinition().getSupportedDevices();
        
        for(int i=0; i<vct.size(); i++)
        {
            SelectableInterface si = vct.get(i);
            if ((si.getColumnValue(1).equals(this.current_entry.device_company)) &&
                (si.getColumnValue(2).equals(this.current_entry.device_device)))
                return (DeviceInterface)si;
        }

        System.out.println("Device not found: " + this.current_entry.device_company + " " + this.current_entry.device_device);
        return null;
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
        
        if (this.current_index==this.cb_entry.getSelectedIndex())
            return;
        
        act = true;
        saveItemData();
        
        String en = (String)this.cb_entry.getSelectedItem();
        
        if (en.startsWith(m_ic.getMessage("NEW__")))
        {
            String en_num = en.substring(en.indexOf("[")+1, en.indexOf("]"));
            //System.out.println("NEW Entry");
            this.tf_name.setText("My meter" + " #" + en_num);
            resetEntry();
        }
        else
        {
            en = en.substring(0, en.indexOf(" "));
            this.current_entry = this.data.get(en);
            loadItemData();
        }
        
        
//        System.out.println("item changed: " + this.cb_entry.getSelectedItem());
        this.current_index=this.cb_entry.getSelectedIndex();
        this.current_index_object = (String)this.cb_entry.getSelectedItem();
        act = false;
    }
    
    
    
    
    // ****************************************************************
    // ******              HelpCapable Implementation             *****
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
        return this.m_da.getDeviceConfigurationDefinition().getHelpPrefix() + "Configuration";
    }
    
    
    
    
    
}
