package ggc.cgms.data;

import ggc.cgms.data.defs.extended.CGMSExtendedDataType;
import ggc.cgms.util.CGMSUtil;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.core.db.hibernate.cgms.CGMSDataExtendedH;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.output.OutputWriterType;

import java.util.ArrayList;
import java.util.Hashtable;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.db.ext.ExtendedHandler;
import com.atech.misc.statistics.StatisticsItem;
import com.atech.utils.data.ATechDate;

public class CGMSValuesExtendedEntry extends DeviceValuesEntry implements StatisticsItem
{

    public long id;
    public long datetime;
    // public int type;
    private CGMSExtendedDataType type;
    public String value;
    public String extended;
    public int personId;
    public String comment;
    public int subType = -1; // not stored directly

    // DataAccessCGMS dataAccess = DataAccessCGMS.getInstance();

    public CGMSValuesExtendedEntry()
    {
        super();
        this.source = DataAccessCGMS.getInstance().getSourceDevice();
    }

    /**
     * Constructor
     * 
     * @param pdh 
     */
    public CGMSValuesExtendedEntry(CGMSDataExtendedH pdh)
    {
        super();

        loadDbData(pdh);
    }

    private void loadDbData(CGMSDataExtendedH pdh)
    {
        this.id = pdh.getId();

        this.datetime = pdh.getDt_info();
        this.type = CGMSExtendedDataType.getEnum(pdh.getType());
        this.value = pdh.getValue();
        loadExtended(pdh.getExtended());
        this.personId = pdh.getPerson_id();
        this.comment = pdh.getComment();
    }

    public Object getTableColumnValue(int index)
    {
        switch (index)
        {
            case 0:
                return this.getDateTimeObject().getDateTimeString();

            case 1:
                return CGMSUtil.getTranslatedString(this.type.getDescription());

            case 2:
                return getDisplayValue();

            case 3:
                return new Boolean(getChecked());

            case 4:
                return this.getStatus();

            default:
                return "";
        }
    }

    private Object getDisplayValue()
    {
        // FIXME
        switch (type)
        {
            case Insulin:
            case Carbs:
                return this.value;

            case Exercise:
                // FIXME
                return "N/I: " + this.subType;

            case Health:
                // FIXME
                return "N/I: " + this.subType;

            case None:
            default:
                break;

        }

        // TODO Auto-generated method stub
        return "N/I";
    }

    /**
     * Prepare Entry [Framework v2]
     */
    @Override
    public void prepareEntry_v2()
    {
        this.saveExtended();
    }

    public String getDVEName()
    {
        return "CGMSValuesExtendedEntry";
    }

    public String getValue()
    {
        return this.value;
    }

    long old_id;

    /**
     * Set Old Id (this is used for changing old objects in framework v2)
     * 
     * @param id_in
     */
    public void setId(long id_in)
    {
        this.old_id = id_in;
    }

    /**
     * Get Old Id (this is used for changing old objects in framework v2)
     * 
     * @return id of old object
     */
    public long getId()
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

    public String getSpecialId()
    {
        return "CDE_" + this.datetime + "_" + this.type.getValue();
    }

