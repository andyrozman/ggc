package ggc.pump.device.insulet.data.dto.config;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.i18n.I18nControlAbstract;

import ggc.core.util.DataAccess;
import ggc.pump.data.defs.PumpConfigurationGroup;
import ggc.pump.device.insulet.data.enums.OmnipodDataType;
import ggc.pump.device.insulet.data.enums.ProfileType;
import ggc.pump.device.insulet.util.InsuletUtil;
import ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 19.05.15.
 */

public class Profile extends ConfigRecord
{

    private static final Logger LOG = LoggerFactory.getLogger(Profile.class);

    private ProfileHeader header = new ProfileHeader();
    private ProfileType profileType;
    private Map<Integer, Double> basalProfile = null;
    private Map<Integer, Integer> otherProfile = null;
    private static String templateFromValueInt = null;
    private static String templateFromValueDouble = null;


    // profile_hdr: { format: 'b6.Si', fields: [
    // 'profile_idx', 'error_code', 'operation_time'
    // ] },
    public Profile()
    {
        super(true);

        createTranslations();
    }


    private void createTranslations()
    {
        if (templateFromValueInt == null)
        {
            I18nControlAbstract ic = DataAccessPump.getInstance().getI18nControlInstance();

            templateFromValueInt = ic.getMessage("CFG_BASE_FROM") + "=%s, " + ic.getMessage("CFG_BASE_AMOUNT") + "=%d";
            templateFromValueDouble = ic.getMessage("CFG_BASE_FROM") + "=%s, " + ic.getMessage("CFG_BASE_AMOUNT")
                    + "=%4.2f";

        }
    }


    public int process(int[] data)
    {
        length = getShort(data, 0) + 2;
        rawData = data;

        header.process(data);

        this.profileType = ProfileType.getByCode(header.profileIdx);

        if (this.profileType.isBasal())
        {
            basalProfile = new HashMap<Integer, Double>();
        }
        else
        {
            otherProfile = new HashMap<Integer, Integer>();
        }

        this.customProcess(data);

        crc = getShort(data, this.length - 2);

        return this.length;
    }


    @Override
    public void customProcess(int[] data)
    {
        int offset = 15;

        for (int j = 0; j < 48; ++j)
        {
            int value = getIntInverted(offset + (j * 4));

            if (this.profileType.isBasal())
            {
                basalProfile.put(j, toDecimal(value));
                // System.out.println("Time: " + DataAccess.getTimeFromMinutes(j
                // * 30) + " Value: " + toDecimal(value));
            }
            else
            {
                otherProfile.put(j, value);
                // System.out.println("Time: " + DataAccess.getTimeFromMinutes(j
                // * 30) + " Value: " + value);
            }
        }
    }


    @Override
    public OmnipodDataType getOmnipodDataType()
    {
        return OmnipodDataType.Profile;
    }


    @Override
    public String toString()
    {
        return "Profile [" + "header=" + header + ']';
    }


    @Override
    public void writeConfigData()
    {
        if (profileType == ProfileType.Unknown)
        {
            LOG.warn("Unknown profile type. [profileIdx=" + header.profileIdx + "]");
        }

        if (profileType.isBasal())
        {
            I18nControlAbstract i18nControl = InsuletUtil.getI18nControl();

            BasalProgramNames bpn = InsuletUtil.getBasalProgramNames();
            int profileNr = this.profileType.getProfileNr();

            writeSetting(
                i18nControl.getMessage("PCFG_BASAL_PROFILE") + " #" + profileNr + " "
                        + i18nControl.getMessage("CFG_BASE_NAME"),
                bpn.basalNames.get(profileNr), "", PumpConfigurationGroup.Basal);

            int slot = 1;
            String start = DataAccess.getTimeFromMinutes(0);
            Double startValue = basalProfile.get(0);

            for (int i = 0; i < 48; i++)
            {
                if (startValue.doubleValue() != basalProfile.get(i).doubleValue())
                {
                    String key = i18nControl.getMessage("PCFG_BASAL_PROFILE") + " #" + profileNr + " "
                            + i18nControl.getMessage("CFG_BASE_SLOT") + " " + ((slot < 10) ? "0" + slot : slot);

                    writeSetting(key, String.format(templateFromValueDouble, start, startValue), startValue,
                        PumpConfigurationGroup.Basal);
                    slot++;

                    start = DataAccess.getTimeFromMinutes(i * 30);
                    startValue = basalProfile.get(i);
                }
            }

            String key = i18nControl.getMessage("PCFG_BASAL_PROFILE") + " #" + profileNr + " "
                    + i18nControl.getMessage("CFG_BASE_SLOT") + " " + ((slot < 10) ? "0" + slot : slot);

            writeSetting(key, // profileType.getTranslation() + " #" +
                              // profileNr,
                String.format(templateFromValueDouble, start, startValue), startValue, PumpConfigurationGroup.Basal);
            slot++;

        }
        else
        {
            // DataAccess.getTimeFromMinutes(j * 30)

            String start = DataAccess.getTimeFromMinutes(0);
            Integer startValue = otherProfile.get(0);

            int j = 0;
            for (int i = 0; i < 48; i++)
            {
                if (startValue.intValue() != otherProfile.get(i).intValue())
                {
                    writeSetting(profileType.getTranslation() + " #" + j,
                        String.format(templateFromValueInt, start, startValue), startValue,
                        PumpConfigurationGroup.Bolus);
                    j++;

                    start = DataAccess.getTimeFromMinutes(i * 30);
                    startValue = otherProfile.get(i);
                }
            }

            writeSetting(profileType.getTranslation() + " #" + j,
                String.format(templateFromValueInt, start, startValue), startValue, PumpConfigurationGroup.Bolus);

        }

    }
}
