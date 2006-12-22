/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.data.meter.device;


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




    String getVersion() throws MeterException;








    /**
     * Import the data from the resource.
     */
    void importData() throws ImportException;


    /**
     * Return a list with all imported data values.
     * @return DailyValuesRow[]
     */
    DailyValuesRow[] getImportedData();


    void addImportEventListener(ImportEventListener listener);


    void removeImportEventListener(ImportEventListener listener);
}
