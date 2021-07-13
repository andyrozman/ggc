package ggc.connect.enums;

public enum ConnectHandlerConfiguration
{

    DiaSendExcelImport("connect/diasend_xls.jpg", //
            "DIASEND_XSL_NAME", "DIASEND_XSL_DESCRIPTION", "DIASEND_XSL_SPECIAL_DESCRIPTION", //
            ConnectConfigType.File, "DIASEND_XSL_SELECT_FILE", ConnectOperationType.ImportAndView), //

    ;

    private String iconName;
    private String name;
    private String description;
    private String specialDescription;
    private ConnectConfigType configType;
    private String configTypeValue;
    private ConnectOperationType operationType;


    ConnectHandlerConfiguration(String iconName, String name, String description, String specialDescription,
            ConnectConfigType configType, String configTypeValue, ConnectOperationType operationType)
    {
        this.iconName = iconName;
        this.name = name;
        this.description = description;
        this.specialDescription = specialDescription;
        this.configType = configType;
        this.configTypeValue = configTypeValue;
        this.operationType = operationType;
    }


    public String getIconName()
    {
        return iconName;
    }


    public String getDescription()
    {
        return description;
    }


    public String getSpecialDescription()
    {
        return specialDescription;
    }


    public String getConfigTypeValue()
    {
        return configTypeValue;
    }


    public ConnectConfigType getConfigType()
    {
        return configType;
    }


    public String getName()
    {
        return name;
    }


    public ConnectOperationType getOperationType()
    {
        return operationType;
    }
}
