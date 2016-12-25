package ggc.plugin.device.impl.accuchek;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.*;

import com.atech.utils.ATSwingUtils;

import ggc.plugin.device.DeviceAbstract;
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
 *  Filename:     AccuChekSmartPixSpecialConfig  
 *  Description:  AccuChek SmartPix Special Config
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class AccuChekSmartPixSpecialConfig extends DeviceSpecialConfigPanelAbstract
        implements ActionListener, ItemListener
{

    String[] data_versions = null;
    JComboBox cb_versions = null;
    boolean inited = false;

    /**
     * SmartPix Version 2.x
     */
    public static final String SMARTPIX_V2 = "2.x";

    /**
     * SmartPix Version 3.x
     */
    public static final String SMARTPIX_V3 = "3.x";


    /**
     * {@inheritDoc}
     */
    public AccuChekSmartPixSpecialConfig(DataAccessPlugInBase da, DeviceAbstract deviceInstanceV1)
    {
        super(da, deviceInstanceV1);
    }


    /**
     * {@inheritDoc}
     */
    public AccuChekSmartPixSpecialConfig(DataAccessPlugInBase da, DeviceInstanceWithHandler deviceInstanceV2)
    {
        super(da, deviceInstanceV2);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void initPanel()
    {
        if (inited)
            return;

        // System.out.println("Init panel");

        this.configPanel = new JPanel();
        this.configPanel.setLayout(null);

        ATSwingUtils.initLibrary();

        String[] dta = { this.i18nControl.getMessage("SMARTPIX_VERSION_2"),
                         this.i18nControl.getMessage("SMARTPIX_VERSION_3") };
        data_versions = dta;

        ATSwingUtils.getLabel(this.i18nControl.getMessage("DEVICE_VERSION") + ":", 20, 5, 150, 25, this.configPanel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        cb_versions = ATSwingUtils.getComboBox(dta, 165, 5, 190, 25, this.configPanel, ATSwingUtils.FONT_NORMAL);
        cb_versions.addItemListener(this);

        int size[] = { 17, 17 };

        ATSwingUtils.getButton("", 365, 5, 25, 25, this.configPanel, ATSwingUtils.FONT_NORMAL, "about.png", "get_info",
            this, dataAccess, size); // , this, dataAccess, size);

        inited = true;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean areCustomParametersValid()
    {
        return true;
    }


    /**
     * {@inheritDoc}
     */
    public int getHeight()
    {
        return 35;
    }


    /**
     * {@inheritDoc}
     */
    public void readParametersFromGUI()
    {
        /*
         * this.parameters.remove("SMARTPIX_VERSION");
         * System.out.println("readParametersFromGUI : " +
         * this.cb_versions.getSelectedIndex() + ", item=" +
         * this.cb_versions.getSelectedItem());
         * if (this.cb_versions.getSelectedIndex()==0)
         * this.parameters.put("SMARTPIX_VERSION", "2.x");
         * else
         * this.parameters.put("SMARTPIX_VERSION", "3.x");
         */
    }


    /**
     * {@inheritDoc}
     */
    public void initParameters()
    {
        if (!this.parameters.containsKey("SMARTPIX_VERSION"))
        {
            this.parameters.put("SMARTPIX_VERSION", "3.x");
        }
    }


    /**
     * {@inheritDoc}
     */
    public void loadParametersToGUI()
    {
        // System.out.println("loadParametersToGUI: " + this.parameters);

        String p1 = this.parameters.get("SMARTPIX_VERSION");

        // if (this.cb_versions == null)
        // {
        // this.initPanel();
        // }

        // System.out.println("P1: " + p1);

        if (p1.equals("2.x"))
        {
            this.cb_versions.setSelectedIndex(0);
        }
        else
        {
            this.cb_versions.setSelectedIndex(1);
        }

        // System.out.println("P1: index: " +
        // this.cb_versions.getSelectedIndex());
    }


    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getActionCommand().contains("get_info"))
        {
            JOptionPane.showMessageDialog(dataAccess.getCurrentComponentParent(),
                i18nControl.getMessage("SMARTPIX_VERSION_INFO"), //
                i18nControl.getMessage("INFORMATION"), //
                JOptionPane.INFORMATION_MESSAGE);
        }
    }


    public void itemStateChanged(ItemEvent e)
    {
        JComboBox cb = (JComboBox) e.getSource();

        this.parameters.put("SMARTPIX_VERSION", cb.getSelectedIndex() == 0 ? "2.x" : "3.x");
    }

}
