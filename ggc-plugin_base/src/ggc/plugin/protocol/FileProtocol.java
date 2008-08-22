package ggc.plugin.protocol;


import ggc.plugin.util.DataAccessPlugInBase;


//STUB ONLY. Not implemented
//Will be used by Minimed (Pump/CGMS) in future


public abstract class FileProtocol 
{

    protected DataAccessPlugInBase m_da = null; 



    public FileProtocol()
    {
    }

    public FileProtocol(DataAccessPlugInBase da)
    {
        this.m_da = da;
    }


    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_FILE_IMPORT;
    }
    


}
