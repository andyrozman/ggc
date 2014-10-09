package ggc.pump.data;

import ggc.plugin.data.DeviceValuesEntry;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpBaseType;
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

        String keyBG = dataAccessPump.getAdditionalTypes().getTypeDescription(PumpAdditionalDataType.PUMP_ADD_DATA_BG);
        String keyCH = dataAccessPump.getAdditionalTypes().getTypeDescription(PumpAdditionalDataType.PUMP_ADD_DATA_CH);

        // String keyBG =
        // dataAccessPump.getI18nControlInstance().getMessage(PumpDeviceValueType.BG.getAdditionalKey());

        // System.out.println("KeyBG: " + keyBG + ", " + keyBG1);

        for (DeviceValuesEntry entry : deviceValues)
        {
            PumpValuesEntry pve = (PumpValuesEntry) entry;

            // System.out.println("Time: ")

            // System.out.println("PVE Additional Data: " +
            // pve.getAdditionalData());

            if (pve.getAdditionalData().containsKey(keyBG))
            {
                pumpValuesHour.addBGEntry(pve.getAdditionalData().get(keyBG));
            }

            if (pve.getAdditionalData().containsKey(keyCH))
            {
                pumpValuesHour.addCHEntry(pve.getAdditionalData().get(keyCH).getValue());
            }

            if ((pve.getBaseType() == PumpBaseType.PUMP_DATA_BOLUS)
                    || (pve.getBaseType() == PumpBaseType.PUMP_DATA_PEN_INJECTION_BOLUS))
            {
                pumpValuesHour.addBolus(pve.getValue());
                System.out.println("Bolus");
            }

            // if keyCH

        }

        return pumpValuesHour;
    }
}
