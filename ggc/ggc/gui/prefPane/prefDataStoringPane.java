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
 *  Filename: prefDataStoringPane.java
 *  Purpose:  Preferences Pane to choose the data storing method.
 *
 *  Author:   schultd
 */

package ggc.gui.prefPane;


import javax.swing.*;
import java.awt.*;


public class prefDataStoringPane extends AbstractPrefOptionsPanel
{
    private JComboBox comboDataSource;
    private JCheckBox chkAutoConnect;

    public prefDataStoringPane()
    {
        init();
    }

    public void init()
    {
        setLayout(new BorderLayout());
        Box a = Box.createHorizontalBox();
        a.add(new JLabel(m_ic.getMessage("DATA_SOURCE")+":"));

        String[] choices = {"MySQL", "Textfile", "HSQL" };

        comboDataSource = new JComboBox(choices);
        comboDataSource.setSelectedItem(props.getDataSource());
        comboDataSource.addItemListener(this);
        a.add(comboDataSource);

        chkAutoConnect = new JCheckBox(m_ic.getMessage("AUTOCONNECT_TO_DB_ON_STARTUP"), props.getAutoConnect());
        chkAutoConnect.addActionListener(this);

        Box b = Box.createVerticalBox();
        b.add(a);
        b.add(chkAutoConnect);

        add(b, BorderLayout.NORTH);
    }

    public void saveProps()
    {
        props.set("DataSource", comboDataSource.getSelectedItem().toString());
        props.set("AutoConnect", chkAutoConnect.isSelected());
    }
}
