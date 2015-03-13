package ggc.plugin.data;

import ggc.core.util.DataAccess;
import ggc.plugin.data.enums.DeviceConfigurationGroup;

/**
 * Created by andy on 29.12.14.
 */
public class DeviceValueConfigEntry implements DeviceValueConfigEntryInterface
{

    String key;
    String value;
    boolean isBg = false;
    DeviceConfigurationGroup group;
    int index = 2;


    public DeviceValueConfigEntry()
    {
    }

    public DeviceValueConfigEntry(DeviceConfigurationGroup group)
    {
        this.key = group.getTranslation();
        this.group = group;
        this.value = "";
        this.index = 1;
    }

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

    public DeviceValueConfigEntry(String key, String value, DeviceConfigurationGroup group)
    {
        this.key = key;
        this.value = value;
        this.group = group;
    }

    public Object getColumnValue(int index)
    {
        switch (index)
        {
            case 1:
                return this.getDataValue();

            case 2:
                return this.getIndex();

            default:
                return this.getDataKey();
        }
    }


    public void setOutputType(int type)
    {
    }


    public int compare(DeviceValueConfigEntryInterface d1, DeviceValueConfigEntryInterface d2)
    {
        //if (true)
        //    return 0;


        if (d2.getGroup()==d1.getGroup())
        {

            if (d1.getIndex()==d2.getIndex())
            {
                return (d1.getDataKey().compareTo(d2.getDataKey()));
            }
            else
            {
                return d1.getIndex() - d2.getIndex();
                //return 0;
            }
        }
        else
        {
            //return 0;
            return d1.getGroup().getCode() - d2.getGroup().getCode();
        }



        //return (d1.getDataKey().compareTo(d2.getDataKey()));
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

    public DeviceConfigurationGroup getGroup()
    {
        return group;
    }


    public void setGroup(DeviceConfigurationGroup group)
    {
        this.group = group;
    }

    public Integer getIndex()
    {
        return this.index;
    }

}
