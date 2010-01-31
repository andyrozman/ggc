package ggc.cgms.data;

import ggc.cgms.util.DataAccessCGMS;
import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.data.DeviceValuesTableModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import com.atech.utils.ATechDate;


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


public class CGMSValuesTableModel extends DeviceValuesTableModel 
{

    private static final long serialVersionUID = 2881771615052748327L;

//    private I18nControl m_ic = I18nControl.getInstance();

//    int current_filter = DeviceDisplayDataDialog.FILTER_NEW_CHANGED;

//    private String[] column_names = { m_ic.getMessage("DATETIME"), m_ic.getMessage("BG_MMOLL"),
//                                     m_ic.getMessage("BG_MGDL"), m_ic.getMessage("STATUS"), m_ic.getMessage(""), };

    /**
     * Constructor
     * 
     * @param ddh DeviceDataHandler instance
     * @param source 
     */
    public CGMSValuesTableModel(DeviceDataHandler ddh, String source)
    {
        super(DataAccessCGMS.getInstance(), ddh, source);
        htable = new Hashtable<String, CGMSValuesEntry>();

    }

    /**
     * Get Column Count
     * 
     * @see ggc.plugin.data.DeviceValuesTableModel#getColumnCount()
     */
    public int getColumnCount()
    {
        return 5;
    }

    /**
     * Is Boolean
     * 
     * @param column column index
     * @return true if column type is boolean
     */
    public boolean isBoolean(int column)
    {
        if (column == this.getCheckableColumn())
            return true;
        else
            return false;
    }

    
    
    // protected ArrayList<DeviceValuesEntryInterface> dl_data;

    Hashtable<String, CGMSValuesEntry> htable = null;
    String old_key = null;
    CGMSValuesEntry current_main = null;
    
    int i = 0;
    
    /**
     * Add Entry
     * 
     * @param mve DeviceValuesEntry instance
     */
    public void addEntry(DeviceValuesEntryInterface mve)
    {
        //if (i>5)
        //    return;
        
        CGMSValuesSubEntry se = (CGMSValuesSubEntry)mve;
  
        String key = se.date + "_" + se.type;
        
        if (old_key == null)
        {
            old_key = key;
            this.current_main = new CGMSValuesEntry();
        }
        
        if (!old_key.equals(key))
        {
            processDeviceValueEntry(this.current_main);
            this.dl_data.add(this.current_main);
            
            if (this.shouldBeDisplayed(this.current_main.getStatus()))
            {
                this.displayed_dl_data.add(this.current_main);
                Collections.sort(displayed_dl_data);
            }
            this.fireTableDataChanged();

            this.current_main = new CGMSValuesEntry();
            
            i++;
        }
        
        
        if (current_main.isEmpty())
        {
            //CGMSValuesEntry cve = new CGMSValuesEntry();
            this.current_main.setDateTimeObject(new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, se.datetime));
            this.current_main.setDate(se.date);
            
            this.current_main.setEmpty(false);
            this.current_main.setType(se.type);
            //this.htable.put(key, cve);
            this.current_main.addSubEntry(se);
            
            old_key = key;
            
            this.htable.put(key, this.current_main);
        }
        else
        {
            this.current_main.addSubEntry(se);
        }
        
        
        
        /*
        if (this.htable.containsKey(key))
        {
            this.htable.get(key).addSubEntry(se);
        }
        else
        {
            CGMSValuesEntry cve = new CGMSValuesEntry();
            cve.setDateTimeObject(new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, se.datetime));
            cve.addSubEntry(se);
            this.htable.put(key, cve);
        }*/
        
        //if se.
        
        //getDate(se.datetime);
        
        System.out.println("HTable: " + htable.size());
        
        
        /*
        
        System.out.println(".");
        processDeviceValueEntry(mve);
        this.dl_data.add(mve);
        
        if (this.shouldBeDisplayed(mve.getStatus()))
        {
            this.displayed_dl_data.add(mve);
            Collections.sort(displayed_dl_data);
        }
        this.fireTableDataChanged();
        */
    }


    public void finishReading()
    {
        processDeviceValueEntry(this.current_main);
        this.dl_data.add(this.current_main);
        
        if (this.shouldBeDisplayed(this.current_main.getStatus()))
        {
            this.displayed_dl_data.add(this.current_main);
            Collections.sort(displayed_dl_data);
        }
        this.fireTableDataChanged();
    }
    
    

    /**
     * Add To Array 
     * 
     * @param lst
     * @param source
     */
    @Override
    public void addToArray(ArrayList<?> lst, ArrayList<?> source)
    {
        // TODO Auto-generated method stub
    }

    /**
     * Get Empty ArrayList
     * 
     * @return
     */
    @Override
    public ArrayList<? extends GGCHibernateObject> getEmptyArrayList()
    {
        return new ArrayList<GGCHibernateObject>();
    }

    /**
     * Process Device Value Entry
     * 
     * @param mve DeviceValuesEntry instance
     */
    @Override
    public void processDeviceValueEntry(DeviceValuesEntryInterface mve)
    {
        mve.setStatus(DeviceValuesEntry.STATUS_NEW);
        // TODO Auto-generated method stub
    }

    /** 
     * Get Checkable Column
     */
    @Override
    public int getCheckableColumn()
    {
        return 3;
    }
   
}
