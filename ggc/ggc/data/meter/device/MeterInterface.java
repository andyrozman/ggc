/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.data.meter.device;


import java.util.ArrayList;

import javax.swing.ImageIcon;

import ggc.data.DailyValuesRow;
import ggc.data.event.ImportEventListener;
import ggc.data.imports.*;


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
public interface MeterInterface
{

    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    boolean open() throws MeterException;


    /**
     * Will be called, when the import is ended and freeing resources.
     */
    void close() throws MeterException;


    void loadInitialData();

    /**
     * getName - Get Name of meter
     */
    String getName();


    /**
     * getIcon - Get Icon of meter
     */
    ImageIcon getIcon();


    /**
     * getMeterIndex - Get Index of Meter 
     */
    int getMeterIndex();


    /**
     * getTimeDifference - returns time difference between Meter and Computer
     */
    int getTimeDifference(); 


    /**
     * getInfo - returns Meter information
     */
    String getInfo(); 

    
    /**
     * getStatus - get Status of meter
     */
    int getStatus();


    /**
     * isStatusOK - has Meter OK status
     */
    boolean isStatusOK();
    
    
    /**
     * getDataFull - get all data from Meter
     */
    ArrayList getDataFull();


    /**
     * getData - get data for specified time
     */
    ArrayList getData(int from, int to);
    

}
