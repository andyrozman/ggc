package ggc.gui.little.panels;


import ggc.core.util.I18nControl;
import ggc.gui.little.GGCLittle;
import ggc.gui.panels.info.AbstractInfoPanel;

import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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
 *  Filename:     MainLittlePanel  
 *  Description:  Main Little Panel
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class MainLittlePanel extends JPanel
{
    private static final long serialVersionUID = 8210688971878569493L;

    private Vector<JPanel> vInfoPanels = new Vector<JPanel>();

    GGCLittle m_little = null;

    private GeneralInfoPanelL   general = null;
    
    /**
     * Daily Stats Panel
     */
    public DailyStatsPanelL    dailyStats = null;

    // tabs
    private DailyStatsControlsL control = null;
    @SuppressWarnings("unused")
    private PlugInPanelL plug_in = null;
    @SuppressWarnings("unused")
    private StocksInfoPanelL stocks = null;
    private ScheduleInfoPanelL  schedule = null;
    
    
    
    private JTabbedPane tabbedPane = null;
    private I18nControl ic = I18nControl.getInstance();


    /**
     * Constructor
     * 
     * @param little
     */
    public MainLittlePanel(GGCLittle little)
    {
        m_little = little;
        setLayout(new GridLayout(2, 1));
        //setBackground(Color.white);

    	JPanel pane = new JPanel();
    	pane.setLayout(new GridLayout(1, 2));

    	pane.add(general = new GeneralInfoPanelL());
        
        JPanel control_app_panel = new JPanel();
        control_app_panel.setLayout(new GridLayout(1, 1));
        control_app_panel.add(this.tabbedPane = new JTabbedPane());

        this.tabbedPane.addTab(ic.getMessage("TAB_CONTROL"), control = new DailyStatsControlsL(this));
        this.tabbedPane.addTab(ic.getMessage("TAB_PLUGIN"), plug_in = new PlugInPanelL());
        this.tabbedPane.addTab(ic.getMessage("TAB_SCHEDULE"), schedule = new ScheduleInfoPanelL());
        this.tabbedPane.addTab(ic.getMessage("TAB_STOCKS"), stocks = new StocksInfoPanelL());
        

        
        //public PlugInPanelL plug_in = null;
//        public StocksInfoPanelL stocks = null;
//        public ScheduleInfoPanelL  schedule = null;
        
        //	control = new DailyStatsControlsL(this));
        
        
        
        /*
        control_app_panel.setLayout(new GridLayout(2, 1));
        control_app_panel.add(control = new DailyStatsControlsL(this));
        control_app_panel.add(schedule = new ScheduleInfoPanelL()); //new DailyStatsControlsPanel(little));
        */
    	pane.add(control_app_panel);

        add(pane);
        add(dailyStats = new DailyStatsPanelL());

        this.vInfoPanels.add(general);
        this.vInfoPanels.add(control);
        this.vInfoPanels.add(schedule);
        this.vInfoPanels.add(dailyStats);

    }

    
    private void addPanels()
    {
        for (int i = 0; i < vInfoPanels.size(); i++)
            add(vInfoPanels.get(i));
    }

    /**
     * Refresh Panels
     */
    public void refreshPanels()
    {
        for (int i = 0; i < vInfoPanels.size(); i++)
            ((AbstractInfoPanel)vInfoPanels.get(i)).refreshInfo();
    }

    /**
     * Add Panel At
     * @param index index of panel
     * @param panel
     */
    public void addPanelAt(int index, AbstractInfoPanel panel)
    {
        vInfoPanels.add(index, panel);
        removeAll();
        addPanels();
    }

    /**
     * Remove Panel At
     * 
     * @param index index of panel
     */
    public void removePanelAt(int index)
    {
        vInfoPanels.remove(index);
        removeAll();
        addPanels();
    }

}
