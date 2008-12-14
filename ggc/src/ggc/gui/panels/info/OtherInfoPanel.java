package ggc.gui.panels.info;

import ggc.core.util.I18nControl;

import java.awt.GridLayout;

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
 *  Filename:     OtherInfoPanel  
 *  Description:  Panel for two other panels (without title)
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class OtherInfoPanel extends AbstractInfoPanel
{
    private static final long serialVersionUID = -1695058871914412588L;
    private AbstractInfoPanel panel_1; 
    private AbstractInfoPanel panel_2;
    
    
    /**
     * Constructor
     * @param p1 
     * @param p2 
     */
    public OtherInfoPanel(AbstractInfoPanel p1, AbstractInfoPanel p2)
    {
        super(I18nControl.getInstance().getMessage("OTHER_INFO"), false);
        setLayout(new GridLayout(2, 0));
        
        this.panel_1 = p1;
        this.panel_2 = p2;
        add(p1);
        add(p2);
    }


    /**
     * Refresh Information 
     */
    @Override
    public void refreshInfo()
    {
        this.panel_1.refreshInfo();
        this.panel_2.refreshInfo();
    }
}
