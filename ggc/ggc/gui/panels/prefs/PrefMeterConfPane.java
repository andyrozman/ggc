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

package ggc.gui.panels.prefs;


import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.*;

import ggc.data.imports.SerialMeterImport;
import ggc.db.hibernate.MeterCompanyH;
import ggc.db.hibernate.MeterH;
import ggc.util.DataAccess;


public class PrefMeterConfPane extends AbstractPrefOptionsPanel
{
    private JComboBox cb_meter_type, cb_meter_company;
    private JTextField tb_port;
    private JButton b_select;

    private JComboBox comboPortId;
    private JLabel    meterPicture;

    private boolean error = false;

    Hashtable<String,MeterCompanyH> meter_companies = null;
    Object[] meter_companies_array = null;

    Hashtable<String,MeterH> meters = null;
    Object[] meters_array = null;


    public PrefMeterConfPane()
    {
	initData();
        init();
    }


    public void initData()
    {
	ArrayList<MeterCompanyH> mcomp = m_da.getDb().meter_companies;

	System.out.println("Size: " + mcomp.size());

	ArrayList<String> comps = new ArrayList<String>();
	comps.add(m_ic.getMessage("SELECT"));

	meter_companies = new Hashtable<String,MeterCompanyH>();

	for(int i=0; i<mcomp.size(); i++)
	{
	    MeterCompanyH mc = (MeterCompanyH)mcomp.get(i);

	    comps.add(mc.getName());
	    meter_companies.put(mc.getName(), mc);
	}

	meter_companies_array = comps.toArray();

	meters_array = new Object[1];
	meters_array[0] = m_ic.getMessage("SELECT");

    }


    public void init()
    {
        setLayout(new BorderLayout());

	cb_meter_company = new JComboBox(meter_companies_array);
	cb_meter_company.addItemListener(this);


	this.cb_meter_type = new JComboBox(this.meters_array);

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
        JPanel a = new JPanel(new GridLayout(3, 3));

        a.add(new JLabel(m_ic.getMessage("METER_COMPANY")+":"));
        a.add(cb_meter_company);
	a.add(new JLabel(""));

	a.add(new JLabel(m_ic.getMessage("GLUCOSE_METERS")+":"));
	a.add(this.cb_meter_type);
	a.add(new JLabel(""));

	a.add(new JLabel(m_ic.getMessage("SERIAL_PORTS")+":"));
	a.add(tb_port = new JTextField());
	a.add(this.b_select = new JButton(m_ic.getMessage("CHANGE_PORT")));

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
        // picture
        JPanel pa = new JPanel(new GridLayout(1, 1));
        meterPicture = new JLabel(); //new JLabel(m_da.getMeterManager().getMeterImage(comboMeterType.getSelectedIndex()));

        pa.add(meterPicture);
        pa.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("METER_PICTURE")));

        a.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("METER_CONFIGURATION")));
        add(a, BorderLayout.NORTH);
        add(pa, BorderLayout.CENTER);

    }


    @Override
    public void itemStateChanged(ItemEvent e)
    {
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

	    for(int i=0; i<mtrs.size(); i++)
	    {
		MeterH mh = (MeterH)mtrs.get(i);

		mtrs_str_out.add(mh.getName());
		mtrs_out.put(mh.getName(), mh);
	    }

	    this.meters = mtrs_out;
	    this.meters_array = mtrs_str_out.toArray();

	    this.cb_meter_type.removeAllItems();

	    for(int j=0; j<this.meters_array.length; j++)
	    {
		this.cb_meter_type.addItem(this.meters_array[j]);
	    }

	}


	/*
        changed = true;
        meterPicture.setIcon(m_da.getMeterManager().getMeterImage(comboMeterType.getSelectedIndex()));
	*/
    }


    @Override
    public void saveProps()
    {
	/*
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
