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

package gui.prefPane;


import meter.SerialMeterReader;

import javax.swing.*;
import java.awt.*;


public class prefMeterConfPane extends AbstractPrefOptionsPanel
{
    private JComboBox comboMeterType;
    private JComboBox comboPortId;

    public prefMeterConfPane()
    {
        init();
    }

    public void init()
    {
        setLayout(new BorderLayout());

        String[] choicesMeterType = {"GlucoCard"};
        comboMeterType = new JComboBox(choicesMeterType);
        comboPortId = new JComboBox(SerialMeterReader.getAvaibleSerialPorts());

        comboMeterType.setSelectedItem(props.getMeterType());
        comboPortId.setSelectedItem(props.getMeterPort());
        comboMeterType.addItemListener(this);
        comboPortId.addItemListener(this);

        JPanel a = new JPanel(new GridLayout(2,2));
        a.add(new JLabel("Meter type:"));
        a.add(comboMeterType);
        a.add(new JLabel("Port to use:"));
        a.add(comboPortId);

        a.setBorder(BorderFactory.createTitledBorder("Meter Configuration"));

        add(a, BorderLayout.NORTH);
    }

    public void saveProps()
    {
        props.set("MeterType", comboMeterType.getSelectedItem().toString());
        props.set("MeterPort", comboPortId.getSelectedItem().toString());
    }
}
