package ggc.pump.data;

import ggc.plugin.data.DeviceValuesEntry;
import ggc.pump.util.DataAccessPump;

import java.util.List;

public class PumpValuesHourProcessor
{

    DataAccessPump dataAccessPump = DataAccessPump.getInstance();

    public float getValueForType(List<DeviceValuesEntry> deviceValues, PumpDeviceValueType valueType)
    {
        if (valueType == PumpDeviceValueType.BG)
        {
            String key = dataAccessPump.getI18nControlInstance().getMessage(valueType.getAdditionalKey());
            // List<String> additionalData =

            for (DeviceValuesEntry entry : deviceValues)
            {
                PumpValuesEntry pve = (PumpValuesEntry) entry;

                if (pve.getAdditionalData().containsKey(key))
                {

                }
            }
        }

        return 0.0f;
    }

    public PumpValuesHour createPumpValuesHour(List<DeviceValuesEntry> deviceValues)
    {
        PumpValuesHour pumpValuesHour = new PumpValuesHour();

        String keyBG = dataAccessPump.getI18nControlInstance().getMessage(PumpDeviceValueType.BG.getAdditionalKey());

        for (DeviceValuesEntry entry : deviceValues)
        {
            PumpValuesEntry pve = (PumpValuesEntry) entry;

            if (pve.getAdditionalData().containsKey(keyBG))
            {
                pumpValuesHour.addBGEntry(pve.getAdditionalData().get(keyBG));
            }
        }

        return pumpValuesHour;
    }
}
