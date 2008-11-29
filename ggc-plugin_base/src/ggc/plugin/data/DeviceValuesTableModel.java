package ggc.plugin.data;

import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.plugin.util.DataAccessPlugInBase;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.table.AbstractTableModel;


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
 *  Filename:     CGMValuesTableModel  
 *  Description:  Model for table of CGMS values
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class DeviceValuesTableModel extends AbstractTableModel 
{


//    private I18nControl m_ic = I18nControl.getInstance();
    // x private DataAccessMeter m_da = DataAccessMeter.getInstance();

    // GlucoValues dayData;

    private static final long serialVersionUID = -6542265335372702616L;
    protected ArrayList<DeviceValuesEntry> dl_data;
    protected ArrayList<DeviceValuesEntry> displayed_dl_data;
    
    //Hashtable<String,Object> old_data = null;
    protected DeviceDataHandler m_ddh = null;

    // GGCProperties props = GGCProperties.getInstance();

    // TODO: fix this
    int current_filter = 0; //DeviceDisplayDataDialog.FILTER_NEW_CHANGED;

    protected DataAccessPlugInBase m_da;
    // public String status_icon_name

    //private String[] column_names = { m_ic.getMessage("DATETIME"), m_ic.getMessage("BG_MMOLL"),
    //                                 m_ic.getMessage("BG_MGDL"), m_ic.getMessage("STATUS"), m_ic.getMessage(""), };

    public DeviceValuesTableModel(DataAccessPlugInBase da, DeviceDataHandler ddh)
    {
        this.m_ddh = ddh;
        this.m_da = da;
        this.displayed_dl_data = new ArrayList<DeviceValuesEntry>();
        this.dl_data = new ArrayList<DeviceValuesEntry>();
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
        // TODO
        if (column == 4)
            return true;
        else
            return false;

    }

    public int getColumnWidth(int column, int width)
    {
        // TODO
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
            this.displayed_dl_data.get(i).setChecked(select);
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
            DeviceValuesEntry mve = this.dl_data.get(i);
            
            if (shouldBeDisplayed(mve.getStatus()))
            {
                this.displayed_dl_data.add(mve);
            }
        }
        
        this.fireTableDataChanged();
        
    }
    
    
    

    public boolean shouldBeDisplayed(int status)
    {
        
/*        
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
*/
        return true;
    }

    public int getRowCount()
    {
        return this.displayed_dl_data.size();
    }

    
    public abstract Object getValueAt(int row, int column);

/*    
    public Object getValueAt(int row, int column)
    {
        DeviceValuesEntry mve = this.displayed_dl_data.get(row);

        switch (column)
        {
        case 0:
            return mve.getDateTimeObject().getDateTimeString();

        case 1:
            return mve.getBGValue(DataAccessCGM.BG_MMOL);

        case 2:
            return mve.getBGValue(DataAccessCGM.BG_MGDL);

        case 3:
            return new Integer(mve.getStatus());

        case 4:
            return new Boolean(mve.getChecked());

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
  //  }

    public void addEntry(DeviceValuesEntry mve)
    {
        processDeviceValueEntry(mve);
        this.dl_data.add(mve);
        
        if (this.shouldBeDisplayed(mve.getStatus()))
        {
            this.displayed_dl_data.add(mve);
        }
        this.fireTableDataChanged();
    }

    
    public abstract void processDeviceValueEntry(DeviceValuesEntry mve);
    
    
    /*
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
                mve.object_status = MeterValuesEntry.OBJECT_STATUS_NEW;
            }
            else
            {
                
             //   System.out.println("Found !!!");
                
                DayValueH gvh = old_data.get("" + dt);
                  
                int vl = Integer.parseInt(mve.getBGValue(OutputUtil.BG_MGDL));
                
                //if (((vl-1) >= gvh.getBg()) && (gvh.getBg() <= (vl+1)))
                if (gvh.getBg()==vl)
                {
                    mve.status = MeterValuesEntry.STATUS_OLD;
                    mve.object_status = MeterValuesEntry.OBJECT_STATUS_OLD;
                }
                else
                {
                    mve.status = MeterValuesEntry.STATUS_CHANGED;
                    mve.object_status = MeterValuesEntry.OBJECT_STATUS_EDIT;
                    mve.entry_object = gvh;
                    
                    //System.out.println("Changed: " + gvh.getId());
                    
                }
                    
                //gvh.getBg()
            }
        }
        else
        {
            System.out.println("oldData == null");

            mve.status = MeterValuesEntry.STATUS_NEW;
        }
    }
    */
    
    
    public Hashtable<String,ArrayList<?>> getCheckedEntries()
    {
        
        Hashtable<String,ArrayList<?>> ht = new Hashtable<String,ArrayList<?>>();
        
        ht.put("ADD", getEmptyArrayList());
        ht.put("EDIT", getEmptyArrayList());
        
        
        for(int i=0; i<this.dl_data.size(); i++)
        {
            DeviceValuesEntry mve = this.dl_data.get(i);
            
            if (!mve.getChecked())
                continue;
            
            mve.prepareEntry();
            
            if (mve.object_status==DeviceValuesEntry.OBJECT_STATUS_NEW)
            {
                //addToArray(ht.get("ADD").addAll(mve.getDbObjects());
                addToArray(ht.get("ADD"), mve.getDbObjects());
            }
            else if (mve.object_status==DeviceValuesEntry.OBJECT_STATUS_EDIT)
            {
                addToArray(ht.get("EDIT"), mve.getDbObjects());
            }
        }
        
        return ht;
    }
    
    
    public abstract ArrayList<? extends GGCHibernateObject> getEmptyArrayList();
    
    public abstract void addToArray(ArrayList<?> lst, ArrayList<?> source);
    
    /*
    {
        for(int i=0; i<source.size(); i++)
        {
            lst.add(source.get(i));
        }
        
    }
    */
    
    
    
    @Override
    public String getColumnName(int column)
    {
        return this.m_da.getColumnsTable()[column];
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

    /*
    @Override
    public void setValueAt(Object aValue, int row, int column)
    {
        Boolean b = (Boolean) aValue;
        this.displayed_dl_data.get(row).setChecked(b.booleanValue());
        // System.out.println("set Value: rw=" + row + ",column=" + column +
        // ",value=" + aValue);
        // dayData.setValueAt(aValue, row, column);
        // fireTableChanged(null);
    }
*/
    
    //public abstract void setOldValues(Hashtable<String,?> data);
    
    /*
    {
        this.old_data = data;
        //System.out.println(this.old_data);
        //System.out.println(this.old_data.keys());
    }
    */
    

}
