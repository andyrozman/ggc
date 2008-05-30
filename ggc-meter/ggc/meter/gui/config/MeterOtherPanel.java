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
 *  Filename: prefMeterConfPane.java
 *  Purpose:  Preferences Pane to configure the meter.
 *
 *  Author:   schultd
 */

package ggc.meter.gui.config;


//import ggc.gui.dialogs.PortSelectionDialog;

import ggc.meter.util.DataAccess;
import ggc.meter.util.I18nControl;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class MeterOtherPanel extends AbstractPreferencesPanel implements ItemListener
{
	static final long serialVersionUID = 0;
	
    private JComboBox cb_meter_type, cb_meter_company, cb_timezone;
    private JCheckBox chb_timezone_fix;
    private JTextField tb_port;
    private JButton b_select;

//x	    private JComboBox comboPortId;
    private JLabel    meterPicture;

    //private JComboBox c

//x    private boolean error = false;
/*
    Hashtable<String,MeterCompanyH> meter_companies = null;
    Object[] meter_companies_array = null;

    Hashtable<String,MeterH> meters_full = null;
    Hashtable<String,MeterH> meters = null; */
    Object[] meters_array = null;

    JDialog parent = null;

/*
    public PrefMeterConfPane()
    {
        initData();
        init();
    }
*/
    DataAccess m_da = DataAccess.getInstance();
    I18nControl m_ic = m_da.getI18nInstance();
    
    public MeterOtherPanel(ConfigurationDialog parent)
    {
        super(parent);
        this.parent = parent;
        initData();
        init();
    }


    public void initData()
    {
    	
    	System.out.println("Commented PrefMeterConfPane::initData");
    	
    	/*
        ArrayList<MeterCompanyH> mcomp = m_da.getDb().meter_companies;

        System.out.println("Size: " + mcomp.size());

        ArrayList<String> comps = new ArrayList<String>();
        comps.add(m_ic.getMessage("SELECT"));

        meter_companies = new Hashtable<String,MeterCompanyH>();

        for (int i=0; i<mcomp.size(); i++)
        {
            MeterCompanyH mc = (MeterCompanyH)mcomp.get(i);

            comps.add(mc.getName());
            meter_companies.put(mc.getName(), mc);
        }

        meter_companies_array = comps.toArray();

        meters_array = new Object[1];
        meters_array[0] = m_ic.getMessage("SELECT");


        this.meters_full = m_da.getDb().meters_full;
        
		*/

    }


    public void init()
    {
        setLayout(new BorderLayout());

        cb_meter_company = new JComboBox(/*meter_companies_array */);
        cb_meter_company.addItemListener(this);


        this.cb_meter_type = new JComboBox(/*this.meters_array */);

//cb_meter_type, cb_meter_company
/*
        //String[] choicesMeterType = {"GlucoCard"};
        comboMeterType = new JComboBox(m_da.getMeterManager().getAvailableMeters());

    System.out.println("Meter selected: " + m_da.getSettings().getMeterType());

        comboMeterType.setSelectedIndex(m_da.getSettings().getMeterType());
        comboMeterType.addItemListener(this);

        //System.out.println(props.getMeterType());

        try
        {

        Vector<String> vct = SerialMeterImport.getAvailableSerialPorts();

        if (vct.size()==0)
        {
        vct.add(m_ic.getMessage("NO_COM_PORT_AVAILABLE"));
        comboPortId = new JComboBox(vct);
        }
        else
        {
        comboPortId = new JComboBox(SerialMeterImport.getAvailableSerialPorts());
        comboPortId.setSelectedItem(m_da.getSettings().getMeterPort());
        }

        comboPortId.addItemListener(this);

        }
        catch(java.lang.NoClassDefFoundError ex)
        {
            comboPortId = null;
        }
        catch(Exception ex)
        {
            comboPortId = null;
            System.out.println("Problem loading COMM API: " + ex);
        }
*/
        JPanel a = new JPanel(new GridLayout(5, 3));

        a.add(new JLabel(m_ic.getMessage("METER_COMPANY")+":"));
        a.add(cb_meter_company);
        a.add(new JLabel(""));

        a.add(new JLabel(m_ic.getMessage("GLUCOSE_METERS")+":"));
        a.add(this.cb_meter_type);
        a.add(new JLabel(""));

        a.add(new JLabel(m_ic.getMessage("SERIAL_PORTS")+":"));
        a.add(tb_port = new JTextField());
        //tb_port.setText(
        //tb_port.setEnabled(false);
        tb_port.setEditable(false);
        a.add(this.b_select = new JButton(m_ic.getMessage("CHANGE_PORT")));
        this.b_select.addActionListener(new ActionListener(){
                /**
                 * Invoked when an action occurs.
                 */
                public void actionPerformed(ActionEvent e)
                {
                    /*PortSelectionDialog psd =*/
                    //new PortSelectionDialog(parent);

                }
                });

/*
        if (comboPortId==null) 
        {
            a.add(new JLabel(""));
            a.add(new JLabel(m_ic.getMessage("PROBLEM_READING_AVAILABLE_PORTS")));
        }
        else
        {
            a.add(new JLabel(m_ic.getMessage("PORT_TO_USE")+":"));
            a.add(comboPortId);
        }
*/


        this.cb_timezone = new JComboBox(getTimeZoneDescs());

        // timezone fix
        a.add(new JLabel(""));
        a.add(this.chb_timezone_fix = new JCheckBox(m_ic.getMessage("DAYLIGHTSAVINGS_FIX")));
        this.chb_timezone_fix.setToolTipText(m_ic.getMessage("DAYLIGHTSAVINGS_FIX_DESC"));
        this.chb_timezone_fix.addActionListener(new ActionListener(){
                /**
                 * Invoked when an action occurs.
                 */
                public void actionPerformed(ActionEvent e)
                {
                    cb_timezone.setEnabled(chb_timezone_fix.isSelected());
                }
                });
        a.add(new JLabel(""));
        //a.addAncestorListener(
        
        a.add(new JLabel(m_ic.getMessage("TIMEZONE")));
        a.add(this.cb_timezone);
        a.add(new JLabel(""));


        // picture
        JPanel pa = new JPanel(new GridLayout(1, 1));
        meterPicture = new JLabel(); //new JLabel(m_da.getMeterManager().getMeterImage(comboMeterType.getSelectedIndex()));

        pa.add(meterPicture);
        pa.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("METER_PICTURE")));

        a.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("METER_CONFIGURATION")));
        add(a, BorderLayout.NORTH);
        add(pa, BorderLayout.CENTER);

        loadProps();
    }


    public Vector<String> getTimeZoneDescs()
    {
        Vector<String> vec = new Vector<String>();

        for(Enumeration<String> en = m_da.timeZones.keys(); en.hasMoreElements(); )
        {
            vec.add(en.nextElement());
        }

        Collections.sort(vec, new TimeZoneComparator());
        

        return vec;
    }


    public String getTimeZoneKeyFromValue(String value)
    {
        for(Enumeration<String> en = m_da.timeZones.keys(); en.hasMoreElements(); )
        {
            String key = en.nextElement();

            System.out.println(m_da.timeZones.get(key) + " = " + value);

            if (m_da.timeZones.get(key).contains(value))
            {
                return key;
            }
        }

        return "(GMT) Greenwich Mean Time : Dublin, Edinburgh, Lisbon, London";
    }


    private class TimeZoneComparator implements Comparator<String>
    {
     /**
       * Compare two TimeZones. 
       *
       *  GMT- (12->1) < GMT < GMT+ (1->12)
       *
       * @return +1 if less, 0 if same, -1 if greater
       */
       public final int compare ( String pFirst, String pSecond )
       {

           //System.out.println(pFirst + " " + pSecond);

           if (areSameType(pFirst, pSecond))
           {
               return ((pFirst.compareTo(pSecond)) * typeChanger(pFirst, pSecond));
           }
           else
           {
               if ((pFirst.startsWith("(GMT-"))) // && (second.contains("(GMT)")))
               {
                   return -1;
               }
               else if ((pFirst.startsWith("(GMT)"))) // && (second.contains("(GMT)")))
               {
                   if (pSecond.startsWith("(GMT-"))
                       return 1;
                   else
                       return -1;
               }
               else
               {
                   return 1;
               }

           }
       } // end compare

       public int typeChanger(String first, String second)
       {
           if ((first.startsWith("(GMT-")) && (second.startsWith("(GMT-")))
               return -1;
           else
               return 1;
       }
       

       public boolean areSameType(String first, String second)
       {
           if (((first.startsWith("(GMT+")) && (second.startsWith("(GMT+"))) ||
               ((first.startsWith("(GMT)")) && (second.startsWith("(GMT)"))) ||
               ((first.startsWith("(GMT-")) && (second.startsWith("(GMT-"))))
           {
               return true;
           }
           else
               return false;

       }


    }








    public void loadProps()
    {
    	
    	System.out.println("Commented: loadProps");
    	
    	/*
        tb_port.setText(settings.getMeterPort());

        this.chb_timezone_fix.setSelected(settings.getMeterDaylightSavingsFix());

        String tz = settings.getTimeZone();

        if (tz.equals(""))
        {
            // set default
            //TimeZone tzobj = new TimeZone();
            cb_timezone.setSelectedItem(getTimeZoneKeyFromValue(TimeZone.getDefault().getID()));

            //System.out.println("Default TZ: " + TimeZone.getDefault().getID());
            //System.out.println("Default TZ: " + TimeZone.getDefault().toString());

            this.cb_timezone.setEnabled(false);

        }
        else
        {
            cb_timezone.setSelectedItem(tz);
        }

        // meter companies
        // meters

        int meter_id = settings.getMeterType();

        if (meter_id>0)
        {
            MeterH met = this.m_da.getDb().getMeterById(meter_id);

            //this.cb_meter_type.setSelectedIndex(getMeterArrayIndex(met.getName()));
            MeterCompanyH mc = this.m_da.getDb().getMeterCompanyById((int)met.getCompany_id());

            this.cb_meter_company.setSelectedItem(mc.getName());
            this.cb_meter_type.setSelectedItem(met.getName());

        }
*/

/*
        Hashtable<String,MeterCompanyH> meter_companies = null;
        Object[] meter_companies_array = null;

        Hashtable<String,MeterH> meters = null;
        Object[] meters_array = null;
*/

    }

    public int getMeterArrayIndex(String name)
    {
        for(int i=0; i<this.meters_array.length; i++)
        {
            if (this.meters_array[i].equals(name))
            {
                return i;
            }
        }

        return 0;
    }



    //@Override
    public void itemStateChanged(ItemEvent e)
    {
    	
    	System.out.println("Commented PrefMeterConfPane::itemState changed");
    	
    	/*
        if (this.cb_meter_company.getSelectedIndex()!=0)
        {

            System.out.println("Meter Company: " + this.cb_meter_company.getSelectedIndex());

            String cmp_name = (String)this.meter_companies_array[this.cb_meter_company.getSelectedIndex()];
            MeterCompanyH mc = this.meter_companies.get(cmp_name);

            long id = mc.getId();

            ArrayList<MeterH> mtrs = this.m_da.getDb().meters_by_cmp.get("" +id);

            System.out.println("Meters: " + mtrs.size());


            Hashtable<String,MeterH> mtrs_out = new Hashtable<String,MeterH>();
            ArrayList<String> mtrs_str_out = new ArrayList<String>();

            mtrs_str_out.add(m_ic.getMessage("SELECT"));

            for (int i=0; i<mtrs.size(); i++)
            {
                MeterH mh = (MeterH)mtrs.get(i);

                mtrs_str_out.add(mh.getName());
                mtrs_out.put(mh.getName(), mh);
            }

            this.meters = mtrs_out;
            this.meters_array = mtrs_str_out.toArray();

            this.cb_meter_type.removeAllItems();

            for (int j=0; j<this.meters_array.length; j++)
            {
                this.cb_meter_type.addItem(this.meters_array[j]);
            }

        }
*/

        /*
            changed = true;
            meterPicture.setIcon(m_da.getMeterManager().getMeterImage(comboMeterType.getSelectedIndex()));
        */
    }


    //@Override
    public void saveProps()
    {

    	System.out.println("Commented: saveProps");
    	
    	/*
        settings.setMeterDaylightSavingsFix(this.chb_timezone_fix.isSelected());

        if (this.chb_timezone_fix.isSelected())
        {
            settings.setTimeZone((String)cb_timezone.getSelectedItem());
        }
        else
        {
            settings.setTimeZone("");
        }

        // meter companies
        // meters

        if (this.cb_meter_type.getSelectedIndex()>0)
        {
            String name = (String)this.cb_meter_type.getSelectedItem();

            MeterH met = this.meters.get(name);

            settings.setMeterType((int)met.getId());
        }
        else
            settings.setMeterType(0);

*/
    	
    	
    	
        /*  ddd
            settings.setMeterType(comboMeterType.getSelectedIndex());
    
        System.out.println("Meter index: " + comboMeterType.getSelectedIndex());
    
            if (comboPortId!=null)
        {
            if (comboPortId.getSelectedItem()==null)
    
            settings.setMeterPort(comboPortId.getSelectedItem().toString());
        }
        */

    }
}
