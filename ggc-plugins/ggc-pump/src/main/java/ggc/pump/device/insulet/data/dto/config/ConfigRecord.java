package main.java.ggc.pump.device.insulet.data.dto.config;

import main.java.ggc.pump.data.defs.PumpConfigurationGroup;
import main.java.ggc.pump.device.insulet.data.dto.AbstractRecord;
import main.java.ggc.pump.device.insulet.util.InsuletUtil;

/**
 * Created by andy on 24.05.15.
 */
public abstract class ConfigRecord extends AbstractRecord
{

    public ConfigRecord(boolean isImplemented)
    {
        super(isImplemented);
    }


    public abstract void writeConfigData();


    protected void writeSetting(String key, String value, Object rawValue, PumpConfigurationGroup group)
    {
        if (rawValue != null)
        {
            InsuletUtil.writeConfiguration(key, value, group);
        }
    }

}
