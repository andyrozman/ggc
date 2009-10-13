package ggc.meter.data;

import ggc.core.db.hibernate.DayValueH;
import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.data.DeviceValuesTableModel;

import java.util.ArrayList;

import com.atech.i18n.I18nControlAbstract;

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


public class MeterValuesExtTableModel extends DeviceValuesTableModel        //extends AbstractTableModel 
{

    private I18nControlAbstract m_ic = DataAccessMeter.getInstance().getI18nControlInstance();

//    private String[] column_names = { m_ic.getMessage("DATETIME"), m_ic.getMessage("BG_MMOLL"),
//                                     m_ic.getMessage("BG_MGDL"), m_ic.getMessage("STATUS"), m_ic.getMessage(""), };

    
    /**
     * 
     */
    private static final long serialVersionUID = -660580365600276458L;

    /**
     * Constructor
     * 
     * @param ddh DeviceDataHandler instance
     * @param source 
     */
    public MeterValuesExtTableModel(DeviceDataHandler ddh, String source)
    {
        super(DataAccessMeter.getInstance(), ddh, source);
    }

    /**
     * Get Column Count
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
    {
        return 5;
    }


    
    /**
     * Get Checkable Column (one column if checkable, all others are non-editable)
     * 
     * @return
     */
    public int getCheckableColumn()
    {
        return 4;
    }
    
    private int column_width[] = { 120, 150, 200, 90, 50 };
    
    
    /**
     * Get Column Width
     * 
     * @param column column index
     * @param width width for column
     * @return calculated size of column
     */
    public int getColumnWidth(int column, int width)
    {
        return this.column_width[column];
        
        /*
        if (column == 0)
        {
            return 100; // third column is bigger
        }
        else
        {
            return 50;
        }*/

    }
    
 
    
    
    
    /**
     * Should be displayed filter
     * 
     * @param status
     * @return
     */
    /*public boolean shouldBeDisplayed(int status)
    {
        //System.out.println("Should be displayed: " + status);
        switch (this.current_filter)
        //switch (status)
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

    }*/


    /**
     * Get Value At
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int column)
    {
        MeterValuesEntry mve = (MeterValuesEntry)this.displayed_dl_data.get(row);

        switch (column)
        {
        case 0:
            return mve.getDateTimeObject().getDateTimeString();

        case 1:
            return mve.getExtendedTypeDescription();

        case 2:
            return mve.getExtendedTypeValue();

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

    

    
    /**
     * Process Device Value Entry
     * 
     * @param mve DeviceValuesEntry instance
     */
    @SuppressWarnings("deprecation")
    public void processDeviceValueEntry(DeviceValuesEntryInterface mve)
    {
        
        if (this.m_ddh.hasOldData())
        {
            if (!this.m_ddh.getOldData().containsKey("" + mve.getSpecialId()))
            {
                mve.setStatus(DeviceValuesEntry.STATUS_NEW);
                mve.setObjectStatus(MeterValuesEntry.OBJECT_STATUS_NEW);
            }
            else
            {
                MeterValuesEntry mve2 = (MeterValuesEntry)mve; 

                MeterValuesEntry mve_old = (MeterValuesEntry)this.m_ddh.getOldData().get(mve.getSpecialId());
                
                //DayValueH gvh = (DayValueH)this.m_ddh.getOldData().get("" + dt);
                  
                //int vl = Integer.parseInt(mve2.getBGValue(OutputUtil.BG_MGDL));
                
                //System.out.println(mve_old.getDateTimeObject() + "=" + mve_old.getValue());
                //System.out.println(mve2.getDateTimeObject() + "=" + mve2.getValue());
                
                //if (((vl-1) >= gvh.getBg()) && (gvh.getBg() <= (vl+1)))
                if (mve_old.getValue().equals(mve2.getValue()))
                {
                    mve2.setStatus(MeterValuesEntry.STATUS_OLD);
                    mve2.object_status = MeterValuesEntry.OBJECT_STATUS_OLD;
                }
                else
                {
                    mve2.setStatus(MeterValuesEntry.STATUS_CHANGED);
                    mve2.object_status = MeterValuesEntry.OBJECT_STATUS_EDIT;
                    mve2.entry_object = mve_old.getHibernateObject();
                    
                    //System.out.println("Changed: " + gvh.getId());
                    
                }
                    
                //gvh.getBg()
            }
        }
        else
        {
//            System.out.println("oldData == null");
            mve.setStatus(MeterValuesEntry.STATUS_NEW);
        }
        
        
        
        
/*        
        if (this.m_ddh.hasOldData())
        {
//            System.out.println("oldData != null");
            long dt = mve.getDateTime(); //.getATDateTimeAsLong();
            
//            System.out.println("Dt='" + dt + "'");
//            System.out.println("Found: " + this.m_ddh.getOldData().containsKey("" + dt));
            
            
            if (!this.m_ddh.getOldData().containsKey("" + dt))
            {
//                System.out.println("not Contains");
                mve.setStatus(DeviceValuesEntry.STATUS_NEW);
                mve.setObjectStatus(MeterValuesEntry.OBJECT_STATUS_NEW);
            }
            else
            {
                
                MeterValuesEntry mve2 = (MeterValuesEntry)mve; 
                
//                System.out.println("Found !!!");
                
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
//            System.out.println("oldData == null");
            mve.setStatus(MeterValuesEntry.STATUS_NEW);
        }
        */
        
        
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
    
    
    private String column_names[] = { m_ic.getMessage("DATETIME"), m_ic.getMessage("ENTRY_TYPE"), m_ic.getMessage("VALUE"), m_ic.getMessage("STATUS"), ""};
    
    /**
     * Get Column Name
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column)
    {
        return column_names[column];
    }

    /**
     * Get Column Class
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
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

    /**
     * Is Cell Editable
     * 
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int row, int col)
    {
        if (col == 4)
            return true;
        else
            return false;
    }

    /**
     * Set Value At
     * 
     * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object, int, int)
     */
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


    /**
     * Add To Array 
     * 
     * @param lst
     * @param source
     */
    @SuppressWarnings("unchecked")
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

    /**
     * Get Empty ArrayList
     * 
     * @return
     */
    @Override
    public ArrayList<? extends GGCHibernateObject> getEmptyArrayList()
    {
        return new ArrayList<DayValueH>();
    }


  

}