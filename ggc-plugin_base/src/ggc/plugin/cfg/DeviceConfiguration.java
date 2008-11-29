package ggc.plugin.cfg;

import ggc.plugin.util.DataAccessPlugInBase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Enumeration;
import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;

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


public class DeviceConfiguration
{
    
    DataAccessPlugInBase m_da = null;
    String config_text = "";
    File config_file = null;
    I18nControlAbstract ic;
    //MeterConfigEntry default_meter = null;
    
    Hashtable<String,DeviceConfigEntry> device_entries;
    //Hashtable<String,DeviceConfigEntry> device_entries_old;

    String default_device = "";
    DeviceConfigurationDefinition dcd;
    String dev_prefix;
    
    public DeviceConfiguration(DataAccessPlugInBase da)
    {
        this.m_da = da;
        device_entries = new Hashtable<String,DeviceConfigEntry>();

        init();
        readConfigData();
    }
    
    private void init()
    {
        ic = m_da.getI18nControlInstance();
        dcd = m_da.getDeviceConfigurationDefinition();
        config_file = new File(dcd.getDevicesConfigurationFile());
        dev_prefix = dcd.getDevicePrefix();
        
    }
    
    
    public Hashtable<String,DeviceConfigEntry> getConfigDataCopy()
    {
        Hashtable<String,DeviceConfigEntry> devent_new = new Hashtable<String,DeviceConfigEntry>();
        
        for(Enumeration<String> en = this.device_entries.keys(); en.hasMoreElements(); )
        {
            String key = en.nextElement();
            devent_new.put(key, this.device_entries.get(key).clone());
        }
        
        return devent_new;
        
    }
    
    
    public void readConfigData()
    {
        
        if (this.config_file.exists())
        {
            try
            {
                Hashtable<String,String> cfg = m_da.loadPropertyFile(this.config_file.getPath());
                
                if (cfg.containsKey("SELECTED_" + dev_prefix))
                {
                    //System.out.println("selected meter");
                    int sel = Integer.parseInt(cfg.get("SELECTED_" + dev_prefix));
                    this.default_device = "" + sel;
                }
                
                for(int i=1; i<21; i++)
                {
                    if (cfg.containsKey(dev_prefix + "_" + i + "_DEVICE"))
                    {
                        DeviceConfigEntry mce = new DeviceConfigEntry(dcd, ic);
                        mce.readConfiguration(cfg, i);
                        
                        this.device_entries.put("" + i, mce);
                        
                        //System.out.println(mce);
                        
                        //this.device_entries_old.put("" + i, mce.clone());
                    }
                }
                
                    
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                //this.default_meter = null;
            }
            
            
            
        }
    }
    
    public DeviceConfigEntry getSelectedDeviceInstance()
    {
        if ((this.default_device == null) || (this.default_device.length()==0))
            return null;
        else
        {
            if (this.device_entries.containsKey(this.default_device))
                return this.device_entries.get(this.default_device);
            else
                return null;
        }
        
    }
    
    
    public String getSelectedDeviceIndex()
    {
        return this.default_device;
    }
    
    
    public void setNewConfigData(Hashtable<String,DeviceConfigEntry> data)
    {
        this.device_entries = data;
    }
    
    public void setSelectedDevice(String sel)
    {
        this.default_device = sel;
    }

    public void setSelectedDeviceFromConfigurator(String sel)
    {
        if (sel.startsWith(ic.getMessage("NEW__")))
        {
            String id = sel.substring(sel.indexOf("[")+1, sel.indexOf("]"));
            setSelectedDevice(id);
        }
        else
        {
            String id = sel.substring(0, sel.indexOf(" ")).trim();
            setSelectedDevice(id);
        }
    }
    
    
    public void setSelectedDevice(int sel)
    {
        this.default_device = "" + sel;
    }

    
    public void writeConfigData()
    {
        checkDirectoryStructure();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("#\n# " + ic.getMessage("DEVICE_NAME_BIG") + " Tool (v" + m_da.getPlugInVersion() + ") Configurator\n");
        sb.append("#\n# Generated by software, please don't edit by hand.\n");
        sb.append("# Created: " + m_da.getCurrentDateTimeString() + "\n#\n");
        
        sb.append("\n\n#\n#  Selected " + ic.getMessage("DEVICE_NAME_BIG") + " device\n#\n");
        
        sb.append("SELECTED_" + this.dcd.getDevicePrefix() + "=" + this.default_device + "\n");
        
        for(int i=1; i<21; i++)
        {
            if (this.device_entries.containsKey("" + i))
            {
                DeviceConfigEntry dce = this.device_entries.get("" + i);

                sb.append("\n\n#\n#  " + ic.getMessage("DEVICE_NAME") + " Device #" + i + " - " + dce.name + "\n#\n");
                
                dce.setDeviceConfigurationDefinition(dcd);
                dce.setId(i);
                
                sb.append(dce.getFileEntry());
            }
        }
        
        
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.dcd.getDevicesConfigurationFile()));
            bw.write(sb.toString());
            bw.flush();
            bw.close();
        }
        catch(Exception ex)
        {
            System.out.println("Error writing to configuration: " + ex);
        }
        
    }
    
    
    
    public void saveConfig()
    {
        try
        {
            
            checkDirectoryStructure();
            
            if (!this.config_file.exists())
                this.config_file.createNewFile();
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.config_file));
            
            bw.write(this.config_text);
            bw.flush();
            bw.close();
        }
        catch(Exception ex)
        {
            System.out.println("Error saving configuration: " + ex);
        }
        
    }
    
    
    public void checkDirectoryStructure()
    {
        File f = new File("../data");
        
        if (!f.exists())
            f.mkdir();

    
        f = new File("../data/tools");
        
        if (!f.exists())
            f.mkdir();
    
    }
    
    

}

