package ggc.cgm.data;

import ggc.cgm.util.DataAccessCGM;
import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.data.DeviceValuesTableModel;

import java.util.ArrayList;


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


public class CGMValuesTableModel extends DeviceValuesTableModel 
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
     */
    public CGMValuesTableModel(DeviceDataHandler ddh)
    {
        super(DataAccessCGM.getInstance(), ddh);
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
        if (column == 4)
            return true;
        else
            return false;
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
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Process Device Value Entry
     * 
     * @param mve DeviceValuesEntry instance
     */
    @Override
    public void processDeviceValueEntry(DeviceValuesEntryInterface mve)
    {
        // TODO Auto-generated method stub
    }

    /** 
     * Get Checkable Column
     */
    @Override
    public int getCheckableColumn()
    {
        return 4;
    }
   
}
