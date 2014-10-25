package ggc.pump.data;

import ggc.plugin.data.DeviceValuesEntry;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.db.PumpProfile;
import ggc.pump.util.DataAccessPump;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class PumpValuesHourProcessor
{

    DataAccessPump dataAccessPump = DataAccessPump.getInstance();
    HashMap<PumpDeviceValueType, List<String>> additionalData = null;
    HashMap<String, String> index = new HashMap<String, String>();

    public void clearComments()
    {
        if (additionalData == null)
        {
            additionalData = new HashMap<PumpDeviceValueType, List<String>>();
            additionalData.put(PumpDeviceValueType.BG, new ArrayList<String>());
            additionalData.put(PumpDeviceValueType.PUMP_ADDITIONAL_DATA, new ArrayList<String>());
            additionalData.put(PumpDeviceValueType.COMMENT, new ArrayList<String>());
        }
        else
        {
            additionalData.get(PumpDeviceValueType.BG).clear();
            additionalData.get(PumpDeviceValueType.PUMP_ADDITIONAL_DATA).clear();
            additionalData.get(PumpDeviceValueType.COMMENT).clear();
        }

    }

    public PumpProfile getProfileForHour(List<PumpProfile> profiles, GregorianCalendar gregorianCalendar, int hour,
            String profileName)
    {

        if (profiles.size() == 1)
        {
            return profiles.get(0);
        }

        for (PumpProfile profile : profiles)
        {
            // FIXME doesn't totally work, need to check Andy
            if (profile.containsPatternForHourAndDate(gregorianCalendar, hour))
            {
                return profile;
            }
        }

        return null;
    }

    public PumpValuesHour createPumpValuesHour(List<DeviceValuesEntry> deviceValues)
    {
        PumpValuesHour pumpValuesHour = new PumpValuesHour(this.dataAccessPump);

        String keyBG = dataAccessPump.getAdditionalTypes().getTypeDescription(PumpAdditionalDataType.PUMP_ADD_DATA_BG);
        String keyCH = dataAccessPump.getAdditionalTypes().getTypeDescription(PumpAdditionalDataType.PUMP_ADD_DATA_CH);
        String keyComment = dataAccessPump.getAdditionalTypes().getTypeDescription(
            PumpAdditionalDataType.PUMP_ADD_DATA_COMMENT);

        // String keyBG =
        // dataAccessPump.getI18nControlInstance().getMessage(PumpDeviceValueType.BG.getAdditionalKey());

        // System.out.println("KeyBG: " + keyBG + ", " + keyBG1);

        for (DeviceValuesEntry entry : deviceValues)
        {
            PumpValuesEntry pve = (PumpValuesEntry) entry;

            // System.out.println(pve);

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

            if (pve.getAdditionalData().containsKey(keyComment))
            {
                String com = pve.getAdditionalData().get(keyComment).getValue();

                if (StringUtils.isNotBlank(com))
                {
                    if (!this.index.containsKey(pve.toString()))
                    {
                        this.addAdditionalData(PumpDeviceValueType.COMMENT, com);
                        this.index.put(pve.toString(), "");
                    }

                    // System.out.println(pve);
                    // System.out.println(pve.getAdditionalData().get(keyComment));
                }
            }

            if ((pve.getBaseType() == PumpBaseType.PUMP_DATA_BOLUS)
                    || (pve.getBaseType() == PumpBaseType.PUMP_DATA_PEN_INJECTION_BOLUS))
            {
                pumpValuesHour.addBolus(pve);
            }

            if (StringUtils.isNotBlank(pve.getComment()))
            {
                if (!this.index.containsKey(pve.toString()))
                {
                    System.out.println("C:" + pve.getComment());
                    this.addAdditionalData(PumpDeviceValueType.COMMENT, pve.getComment());
                    this.index.put(pve.toString(), "");
                }
            }
        }

        return pumpValuesHour;
    }

    public void addAdditionalData(PumpDeviceValueType valueType, String partComment)
    {
        additionalData.get(valueType).add(partComment);
    }

    public boolean isAdditionalDataForPumpTypeSet(PumpDeviceValueType type)
    {
        return (this.additionalData.get(type).size() > 0);
    }

    public String getAdditionalDataForPumpTypeSet(PumpDeviceValueType type)
    {
        List<String> listComms = additionalData.get(type);

        if (!listComms.isEmpty())
        {
            StringBuffer sb = new StringBuffer();

            for (String entry : listComms)
            {
                sb.append(entry);
                sb.append(",");
            }

            return sb.toString().substring(0, sb.length() - 1);
        }
        else
            return "";

    }

    public String getFullComment()
    {
        StringBuffer fullComment = new StringBuffer();

        List<String> listComms = additionalData.get(PumpDeviceValueType.BG);

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

        listComms = additionalData.get(PumpDeviceValueType.BOLUS);

        if (!listComms.isEmpty())
        {
            StringBuffer sb = new StringBuffer();
            sb.append(dataAccessPump.getI18nControlInstance().getMessage("BOLUS"));
            sb.append(": ");

            for (String entry : listComms)
            {
                sb.append(entry);
                sb.append(",");
            }

            fullComment.append(sb.toString().substring(0, sb.length() - 1));
            fullComment.append("; ");
        }

        listComms = additionalData.get(PumpDeviceValueType.COMMENT);

        if (!listComms.isEmpty())
        {
            StringBuffer sb = new StringBuffer();
            sb.append(dataAccessPump.getI18nControlInstance().getMessage("OTHER"));
            sb.append(": ");

            for (String entry : listComms)
            {
                sb.append(entry);
                sb.append(",");
            }

            fullComment.append(sb.toString().substring(0, sb.length() - 1));
            fullComment.append("; ");
        }

        if (fullComment.length() == 0)
        {
            return "";
        }
        else
        {
            return fullComment.toString().substring(0, fullComment.length() - 2);
        }

    }

}
