package ggc.plugin.device.impl.minimed.cfg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.apache.commons.lang.StringUtils;

import com.atech.utils.ATSwingUtils;

import ggc.plugin.cfg.DeviceConfigurationDialog;
import ggc.plugin.device.impl.minimed.enums.MinimedCommInterfaceType;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;
import ggc.plugin.gui.DeviceSpecialConfigPanelAbstract;
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
 *  Filename:     MinimedSpecialConfig
 *  Description:  Minimed Special Config
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedSpecialConfig extends DeviceSpecialConfigPanelAbstract implements ActionListener
{

    String[] dataVersions = null;
    JComboBox cbInterface = null;
    private JTextField tfSerialNr;
    boolean inited = false;


    public MinimedSpecialConfig(DataAccessPlugInBase da, DeviceInstanceWithHandler deviceInstanceV2)
    {
        super(da, deviceInstanceV2);
    }


    @Override
    public void initPanel()
    {
        if (inited)
            return;

        this.configPanel = new JPanel();
        this.configPanel.setLayout(null);
        this.configPanel.setSize(400, getHeight());

        ATSwingUtils.initLibrary();

        dataVersions = MinimedCommInterfaceType.getSupportedInterfacesArray();

        ATSwingUtils.getLabel(this.i18nControl.getMessage("MM_CONN_INTERFACE") + ":", 20, 5, 150, 25, this.configPanel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        cbInterface = ATSwingUtils.getComboBox(dataVersions, 165, 5, 190, 25, this.configPanel,
            ATSwingUtils.FONT_NORMAL);

        int size[] = { 17, 17 };

        ATSwingUtils.getLabel(this.i18nControl.getMessage("MM_SERIAL_NR") + ":", 20, 40, 150, 25, this.configPanel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        ATSwingUtils.getButton("", 365, 40, 25, 25, this.configPanel, ATSwingUtils.FONT_NORMAL, "about.png", "get_info",
            this, dataAccess, size);

        this.tfSerialNr = ATSwingUtils.getTextField(null, 165, 40, 150, 25, this.configPanel, ATSwingUtils.FONT_NORMAL);
        // this.tfSerialNr.getDocument().addDocumentListener(this);

        inited = true;
    }


    public int getHeight()
    {
        return 75;
    }


    public void readParametersFromGUI()
    {
        setParameter("MINIMED_SERIAL", tfSerialNr.getText());

        setParameter("MINIMED_INTERFACE",
            MinimedCommInterfaceType.getByDescription((String) cbInterface.getSelectedItem()).name());
    }


    public void initParameters()
    {

    }


    public void loadParametersToGUI()
    {
        String p1 = this.parameters.get("MINIMED_INTERFACE");

        this.cbInterface.setSelectedItem(MinimedCommInterfaceType.getByName(p1));

        p1 = this.parameters.get("MINIMED_SERIAL");

        if (StringUtils.isNotBlank(p1))
        {
            this.tfSerialNr.setText(p1);
        }
    }


    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getActionCommand().contains("get_info"))
        {
            ATSwingUtils.showMessageDialog(dataAccess.getDeviceConfigurationDialog(), //
                ATSwingUtils.DialogType.Info, //
                i18nControl.getMessage("MM_CONFIG_INFO"), //
                i18nControl);
        }
    }


    @Override
    public boolean areCustomParametersValid()
    {
        readParametersFromGUI();

        if (StringUtils.isBlank(tfSerialNr.getText()))
            return false;

        // check

        DeviceConfigurationDialog dialog = dataAccess.getDeviceConfigurationDialog();

        String deviceName = dialog.getCurrentDeviceV2().getDeviceDefinitionBase().getDeviceName();

        MinimedCommInterfaceType interfaceType = MinimedCommInterfaceType
                .getByDescription((String) cbInterface.getSelectedItem());

        if (((interfaceType == MinimedCommInterfaceType.ContourNextLink2) && //
                (isContourNextLink24Required(deviceName))) //
                || //
                ((interfaceType == MinimedCommInterfaceType.ContourNextLink24) && //
                        (!isContourNextLink24Required(deviceName))))
        {
            return false;
        }

        return true;
    }


    private boolean isContourNextLink24Required(String deviceName)
    {
        // add new 6xx devices here (name from PumpDevices)
        return deviceName.equals("Minimed_640G");
    }


    public String getCustomErrorMessage()
    {
        return "MM_CUSTOM_ERROR";
    }

}
