package ggc.plugin.cfg;

import ggc.plugin.protocol.ConnectionProtocols;
import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;


public class CommunicationPortComponent extends JPanel implements ActionListener
{
    /* When adding new protocol search for 'New_Item_Edit' entries. There you need to extend everything */
    // New_Item_Edit

    private static final long serialVersionUID = 6490997313283293063L;
    
    I18nControlAbstract m_ic;
    JLabel label;
    JTextField tf_port;
    JButton bt_select;
    
    int m_type = 0;
    JDialog parent;
    DataAccessPlugInBase m_da;
    
    public CommunicationPortComponent(DataAccessPlugInBase da, JDialog parent)
    {
        super();
     
        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
        this.parent = parent;
        
        ATSwingUtils.initLibrary();
        
        init();
    }
    
    
    public void init()
    {
        this.setLayout(null);
        this.setBounds(25, 115, 370, 25);
        
        //125, 85
        
        label = ATSwingUtils.getLabel(m_ic.getMessage("COMMUNICATION_PORT") + ":", 0, 0, 150, 25, this, ATSwingUtils.FONT_NORMAL_BOLD);
        
        tf_port = ATSwingUtils.getTextField("", 145, 0, 110, 25, this);
        tf_port.setEditable(false);

        
        
        /*
        label = new JLabel(m_ic.getMessage("COMMUNICATION_PORT") + ":");
        label.setBounds(0, 0, 150, 25);
        this.add(label);
        
        tf_port = new JTextField();
        tf_port.setBounds(160, 0, 80, 25);
        tf_port.setEditable(false);
        this.add(tf_port);
        */
       
        bt_select = new JButton(m_ic.getMessage("SELECT"));
        bt_select.setBounds(270, 0, 100, 25);
        bt_select.addActionListener(this);
        this.add(bt_select);
        
        
        
    }

    public void setCommunicationPort(String val)
    {
        this.tf_port.setText(val);
        this.tf_port.setToolTipText(val);
    }
    
    public String getCommunicationPort()
    {
        return this.tf_port.getText();
    }
    

    public void actionPerformed(ActionEvent arg0)
    {
        /*if (m_type!=0)
        {
            System.out.println("Set action: " + m_type);
        }
        else*/ 
        if ((m_type==ConnectionProtocols.PROTOCOL_SERIAL_USBBRIDGE) ||
            (m_type==ConnectionProtocols.PROTOCOL_MASS_STORAGE_XML))
        {
            CommunicationPortSelector cps = new CommunicationPortSelector(this.parent, m_da, this.m_type);
            if (cps.wasAction())
            {
                this.tf_port.setText(cps.getSelectedItem());
            }
            
        }
        
    }
    

    public void setProtocol(int protocol)
    {
        // New_Item_Edit
        this.m_type = protocol;
        switch(protocol)
        {
            case ConnectionProtocols.PROTOCOL_NONE:
            {
                label.setText(m_ic.getMessage("COMMUNICATION_PORT") + ":");
                setCommunicationPort("");
                this.bt_select.setEnabled(false);
            } break;
            
            case ConnectionProtocols.PROTOCOL_SERIAL_USBBRIDGE:
            {
                label.setText(m_ic.getMessage("SERIAL_PORT") + ":");
                setCommunicationPort("");
                this.bt_select.setEnabled(true);
            } break;
            
            case ConnectionProtocols.PROTOCOL_MASS_STORAGE_XML:
            {
                label.setText(m_ic.getMessage("MASS_STORAGE_DRIVE") + ":");
                setCommunicationPort("");
                this.bt_select.setEnabled(true);
            } break;
            
            
            default:
            {
                label.setText(m_ic.getMessage("COMMUNICATION_PORT") + ":");
                setCommunicationPort("N/A");
                this.bt_select.setEnabled(false);
            }
            
        }
        
        
        
    }
    
    
    
}
