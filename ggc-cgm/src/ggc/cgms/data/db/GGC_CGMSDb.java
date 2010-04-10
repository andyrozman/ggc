package ggc.cgms.data.db;

import ggc.cgms.data.CGMSDataReader;
import ggc.cgms.data.CGMSValuesEntry;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.db.hibernate.cgms.CGMSDataH;
import ggc.plugin.data.DeviceValuesDay;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.data.DeviceValuesRange;
import ggc.plugin.db.PluginDb;

import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;

import com.atech.db.hibernate.HibernateDb;
import com.atech.utils.ATechDate;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class GGC_CGMSDb extends PluginDb
{
    private static Log log = LogFactory.getLog(GGC_CGMSDb.class);
    DataAccessCGMS m_da = DataAccessCGMS.getInstance();

    /**
     * Constructor
     * 
     * @param db
     */
    public GGC_CGMSDb(HibernateDb db)
    {
        super(db);
        log.debug("Created CGMSDb");
        //getAllElementsCount();
    }
    
    
    /**
     * Get Daily Pump Values
     * 
     * @param gc
     * @return
     */
    public DeviceValuesDay getDailyCGMSValues(GregorianCalendar gc)
    {
//        DataAccessCGMS.notImplemented("GGC_CGMSDb::getDailyCGMSValues()");

//        DeviceValuesDay dV = new DeviceValuesDay(m_da);
//        dV.setDateTimeGC(gc);

        
        log.info("getCGMSDayStats()");

        long dt = ATechDate.getATDateTimeFromGC(gc, ATechDate.FORMAT_DATE_ONLY);
        //long dt = 20070323;
        
        DeviceValuesDay dV = new DeviceValuesDay(m_da);
        dV.setDateTimeGC(gc);
        
        
        
        String sql = "";
        
        try
        {
            sql = "SELECT dv from ggc.core.db.hibernate.cgms.CGMSDataH as dv WHERE dv.dt_info =  " + dt + " ORDER BY dv.dt_info ";  
//            + "000000 AND dv.dt_info <= " + dt +  "235959 ORDER BY dv.dt_info";
            
            Query q = this.db.getSession().createQuery(sql);

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                CGMSDataH pdh = (CGMSDataH)it.next();
                CGMSValuesEntry dv = new CGMSValuesEntry(pdh);
                
                dV.addList(dv.getSubEntryList());
            }
            
        }
        catch (Exception ex)
        {
            log.debug("Sql: " + sql);
            log.error("getCGMSStats(). Exception: " + ex, ex);
        }

        return dV;
 
  //      return null;
    }
    

    
    /**
     * Get Pump Values Range
     * 
     * @param from 
     * @param to 
     * @return
     */
    public DeviceValuesRange getRangePumpValues(GregorianCalendar from, GregorianCalendar to)
    {
        DataAccessCGMS.notImplemented("GGC_CGMSDb::getRangePumpValues()");
        return null;

        /*
        log.info("getPumpDayStats()");

        long dt_from = ATechDate.getATDateTimeFromGC(from, ATechDate.FORMAT_DATE_ONLY);
        long dt_to = ATechDate.getATDateTimeFromGC(to, ATechDate.FORMAT_DATE_ONLY);
        
        String sql = "";
        DeviceValuesRange dvr = new DeviceValuesRange(DataAccessPump.getInstance(), from, to);
        
        try
        {
            sql = "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpDataH as dv " + "WHERE dv.dt_info >=  " + dt_from  
            + "000000 AND dv.dt_info <= " + dt_to + "235959 ORDER BY dv.dt_info";
            
            //System.out.println("SQL: " + sql);
            
            Query q = this.db.getSession().createQuery(sql);

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                PumpDataH pdh = (PumpDataH)it.next();
                PumpValuesEntry dv = new PumpValuesEntry(pdh);
                
                dvr.addEntry(dv);
            }
            
            ArrayList<PumpValuesEntryExt> lst_ext = getRangePumpValuesExtended(from, to);
            mergeRangePumpData(dvr, lst_ext);
            
            
//            ArrayList<PumpValuesEntryExt> lst_ext = getDailyPumpValuesExtended(gc);
//            mergeDailyPumpData(dV, lst_ext);
            
            
        }
        catch (Exception ex)
        {
            log.debug("Sql: " + sql);
            log.error("getDayStats(). Exception: " + ex, ex);
        }

        return dvr;*/
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Get All Elements Count
     * 
     * @return
     */
    public int getAllElementsCount()
    {
        DataAccessCGMS.notImplemented("GGC_CGMSDb::getAllElementsCount()");
        
        Integer in = null;
        int sum_all = 0;
        
        /*
        CGMSInterface pe = (CGMSInterface)m_da.getSelectedDeviceInstance();
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(GregorianCalendar.MONTH, (-1) * pe.howManyMonthsOfDataStored());
        
        long dt_from = ATechDate.getATDateTimeFromGC(gc, ATechDate.FORMAT_DATE_ONLY) * 10000;
        */
        
        Criteria criteria = this.getSession().createCriteria(CGMSDataH.class);
        criteria.add(Expression.eq("person_id", (int)m_da.getCurrentUserId()));
        criteria.setProjection(Projections.rowCount());
        in = (Integer) criteria.list().get(0);
        sum_all = in.intValue();

        System.out.println("Pump Data : " + in.intValue());

        return sum_all;
    }
    
    
    /**
     * Get Pump Values
     * @param pdr
     * @return
     */
    public Hashtable<String, DeviceValuesEntryInterface> getCGMSValues(CGMSDataReader pdr)
    {
        String sql = "";

        Hashtable<String, DeviceValuesEntryInterface> dt = new Hashtable<String, DeviceValuesEntryInterface>(); 
        
        try
        {
            int counter = 0;
            
            sql = "SELECT dv from ggc.core.db.hibernate.cgms.CGMSDataH as dv WHERE dv.person_id=" + m_da.getCurrentUserId() + " ORDER BY dv.dt_info ";
            
            Query q = this.db.getSession().createQuery(sql);

            Iterator<?> it = q.list().iterator();
            
            pdr.writeStatus(-1);
            //id = "PD_%s_%s_%s";
            
            while (it.hasNext())
            {
                counter++;
                
                CGMSValuesEntry pve = new CGMSValuesEntry((CGMSDataH)it.next());
                dt.put(pve.getSpecialId(), pve);
                pdr.writeStatus(counter);
            }
            
            pdr.finishReading();   
        }
        catch(Exception ex)
        {
            //System.out.println("Exception on getCGMSValues: " + ex);
            log.error("Exception on getCGMSValues.\nsql:"+ sql + "\nEx: " + ex, ex);
        }
        
        return dt;
    }
    
    
}
