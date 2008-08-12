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

import ggc.meter.manager.MeterManager;
import ggc.meter.manager.company.MeterCompanyInterface;
import ggc.meter.util.DataAccessMeter;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import com.atech.i18n.I18nControlAbstract;


public class MeterCompanyPanel extends AbstractPreferencesPanel implements ItemListener
{



    static final long serialVersionUID = 0;
	
    JComboBox cb_meter_company; 
    
    //private JComboBox cb_meter_type, cb_meter_company, cb_timezone;
    //private JCheckBox chb_timezone_fix;
    //private JTextField tb_port;
    //private JButton b_select;

//x	    private JComboBox comboPortId;
    private JLabel    label;

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
    DataAccessMeter m_da = DataAccessMeter.getInstance();
    I18nControlAbstract m_ic = m_da.getI18nControlInstance();
    JLabel label_status;
    
    public MeterCompanyPanel(ConfigurationDialog parent)
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
        //setLayout(new BorderLayout());
        setLayout(null);

        label = new JLabel(m_ic.getMessage("METER_COMPANIES"));
        label.setBounds(0,30, 600, 40);
        label.setFont(m_da.getFont(DataAccessMeter.FONT_BIG_BOLD));
        label.setHorizontalAlignment(JLabel.CENTER);
        this.add(label, null);

        
        label = new JLabel(m_ic.getMessage("SELECT_METER_COMPANY"));
        label.setBounds(100,140, 300, 25);
        this.add(label, null);

        
        cb_meter_company = new JComboBox(MeterManager.getInstance().getCompanies());
        cb_meter_company.addItemListener(this);
        cb_meter_company.setBounds(100,180,220,25);
        this.add(cb_meter_company, null);


        
        label = new JLabel(m_ic.getMessage("STATUS") + ":");
        label.setBounds(100,250, 300, 25);
        label.setFont(m_da.getFont(DataAccessMeter.FONT_NORMAL_BOLD));
        this.add(label, null);
        
        label_status = new JLabel("<html>" + m_ic.getMessage("ABBOTT_DESC") + "</html>");
        label_status.setBounds(100,280, 400, 100);
        label_status.setFont(m_da.getFont(DataAccessMeter.FONT_NORMAL));
        label_status.setVerticalAlignment(JLabel.TOP);
        this.add(label_status, null);
        
        
        
        //this.cb_meter_type = new JComboBox(/*this.meters_array*/);

//cb_meter_type, cb_meter_company
/*
*/
        /*
        JPanel a = new JPanel(new GridLayout(5, 3));

        a.add(new JLabel(m_ic.getMessage("METER_COMPANY")+":"));
        a.add(cb_meter_company);
        a.add(new JLabel(""));
        */

        //a.add(new JLabel(m_ic.getMessage("GLUCOSE_METERS")+":"));
        //a.add(this.cb_meter_type);
        //a.add(new JLabel(""));




/*
        // picture
        JPanel pa = new JPanel(new GridLayout(1, 1));
        meterPicture = new JLabel(); //new JLabel(m_da.getMeterManager().getMeterImage(comboMeterType.getSelectedIndex()));

        pa.add(meterPicture);
        pa.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("METER_PICTURE")));

        a.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("METER_CONFIGURATION")));
        add(a, BorderLayout.NORTH);
        add(pa, BorderLayout.CENTER);
*/
        loadProps();
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
    	
    	MeterCompanyInterface mc = (MeterCompanyInterface)this.cb_meter_company.getSelectedItem();
    	//this.label_status.setText(m_ic.getMessage(mc.getDescription()));
    	
    	this.label_status.setText("<html>" + m_ic.getMessage(mc.getDescription()) + "</html>");
    	
    	
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

    
    /* 
     * saveProps
     */
    @Override
    public void saveProps()
    {
        // TODO Auto-generated method stub
        
    }
    
    

}
