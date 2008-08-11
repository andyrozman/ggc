package ggc.cgm.device.minimed;

import ggc.cgm.util.DataAccessCGM;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MinimedCareLinkData
{
    DataAccessCGM m_da = DataAccessCGM.getInstance();
  
    private String index;           // 0
    private String date;            // 1
    private String time;            // 2 
    private String raw_type;        // 33
    private String raw_values;      // 34
    private String raw_id;          // 35
    private String raw_upload_id;   // 36
    private String raw_seq_num;     // 37
    private String raw_device_type; // 38
    
    
//  Index,Date,Time,Timestamp,New Device Time,BG Reading (mmol/L),Linked BG Meter ID,Temp Basal Amount (U/h),
//  Temp Basal Type,Temp Basal Duration (hh:mm:ss),Bolus Type,Bolus Volume Selected (U),Bolus Volume Delivered (U),
//  Programmed Bolus Duration (hh:mm:ss),Prime Type,Prime Volume Delivered (U),Suspend,Rewind,BWZ Estimate (U),
//  BWZ Target High BG (mmol/L),BWZ Target Low BG (mmol/L),BWZ Carb Ratio (grams),BWZ Insulin Sensitivity (mmol/L),
//  BWZ Carb Input (grams),BWZ BG Input (mmol/L),BWZ Correction Estimate (U),BWZ Food Estimate (U),
//  BWZ Active Insulin (U),Alarm,Sensor Calibration BG (mmol/L),Sensor Glucose (mmol/L),ISIG Value,
//  Daily Insulin Total (U),Raw-Type,Raw-Values,Raw-ID,Raw-Upload ID,Raw-Seq Num,Raw-Device Type

    public MinimedCareLinkData(String[] data)
    {
        this.index = data[0];
        this.date = data[1];
        this.time = data[2];

        this.raw_type = data[33];        // 33
        this.raw_values = data[34];      // 34
        this.raw_id = data[35];          // 35
        this.raw_upload_id = data[36];   // 36
        this.raw_seq_num = data[37];     // 37
        this.raw_device_type = data[38]; // 38
    }
    
    
    
    
//    System.out.println(count + ": [size=" + ld.length + ",id=" + ld[0] + ",el33=" + ld[33] + "]");

    public String toString()
    {
        return "MinimedCareLinkData [index=" + this.index + "date=" + this.date + ",time=" + this.time + ",raw_type=" + this.raw_type +
               ",raw_values=" + this.raw_values + ",raw_device_type=" + this.raw_device_type + "]"; 
//               "\nraw_id=" + this.raw_id + ",raw_upload_id=" + this.raw_upload_id + ",raw_seq_num=" + this.raw_seq_num;
    }
    

}