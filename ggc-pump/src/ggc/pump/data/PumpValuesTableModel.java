package ggc.pump.data;

import ggc.core.db.hibernate.DayValueH;
import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.DeviceValuesTableModel;
import ggc.plugin.gui.DeviceDisplayDataDialog;
import ggc.pump.util.DataAccessPump;
import ggc.pump.util.I18nControl;

import java.util.ArrayList;
import java.util.Hashtable;

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
 *  Filename:     PumpValuesTableModel  
 *  Description:  Model for table of Pump values
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// FIX: Remove unused methods...


public class PumpValuesTableModel extends DeviceValuesTableModel 
{

    private static final long serialVersionUID = -3199123443953228082L;

    private I18nControl m_ic = I18nControl.getInstance();
    // x private DataAccessMeter m_da = DataAccessMeter.getInstance();

    // GlucoValues dayData;

    ArrayList<PumpValuesEntry> dl_data;
    ArrayList<PumpValuesEntry> displayed_dl_data;
    
    Hashtable<String,DayValueH> old_data = null;

    // GGCProperties props = GGCProperties.getInstance();

    //int current_filter = PumpDisplayDataDialog.FILTER_NEW_CHANGED;

    // public String status_icon_name

    private String[] column_names = { m_ic.getMessage("DATETIME"), m_ic.getMessage("BG_MMOLL"),
                                     m_ic.getMessage("BG_MGDL"), m_ic.getMessage("STATUS"), m_ic.getMessage(""), };

    /**
     * Constructor
     */
    public PumpValuesTableModel(DeviceDataHandler ddh)
    {
        super(DataAccessPump.getInstance(), ddh);
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
        //if (column == 4)
        //    return true;
        //else
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



    
    public void setFilter(int filter)
    {
        if (this.current_filter==filter)
            return;
        
        this.current_filter = filter;
        
        this.displayed_dl_data.clear();
        
        for(int i=0; i< this.dl_data.size(); i++)
        {
            PumpValuesEntry mve = this.dl_data.get(i);
            
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
            case DeviceDisplayDataDialog.FILTER_ALL:
                return true;
                
            case DeviceDisplayDataDialog.FILTER_NEW:
                return (status == PumpValuesEntry.STATUS_NEW);
    
            case DeviceDisplayDataDialog.FILTER_CHANGED:
                return (status == PumpValuesEntry.STATUS_CHANGED);
                
            case DeviceDisplayDataDialog.FILTER_EXISTING:
                return (status == PumpValuesEntry.STATUS_OLD);
                
            case DeviceDisplayDataDialog.FILTER_UNKNOWN:
                return (status == PumpValuesEntry.STATUS_UNKNOWN);
                
            case DeviceDisplayDataDialog.FILTER_NEW_CHANGED:
                return ((status == PumpValuesEntry.STATUS_NEW) ||
                        (status == PumpValuesEntry.STATUS_CHANGED));
                
            case DeviceDisplayDataDialog.FILTER_ALL_BUT_EXISTING:
                return (status != PumpValuesEntry.STATUS_OLD);
        }
        return false;

    }

    public int getRowCount()
    {
        return this.displayed_dl_data.size();
    }

    public Object getValueAt(int row, int column)
    {
        // TODO: Fix this
        PumpValuesEntry mve = this.displayed_dl_data.get(row);

        switch (column)
        {
        case 0:
            return mve.getDateTimeObject().getDateTimeString();

        case 1:
            //return mve.getBGValue(DataAccessPump.BG_MMOL);

        case 2:
            //return mve.getBGValue(DataAccessPump.BG_MGDL);

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

    public void addEntry(PumpValuesEntry mve)
    {
        processPumpValuesEntry(mve);
        this.dl_data.add(mve);
        
        if (this.shouldBeDisplayed(mve.status))
        {
            this.displayed_dl_data.add(mve);
        }
        this.fireTableDataChanged();
    }

    
    public void processPumpValuesEntry(PumpValuesEntry mve)
    {
        //System.out.println("processMeterValuesEntry");
        if (old_data!=null)
        {
            //System.out.println("oldData != null");
            long dt = mve.getDt_info(); //.getDateTime();
            
            //System.out.println("Dt='" + dt + "'");
            
            //System.out.println("Found: " + old_data.containsKey("" + dt));
            
            
            if (!old_data.containsKey("" + dt))
            {
            //    System.out.println("not Contains");
                mve.status = PumpValuesEntry.STATUS_NEW;
                mve.object_status = PumpValuesEntry.OBJECT_STATUS_NEW;
            }
            else
            {
                
             //   System.out.println("Found !!!");
                
                DayValueH gvh = old_data.get("" + dt);
                  
//                int vl = Integer.parseInt(mve.getBGValue(OutputUtil.BG_MGDL));
                int vl = 1;
                //if (((vl-1) >= gvh.getBg()) && (gvh.getBg() <= (vl+1)))
                if (gvh.getBg()==vl)
                {
                    mve.status = PumpValuesEntry.STATUS_OLD;
                    mve.object_status = PumpValuesEntry.OBJECT_STATUS_OLD;
                }
                else
                {
                    mve.status = PumpValuesEntry.STATUS_CHANGED;
                    mve.object_status = PumpValuesEntry.OBJECT_STATUS_EDIT;
                    mve.entry_object = gvh;
                    
                    //System.out.println("Changed: " + gvh.getId());
                    
                }
                    
                //gvh.getBg()
            }
        }
        else
        {
            System.out.println("oldData == null");

            mve.status = PumpValuesEntry.STATUS_NEW;
        }
    }
    
    /*
    public Hashtable<String,ArrayList<DayValueH>> getCheckedEntries()
    {
        
        Hashtable<String,ArrayList<DayValueH>> ht = new Hashtable<String,ArrayList<DayValueH>>();
        
        ht.put("ADD", new ArrayList<DayValueH>());
        ht.put("EDIT", new ArrayList<DayValueH>());
        
        
        for(int i=0; i<this.dl_data.size(); i++)
        {
            PumpValuesEntry mve = this.dl_data.get(i);
            
            if (!mve.checked)
                continue;
            
            mve.prepareEntry();
            
            if (mve.object_status==PumpValuesEntry.OBJECT_STATUS_NEW)
            {
                ht.get("ADD").add(mve.getDbObject());
            }
            else if (mve.object_status==PumpValuesEntry.OBJECT_STATUS_EDIT)
            {
                ht.get("EDIT").add(mve.getDbObject());
            }
        }
        
        return ht;
    }*/
    
    
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

    public void setOldValues(Hashtable<String,DayValueH> data)
    {
        this.old_data = data;
        //System.out.println(this.old_data);
        //System.out.println(this.old_data.keys());
    }

    @Override
    public void addToArray(ArrayList<?> lst, ArrayList<?> source)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ArrayList<? extends GGCHibernateObject> getEmptyArrayList()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void processDeviceValueEntry(DeviceValuesEntry mve)
    {
        // TODO Auto-generated method stub
        
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
