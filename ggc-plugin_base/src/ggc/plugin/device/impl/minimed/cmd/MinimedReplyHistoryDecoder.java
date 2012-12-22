package ggc.plugin.device.impl.minimed.cmd;

import ggc.core.db.GGCDb;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.HexUtils;

public class MinimedReplyHistoryDecoder
{
    HexUtils hu = new HexUtils();
    private static Log log = LogFactory.getLog(MinimedReplyHistoryDecoder.class);
    
    public MinimedReplyHistoryDecoder()
    {
    }
    

    public void testDecode(byte[] data)
    {
        log.debug("Test Decode: " + data.length);

        
        
        for(int i=0; i<data.length; i++)
        {
            //System.out.print(data[i]);
            //System.out.print(" ");

            
            if (data[i] == 0x0B)
            {
                System.out.println("TAB");
            }
            else
            {
                if (data[i] != 0x00)
                {
                    System.out.print(hu.getHex(data[i]));
                    System.out.print(" ");
                }
            }
        }
    }
    
    public void displayLines(ArrayList<Byte> lst)
    {
        
    }
    
    
    public byte[] testReadFile(File file)
    {
         
            //log("Reading in binary file named : " + aInputFileName);
            //File file = new File(aInputFileName);
            log.debug("File size: " + file.length());
            byte[] result = new byte[(int)file.length()];
            try {
              InputStream input = null;
              try {
                int totalBytesRead = 0;
                input = new BufferedInputStream(new FileInputStream(file));
                while(totalBytesRead < result.length){
                  int bytesRemaining = result.length - totalBytesRead;
                  //input.read() returns -1, 0, or more :
                  int bytesRead = input.read(result, totalBytesRead, bytesRemaining); 
                  if (bytesRead > 0){
                    totalBytesRead = totalBytesRead + bytesRead;
                  }
                }
                /*
                 the above style is a bit tricky: it places bytes into the 'result' array; 
                 'result' is an output parameter;
                 the while loop usually has a single iteration only.
                */
                log.debug("Num bytes read: " + totalBytesRead);
              }
              finally {
                  log.debug("Closing input stream.");
                input.close();
              }
            }
            catch (FileNotFoundException ex) 
            {
                log.error("File not found.", ex);
            }
            catch (IOException ex) {
              log.error("Exception: " + ex, ex);
            }
            return result;
          
    }
    
    public byte[] postProcessTestData(byte[] data)
    {
        //if (data.length==4096)
           
        log.debug("Data length: " + data.length);
        
        byte[] dta = new byte[1024];
        int j = 0;
        
        for(int i=0; i<data.length; i++)
        {
            if ((i+1)%4==0)
            {
                dta[j] = data[i];
                j++;
            }
        }
        
        return dta;
        
    }
    
    
    public static void main(String[] args)
    {
        File f = new File("../test/mm/mm_history_cmd_128_page_0.data");
        MinimedReplyHistoryDecoder mrhd = new MinimedReplyHistoryDecoder();
        
        if (f.exists())
        {
            System.out.println("We have a file");
            
            byte[] dta = mrhd.testReadFile(f);
            mrhd.postProcessTestData(dta);
            dta = mrhd.postProcessTestData(dta);
            
            mrhd.testDecode(dta);
            
        }
        else
            System.out.println("NO file");
    }
    
    
    
    
    
    
    
    
    
}