    public String getDataAsString()
    {
        switch (output_type)
        {
            case OutputWriterType.DUMMY:
                return "";

            case OutputWriterType.CONSOLE:
            case OutputWriterType.FILE:
                return this.getDateTimeObject().getDateTimeString() + ":  Type=" + this.type.getDescription()
                        + ", Value=" + this.getValue();

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
                     * log.error(
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

    public String getObjectUniqueId()
    {
        return "" + this.datetime;
    }

    public String DbAdd(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();

        CGMSDataExtendedH ext = new CGMSDataExtendedH();

        this.saveDbData(ext);

        Long _id = (Long) sess.save(ext);
        tx.commit();

        ext.setId(_id.longValue());
        this.id = _id.longValue();

        return "" + _id.longValue();
    }

    private void saveDbData(CGMSDataExtendedH ch)
    {
        this.personId = CGMSUtil.getCurrentUserId();

        ch.setDt_info(this.datetime);
        ch.setType(this.type.getValue());
        ch.setValue(this.getValue());
        ch.setExtended(extended = this.saveExtended());
        ch.setPerson_id(this.personId);
        ch.setComment(this.comment);
        ch.setChanged(System.currentTimeMillis());
    }

    public boolean DbEdit(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();
        // System.out.println("id: " + old_id);
        CGMSDataExtendedH ext = (CGMSDataExtendedH) sess.get(CGMSDataExtendedH.class, this.id);

        //ext.setId(this.id);

        this.saveDbData(ext);

        sess.update(ext);
        tx.commit();

        return true;
    }

    public boolean DbDelete(Session sess) throws Exception
    {
        Transaction tx = sess.beginTransaction();

        CGMSDataExtendedH ch = (CGMSDataExtendedH) sess.get(CGMSDataExtendedH.class, this.getId());

        sess.delete(ch);
        tx.commit();

        return true;
    }

    public boolean DbHasChildren(Session sess) throws Exception
    {
        return false;
    }

    public boolean DbGet(Session sess) throws Exception
    {
        CGMSDataExtendedH ch = (CGMSDataExtendedH) sess.get(CGMSDataExtendedH.class, this.getId());

        loadDbData(ch);

        return true;
    }

    public String getObjectName()
    {
        return "CGMSValuesExtendedEntry";
    }

    public boolean isDebugMode()
    {
        return false;
    }

    public int getAction()
    {
        return 0;
    }

    public float getValueForItem(int index)
    {
        return 0;
    }

    public int getStatisticsAction(int index)
    {
        return 0;
    }

    public boolean isSpecialAction(int index)
    {
        return false;
    }

    public int getMaxStatisticsObject()
    {
        return 0;
    }

    public boolean weHaveSpecialActions()
    {
        return false;
    }

    @Override
    public ArrayList<? extends GGCHibernateObject> getDbObjects()
    {
        return null;
    }

    @Override
    public Object getColumnValue(int index)
    {
        return "N/A";
    }

    @Override
    public long getDateTime()
    {
        return this.datetime;
    }

    @Override
    public void setDateTimeObject(ATechDate dt)
    {
        this.datetime = dt.getATDateTimeAsLong();
    }

    @Override
    public ATechDate getDateTimeObject()
    {
        return new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, this.datetime);
    }

    @Override
    public int getDateTimeFormat()
    {
        return ATechDate.FORMAT_DATE_AND_TIME_S;
    }

    private void loadExtended(String extended2)
    {
        ExtendedHandler handler = CGMSUtil.getExtendedHandler(this.getDVEName());
        Hashtable<String, String> data = handler.loadExtended(extended2);

        if (handler.isExtendedValueSet(ExtendedCGMSValuesExtendedEntry.EXTENDED_SUB_TYPE, data))
        {
            this.subType = Integer.parseInt(handler.getExtendedValue(ExtendedCGMSValuesExtendedEntry.EXTENDED_SUB_TYPE,
                data));
        }

        if (handler.isExtendedValueSet(ExtendedCGMSValuesExtendedEntry.EXTENDED_SUB_TYPE, data))
        {
            this.source = handler.getExtendedValue(ExtendedCGMSValuesExtendedEntry.EXTENDED_SOURCE, data);
        }
    }

    private String saveExtended()
    {
        ExtendedHandler handler = CGMSUtil.getExtendedHandler(this.getDVEName());
        Hashtable<String, String> data = new Hashtable<String, String>();

        if (this.subType > 0)
        {
            handler.setExtendedValue(ExtendedCGMSValuesExtendedEntry.EXTENDED_SUB_TYPE, "" + this.subType, data);
        }

        if (StringUtils.isNotBlank(this.source))
        {
            handler.setExtendedValue(ExtendedCGMSValuesExtendedEntry.EXTENDED_SOURCE, this.source, data);
        }

        String ext = handler.saveExtended(data);

        return StringUtils.isNotBlank(ext) ? ext : null;

    }

    public CGMSExtendedDataType getType()
    {
        return type;
    }

    public void setType(CGMSExtendedDataType type)
    {
        this.type = type;
    }

}
