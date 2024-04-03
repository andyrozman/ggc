package ggc.connect.software.web.nightscout.data.defs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andy on 4/25/18.
 */
public enum EntryType {

    Sgv("sgv"), //
    Calibration("cal"),
    MeterBg("mbg"); //

    private String code;
    private static Map<String,EntryType> mapByCode;


    static
    {
        mapByCode = new HashMap<String, EntryType>();

        for (EntryType entryType : values()) {
            mapByCode.put(entryType.getCode(), entryType);
        }
    }



    EntryType(String code) {
        this.code = code;
    }

    public static EntryType getByCode(String type) {
        return null;
    }

    public String getCode() {
        return code;
    }
}