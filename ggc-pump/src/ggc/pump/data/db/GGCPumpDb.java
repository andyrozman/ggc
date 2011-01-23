package ggc.pump.data.db;

import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.core.db.hibernate.pump.PumpDataH;
import ggc.core.db.hibernate.pump.PumpProfileH;
import ggc.plugin.data.DeviceValuesDay;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.data.DeviceValuesRange;
import ggc.plugin.db.PluginDb;
import ggc.plugin.graph.data.GraphValuesCapable;
import ggc.plugin.graph.data.GraphValuesCollection;
import ggc.plugin.graph.data.PlugInGraphDb;
import ggc.pump.data.PumpDataReader;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.PumpValuesEntryExt;
import ggc.pump.data.PumpValuesEntryProfile;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.db.PumpProfile;
import ggc.pump.device.PumpInterface;
import ggc.pump.util.DataAccessPump;

import java.util.ArrayList;
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
import com.atech.utils.data.ATechDate;

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


public class GGCPumpDb extends PluginDb implements PlugInGraphDb
{
    private static Log log = LogFactory.getLog(GGCPumpDb.class);
    DataAccessPump m_da = DataAccessPump.getInstance();

    /**
     * Constructor
     * 
     * @param db
     */
    public GGCPumpDb(HibernateDb db)
    {
        super(db);
        
        //getAllElementsCount();
    }
    
    
    /**
     * Get Daily Pump Values
     * 
     * @param gc
     * @return
     */
    public DeviceValuesDay getDailyPumpValues(GregorianCalendar gc)
    {
        log.info("getPumpDayStats()");

        long dt = ATechDate.getATDateTimeFromGC(gc, ATechDate.FORMAT_DATE_ONLY);
        
        DeviceValuesDay dV = new DeviceValuesDay(m_da);
        dV.setDateTimeGC(gc);
        
        
        String sql = "";
        
        try
        {
            sql = "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpDataH as dv " + "WHERE dv.dt_info >=  " + dt  /* atd.getDateString() */
            + "000000 AND dv.dt_info <= " + dt + /*atd.getDateString()*/ "235959 ORDER BY dv.dt_info";
            
            Query q = this.db.getSession().createQuery(sql);

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                PumpDataH pdh = (PumpDataH)it.next();
                PumpValuesEntry dv = new PumpValuesEntry(pdh);
                
                dV.addEntry(dv);
            }
            
            ArrayList<PumpValuesEntryExt> lst_ext = getDailyPumpValuesExtended(gc);
            mergeDailyPumpData(dV, lst_ext);
        }
        catch (Exception ex)
        {
            log.debug("Sql: " + sql);
            log.error("getDayStats(). Exception: " + ex, ex);
        }

        return dV;
   
//        return null;
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
        log.info("getPumpDayStats()");

        long dt_from = ATechDate.getATDateTimeFromGC(from, ATechDate.FORMAT_DATE_ONLY);
        long dt_to = ATechDate.getATDateTimeFromGC(to, ATechDate.FORMAT_DATE_ONLY);
        
        String sql = "";
        DeviceValuesRange dvr = new DeviceValuesRange(DataAccessPump.getInstance(), from, to);
        
        try
        {
            sql = "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpDataH as dv " + "WHERE dv.dt_info >=  " + dt_from  /* atd.getDateString() */
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

        return dvr;
    }
    
    
    
    
    
