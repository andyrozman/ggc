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
    DataAccessPlugInBase dataAccess;
    I18nControlAbstract i18nControl;
    CommunicationPortComponent communicationPortComponent;
    JDialog parent;
    DeviceSpecialConfigPanelInterface specialConfig = null;
    int element_size = 65;

    DeviceInterface currentDeviceV1 = null;
    DeviceInstanceWithHandler currentDeviceV2 = null;

    DeviceConfigurationDialog deviceConfigurationDialog;


    /**
     * Constructor
     * 
     * @param x
     * @param y
     * @param da
     * @param deviceConfigurationDialog
     */
    public CommunicationSettingsPanel(int x, int y, DataAccessPlugInBase da,
            DeviceConfigurationDialog deviceConfigurationDialog)
    {
        super();
        this.setLayout(null);
        this.dataAccess = da;
        this.i18nControl = da.getI18nControlInstance();
        this.parent = deviceConfigurationDialog;
        this.deviceConfigurationDialog = deviceConfigurationDialog;
        this.x_pos = x;
        this.y_pos = y;

        this.setBorder(new TitledBorder(i18nControl.getMessage("COMMUNICATION_SETTINGS")));

        init();

        this.setBounds(x, y, 410, element_size);
    }


    /**
     * Init
     */
    public void init()
    {

        this.communicationPortComponent = new CommunicationPortComponent(dataAccess, this.parent);
        this.add(this.communicationPortComponent);
    }


    /**
     * Set Current Device
     * 
     * @param deviceInterface
     */
    public void setCurrentDevice(DeviceInterface deviceInterface)
    {
        resetDevices(false);

        this.currentDeviceV1 = deviceInterface;

        if (this.currentDeviceV1 == null)
        {
            this.communicationPortComponent.setProtocol(DeviceConnectionProtocol.None);
        }
        else
        {
            this.communicationPortComponent.setProtocol(this.currentDeviceV1.getConnectionProtocol());
        }

        this.specialConfig = deviceInterface.getSpecialConfigPanel();

        resetLayout(true);
    }


    /**
     * Set Current Device
     *
     * @param deviceInterface
     */
    public void setCurrentDevice(DeviceInstanceWithHandler deviceInterface)
    {
        resetDevices(false);

        this.currentDeviceV2 = deviceInterface;

        if (this.currentDeviceV2 == null)
        {
            this.communicationPortComponent.setProtocol(DeviceConnectionProtocol.None);
        }
        else
        {
            this.communicationPortComponent.setProtocol(this.currentDeviceV2.getConnectionProtocol());
        }

        this.specialConfig = deviceInterface.getSpecialConfigPanel();

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
            this.specialConfig = null;

            if (this.currentDeviceV2 != null && this.currentDeviceV2.hasSpecialConfig())
            {
                this.specialConfig = this.currentDeviceV2.getSpecialConfigPanel();
            }
            else if (this.currentDeviceV1 != null && this.currentDeviceV1.hasSpecialConfig())
            {
                this.specialConfig = this.currentDeviceV1.getSpecialConfigPanel();
            }
        }

        // System.out.println("Special config: " + this.specialConfig);

        this.removeAll();

        this.add(this.communicationPortComponent);

        if (this.specialConfig != null)
        {
            JPanel panel = this.specialConfig.getPanel();

            if (panel != null)
            {
                this.specialConfig.initPanel();
                panel.setBounds(5, 55, 400, this.specialConfig.getHeight());
                panel.setEnabled(true);
                this.add(panel);

                this.specialConfig.loadParametersToGUI();
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
        this.communicationPortComponent.setProtocol(protocol);
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
            this.communicationPortComponent.setCommunicationPort(i18nControl.getMessage("NOT_SET"));
        }
        else
        {
            if (this.specialConfig == null)
            {
                this.communicationPortComponent.setCommunicationPort(param);
            }
            else
            {
                this.specialConfig.loadConnectionParameters(param);
                this.communicationPortComponent.setCommunicationPort(this.specialConfig.getDefaultParameter());

                // System.out.println("Def parameter: " +
                // this.specialConfig.getDefaultParameter());

            }
        }

        // this.communicationPortComponent
    }


    /**
     * Get Parameters
     * 
     * @return
     */
    public String getParameters()
    {
        if (this.specialConfig == null)
            return this.communicationPortComponent.getCommunicationPort();
        else
        {
            this.specialConfig.setDefaultParameter(this.communicationPortComponent.getCommunicationPort());
            return this.specialConfig.saveConnectionParameters();
        }
    }


    /**
     * Are parameters set
     * 
     * @return
     */
    public boolean areParametersSet()
    {
        if (this.specialConfig == null)
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
                    this.specialConfig.setDefaultParameter(this.communicationPortComponent.getCommunicationPort());
                    return this.specialConfig.areConnectionParametersValid();
                }
                else
                    return false;
            }
            else
                return this.specialConfig.areConnectionParametersValid();

        }

    }


    public String getCustomErrorMessage()
    {
        if (this.specialConfig == null)
        {
            return null;
        }
        else
        {
            return this.specialConfig.getCustomErrorMessage();
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
        if (i18nControl.getMessage("NOT_SET").equals(this.communicationPortComponent.getCommunicationPort()))
            return false;

        if (this.communicationPortComponent.getCommunicationPort().trim().length() == 0)
            return false;
        else
            return true;
    }


    @Override
    public int getHeight()
    {
        int sz = element_size;

        if (this.specialConfig != null)
        {
            sz += this.specialConfig.getHeight();
        }

        return sz;

    }

}
