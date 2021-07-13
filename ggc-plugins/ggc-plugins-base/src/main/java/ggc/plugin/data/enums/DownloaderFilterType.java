package ggc.plugin.data.enums;

import java.util.HashMap;
import java.util.Map;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.CodeEnumWithTranslation;

/**
 * Created by andy on 29/11/17.
 */
public enum DownloaderFilterType implements CodeEnumWithTranslation {

    All(0, "FILTER_ALL", null), // 0
    New(1, "FILTER_NEW", DeviceEntryStatus.New), // 1
    Changed(2, "FILTER_CHANGED", DeviceEntryStatus.Changed), // 2
    Existing(3, "FILTER_EXISTING", DeviceEntryStatus.Old), // 3
    Unknown(4, "FILTER_UNKNOWN", DeviceEntryStatus.Unknown), // 4
    NewChanged(5, "FILTER_NEW_CHANGED", null), // 5
    AllButExisting(6, "FILTER_ALL_BUT_EXISTING", null), // 6

    ;

    int code;
    String i18nKey;
    String translation;
    static boolean translated = false;
    DeviceEntryStatus deviceEntryStatus = null;

    static Map<Integer, DownloaderFilterType> alarmCodeMapping = new HashMap<Integer, DownloaderFilterType>();
    //static Map<String, CodeEnumWithTranslation> translationMap = new HashMap<String, CodeEnumWithTranslation>();
    //static String[] descriptions;

    static
    {
        for (DownloaderFilterType pbt : values())
        {
            alarmCodeMapping.put(pbt.code, pbt);
        }
    }


    public static void translateKeywords(I18nControlAbstract ic)
    {
        if (translated)
            return;

        for (DownloaderFilterType pbt : values())
        {
            pbt.setTranslation(ic.getMessage(pbt.i18nKey));
            //translationMap.put(pbt.getTranslation(), pbt);
        }

        translated = true;
    }


    DownloaderFilterType(int code, String i18nKey, DeviceEntryStatus entryStatus)
    {
        this.code = code;
        this.i18nKey = i18nKey;
    }


    public int getCode()
    {
        return code;
    }


    public String getI18nKey()
    {
        return i18nKey;
    }


    public String getTranslation()
    {
        return translation;
    }


    public void setTranslation(String translation)
    {
        this.translation = translation;
    }


    public String getName()
    {
        return this.name();
    }

    public DeviceEntryStatus getDeviceEntryStatus()
    {
        return this.deviceEntryStatus;
    }

    public String toString()
    {
        return this.getTranslation();
    }

//    public static DownloaderFilterType getByCode(int code)
//    {
//        if (alarmCodeMapping.containsKey(code))
//        {
//            return alarmCodeMapping.get(code);
//        }
//        else
//        {
//            return DownloaderFilterType.UnknownAlarm;
//        }
//    }


//    public static String[] getDescriptions()
//    {
//        return descriptions;
//    }




} 