    /**
     * Get Daily Pump Values Extended
     * 
     * @param gc
     * @return
     */
    public ArrayList<PumpValuesEntryExt> getDailyPumpValuesExtended(GregorianCalendar gc)
    {
        log.info("getDailyPumpValuesExtended() - Run");

        long dt = ATechDate.getATDateTimeFromGC(gc, ATechDate.FORMAT_DATE_ONLY);
        
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

    
    /**
     * Get Daily Pump Values Extended
     * 
     * @param from 
     * @param to 
     * @return
     */
    public ArrayList<PumpValuesEntryExt> getRangePumpValuesExtended(GregorianCalendar from, GregorianCalendar to)
    {
        log.info("getRangePumpValuesExtended() - Run");

        long dt_from = ATechDate.getATDateTimeFromGC(from, ATechDate.FORMAT_DATE_ONLY);
        long dt_to = ATechDate.getATDateTimeFromGC(to, ATechDate.FORMAT_DATE_ONLY);
        
        ArrayList<PumpValuesEntryExt> lst = new ArrayList<PumpValuesEntryExt>();
        
        String sql = "";
        
        try
        {
            sql = "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpDataExtendedH as dv " + 
                  "WHERE dv.dt_info >=  " + dt_from + "000000 AND dv.dt_info <= " + 
                  dt_to + "235959 ORDER BY dv.dt_info";

            Query q = this.db.getSession().createQuery(sql);

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
    
    
    
    
    /**
     * Merge Daily Pump Data
     * 
     * @param dV
     * @param lst_ext
     */
    public void mergeDailyPumpData(DeviceValuesDay dV, ArrayList<PumpValuesEntryExt> lst_ext)
    {
        for(int i=0; i<lst_ext.size(); i++)
        {
            PumpValuesEntryExt pvex = lst_ext.get(i);
            
            //System.out.println(pvex.getDt_info());
            
            if (dV.isEntryAvailable(pvex.getDt_info()))
            {
                
                PumpValuesEntry pve = (PumpValuesEntry)dV.getEntry(pvex.getDt_info());
                pve.addAdditionalData(pvex);
            }
            else
            {
                
                PumpValuesEntry pve = new PumpValuesEntry();
                pve.setDateTimeObject(new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, pvex.getDt_info()));
                pve.setBaseType(PumpBaseType.PUMP_DATA_ADDITIONAL_DATA);
                
                pve.addAdditionalData(pvex);
                
                dV.addEntry(pve);
            }
        }
    }

    
    
    /**
     * Merge Daily Pump Data
     * 
     * @param dvr 
     * @param lst_ext
     */
    public void mergeRangePumpData(DeviceValuesRange dvr, ArrayList<PumpValuesEntryExt> lst_ext)
    {
        
        // FIXME
        
        for(int i=0; i<lst_ext.size(); i++)
        {
            PumpValuesEntryExt pvex = lst_ext.get(i);
            
            DeviceValuesDay dvd;
            
            if (dvr.isDayEntryAvailable(pvex.getDt_info()))
            {
                //System.out.println("DeviceValuesDay Found");
                dvd = dvr.getDayEntry(pvex.getDt_info());
            }
            else
            {
                //System.out.println("DeviceValuesDay Created");
                ATechDate atd = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, pvex.getDt_info());
                dvd = new DeviceValuesDay(m_da, atd.getGregorianCalendar());
                dvr.addEntry(dvd);
            }
            
            if (dvd.isEntryAvailable(pvex.getDt_info()))
            {
                //System.out.println("PumpValuesEntry Found");
                
                PumpValuesEntry pve = (PumpValuesEntry)dvd.getEntry(pvex.getDt_info());
                pve.addAdditionalData(pvex);
            }
            else
            {
                //System.out.println("PumpValuesEntry Created");

                PumpValuesEntry pve = new PumpValuesEntry();
                pve.setDateTimeObject(new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, pvex.getDt_info()));
                pve.setBaseType(PumpBaseType.PUMP_DATA_ADDITIONAL_DATA);
                
                pve.addAdditionalData(pvex);
                
                dvd.addEntry(pve);
            }
        }
        
    }
    
    
    
    /**
     * Get Profiles
     * 
     * @return
     */
    public ArrayList<PumpProfile> getProfiles()
    {
        log.info("getProfiles() - Run");

        ArrayList<PumpProfile> lst = new ArrayList<PumpProfile>();
        String sql = "";
        
        try
        {
            sql = "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpProfileH as dv "; 
            
            Query q = this.db.getSession().createQuery(sql);

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                PumpProfileH pdh = (PumpProfileH) it.next();
                lst.add(new PumpProfile(pdh));
            }

        }
        catch (Exception ex)
        {
            log.debug("Sql: " + sql);
            log.error("getProfiles(). Exception: " + ex, ex);
        }

