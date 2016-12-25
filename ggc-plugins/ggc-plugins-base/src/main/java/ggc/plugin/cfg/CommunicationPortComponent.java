package ggc.plugin.cfg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.plugin.comm.ports.discovery.PortDiscoveryAgentInterface;
import ggc.plugin.comm.ports.discovery.PortDiscoveryManager;
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
 *  Filename:     CommunicationPortComponent
 *  Description:  Communication Port Component
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class CommunicationPortComponent extends JPanel implements ActionListener
{
    /*
     * When adding new protocol search for 'New_Item_Edit' entries. There you
     * need to extend everything
     */

    private static final long serialVersionUID = 6490997313283293063L;

    I18nControlAbstract m_ic;
    JLabel label;
    JTextField tf_port;
    JButton bt_select;

    JDialog parent;
    DataAccessPlugInBase m_da;

    private DeviceConnectionProtocol protocolType;
    private PortDiscoveryAgentInterface portDiscoveryAgent;
    PortDiscoveryManager portDiscoveryManager = PortDiscoveryManager.getInstance();


    /**
     * Constructor
     * 
     * @param da
     * @param parent
     */
    public CommunicationPortComponent(DataAccessPlugInBase da, JDialog parent)
    {
        super();

        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
        this.parent = parent;

        ATSwingUtils.initLibrary();

        init();
    }


    /**
     * Init
     */
    public void init()
    {
        this.setLayout(null);
        this.setBounds(25, 25, 370, 25);

        label = ATSwingUtils.getLabel(m_ic.getMessage("COMMUNICATION_PORT") + ":", 0, 0, 150, 25, this,
            ATSwingUtils.FONT_NORMAL_BOLD);

        tf_port = ATSwingUtils.getTextField("", 145, 0, 110, 25, this);
        tf_port.setEditable(false);

        this.bt_select = ATSwingUtils.getButton(m_ic.getMessage("SELECT"), 270, 0, 100, 25, this,
            ATSwingUtils.FONT_NORMAL, null, "", this, m_da);
    }


    /**
     * Set Communication Port
     * 
     * @param val
     */
    public void setCommunicationPort(String val)
    {
        this.tf_port.setText(val);
        this.tf_port.setToolTipText(val);
    }


    /**
     * Get Communication Port
     * @return
     */
    public String getCommunicationPort()
    {
        return this.tf_port.getText();
    }


    /**
     * Action Performed
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0)
    {
        if ((this.portDiscoveryAgent != null) && (!this.portDiscoveryAgent.isEmptyDiscoveryAgent()))
        {
            CommunicationPortSelector cps = new CommunicationPortSelector(this.parent, m_da, this.portDiscoveryAgent);
            if (cps.wasAction())
            {
                this.tf_port.setText(cps.getSelectedItem());
            }
        }
    }


    /**
     * Set Protocol
     *
     * @param protocol
     */
    public void setProtocol(DeviceConnectionProtocol protocol)
    {
        if (protocol == null)
        {
            label.setText(m_ic.getMessage("COMMUNICATION_PORT") + ":");
            setCommunicationPort("N/A");
            this.bt_select.setEnabled(false);
            return;
        }

        this.portDiscoveryAgent = portDiscoveryManager.getDiscoveryAgent(protocol);

        label.setText(m_ic.getMessage(this.portDiscoveryAgent.getPortDeviceName()) + ":");
        setCommunicationPort("");
        this.bt_select.setEnabled(!this.portDiscoveryAgent.isEmptyDiscoveryAgent());
    }

}
