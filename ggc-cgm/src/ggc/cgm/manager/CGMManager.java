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
 *  Filename: MeterManager.java
 *  Purpose:  This class contains all definitions for Meters. This includes:
 *        meter names, classes that handle meter and all other relevant data.
 *
 *  Author:   andyrozman
 */


package ggc.cgm.manager; 

import ggc.cgm.device.CGMInterface;
import ggc.cgm.manager.company.AbstractCGMCompany;
import ggc.cgm.util.DataAccessCGM;
import ggc.cgm.util.I18nControl;

import java.util.Hashtable;
import java.util.Vector;

public class CGMManager
{

    protected I18nControl m_ic = I18nControl.getInstance();
    protected DataAccessCGM m_da = DataAccessCGM.getInstance();
    

    private Hashtable<String,AbstractCGMCompany> companies_ht = new Hashtable<String,AbstractCGMCompany>(); 
    private Vector<AbstractCGMCompany> companies = new Vector<AbstractCGMCompany>(); 
    private Vector<CGMInterface> supported_devices = new Vector<CGMInterface>(); 





    
    
    
    
    public static CGMManager s_manager = null;
    

    /**
     * Constructor for SerialMeterImport.
     */
    private CGMManager()
    {
    	this.loadCGMCompanies();
    	this.loadSupportedDevices();
    }

    
    public static CGMManager getInstance()
    {
    	if (CGMManager.s_manager==null)
    	    CGMManager.s_manager = new CGMManager();
    	
    	return CGMManager.s_manager;
    }


    
    public void loadCGMCompanies()
    {
    }
    
    /*
    private void addCGMCompany(AbstractCGMCompany company)
    {
        this.companies.add(company);
        this.companies_ht.put(company.getName(), company);
    }*/
    
    
    public void loadSupportedDevices()
    {
        //this.supported_devices.addAll(new AscensiaBayer().getDevices());
    }
    
    
    public Vector<AbstractCGMCompany> getCompanies()
    {
        return this.companies;
    }
   
    
    public Vector<CGMInterface> getSupportedDevices()
    {
        return this.supported_devices;
    }

    /**
     * Gets the image
     * @return Returns a ImageIcon
     */
/*    public ImageIcon getMeterImage(int index)
    {
        return this.meter_pictures[index];
    } */


    /**
     * Gets the name
     * @return Returns a String
     */
/*    public String getMeterName(int index)
    {
        return this.meter_names[index];
    } */

/*
    public String[] getAvailableMeters()
    {
        return this.meter_names;
    }


    public String getMeterClassName(int index)
    {
        return this.meter_classes[index];
    }

    public String getMeterDeviceClassName(int index)
    {
        return this.meter_device_classes[index];
    }
    

    public String getMeterClassName(String name)
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

    public int getSelectedMeterIndex(int type, int index)
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
    
    public CGMInterface getMeterDevice(String group, String device)
    {
        AbstractCGMCompany cmp = getCompany(group);
        
        if (cmp==null)
        {
            System.out.println("Company not found !");
            System.out.println("companies_nt: " + this.companies_ht);
            return null;
        }
        
        return cmp.getDevice(device);
    }
    
    
    public String getMeterDeviceClassName(String group, String device)
    {
        //CGMInterface mi = getMeterDevice(group, device);
        //return mi.getDeviceClassName();
        return null;
    }
    
    
    public AbstractCGMCompany getCompany(String name)
    {
        if (this.companies_ht.containsKey(name))
        {
            return this.companies_ht.get(name);
        }
        else
            return null;
        
    }
    
    
    
    
    
    /*
    public Hashtable<String,MeterCompany> groups = new Hashtable<String,MeterCompany>();
    public Hashtable<String,MeterDevice> meters_list = new Hashtable<String,MeterDevice>();
    
    
    public void loadMetersDefinitions()
    {
    	Hashtable<String,String> defs = m_da.loadPropertyFile("MeterDefinition.properties");
    	
    	if (defs==null)
    	{
    		System.out.println("loadMeterDefinition failed !");
    		return;
    	}
    	
    	int last_group = Integer.parseInt(defs.get("LAST_GROUP"));
    	int last_meter = Integer.parseInt(defs.get("LAST_METER"));
    	
    	for(int i = 0; i<=last_group; i++)
    	{
    		MeterCompany mc = new MeterCompany(i, defs.get("GROUP_" + i + "_ID"), defs.get("GROUP_" + i + "_NAME"));

    		this.groups.put(mc.id, mc);
    		System.out.println(mc);
    	}
    	
    	for(int i = 1; i<=last_meter; i++)
    	{
    		MeterDevice md = new MeterDevice(
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
    public Object[] getAvailableMetersCombo()
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
