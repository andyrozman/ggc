package main.java.ggc.pump.device.insulet.data.enums;

import java.util.HashMap;
import java.util.Map;

import com.atech.i18n.I18nControlAbstract;

/**
 * Created by andy on 25.05.15.
 */
public enum ProfileType
{
    Unknown(0, "Unknown", false, ""), //
    CarbRatio(11, "IC Ratio", false, "PCFG_CH_INS_RATIO"), //
    InsulinSensitivity(12, "Correction", false, "PCFG_BG_INS_RATIO"), //
    BGTarget(13, "Target BG", false, "PCFG_TARGET_BG"), //
    BGTreshold(14, "BG Threshold", false, "PCFG_BG_TRESHOLD"), //
    BasalProfile0(15, "Basal Profile 0", true, "PCFG_BASAL_PROFILE", 0), //
    BasalProfile1(16, "Basal Profile 1", true, "PCFG_BASAL_PROFILE", 1), //
    BasalProfile2(17, "Basal Profile 2", true, "PCFG_BASAL_PROFILE", 2), //
    BasalProfile3(18, "Basal Profile 3", true, "PCFG_BASAL_PROFILE", 3), //
    BasalProfile4(19, "Basal Profile 4", true, "PCFG_BASAL_PROFILE", 4), //
    BasalProfile5(20, "Basal Profile 5", true, "PCFG_BASAL_PROFILE", 5), //
    BasalProfile6(21, "Basal Profile 6", true, "PCFG_BASAL_PROFILE", 6), //

    ;

    private int code;
    private String description;
    private boolean isBasal;
    private String i18nKey;
    private String translation;
    private Integer profileNr;

    static Map<Integer, ProfileType> mapByCode = new HashMap<Integer, ProfileType>();

    static
    {
        for (ProfileType hrt : values())
        {
            mapByCode.put(hrt.code, hrt);
        }
    }


    private ProfileType(int code, String description, boolean isBasal, String i18nKey)
    {
        this(code, description, isBasal, i18nKey, null);
    }


    private ProfileType(int code, String description, boolean isBasal, String i18nKey, Integer profileNr)
    {
        this.code = code;
        this.description = description;
        this.isBasal = isBasal;
        this.i18nKey = i18nKey;
        this.profileNr = profileNr;
    }


    public static void translateKeywords(I18nControlAbstract i18n)
    {
        for (ProfileType hrt : values())
        {
            hrt.translation = i18n.getMessage(hrt.i18nKey);

            if (hrt.profileNr != null)
            {
                hrt.translation += " #" + hrt.profileNr;
            }
        }
    }


    public static ProfileType getByCode(int code)
    {
        if (mapByCode.containsKey(code))
        {
            return mapByCode.get(code);
        }

        return ProfileType.Unknown;
    }


    public int getCode()
    {
        return code;
    }


    public String getDescription()
    {
        return description;
    }


    public String getTranslation()
    {
        return translation;
    }


    public boolean isBasal()
    {
        return isBasal;
    }


    public Integer getProfileNr()
    {
        return profileNr;
    }

    // var PROFILES = {
    // carbRatio: {
    // value: 11, name: 'carbRatio', mfrname: 'IC Ratio', isBasal: false,
    // keyname: 'amount', valuename: 'value'
    // },
    // insulinSensitivity: {
    // value: 12, name: 'insulinSensitivity', mfrname: 'Correction', isBasal:
    // false,
    // keyname: 'amount', valuename: 'value'
    // },
    // bgTarget: {
    // value: 13, name: 'bgTarget', mfrname: 'Target BG', isBasal: false,
    // keyname: 'low', valuename: 'value'
    // },
    // bgThreshold: {
    // value: 14, name: 'bgThreshold', mfrname: 'BG Threshold', isBasal: false,
    // keyname: 'amount', valuename: 'value'
    // },
    // basalprofile0: {
    // value: 15, name: 'basalprofile0', mfrname: 'Basal Profile 0', isBasal:
    // true,
    // keyname: 'rate', valuename: 'units'
    // },
    // basalprofile1: {
    // value: 16, name: 'basalprofile1', mfrname: 'Basal Profile 1', isBasal:
    // true,
    // keyname: 'rate', valuename: 'units'
    // },
    // basalprofile2: {
    // value: 17, name: 'basalprofile2', mfrname: 'Basal Profile 2', isBasal:
    // true,
    // keyname: 'rate', valuename: 'units'
    // },
    // basalprofile3: {
    // value: 18, name: 'basalprofile3', mfrname: 'Basal Profile 3', isBasal:
    // true,
    // keyname: 'rate', valuename: 'units'
    // },
    // basalprofile4: {
    // value: 19, name: 'basalprofile4', mfrname: 'Basal Profile 4', isBasal:
    // true,
    // keyname: 'rate', valuename: 'units'
    // },
    // basalprofile5: {
    // value: 20, name: 'basalprofile5', mfrname: 'Basal Profile 5', isBasal:
    // true,
    // keyname: 'rate', valuename: 'units'
    // },
    // basalprofile6: {
    // value: 21, name: 'basalprofile6', mfrname: 'Basal Profile 6', isBasal:
    // true,
    // keyname: 'rate', valuename: 'units'
    // }
    // };

}
