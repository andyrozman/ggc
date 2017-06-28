package ggc.gui.little.panels;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.observe.EventObserverInterface;
import com.atech.i18n.I18nControlAbstract;

import ggc.core.data.defs.GGCObservableType;
import ggc.core.data.defs.RefreshInfoType;
import ggc.core.util.DataAccess;
import ggc.gui.little.GGCLittle;
import ggc.gui.main.panels.*;

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

public class MainLittlePanel extends JPanel implements EventObserverInterface
{

    private static final long serialVersionUID = 8210688971878569493L;

    private static Logger LOG = LoggerFactory.getLogger(MainLittlePanel.class);

    // private Vector<JPanel> vInfoPanels = new Vector<JPanel>();

    List<AbstractInfoPanel> vInfoPanels = new ArrayList<AbstractInfoPanel>();

    GGCLittle m_little = null;

    private GeneralInfoPanelL general = null;

    /**
     * Daily Stats Panel
     */
    public DailyStatsListPanelL dailyStats = null;

    // tabs
    private DailyStatsControlsL control = null;
    @SuppressWarnings("unused")
    private PlugInsInfoPanel plug_in = null;
    @SuppressWarnings("unused")
    private StocksInfoPanelL stocks = null;
    private ScheduleInfoPanelL schedule = null;

    private JTabbedPane tabbedPaneGeneral = null;
    private JTabbedPane tabbedPaneControl = null;

    private DataAccess dataAccess = DataAccess.getInstance();
    private I18nControlAbstract ic = dataAccess.getI18nControlInstance();


    /**
     * Constructor
     * 
     * @param little
     */
    public MainLittlePanel(GGCLittle little)
    {
        m_little = little;
        setLayout(new GridLayout(2, 1));
        // setBackground(Color.white);

        JPanel pane = new JPanel();
        pane.setLayout(new GridLayout(1, 2));

        // pane.add(general = new GeneralInfoPanelL());

        pane.add(tabbedPaneGeneral = new JTabbedPane());
        addTab("TAB_GENERAL", this.tabbedPaneGeneral, new GeneralInfoPanelL());
        // addTab("TAB_GENERAL", this.tabbedPaneGeneral, new
        // GeneralInfoPanelL());
        addTab("TAB_STATISTICS", this.tabbedPaneGeneral, new StatisticsInfoPanel());

        JPanel control_app_panel = new JPanel();
        control_app_panel.setLayout(new GridLayout(1, 1));
        control_app_panel.add(this.tabbedPaneControl = new JTabbedPane());

        addTab("TAB_CONTROL", this.tabbedPaneControl, new DailyStatsControlsL(this));
        addTab("TAB_PLUGIN", this.tabbedPaneControl, new PlugInsInfoPanel());
        addTab("TAB_DEVICES", this.tabbedPaneControl, new DeviceInfoPanel());
        addTab("TAB_SCHEDULE", this.tabbedPaneControl, new ScheduleInfoPanelL());
        addTab("TAB_STOCKS", this.tabbedPaneControl, new StocksInfoPanelL());

        // this.tabbedPaneControl.addTab(i18nControl.getMessage("TAB_CONTROL"),
        // control =
        // new
        // DailyStatsControlsL(this));
        // this.tabbedPaneControl.addTab(i18nControl.getMessage("TAB_PLUGIN"),
        // plug_in =
        // new
        // PlugInsInfoPanel());
        // this.tabbedPaneControl.addTab(i18nControl.getMessage("TAB_SCHEDULE"),
        // schedule
        // = new
        // ScheduleInfoPanelL());
        // this.tabbedPaneControl.addTab(i18nControl.getMessage("TAB_STOCKS"),
        // stocks =
        // new
        // StocksInfoPanelL());
        // this.tabbedPaneControl.addTab(i18nControl.getMessage("TAB_DEVICES"),
        // pluginDevices =
        // new DeviceInfoPanel());

        // public PlugInPanelL plug_in = null;
        // public StocksInfoPanelL stocks = null;
        // public ScheduleInfoPanelL schedule = null;

        // control = new DailyStatsControlsL(this));

        /*
         * control_app_panel.setLayout(new GridLayout(2, 1));
         * control_app_panel.add(control = new DailyStatsControlsL(this));
         * control_app_panel.add(schedule = new ScheduleInfoPanelL()); //new
         * DailyStatsControlsPanel(little));
         */
        pane.add(control_app_panel);

        add(pane);
        add(dailyStats = new DailyStatsListPanelL());

        // this.vInfoPanels.add(general);
        // this.vInfoPanels.add(control);
        // this.vInfoPanels.add(schedule);
        // this.vInfoPanels.add(dailyStats);
        // this.vInfoPanels.add(plug_in);

        dataAccess.getObserverManager().addObserver(GGCObservableType.InfoPanels, this);

    }


