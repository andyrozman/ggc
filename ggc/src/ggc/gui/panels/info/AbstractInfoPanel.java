package ggc.gui.panels.info;

import javax.swing.JPanel;
import javax.swing.BorderFactory;

import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;

import javax.swing.border.TitledBorder;

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
    protected I18nControl m_ic = I18nControl.getInstance();
    protected DataAccess m_da = DataAccess.getInstance();
    protected boolean first_refresh = true;

    /**
     * Constructor
     * 
     * @param title
     */
    public AbstractInfoPanel(String title)
    {
        super();
        setBorder(BorderFactory.createTitledBorder(title));
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
            setBorder(BorderFactory.createTitledBorder(title));
        setOpaque(false);
    }
    
    
    
    /**
     * Set Title
     * 
     * @param title
     */
    public void setTitle(String title)
    {
        TitledBorder tb = (TitledBorder)this.getBorder();
        tb.setTitle(title);
    }

    /**
     * Refresh Information 
     */
    public abstract void refreshInfo();

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
     * RefreshInfo - Refresh info by id 
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
     * Do Refresh - This method can do Refresh
     */
    public abstract void doRefresh();
    
    
}
