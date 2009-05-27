using System.Collections.Generic;
using System.IO;
using System;
using System.Data;
using log4net;
namespace GGCMobileNET.Data.Tools
{


// this one should be extended, we have several variables that need to be filled

public abstract class ImportTool : ImportExportAbstract
{

    public Dictionary<String, String> classDef = null;
    private readonly ILog log = LogManager.GetLogger(typeof(ImportTool));

    protected FileInfo restore_file = null;
    //protected BufferedReader file_reader;
    
/*
    public ImportTool(Configuration cfg, int i)
    {
        super(cfg,i);
    }
*/

    public ImportTool() : base()
    {
    }
    
    
    public ImportTool(IDbConnection conn) : base(conn)
    {
    }
    
    public ImportTool(IDbConnection conn, RestoreFileInfo res) : base(conn)
    {
        setRestoreFileInfo(res);
    }
    
    
    public void setRestoreFileInfo(RestoreFileInfo res)
    {
        this.statusSetMaxEntry(res.element_count);
        this.restore_file = res.File;
    }
    
    
    
    

    
    
    
    
    
    

    public int getInt(String input)
    {

        if (input.StartsWith("~"))
            input = input.Substring(1, input.Length - 1);

        if (input.Length == 0)
            return 0;
        else
            return Convert.ToInt32(input);

    }

    public Int16 getShort(String input)
    {
        if (input.StartsWith("~"))
            input = input.Substring(1, input.Length - 1);

        if (input.Length == 0)
            return 0;
        else
            return Convert.ToInt16(input);
    }

    public long getLong(String input)
    {
        if (input.StartsWith("~"))
            input = input.Substring(1, input.Length - 1);

        if (input.Length == 0)
            return 0;
        else
            return Convert.ToInt64(input);
    }

    public float getFloat(String input)
    {

        if (input.StartsWith("~"))
            input = input.Substring(1, input.Length - 1);

        if (input.Length == 0)
            return 0;
        else
            return Convert.ToSingle(input.Replace(',', '.'));

        // return Float.parseFloat(input);

    }

    public String getString(String input)
    {

        if (input.StartsWith("~"))
            input = input.Substring(1, input.Length - 1);

        if (input.Trim().Length == 0)
            return null;

        if (input == "null")
            return null;

        return input;

    }

    
    public void clearExistingData(String table_name)
    {
        //DayValueDAO dvd = new DayValueDAO();
        DummyDAO dd = new DummyDAO();
        
        try
        {
            dd.ProcessSqlItem(this.connection, 
                              null,
                              "delete from " + table_name,
                              DatabaseAccessObject.SQL_DELETE_ALL,
                              log
                              );
        }
        catch(Exception ex)
        {
            Console.WriteLine("Error clearing data: " + ex);
        }
        
        
//        Query q = getSession().createQuery("delete from " + class_name );
//        q.executeUpdate();
    }
    
    
    /*
     * public static void main(String args[]) { GGCDb db = new GGCDb();
     * 
     * ExportTool tool = new ExportTool(db.getConfiguration()); tool.export(); }
     */

}
}