    public GGCLittle getApplication()
    {
        return this.m_little;
    }


    private void addTab(String i18nKey, JTabbedPane tabbedPane, AbstractInfoPanel panel)
    {
        tabbedPane.addTab(ic.getMessage(i18nKey), panel);
        this.vInfoPanels.add(panel);
    }

    // private void addPanels()
    // {
    // for (int i = 0; i < vInfoPanels.size(); i++)
    // {
    // add(vInfoPanels.get(i));
    // }
    // }

    // /**
    // * Refresh Panels
    // */
    // public void refreshPanels()
    // {
    // for (int i = 0; i < vInfoPanels.size(); i++)
    // {
    // ((AbstractInfoPanel) vInfoPanels.get(i)).refreshInfo();
    // }
    // }

    // /**
    // * Add Panel At
    // * @param index index of panel
    // * @param panel
    // */
    // public void addPanelAt(int index, AbstractInfoPanel panel)
    // {
    // vInfoPanels.add(index, panel);
    // removeAll();
    // addPanels();
    // }

    // /**
    // * Remove Panel At
    // *
    // * @param index index of panel
    // */
    // public void removePanelAt(int index)
    // {
    // vInfoPanels.remove(index);
    // removeAll();
    // addPanels();
    // }

    // TODO missing refresh functionality see ggc-desktop


    /**
     * RefreshInfo - Refresh info by id
     *
     * @param mask
     */
    public void refreshPanels(InfoPanelType... mask)
    {
        for (AbstractInfoPanel panel : vInfoPanels)
        {
            panel.refreshInfo(mask);
        }
    }


    /**
     * Refresh Group
     *
     * @param refreshInfoType
     */
    public void refreshGroup(RefreshInfoType refreshInfoType)
    {
        System.out.println("Refresh Group: " + refreshInfoType);

        if (refreshInfoType == RefreshInfoType.DevicesConfiguration)
        {
            this.refreshPanels(InfoPanelType.ConfiguredDevices, InfoPanelType.DailyValuesController);
        }
        else if (refreshInfoType == RefreshInfoType.DeviceDataAll)
        {
            this.refreshPanels(InfoPanelType.HbA1c, InfoPanelType.Statistics);
        }
        else if (refreshInfoType == RefreshInfoType.GeneralInfo)
        {
            this.refreshPanels(InfoPanelType.General);
        }
        else if (refreshInfoType == RefreshInfoType.PluginsAll)
        {
            this.refreshPanels(InfoPanelType.Plugins, InfoPanelType.ConfiguredDevices);
        }
    }


    /**
     * Update (From Observable)
     */
    public void update(Observable obj, Object arg)
    {
        System.out.println("!!!! update panels: " + this.getClass().getSimpleName() + " - " + arg);

        if (arg instanceof RefreshInfoType)
        {
            RefreshInfoType refreshInfoType = (RefreshInfoType) arg;
            this.refreshGroup(refreshInfoType);
        }
        else
        {
            LOG.error("Unallowed update type ({}) - {}.", arg.getClass().getSimpleName(), arg);
        }
    }

}
