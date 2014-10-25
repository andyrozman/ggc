package ggc.core.plugins;

public enum GGCPluginType
{
    METER_TOOL_PLUGIN("MetersPlugIn"), //
    PUMP_TOOL_PLUGIN("PumpsPlugIn"), //
    NUTRITION_TOOL_PLUGIN("NutritionPlugIn"), //
    CGMS_TOOL_PLUGIN("CGMSPlugIn");

    private String key;

    private GGCPluginType(String key)
    {
        this.key = key;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

}
