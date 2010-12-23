package ggc.plugin.cfg;

import ggc.plugin.protocol.BlueToothProtocol;
import ggc.plugin.protocol.ConnectionProtocols;
import ggc.plugin.protocol.SerialProtocol;
import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.update.startup.os.OSUtil;
import com.atech.utils.ATSwingUtils;

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
    /* When adding new protocol search for 'New_Item_Edit' entries. There you need to extend everything */
    // New_Item_Edit
    
    
    /**
     * 
     */
    private static final long serialVersionUID = 1965963565398592466L;
    I18nControlAbstract m_ic;
    JLabel label;
    JTextField tf_port;
    JButton bt_select;
    DataAccessPlugInBase m_da;
    JPanel panel;
    JButton help_button;
    JList data_list;
    boolean was_action = false;
    
    int connection_protocol_type = 0;
    
    /**
     * Constructor 
     * 
     * @param parent
     * @param da
     * @param protocol_type
     */
    public CommunicationPortSelector(JDialog parent, DataAccessPlugInBase da, int protocol_type)
    {
        super(parent, true);
     
        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
        this.connection_protocol_type = protocol_type;

        if (checkNative())
        {
            this.m_da.addComponent(this);
            ATSwingUtils.initLibrary();
            
            init();
            this.setVisible(true);
        }
        
        
        
    }

    
    private boolean checkNative()
    {
        if (this.connection_protocol_type==ConnectionProtocols.PROTOCOL_SERIAL_USBBRIDGE)
        {
            if (!m_da.checkNativeLibrary("rxtxSerial"))
            {
                JOptionPane.showMessageDialog(this,
                    String.format(m_ic.getMessage("NO_BINARY_PART_FOUND"), "Rxtx", OSUtil.getShortOSName(), "rxtxSerial"),
                    m_ic.getMessage("ERROR") + ": Rxtx", 
                    JOptionPane.ERROR_MESSAGE, 
                    null);
                
                return false;
            }
        }
        
        return true;
        
    }
    
    /**
     * Init
     */
    public void init()
    {
        this.setLayout(null);
        this.setBounds(25, 115, 320, 300);
        
        panel = new JPanel();
        panel.setBounds(0, 0, 350, 400);
        panel.setLayout(null);
        this.getContentPane().add(panel);
        
        
        int st_y = initType() + 25;
        
        //125, 85
        
        label = ATSwingUtils.getLabel(m_ic.getMessage(getProtocolParameterName()) + ":", 20, 20, 300, 25, panel);
        

        
        
        
        //getAllAvailablePortsString()
        
        /*
        label = new JLabel(m_ic.getMessage("COMMUNICATION_PORT") + ":");
        label.setBounds(0, 0, 150, 25);
        this.add(label);
        
        tf_port = new JTextField();
        tf_port.setBounds(160, 0, 80, 25);
        tf_port.setEditable(false);
        this.add(tf_port);
        */
       
        
        //set the buttons up...
        JButton button = new JButton("  " + m_ic.getMessage("OK"));
        //okButton.setPreferredSize(dim);
        button.setIcon(m_da.getImageIcon_22x22("ok.png", this));
        button.setActionCommand("ok");
        button.setFont(m_da.getFont(DataAccessPlugInBase.FONT_NORMAL));
        button.setBounds(20, st_y, 110, 25);
        button.addActionListener(this);
        panel.add(button);

        button = new JButton("  " +m_ic.getMessage("CANCEL"));
        //cancelButton.setPreferredSize(dim);
        button.setIcon(m_da.getImageIcon_22x22("cancel.png", this));
        button.setActionCommand("cancel");
        button.setFont(m_da.getFont(DataAccessPlugInBase.FONT_NORMAL));
        button.setBounds(140, st_y, 110, 25);
        button.addActionListener(this);
        panel.add(button);
        
        help_button = m_da.createHelpButtonByBounds(260, st_y, 30, 25, this);
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
        switch(this.connection_protocol_type)
        {
            case ConnectionProtocols.PROTOCOL_MASS_STORAGE_XML:
            case ConnectionProtocols.PROTOCOL_SERIAL_USBBRIDGE:
            case ConnectionProtocols.PROTOCOL_BLUETOOTH_SERIAL:
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

            Vector<String> elems = getDataForList();
            
            
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
            
        }
        );
        
        JScrollPane scr = new JScrollPane(data_list);
        scr.setBounds(30, 50, 220, 100);
        
        panel.add(scr);
        
        return 150;
        
    }
    
    
    /**
     * Check If Item Selected
     * 
     * @return
     */
    public boolean checkIfItemSelected()
    {
        // New_Item_Edit
        switch(this.connection_protocol_type)
        {
            case ConnectionProtocols.PROTOCOL_MASS_STORAGE_XML:
            case ConnectionProtocols.PROTOCOL_SERIAL_USBBRIDGE:
            case ConnectionProtocols.PROTOCOL_BLUETOOTH_SERIAL:
                {
                    return (this.data_list.getSelectedIndex()>-1);
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
        switch(this.connection_protocol_type)
        {
            case ConnectionProtocols.PROTOCOL_MASS_STORAGE_XML:
            case ConnectionProtocols.PROTOCOL_SERIAL_USBBRIDGE:
            case ConnectionProtocols.PROTOCOL_BLUETOOTH_SERIAL:
                {
                    return ((String)this.data_list.getSelectedValue());
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
        switch(this.connection_protocol_type)
        {
            case ConnectionProtocols.PROTOCOL_MASS_STORAGE_XML:
            {
                return "SELECT_MASS_STORAGE_DRIVE";
            }
            case ConnectionProtocols.PROTOCOL_BLUETOOTH_SERIAL:
            case ConnectionProtocols.PROTOCOL_SERIAL_USBBRIDGE:
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
        switch(this.connection_protocol_type)
        {
            case ConnectionProtocols.PROTOCOL_MASS_STORAGE_XML:
            case ConnectionProtocols.PROTOCOL_SERIAL_USBBRIDGE:
            case ConnectionProtocols.PROTOCOL_BLUETOOTH_SERIAL:
                {
                    return "SELECT_ITEM_OR_CANCEL"; 
                }
            default:
                return "";
        }
        
    }
    
    
    
    protected Vector<String> getDataForList()
    {
        // New_Item_Edit
        if (this.connection_protocol_type == ConnectionProtocols.PROTOCOL_SERIAL_USBBRIDGE)
        {
            try
            {
                return SerialProtocol.getAllAvailablePortsString();
            }
            catch(Exception ex)
            {
                System.out.println("Exception: " + ex);
                return new Vector<String>();
            }
            
            
        }
        else if (this.connection_protocol_type == ConnectionProtocols.PROTOCOL_MASS_STORAGE_XML)
        {
            File[] fls = File.listRoots();
            
            Vector<String> drives = new Vector<String>();
            
            for(int i=0; i<fls.length; i++)
            {
                drives.add(fls[i].toString());
            }
            
            if (!System.getProperty("os.name").contains("Win"))
            {
                // non windows system, will load data from /mnt and /media and /Volumes
                String[] rts = { "/mnt", "/media", "/Volumes" };
                
                for(int i=0; i<rts.length; i++)
                {
                    File f = new File(rts[i]);
                    
                    if (f.exists())
                    {
                        File[] f2 = f.listFiles();
                        
                        for(int j=0; j<f2.length; j++)
                        {
                            if (f2[j].isDirectory())
                            {
                                drives.add(f2[j].toString());
                            }
                        }
                    }
                }
                
                
                
            }
            
            
            
            return drives;
        }
        else if (this.connection_protocol_type == ConnectionProtocols.PROTOCOL_BLUETOOTH_SERIAL)
        {
            try
            {
                return BlueToothProtocol.getAllAvailablePortsString();
            }
            catch(Exception ex)
            {
                System.out.println("Exception: " + ex);
                return new Vector<String>();
            }
            
            
        }
        else
            return null;
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
                JOptionPane.showMessageDialog(this, m_ic.getMessage(getNotFilledError()), m_ic.getMessage("ERROR"), JOptionPane.ERROR_MESSAGE);
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
    // ******              HelpCapable Implementation             *****
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
        //return m_da.getDeviceConfigurationDefinition().getHelpPrefix() + "Configuration_PortSelector";
        return "DeviceTool_Configuration_PortSelector";
    }
    
    
}
