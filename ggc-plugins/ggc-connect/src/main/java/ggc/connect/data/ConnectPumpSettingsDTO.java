package ggc.connect.data;

import java.util.ArrayList;
import java.util.List;

import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.defs.PumpConfigurationGroup;
import ggc.pump.data.defs.PumpSettingsType;
import ggc.pump.data.dto.PumpSettingsDTO;

public class ConnectPumpSettingsDTO extends PumpSettingsDTO
{

    String sourceItem;
    String sourceSubItem;
    String source;

    List<ConnectConfigEntry> configEntryList;


    public ConnectPumpSettingsDTO(DataAccessPlugInBase dataAccess)
    {
        super(dataAccess);

        configEntryList = new ArrayList<ConnectConfigEntry>();
    }


    public void setSourceItem(String value)
    {
        this.sourceItem = value;
    }


    public void setSourceSubItem(String value)
    {
        this.sourceSubItem = value;

    }


    protected void writeSetting(String key, String value, PumpConfigurationGroup group)
    {
        if (value != null)
        {
            writer.writeConfigurationData(
                new ConnectConfigEntry(i18nControl.getMessage(key), value, group, this.sourceItem, this.sourceSubItem));
        }
    }


    // public String getSource()
    // {
    // if (source == null)
    // {
    // if (sourceItem == null && sourceSubItem == null)
    // this.source = "";
    // else if (sourceItem != null && sourceSubItem != null)
    // this.source = sourceItem + " - " + sourceSubItem;
    // else
    // this.source = sourceItem;
    // }
    //
    // return source;
    // }

    public void writeAllSettingsToGGC(OutputWriter outputWriter)
    {
        this.writer = outputWriter;

        for (PumpSettingsType pumpSettingsType : PumpSettingsType.values())
        {
            writeSettingsToGGC(pumpSettingsType);
        }

        writeUngroupedSettings();

    }


    private void writeUngroupedSettings()
    {
        for (ConnectConfigEntry connectConfigEntry : configEntryList)
        {
            connectConfigEntry.setSource(this.sourceItem, this.sourceSubItem);

            writer.writeConfigurationData(connectConfigEntry);
        }
    }


    public void addConfigEntry(ConnectConfigEntry configEntry)
    {
        this.configEntryList.add(configEntry);
    }
}
