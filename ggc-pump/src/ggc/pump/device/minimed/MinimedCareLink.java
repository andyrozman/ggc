package ggc.pump.device.minimed;

import ggc.pump.util.DataAccessPump;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

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


public class MinimedCareLink
{
    DataAccessPump m_da = DataAccessPump.getInstance();
    int count_unk = 0; 

    public MinimedCareLink()
    {
        
    }
    
    public void parseExportFile(File file)
    {
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            int count=0;
            boolean inside = false;
            int cnl = 0;
            
            while((line = br.readLine())!=null)
            {
                count++;
//                Index,Date,Time,Timestamp,New Device Time,BG Reading (mmol/L),Linked BG Meter ID,Temp Basal Amount (U/h),Temp Basal Type,Temp Basal Duration (hh:mm:ss),Bolus Type,Bolus Volume Selected (U),Bolus Volume Delivered (U),Programmed Bolus Duration (hh:mm:ss),Prime Type,Prime Volume Delivered (U),Suspend,Rewind,BWZ Estimate (U),BWZ Target High BG (mmol/L),BWZ Target Low BG (mmol/L),BWZ Carb Ratio (grams),BWZ Insulin Sensitivity (mmol/L),BWZ Carb Input (grams),BWZ BG Input (mmol/L),BWZ Correction Estimate (U),BWZ Food Estimate (U),BWZ Active Insulin (U),Alarm,Sensor Calibration BG (mmol/L),Sensor Glucose (mmol/L),ISIG Value,Daily Insulin Total (U),Raw-Type,Raw-Values,Raw-ID,Raw-Upload ID,Raw-Seq Num,Raw-Device Type

                if (!inside)
                {
                    if (line.startsWith("DEVICE DATA"))
                        cnl++;
                    else
                    {
                        if (cnl>0)
                        {
                            cnl++;
                            
                            if (cnl==3)
                                inside = true;
                        }
                    }
                    
                    
                }
                else
                {
                    //System.out.println(""  + line);
                    
                    //if (count>30)
                    //    break;
                    if (line.length()>0)
                        readLineData(line, count);
                }
/*                
                System.out.println(""  + line);
                
                if (count>12)
                {
                    if (line.length()>0)
                        readLineData(line, count);
                } */
            }
            
            System.out.println("How many entries unknown: " + count_unk);
            
        }
        catch(Exception ex)
        {
            System.out.println("parseExportFile exception: " + ex);
        }
    }
//    ||||||||||||||||
//    ,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
    
    private void readLineData(String line, int count)
    {
        String[] ld = buildLineData(line);
        
        MinimedCareLinkData mcld = new MinimedCareLinkData(ld);

        if (!mcld.isIdentified())
        {
            /*
            if (mcld.getRawType().startsWith("Current"))
            {
                System.out.println(mcld.getRawType());
            }*/
            System.out.println(mcld);
            
            //System.out.println(mcld.getRawType());
            count_unk++;
        }
        
        
        //if (count==5105)
        //    showCollection(ld);
            
        //System.out.println(count + ": [size=" + ld.length + ",id=" + ld[0] + ",el33=" + ld[33] + "]");
        
    }
    
    
    void showCollection(String[] da)
    {
        StringBuffer sb = new StringBuffer();
        
        for(int i=0; i<da.length; i++)
        {
            sb.append(da[i]);
            sb.append("|");
        }
        
        System.out.println(sb.toString());
    }
    
    
    boolean multi_row = false;
    String multi_row_data = "";
    
    private String[] buildLineData(String line)
    {
        line = m_da.replaceExpression(line, ",,", ", ,");
        
        StringTokenizer strtok = new StringTokenizer(line, ",");
        ArrayList<String> elems = new ArrayList<String>();

        String[] ret = {};
        
        while(strtok.hasMoreTokens())
        {
            String item = strtok.nextToken().trim();
            
            if (item.length()==0)
                elems.add("");
            else
            {
                if (multi_row)
                {
                    multi_row_data += item;
                    if (item.endsWith("\""))
                    {
                        elems.add(multi_row_data);
                        multi_row_data = "";
                        multi_row = false;
                    }
                    else
                        multi_row_data += ", ";
                        
                }
                else
                {
                
                if (item.startsWith("\""))
                {
                    multi_row_data += item + ", ";
                    multi_row = true;
                }
                else
                    elems.add(item);
                
                }
                
            }
        }
        
        
        return elems.toArray(ret);
        
    }
    
    
    
    
    

}