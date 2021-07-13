package ggc.connect.enums;

public enum ConnectConfigType
{
    File("CONNECT_SELECT_FILE"), //
    Special("");

    private String description;


    ConnectConfigType(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return description;
    }

}
