package ggc.gui.panels.info;

import ggc.core.util.DataAccess;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import org.sun.swing.layout.SpringUtilities;

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
 *  Filename:     PlugInsInfoPanel  
 *  Description:  Panel for displaying PlugIns info
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class PlugInsInfoPanel extends AbstractInfoPanel
{

    private static final long serialVersionUID = 6475014158593005970L;
    private JLabel lblMeter;
    private JLabel lblPump;
    private JLabel lblCgms;
    private JLabel lblNutri;
    //private DataAccess m_da = DataAccess.getInstance();

    /**
     * Constructor
     */
    public PlugInsInfoPanel()
    {
        super("PLUGINS");
        init();
        refreshInfo();
    }

    private void init()
    {
        setLayout(new BorderLayout());

        JPanel lblPanel = new JPanel(new SpringLayout()); //new GridLayout(6, 2));
        lblPanel.setBackground(Color.white);
        
//        lblPanel.add(new JLabel(" ", JLabel.TRAILING));
//        lblPanel.add(new JLabel());

        lblPanel.add(new JLabel(m_ic.getMessage("NUTRITION_PLUGIN")+ ":", JLabel.LEADING));
        lblPanel.add(this.lblNutri = new JLabel("N/A"));
        
        lblPanel.add(new JLabel("", JLabel.TRAILING));
        lblPanel.add(new JLabel());
        
        lblPanel.add(new JLabel(m_ic.getMessage("METERS_PLUGIN") + ":", JLabel.LEADING));
        lblPanel.add(this.lblMeter = new JLabel("N/A"));

        lblPanel.add(new JLabel("" , JLabel.TRAILING));
        lblPanel.add(new JLabel());
        
        lblPanel.add(new JLabel(m_ic.getMessage("PUMPS_PLUGIN")+ ":", JLabel.LEADING));
        lblPanel.add(this.lblPump = new JLabel("N/A"));

        lblPanel.add(new JLabel("", JLabel.TRAILING));
        lblPanel.add(new JLabel());
        
        lblPanel.add(new JLabel(m_ic.getMessage("CGMS_PLUGIN")+ ":", JLabel.LEADING));
        lblPanel.add(this.lblCgms = new JLabel("N/A"));

        
        
        
/*        
        add(new JLabel(m_ic.getMessage("METERS_PLUGIN")+":"));
        add(lblMeter);
        add(new JLabel(m_ic.getMessage("PUMPS_PLUGIN")+":"));
        add(lblPumps);
        add(new JLabel(m_ic.getMessage("CGMS_PLUGIN")+":"));
        add(lblCGMS);
*/        
        
        
        SpringUtilities.makeCompactGrid(lblPanel,
            7, 2, //rows, cols
            10, 3,        //initX, initY  // 8
            40, 3);       //xPad, yPad    // 6
        
        
        add(lblPanel, BorderLayout.NORTH);
    }

    /**
     * Refresh Information 
     */
    @Override
    public void refreshInfo()
    {
        if (m_da.getPlugIn(DataAccess.PLUGIN_METERS)==null)
            return;
        
        lblMeter.setText(m_da.getPlugIn(DataAccess.PLUGIN_METERS).getShortStatus());
        lblPump.setText(m_da.getPlugIn(DataAccess.PLUGIN_PUMPS).getShortStatus());
        lblCgms.setText(m_da.getPlugIn(DataAccess.PLUGIN_CGMS).getShortStatus());
        lblNutri.setText(m_da.getPlugIn(DataAccess.PLUGIN_NUTRITION).getShortStatus());
        
//        lblMeter.setText(getDeviceInfo(m_da.getPlugIn(DataAccess.PLUGIN_METERS)));
//        lblPump.setText(getDeviceInfo(m_da.getPlugIn(DataAccess.PLUGIN_PUMPS)));
//        lblCgms.setText(getDeviceInfo(m_da.getPlugIn(DataAccess.PLUGIN_CGMS)));
        //m_da.getPlugIn(key)
    }
    
    
    /**
     * Get Tab Name
     * 
     * @return name as string
     */
    public String getTabName()
    {
        return "PlugInsInfo";
    }
    
    /**
     * Do Refresh - This method can do Refresh
     */
    public void doRefresh()
    {
        refreshInfo();
    }
    
    
    /**
     * Get Panel Id
     * 
     * @return id of panel
     */
    @Override
    public int getPanelId()
    {
        return InfoPanelsIds.INFO_PANEL_PLUGINS;
    }
    
    
}
