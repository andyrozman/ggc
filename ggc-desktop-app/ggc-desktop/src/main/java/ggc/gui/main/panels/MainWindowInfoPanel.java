package ggc.gui.main.panels;

import java.awt.*;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.observe.EventObserverInterface;

import ggc.core.data.defs.GGCObservableType;
import ggc.core.data.defs.RefreshInfoType;
import ggc.core.util.DataAccess;

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

    private static Logger LOG = LoggerFactory.getLogger(MainWindowInfoPanel.class);

    private ArrayList<AbstractInfoPanel> vInfoPanels = new ArrayList<AbstractInfoPanel>();
    DataAccess dataAccess = DataAccess.getInstance();


    /**
     * Constructor
     */
    public MainWindowInfoPanel()
    {
        setLayout(new GridLayout(0, 2));
        setBackground(Color.white);

        vInfoPanels.add(new SplittedInfoPanel(new GeneralInfoPanel(), new HbA1cInfoPanel()));
        vInfoPanels.add(new SplittedInfoPanel(new PlugInsInfoPanel(), new DeviceInfoPanel()));
        vInfoPanels.add(new SplittedInfoPanel(new AppointmentsInfoPanel(), new InventoryInfoPanel()));
        vInfoPanels.add(new StatisticsInfoPanel());

        addPanels();

        dataAccess.getObserverManager().addObserver(GGCObservableType.InfoPanels, this);
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
     * @param refreshInfoType
     */
    public void refreshGroup(RefreshInfoType refreshInfoType)
    {
        if (refreshInfoType == RefreshInfoType.DevicesConfiguration)
        {
            this.refreshPanels(InfoPanelType.ConfiguredDevices);
        }
        else if (refreshInfoType == RefreshInfoType.DeviceDataAll)
        {
            this.refreshPanels(InfoPanelType.HbA1c, InfoPanelType.Statistics);
        }
        else if (refreshInfoType == RefreshInfoType.GeneralInfo)
        {
            this.refreshPanels(InfoPanelType.General, InfoPanelType.Appointments);
        }
        else if (refreshInfoType == RefreshInfoType.PluginsAll)
        {
            this.refreshPanels(InfoPanelType.Plugins, InfoPanelType.ConfiguredDevices);
        }
        else if (refreshInfoType == RefreshInfoType.Appointments)
        {
            this.refreshPanels(InfoPanelType.Appointments);
        }
        else if (refreshInfoType == RefreshInfoType.Inventory)
        {
            this.refreshPanels(InfoPanelType.Inventory);
        }
    }


    /**
     * Update (From Observable)
     */
    public void update(Observable obj, Object arg)
    {
        if (arg instanceof RefreshInfoType)
        {
            RefreshInfoType refreshInfoType = (RefreshInfoType) arg;
            this.refreshGroup(refreshInfoType);
        }
        else
        {
            LOG.error("Unallowed update type ({}).", arg.getClass().getSimpleName());
        }
    }

}
