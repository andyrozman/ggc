/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.data.imports;


import ggc.datamodels.DailyValuesRow;
import ggc.event.ImportEventListener;


/**

 * @author stephan

 *

 * To change this generated comment edit the template variable "typecomment":

 * Window>Preferences>Java>Templates.

 */
public interface DataImport
{

    /**
     * Will be called, to initialize the data import.
     * @return boolean - initialize succesful or not
     */
    boolean open() throws ImportException;


    /**
     * Will be called, when the import is ended and freeing resources.
     */
    void close();


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
