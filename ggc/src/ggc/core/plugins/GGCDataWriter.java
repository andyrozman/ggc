/*
 * Created on 12.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.core.plugins; //com.atech.db;

import ggc.core.db.GGCDb;
import ggc.core.db.datalayer.DailyValue;
import ggc.core.db.hibernate.DayValueH;
import ggc.core.util.DataAccess;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import com.atech.db.DbDataWriterAbstract;
import com.atech.graphics.components.StatusReporterInterface;



public class GGCDataWriter extends DbDataWriterAbstract 
{

    public static final int DATA_NONE = 0;
    public static final int DATA_METER = 1;
    
    protected int current_status = 0;
    protected StatusReporterInterface stat_rep_int = null;
    protected Hashtable<String, ArrayList<DayValueH>> meter_data;
    protected GGCDb db = null;
    
    public GGCDataWriter(int type, Object data, StatusReporterInterface stat_rep_int)
    {
        super(type, stat_rep_int);
        this.selected_data_type = type;
        
        this.meter_data = (Hashtable<String, ArrayList<DayValueH>>)data;
        //this.data = (ArrayList<MeterValuesEntry>)data;
        this.stat_rep_int = stat_rep_int;
        
        this.db = DataAccess.getInstance().getDb();
        
    }
    
    
    boolean running = true;
    
    public void run()
    {
        try
        {
            Thread.sleep(1000);
        }
        catch(Exception ex)
        {
        }
        
        while (running)
        {
            int full_count = 0;
            int current = 0;
            
            full_count += this.meter_data.get("ADD").size();
            full_count += this.meter_data.get("EDIT").size();
            
            for(Enumeration<String> en=this.meter_data.keys(); en.hasMoreElements(); )
            {
                String key = en.nextElement();
                ArrayList<DayValueH> lst = this.meter_data.get(key);
                
                for(int i=0; i<lst.size(); i++)
                {
                    if (key.equals("ADD"))
                    {
                        DayValueH dv = lst.get(i);
                        dv.setPerson_id(DataAccess.getInstance().getCurrentPersonId());
                        db.addHibernate(dv);
                    }
                    else
                    {
                        db.editHibernate(lst.get(i));
                    }
                    
                    current++;
                    
                    float f = (current/(full_count * 1.0f));
                    
                    int pr = (int)(100.0f * f);
                    
                    this.stat_rep_int.setStatus(pr);
                    
                } // for i
            } // for enum

            
            this.stat_rep_int.setStatus(100);
            this.running = false;

            
        } // while running
        
        
    }
  


}
