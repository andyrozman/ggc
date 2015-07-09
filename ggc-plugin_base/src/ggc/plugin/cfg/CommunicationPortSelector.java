package ggc.plugin.cfg;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListDataListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.plugin.protocol.BlueToothProtocol;
import ggc.plugin.protocol.DeviceConnectionProtocol;
import ggc.plugin.protocol.SerialProtocol;
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

    /*
     * When adding new protocol search for 'New_Item_Edit' entries. There you
     * need to extend everything
     */
    // New_Item_Edit

    private static final long serialVersionUID = 1965963565398592466L;
    private static final Log LOG = LogFactory.getLog(CommunicationPortSelector.class);

    I18nControlAbstract m_ic;
    JLabel label;
    JTextField tf_port;
    JButton bt_select;
    DataAccessPlugInBase m_da;
    JPanel panel;
    JButton help_button;
    JList data_list;
    boolean was_action = false;

    // int connection_protocol_type = 0;

    // Integer protocolTypeV1;
    DeviceConnectionProtocol protocolType;


    /**
     * Constructor 
     * 
     * @param parent
     * @param da
     * @param protocolType
     */
    // public CommunicationPortSelector(JDialog parent, DataAccessPlugInBase da,
    // int protocolType)
    // {
    // super(parent, true);
    //
    // this.dataAccess = da;
    // this.m_ic = da.getI18nControlInstance();
    // this.protocolTypeV1 = protocolType;
    //
    // this.dataAccess.addComponent(this);
    // ATSwingUtils.initLibrary();
    //
    // init();
    // this.setVisible(true);
    // }

    /**
     * Constructor
     *
     * @param parent
     * @param da
     * @param protocolType
     */
    public CommunicationPortSelector(JDialog parent, DataAccessPlugInBase da, DeviceConnectionProtocol protocolType)
    {
        super(parent, true);

        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
        this.protocolType = protocolType;

        this.m_da.addComponent(this);
        ATSwingUtils.initLibrary();

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

        panel = new JPanel();
        panel.setBounds(0, 0, 350, 400);
        panel.setLayout(null);
        this.getContentPane().add(panel);

        int st_y = initType() + 25;

        // 125, 85

        label = ATSwingUtils.getLabel(m_ic.getMessage(getProtocolParameterName()) + ":", 20, 20, 300, 25, panel);

        // getAllAvailablePortsString()

        /*
         * label = new JLabel(m_ic.getMessage("COMMUNICATION_PORT") + ":");
         * label.setBounds(0, 0, 150, 25);
         * this.add(label);
         * tf_port = new JTextField();
         * tf_port.setBounds(160, 0, 80, 25);
         * tf_port.setEditable(false);
         * this.add(tf_port);
         */

        // set the buttons up...
        JButton button = new JButton("  " + m_ic.getMessage("OK"));
        // okButton.setPreferredSize(dim);
        button.setIcon(ATSwingUtils.getImageIcon_22x22("ok.png", this, m_da));
        button.setActionCommand("ok");
        button.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        button.setBounds(20, st_y, 110, 25);
        button.addActionListener(this);
        panel.add(button);

        button = new JButton("  " + m_ic.getMessage("CANCEL"));
        // cancelButton.setPreferredSize(dim);
        button.setIcon(ATSwingUtils.getImageIcon_22x22("cancel.png", this, m_da));
        button.setActionCommand("cancel");
        button.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        button.setBounds(140, st_y, 110, 25);
        button.addActionListener(this);
        panel.add(button);

        help_button = ATSwingUtils.createHelpButtonByBounds(260, st_y, 30, 25, this, ATSwingUtils.FONT_NORMAL, m_da);
        help_button.setText("");
        panel.add(help_button);

        this.setBounds(25, 115, 320, st_y + 80);
    }


    /**
     * Init Type
     * 
     * @return
     */
    public int initType()
    {
        // New_Item_Edit
        switch (this.protocolType)
        {
            case MassStorageXML:
            case Serial_USBBridge:
            case BlueTooth_Serial:
                {
                    return initList();
                }
            default:
                return 0;
        }
    }


    private int initList()
    {
        data_list = new JList();
        data_list.setModel(new ListModel()
        {

            List<String> elems = getDataForList();


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
        scr.setBounds(30, 50, 220, 100);

        panel.add(scr);

        return 150;

    }


    private boolean checkIfItemSelected()
    {
        // New_Item_Edit
        switch (this.protocolType)
        {
            case MassStorageXML:
            case Serial_USBBridge:
            case BlueTooth_Serial:
                {
                    return this.data_list.getSelectedIndex() > -1;
                }
            default:
                return false;
        }
    }


    /**
     * Get Selected Item
     * 
     * @return
     */
    public String getSelectedItem()
    {
        // New_Item_Edit
        switch (this.protocolType)
        {
            case MassStorageXML:
            case Serial_USBBridge:
            case BlueTooth_Serial:
                {
                    return (String) this.data_list.getSelectedValue();
                }
            default:
                return null;
        }

    }


    /**
     * Get Protocol Parameter Name
     * 
     * @return
     */
    public String getProtocolParameterName()
    {
        // New_Item_Edit
        switch (this.protocolType)
        {
            case MassStorageXML:
                {
                    return "SELECT_MASS_STORAGE_DRIVE";
                }
            case Serial_USBBridge:
            case BlueTooth_Serial:
                {
                    return "SELECT_SERIAL_PORT";
                }
            default:
                return "";
        }

    }


    /**
     * Get Not Fillled Error
     * 
     * @return
     */
    public String getNotFilledError()
    {
        // New_Item_Edit
        switch (this.protocolType)
        {
            case MassStorageXML:
            case Serial_USBBridge:
            case BlueTooth_Serial:
                {
                    return "SELECT_ITEM_OR_CANCEL";
                }
            default:
                return "";
        }

    }


    protected List<String> getDataForList()
    {
        List<String> portList = new ArrayList<String>();

        // New_Item_Edit
        if (this.protocolType == DeviceConnectionProtocol.Serial_USBBridge)
        {
            try
            {
                return SerialProtocol.getAllAvailablePortsString();
            }
            catch (Exception ex)
            {
                LOG.warn("Couldn't read available ports. Ex: " + ex);
            }

        }
        else if (this.protocolType == DeviceConnectionProtocol.MassStorageXML)
        {

            Vector<String> drives = new Vector<String>();

            if (System.getProperty("os.name").contains("Win"))
            {
                File[] fls = File.listRoots();

                for (File fl : fls)
                {
                    drives.add(fl.toString());
                }
            }
            else
            {
                // non windows system, will load data from /mnt and /media and
                // /Volumes. Some Linux machines also have additional mount
                // point in latest releases (/media/<username>), which is
                // also checked.

                List<String> rts = new ArrayList<String>();
                rts.add("/mnt");
                rts.add("/Volumes");

                if (System.getProperty("os.name").equals("Linux"))
                {
                    String fName = "/media/" + System.getProperty("user.name");
                    File f = new File(fName);

                    if (f.exists())
                    {
                        rts.add(fName);
                    }
                    else
                    {
                        rts.add("/media");
                    }
                }

                for (String rt : rts)
                {
                    File f = new File(rt);

                    if (f.exists())
                    {
                        File[] f2 = f.listFiles();

                        for (File element : f2)
                        {
                            if (element.isDirectory())
                            {
                                drives.add(element.toString());
                            }
                        }
                    }
                }

            }

            return drives;
        }
        else if (this.protocolType == DeviceConnectionProtocol.BlueTooth_Serial)
        {
            try
            {
                return BlueToothProtocol.getAllAvailablePortsString();
            }
            catch (Exception ex)
            {
                LOG.warn("Couldn't read available ports. Ex: " + ex);
            }
        }

        return portList;
    }


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
                this.m_da.removeComponent(this);
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
            this.m_da.removeComponent(this);
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
