package ggc.gui.little.panels;

import java.awt.*;

import javax.swing.*;

import ggc.gui.main.panels.AbstractInfoPanel;
import ggc.gui.main.panels.InfoPanelType;

/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     GeneralInfoPanelL  
 *  Description:  Panel for General Info
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class GeneralInfoPanelL extends AbstractInfoPanel
{

    private static final long serialVersionUID = 1872339281254813097L;
    private JLabel lblName = new JLabel();
    private JLabel lblIns1 = new JLabel();
    private JLabel lblIns2 = new JLabel();
    private JLabel lblUnit = new JLabel();


    /**
     * Constructor
     */
    public GeneralInfoPanelL()
    {
        super("GENERAL_INFORMATION");
        init();
        refreshInfo();
    }


    private void init()
    {
        setLayout(new GridLayout(0, 2));

        add(new JLabel(m_ic.getMessage("YOUR_NAME") + ":"));
        add(lblName);
        add(new JLabel(m_ic.getMessage("BOLUS_INSULIN") + ":"));
        add(lblIns1);
        add(new JLabel(m_ic.getMessage("BASAL_INSULIN") + ":"));
        add(lblIns2);
        add(new JLabel(m_ic.getMessage("BG_UNIT") + ":"));
        add(lblUnit);

    }


    @Override
    public InfoPanelType getPanelType()
    {

        return InfoPanelType.General;
    }


    /**
     * Do Refresh - This method can do Refresh
     */
    @Override
    public void doRefresh()
    {
        if (this.m_da.isDatabaseInitialized())
        {
            lblName.setText(configurationManagerWrapper.getUserName());
            lblIns1.setText(
                configurationManagerWrapper.getIns1Name() + "  (" + configurationManagerWrapper.getIns1Abbr() + ")");
            lblIns2.setText(
                configurationManagerWrapper.getIns2Name() + "  (" + configurationManagerWrapper.getIns2Abbr() + ")");
            lblUnit.setText(configurationManagerWrapper.getGlucoseUnit().getTranslation());
        }
    }

}
