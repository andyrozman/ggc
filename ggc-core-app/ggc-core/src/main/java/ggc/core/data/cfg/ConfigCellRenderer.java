package ggc.core.data.cfg;

import java.awt.*;

import javax.swing.*;

import ggc.core.enums.PropertiesDialogType;
import ggc.core.util.DataAccess;

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
 *  Filename:     ConfigCellRenderer
 *  Description:  ConfigCellRenderer is class for rendering Config cells
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class ConfigCellRenderer extends DefaultListCellRenderer
{

    private static final long serialVersionUID = -2728552153803274776L;
    DataAccess da = DataAccess.getInstance();
    PropertiesDialogType propertiesDialogType;


    public ConfigCellRenderer(PropertiesDialogType propertiesDialogType)
    {
        super();
        this.propertiesDialogType = propertiesDialogType;
    }


    @Override
    @SuppressWarnings("rawtypes")
    public Component getListCellRendererComponent(JList list, Object value, // value
                                                                            // to
                                                                            // display
            int index, // cell index
            boolean iss, // is the cell selected
            boolean chf) // the list and the cell have the focus
    {
        super.getListCellRendererComponent(list, value, index, iss, chf);
        if (propertiesDialogType == PropertiesDialogType.Standard)
            setIcon(da.config_icons[index]);
        else
            setIcon(da.config_icons[index == 1 ? 10 : 0]); // we have only two
                                                           // icons here
        this.setHorizontalTextPosition(SwingConstants.CENTER);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalTextPosition(SwingConstants.BOTTOM);

        return this;
    }
}
