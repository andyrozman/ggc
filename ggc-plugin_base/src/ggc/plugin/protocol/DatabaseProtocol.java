package ggc.plugin.protocol;


import ggc.plugin.util.DataAccessPlugInBase;


// STUB ONLY. Not implemented
// Will be used by Animas (Pump) in future

public abstract class DatabaseProtocol 
{

    protected DataAccessPlugInBase m_da = null; 



    public DatabaseProtocol()
    {
    }

    public DatabaseProtocol(DataAccessPlugInBase da)
    {
        this.m_da = da;
    }


    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_FILE_IMPORT;
    }
    


}
