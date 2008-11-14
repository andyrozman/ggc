package ggc.pump.data.db;

import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.plugin.db.PluginDb;
import ggc.pump.data.PumpValuesDay;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.PumpValuesEntryExt;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;

import com.atech.db.hibernate.HibernateDb;
import com.atech.utils.ATechDate;



public class GGCPumpDb extends PluginDb
{
    private static Log log = LogFactory.getLog(GGCPumpDb.class);

    public GGCPumpDb(HibernateDb db)
    {
        super(db);
    }
    
    
    public PumpValuesDay getDailyPumpValues(GregorianCalendar gc)
    {
        log.info("getDayStats()");

        //ATechDate atd = new ATechDate(ATechDate.DT_DATE, gc);
        
        long dt = ATechDate.getATDateTimeFromGC(gc, ATechDate.FORMAT_DATE_ONLY);
        
        PumpValuesDay dV = new PumpValuesDay();
        //dV.setDate(m_da.getDateTimeFromDateObject(day.getTime()) / 10000);

        String sql = "";
        
        try
        {
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //String sDay = sdf.format(day.getTime());

            sql = "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpDataH as dv " + "WHERE dv.dt_info >=  " + dt  /* atd.getDateString() */
            + "000000 AND dv.dt_info <= " + dt + /*atd.getDateString()*/ "235959 ORDER BY dv.dt_info";
            
            Query q = this.db.getSession().createQuery(sql);

            
//            System.out.println("sql base: " + "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpDataH as dv WHERE dv.dt_info >=  " + dt /*atd.getDateString() */
//                + "000000 AND dv.dt_info <= " + dt /*atd.getDateString()*/ + "235959 ORDER BY dv.dt_info");

            
            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                PumpValuesEntry dv = (PumpValuesEntry) it.next();
                
                dV.addEntry(dv);
            }
            
            System.out.println("Base entries: " + dV.getRowCount());

            // TODO extended
            ArrayList<PumpValuesEntryExt> lst_ext = getDailyPumpValuesExtended(gc);
            
            System.out.println("Extended list: " + lst_ext.size());
            
            dV.addExtendedEntries(lst_ext);
            
//            mergePumpData(dV, lst_ext);
            
        }
        catch (Exception ex)
        {
            log.debug("Sql: " + sql);
            log.error("getDayStats(). Exception: " + ex, ex);
        }

        return dV;
        
    }
    
    
    public ArrayList<PumpValuesEntryExt> getDailyPumpValuesExtended(GregorianCalendar gc)
    {
        log.info("getDailyPumpValuesExtended() - Run");

        //ATechDate atd = new ATechDate(ATechDate.FORMAT_DATE_ONLY, gc);
        
        
        long dt = ATechDate.getATDateTimeFromGC(gc, ATechDate.FORMAT_DATE_ONLY);
        
        //PumpValuesDay dV = new PumpValuesDay();
        //dV.setDate(m_da.getDateTimeFromDateObject(day.getTime()) / 10000);

//        System.out.println("dt: " + dt + "\n" + gc);
        
        ArrayList<PumpValuesEntryExt> lst = new ArrayList<PumpValuesEntryExt>();
        
        String sql = "";
        
        try
        {
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //String sDay = sdf.format(day.getTime());

            sql = "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpDataExtendedH as dv " + 
                  "WHERE dv.dt_info >=  " + dt + "000000 AND dv.dt_info <= " + 
                  dt + "235959 ORDER BY dv.dt_info";

            
            Query q = this.db.getSession().createQuery(sql);

            //System.out.println("Ext sql: " +                 "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpDataExtendedH as dv " + "WHERE dv.dt_info >=  " +dt
            //    + "000000 AND dv.dt_info <= " + dt /*atd.getDateString()*/ + "235959 ORDER BY dv.dt_info");

            
            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                PumpDataExtendedH pdh = (PumpDataExtendedH) it.next();
                
                
                PumpValuesEntryExt dv = new PumpValuesEntryExt(pdh);
                lst.add(dv);
            }

        }
        catch (Exception ex)
        {
            log.debug("Sql: " + sql);
            log.error("getDailyPumpValuesExtended(). Exception: " + ex, ex);
        }

        return lst;
        
    }
    
    
    
    
}
