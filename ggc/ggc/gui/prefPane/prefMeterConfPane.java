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

package ggc.gui.prefPane;


import java.awt.*;

import javax.swing.*;

import ggc.data.imports.SerialMeterImport;
import ggc.util.DataAccess;


public class prefMeterConfPane extends AbstractPrefOptionsPanel
{
    private JComboBox comboMeterType;
    private JComboBox comboPortId;
    private JLabel    meterPicture;

    private boolean error = false;

    private DataAccess m_da = DataAccess.getInstance();

    public prefMeterConfPane()
    {
        init();
    }

    public void init()
    {
        setLayout(new BorderLayout());

        //String[] choicesMeterType = {"GlucoCard"};
        comboMeterType = new JComboBox(m_da.getMeterManager().getAvailableMeters());
        comboMeterType.setSelectedItem(props.getMeterType());
        comboMeterType.addItemListener(this);

        try
        {
            comboPortId = new JComboBox(SerialMeterImport.getAvailableSerialPorts());
            comboPortId.setSelectedItem(props.getMeterPort());
            comboPortId.addItemListener(this);
        }
        catch(java.lang.NoClassDefFoundError ex)
        {
        }

        JPanel a = new JPanel(new GridLayout(2, 2));
        a.add(new JLabel(m_ic.getMessage("METER_TYPE")+":"));
        a.add(comboMeterType);

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

        // picture
        
        JPanel pa = new JPanel(new GridLayout(1, 1));
        meterPicture = new JLabel(m_da.getMeterManager().getMeterImage(comboMeterType.getSelectedIndex()));

//        System.out.println(m_da.getMeterManager().getMeterImage(comboMeterType.getSelectedIndex()));
//        System.out.println(meterPicture);

        pa.add(meterPicture);
        pa.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("METER_PICTURE")));

        a.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("METER_CONFIGURATION")));
        add(a, BorderLayout.NORTH);

        add(pa, BorderLayout.CENTER);

    }



    public void saveProps()
    {
        props.set("MeterType", comboMeterType.getSelectedItem().toString());
        if (comboPortId!=null)
            props.set("MeterPort", comboPortId.getSelectedItem().toString());

    }
}
