package ggc.plugin.cfg;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListDataListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.plugin.comm.ports.DevicePortDto;
import ggc.plugin.comm.ports.discovery.PortDiscoveryAgentInterface;
import ggc.plugin.device.PlugInBaseException;
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
 *  Description:  Selector for communication ports
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class CommunicationPortSelector extends JDialog implements ActionListener, HelpCapable
{

    /**
     * In new version we have now so called discovery agents, which try to give us list of all ports. This
     * needs to be implemented for each DeviceConnectionProtocol instance. If not emptyDiscoveryAgent will be returned
     * which has no ports.
     */

    private static final long serialVersionUID = 1965963565398592466L;
    private static final Log LOG = LogFactory.getLog(CommunicationPortSelector.class);

    private PortDiscoveryAgentInterface discoveryAgent;

    I18nControlAbstract m_ic;
    JLabel label;
    DataAccessPlugInBase dataAccess;
    JPanel panel;
    JButton help_button;
    JList data_list;
    boolean was_action = false;


    /**
     * Constructor
     *
     * @param parent
     * @param da
     * @param discoveryAgent
     */
    public CommunicationPortSelector(JDialog parent, DataAccessPlugInBase da,
            PortDiscoveryAgentInterface discoveryAgent)
    {
        super(parent, true);

        this.dataAccess = da;
        this.m_ic = da.getI18nControlInstance();
        // this.protocolType = protocolType;

        this.dataAccess.addComponent(this);
        ATSwingUtils.initLibrary();

        this.discoveryAgent = discoveryAgent;

        LOG.debug("Selected Discovery Agent: " + this.discoveryAgent.getClass().getSimpleName());

        init();
        this.setVisible(true);
    }


    /**
     * Init
     */
    public void init()
    {
        ATSwingUtils.initLibrary();

        this.setLayout(null);
        this.setBounds(25, 115, 320, 300);
        this.setResizable(false);

        panel = new JPanel();
        panel.setBounds(0, 0, 320, 400);
        panel.setLayout(null);
        this.getContentPane().add(panel);

        int st_y = initType() + 25;

        // 125, 85

        String name = m_ic.getMessage(getProtocolParameterName());

        this.setTitle(name);

        label = ATSwingUtils.getTitleLabel(name, 0, 20, 320, 25, panel, ATSwingUtils.FONT_BIG_BOLD);

        // set the buttons up...
        JButton button = new JButton("  " + m_ic.getMessage("OK"));
        // okButton.setPreferredSize(dim);
        button.setIcon(ATSwingUtils.getImageIcon_22x22("ok.png", this, dataAccess));
        button.setActionCommand("ok");
        button.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        button.setBounds(20, st_y, 110, 25);
        button.addActionListener(this);
        panel.add(button);

        button = new JButton("  " + m_ic.getMessage("CANCEL"));
        // cancelButton.setPreferredSize(dim);
        button.setIcon(ATSwingUtils.getImageIcon_22x22("cancel.png", this, dataAccess));
        button.setActionCommand("cancel");
        button.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        button.setBounds(140, st_y, 110, 25);
        button.addActionListener(this);
        panel.add(button);

        help_button = ATSwingUtils.createHelpButtonByBounds(260, st_y, 30, 25, this, ATSwingUtils.FONT_NORMAL,
            dataAccess);
        help_button.setText("");
        panel.add(help_button);

        this.setBounds(25, 115, 320, st_y + 80);

        this.dataAccess.centerJDialog(this);
    }


    /**
     * Init Type
     * 
     * @return
     */
    public int initType()
    {
        if (discoveryAgent.isEmptyDiscoveryAgent())
            return 0;
        else
            return initList();

        // // FIXME remove
        // // New_Item_Edit
        // switch (this.protocolType)
        // {
        // case MassStorageXML:
        // case Serial_USBBridge:
        // case BlueTooth_Serial:
        // case USB_Hid:
        // {
        // return initList();
        // }
        //
        // default:
        // return 0;

    }


    private int initList()
    {
        data_list = new JList();
        data_list.setModel(new ListModel()
        {

            List<DevicePortDto> elems = getDataForList();


            public void addListDataListener(ListDataListener arg0)
            {
            }


            public Object getElementAt(int index)
            {
                return elems.get(index);
            }


            public int getSize()
            {
                return elems.size();
            }


            public void removeListDataListener(ListDataListener arg0)
            {
            }

        });

        JScrollPane scr = new JScrollPane(data_list);
        scr.setBounds(25, 70, 260, 150);

        panel.add(scr);

        return 210;

    }


    private boolean checkIfItemSelected()
    {

        if (discoveryAgent.isEmptyDiscoveryAgent())
            return false;
        else
            return this.data_list.getSelectedIndex() > -1;

        //
        // // New_Item_Edit
        // switch (this.protocolType)
        // {
        // case MassStorageXML:
        // case Serial_USBBridge:
        // case BlueTooth_Serial:
        // case USB_Hid:
        // {
        // return this.data_list.getSelectedIndex() > -1;
        // }
        // default:
        // return false;
        // }
    }


    /**
     * Get Selected Item
     * 
     * @return
     */
    public String getSelectedItem()
    {

        if (discoveryAgent.isEmptyDiscoveryAgent())
            return null;
        else
        {
            DevicePortDto portDto = (DevicePortDto) this.data_list.getSelectedValue();
            return portDto.getSetValue();
        }

        // // TODO remove
        // // New_Item_Edit
        // switch (this.protocolType)
        // {
        // case MassStorageXML:
        // case Serial_USBBridge:
        // case BlueTooth_Serial:
        // case USB_Hid:
        // {
        // return (String) this.data_list.getSelectedValue();
        // }
        // default:
        // return null;
        // }

    }


    /**
     * Get Protocol Parameter Name
     * 
     * @return
     */
    public String getProtocolParameterName()
    {
        return discoveryAgent.getSelectProtocolString();
        // if (discoveryAgent.isEmptyDiscoveryAgent())
        // return "";
        // else
        // {
        // return discoveryAgent.getSelectProtocolString();
        // }

        //
        // // New_Item_Edit
        // switch (this.protocolType)
        // {
        // case MassStorageXML:
        // {
        // return "SELECT_MASS_STORAGE_DRIVE";
        // }
        // case Serial_USBBridge:
        // case BlueTooth_Serial:
        // {
        // return "SELECT_SERIAL_PORT";
        // }
        // case USB_Hid:
        // {
        //
        // }
        // default:
        // return "";
        // }

    }


    /**
     * Get Not Fillled Error
     * 
     * @return
     */
    public String getNotFilledError()
    {
        if (discoveryAgent.isEmptyDiscoveryAgent())
            return "";
        else
        {
            return "SELECT_ITEM_OR_CANCEL";
        }

        // // TODO remove
        // // New_Item_Edit
        // switch (this.protocolType)
        // {
        // case MassStorageXML:
        // case Serial_USBBridge:
        // case BlueTooth_Serial:
        // case USB_Hid:
        // {
        // return "SELECT_ITEM_OR_CANCEL";
        // }
        // default:
        // return "";
        // }

    }


    protected List<DevicePortDto> getDataForList()
    {
        try
        {
            return discoveryAgent.getAllPossiblePorts();
        }
        catch (PlugInBaseException ex)
        {
            dataAccess.createErrorDialog(ex, ex.getExceptionType().getErrorMessage(), "CONTACT_SUPPORT_WITH_LOG");
            return null;
        }
    }


    // protected List<String> getDataForListWithString()
    // {
    // List<String> portList = new ArrayList<String>();
    //
    // // New_Item_Edit
    // if (this.protocolType == DeviceConnectionProtocol.Serial_USBBridge)
    // {
    // try
    // {
    // return SerialProtocol.getAllAvailablePortsString();
    // }
    // catch (Exception ex)
    // {
    // LOG.warn("Couldn't read available ports. Ex: " + ex);
    // }
    //
    // }
    // else if (this.protocolType == DeviceConnectionProtocol.BlueTooth_Serial)
    // {
    // try
    // {
    // return BlueToothProtocol.getAllAvailablePortsString();
    // }
    // catch (Exception ex)
    // {
    // LOG.warn("Couldn't read available ports. Ex: " + ex);
    // }
    // }
    // else if (this.protocolType == DeviceConnectionProtocol.USB_Hid)
    // {
    //
    // }
    //
    // return portList;
    // }

    /**
     * Was Action
     * 
     * @return true if action was executed
     */
    public boolean wasAction()
    {
        return was_action;
    }


    /**
     * Action Performed (for Action Listener)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae)
    {
        String action = ae.getActionCommand();

        if (action.equals("ok"))
        {
            if (checkIfItemSelected())
            {
                this.was_action = true;
                this.dispose();
                this.dataAccess.removeComponent(this);
            }
            else
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage(getNotFilledError()), m_ic.getMessage("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

        }
        else if (action.equals("cancel"))
        {
            this.was_action = false;
            this.dispose();
            this.dataAccess.removeComponent(this);
        }

    }


    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    /** 
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
        return this.getRootPane();
    }


    /** 
     * getHelpButton - get Help button
     */
    public JButton getHelpButton()
    {
        return this.help_button;
    }


    /** 
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        // return dataAccess.getDeviceConfigurationDefinition().getHelpPrefix()
        // +
        // "Configuration_PortSelector";
        return "DeviceTool_Configuration_PortSelector";
    }

}
