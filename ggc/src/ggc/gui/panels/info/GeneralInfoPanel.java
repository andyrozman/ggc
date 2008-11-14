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

package ggc.gui.panels.info;

import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;

import javax.swing.*;
import java.awt.*;


public class GeneralInfoPanel extends AbstractInfoPanel
{
    /**
     * 
     */
    private static final long serialVersionUID = 6567668717839226425L;
    private JLabel lblName = new JLabel();
    private JLabel lblIns1 = new JLabel();
    private JLabel lblIns2 = new JLabel();
    private JLabel lblUnit = new JLabel();
    private JLabel lblNutri = new JLabel();
    private JLabel lblMeter = new JLabel();
    private JLabel lblPumps = new JLabel();
    private JLabel lblCGMS = new JLabel();


    public GeneralInfoPanel()
    {
        super(I18nControl.getInstance().getMessage("GENERAL_INFORMATION")+":");
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
        add(new JLabel(m_ic.getMessage("BG_UNIT")+":"));
        add(lblUnit);
        //add(new JLabel(m_ic.getMessage("NUTRITION_PLUGIN")+":"));
        //add(lblNutri);
        add(new JLabel(m_ic.getMessage("METERS_PLUGIN")+":"));
        add(lblMeter);
        add(new JLabel(m_ic.getMessage("PUMPS_PLUGIN")+":"));
        add(lblPumps);
        add(new JLabel(m_ic.getMessage("CGMS_PLUGIN")+":"));
        add(lblCGMS);

        add(new JLabel());
        add(new JLabel());

    }

    @Override
    public void refreshInfo()
    {
        if (!this.m_da.isDatabaseInitialized())
            return;

        lblName.setText(m_da.getSettings().getUserName());
        lblIns1.setText(m_da.getSettings().getIns1Name() + "  (" + m_da.getSettings().getIns1Abbr() + ")");
        lblIns2.setText(m_da.getSettings().getIns2Name() + "  (" + m_da.getSettings().getIns2Abbr() + ")");
        lblUnit.setText(m_da.getSettings().getBG_unitString());
        
        m_da.getPlugIn(DataAccess.PLUGIN_METERS).getWhenWillBeImplemented();
        lblNutri.setText("N/A"); //m_da.getPlugIn(DataAccess.PLUGIN_METERS).getShortStatus());
        lblMeter.setText(m_da.getPlugIn(DataAccess.PLUGIN_METERS).getShortStatus());
        lblPumps.setText(m_da.getPlugIn(DataAccess.PLUGIN_PUMPS).getShortStatus());
        lblCGMS.setText(m_da.getPlugIn(DataAccess.PLUGIN_CGMS).getShortStatus());

        /*
        int meter_type = m_da.getSettings().getMeterType();
        if (meter_type>0)
        {
            lblMeter.setText(m_da.getDb().getMeterById(meter_type).getName() + "  (" + m_da.getSettings().getMeterPort() + ")");
        }
        else
        {
            lblMeter.setText(m_ic.getMessage("NO_METER_SELECTED"));
        }
*/
    }

/*
        <property name="meter_type"  type="int" />
        <property name="meter_port"  type="string" length="50" />
        <property name="bg_unit"  type="int" />  <!-- 1= mmol/l, 2=mg/dl -->
*/

}
