package ggc.plugin.data;


import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import com.atech.graphics.components.MultiLineTooltipModel;


/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     DeviceValuesTable  
 *  Description:  Table for Device values
 * 
 *  Author: Andy {andy@atech-software.com}
 */


//IMPORTANT NOTICE: 
//This class is not implemented yet, all existing methods should be rechecked (they were copied from similar 
//class, with different type of data. Trying to find a way to use super class instead of this.


public class DeviceValuesConfigTable extends JTable //implements TableModelListener
{

    private static final long serialVersionUID = 4105030567099843263L;
    protected DeviceValuesTableModel model = null;
	protected DataAccessPlugInBase m_da;

	
    /**
     * Constructor
     *  
     * @param da 
     */
    public DeviceValuesConfigTable(DataAccessPlugInBase da)
    {
        super();
        m_da = da;
        this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        //this.setDefaultRenderer(Boolean.class, new CheckCellRenderer());
        //this.setDefaultRenderer(Integer.class, new StatusCellRenderer());

        this.setColumnSelectionAllowed(false);
        this.setRowSelectionAllowed(false);
        
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
        this.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        //this.setDefaultRenderer(Boolean.class, new CheckCellRenderer());
        //this.setDefaultRenderer(Integer.class, new StatusCellRenderer());
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

        //header.setPreferredSize(new Dimension(100, 20));
        header.setFont(header.getFont().deriveFont(12.0f).deriveFont(Font.BOLD));

        return header;
    }

    /**
     * Set Model
     * @param model
     */
    public void setModel(DeviceValuesTableModel model)
    {
        super.setModel(model);
        this.model = model;
        //createGlucoTable(model);

        model.addTableModelListener(new TableModelListener()
        {
            public void tableChanged(TableModelEvent e)
            {
                
             /*   int row = e.getFirstRow();
                int column = e.getColumn();
                TableModel model = (TableModel)e.getSource();
                String columnName = model.getColumnName(column);
                Object data = model.getValueAt(row, column);                
               */ 
                /* FIX
                if (model.getRowCount() == 0 && deleteRowAction.isEnabled())
                    deleteRowAction.setEnabled(false);
                if (model.getRowCount() > 0 && !deleteRowAction.isEnabled())
                    deleteRowAction.setEnabled(true);
                    */
            }
        });
    }

    
    /** 
     * Get ToolTip Text
     */
    public String getToolTipText(MouseEvent e) 
    {
        //Object source = e.getSource();
        String tip = null;
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);
        int realColumnIndex = convertColumnIndexToModel(colIndex);

        
        
        
        if (model instanceof MultiLineTooltipModel)
        {
            tip = ((MultiLineTooltipModel)model).getToolTipValue(rowIndex, colIndex);
        }
        else
        {
            Object o = getValueAt(rowIndex, realColumnIndex);
            
            if (o instanceof String)
            {
                tip = (String)getValueAt(rowIndex, realColumnIndex);
            }
            else
                tip = o.toString();
        }

        if ((tip!=null) && (tip.length()==0))
            tip = null;
        
        return tip;
    }
    
    
    
    /**
     * Create Values Table
     * 
     * @param da 
     * @return
     */
    public JComponent createValuesTable(DataAccessPlugInBase da)
    {
        DeviceValuesTable table = new DeviceValuesTable(da, model);
        JScrollPane scroller = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        toolBar.setFloatable(false);
        //UIUtilities.addToolBarButton(toolBar, addRowAction);
        //UIUtilities.addToolBarButton(toolBar, deleteRowAction);
        //toolBar.add(addRowAction);
        //toolBar.add(deleteRowAction);

        TableColumn column = null;
        for (int i = 0; i < m_da.getColumnsWidthTable().length; i++) 
        {
            column = table.getColumnModel().getColumn(i);
            
            column.setPreferredWidth(m_da.getColumnsWidthTable()[i]);
            
            /*
            if (i == 0) 
            {
                column.setPreferredWidth(100); //third column is bigger
            } 
            else 
            {
                column.setPreferredWidth(50);
            }*/
        }        

        JPanel container = new JPanel(new BorderLayout());
        container.add(toolBar, "North");
        container.add(scroller, "Center");

        return container;
    }

    /*
    public static JComponent createMeterValuesTable(final MeterValuesTable table)
    {
        //MeterValuesTable table = new MeterValuesTable(model);
        JScrollPane scroller = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        final AddRowAction addRowAction = new AddRowAction(table);
        final DeleteRowAction deleteRowAction = new DeleteRowAction(table);

        JToolBar toolBar = new JToolBar();
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT, 1, 1));
        toolBar.setFloatable(false);
        toolBar.add(addRowAction);
        toolBar.add(deleteRowAction);
        //UIUtilities.addToolBarButton(toolBar, addRowAction);
        //UIUtilities.addToolBarButton(toolBar, deleteRowAction);
        //toolBar.add(addRowAction);
        //toolBar.add(deleteRowAction);

        

        JPanel container = new JPanel(new BorderLayout());
        container.add(toolBar, "North");
        container.add(scroller, "Center");

        return container;
    }
    */
    
    
    
    
    
    
}
