/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: PumpManager.java
 *  Purpose:  This class contains all definitions for Pumps. This includes:
 *        meter names, classes that handle meter and all other relevant data.
 *
 *  Author:   andyrozman
 */


package ggc.pump.manager; 

import ggc.pump.util.DataAccessPump;
import ggc.pump.util.I18nControl;

import java.util.Hashtable;

public class PumpManager
{

    protected I18nControl m_ic = I18nControl.getInstance();
    protected DataAccessPump m_da = DataAccessPump.getInstance();
    

    public static final int METER_NONE = 0;

    // old meters support
    public static final int METER_GLUCO_CARD = 1;
    public static final int METER_EURO_FLASH = 2;
    public static final int METER_FREESTYLE = 3;
    public static final int METER_ASCENSIA_CONTOUR = 4;
    public static final int METER_ASCENSIA_DEX = 5;
    public static final int METER_ASCENSIA_BREEZE = 6;
    public static final int METER_ASCENSIA_ELITE_XL = 7;


    public static final int METER_COMPANY_NO = 0;
    public static final int METER_COMPANY_UNKNOWN = 1;
    public static final int METER_COMPANY_ASCENSIA_BAYER = 2;

    public int[] meter_company = {
	METER_COMPANY_NO, // 0
	METER_COMPANY_UNKNOWN, // 1
	METER_COMPANY_UNKNOWN, // 2
	METER_COMPANY_UNKNOWN, // 3
	METER_COMPANY_ASCENSIA_BAYER, // 4
	METER_COMPANY_ASCENSIA_BAYER, // 5
	METER_COMPANY_ASCENSIA_BAYER, // 6
	METER_COMPANY_ASCENSIA_BAYER, // 7
    };



    




    public String[] meter_names = {
        m_ic.getMessage("NONE"),
        "GlucoCard", 
        "EuroFlash",
        "FreeStyle",
	"Ascensia Contour",
	"Ascensia DEX",
	"Ascensia Breeze",
	"Ascensia Elite XL"
    };





    public String[] meter_classes = {
        "ggc.data.imports.DummyImport",
        "ggc.data.imports.GlucoCardImport",
        "ggc.data.imports.EuroFlashImport",
        "ggc.data.imports.FreeStyleImport",
    };

    public String[] meter_device_classes = {
        "ggc.data.meter.device.DummyPump",
        "ggc.data.meter.device.DummyPump",
        "ggc.data.meter.device.DummyPump",
        "ggc.data.meter.device.DummyPump",
	"ggc.data.meter.device.AscensiaContourPump",
	"ggc.data.meter.device.AscensiaDEXPump",
	"ggc.data.meter.device.AscensiaBreezePump",
	"ggc.data.meter.device.AscensiaEliteXLPump",
    };

    // false =old interface, true = new interface
    public boolean[] meter_interface_type = {
	false,
	false,
	false,
	false,
	true,
	true,
	true,
	true
    };
/*
    public ImageIcon[] meter_pictures = {
        new ImageIcon(getClass().getResource("/icons/m_noPump.gif")),
        new ImageIcon(getClass().getResource("/icons/m_glucocard.gif")),
        new ImageIcon(getClass().getResource("/icons/m_euroflash.gif")),
        new ImageIcon(getClass().getResource("/icons/m_freestyle.gif")),
	new ImageIcon(getClass().getResource("/icons/m_asc_contour.gif")),
	new ImageIcon(getClass().getResource("/icons/m_asc_dex.gif")),
	new ImageIcon(getClass().getResource("/icons/m_asc_breeze.gif")),
	new ImageIcon(getClass().getResource("/icons/m_asc_elite.gif")),
    };
*/

    public static PumpManager s_manager = null;
    

    /**
     * Constructor for SerialPumpImport.
     */
    private PumpManager()
    {
    	this.loadPumpsDefinitions();
    }

    
    public static PumpManager getInstance()
    {
    	if (PumpManager.s_manager==null)
    		PumpManager.s_manager = new PumpManager();
    	
    	return PumpManager.s_manager;
    }



    /**
     * Gets the image
     * @return Returns a ImageIcon
     */
/*    public ImageIcon getPumpImage(int index)
    {
        return this.meter_pictures[index];
    } */


    /**
     * Gets the name
     * @return Returns a String
     */
    public String getPumpName(int index)
    {
        //return this.meter_names[index];
        return null;
    } 


    public String[] getAvailablePumps()
    {
        return this.meter_names;
    }


    public String getPumpClassName(int index)
    {
        return this.meter_classes[index];
    }
/*
    public String getPumpDeviceClassName(int index)
    {
        return this.meter_device_classes[index];
    }
    

    public String getPumpClassName(String name)
    {
    int index = 0;

    for (int i=0; i<this.meter_names.length; i++)
    {
        if (this.meter_names.equals(name))
        {
        index = i;
        break;
        }
    }

    return this.meter_classes[index];
    }


    public static final int METER_INTERFACE_1 = 1;  // old meter interface (stephen)
    public static final int METER_INTERFACE_2 = 2;  // new meter interface (andy)

    public int getSelectedPumpIndex(int type, int index)
    {
	if (type==1)
	{
	    if (index<4)
	    {
		return index;
	    }
	    else
		return 0;
	}
	else if (type==2)
	{
	    if (index>3)
	    {
		return index;
	    }
	    else
		return 0;
	}
	else
	    return 0;

    }
*/
    
    public PumpDevice getPumpDevice(String group, String device)
    {
    	return this.getPumpDevice(group + "_" + device);
    }
    
    
    public PumpDevice getPumpDevice(String group_and_device)
    {
    	return this.meters_list.get(group_and_device);
    }
    
    
    
    
    public Hashtable<String,PumpCompany> groups = new Hashtable<String,PumpCompany>();
    public Hashtable<String,PumpDevice> meters_list = new Hashtable<String,PumpDevice>();
    
    
    public void loadPumpsDefinitions()
    {
    	Hashtable<String,String> defs = m_da.loadPropertyFile("PumpDefinition.properties");
    	
    	if (defs==null)
    	{
    		System.out.println("loadPumpDefinition failed !");
    		return;
    	}
    	
    	int last_group = Integer.parseInt(defs.get("LAST_GROUP"));
    	int last_meter = Integer.parseInt(defs.get("LAST_METER"));
    	
    	for(int i = 0; i<=last_group; i++)
    	{
    		PumpCompany mc = new PumpCompany(i, defs.get("GROUP_" + i + "_ID"), defs.get("GROUP_" + i + "_NAME"));

    		this.groups.put(mc.id, mc);
    		System.out.println(mc);
    	}
    	
    	for(int i = 1; i<=last_meter; i++)
    	{
    		PumpDevice md = new PumpDevice(
    				defs.get("DEVICE_" + i + "_ID"),
    				defs.get("DEVICE_" + i + "_GROUP"),
    				defs.get("DEVICE_" + i + "_NAME"),
    				defs.get("DEVICE_" + i + "_PICTURE"),
    				defs.get("DEVICE_" + i + "_CLASS"),
    				defs.get("DEVICE_" + i + "_STATUS"));
    				
    		this.groups.get(md.group).addDevice(md);
    		this.meters_list.put(md.getFullName(), md);
    	}
    	
    }

    


/*
    public Object[] getAvailablePumpsCombo()
    {
        Object oo[] = new Object[this.meter_names.length+1];
        oo[0] = this.m_ic.getMessage("NONE");

        for (int i=0; i<this.meter_names.length; i++) 
        {
            oo[i+1] = this.meter_names[i];
        }

        return oo;
    }
*/

}
