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
 *  Filename: InfoPanel.java
 *  Purpose:  Container for all InfoPanels an the MainFrame. Responsible for
 *            managing the handling of its children. (fix)
 *
 *  Author:   andyrozman
 */

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// andyrozman

package ggc.gui.little;


import javax.swing.*;
import java.awt.*;
import java.util.Vector;
import ggc.gui.panels.info.AbstractInfoPanel;


public class MainLittlePanel extends JPanel
{
    private Vector vInfoPanels = new Vector();

    GGCLittle m_little = null;

    public DailyStatsControlsL control = null;
    public GeneralInfoPanelL   general = null;
    public DailyStatsPanelL    dailyStats = null;
    public ScheduleInfoPanelL  schedule = null;


    public MainLittlePanel(GGCLittle little)
    {
        m_little = little;
        setLayout(new GridLayout(2, 1));
        //setBackground(Color.white);

    	JPanel pane = new JPanel();
    	pane.setLayout(new GridLayout(1, 2));

    	pane.add(general = new GeneralInfoPanelL());
        
        JPanel control_app_panel = new JPanel();
        control_app_panel.setLayout(new GridLayout(2, 1));
        control_app_panel.add(control = new DailyStatsControlsL(this));
        control_app_panel.add(schedule = new ScheduleInfoPanelL()); //new DailyStatsControlsPanel(little));
        
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
            add((AbstractInfoPanel)vInfoPanels.get(i));
    }

    public void refreshPanels()
    {
        for (int i = 0; i < vInfoPanels.size(); i++)
            ((AbstractInfoPanel)vInfoPanels.get(i)).refreshInfo();
    }

    public void addPanelAt(int index, AbstractInfoPanel panel)
    {
        vInfoPanels.add(index, panel);
        removeAll();
        addPanels();
    }

    public void removePanelAt(int index)
    {
        vInfoPanels.remove(index);
        removeAll();
        addPanels();
    }

}