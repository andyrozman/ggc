package ggc.gui.main.panels;

import javax.swing.*;

import com.atech.plugin.PlugInClient;

import ggc.core.plugins.GGCPluginType;
import ggc.core.util.DataAccess;
import info.clearthought.layout.TableLayout;

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
 *  Filename:     DeviceInfoPanel  
 *  Description:  Panel for displaying Device info
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */
public class DeviceInfoPanel extends AbstractInfoPanel
{

    private static final long serialVersionUID = -4862594518423924680L;

    GGCPluginType[] pluginTypes = { GGCPluginType.MeterToolPlugin, //
                                    GGCPluginType.PumpToolPlugin, //
                                    GGCPluginType.CGMSToolPlugin };
    JLabel[] deviceLabels = null;


    /**
     * Constructor
     */
    public DeviceInfoPanel()
    {
        super(DataAccess.getInstance().getI18nControlInstance().getMessage("DEVICES_USED"));
        init();
        refreshInfo();
    }


    private void init()
    {
        double sizes[][] = { { 10, 0.20, 10, TableLayout.FILL, 10 }, //
                             { 0.10, 0.27, 0.27, 0.27, TableLayout.FILL }, };

        JLabel lblMeter, lblPump, lblCgms;

        setLayout(new TableLayout(sizes));

        add(new JLabel(i18nControl.getMessage("DEVICE_METER") + ":"), "1, 1");
        add(lblMeter = new JLabel("N/A"), "3, 1");

        add(new JLabel(i18nControl.getMessage("DEVICE_PUMP") + ":"), "1, 2");
        add(lblPump = new JLabel("N/A"), "3, 2");

        add(new JLabel(i18nControl.getMessage("DEVICE_CGMS") + ":"), "1, 3");
        add(lblCgms = new JLabel("N/A"), "3, 3");

        JLabel[] labels = { lblMeter, lblPump, lblCgms };

        this.deviceLabels = labels;

    }


    private String getDeviceInfo(PlugInClient cl)
    {
        String st = (String) cl.getReturnObject(1);

        if (st == null)
        {
            if (!cl.isPlugInInstalled())
                return i18nControl.getMessage("PLUGIN_NA");
            else
                return i18nControl.getMessage("PLUGIN_NO_FUNCTIONALITY");
            // else
            // return i18nControl.getMessage("PLUGIN_NOT_INSTALLED");
        }
        else
            return st;

    }


    /**
     * Do Refresh - This method can do Refresh
     */
    @Override
    public void doRefresh()
    {
        for (int i = 0; i < deviceLabels.length; i++)
        {
            GGCPluginType pluginType = pluginTypes[i];

            if (dataAccess.isPluginAvailable(pluginType))
            {
                String deviceInfo = getDeviceInfo(dataAccess.getPlugIn(pluginType));

                deviceLabels[i].setText(deviceInfo);
                deviceLabels[i].setToolTipText(deviceInfo);
            }
        }
    }


    @Override
    public InfoPanelType getPanelType()
    {
        return InfoPanelType.ConfiguredDevices;
    }

}
