package ggc.meter.data.cfg;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MeterConfiguration
{
    
    String config_text = "";
    File config_file = new File("../data/tools/MeterConfiguration.properties");
    
    public MeterConfiguration(boolean edit)
    {
        if (edit)
        {
            loadConfig();
        }
        else
        {
            //throw new Exception("");
            System.out.println("Error processing data");
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
        File f = new File("../data");
        
        if (!f.exists())
            f.mkdir();

    
        f = new File("../data/tools");
        
        if (!f.exists())
            f.mkdir();
    
    }
    
    

}


