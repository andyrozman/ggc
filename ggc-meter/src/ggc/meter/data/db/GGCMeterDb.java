package ggc.meter.data.db;

import ggc.core.db.hibernate.DayValueH;
import ggc.meter.data.MeterDataReader;
import ggc.meter.data.MeterValuesEntry;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.db.PluginDb;

import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;

import com.atech.db.hibernate.HibernateDb;

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


public class GGCMeterDb extends PluginDb
{
    private static Log log = LogFactory.getLog(GGCMeterDb.class);
    DataAccessMeter m_da = DataAccessMeter.getInstance();

    /**
     * Constructor
     * 
     * @param db
     */
    public GGCMeterDb(HibernateDb db)
    {
        super(db);
        
        //getAllElementsCount();
    }
    
    
    
    /**
     * Get All Elements Count
     * 
     * @return
     */
    public int getAllElementsCount()
    {
        try
        {
            Integer in = null;
            int sum_all = 0;
            
            Criteria criteria = this.getSession().createCriteria(DayValueH.class);
            criteria.add(Expression.eq("person_id", (int)m_da.getCurrentUserId()));
            criteria.add(Expression.gt("bg", 0));
            //criteria.createCriteria("person_id", (int)m_da.getCurrentUserId());
            criteria.setProjection(Projections.rowCount());
            in = (Integer) criteria.list().get(0);
            sum_all = in.intValue();
            
            log.debug("Old Meter Data in Db: " + in.intValue());
            
            return sum_all;
        }
        catch(Exception ex)
        {
            log.error("getAllElementsCount: " + ex, ex);
            ex.printStackTrace();
            return 0;
        }
    }
    
    
    /**
     * Get Meter Values
     * 
     * @param mdr MeterDataReader instance for writing progress on reading this data
     * 
     * @return
     */
    public Hashtable<String,DeviceValuesEntryInterface> getMeterValues(MeterDataReader mdr)
    {
        //Hashtable<String,DayValueH>
        Hashtable<String,DeviceValuesEntryInterface> ht = new Hashtable<String,DeviceValuesEntryInterface>(); 
        
        log.info("getMeterValues()");

        //mdr.se
        
        mdr.writeStatus(-1);
        
        try
        {

            log.debug("getMeterValues() - Process");

            Query q = this.getSession().createQuery(
                "SELECT dv from ggc.core.db.hibernate.DayValueH as dv " + 
                "WHERE (dv.bg>0) and person_id=" + m_da.getCurrentUserId() + 
                " ORDER BY dv.dt_info");

            //System.out.println("Found elements: " + q.list().size());
            
            Iterator<?> it = q.list().iterator();

            int counter = 0;
            
            mdr.writeStatus(counter);
            
            while (it.hasNext())
            {
                counter++;
                //DayValueH gv = (DayValueH)it.next();
                
                MeterValuesEntry mve = new MeterValuesEntry((DayValueH)it.next());
                
                ht.put("" + mve.getSpecialId(), mve);
                mdr.writeStatus(counter);
            }

        }
        catch (Exception ex)
        {
            log.error("getMeterValues.Exception: " + ex, ex);
            ex.printStackTrace();
        }

        return ht;
    }

    
}
