package ggc.core.data.cfg;


/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: ConfigCellRenderer
 *  Purpose:  ConfigCellRenderer is class for rendering Config cells
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */


import ggc.core.util.DataAccess;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JLabel;


public class ConfigCellRenderer extends DefaultListCellRenderer
{

    DataAccess da = DataAccess.getInstance();

    //final static ImageIcon longIcon = new ImageIcon("long.gif");
    //final static ImageIcon shortIcon = new ImageIcon("short.gif");

    /* This is the only method defined by ListCellRenderer.  We just
     * reconfigure the Jlabel each time we're called.
     */
    public Component getListCellRendererComponent(
                                                 JList list,
                                                 Object value,   // value to display
                                                 int index,      // cell index
                                                 boolean iss,    // is the cell selected
                                                 boolean chf)    // the list and the cell have the focus
    {
        /* The DefaultListCellRenderer class will take care of
         * the JLabels text property, it's foreground and background
         * colors, and so on.
         */
        super.getListCellRendererComponent(list, value, index, iss, chf);

        /* We additionally set the JLabels icon property here.
         */
        //String s = value.toString();

        //DataAccess da = DataAccess.getInstance();

        //int idx = da.getSelectedConfigTypePart(s);

        setIcon(da.config_icons[index]);

        this.setHorizontalTextPosition(JLabel.CENTER);
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setVerticalTextPosition(JLabel.BOTTOM);

        return this;
    }
}
