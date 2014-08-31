package ggc.cgms.data.defs.extended;

import java.util.HashMap;

public enum CGMSExtendedDataType {

    
    None(0, "NONE"), //
    Carbs(1, "CGMS_EXT_CARBS"), // 
    Insulin(2, "CGMS_EXT_INSULIN"), //
    Health(3, "CGMS_EXT_HEALTH"), //
    Exercise(4, "CGMS_EXT_EXERCISE"), //
    ;
    
    
    
    private int value;
    private String description;
    private static HashMap<Integer,CGMSExtendedDataType> map = new HashMap<Integer,CGMSExtendedDataType>();
    
    static
    {
        for(CGMSExtendedDataType el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    CGMSExtendedDataType(int value, String description)
    {
        this.value = value;
        this.description = description;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public static CGMSExtendedDataType getEnum(int value)
    {
        if (map.containsKey(value))
            return map.get(value);
        else
            return CGMSExtendedDataType.None;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
