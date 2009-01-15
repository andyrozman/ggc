package ggc.gui.panels.info;

import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;

import java.awt.GridLayout;

import javax.swing.JLabel;

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
 *  Filename:     GeneralInfoPanel  
 *  Description:  Panel to display general info
 *
 *  Author: schultd
 *          andyrozman {andy@atech-software.com}  
 */


public class GeneralInfoPanel extends AbstractInfoPanel
{
    private static final long serialVersionUID = 6567668717839226425L;
    private JLabel lblName = new JLabel();
    private JLabel lblIns1 = new JLabel();
    private JLabel lblIns2 = new JLabel();
    private JLabel lblUnit = new JLabel();
    private JLabel lblTarget = new JLabel();
    //private JLabel lblNutri = new JLabel();
    //private JLabel lblMeter = new JLabel();
    //private JLabel lblPumps = new JLabel();
    //private JLabel lblCGMS = new JLabel();


    /**
     * Constructor
     */
    public GeneralInfoPanel()
    {
        super(I18nControl.getInstance().getMessage("GENERAL_INFORMATION")+":");
        init();
        refreshInfo();
    }

    private void init()
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
        add(new JLabel(m_ic.getMessage("BG_TARGET")+":"));
        add(lblTarget);
        //add(new JLabel(m_ic.getMessage("NUTRITION_PLUGIN")+":"));
        //add(lblNutri);
/*        add(new JLabel(m_ic.getMessage("METERS_PLUGIN")+":"));
        add(lblMeter);
        add(new JLabel(m_ic.getMessage("PUMPS_PLUGIN")+":"));
        add(lblPumps);
        add(new JLabel(m_ic.getMessage("CGMS_PLUGIN")+":"));
        add(lblCGMS);
*/
        //add(new JLabel());
        //add(new JLabel());

    }

    /**
     * Refresh Information 
     */
    @Override
    public void refreshInfo()
    {
        if (!this.m_da.isDatabaseInitialized())
            return;

        lblName.setText(m_da.getSettings().getUserName());
        lblIns1.setText(m_da.getSettings().getIns1Name() + "  (" + m_da.getSettings().getIns1Abbr() + ")");
        lblIns2.setText(m_da.getSettings().getIns2Name() + "  (" + m_da.getSettings().getIns2Abbr() + ")");
        lblUnit.setText(m_da.getSettings().getBG_unitString());
        
        int unit = m_da.getSettings().getBG_unit();

        float min,max;
        
        if (unit==1)
        {
            min = m_da.getSettings().getBG1_TargetLow();
            max = m_da.getSettings().getBG1_TargetHigh();
            
        }
        else
        {
            min = m_da.getSettings().getBG2_TargetLow();
            max = m_da.getSettings().getBG2_TargetHigh();
        }
        
        float avg = (float)((min+max)/2.0);
        
        //String s = m_da.getConfigurationManager().getFloatValue(key)
        
        lblTarget.setText(DataAccess.Decimal1Format.format(min) + " - " +
            DataAccess.Decimal1Format.format(max) + " [" + 
            DataAccess.Decimal1Format.format(avg) + "]");
            
        
//        m_da.getPlugIn(DataAccess.PLUGIN_METERS).getWhenWillBeImplemented();
//        lblNutri.setText("N/A"); //m_da.getPlugIn(DataAccess.PLUGIN_METERS).getShortStatus());
     /*   lblMeter.setText(m_da.getPlugIn(DataAccess.PLUGIN_METERS).getShortStatus());
        lblPumps.setText(m_da.getPlugIn(DataAccess.PLUGIN_PUMPS).getShortStatus());
        lblCGMS.setText(m_da.getPlugIn(DataAccess.PLUGIN_CGMS).getShortStatus()); */
    }


    /**
     * Get Tab Name
     * 
     * @return name as string
     */
    public String getTabName()
    {
        return "GeneralInfo";
    }
    
    
    /**
     * Do Refresh - This method can do Refresh
     */
    public void doRefresh()
    {
    }

    
    /**
     * Get Panel Id
     * 
     * @return id of panel
     */
    @Override
    public int getPanelId()
    {
        return InfoPanelsIds.INFO_PANEL_GENERAL;
    }
    
}
