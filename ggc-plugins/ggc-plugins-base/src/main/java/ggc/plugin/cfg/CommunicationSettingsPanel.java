package ggc.plugin.cfg;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.atech.i18n.I18nControlAbstract;

import ggc.plugin.data.enums.DevicePortParameterType;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;
import ggc.plugin.gui.DeviceSpecialConfigPanelInterface;
import ggc.plugin.protocol.DeviceConnectionProtocol;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     CommunicationPortSelector
 *  Description:  --------------------------Selector for communication ports
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class CommunicationSettingsPanel extends JPanel
{

    private static final long serialVersionUID = 9197820657243699214L;

    int x_pos, y_pos;
    DataAccessPlugInBase m_da;
    I18nControlAbstract m_ic;
    CommunicationPortComponent comm_port_comp;
    JDialog parent;
    DeviceSpecialConfigPanelInterface special_config = null;
    int element_size = 65;

    DeviceInterface currentDeviceV1 = null;
    DeviceInstanceWithHandler currentDeviceV2 = null;


    /**
     * Constructor
     * 
     * @param x
     * @param y
     * @param da
     * @param parent_
     */
    public CommunicationSettingsPanel(int x, int y, DataAccessPlugInBase da, JDialog parent_)
    {
        super();
        this.setLayout(null);
        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
        this.parent = parent_;
        this.x_pos = x;
        this.y_pos = y;

        this.setBorder(new TitledBorder(m_ic.getMessage("COMMUNICATION_SETTINGS")));

        init();

        this.setBounds(x, y, 410, element_size);
    }


    /**
     * Init
     */
    public void init()
    {

        this.comm_port_comp = new CommunicationPortComponent(m_da, this.parent);
        this.add(this.comm_port_comp);
    }


    /**
     * Set Current Device
     * 
     * @param dev_interface
     */
    public void setCurrentDevice(DeviceInterface dev_interface)
    {
        resetDevices(false);

        this.currentDeviceV1 = dev_interface;

        if (this.currentDeviceV1 == null)
        {
            this.comm_port_comp.setProtocol(DeviceConnectionProtocol.None);
        }
        else
        {
            this.comm_port_comp.setProtocol(this.currentDeviceV1.getConnectionProtocol());
        }

        this.special_config = dev_interface.getSpecialConfigPanel();

        resetLayout(true);
    }


    /**
     * Set Current Device
     *
     * @param dev_interface
     */
    public void setCurrentDevice(DeviceInstanceWithHandler dev_interface)
    {
        resetDevices(false);

        this.currentDeviceV2 = dev_interface;

        if (this.currentDeviceV2 == null)
        {
            this.comm_port_comp.setProtocol(DeviceConnectionProtocol.None);
        }
        else
        {
            this.comm_port_comp.setProtocol(this.currentDeviceV2.getConnectionProtocol());
        }

        this.special_config = dev_interface.getSpecialConfigPanel();

        resetLayout(true);
    }


    public void resetDevices(boolean withLayout)
    {
        this.currentDeviceV2 = null;
        this.currentDeviceV1 = null;

        if (withLayout)
        {
            resetLayout(false);
        }
    }


    /**
     * Reset Layout
     */
    public void resetLayout(boolean selected)
    {
        if (selected)
        {
            this.special_config = null;

            if (this.currentDeviceV2 != null && this.currentDeviceV2.hasSpecialConfig())
            {
                this.special_config = this.currentDeviceV2.getSpecialConfigPanel();
            }
            else if (this.currentDeviceV1 != null && this.currentDeviceV1.hasSpecialConfig())
            {
                this.special_config = this.currentDeviceV1.getSpecialConfigPanel();
            }
        }

        // System.out.println("Special config: " + this.special_config);

        this.removeAll();

        this.add(this.comm_port_comp);

        if (this.special_config != null)
        {
            JPanel panel = this.special_config.getPanel();

            if (panel != null)
            {
                this.special_config.initPanel();
                panel.setBounds(5, 55, 400, this.special_config.getHeight());
                panel.setEnabled(true);
                this.add(panel);

                this.special_config.loadParametersToGUI();
                // panel.setBounds(40, 40, 400, 35);

            }
        }

        this.setBounds(x_pos, y_pos, 410, this.getHeight());
        this.repaint();
    }


    /**
     * Set Protocol
     * 
     * @param protocol
     */
    public void setProtocol(DeviceConnectionProtocol protocol)
    {
        this.comm_port_comp.setProtocol(protocol);
    }


    /**
     * Set Parameters
     * 
     * @param param
     */
    public void setParameters(String param)
    {
        if (param == null)
        {
            this.comm_port_comp.setCommunicationPort(m_ic.getMessage("NOT_SET"));
        }
        else
        {
            if (this.special_config == null)
            {
                this.comm_port_comp.setCommunicationPort(param);
            }
            else
            {
                this.special_config.loadConnectionParameters(param);
                this.comm_port_comp.setCommunicationPort(this.special_config.getDefaultParameter());

                // System.out.println("Def parameter: " +
                // this.special_config.getDefaultParameter());

            }
        }

        // this.comm_port_comp
    }


    /**
     * Get Parameters
     * 
     * @return
     */
    public String getParameters()
    {
        if (this.special_config == null)
            return this.comm_port_comp.getCommunicationPort();
        else
        {
            this.special_config.setDefaultParameter(this.comm_port_comp.getCommunicationPort());
            return this.special_config.saveConnectionParameters();
        }
    }


    /**
     * Are parameters set
     * 
     * @return
     */
    public boolean areParametersSet()
    {
        if (this.special_config == null)
        {
            if (hasDefaultParameter())
            {
                return checkIfDefaultParameterSet();
            }
            else
                return true;
        }
        else
        {
            if (hasDefaultParameter())
            {
                if (checkIfDefaultParameterSet())
                {
                    this.special_config.setDefaultParameter(this.comm_port_comp.getCommunicationPort());
                    return this.special_config.areConnectionParametersValid();
                }
                else
                    return false;
            }
            else
                return this.special_config.areConnectionParametersValid();

        }

    }


    private boolean hasDefaultParameter()
    {
        return (((this.currentDeviceV2 != null)
                && (this.currentDeviceV2.getDevicePortParameterType() != DevicePortParameterType.NoParameters))
                || ((this.currentDeviceV1 != null) && (this.currentDeviceV1.hasDefaultParameter())));
    }


    private boolean checkIfDefaultParameterSet()
    {
        if (m_ic.getMessage("NOT_SET").equals(this.comm_port_comp.getCommunicationPort()))
            return false;

        if (this.comm_port_comp.getCommunicationPort().trim().length() == 0)
            return false;
        else
            return true;
    }


    @Override
    public int getHeight()
    {
        int sz = element_size;

        if (this.special_config != null)
        {
            sz += this.special_config.getHeight();
        }

        return sz;

    }

}
