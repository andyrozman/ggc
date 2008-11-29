package ggc.meter.data;

import ggc.core.db.hibernate.DayValueH;
import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.DeviceValuesTableModel;
import ggc.plugin.gui.DeviceDisplayDataDialog;
import ggc.plugin.output.OutputUtil;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     MeterValuesTableModel
 *  Description:  MeterValues Table Model
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class MeterValuesTableModel extends DeviceValuesTableModel        //extends AbstractTableModel 
{

    private static final long serialVersionUID = 7198690314603156531L;
    private I18nControl m_ic = I18nControl.getInstance();
    // x private DataAccessMeter m_da = DataAccessMeter.getInstance();


    //ArrayList<MeterValuesEntry> dl_data;
    //ArrayList<MeterValuesEntry> displayed_dl_data;
    //Hashtable<String,DayValueH> old_data = null;
    
    //Hashtable<String,DayValueH> old_data_typed = null;
    //old_data_typed
    

    // GGCProperties props = GGCProperties.getInstance();

    int current_filter = DeviceDisplayDataDialog.FILTER_NEW_CHANGED;

    // public String status_icon_name

    private String[] column_names = { m_ic.getMessage("DATETIME"), m_ic.getMessage("BG_MMOLL"),
                                     m_ic.getMessage("BG_MGDL"), m_ic.getMessage("STATUS"), m_ic.getMessage(""), };

    public MeterValuesTableModel(DeviceDataHandler ddh)
    {
        super(DataAccessMeter.getInstance(), ddh);
        
        //this.displayed_dl_data = new ArrayList<MeterValuesEntry>();
        //this.dl_data = new ArrayList<MeterValuesEntry>();
        //fireTableChanged(null);
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

    /*
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
*/
  /*  
    public void setFilter(int filter)
    {
        if (this.current_filter==filter)
            return;
        
        this.current_filter = filter;
        
        this.displayed_dl_data.clear();
        
        for(int i=0; i< this.dl_data.size(); i++)
        {
            MeterValuesEntry mve = (MeterValuesEntry)this.dl_data.get(i);
            
            if (shouldBeDisplayed(mve.getStatus()))
            {
                this.displayed_dl_data.add(mve);
            }
        }
        
        this.fireTableDataChanged();
        
    }
    */
    
    

    public boolean shouldBeDisplayed(int status)
    {
        switch (this.current_filter)
        {
            case DeviceDisplayDataDialog.FILTER_ALL:
                return true;
                
            case DeviceDisplayDataDialog.FILTER_NEW:
                return (status == DeviceValuesEntry.STATUS_NEW);
    
            case DeviceDisplayDataDialog.FILTER_CHANGED:
                return (status == DeviceValuesEntry.STATUS_CHANGED);
                
            case DeviceDisplayDataDialog.FILTER_EXISTING:
                return (status == DeviceValuesEntry.STATUS_OLD);
                
            case DeviceDisplayDataDialog.FILTER_UNKNOWN:
                return (status == DeviceValuesEntry.STATUS_UNKNOWN);
                
            case DeviceDisplayDataDialog.FILTER_NEW_CHANGED:
                return ((status == DeviceValuesEntry.STATUS_NEW) ||
                        (status == DeviceValuesEntry.STATUS_CHANGED));
                
            case DeviceDisplayDataDialog.FILTER_ALL_BUT_EXISTING:
                return (status != DeviceValuesEntry.STATUS_OLD);
        }
        return false;

    }

    public int getRowCount()
    {
        return this.displayed_dl_data.size();
    }

    public Object getValueAt(int row, int column)
    {
        MeterValuesEntry mve = (MeterValuesEntry)this.displayed_dl_data.get(row);

        switch (column)
        {
        case 0:
            return mve.getDateTimeObject().getDateTimeString();

        case 1:
            return mve.getBGValue(DataAccessMeter.BG_MMOL);

        case 2:
            return mve.getBGValue(DataAccessMeter.BG_MGDL);

        case 3:
            return new Integer(mve.getStatus());

        case 4:
            return new Boolean(mve.getChecked());

        default:
            return "";
        }

        
    }

/*    
    public void addEntry(DeviceValuesEntry dve)
    {
        MeterValuesEntry mve = (MeterValuesEntry)dve; 
        
        processMeterValuesEntry(mve);
        this.dl_data.add(mve);
        
        if (this.shouldBeDisplayed(mve.status))
        {
            this.displayed_dl_data.add(mve);
        }
        this.fireTableDataChanged();
    }
*/
    
    public void processDeviceValueEntry(DeviceValuesEntry mve)
    {
        //System.out.println("processMeterValuesEntry");
        if (this.m_ddh.hasOldData())
        {
            //System.out.println("oldData != null");
            long dt = mve.getDateTime(); //.getATDateTimeAsLong();
            
            //System.out.println("Dt='" + dt + "'");
            
            //System.out.println("Found: " + old_data.containsKey("" + dt));
            
            
            if (!this.m_ddh.getOldData().containsKey("" + dt))
            {
            //    System.out.println("not Contains");
                mve.setStatus(DeviceValuesEntry.STATUS_NEW);
                mve.object_status = MeterValuesEntry.OBJECT_STATUS_NEW;
            }
            else
            {
                
                MeterValuesEntry mve2 = (MeterValuesEntry)mve; 
                
             //   System.out.println("Found !!!");
                
                DayValueH gvh = (DayValueH)this.m_ddh.getOldData().get("" + dt);
                  
                int vl = Integer.parseInt(mve2.getBGValue(OutputUtil.BG_MGDL));
                
                //if (((vl-1) >= gvh.getBg()) && (gvh.getBg() <= (vl+1)))
                if (gvh.getBg()==vl)
                {
                    mve2.setStatus(MeterValuesEntry.STATUS_OLD);
                    mve2.object_status = MeterValuesEntry.OBJECT_STATUS_OLD;
                }
                else
                {
                    mve2.setStatus(MeterValuesEntry.STATUS_CHANGED);
                    mve2.object_status = MeterValuesEntry.OBJECT_STATUS_EDIT;
                    mve2.entry_object = gvh;
                    
                    //System.out.println("Changed: " + gvh.getId());
                    
                }
                    
                //gvh.getBg()
            }
        }
        else
        {
            System.out.println("oldData == null");
            mve.setStatus(MeterValuesEntry.STATUS_NEW);
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
            MeterValuesEntry mve = this.dl_data.get(i);
            
            if (!mve.checked)
                continue;
            
            mve.prepareEntry();
            
            if (mve.object_status==MeterValuesEntry.OBJECT_STATUS_NEW)
            {
                ht.get("ADD").add(mve.getDbObject());
            }
            else if (mve.object_status==MeterValuesEntry.OBJECT_STATUS_EDIT)
            {
                ht.get("EDIT").add(mve.getDbObject());
            }
        }
        
        return ht;
    }
    */
    
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
        this.displayed_dl_data.get(row).setChecked(b.booleanValue());
        // System.out.println("set Value: rw=" + row + ",column=" + column +
        // ",value=" + aValue);
        // dayData.setValueAt(aValue, row, column);
        // fireTableChanged(null);
    }


    @Override
    public void addToArray(ArrayList<?> lst, ArrayList<?> source)
    {
        if ((source==null) || (source.size()==0))
            return;
        
        ArrayList<DayValueH> lst2 = (ArrayList<DayValueH>)lst;
        ArrayList<DayValueH> src2 = (ArrayList<DayValueH>)source;
        
        for(int i=0; i<source.size(); i++)
        {
            lst2.add(src2.get(i));
        }
    }

    @Override
    public ArrayList<? extends GGCHibernateObject> getEmptyArrayList()
    {
        return new ArrayList<DayValueH>();
    }

  

}