        return lst;
        
    }
    
    
    /**
     * Get All Elements Count
     * 
     * @return
     */
    public int getAllElementsCount()
    {
        Integer in = null;
        int sum_all = 0;
        
        PumpInterface pe = (PumpInterface)m_da.getSelectedDeviceInstance();
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(GregorianCalendar.MONTH, (-1) * pe.howManyMonthsOfDataStored());
        
        long dt_from = ATechDate.getATDateTimeFromGC(gc, ATechDate.FORMAT_DATE_ONLY) * 10000;
        
        
        Criteria criteria = this.getSession().createCriteria(PumpDataH.class);
        criteria.add(Expression.eq("person_id", (int)m_da.getCurrentUserId()));
        criteria.add(Expression.ge("dt_info", dt_from));
        criteria.setProjection(Projections.rowCount());
        in = (Integer) criteria.list().get(0);
        sum_all = in.intValue();

        System.out.println("Pump Data : " + in.intValue());

        criteria = this.getSession().createCriteria(PumpDataExtendedH.class);
        criteria.add(Expression.eq("person_id", (int)m_da.getCurrentUserId()));
        criteria.add(Expression.ge("dt_info", dt_from));
        //criteria.add(Restrictions.gt("id", minLogID));
        criteria.setProjection(Projections.rowCount());
        in = (Integer) criteria.list().get(0);
        sum_all += in.intValue();
        
        System.out.println("Pump Extended Data : " + in.intValue());
        
        criteria = this.getSession().createCriteria(PumpProfileH.class);
        criteria.add(Expression.eq("person_id", (int)m_da.getCurrentUserId()));
        criteria.add(Expression.ge("active_from", dt_from));
        criteria.setProjection(Projections.rowCount());
        in = (Integer) criteria.list().get(0);
        sum_all += in.intValue();
        
        System.out.println("Pump Profiles : " + in.intValue());
        
        return sum_all;
    }
    
    
    /**
     * Get Pump Values
     * @param pdr
     * @return
     */
    public Hashtable<String, DeviceValuesEntryInterface> getPumpValues(PumpDataReader pdr)
    {

        String sql = "";

        PumpInterface pe = (PumpInterface)m_da.getSelectedDeviceInstance();
        
        
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(GregorianCalendar.MONTH, (-1) * pe.howManyMonthsOfDataStored());
        
        Hashtable<String, DeviceValuesEntryInterface> dt = new Hashtable<String, DeviceValuesEntryInterface>(); 
        
        long dt_from = ATechDate.getATDateTimeFromGC(gc, ATechDate.FORMAT_DATE_ONLY);

        //String id = "";
        
        try
        {
            int counter = 0;
            
            sql = "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpDataH as dv " + "WHERE dv.dt_info >=  " + dt_from  /* atd.getDateString() */
            + "000000 and dv.person_id=" + m_da.getCurrentUserId() + " ORDER BY dv.dt_info ";
            
            Query q = this.db.getSession().createQuery(sql);

            Iterator<?> it = q.list().iterator();
            
            pdr.writeStatus(-1);
            //id = "PD_%s_%s_%s";
            
            while (it.hasNext())
            {
                counter++;
                
                PumpValuesEntry pve = new PumpValuesEntry((PumpDataH)it.next());
                //PumpDataH pdh = (PumpDataH)it.next();
                
                dt.put(pve.getSpecialId(), pve);
                /*    
                    String.format(id, 
                                     pdh.getDt_info(),
                                     pdh.getBase_type(),
                                     pdh.getSub_type()) , pdh); */
                
                pdr.writeStatus(counter);
            }
            
            
            sql = "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpDataExtendedH as dv " + 
                  "WHERE dv.dt_info >=  " + dt_from + "000000 and dv.person_id=" + m_da.getCurrentUserId() + " ORDER BY dv.dt_info ";

            q = this.db.getSession().createQuery(sql);

            it = q.list().iterator();
            
            pdr.writeStatus(-1);
            //id = "PE_%s_%s";

            while (it.hasNext())
            {
                counter++;

                PumpValuesEntryExt pvex = new PumpValuesEntryExt((PumpDataExtendedH) it.next());
                /*PumpDataExtendedH pdh = (PumpDataExtendedH) it.next();
                  
                dt.put(String.format(id, 
                    pdh.getDt_info(),
                    pdh.getType()) , pdh); */
                
                dt.put(pvex.getSpecialId(), pvex);

                pdr.writeStatus(counter);
            }
            
            
            sql = "SELECT dv from ggc.core.db.hibernate.pump.PumpProfileH as dv " +
            	  "WHERE dv.active_from >= " + dt_from + " and dv.person_id=" + m_da.getCurrentUserId() + 
            	  " ORDER BY dv.active_from "; 
            
            q = this.db.getSession().createQuery(sql);

            it = q.list().iterator();

            pdr.writeStatus(-1);
            //id = "PP_%s";
            
            while (it.hasNext())
            {
                counter++;

                PumpValuesEntryProfile pvep = new PumpValuesEntryProfile((PumpProfileH) it.next());
                
                /*
                PumpProfileH pdh = (PumpProfileH) it.next();
                  
                dt.put(String.format(id, 
                    pdh.getActive_from()) , pdh); */

                dt.put(pvep.getSpecialId(), pvep);
                
                pdr.writeStatus(counter);
            }
            
            pdr.finishReading();   
            //pdr.writeStatus(counter);
            
        }
        catch(Exception ex)
        {
            System.out.println("Exception on getPumpValues: " + ex);
            log.error("Exception on getPumpValues.\nsql:"+ sql + "\nEx: " + ex, ex);
        }
        
        return dt;
    }


    public GraphValuesCollection getGraphData(GregorianCalendar gc_from, GregorianCalendar gc_to, int filter)
    {
        GraphValuesCollection gvc = new GraphValuesCollection(gc_from, gc_to);
        
        DeviceValuesRange dvr = getRangePumpValues(gc_from, gc_to);
        ArrayList<DeviceValuesEntry> list = dvr.getAllEntries();
        
        for(int i=0; i<list.size(); i++)
        {
            DeviceValuesEntry dve = list.get(i);
            
            if (dve instanceof GraphValuesCapable)
            {
                gvc.addCollection(((GraphValuesCapable) dve).getGraphValues());
            }
            
        }
        
        //dvr.
        
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
