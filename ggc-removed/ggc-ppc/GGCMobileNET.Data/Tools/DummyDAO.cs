using log4net;
using System;
namespace GGCMobileNET.Data.Tools
{

public class DummyDAO : DatabaseAccessObject
{

    private readonly ILog log = LogManager.GetLogger(typeof(DummyDAO));

    /*  full constructor */
    public DummyDAO()
    {
    }


    public String toString()
    {
        return "DummyDAO";
    }


    public override string GetSQL(int index)
    {
        return null;
    }
    
    public String getObjectName()
    {
        return "DummyDAO";
    }
    
    
    
    public String getTableName()
    {
        return "none";
    }
    
    

    public String getObjectUniqueId()
    {
        return "fldjksfjdsfjhsdjfkhsd";
    }

    
    public String getIdColumnName()
    {
        return "id";
    }



    public override string ObjectName
    {
        get
        {
            return "DummyDAO";
        }
    }

    public override bool GetDb(int subType, System.Data.DataRow row)
    {
        throw new System.NotImplementedException();
    }

    public override string TableName
    {
        get 
        {
            return "";
        }
    }

    public override string IdColumnName
    {
        get
        {
            return "id";
        }
    }

    public override string ObjectUniqueId
    {
        get
        {
            return "0";
        }
    }
}
}