package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

public enum ClockModeType
{
    ClockMode24Hour(0, "CLOCK_MODE_12H"), // 
    ClockMode12Hour(1, "CLOCK_MODE_24H"), //
    ;
    
    private int value;
    private String description;
    private static HashMap<Integer, ClockModeType> map = new HashMap<Integer, ClockModeType>();

    static
    {
        for (ClockModeType el : values())
        {
            map.put(el.getValue(), el);
        }
    }

    ClockModeType(int value, String description)
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

    public static ClockModeType getEnum(int value)
    {
        return map.get(value);
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
