package ggc.connect.data;

import ggc.plugin.data.DeviceValueConfigEntry;
import ggc.plugin.data.DeviceValueConfigEntryInterface;
import ggc.plugin.data.enums.DeviceConfigurationGroup;
import ggc.pump.data.defs.PumpConfigurationGroup;

public class ConnectConfigEntry extends DeviceValueConfigEntry
{

    private String source;
    private String groupName;

    private String sourceSubItem;


    public ConnectConfigEntry()
    {
        super();
    }


    public ConnectConfigEntry(DeviceConfigurationGroup group, String source, String sourceSubItem)
    {
        this.key = group.getTranslation();
        this.group = group;
        this.value = "";
        this.index = 1;
        this.source = source;
        this.sourceSubItem = sourceSubItem;
    }


    public ConnectConfigEntry(String key, String value)
    {
        super(key, value, false);
    }


    public ConnectConfigEntry(String key, String value, DeviceConfigurationGroup group, String sourceItem,
            String sourceSubItem)
    {
        this.key = key;
        this.value = value;
        this.group = group;
        // this.index = 2;
        this.source = sourceItem;
        this.sourceSubItem = sourceSubItem;
    }


    @Override
    public Object getColumnValue(int column)
    {
        switch (column)
        {
            case 1:
                return this.getDataValue();

            case 2:
                if (this.index == 1)
                    return this.getSourceSubItem();
                else
                    return this.getSource();

            case 3:
                return this.getIndex();

            default:
                return this.getDataKey();
        }
    }


    public String getFullSource()
    {
        return this.source + " - " + this.sourceSubItem;
    }


    public int compare(DeviceValueConfigEntryInterface d1, DeviceValueConfigEntryInterface d2)
    {
        if ((d1 instanceof ConnectConfigEntry) && (d2 instanceof ConnectConfigEntry))
        {
            return compare((ConnectConfigEntry) d1, (ConnectConfigEntry) d2);
        }
        else
            return -1;
    }


    public int compare(ConnectConfigEntry d1, ConnectConfigEntry d2)
    {
        // if (true)
        // return 0;

        if (d2.getFullSource().equals(d1.getFullSource()))
        {
            if (d2.getGroup() == d1.getGroup())
            {

                if (d1.getIndex() == d2.getIndex())
                {
                    return (d1.getDataKey().compareTo(d2.getDataKey()));
                }
                else
                {
                    return d1.getIndex() - d2.getIndex();
                    // return 0;
                }
            }
            else
            {
                // return 0;
                return d1.getGroup().getCode() - d2.getGroup().getCode();
            }
        }
        else
        {
            return d2.getSource().compareTo(d1.getSource());
        }

        // return (d1.getDataKey().compareTo(d2.getDataKey()));
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


    public String getMultiLineToolTip(int column)
    {
        return null;
    }


    public DeviceConfigurationGroup getGroup()
    {
        return group;
    }


    public void setGroup(DeviceConfigurationGroup group)
    {
        this.group = group;
    }


    public int getColumnCount()
    {
        return 3;
    }


    public String getColumnName(int column)
    {
        if (column == 0)
            return "SETTING_GROUP";
        else if (column == 1)
            return "VALUE";
        else
            return "SOURCE";
    }


    public String getSource()
    {
        return source;
    }


    public void setSource(String source, String sourceSubItem)
    {
        this.source = source;
        this.sourceSubItem = sourceSubItem;
    }


    public void setGroupName(String groupName)
    {
        this.groupName = groupName;

        if (this.group == PumpConfigurationGroup.UnknownGroup)
        {
            this.key = groupName;
        }
    }


    public String getGroupName()
    {
        return groupName;
    }


    public String getSourceSubItem()
    {
        return sourceSubItem;
    }
}
