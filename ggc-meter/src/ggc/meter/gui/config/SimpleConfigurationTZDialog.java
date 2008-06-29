/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: PropertiesDialog
 *
 *  Purpose:  Dialog for setting properties for application.
 *
 *  Author:   andyrozman {andy@atech-software.com}
 *
 */
package ggc.meter.gui.config;


import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.atech.utils.ATDataAccessAbstract;


public class SimpleConfigurationTZDialog extends JDialog implements ActionListener, ChangeListener //, HelpCapable
{

    private I18nControl m_ic = I18nControl.getInstance();        
    private ATDataAccessAbstract m_da; // = DataAccessMeter.getInstance();

    private JPanel prefPane;
    
    public static Vector<String> time_zones_vector = null;
    public static Hashtable<String,String> time_zones = null;
    JComboBox cb_timezone, cb_winter_fix, cb_summer_fix;
    JCheckBox chb_fix;

    public SimpleConfigurationTZDialog(JDialog parent, ATDataAccessAbstract da)
    {
        super(parent, "", true);
        
        m_da = da;
        
        m_da.addComponent(this);

        setSize(450,380);
        setTitle(m_ic.getMessage("TIMEZONE_PREFERENCES"));
        m_da.centerJDialog(this, m_da.getCurrentComponentParent());

        if (SimpleConfigurationTZDialog.time_zones_vector==null)
            this.loadTimeZones();
        
        init();
        this.setResizable(false);
        this.setVisible(true);
    }


    private void init()
    {
        this.setLayout(null);

        prefPane = new JPanel(null);
        prefPane.setBounds(0,0,450,400);
        
        Font normal = m_da.getFont(DataAccessMeter.FONT_NORMAL);
        
        JLabel label = new JLabel(m_ic.getMessage("TIMEZONE_PREFERENCES"));
        label.setBounds(0,20, 450, 30);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(m_da.getFont(DataAccessMeter.FONT_BIG_BOLD));
        prefPane.add(label);

        
        label = new JLabel(m_ic.getMessage("SELECT_TIMEZONE_LIST") + ":");
        label.setBounds(40,75, 450, 25);
        prefPane.add(label);
        
        
        cb_timezone = new JComboBox(SimpleConfigurationTZDialog.time_zones_vector);
        cb_timezone.setBounds(40, 100, 350, 25);
        prefPane.add(cb_timezone);
        
        chb_fix = new JCheckBox(m_ic.getMessage("NEED_DAYLIGHTSAVING_FIX"));
        chb_fix.setBounds(40, 140, 350, 25);
        chb_fix.addChangeListener(this);
        chb_fix.setFont(normal);
        chb_fix.setSelected(false);
        prefPane.add(chb_fix);
        
        label = new JLabel(m_ic.getMessage("WINTERTIME_FIX") + ":");
        label.setBounds(40,180, 200, 25);
        label.setFont(normal);
        prefPane.add(label);

        String[] changes = { "-1", "0", "+1" }; 
        
        this.cb_winter_fix = new JComboBox(changes);
        this.cb_winter_fix.setSelectedIndex(1);
        this.cb_winter_fix.setBounds(240, 180, 60, 25);
        this.cb_winter_fix.setFont(normal);
        prefPane.add(this.cb_winter_fix);
        
        
        label = new JLabel(m_ic.getMessage("SUMMERTIME_FIX") + ":");
        label.setBounds(40,220, 200, 25);
        label.setFont(normal);
        prefPane.add(label);
        
        this.cb_summer_fix = new JComboBox(changes);
        this.cb_summer_fix.setSelectedIndex(1);
        this.cb_summer_fix.setBounds(240, 220, 60, 25);
        this.cb_summer_fix.setFont(normal);
        prefPane.add(this.cb_summer_fix);
        
        
        //set the buttons up...
        JButton button = new JButton("  " + m_ic.getMessage("OK"));
        //okButton.setPreferredSize(dim);
        button.setIcon(m_da.getImageIcon_22x22("ok.png", this));
        button.setActionCommand("ok");
        button.setFont(m_da.getFont(DataAccessMeter.FONT_NORMAL));
        button.setBounds(120, 280, 110, 25);
        button.addActionListener(this);
        prefPane.add(button);

        button = new JButton("  " +m_ic.getMessage("CANCEL"));
        //cancelButton.setPreferredSize(dim);
        button.setIcon(m_da.getImageIcon_22x22("cancel.png", this));
        button.setActionCommand("cancel");
        button.setFont(m_da.getFont(DataAccessMeter.FONT_NORMAL));
        button.setBounds(250, 280, 110, 25);
        button.addActionListener(this);
        prefPane.add(button);

        enableDisableFix(false);
        
        getContentPane().add(prefPane, null);
    }


    private void enableDisableFix(boolean enable)
    {
        this.cb_summer_fix.setEnabled(enable);
        this.cb_winter_fix.setEnabled(enable);
    }
    
    

    // ---
    // ---  End
    // ---


    
    private boolean action_was = false;
    
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
            this.dispose();
        }
        else if (action.equals("cancel"))
        {
            action_was = false;
            this.dispose();
        }
        else
            System.out.println("PropertiesFrame: Unknown command: " + action);

    }

    
    
    public String getData()
    {
        StringBuilder sb = new StringBuilder();
        
        String tz = (String)this.cb_timezone.getSelectedItem();
        
        sb.append("METER_X_TIMEZONE=" +  SimpleConfigurationTZDialog.time_zones.get(tz) + "\n");
        
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



    
    /* 
     * stateChanged
     */
    public void stateChanged(ChangeEvent e)
    {
        enableDisableFix(this.chb_fix.isSelected());
    }

    
    
    Hashtable<String,String> timeZones;
    public void loadTimeZones()
    {
        SimpleConfigurationTZDialog.time_zones = new Hashtable<String,String>();
        SimpleConfigurationTZDialog.time_zones_vector = new Vector<String>();

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
    
    
    public void addTimeZoneEntry(String long_desc, String keycode)
    {
        SimpleConfigurationTZDialog.time_zones.put(long_desc, keycode);
        SimpleConfigurationTZDialog.time_zones_vector.add(long_desc);
    }
    
    
    
    
    
}
