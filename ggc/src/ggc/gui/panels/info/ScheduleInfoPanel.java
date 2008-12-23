package ggc.gui.panels.info;

import ggc.core.util.I18nControl;

import javax.swing.*;
import java.awt.*;

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
 *  Filename:     ScheduleInfoPanel  
 *  Description:  Panel for displaying Schedule info
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class ScheduleInfoPanel extends AbstractInfoPanel
{

    private static final long serialVersionUID = 3297655715644239482L;

    /**
     * Constructor
     */
    public ScheduleInfoPanel()
    {
        super(I18nControl.getInstance().getMessage("SCHEDULE"));
        setLayout(new GridLayout(0, 2));
        init();
        refreshInfo();
    }

    private void init()
    {
        add(new JLabel(m_ic.getMessage("YOUR_NEXT_APPOINTMENT")+":"));
        add(new JLabel(m_ic.getMessage("APP_WILL_BE_FOUND_HERE")+ "..." + m_ic.getMessage("NOT_YET")));
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
    
    
}
