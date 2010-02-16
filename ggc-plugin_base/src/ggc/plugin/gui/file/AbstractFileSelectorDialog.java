package ggc.plugin.gui.file;

import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;

public abstract class AbstractFileSelectorDialog extends JDialog implements ActionListener, HelpCapable 
{

    private static final long serialVersionUID = -7597376030031612506L;
    JButton help_button = null;
    protected DataAccessPlugInBase m_da = null;
    protected I18nControlAbstract m_ic = null;
    protected JDialog dialog_parent = null;
    protected DeviceDataHandler m_ddh = null;
    
    
    
    
    
//    public abstract int[] getPanelSizes();
    
    
    public AbstractFileSelectorDialog(DataAccessPlugInBase da, DeviceDataHandler ddh, JDialog parent)
    {
        super(parent);
        this.dialog_parent = parent;
        this.m_ddh = ddh;
        this.m_da = da;
        m_ic = da.getI18nControlInstance();
        init();
        setSize(getSize());
        m_da.centerJDialog(this, this.dialog_parent);
        this.setVisible(true);
    }
    
    public abstract void init();
    
    
    public abstract Dimension getSize();
    
    
    public Component getComponent() 
    {
        return this;
    }

    public JButton getHelpButton() 
    {
        return this.help_button;
    }
    
    
}
