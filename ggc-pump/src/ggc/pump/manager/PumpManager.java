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

import ggc.plugin.device.DeviceInterface;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.pump.device.PumpInterface;
import ggc.pump.manager.company.Animas;
import ggc.pump.manager.company.Deltec;
import ggc.pump.manager.company.Insulet;
import ggc.pump.manager.company.Minimed;
import ggc.pump.manager.company.Roche;
import ggc.pump.manager.company.Sooil;
import ggc.pump.util.DataAccessPump;
import ggc.pump.util.I18nControl;

import java.util.Hashtable;
import java.util.Vector;

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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class PumpManager
{

    protected I18nControl m_ic = I18nControl.getInstance();
    protected DataAccessPump m_da = DataAccessPump.getInstance();
    

    protected Hashtable<String,PumpInterface> pump_devices;



    private Hashtable<String,AbstractDeviceCompany> companies_ht = new Hashtable<String,AbstractDeviceCompany>(); 
    private Vector<AbstractDeviceCompany> companies = new Vector<AbstractDeviceCompany>(); 
    private Vector<DeviceInterface> supported_devices = new Vector<DeviceInterface>(); 

    





    public static PumpManager s_manager = null;
    

    /**
     * Constructor for SerialPumpImport.
     */
    private PumpManager()
    {
        loadPumpCompanies();
        loadSupportedDevices();
    	this.pump_devices = new Hashtable<String,PumpInterface>();
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
     * @param index 
     * @return Returns a String
     */
    public String getPumpName(int index)
    {
        return this.pump_devices.get(index).getName();
    } 


    public String[] getAvailablePumps()
    {
        //return this.pump_devices.values().toArray(new String[]);
        return null;
    }


    public String getPumpClassName(int index)
    {
        return this.pump_devices.get(index).getDeviceClassName();
    }
    
    
    
    
    public void loadPumpCompanies()
    {
        addPumpCompany(new Animas());
        addPumpCompany(new Deltec());
        addPumpCompany(new Insulet());
        addPumpCompany(new Minimed());
        addPumpCompany(new Roche());
        addPumpCompany(new Sooil());
        //addMeterCompany(new PseudoMeters());
    }
    
    
    private void addPumpCompany(AbstractDeviceCompany company)
    {
        this.companies.add(company);
        this.companies_ht.put(company.getName(), company);
    }
    
    
    
    public void loadSupportedDevices()
    {
        // TODO add supported devices
        //this.supported_devices.addAll(new AscensiaBayer().getDevices());
        //this.supported_devices.addAll(new PseudoMeters().getDevices());
    }
    
    
    public Vector<AbstractDeviceCompany> getCompanies()
    {
        return this.companies;
    }
   
    
    public Vector<DeviceInterface> getSupportedDevices()
    {
        return this.supported_devices;
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
    
    public PumpInterface getPumpDevice(String group, String device)
    {
        // TODO
        return null;
        //return this.pump_devices.get(index).getName();

    	//return this.getPumpDevice(group + "_" + device);
    }
    
    
    public PumpInterface getPumpDevice(String group_and_device)
    {
//    	return this.meters_list.get(group_and_device);
    	return null;
    }
    
    public String getPumpDeviceClassName(String group, String device)
    {
        // TODO
        return null;
    }
    
    
//    public Hashtable<String,PumpCompany> groups = new Hashtable<String,PumpCompany>();
//    public Hashtable<String,PumpDevice> meters_list = new Hashtable<String,PumpDevice>();
    
/*    
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
*/
    


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
