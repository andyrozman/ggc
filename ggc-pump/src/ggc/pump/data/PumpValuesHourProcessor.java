package ggc.pump.data;

import ggc.plugin.data.DeviceValuesEntry;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.util.DataAccessPump;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PumpValuesHourProcessor
{

    DataAccessPump dataAccessPump = DataAccessPump.getInstance();
    HashMap<PumpDeviceValueType, List<String>> comments = null;

    public void clearComments()
    {
        if (comments == null)
        {
            comments = new HashMap<PumpDeviceValueType, List<String>>();
            comments.put(PumpDeviceValueType.BG, new ArrayList<String>());
            comments.put(PumpDeviceValueType.BOLUS, new ArrayList<String>());
            comments.put(PumpDeviceValueType.COMMENT, new ArrayList<String>());
        }
        else
        {
            comments.get(PumpDeviceValueType.BG).clear();
            comments.get(PumpDeviceValueType.BOLUS).clear();
            comments.get(PumpDeviceValueType.COMMENT).clear();
        }

    }

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
        PumpValuesHour pumpValuesHour = new PumpValuesHour(this.dataAccessPump);

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
                pumpValuesHour.addBolus(pve);
            }
        }

        return pumpValuesHour;
    }

    public void addComments(PumpDeviceValueType valueType, String partComment)
    {
        comments.get(valueType).add(partComment);
    }

    public String getFullComment()
    {
        StringBuffer fullComment = new StringBuffer();

        List<String> listComms = comments.get(PumpDeviceValueType.BG);

        if (!listComms.isEmpty())
        {
            StringBuffer sb = new StringBuffer();
            sb.append(dataAccessPump.getI18nControlInstance().getMessage("BG"));
            sb.append(": ");

            for (String entry : listComms)
            {
                sb.append(entry);
                sb.append(",");
            }

            fullComment.append(sb.toString().substring(0, sb.length() - 1));
            fullComment.append("; ");
        }

        return fullComment.toString();
    }

}
