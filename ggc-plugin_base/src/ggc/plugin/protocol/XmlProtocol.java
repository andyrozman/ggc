package ggc.plugin.protocol;


import ggc.plugin.util.DataAccessPlugInBase;

//STUB ONLY. Not implemented
//Will be used by Roche (Pump/Meter) in future


public abstract class XmlProtocol 
{

    protected DataAccessPlugInBase m_da = null; 



    public XmlProtocol()
    {
    }

    public XmlProtocol(DataAccessPlugInBase da)
    {
        this.m_da = da;
    }


    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_FILE_IMPORT;
    }
    


}
