
package ggc.core.plugins;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.meter.GlucoValueH;

import java.util.Hashtable;

import com.atech.db.DbDataReaderAbstract;
import com.atech.db.DbDataWriterAbstract;


public class GGCDataWriter extends DbDataWriterAbstract
{

    private boolean running = true;
    private GGCDb db = null;
    public static final int DATA_NONE = 0;
    public static final int DATA_METER = 1;
    
    int type = 0;
    Hashtable<String,GlucoValueH> data_meter = null;
    
    
    public GGCDataWriter(GGCDb db, int type, Object data)
    {
        super(type, data);
        this.db = db;
        this.data_meter = new Hashtable<String,GlucoValueH>();
        this.setStatus(DbDataWriterAbstract.STATUS_READY);
    }
    
    
    public void run()
    {

        while(running)
        {
            System.out.println("run.running Writer");

            try
            {
                this.setStatus(DbDataReaderAbstract.STATUS_READING);
    
                this.data_meter = this.db.getMeterValues();
                
                this.setStatus(DbDataReaderAbstract.STATUS_FINISHED_READING);
                
            }
            catch(Exception ex)
            {
                this.setStatus(DbDataReaderAbstract.STATUS_FINISHED_READING_ERROR);
                System.out.println("Exception: " + ex);
                ex.printStackTrace();
                running = false;
            }
            
            running = false;
            
        }  // while

        System.out.println("Exited runner");
    }





}
