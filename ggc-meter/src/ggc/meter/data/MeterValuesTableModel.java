package ggc.meter.data;

/*
 * GGC - GNU Gluco Control
 * 
 * A pure java app to help you manage your diabetes.
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename: GlucoTableModel.java Purpose: The Model behind the "readFromMeter"
 * Table. It is the bridge between the table and the data behind.
 * 
 * Author: schultd
 */

import ggc.core.db.hibernate.meter.GlucoValueH;
import ggc.meter.gui.MeterDisplayDataDialog;
import ggc.meter.output.OutputUtil;
import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.table.AbstractTableModel;

public class MeterValuesTableModel extends AbstractTableModel // implements
                                                              // GlucoValueEventListener
{

    private I18nControl m_ic = I18nControl.getInstance();
    // x private DataAccessMeter m_da = DataAccessMeter.getInstance();

    // GlucoValues dayData;

    ArrayList<MeterValuesEntry> dl_data;
    ArrayList<MeterValuesEntry> displayed_dl_data;
    
    Hashtable<String,GlucoValueH> old_data = null;

    // GGCProperties props = GGCProperties.getInstance();

    int current_filter = MeterDisplayDataDialog.FILTER_NEW_CHANGED;

    // public String status_icon_name

    private String[] column_names = { m_ic.getMessage("DATETIME"), m_ic.getMessage("BG_MMOLL"),
                                     m_ic.getMessage("BG_MGDL"), m_ic.getMessage("STATUS"), m_ic.getMessage(""), };

    public MeterValuesTableModel()
    {
        this.displayed_dl_data = new ArrayList<MeterValuesEntry>();
        this.dl_data = new ArrayList<MeterValuesEntry>();
        // this.dayData = dayData;
        fireTableChanged(null);
        // dayData.addGlucoValueEventListener(this);
    }

    public int getColumnCount()
    {
        return 5;
    }

    public boolean isBoolean(int column)
    {
        if (column == 4)
            return true;
        else
            return false;
    }

    public boolean isEditableColumn(int column)
    {
        if (column == 4)
            return true;
        else
            return false;

    }

    public int getColumnWidth(int column, int width)
    {
        if (column == 0)
        {
            return 100; // third column is bigger
        }
        else
        {
            return 50;
        }

    }

    public void selectAll()
    {
        setSelectors(true);
    }

    public void deselectAll()
    {
        setSelectors(false);
    }

    private void setSelectors(boolean select)
    {
        for (int i = 0; i < this.displayed_dl_data.size(); i++)
        {
            this.displayed_dl_data.get(i).checked = select;
        }

        this.fireTableDataChanged();
    }

    
    public void setFilter(int filter)
    {
        if (this.current_filter==filter)
            return;
        
        this.current_filter = filter;
        
        this.displayed_dl_data.clear();
        
        for(int i=0; i< this.dl_data.size(); i++)
        {
            MeterValuesEntry mve = this.dl_data.get(i);
            
            if (shouldBeDisplayed(mve.getStatus()))
            {
                this.displayed_dl_data.add(mve);
            }
        }
        
        this.fireTableDataChanged();
        
    }
    
    
    

    public boolean shouldBeDisplayed(int status)
    {
        switch (this.current_filter)
        {
            case MeterDisplayDataDialog.FILTER_ALL:
                return true;
                
            case MeterDisplayDataDialog.FILTER_NEW:
                return (status == MeterValuesEntry.STATUS_NEW);
    
            case MeterDisplayDataDialog.FILTER_CHANGED:
                return (status == MeterValuesEntry.STATUS_CHANGED);
                
            case MeterDisplayDataDialog.FILTER_EXISTING:
                return (status == MeterValuesEntry.STATUS_OLD);
                
            case MeterDisplayDataDialog.FILTER_UNKNOWN:
                return (status == MeterValuesEntry.STATUS_UNKNOWN);
                
            case MeterDisplayDataDialog.FILTER_NEW_CHANGED:
                return ((status == MeterValuesEntry.STATUS_NEW) ||
                        (status == MeterValuesEntry.STATUS_CHANGED));
                
            case MeterDisplayDataDialog.FILTER_ALL_BUT_EXISTING:
                return (status != MeterValuesEntry.STATUS_OLD);
        }
        return false;

    }

    public int getRowCount()
    {
        return this.displayed_dl_data.size();
    }

    public Object getValueAt(int row, int column)
    {
        MeterValuesEntry mve = this.displayed_dl_data.get(row);

        switch (column)
        {
        case 0:
            return mve.getDateTime().getDateTimeString();

        case 1:
            return mve.getBGValue(DataAccessMeter.BG_MMOL);

        case 2:
            return mve.getBGValue(DataAccessMeter.BG_MGDL);

        case 3:
            return new Integer(mve.getStatus());

        case 4:
            return new Boolean(mve.getCheched());

        default:
            return "";
        }

        // Object o = dayData.getValueAt(row, column);
        /*
         * if (o != null && column == 0) { SimpleDateFormat sdf = new
         * SimpleDateFormat("dd.MM.yyyy HH:mm"); return sdf.format(o); }
         * 
         * return o;
         */
    }

    public void addEntry(MeterValuesEntry mve)
    {
        processMeterValuesEntry(mve);
        this.dl_data.add(mve);
        
        if (this.shouldBeDisplayed(mve.status))
        {
            this.displayed_dl_data.add(mve);
        }
        this.fireTableDataChanged();
    }

    
    public void processMeterValuesEntry(MeterValuesEntry mve)
    {
        //System.out.println("processMeterValuesEntry");
        if (old_data!=null)
        {
            //System.out.println("oldData != null");
            long dt = mve.getDateTime().getATDateTimeAsLong();
            
            //System.out.println("Dt='" + dt + "'");
            
            //System.out.println("Found: " + old_data.containsKey("" + dt));
            
            
            if (!old_data.containsKey("" + dt))
            {
            //    System.out.println("not Contains");
                mve.status = MeterValuesEntry.STATUS_NEW;
            }
            else
            {
                
             //   System.out.println("Found !!!");
                
                GlucoValueH gvh = old_data.get("" + dt);
                  
                int vl = Integer.parseInt(mve.getBGValue(OutputUtil.BG_MGDL));
                
                //if (((vl-1) >= gvh.getBg()) && (gvh.getBg() <= (vl+1)))
                if (gvh.getBg()==vl)
                    mve.status = MeterValuesEntry.STATUS_OLD;
                else
                    mve.status = MeterValuesEntry.STATUS_CHANGED;
                    
                //gvh.getBg()
            }
        }
        else
        {
            System.out.println("oldData == null");

            mve.status = MeterValuesEntry.STATUS_NEW;
        }
    }
    
    
    public ArrayList<MeterValuesEntry> getCheckedEntries()
    {
        return null;
    }
    
    
    @Override
    public String getColumnName(int column)
    {
        return column_names[column];
    }

    @Override
    public Class<?> getColumnClass(int c)
    {
        Object o = getValueAt(0, c);
        if (o != null)
            return o.getClass();
        else
            return null;
        // return getValueAt(0,c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col)
    {
        if (col == 4)
            return true;
        else
            return false;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column)
    {
        Boolean b = (Boolean) aValue;
        this.displayed_dl_data.get(row).checked = b.booleanValue();
        // System.out.println("set Value: rw=" + row + ",column=" + column +
        // ",value=" + aValue);
        // dayData.setValueAt(aValue, row, column);
        // fireTableChanged(null);
    }

    public void setOldValues(Hashtable<String,GlucoValueH> data)
    {
        this.old_data = data;
        
        System.out.println(this.old_data);
     
        System.out.println(this.old_data.keys());
    }
    
    
    /*
     * @see event.GlucoValueEventListener#glucoValuesChanged(GlucoValueEvent)
     */
    /*
     * public void glucoValuesChanged(GlucoValueEvent event) { switch
     * (event.getType()) { case GlucoValueEvent.INSERT:
     * fireTableRowsInserted(event.getFirstRow(), event.getLastRow()); break;
     * case GlucoValueEvent.DELETE: fireTableRowsDeleted(event.getFirstRow(),
     * event.getLastRow()); break; case GlucoValueEvent.UPDATE:
     * fireTableCellUpdated(event.getFirstRow(), event.getColumn()); break; } }
     */

    /*
     * Returns the dayData.
     * 
     * @return GlucoValues
     */
    /*
     * public GlucoValues getDayData() { return dayData; }
     */

}
