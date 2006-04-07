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


import ggc.data.imports.SerialMeterImport;

import javax.swing.*;
import java.awt.*;


public class prefMeterConfPane extends AbstractPrefOptionsPanel
{
    private JComboBox comboMeterType;
    private JComboBox comboPortId;

    private boolean error = false;

    public prefMeterConfPane()
    {
        init();
    }

    public void init()
    {
        setLayout(new BorderLayout());

        try
        {
            //String[] choicesMeterType = {"GlucoCard"};
            comboMeterType = new JComboBox(SerialMeterImport.getAvailableMeters());
            comboPortId = new JComboBox(SerialMeterImport.getAvailableSerialPorts());

            comboMeterType.setSelectedItem(props.getMeterType());
            comboPortId.setSelectedItem(props.getMeterPort());
            comboMeterType.addItemListener(this);
            comboPortId.addItemListener(this);

            JPanel a = new JPanel(new GridLayout(2, 2));
            a.add(new JLabel(m_ic.getMessage("METER_TYPE")+":"));
            a.add(comboMeterType);
            a.add(new JLabel(m_ic.getMessage("PORT_TO_USE")+":"));
            a.add(comboPortId);

            a.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("METER_CONFIGURATION")));

            add(a, BorderLayout.NORTH);

        }
        catch(Exception ex)
        {
            setError();
        }
        catch(java.lang.NoClassDefFoundError ex)
        {
            setError();
        }

    }

    public void setError()
    {
        System.out.println("EXXXXXXXXXXXXXX");
        error = true;
        JPanel a = new JPanel(new GridLayout(2, 2));
        a.add(new JLabel(m_ic.getMessage("NO_COM_CLASSES_AVAILABLE")));
        add(a, BorderLayout.NORTH);
    }


    public void saveProps()
    {
        if (!error) 
        {
            props.set("MeterType", comboMeterType.getSelectedItem().toString());
            props.set("MeterPort", comboPortId.getSelectedItem().toString());
        }
    }
}
