package ggc.pump.gui.manual;

import ggc.plugin.data.DeviceValuesDay;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.util.DataAccessPump;
import ggc.pump.util.I18nControl;

import javax.swing.table.AbstractTableModel;

import com.atech.graphics.components.MultiLineTooltip;
import com.atech.graphics.components.MultiLineTooltipModel;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     PumpDataTableModel  
 *  Description:  Table Model for Pump Data
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class PumpDataTableModel extends AbstractTableModel implements MultiLineTooltipModel
{
    
    private static final long serialVersionUID = 412835707138372687L;

    DeviceValuesDay dayData;
    
    DataAccessPump m_da = DataAccessPump.getInstance();
    I18nControl m_ic = I18nControl.getInstance(); 

    /*
    Object objects[] = 
    { 
        new Long(0L), 
        new String(""),
        new Float(0.0d),
        new Float(0.0d),
        new Float(0.0d),
        new String(""),
        new String(""),
        new String("")
    };*/

    Object objects[] = 
    { 
        new String(""), 
        new String(""),
        new String(""),
        new String(""),
        new String(""),
        new String(""),
        new String(""),
        new String("")
    };


    private String[] column_names = { 
                                     m_ic.getMessage("TIME"),
                                     m_ic.getMessage("BASE_TYPE"),
                                     m_ic.getMessage("SUB_TYPE"),
                                     m_ic.getMessage("VALUE"),
                                     m_ic.getMessage("ADDITIONAL"),
                                     m_ic.getMessage("FOOD") };
    
    
    
    /**
     * Constructor
     * 
     * @param dayData
     */
    public PumpDataTableModel(DeviceValuesDay dayData)
    {
        setDailyValues(dayData);
    }

    /**
     * Get Daily Values
     * 
     * @return
     */
    public DeviceValuesDay getDailyValues()
    {
        return this.dayData;
    }

    /**
     * Set Daily Values
     * 
     * @param dayData
     */
    public void setDailyValues(DeviceValuesDay dayData)
    {
        this.dayData = dayData;
        //this.dayData.sort(new PumpValuesEntry());
        
        
        //System.out.println("DayData: " + dayData.getRowCount()); //.getRowCount());
        
        
        fireTableChanged(null);
    }

    /**
     * Get Column Count
     */
    public int getColumnCount()
    {
        //if (dayData == null)
        //    return 0;

        return this.column_names.length;
    }

    /**
     * Get Row Count
     */
    public int getRowCount()
    {
        if (dayData == null)
            return 0;

        return dayData.getRowCount();
    }

    /**
     * Get Value At
     */
    public Object getValueAt(int row, int column)
    {
        Object o = dayData.getValueAt(row, column);

	/*
        if (o != null && column == 0) 
        {
            return m_da.getDateTimeAsTimeString(((Long)o).longValue());
            //System.out.println("DailyStatsTableModel: " + o);
            //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            //return sdf.format(o);
        } 
      */  
        return o;
    }

    /** 
     * Get Column Name
     */
    @Override
    public String getColumnName(int column)
    {
        return this.column_names[column];
        /*if (column == 2)
            return m_da.getSettings().getIns1Abbr();
        if (column == 3)
            return m_da.getSettings().getIns2Abbr();
*/
        //..return dayData.getColumnName(column);
    }

    /**
     * Get Column Class
     */
    @Override
    public Class<?> getColumnClass(int c)
    {
        return this.objects[c].getClass();

        /*
        Object o = getValueAt(0, c);
        if (o != null)
            return o.getClass();
        else
            return null;
            */
        //return getValueAt(0,c).getClass();
    }

/*    
    protected JTableHeader createDefaultTableHeader() {
        return new JTableHeader(columnModel) {
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int index = columnModel.getColumnIndexAtX(p.x);
                int realIndex = 
                        columnModel.getColumn(index).getModelIndex();
                return columnToolTips[realIndex];
            }
        };
    }
  */  
    
    /*
    public String getToolTipText(MouseEvent e) 
    {
        String tip = null;
        java.awt.Point p = e.getPoint();
        int rowIndex = rowAtPoint(p);
        int colIndex = columnAtPoint(p);
        int realColumnIndex = convertColumnIndexToModel(colIndex);

        if (realColumnIndex == 2) { //Sport column
            tip = "This person's favorite sport to "
                   + "participate in is: "
                   + getValueAt(rowIndex, colIndex);

        } 
        else if (realColumnIndex == 4) { //Veggie column
            TableModel model = getModel();
            String firstName = (String)model.getValueAt(rowIndex,0);
            String lastName = (String)model.getValueAt(rowIndex,1);
            Boolean veggie = (Boolean)model.getValueAt(rowIndex,4);
            if (Boolean.TRUE.equals(veggie)) {
                tip = firstName + " " + lastName
                      + " is a vegetarian";
            } else {
                tip = firstName + " " + lastName
                      + " is not a vegetarian";
            }

        } else { //another column
            //You can omit this part if you know you don't 
            //have any renderers that supply their own tool 
            //tips.
            tip = super.getToolTipText(e);
        }
        return tip;
    }
    */
    
    
    
    /**
     * Is Cell Editable
     */
    @Override
    public boolean isCellEditable(int row, int col)
    {
        return false;
    }

    
    /** 
     * get ToolTip Value
     */
    public String getToolTipValue(int row, int column)
    {
        DeviceValuesEntry o = dayData.getRowAt(row);
        
        if (o instanceof MultiLineTooltip)
        {
            return ((MultiLineTooltip)o).getMultiLineToolTip(column);
        }
        else
        {
            return (String)o.getColumnValue(column);
        }
    }
    

}