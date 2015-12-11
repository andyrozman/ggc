package ggc.plugin.device.impl.minimed.cfg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.apache.commons.lang.StringUtils;

import com.atech.utils.ATSwingUtils;

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

public class MinimedSpecialConfig extends DeviceSpecialConfigPanelAbstract implements ActionListener // ,
                                                                                                     // ItemListener,
                                                                                                     // DocumentListener
{

    String[] data_versions = null;
    JComboBox cbInterface = null;

    private JTextField tfSerialNr;

    boolean inited = false;


    public MinimedSpecialConfig(DataAccessPlugInBase da, DeviceInstanceWithHandler deviceInstanceV2)
    {
        super(da, deviceInstanceV2);
        System.out.println("MinimedSpecialConfig: " + this);
    }


    @Override
    public void initPanel()
    {
        if (inited)
            return;

        System.out.println("Init panel");

        this.configPanel = new JPanel();
        this.configPanel.setLayout(null);
        this.configPanel.setSize(400, getHeight());

        ATSwingUtils.initLibrary();

        String[] dta = MinimedCommInterfaceType.getSupportedInterfacesArray();

        data_versions = MinimedCommInterfaceType.getSupportedInterfacesArray();

        ATSwingUtils.getLabel(this.i18nControl.getMessage("MM_CONN_INTERFACE") + ":", 20, 5, 150, 25, this.configPanel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        cbInterface = ATSwingUtils.getComboBox(data_versions, 165, 5, 190, 25, this.configPanel,
            ATSwingUtils.FONT_NORMAL);
        // cbInterface.addItemListener(this);

        int size[] = { 17, 17 };

        ATSwingUtils.getLabel(this.i18nControl.getMessage("MM_SERIAL_NR") + ":", 20, 40, 150, 25, this.configPanel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        ATSwingUtils.getButton("", 365, 40, 25, 25, this.configPanel, ATSwingUtils.FONT_NORMAL, "about.png", "get_info",
            this, dataAccess, size); // , this, dataAccess, size);

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
            JOptionPane.showMessageDialog(dataAccess.getCurrentComponentParent(),
                i18nControl.getMessage("MINIMED_CONFIG_INFO"), //
                i18nControl.getMessage("INFORMATION"), //
                JOptionPane.INFORMATION_MESSAGE);
        }
    }


    @Override
    public boolean areCustomParametersValid()
    {
        readParametersFromGUI();
        return StringUtils.isNotBlank(tfSerialNr.getText());
    }
}
