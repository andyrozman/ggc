package ggc.cgms.device.dexcom;

import ggc.cgms.data.CGMSValuesSubEntry;
import ggc.cgms.data.CGMSValuesTableModel;
import ggc.plugin.protocol.XmlProtocol;
import ggc.plugin.util.DataAccessPlugInBase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import com.atech.utils.file.FileReaderContext;

public class FRC_DexcomTxt_DM3 extends XmlProtocol implements FileReaderContext
{

    ArrayList<CGMSValuesSubEntry> list = new ArrayList<CGMSValuesSubEntry>();
    CGMSValuesTableModel cvtm = null;
    
    public FRC_DexcomTxt_DM3(DataAccessPlugInBase da)
    {
        super(da);
    }

    public String getFileDescription()
    {
        return "DM3 Dexcom Software Export";
    }

    public JPanel getFileDownloadPanel()
    {
        return null;
    }

    public String getFileExtension()
    {
        return ".txt";
    }

    public String getFullFileDescription()
    {
        return "DM3 Dexcom Software Export (TXT)";
    }
    
    
    public boolean hasSpecialSelectorDialog()
    {
        return false;
    }

    public void readFile(String filename)
    {
        try
        {
            cvtm = (CGMSValuesTableModel)m_da.getDeviceDataHandler().getDeviceValuesTableModel();
            
            BufferedReader br = new BufferedReader(new FileReader(filename));
            
            String line = null;
            
            while ((line=br.readLine()) != null)
            {
                processLine(line);
            }
        
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        System.out.println("i = " + i + " lisr: " + list.size());
        
    }
    
    
    int i = 0;
    String tmp_time;
    
    
    public void addEntry(CGMSValuesSubEntry entry)
    {
        this.list.add(entry);
        this.cvtm.addEntry(entry); 
    }
    
    
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
            addEntry(sub);
            
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
                addEntry(sub);
                
                //System.out.println("" + sub);
            }
            
        }
        
        
        i++;
        
    }

    public FileFilter getFileFilter()
    {
        
        return new FileFilter() 
        {

            @Override
            public boolean accept(File f)
            {
                if (f.isDirectory())
                    return true;
                
                return (f.getName().toLowerCase().endsWith(getFileExtension()));
            }

            @Override
            public String getDescription()
            {
                return getFileDescription() + " (" + getFileExtension() + ")";
            }
            
        };
        
        
    }

    public void goToNextDialog(JDialog currentDialog)
    {
    }
    
    
    
    
    public String toString()
    {
        return this.getFullFileDescription();
    }
    
    
    
    
    

}
