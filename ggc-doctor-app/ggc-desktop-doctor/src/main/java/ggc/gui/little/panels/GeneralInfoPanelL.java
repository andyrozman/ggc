package ggc.gui.little.panels;

import ggc.gui.panels.info.AbstractInfoPanel;
import ggc.gui.panels.info.InfoPanelsIds;

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

    //private JLabel lblMeter = new JLabel();
    private JLabel lblUnit = new JLabel();
    //GGCProperties props = GGCProperties.getInstance();
    //private I18nControl m_ic = I18nControl.getInstance();
    //DataAccess m_da = DataAccess.getInstance();


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

        add(new JLabel(m_ic.getMessage("YOUR_NAME")+":"));
        add(lblName);
        add(new JLabel(m_ic.getMessage("BOLUS_INSULIN")+":"));
        add(lblIns1);
        add(new JLabel(m_ic.getMessage("BASAL_INSULIN")+":"));
        add(lblIns2);
        //add(new JLabel(m_ic.getMessage("GLUCOMETER")+":"));
        //add(lblMeter);
        add(new JLabel(m_ic.getMessage("BG_UNIT")+":"));
        add(lblUnit);
        
    }

    /**
     * Refresh Information 
     */
    @Override
    public void refreshInfo()
    {
	if (this.m_da.isDatabaseInitialized())
	{
	    lblName.setText(m_da.getSettings().getUserName());
	    lblIns1.setText(m_da.getSettings().getIns1Name() + "  (" + m_da.getSettings().getIns1Abbr() + ")");
	    lblIns2.setText(m_da.getSettings().getIns2Name() + "  (" + m_da.getSettings().getIns2Abbr() + ")");
//	    lblMeter.setText(m_da.getSettings().getMeterTypeString() + "  (" + m_da.getSettings().getMeterPort() + ")");
	    lblUnit.setText(m_da.getSettings().getBG_unitString());
	}
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
        return InfoPanelsIds.INFO_PANEL_NONE;
    }
    
    
}
