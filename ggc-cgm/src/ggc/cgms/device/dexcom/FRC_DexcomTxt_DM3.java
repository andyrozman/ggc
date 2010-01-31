package ggc.cgms.device.dexcom;

import ggc.cgms.data.CGMSValuesSubEntry;
import ggc.plugin.protocol.XmlProtocol;
import ggc.plugin.util.DataAccessPlugInBase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JPanel;

import com.atech.utils.file.FileReaderContext;

public class FRC_DexcomTxt_DM3 extends XmlProtocol implements FileReaderContext
{

    ArrayList<CGMSValuesSubEntry> list = new ArrayList<CGMSValuesSubEntry>();
    
    
    public FRC_DexcomTxt_DM3(DataAccessPlugInBase da)
    {
        super(da);
        // TODO Auto-generated constructor stub
    }

    public String getFileDescription()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public JPanel getFileDownloadPanel()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getFileExtension()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasSpecialSelectorDialog()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void readFile(String filename)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            
            String line = null;
            
            while ((line=br.readLine()) != null)
            {
                //System.out.println(line);
                
                processLine(line);
                
            }
            
        
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        System.out.println("i = " + i + " lisr: " + list.size());
        
        // TODO Auto-generated method stub

    }
    
    
    int i = 0;
    String tmp_time;
    
    private void processLine(String line)
    {
        
        //if (i>20)
        //    return;
        
        StringTokenizer strtok = new StringTokenizer(line, "\t");
        
        
        int count = strtok.countTokens();
        
        int read_type = 0;
        
        if (count==6)
        {
            read_type = 2;
        }
        else if (count==12)
        {
            read_type = 0;
        }
        else if (count==8)
        {
            strtok.nextToken();
            strtok.nextToken();
            read_type = 2;
        }
        else if (count==7)
        {
            strtok.nextToken();
            read_type = 2;
        }
        else if (count==3)
        {
            read_type = 1;
        }
        else
        {
            System.out.println("Tokens: " + count);
        }
        
        
        
        //if (read_type>0)
//        if (read_type == 20)
        if (read_type>0)
        {
            CGMSValuesSubEntry sub = new CGMSValuesSubEntry();
            tmp_time = strtok.nextToken();
            //sub.setDate(DexcomCGMS.getDateFromString(tmp_time));
            //sub.datetime = DexcomCGMS.getDateFromString(tmp_time);
            //sub.time = DexcomCGMS.getTimeFromString(tmp_time);
            sub.setDateTime(DexcomCGMS.getDateTimeFromString(tmp_time));
            sub.type = CGMSValuesSubEntry.CGMS_BG_READING;
            strtok.nextToken();
            sub.value = Integer.parseInt(strtok.nextToken());
            this.list.add(sub);
            
            if (read_type==2)
            {
                sub = new CGMSValuesSubEntry(); 
                tmp_time = strtok.nextToken();
                sub.setDateTime(DexcomCGMS.getDateTimeFromString(tmp_time));
                //sub.datetime = DexcomCGMS.getDateFromString(tmp_time);
                //sub.time = DexcomCGMS.getTimeFromString(tmp_time);
                sub.type = CGMSValuesSubEntry.METER_CALIBRATION_READING;
                strtok.nextToken();
                sub.value = Integer.parseInt(strtok.nextToken());
                this.list.add(sub);
                
                System.out.println("" + sub);
            }
            
        }
        
        
        
        
        
        
        
        
        /*
        while(strtok.hasMoreTokens())
        {
            System.out.print(strtok.nextToken() + " | ");
        }
        System.out.println(); */
        i++;
        
        
        
    }
    
    
    
    
    
    
    
    
    
    

}
