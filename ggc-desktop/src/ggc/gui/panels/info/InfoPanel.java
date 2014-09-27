package ggc.gui.panels.info;

import ggc.core.util.DataAccess;
import ggc.core.util.RefreshInfo;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JPanel;

import com.atech.misc.refresh.EventObserverInterface;

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
 *  Filename:     InfoPanel  
 *  Description:  Main Collection class for all AbstractInfoPanels
 *
 *  Author: schultd
 *          andyrozman {andy@atech-software.com}  
 */

public class InfoPanel extends JPanel implements EventObserverInterface
{
    private static final long serialVersionUID = -8288632669830259690L;
    private ArrayList<AbstractInfoPanel> vInfoPanels = new ArrayList<AbstractInfoPanel>();
    DataAccess m_da = DataAccess.getInstance();

    /**
     * Constructor
     */
    public InfoPanel()
    {
        setLayout(new GridLayout(0, 2));
        setBackground(Color.white);

        vInfoPanels.add(new OtherInfoPanel(new GeneralInfoPanel(), new HbA1cInfoPanel()));
        vInfoPanels.add(new OtherInfoPanel(new PlugInsInfoPanel(), new DeviceInfoPanel()));

        // vInfoPanels.add(new GeneralInfoPanel());
        // vInfoPanels.add(new OtherInfoPanel(new HbA1cInfoPanel(), new
        // DeviceInfoPanel()));
        // vInfoPanels.add(new HbA1cInfoPanel());
        vInfoPanels.add(new OtherInfoPanel(new ScheduleInfoPanel(), new StocksInfoPanel()));
        vInfoPanels.add(new StatisticsInfoPanel());

        addPanels();

        m_da.addObserver(DataAccess.OBSERVABLE_PANELS, this);
    }

    private void addPanels()
    {
        for (int i = 0; i < vInfoPanels.size(); i++)
        {
            add(vInfoPanels.get(i));
        }
    }

    /**
     * Invalidate Panel Constants
     */
    public void invalidatePanelsConstants()
    {
        for (int i = 0; i < vInfoPanels.size(); i++)
        {
            vInfoPanels.get(i).invalidateFirstRefresh();
        }
    }

    /**
     * Refresh Panel
     */
    public void refreshPanels()
    {
        for (int i = 0; i < vInfoPanels.size(); i++)
        {
            vInfoPanels.get(i).refreshInfo();
        }
    }

    /**
     * RefreshInfo - Refresh info by name 
     *  
     * @param name
     */
    public void refreshPanels(String name)
    {
        for (int i = 0; i < vInfoPanels.size(); i++)
        {
            vInfoPanels.get(i).refreshInfo(name);
        }

    }

    /**
     * RefreshInfo - Refresh info by id 
     *  
     * @param mask
     */
    public void refreshPanels(int mask)
    {
        for (int i = 0; i < vInfoPanels.size(); i++)
        {
            vInfoPanels.get(i).refreshInfo(mask);
        }
    }

    /**
     * Refresh Group
     * 
     * @param type
     */
    public void refreshGroup(int type)
    {
        if (type == RefreshInfo.PANEL_GROUP_PLUGINS_DEVICES)
        {
            this.refreshPanels(InfoPanelsIds.INFO_PANEL_PLUGIN_DEVICES);
        }
        else if (type == RefreshInfo.PANEL_GROUP_ALL_DATA)
        {
            this.refreshPanels(InfoPanelsIds.INFO_PANEL_HBA1C | InfoPanelsIds.INFO_PANEL_STATISTICS);
        }
        else if (type == RefreshInfo.PANEL_GROUP_GENERAL_INFO)
        {
            this.refreshPanels(InfoPanelsIds.INFO_PANEL_GENERAL);
        }
        else if (type == RefreshInfo.PANEL_GROUP_PLUGINS_ALL)
        {
            this.refreshPanels(InfoPanelsIds.INFO_PANEL_PLUGINS);
            this.refreshPanels(InfoPanelsIds.INFO_PANEL_PLUGIN_DEVICES);
        }
    }

    /**
     * Update (From Ovservable)
     */
    public void update(Observable obj, Object arg)
    {
        if (arg instanceof Integer)
        {
            Integer i = (Integer) arg;
            this.refreshGroup(i.intValue());
        }
    }

    /*
     * public void addPanelAt(int index, AbstractInfoPanel panel)
     * {
     * vInfoPanels.add(index, panel);
     * removeAll();
     * addPanels();
     * }
     * public void removePanelAt(int index)
     * {
     * vInfoPanels.remove(index);
     * removeAll();
     * addPanels();
     * }
     */
}
