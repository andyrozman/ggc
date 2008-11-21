package ggc.plugin.output;

import ggc.plugin.util.DataAccessPlugInBase;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.atech.utils.TimerControlAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class OutputUtil extends TimerControlAbstract
{

    public int max_records = 1;
    
	public static String[] unitsName = { "", "mg/dL", "mmol/L" };
	
    /**
     * Which BG unit is used: BG_MGDL = mg/dl, BG_MMOL = mmol/l
     */
    public int m_BG_unit = BG_MGDL;
	
    private OutputWriter writer;
    
    public static OutputUtil m_outputUtil;
    
    
    private OutputUtil()
    {
//a    	setAllowedChangeTime(10);
    }
	
    
    
    
    
    public static OutputUtil getInstance()
    {
        if (OutputUtil.m_outputUtil==null)
        {
            OutputUtil.m_outputUtil = new OutputUtil();
        }
        
        return OutputUtil.m_outputUtil;
        
    }
    

    public static OutputUtil getInstance(OutputWriter writer)
    {
        if (OutputUtil.m_outputUtil==null)
        {
            OutputUtil.m_outputUtil = new OutputUtil();
        }
        
        OutputUtil.m_outputUtil.setOutputWriter(writer);
        return OutputUtil.m_outputUtil;
    }
    
    
    
    public void setOutputWriter(OutputWriter writer)
    {
        this.writer = writer;
    }
    
    /*
    public OutputUtil()
    {
    }*/
    
    
    public static final int BG_MGDL = 1;
    public static final int BG_MMOL = 2;

    public int getBGMeasurmentType()
    {
        return this.m_BG_unit;
    }

    public void setBGMeasurmentType(int type)
    {
        this.m_BG_unit = type;
    }

    public String getBGMeasurmentTypeName()
    {
        return OutputUtil.unitsName[this.m_BG_unit];
    }
    
    public String getBGTypeName(int type)
    {
        return OutputUtil.unitsName[type];
    }
    

    public static String getBGTypeNameStatic(int type)
    {
        return OutputUtil.unitsName[type];
    }
    
    
    private static final float MGDL_TO_MMOL_FACTOR = 0.0555f;

    private static final float MMOL_TO_MGDL_FACTOR = 18.016f;

    /**
     * Depending on the return value of <code>getBGMeasurmentType()</code>, either
     * return the mg/dl or the mmol/l value of the database's value. Default is mg/dl.
     * @param dbValue - The database's value (in float)
     * @return the BG in either mg/dl or mmol/l
     */
    public float getDisplayedBG(float dbValue)
    {
        switch (this.m_BG_unit)
        {
        case BG_MMOL:
            // this POS should return a float rounded to 3 decimal places,
            // if I understand the docu correctly
            return (new BigDecimal(dbValue * MGDL_TO_MMOL_FACTOR,
                    new MathContext(3, RoundingMode.HALF_UP)).floatValue());
        case BG_MGDL:
        default:
            return dbValue;
        }
    }

    public float getBGValue(float bg_value)
    {
        switch (this.m_BG_unit)
        {
        case BG_MMOL:
            return (bg_value * MGDL_TO_MMOL_FACTOR);
        case BG_MGDL:
        default:
            return bg_value;
        }
    }


    public float getBGValueByType(int type, float bg_value)
    {
        switch (type)
        {
        case BG_MMOL:
            return (bg_value * MGDL_TO_MMOL_FACTOR);
        case BG_MGDL:
        default:
            return bg_value;
        }
    }


    public float getBGValueByType(int input_type, int output_type, float bg_value)
    {
        
        if (input_type==output_type)
            return bg_value;
        else
        {
            if (output_type==DataAccessPlugInBase.BG_MGDL)
            {
                return bg_value * OutputUtil.MGDL_TO_MMOL_FACTOR;
            }
            else
            {
                return bg_value * OutputUtil.MMOL_TO_MGDL_FACTOR;
            }
        }

    }


    public float getBGValueDifferent(int type, float bg_value)
    {

            if (type==DataAccessPlugInBase.BG_MGDL)
            {
                return bg_value * OutputUtil.MGDL_TO_MMOL_FACTOR;
            }
            else
            {
                return bg_value * OutputUtil.MMOL_TO_MGDL_FACTOR;
            }
    }

	
	
	public void setOutputBGType(int bg_type)
	{
		this.m_BG_unit = bg_type;
	}
	
	
	public void stopAction()
	{
	    
		this.writer.endOutput();
		
		//System.exit(0);

	}
	
	
	public void setMaxMemoryRecords(int val)
	{
	    this.max_records = val;
	}
	
	public int getMaxMemoryRecords()
	{
	    return this.max_records;
	}
	
	
	public static String getBGUnitName(int unit)
	{
	    return OutputUtil.unitsName[unit];
	}
	
	
	
}