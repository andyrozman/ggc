package ggc.meter.data.cfg;

import ggc.meter.util.DataAccessMeter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Hashtable;

public class MeterConfiguration
{
    
    DataAccessMeter m_da = DataAccessMeter.getInstance();
    String config_text = "";
    File config_file = new File("../data/tools/MeterConfiguration.properties");
    
    MeterConfigEntry default_meter = null;
    
    public MeterConfiguration(boolean edit)
    {
        if (edit)
        {
            loadConfig();
        }
        else
        {
            processData();
        }
        
    }
    
    
    public void processData()
    {
        if (this.config_file.exists())
        {
            try
            {
                Hashtable<String,String> cfg = m_da.loadPropertyFile(this.config_file.getPath());
                
                if (cfg.containsKey("SELECTED_METER"))
                {
                    //System.out.println("selected meter");
                    int sel = Integer.parseInt(cfg.get("SELECTED_METER"));
                    
                    MeterConfigEntry mce = new MeterConfigEntry();
                    mce.readConfiguration(cfg, sel);
                    
                    
                    if (mce.isValid())
                        this.default_meter = mce;
                    
                }
                //else
                    //System.out.println("NO selected meter");
                    
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                this.default_meter = null;
            }
            
            
            
        }
    }
    
    
    public String getConfigText()
    {
        return this.config_text;
    }
    
    public void setConfigText(String txt)
    {
        this.config_text = txt;
    }
    
    public MeterConfigEntry getDefaultMeter()
    {
        return this.default_meter;
    }
    
    
    
    public void loadConfig()
    {
        if (config_file.exists())
        {
            try
            {
                BufferedReader br = new BufferedReader(new FileReader(this.config_file));
                
                String text = "";
                String line;
                
                while((line = br.readLine()) != null)
                {
                    text += line;
                    text += "\n";
                }
                
                this.config_text = text;
                
                br.close();
                
            }
            catch(Exception ex)
            {
                System.out.println("Error loading configuration: " + ex);
            }
        }
        else
        {
            this.config_text = getEmptyConfig();
        }
        
    }
    
    
    private String getEmptyConfig()
    {
        StringBuilder sb = new StringBuilder();
        
        sb.append("#\n");
        sb.append("#  Selected meter device\n");
        sb.append("#\n");
        sb.append("SELECTED_METER=1\n");
        sb.append("\n");
        sb.append("\n");
        sb.append("\n");
        sb.append("#\n");
        sb.append("#  Meter Device #1\n");
        sb.append("#\n");
        sb.append("METER_1_NAME=My meter\n");
        sb.append("METER_1_DEVICE=My meter\n");
        sb.append("METER_1_CONNECTION_PARAMETER=COM9\n");
        sb.append("\n");
        
        return sb.toString();
        
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


