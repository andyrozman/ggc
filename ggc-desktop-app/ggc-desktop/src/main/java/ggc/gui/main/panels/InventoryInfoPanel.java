package ggc.gui.main.panels;

import java.awt.*;

import javax.swing.*;

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
 *  Filename:     InventoryInfoPanel
 *  Description:  Panel for displaying stocks
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class InventoryInfoPanel extends AbstractInfoPanel
{

    private static final long serialVersionUID = -6551579241451753792L;


    /**
     * Constructor
     */
    public InventoryInfoPanel()
    {
        super("INVENTORY_TITLE");
        init();
        refreshInfo();
    }


    private void init()
    {
        setLayout(new GridLayout(0, 2));
        add(new JLabel(i18nControl.getMessage("STOCK_DATA_HERE") + ":"));
        add(new JLabel(i18nControl.getMessage("STO_WILL_BE_FOUND_HERE") + "..." + i18nControl.getMessage("NOT_YET")));
    }


    /**
     * Do Refresh - This method can do Refresh
     */
    @Override
    public void doRefresh()
    {
    }


    @Override
    public InfoPanelType getPanelType()
    {
        return InfoPanelType.Inventory;
    }

}
