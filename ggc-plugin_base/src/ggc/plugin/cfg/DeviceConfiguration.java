package ggc.plugin.cfg;

import ggc.plugin.util.DataAccessPlugInBase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Enumeration;
import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;

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
                        
                        System.out.println(mce);
                        
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
        // TODO
        
        File f = new File("../data/tools/MC_" + System.currentTimeMillis() + ".txt");
        
        StringBuilder sb = new StringBuilder();
        
        sb.append("SELECTED_" + this.dcd.getDevicePrefix() + "=" + this.default_device + "\n");
        
        for(int i=1; i<21; i++)
        {
            if (this.device_entries.containsKey("" + i))
            {
                DeviceConfigEntry dce = this.device_entries.get("" + i);
                
                dce.setDeviceConfigurationDefinition(dcd);
                dce.setId(i);
                
                sb.append(dce.getFileEntry());
            }
        }
        
        
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
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
        System.out.println("chekcDireStructure");
        File f = new File("../data");
        
        if (!f.exists())
            f.mkdir();

    
        f = new File("../data/tools");
        
        if (!f.exists())
            f.mkdir();
    
    }
    
    

}


