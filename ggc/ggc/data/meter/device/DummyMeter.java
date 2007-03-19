/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.data.meter.device;

import javax.swing.ImageIcon;
import java.util.ArrayList;

import ggc.data.DailyValuesRow;
import ggc.data.event.ImportEventListener;
import ggc.data.imports.*;
import ggc.util.DataAccess;
import ggc.util.I18nControl;


/**

 * @author stephan

 *

 * To change this generated comment edit the template variable "typecomment":

 * Window>Preferences>Java>Templates.

 * 
 *  This is new interface, that will in future replace DataImport, but so far we are 
 *  keeping all old methods.
 * 
 * 
 * 
 */
public class DummyMeter implements MeterInterface
{

    DataAccess m_da = DataAccess.getInstance();
    I18nControl m_ic = m_da.getI18nInstance();

    public int m_meter_index = 0;
    

    

    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    public boolean open()
    {
        return true;
    }


    /**
     * Will be called, when the import is ended and freeing resources.
     */
    public void close()
    {
        return;
    }


    public static final int LOG_DEBUG = 1;
    public static final int LOG_INFO = 2;
    public static final int LOG_WARN = 3;
    public static final int LOG_ERROR = 4;
    
    public void writeLog(int problem, String text)
    {
        System.out.println("Dummy -> " + text);
    }


    public String getInfo() //throws MeterException
    {
        writeLog(LOG_DEBUG, "getVersion() - Start");
        writeLog(LOG_DEBUG, "getVersion() - End");
        return "Dummy Meter\n" +
               "v0.1\n" +
               m_ic.getMessage("DUMMY_INFO_TEXT");
    }

    

    public int getTimeDifference() // throws MeterException
    {
        writeLog(LOG_DEBUG, "getTimeDifference() - Start");
        writeLog(LOG_DEBUG, "getTimeDifference() - End");
        return 0;
    }


    public int getStatus()
    {
        return 0;
    }
    
    public boolean isStatusOK()
    {
        return true;
    }



    // Internal methods


    public String getName()
    {
        return "Dummy Meter";
    }

    public int getMeterIndex()
    {
        return m_meter_index;
    }

    
    public ImageIcon getIcon()
    {
        return m_da.getMeterManager().getMeterImage(1); //m_meter_index);
    }

    public ArrayList getDataFull()
    {
        writeLog(LOG_DEBUG, "getDataFull() - Start");
        ArrayList al = new ArrayList();
        writeLog(LOG_DEBUG, "getDataFull() - End");
        return al;
    }

    public ArrayList getData(int from, int to)
    {
        writeLog(LOG_DEBUG, "getData() - Start");
        ArrayList al = new ArrayList();
        writeLog(LOG_DEBUG, "getData() - End");
        return al;
    }


    public void loadInitialData()
    {
        // TODO Auto-generated method stub
        
    }





    public void readDeviceData() throws MeterException
    {
    }




    //************************************************
    //***          Process Meter Data              ***
    //************************************************


    /**
     * processMeterDataMain - this is main method for processing data. It should be called on all data received, and 
     * from here it should be sent to other process* methods. This methods are meant to be used, but don't have to 
     * be used if we have other ways to get data for methods needed (methods marked as used in Meter GUI)
     */
    public void processMeterData(String data) {}

    /**
     * processMeterIdentification - this should be used to process identification of meter and versions of firmware.
     */
    public void processMeterIdentification(String data) {}

    /**
     * processMeterTime - this should be used to process time and date of meter
     */
    public void processMeterTime(String data) {}

    /**
     * processMeterBGEntry - this should be used to process BG data from meter
     */
    public void processMeterBGEntry(String data) {}



    //************************************************
    //***                    Test                  ***
    //************************************************

    public void test()
    {
    }







}
