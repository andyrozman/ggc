package ggc.core.data;

import ggc.core.util.DataAccess;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.db.ext.ExtendedHandler;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     DailyValuesRow
 *  Description:  One row in the DailyValues Data Model.
 * 
 *  @author schultd
 *  @author Andy {andy@atech-software.com}  
 */

public class ExtendedDailyValue extends ExtendedHandler //implements Serializable, Comparable<DailyValuesRow>
{

    @SuppressWarnings("unused")
    private static Log log = LogFactory.getLog(ExtendedDailyValue.class); 

    
    
    
    
    
    //private String extended;
    
    //private String[] extended_arr;

    //private boolean changed = false;
    //boolean debug = false;

    
    /**
     * Extended Parameter: Activity
     */
    public static final int EXTENDED_ACTIVITY = 0;

    /**
     * Extended Parameter: Urine
     */
    public static final int EXTENDED_URINE = 1;

    /**
     * Extended Parameter: Food Description
     */
    public static final int EXTENDED_FOOD_DESCRIPTION = 2;
    
    /**
     * Extended Parameter: Food CH
     */
    public static final int EXTENDED_FOOD_CH = 3;
    
    /**
     * Extended Parameter: Decimal Part Ins1
     */
    public static final int EXTENDED_DECIMAL_PART_INS1 = 4;
    
    /**
     * Extended Parameter: Decimal Part Ins2
     */
    public static final int EXTENDED_DECIMAL_PART_INS2 = 5;
    

    /**
     * Extended Parameter: Insulin 3
     */
    public static final int EXTENDED_INSULIN_3 = 6;
    

    
    /**
     * Extended Parameter: Source
     */
    public static final int EXTENDED_SOURCE = 7;
    
    
    @SuppressWarnings("unused")
    private static final int EXTENDED_MAX = 7;

    /*
    private String extended_desc[] = { 
                                    "URINE",
                                    "ACTIVITY",
                                    "FOOD_DESCRIPTION",
                                    "FOOD_DESC_CH",
                                    "DECIMAL_INS1",
                                    "DECIMAL_INS2",
                                    "INSULIN_3"
    };*/
                                    
    

    /**
     * Constructor
     * 
     * @param da 
     */
    public ExtendedDailyValue(DataAccess da)
    {
        super(da);
    }
    


    //loadExtended();



    @Override
    public void initExtended()
    {
        ext_mapped_types = new Hashtable<Integer,String>();

        ext_mapped_types.put(ExtendedDailyValue.EXTENDED_URINE,"URINE");
        ext_mapped_types.put(ExtendedDailyValue.EXTENDED_ACTIVITY ,"ACTIVITY");
        ext_mapped_types.put(ExtendedDailyValue.EXTENDED_FOOD_DESCRIPTION ,"FOOD_DESCRIPTION");
        ext_mapped_types.put(ExtendedDailyValue.EXTENDED_FOOD_CH ,"FOOD_DESC_CH");
        ext_mapped_types.put(ExtendedDailyValue.EXTENDED_DECIMAL_PART_INS1 ,"DECIMAL_INS1");
        ext_mapped_types.put(ExtendedDailyValue.EXTENDED_DECIMAL_PART_INS2 ,"DECIMAL_INS2");
        ext_mapped_types.put(ExtendedDailyValue.EXTENDED_INSULIN_3 ,"INSULIN_3");
        ext_mapped_types.put(ExtendedDailyValue.EXTENDED_SOURCE ,"SOURCE");
    }


    
    public String getExtendedObject()
    {
        return "DailyValuesRow";
    }
    
    

    


    




}
