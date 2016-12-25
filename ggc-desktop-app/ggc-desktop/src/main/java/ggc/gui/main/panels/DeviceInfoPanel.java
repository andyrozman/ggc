package ggc.gui.main.panels;

import java.awt.*;

import javax.swing.*;

import org.sun.swing.layout.SpringUtilities;

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
@Deprecated
public class DeviceInfoPanel extends AbstractInfoPanel
{

    private static final long serialVersionUID = -4862594518423924680L;
    private JLabel lblMeter;
    private JLabel lblPump;
    private JLabel lblCgms;


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

        setLayout(new TableLayout(sizes));

        add(new JLabel(m_ic.getMessage("DEVICE_METER") + ":"), "1, 1");
        add(this.lblMeter = new JLabel("N/A"), "3, 1");

        add(new JLabel(m_ic.getMessage("DEVICE_PUMP") + ":"), "1, 2");
        add(this.lblPump = new JLabel("N/A"), "3, 2");

        add(new JLabel(m_ic.getMessage("DEVICE_CGMS") + ":"), "1, 3");
        add(this.lblCgms = new JLabel("N/A"), "3, 3");
    }


    private void init_old()
    {
        setLayout(new BorderLayout());

        JPanel lblPanel = new JPanel(new SpringLayout()); // new GridLayout(6,
                                                          // 2));
        lblPanel.setBackground(Color.white);

        // lblPanel.add(new JLabel(" ", JLabel.TRAILING));
        // lblPanel.add(new JLabel());

        lblPanel.add(new JLabel(m_ic.getMessage("DEVICE_METER") + ":", SwingConstants.LEADING));
        lblPanel.add(this.lblMeter = new JLabel("N/A"));

        lblPanel.add(new JLabel("", SwingConstants.TRAILING));
        lblPanel.add(new JLabel());

        lblPanel.add(new JLabel(m_ic.getMessage("DEVICE_PUMP") + ":", SwingConstants.LEADING));
        lblPanel.add(this.lblPump = new JLabel("N/A"));

        lblPanel.add(new JLabel("", SwingConstants.TRAILING));
        lblPanel.add(new JLabel());

        lblPanel.add(new JLabel(m_ic.getMessage("DEVICE_CGMS") + ":", SwingConstants.LEADING));
        lblPanel.add(this.lblCgms = new JLabel("N/A"));

        SpringUtilities.makeCompactGrid(lblPanel, 5, 2, // rows, cols
            10, 8, // initX, initY
            40, 6); // xPad, yPad

        add(lblPanel, BorderLayout.NORTH);
    }


    private String getDeviceInfo(PlugInClient cl)
    {
        String st = (String) cl.getReturnObject(1);

        if (st == null)
        {
            if (!cl.isPlugInInstalled())
                return m_ic.getMessage("PLUGIN_NA");
            else
                return m_ic.getMessage("PLUGIN_NO_FUNCTIONALITY");
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
        if (m_da.isPluginAvailable(GGCPluginType.MeterToolPlugin))
        {
            lblMeter.setText(getDeviceInfo(m_da.getPlugIn(GGCPluginType.MeterToolPlugin)));
        }
        if (m_da.isPluginAvailable(GGCPluginType.PumpToolPlugin))
        {
            lblPump.setText(getDeviceInfo(m_da.getPlugIn(GGCPluginType.PumpToolPlugin)));
        }
        if (m_da.isPluginAvailable(GGCPluginType.CGMSToolPlugin))
        {
            lblCgms.setText(getDeviceInfo(m_da.getPlugIn(GGCPluginType.CGMSToolPlugin)));
        }
    }


    @Override
    public InfoPanelType getPanelType()
    {
        return InfoPanelType.ConfiguredDevices;
    }

}
