package ggc.plugin.list;

import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;

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
 *  Filename:     BaseListMainPanel
 *  Description:  Base List Main Panel
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class BaseListMainPanel extends BaseListAbstractPanel implements HelpCapable //JPanel
{
    private static final long serialVersionUID = -740039184514067065L;
    I18nControlAbstract m_ic = null;
    DataAccessPlugInBase m_da = null;

    Font font_big, font_normal, font_normal_b;
    JLabel label;
    JButton button, help_button;

    BaseListDialog m_dialog = null;

    

    /**
     * Constructor
     * 
     * @param dia
     */
    public BaseListMainPanel(BaseListDialog dia)
    {

        super();

        m_dialog = dia;
        m_da = dia.m_da;
        m_ic = m_da.getI18nControlInstance();

        font_big = m_da.getFont(DataAccessPlugInBase.FONT_BIG_BOLD);
        font_normal_b = m_da.getFont(DataAccessPlugInBase.FONT_NORMAL_BOLD);
        font_normal = m_da.getFont(DataAccessPlugInBase.FONT_NORMAL);

        createPanel();

    }


    /**
     * Create Panel
     */
    public void createPanel()
    {

        this.setSize(460, 520);
        this.setLayout(null);

        Font fnt_18 = new Font("Times New Roman", Font.PLAIN, 14);

        label = new JLabel(String.format(m_ic.getMessage("DEVICE_LIST_WEB"), m_ic.getMessage("DEVICE_NAME_BIG")));
        label.setBounds(0, 35, 500, 40);  // 520
        label.setFont(font_big); 
        label.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(label, null);


        label = new JLabel(m_da.getWebListerDescription());
        label.setBounds(40, 120, 400, 250);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(fnt_18); 
        this.add(label, null);

        label = new JLabel(m_ic.getMessage("LEGEND_DESC"));
        label.setBounds(40, 330, 400, 250);
        label.setVerticalAlignment(JLabel.TOP);
        label.setFont(fnt_18); 
        this.add(label, null);
        
        help_button = m_da.createHelpButtonByBounds(430, 10, 30, 30, this, DataAccessPlugInBase.FONT_NORMAL);
        help_button.setText("");
        this.add(help_button, null);
        
        m_da.enableHelp(this);
        
        return;
    }


    /**
     * Set Data
     * 
     * @see ggc.plugin.list.BaseListAbstractPanel#setData(java.lang.Object)
     */
    public void setData(Object obj)
    {
    }

    
    
    // ****************************************************************
    // ******              HelpCapable Implementation             *****
    // ****************************************************************
    
    /** 
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
        return this;
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
        System.out.println("m_da: " + m_da);
        System.out.println("DevConfDef: " + m_da.getDeviceConfigurationDefinition());
        System.out.println("Help Prefix: " + m_da.getDeviceConfigurationDefinition().getHelpPrefix());
        return m_da.getDeviceConfigurationDefinition().getHelpPrefix() + "List";
    }
    
    

}
    
    

