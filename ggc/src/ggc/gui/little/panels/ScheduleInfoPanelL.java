package ggc.gui.little.panels;

import ggc.core.util.I18nControl;
import ggc.gui.panels.info.AbstractInfoPanel;
import ggc.gui.panels.info.InfoPanelsIds;

import java.awt.GridLayout;

import javax.swing.JLabel;

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
 *  Filename:     ScheduleInfoPanelL
 *  Description:  This Panel contains information on your Schedule. Like your
 *                next check, when you see your doctor again, ...
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class ScheduleInfoPanelL extends AbstractInfoPanel
{
    private static final long serialVersionUID = -4785287187015409811L;

    /**
     * Constructor
     */
    public ScheduleInfoPanelL()
    {
        super(I18nControl.getInstance().getMessage("SCHEDULE"));
        setLayout(new GridLayout(0, 1));
        init();
        refreshInfo();
    }

    private void init()
    {
        add(new JLabel());
        add(new JLabel(m_ic.getMessage("YOUR_NEXT_APPOINTMENT")+":"));
        add(new JLabel(m_ic.getMessage("APP_WILL_BE_FOUND_HERE")+ "..." + m_ic.getMessage("NOT_YET")));
        add(new JLabel());
    }

    /**
     * Refresh Information 
     */
    @Override
    public void refreshInfo()
    {
    }
    
    /**
     * Get Tab Name
     * 
     * @return name as string
     */
    public String getTabName()
    {
        return "ScheduleInfo";
    }

    
    /**
     * Do Refresh - This method can do Refresh
     */
    public void doRefresh()
    {
    }

    
    /**
     * Get Panel Id
     * 
     * @return id of panel
     */
    @Override
    public int getPanelId()
    {
        return InfoPanelsIds.INFO_PANEL_NONE;
    }
    
    
}
