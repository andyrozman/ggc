package ggc.pump.data.db;

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



public class GGCPumpDb
{
    private static Log log = LogFactory.getLog(GGCPumpDb.class);
    HibernateDb db;

    public GGCPumpDb(HibernateDb db)
    {
        this.db = db;
    }
    
    
    public PumpValuesDay getDailyPumpValues(GregorianCalendar gc)
    {
        log.info("getDayStats()");

        ATechDate atd = new ATechDate(ATechDate.DT_DATE, gc);
        
        PumpValuesDay dV = new PumpValuesDay();
        //dV.setDate(m_da.getDateTimeFromDateObject(day.getTime()) / 10000);

        try
        {
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //String sDay = sdf.format(day.getTime());

            Query q = this.db.getSession().createQuery(
                "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpDataH as dv " + "WHERE dv.dt_info >=  " + atd.getDateString()
                + "0000 AND dv.dt_info <= " + atd.getDateString() + "2359 ORDER BY dv.dt_info");

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                PumpValuesEntry dv = (PumpValuesEntry) it.next();
                
                dV.addEntry(dv);
            }

            // TODO extended
            
        }
        catch (Exception ex)
        {
            log.error("getDayStats(). Exception: " + ex, ex);
        }

        return dV;
        
    }
    
    
    public ArrayList<PumpValuesEntryExt> getDailyPumpValuesExtended(GregorianCalendar gc)
    {
        log.info("getDailyPumpValuesExtended() - Run");

        ATechDate atd = new ATechDate(ATechDate.DT_DATE, gc);
        
        //PumpValuesDay dV = new PumpValuesDay();
        //dV.setDate(m_da.getDateTimeFromDateObject(day.getTime()) / 10000);

        ArrayList<PumpValuesEntryExt> lst = new ArrayList<PumpValuesEntryExt>();
        
        try
        {
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            //String sDay = sdf.format(day.getTime());

            Query q = this.db.getSession().createQuery(
                "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpDataExtendedH as dv " + "WHERE dv.dt_info >=  " + atd.getDateString()
                + "0000 AND dv.dt_info <= " + atd.getDateString() + "2359 ORDER BY dv.dt_info");

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                PumpValuesEntryExt dv = (PumpValuesEntryExt) it.next();
                lst.add(dv);
            }

        }
        catch (Exception ex)
        {
            log.error("getDailyPumpValuesExtended(). Exception: " + ex, ex);
        }

        return lst;
        
    }
    
    
    
}
