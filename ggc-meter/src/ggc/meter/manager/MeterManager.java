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


package ggc.meter.manager; 

import ggc.meter.device.MeterInterface;
import ggc.meter.manager.company.Abbott;
import ggc.meter.manager.company.AbstractMeterCompany;
import ggc.meter.manager.company.AscensiaBayer;
import ggc.meter.manager.company.DiabeticSupplyOfSunCoast;
import ggc.meter.manager.company.HipoGuard;
import ggc.meter.manager.company.HomeDiagnostic;
import ggc.meter.manager.company.LifeScan;
import ggc.meter.manager.company.Menarini;
import ggc.meter.manager.company.Prodigy;
import ggc.meter.manager.company.PseudoMeters;
import ggc.meter.manager.company.Roche;
import ggc.meter.manager.company.Sanvita;
import ggc.meter.manager.company.USDiagnostic;
import ggc.meter.manager.company.Wavesense;
import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;

import java.util.Hashtable;
import java.util.Vector;

public class MeterManager
{

    protected I18nControl m_ic = I18nControl.getInstance();
    protected DataAccessMeter m_da = DataAccessMeter.getInstance();
    

    private Hashtable<String,AbstractMeterCompany> companies_ht = new Hashtable<String,AbstractMeterCompany>(); 
    private Vector<AbstractMeterCompany> companies = new Vector<AbstractMeterCompany>(); 
    private Vector<MeterInterface> supported_devices = new Vector<MeterInterface>(); 





    
    
    
    
    public static MeterManager s_manager = null;
    

    /**
     * Constructor for SerialMeterImport.
     */
    private MeterManager()
    {
    	this.loadMeterCompanies();
    	this.loadSupportedDevices();
    }

    
    public static MeterManager getInstance()
    {
    	if (MeterManager.s_manager==null)
    		MeterManager.s_manager = new MeterManager();
    	
    	return MeterManager.s_manager;
    }


    
    public void loadMeterCompanies()
    {
        addMeterCompany(new AscensiaBayer());
        addMeterCompany(new Roche());
        addMeterCompany(new LifeScan());
        addMeterCompany(new Abbott());
        addMeterCompany(new Menarini());
        addMeterCompany(new DiabeticSupplyOfSunCoast());
        addMeterCompany(new HipoGuard());
        addMeterCompany(new HomeDiagnostic());
        addMeterCompany(new Prodigy());
        addMeterCompany(new Sanvita());
        addMeterCompany(new USDiagnostic());
        addMeterCompany(new Wavesense());
        addMeterCompany(new PseudoMeters());
    }
    
    
    private void addMeterCompany(AbstractMeterCompany company)
    {
        this.companies.add(company);
        this.companies_ht.put(company.getName(), company);
    }
    
    
    public void loadSupportedDevices()
    {
        this.supported_devices.addAll(new AscensiaBayer().getDevices());
        this.supported_devices.addAll(new PseudoMeters().getDevices());
        this.supported_devices.addAll(new Roche().getDevices());
    }
    
    
    public Vector<AbstractMeterCompany> getCompanies()
    {
        return this.companies;
    }
   
    
    public Vector<MeterInterface> getSupportedDevices()
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
    
    public MeterInterface getMeterDevice(String group, String device)
    {
        AbstractMeterCompany cmp = getCompany(group);
        
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
        MeterInterface mi = getMeterDevice(group, device);
        return mi.getDeviceClassName();
    }
    
    
    public AbstractMeterCompany getCompany(String name)
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
