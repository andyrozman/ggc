package ggc.cgms.data;

import java.util.*;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.graphs.v2.data.GraphTimeDataDto;
import com.atech.misc.statistics.StatisticsItem;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.cgms.data.defs.CGMSBaseDataType;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.db.hibernate.cgms.CGMSDataH;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.output.OutputWriterType;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     CGMSValuesEntry
 *  Description:  Collection of CGMSValuesEntry, which contains all daily values.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSValuesEntry extends DeviceValuesEntry implements StatisticsItem
{

    DataAccessCGMS da = DataAccessCGMS.getInstance();
    private static final Logger LOG = LoggerFactory.getLogger(CGMSValuesEntry.class);

    long id;

    long datetime;
    long date;
    ATechDate date_obj = null;

    int type;

    boolean empty = true;

    private Hashtable<String, String> params;
    ArrayList<CGMSValuesSubEntry> list = null;
    String extended = "";
    int person_id = 0;
    String item_data = null;


    /**
     * Constructor
     */
    public CGMSValuesEntry()
    {
        super();
        list = new ArrayList<CGMSValuesSubEntry>();
        // this.person_id = (int)this.da.getCurrentUserId();
    }


    /**
     * Constructor
     * 
     * @param pdh 
     */
    public CGMSValuesEntry(CGMSDataH pdh)
    {
        super();
        list = new ArrayList<CGMSValuesSubEntry>();
        this.id = pdh.getId();
        this.datetime = pdh.getDtInfo();
        this.type = pdh.getBaseType();
        loadExtended(pdh.getExtended());
        this.person_id = pdh.getPersonId();
    }


    /**
     * Set Date 
     * 
     * @param dt
     */
    public void setDate(long dt)
    {
        this.date = dt;
        this.date_obj = new ATechDate(ATechDateType.DateOnly, dt);
        this.datetime = dt;
    }


    /**
     * Get DateTime (long)
     * 
     * @return
     */
    @Override
    public long getDateTime()
    {
        return this.datetime;
    }


    /**
     * Get DateTime Object (ATechDate)
     * 
     * @return ATechDate instance
     */
    @Override
    public ATechDate getDateTimeObject()
    {
        return new ATechDate(ATechDateType.DateAndTimeSec, this.datetime);
    }


    /**
     * Add Sub Entry
     * 
     * @param subentry
     */
    public void addSubEntry(CGMSValuesSubEntry subentry)
    {
        this.list.add(subentry);
    }


    public void addSubEntries(List<CGMSValuesSubEntry> entries)
    {
        this.list.addAll(entries);
    }


    /**
     * Get Sub Entry List
     * 
     * @return
     */
    public List<CGMSValuesSubEntry> getSubEntryList()
    {
        return this.list;
    }


    public List<GraphTimeDataDto> getGraphSubEntryList()
    {
        List<GraphTimeDataDto> graphList = new ArrayList<GraphTimeDataDto>();

        for (CGMSValuesSubEntry entry : this.list)
        {
            graphList.add(new GraphTimeDataDto(this.datetime, entry.time, Double.parseDouble(entry.value)));
        }

        return graphList;
    }


    /**
     * Set Empty
     * 
     * @param empty_
     */
    public void setEmpty(boolean empty_)
    {
        this.empty = empty_;
    }


    /**
     * Is Empty
     * 
     * @return
     */
    public boolean isEmpty()
    {
        return this.empty;
    }


    /**
     * Add Parameter
     * 
     * @param key
     * @param value_in
     */
    public void addParameter(String key, String value_in)
    {
        if (value_in.equals("_") || value_in.trim().length() == 0)
            return;

        if (params == null)
        {
            params = new Hashtable<String, String>();
        }

        this.params.put(key, value_in);

    }


    /**
     * @return
     */
    public String getParametersAsString()
    {
        if (this.params == null)
            return "";

        StringBuffer sb = new StringBuffer();

        for (java.util.Enumeration<String> en = this.params.keys(); en.hasMoreElements();)
        {
            String key = en.nextElement();

            sb.append(key + "=" + this.params.get(key) + ";");
        }

        return sb.substring(0, sb.length() - 1);

    }


    /**
     * Prepare Entry [Framework v2]
     */
    @Override
    public void prepareEntry_v2()
    {
        this.saveExtended();
    }


    /**
     * Set Type 
     * 
     * @param type_
     */
    public void setType(int type_)
    {
        this.type = type_;
    }


    /**
     * Get Type
     * 
     * @return
     */
    public int getType()
    {
        return this.type;
    }


    /**
     * Create Comment
     * 
     * @return
     */
    public String createComment()
    {
        String p = this.getParametersAsString();

        if (p == null || p.trim().length() == 0)
            return null;
        // return "MTI";
        else
            return p;

    }


    /**
     * To String
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "CGMSValuesEntry [date/time=" + this.datetime + ",readings=" + this.list.size() + //
                ",type=" + type + ",checked=" + this.checked + "]";
    }


    /**
     * Get Column Value
     * 
     * @param index
     * @return
     */
    @Override
    public Object getColumnValue(int index)
    {
        switch (index)
        {
            /*
             * case 0: // time
             * {
             * return this.datetime.getTimeString();
             * }
             * case 1: // type
             * {
             * return getBaseTypeString();
             * }
             * case 2: // subtype
             * {
             * return getSubTypeString();
             * }
             * case 3: // value
             * {
             * //return this.getCode();
             * return getValuePrint();
             * }
             * case 4: // additional
             * {
             * return this.getAdditionalDisplay();
             * }
             * case 5: // food
             * {
             * return this.isFoodSet();
             * }
             */
        }
        return "N/A";
    }


    /**
     * Get DateTime format
     * 
     * @return format of date time (precission)
     */
    @Override
    public ATechDateType getDateTimeFormat()
    {
        return ATechDateType.DateAndTimeSec;
    }


    /**
     * Get Data As String
     * 
     * @see ggc.plugin.output.OutputWriterData#getDataAsString()
     */

    public String getDataAsString()
    {
        switch (output_type)
        {
            case OutputWriterType.DUMMY:
                return "";

            case OutputWriterType.CONSOLE:
            case OutputWriterType.FILE:
                return this.getDateTimeObject().getDateTimeString() + ":  Type=" + this.type + ", Count="
                        + this.list.size();

            case OutputWriterType.GGC_FILE_EXPORT:
                {
                    /*
                     * PumpData pd = new PumpData(this);
                     * try
                     * {
                     * return pd.dbExport();
                     * }
                     * catch(Exception ex)
                     * {
                     * LOG.error(
                     * "Problem with PumpValuesEntry export !  Exception: " +
                     * ex, ex);
                     * return "Value could not be decoded for export!";
                     * }
                     */
                }

            default:
                return "Value is undefined";

        }
    }


    /**
     * Set DateTime Object (ATechDate)
     * 
     * @param dt ATechDate instance
     */
    @Override
    public void setDateTimeObject(ATechDate dt)
    {
        this.datetime = dt.getATDateTimeAsLong();
    }


    /**
     * Get Value For Item
     * 
     * @param index index for statistics item 
     * @return 
     */
    public float getValueForItem(int index)
    {
        return 0;
    }


    /**
     * Get Statistics Action - we define how statistic is done (we have several predefined 
     *    types of statistics
     * 
     * @param index index for statistics item 
     * @return
     */
    public int getStatisticsAction(int index)
    {
        return 0;
    }


    /**
     * Is Special Action - tells if selected statistics item has special actions
     * 
     * @param index
     * @return
     */
    public boolean isSpecialAction(int index)
    {
        return false;
    }


    /**
     * Get Max Statistics Object - we can have several Statistic types defined here
     * 
     * @return
     */
    public int getMaxStatisticsObject()
    {
        return 0;
    }


    /**
     * If we have any special actions for any of objects
     * 
     * @return
     */
    public boolean weHaveSpecialActions()
    {
        return false;
    }


    /**
     * Get Table Column Value (in case that we need special display values for download data table, this method 
     * can be used, if it's the same as getColumnValue, we can just call that one. 
     * 
     * @param index
     * @return
     */
    public Object getTableColumnValue(int index)
    {
        switch (index)
        {
            case 0:
                return this.date_obj.getDateString();

            case 1:
                return CGMSBaseDataType.getByCode(this.type).getTranslation();

            case 2:
                return this.list.size();

            case 3:
                return this.getStatusType();

            case 4:
                return getChecked();

            default:
                return "";
        }

    }


    /**
     * Get Special Id
     * 
     * @return
     */
    public String getSpecialId()
    {
        return "CD_" + this.datetime + "_" + this.type;
    }


    /**
     * getObjectUniqueId - get id of object
     * @return unique object id
     */
    public String getObjectUniqueId()
    {
        return "" + this.datetime;
    }


    /**
     * DbAdd - Add this object to database
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return id in type of String
     */
    public String DbAdd(Session sess) throws Exception
    {
        this.person_id = (int) this.da.getCurrentUserId();

        Transaction tx = sess.beginTransaction();

        CGMSDataH pdh = new CGMSDataH();

        pdh.setId(this.id);
        pdh.setDtInfo(this.datetime);
        pdh.setBaseType(this.type);
        pdh.setExtended(extended = this.saveExtended());
        pdh.setPersonId(this.person_id);
        pdh.setChanged(System.currentTimeMillis());

        Long _id = (Long) sess.save(pdh);
        tx.commit();

        pdh.setId(_id);
        this.id = _id;

        return "" + _id;
    }


    /**
     * DbEdit - Edit this object in database
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbEdit(Session sess) throws Exception
    {
        this.person_id = (int) this.da.getCurrentUserId();

        Transaction tx = sess.beginTransaction();

        System.out.println("old_id: " + old_id);
        System.out.println("id: " + this.id);

        CGMSDataH pdh = (CGMSDataH) sess.get(CGMSDataH.class, this.id);

        pdh.setId(this.id);
        pdh.setBaseType(this.type);
        pdh.setExtended(this.extended = saveExtended());
        pdh.setPersonId(this.person_id);
        pdh.setChanged(System.currentTimeMillis());

        sess.update(pdh);
        tx.commit();

        return true;
    }


    /**
     * DbDelete - Delete this object in database
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbDelete(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();

        CGMSDataH pdh = (CGMSDataH) sess.get(CGMSDataH.class, this.id);
        sess.delete(pdh);
        tx.commit();

        return true;
    }


    /**
     * DbHasChildren - Shows if this entry has any children object, this is needed for delete
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbHasChildren(Session sess) throws Exception
    {
        return false;
    }


    /**
     * DbGet - Loads this object. Id must be set.
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbGet(Session sess) throws Exception
    {

        CGMSDataH pdh = (CGMSDataH) sess.get(CGMSDataH.class, this.id);

        this.id = pdh.getId();
        this.datetime = pdh.getDtInfo();
        this.type = pdh.getBaseType();
        loadExtended(pdh.getExtended());
        this.person_id = pdh.getPersonId();

        return true;
    }


    /**
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName()
    {
        return "CGMSValuesEntry";
    }


    /**
     * isDebugMode - returns debug mode of object
     * 
     * @return true if object in debug mode
     */
    public boolean isDebugMode()
    {
        return false;
    }


    /**
     * getAction - returns action that should be done on object
     *    0 = no action
     *    1 = add action
     *    2 = edit action
     *    3 = delete action
     *    This is used mainly for objects, contained in lists and dialogs, used for 
     *    processing by higher classes (classes calling selectors, wizards, etc...
     * 
     * @return number of action
     */
    public int getAction()
    {
        return 0;
    }


    /**
     * Get DeviceValuesEntry Name
     * 
     * @return
     */
    public String getDVEName()
    {
        return "CGMSValuesEntry";
    }


    /**
     * Get Value of object
     * 
     * @return
     */
    public String getValue()
    {
        return this.item_data;
    }

    long old_id;


    /**
     * Set Old Id (this is used for changing old objects in framework v2)
     * 
     * @param id_in
     */
    public void setId(long id_in)
    {
        this.id = id_in;
    }


    /**
     * Get Old Id (this is used for changing old objects in framework v2)
     * 
     * @return id of old object
     */
    public long getId()
    {
        return this.id;
    }


    /**
     * Set Old Id (this is used for changing old objects in framework v2)
     * THIS IS JUST COPY, IT MIGHT NOT BE USED, OR PERHAPS NOW FUNCTIONALITY WILL BE BROKEN.
     * NEED TO TEST. URGENTLY
     * @param id_in
     */
    public void setOldId(long id_in)
    {
        this.old_id = id_in;
    }


    /**
     * Get Old Id (this is used for changing old objects in framework v2)
     * 
     * @return id of old object
     */
    public long getOldId()
    {
        return this.old_id;
    }

    String source;


    /**
     * Set Source
     * 
     * @param src
     */
    public void setSource(String src)
    {
        this.source = src;

    }


    /**
     * Get Source 
     * 
     * @return
     */
    public String getSource()
    {
        return this.source;
    }


    private String saveExtended()
    {
        Collections.sort(this.list);

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < this.list.size(); i++)
        {
            sb.append(this.list.get(i).getSubEntryValue());
            sb.append(";");
        }

        sb.substring(0, sb.length() - 1);

        this.item_data = sb.toString();

        StringBuffer sb1 = new StringBuffer("DATA=");
        sb1.append(this.item_data);

        sb1.append("#$#SOURCE=");
        sb1.append(this.list.get(0).getSource());

        return sb1.toString();
    }


    private void loadExtended(String entry)
    {
        this.extended = entry;

        StringTokenizer strtok = new StringTokenizer(entry, "#$#");

        while (strtok.hasMoreTokens())
        {
            String tok = strtok.nextToken();

            if (tok.startsWith("DATA="))
            {
                tok = tok.substring(5);

                this.item_data = tok;

                StringTokenizer strtok2 = new StringTokenizer(tok, ";");

                while (strtok2.hasMoreTokens())
                {
                    CGMSValuesSubEntry cvse = new CGMSValuesSubEntry(strtok2.nextToken(), this.type);
                    this.list.add(cvse);
                }

                // System.out.println("tok data: " + tok);
            }
            else if (tok.startsWith("SOURCE="))
            {
                tok = tok.substring(7);
                // System.out.println("tok src: " + tok);
            }
            else
            {
                LOG.warn("Unknown token with extended data: " + tok);
            }

        }

    }


    public void sortSubs()
    {
        Collections.sort(this.list);
    }


    public boolean equals(Object element)
    {
        if (element instanceof CGMSValuesEntry)
        {
            CGMSValuesEntry other = (CGMSValuesEntry) element;

            return ((this.datetime == other.datetime) && (this.type == other.type));
        }
        else
            return false;
    }
}
