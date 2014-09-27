package ggc.gui.panels.info;

import ggc.core.util.DataAccess;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.atech.i18n.I18nControlAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     AbstractInfoPanel  
 *  Description:  Abstract class for info panels
 *
 *  Author: schultd
 *          andyrozman {andy@atech-software.com}  
 */

public abstract class AbstractInfoPanel extends JPanel
{
    private static final long serialVersionUID = -1104646965456304940L;
    protected DataAccess m_da = DataAccess.getInstance();
    protected I18nControlAbstract m_ic = m_da.getI18nControlInstance();
    protected boolean first_refresh = true;

    /**
     * Constructor
     * 
     * @param title
     */
    public AbstractInfoPanel(String title)
    {
        super();
        setBorder(BorderFactory.createTitledBorder(this.m_ic.getMessage(title) + ":"));
        setOpaque(false);
    }

    /**
     * Constructor
     * 
     * @param title
     * @param border
     */
    public AbstractInfoPanel(String title, boolean border)
    {
        super();
        if (border)
        {
            setBorder(BorderFactory.createTitledBorder(this.m_ic.getMessage(title) + ":"));
        }
        setOpaque(false);
    }

    /**
     * Set Title
     * 
     * @param title
     */
    public void setTitle(String title)
    {
        TitledBorder tb = (TitledBorder) this.getBorder();
        tb.setTitle(title);
    }

    /**
     * Refresh Information 
     */
    public void refreshInfo()
    {
        doRefresh();
    }

    /**
     * Invalidate First Refresh
     */
    public void invalidateFirstRefresh()
    {
        this.first_refresh = true;
    }

    /**
     * Get Tab Name
     * 
     * @return name as string
     */
    public abstract String getTabName();

    /**
     * Get Panel Id
     * 
     * @return id of panel
     */
    public abstract int getPanelId();

    /** 
     * Is Tab Checked
     * 
     * @param check_mask 
     * @return 
     */
    public boolean isPanelTaged(int check_mask)
    {
        return (check_mask & this.getPanelId()) == this.getPanelId();
    }

    /**
     * RefreshInfo - Refresh info by name 
     *  
     * @param name
     */
    public void refreshInfo(String name)
    {
        if (this.getTabName().equals(name))
        {
            doRefresh();
        }
    }

    /**
     * RefreshInfo - Refresh info by mask 
     *  
     * @param mask
     */
    public void refreshInfo(int mask)
    {
        if (this.isPanelTaged(mask))
        {
            doRefresh();
        }
    }

    /**
     * Do Refresh - This method can do Refresh
     */
    public abstract void doRefresh();

}
