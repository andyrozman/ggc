package ggc.gui.main.panels;

import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.*;

import com.atech.misc.refresh.EventObserverInterface;

import ggc.core.util.DataAccess;
import ggc.core.util.RefreshInfo;

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

public class MainWindowInfoPanel extends JPanel implements EventObserverInterface
{

    private static final long serialVersionUID = -8288632669830259690L;
    private ArrayList<AbstractInfoPanel> vInfoPanels = new ArrayList<AbstractInfoPanel>();
    DataAccess m_da = DataAccess.getInstance();


    /**
     * Constructor
     */
    public MainWindowInfoPanel()
    {
        setLayout(new GridLayout(0, 2));
        setBackground(Color.white);

        vInfoPanels.add(new SplittedInfoPanel(new GeneralInfoPanel(), new HbA1cInfoPanel()));
        vInfoPanels.add(new SplittedInfoPanel(new PlugInsInfoPanel(), new DeviceInfoPanel()));
        vInfoPanels.add(new SplittedInfoPanel(new ScheduleInfoPanel(), new StocksInfoPanel()));
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
    * RefreshInfo - Refresh info by id
    *
    * @param mask
    */
    public void refreshPanels(InfoPanelType... mask)
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
        // TODO groups as enums if possible

        if (type == RefreshInfo.PANEL_GROUP_PLUGINS_DEVICES)
        {
            this.refreshPanels(InfoPanelType.ConfiguredDevices);
        }
        else if (type == RefreshInfo.PANEL_GROUP_ALL_DATA)
        {
            this.refreshPanels(InfoPanelType.HbA1c, InfoPanelType.Statistics);
        }
        else if (type == RefreshInfo.PANEL_GROUP_GENERAL_INFO)
        {
            this.refreshPanels(InfoPanelType.General);
        }
        else if (type == RefreshInfo.PANEL_GROUP_PLUGINS_ALL)
        {
            this.refreshPanels(InfoPanelType.Plugins, InfoPanelType.ConfiguredDevices);
        }
    }


    /**
     * Update (From Observable)
     */
    public void update(Observable obj, Object arg)
    {
        if (arg instanceof Integer)
        {
            Integer i = (Integer) arg;
            this.refreshGroup(i.intValue());
        }
    }

}
