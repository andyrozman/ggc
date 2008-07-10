package ggc.pump.protocol;

public class ConnectionProtocols
{
    
    public static final int PROTOCOL_NONE = 0;
    public static final int PROTOCOL_SERIAL_USBBRIDGE = 1;
    public static final int PROTOCOL_USB = 2;
    public static final int PROTOCOL_MASS_STORAGE_XML = 3;
    public static final int PROTOCOL_BLUE_TOOTH = 4;
    
    public static String[] connectionProtocolDescription = { 
                             "PROT_NONE",
                             "PROT_SERIAL_BRIDGE",
                             "PROT_SERIAL_USB",
                             "PROT_MASS_STORAGE_XML",
                             "PROT_BLUE_TOOTH"
                           };
    

    /*
    public String getConnectionProtocolDescription(int type)
    {
        return this.desc[type];
    }*/
    
    
}

