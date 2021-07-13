package ggc.plugin.data;

import java.awt.*;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import com.atech.utils.ATSwingUtils;

import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     DeviceValuesConfigTable
 *  Description:  Table for Device Config Values
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DeviceValuesConfigTable extends JTable
{

    private static final long serialVersionUID = 4105030567099843263L;

    protected DeviceValuesConfigTableModel model = null;
    protected DataAccessPlugInBase m_da;

    Color backgroundGroup = new Color(12, 209, 97);
    Font normal, bold;


    /**
     * Constructor
     *  
     * @param da 
     */
    public DeviceValuesConfigTable(DataAccessPlugInBase da)
    {
        super();
        m_da = da;

        init();
    }


    /**
     *  Constructor
     *  
     * @param da 
     * @param model 
     */
    public DeviceValuesConfigTable(DataAccessPlugInBase da, DeviceValuesConfigTableModel model)
    {
        super(model);
        m_da = da;
        this.model = model;

        init();
    }


    private void init()
    {
        ATSwingUtils.initLibrary();

        this.setColumnSelectionAllowed(false);
        this.setRowSelectionAllowed(false);
        this.setCellSelectionEnabled(false);
        this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        normal = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL);
        bold = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD);
    }


    /**
     * Create Default Table Header
     * 
     * @see javax.swing.JTable#createDefaultTableHeader()
     */
    @Override
    protected JTableHeader createDefaultTableHeader()
    {
        JTableHeader header = super.createDefaultTableHeader();
        header.setFont(header.getFont().deriveFont(12.0f).deriveFont(Font.BOLD));

        return header;
    }


    public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
    {
        Component c = super.prepareRenderer(renderer, row, column);

        if (model != null)
        {
            int index = (Integer) model.getIndex(row);

            if (index == 1)
            {
                c.setBackground(backgroundGroup);
                c.setFont(bold);
            }
            else
            {
                c.setBackground(Color.white);
                c.setFont(normal);
            }
        }

        return c;
    }

}
