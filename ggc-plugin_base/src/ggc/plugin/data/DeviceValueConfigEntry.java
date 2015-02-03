package ggc.plugin.data;

import com.atech.utils.data.ATechDate;
import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.core.util.DataAccess;
import ggc.plugin.util.DataAccessPlugInBase;

import java.util.ArrayList;

/**
 * Created by andy on 29.12.14.
 */
public class DeviceValueConfigEntry implements DeviceValueConfigEntryInterface
{

    String key;
    String value;
    boolean isBg = false;

    public DeviceValueConfigEntry(String key, String value)
    {
        this(key, value, false);
    }


    public DeviceValueConfigEntry(String key, String value, boolean isBg)
    {
        this.key = key;
        this.value = value;
        this.isBg = isBg;
    }


    public Object getColumnValue(int index)
    {
        switch (index)
        {
            case 1:
                return this.getDataValue();

            default:
                return this.getDataKey();
        }
    }


    public void setOutputType(int type)
    {
    }


    public int compare(DeviceValueConfigEntryInterface d1, DeviceValueConfigEntryInterface d2)
    {
        return (d1.getDataKey().compareTo(d2.getDataKey()));
    }


    public int compareTo(DeviceValueConfigEntryInterface d2)
    {
        return compare(this, d2);
    }


    public boolean hasMultiLineToolTip()
    {
        return false;
    }


    public void setMultiLineTooltipType(int _multiline_tooltip_type)
    {
    }


    public int getMultiLineTooltipType()
    {
        return 0;
    }


    public boolean isIndexed()
    {
        return false;
    }


    public String getMultiLineToolTip()
    {
        return null;
    }


    public String getMultiLineToolTip(int index)
    {
        return null;
    }


    public String getDataKey()
    {
        return this.key;
    }


    public String getDataValue()
    {
        if (this.isBg)
        {
            return DataAccess.getInstance().getBGValueAsString(this.value);
        }
        else
        {
            return this.value;
        }
    }

    public String getDataValueRaw()
    {
        return this.value;
    }


    public boolean isDataValueBG()
    {
        return this.isBg;
    }


}
