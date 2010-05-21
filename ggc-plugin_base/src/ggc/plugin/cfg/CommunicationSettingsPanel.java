package ggc.plugin.cfg;

import ggc.plugin.device.DeviceInterface;
import ggc.plugin.gui.DeviceSpecialConfigPanelInterface;
import ggc.plugin.util.DataAccessPlugInBase;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.atech.i18n.I18nControlAbstract;

public class CommunicationSettingsPanel extends JPanel
{
    
    int x_pos, y_pos;
    DataAccessPlugInBase m_da;
    I18nControlAbstract m_ic;
    CommunicationPortComponent comm_port_comp; 
    JDialog parent;
    DeviceInterface current_device = null;
    DeviceSpecialConfigPanelInterface special_config = null;
    int element_size = 65;
    
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
    
    
    public void init()
    {
        
        this.comm_port_comp = new CommunicationPortComponent(m_da, this.parent);
        this.add(this.comm_port_comp);        
        
    }
    
    public void setCurrentDevice(DeviceInterface dev_interface)
    {
        this.current_device = dev_interface;
        
        
        if (this.current_device==null)
        {
            this.comm_port_comp.setProtocol(0);
        }
        else
        {
            this.comm_port_comp.setProtocol(this.current_device.getConnectionProtocol());
        }
        
        
        resetLayout();
    }
    
    
    public void resetLayout()
    {
        this.special_config = null;

        if (this.current_device!=null)
        {
            if (this.current_device.hasSpecialConfig())
            {
                this.special_config = this.current_device.getSpecialConfigPanel();
            }
        }
        
        
        // FIXME reset layout
        
    }
    
    
    public void setProtocol(int protocol)
    {
        this.comm_port_comp.setProtocol(protocol);
    }
    

    public void setParameters(String param)
    {
        if (param==null)
        {
            this.comm_port_comp.setCommunicationPort(m_ic.getMessage("NOT_SET"));   
        }
        
        //this.comm_port_comp
    }
    
    public String getParameters()
    {
        return null;
    }
    
    
    public boolean areParametersSet()
    {
        return false;
    }
    
    
    
    public int getHeight()
    {
        int sz = element_size;
        
        if (this.special_config != null)
            sz += this.special_config.getHeight();
        
        return sz ;
        
    }
    
    
    
    
    
}
