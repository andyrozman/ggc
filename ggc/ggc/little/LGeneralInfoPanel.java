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
 *  Filename: LGeneralInfoPanel.java
 *  Purpose:  Shows general information about your Person. Like your name,
 *            Insulin used, your personal BG bounds, ... (fix)
 *
 *  Author:   andyrozman
 */

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// andyrozman


package ggc.little;

import ggc.util.I18nControl;
import ggc.util.GGCProperties;

import javax.swing.*;
import java.awt.*;


public class LGeneralInfoPanel extends JPanel //AbstractInfoPanel
{
    private JLabel lblName = new JLabel();
    private JLabel lblIns1 = new JLabel();
    private JLabel lblIns2 = new JLabel();
    GGCProperties props = GGCProperties.getInstance();
    private I18nControl m_ic = I18nControl.getInstance();

    public LGeneralInfoPanel()
    {
        super();

	setBorder(BorderFactory.createTitledBorder(I18nControl.getInstance().getMessage("GENERAL_INFORMATION")+":"));
	setOpaque(false);

	//I18nControl.getInstance().getMessage("GENERAL_INFORMATION")+":");
        init();
        refreshInfo();
    }

    public void init()
    {
        setLayout(new GridLayout(0, 2));

        add(new JLabel(m_ic.getMessage("YOUR_NAME")+":"));
        add(lblName);
        add(new JLabel(m_ic.getMessage("BOLUS_INSULIN")+":"));
        add(lblIns1);
        add(new JLabel(m_ic.getMessage("BASAL_INSULIN")+":"));
        add(lblIns2);
    }

    public void refreshInfo()
    {
        lblName.setText(props.getUserName());
        lblIns1.setText(props.getIns1Name());
        lblIns2.setText(props.getIns2Name());
    }
}
