package ggc.pump.device.minimed;

import java.util.Hashtable;

import ggc.plugin.device.PlugInBaseException;
import ggc.pump.device.AbstractSerialPump;

public abstract class MinimedDevice extends AbstractSerialPump
{

    // package received:
    //   1- 167 
    //   2,3,4 - Serial1 Serial2 Serial3 (BCD packed)
    //   5 - CMD1
    //   6 - CMD2
    //   
    
    
    // package sent:
    //   Header
    //   1 - 10 (isUseMultiXmitMode), 5 (ParameterCount=0), 4 (ParameterCount<>0)
    //   2 - Element count (bytes)
    
    //   Message
    //   1 - 167 
    //   2-4 - Serial1 Serial2 Serial3 (BCD packed)
    //   5 - CMD1
    //   6 - CMD2
    //   7 - CRC8 of this package
    //   encoded
    
    
    private void establishCommunication()
    {
        // setState(1); 
        // setState(2); // initializing
        //setPhase: phase is now 3 (Initializing Link Device: ComLink on COM4)
        // (rate/parity/data/stop/handshake): 57600 / NONE / 8 / 1 / NONE

        /*
        minimed.ddms.deviceportreader.SerialPort-readUntilEmpty: dumped 1 byte.

        INFO minimed.ddms.deviceportreader.SerialPort-write(byte)(766MS): writing <0x06>

        INFO minimed.ddms.deviceportreader.SerialPort-read()(16MS): read <0x33>

        minimed.ddms.deviceportreader.SerialPort-write(byte)(16MS): writing <0x02>

        minimed.ddms.deviceportreader.SerialPort-read()(16MS): read <0xE4>

        INFO minimed.ddms.deviceportreader.SerialPort-read()(16MS): read <0x00>

        INFO minimed.ddms.deviceportreader.SerialPort-read()(15MS): read <0x55>

        INFO minimed.ddms.deviceportreader.ComLink1-readStatus: CS status follows: NumberReceivedDataBytes=0, ReceivedData=false, RS232Mode=true, FilterRepeat=true, AutoSleep=true, StatusError=false, SelfTestError=false
        */
        
        // PHASE 4 - Init device
        //INFO deviceUpdatePhase: PHASE=4-Initializing Device: 316551; percent complete=1%

        
        
        
    }

    public boolean arePumpSettingsSet()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public Hashtable<String, Integer> getAlarmMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public float getBasalStep()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public Hashtable<String, Integer> getBolusMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public float getBolusStep()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public Hashtable<String, Integer> getErrorMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Hashtable<String, Integer> getEventMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getMaxMemoryRecords()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public Hashtable<String, Integer> getReportMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getTemporaryBasalTypeDefinition()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int howManyMonthsOfDataStored()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public void loadPumpSpecificValues()
    {
        // TODO Auto-generated method stub
        
    }

    public String getComment()
    {
        // TODO Auto-generated method stub
        return null;
    }



    public int getDownloadSupportType()
    {
        // TODO Auto-generated method stub
        return 0;
    }




    public boolean hasSpecialProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void readConfiguration() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }

    public void readDeviceDataFull() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }

    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }

    public void readInfo() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }
    
    
    
}
