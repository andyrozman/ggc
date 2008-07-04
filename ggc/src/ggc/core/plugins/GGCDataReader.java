/*
 * Created on 12.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.core.plugins;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.DayValueH;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.db.DbDataReaderAbstract;


public class GGCDataReader extends DbDataReaderAbstract
{

    private boolean running = true;
    private GGCDb db = null;
    public static final int DATA_NONE = 0;
    public static final int DATA_METER = 1;
    
    int type = 0;
    Hashtable<String,DayValueH> data_meter = null;
    private static Log log = LogFactory.getLog(GGCDataReader.class);

    
    public GGCDataReader(GGCDb db, int type)
    {
        this.db = db;
        this.type = type;
        this.data_meter = new Hashtable<String,DayValueH>();
        this.setStatus(DbDataReaderAbstract.STATUS_READY);
    }
    
    
    public void run()
    {

        while(running)
        {
            //System.out.println("run.running");
            log.info("GGCDataReader - Started");
            
            try
            {
                this.setStatus(DbDataReaderAbstract.STATUS_READING);
    
                this.data_meter = this.db.getMeterValues();
                
                this.setStatus(DbDataReaderAbstract.STATUS_FINISHED_READING);
                
            }
            catch(Exception ex)
            {
                this.setStatus(DbDataReaderAbstract.STATUS_FINISHED_READING_ERROR);
                //System.out.println("Exception: " + ex);
                log.error("GGCDataReader Exception: " + ex, ex);
                //ex.printStackTrace();
                running = false;
            }
            
            running = false;
            
        }  // while

        log.info("GGCDataReader - Finished");
        //System.out.println("Exited runner");
    }

    /* 
     * getData
     */
    @Override
    public Object getData()
    {
        return this.data_meter;
    }

    /* 
     * getTypeOfData
     */
    @Override
    public int getTypeOfData()
    {
        return this.type;
    }
    


    



    




}
