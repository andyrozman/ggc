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
 *  Filename: GeneralInfoPanel.java
 *  Purpose:  Shows general information about your Person. Like your name,
 *            Insulin used, your personal BG bounds, ...
 *
 *  Author:   schultd
 */

package gui.infoPanel;


import javax.swing.*;
import java.awt.*;


public class GeneralInfoPanel extends AbstractInfoPanel
{
    private JLabel lblName = new JLabel();
    private JLabel lblIns1 = new JLabel();
    private JLabel lblIns2 = new JLabel();

    public GeneralInfoPanel()
    {
        super("General Information:");
        init();
        refreshInfo();
    }

    public void init()
    {
        setLayout(new GridLayout(0, 2));

        add(new JLabel("Your Name:"));
        add(lblName);
        add(new JLabel("Basal Insulin:"));
        add(lblIns1);
        add(new JLabel("Bogus Insulin:"));
        add(lblIns2);
    }

    public void refreshInfo()
    {
        lblName.setText(props.getUserName());
        lblIns1.setText(props.getIns1Name());
        lblIns2.setText(props.getIns2Name());
    }
}
