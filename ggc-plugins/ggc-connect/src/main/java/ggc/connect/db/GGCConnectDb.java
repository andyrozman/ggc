package ggc.connect.db;

import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.HibernateDb;
import com.atech.graphics.graphs.v2.data.GraphDefinitionDto;
import com.atech.graphics.graphs.v2.data.GraphTimeDataCollection;

import ggc.connect.util.DataAccessConnect;
import ggc.plugin.db.PluginDb;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Connect Tool (support for import/export of data to 3rd party systems)
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
 *  Filename:  GGCConnectDb
 *  Description: Db Instance
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class GGCConnectDb extends PluginDb // implements GraphDbDataRetriever
{

    private static final Logger LOG = LoggerFactory.getLogger(GGCConnectDb.class);
    DataAccessConnect m_da = DataAccessConnect.getInstance();


    /**
     * Constructor
     *
     * @param db
     */
    public GGCConnectDb(HibernateDb db)
    {
        super(db);
        LOG.debug("Created GGCConnectDb");
        // getAllElementsCount();
    }


    public GraphTimeDataCollection getGraphTimeData(GregorianCalendar gcFrom, GregorianCalendar gcTill,
            GraphDefinitionDto definitionDto)
    {
        return null;
    }

    // /**
    // * Get Daily Pump Values
    // *
    // * @param gc
    // * @return
    // */
    // public DeviceValuesDay getDailyCGMSValues(GregorianCalendar gc)
    // {
    // LOG.info("getDailyCGMSValues()");
    //
    // long dt = ATechDate.getATDateTimeFromGC(gc, ATechDateType.DateOnly);
    // // long dt = 20070323;
    //
    // DeviceValuesDay dV = new DeviceValuesDay(dataAccess);
    // dV.setDateTimeGC(gc);
    //
    // String sql = "";
    //
    // try
    // {
    // sql = "SELECT dv from ggc.core.db.hibernate.cgms.CGMSDataH as dv WHERE
    // dv.dt_info = " + dt
    // + " ORDER BY dv.dt_info ";
    // // + "000000 AND dv.dt_info <= " + dt +
    // // "235959 ORDER BY dv.dt_info";
    //
    // Query q = this.db.getSession().createQuery(sql);
    //
    // Iterator<?> it = q.list().iterator();
    //
    // while (it.hasNext())
    // {
    // CGMSDataH pdh = (CGMSDataH) it.next();
    // CGMSValuesEntry dv = new CGMSValuesEntry(pdh);
    //
    // dV.addList(dv.getSubEntryList());
    // }
    //
    // }
    // catch (Exception ex)
    // {
    // LOG.debug("Sql: " + sql);
    // LOG.error("getCGMSStats(). Exception: " + ex, ex);
    // }
    //
    // return dV;
    //
    // // return null;
    // }

    // /**
    // * Get All Elements Count
    // *
    // * @return
    // */
    // public int getAllElementsCount()
    // {
    // Integer in = null;
    // int sum_all = 0;
    //
    // Criteria criteria = this.getSession().createCriteria(CGMSDataH.class);
    // criteria.add(Restrictions.eq("person_id", (int)
    // dataAccess.getCurrentUserId()));
    // criteria.setProjection(Projections.rowCount());
    // in = (Integer) criteria.list().get(0);
    // sum_all = in.intValue();
    //
    // return sum_all;
    // }
    //

    // public GraphTimeDataCollection getGraphTimeData(GregorianCalendar gcFrom,
    // GregorianCalendar gcTill,
    // GraphDefinitionDto definitionDto)
    // {
    // // TODO types, for now we get only glucose data
    //
    // GraphTimeDataCollection collection = new GraphTimeDataCollection();
    //
    // List<ATechDate> dates = dataAccess.getDatesList(gcFrom, gcTill, true);
    //
    // Criteria criteria = null;
    //
    // try
    // {
    // criteria = getSession().createCriteria(CGMSDataH.class)
    // .add(Restrictions.eq("person_id", (int) dataAccess.getCurrentUserId()))
    // .add(Restrictions.eq("base_type", 1))
    // .add(Restrictions.and(Restrictions.ge("dt_info",
    // dates.get(0).getATDateTimeAsLong()),
    // Restrictions.le("dt_info", dates.get(1).getATDateTimeAsLong())));
    //
    // for (Object cgg : criteria.list())
    // {
    // CGMSDataH pdh = (CGMSDataH) cgg;
    // CGMSValuesEntry dv = new CGMSValuesEntry(pdh);
    //
    // collection.put(dv.getDateTimeObject().getATDateTimeAsLong(),
    // dv.getGraphSubEntryList());
    // }
    //
    // }
    // catch (Exception ex)
    // {
    // LOG.debug("Criteria: " + criteria);
    // LOG.error("getGraphTimeData(). Exception: " + ex, ex);
    // }
    //
    // return collection;
    // }
}
