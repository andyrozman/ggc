using System;
using System.IO;
using System.Data;
using System.Text;
using System.Globalization;
using GGCMobileNET.Data.Utils;
namespace GGCMobileNET.Data.Tools
{

public abstract class ImportExportAbstract
{

    protected int status_type = 0;
    protected int status_max_entry;

    public static int STATUS_NONE = 0;
    public static int STATUS_DOT = 1;
    public static int STATUS_PROCENT = 2;
    public static int STATUS_SPECIAL = 3;

    protected String path_root = null;
    protected String file_2nd_part = null;

    BackupRestoreWorkGiver work_giver = null;

    protected StreamWriter bw_file = null;
    protected StreamReader br_file = null;

    protected IDbConnection connection;

    public ImportExportAbstract(IDbConnection conn)
    {
        this.connection = conn;
    }
    
    
    public ImportExportAbstract()
    {
    }
    
    


    public void setRootPath(String path)
    {
        this.path_root = path;
    }

    public String getRootPath()
    {
        return this.path_root;
    }

    public String getFileLastPart()
    {
        return this.file_2nd_part;
    }

    public void setFileLastPart(String last_part)
    {
        this.file_2nd_part = last_part;
    }

    
    
    
    public void println(String txt)
    {
        Console.WriteLine(txt);
    }

    
    public void setConnection(IDbConnection conn)
    {
        this.connection = conn;
    }
    

    public void openFile(String file)
    {
        try
        {
            //bw_file = new BufferedWriter(new FileWriter(new File(file)));

            bw_file = new StreamWriter(new FileStream(file, FileMode.CreateNew), Encoding.UTF8);
            //BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(file)),"UTF8"));
        }
        catch (Exception ex)
        {
            println("Exception on openFile: " + ex);
        }

    }

    
    public void openFileForReading(FileInfo file)
    {
        try
        {
            br_file = new StreamReader(new FileStream(file.FullName, FileMode.Open), Encoding.UTF8);
                //new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF8"));
        }
        catch (Exception ex)
        {
            println("Exception on openFileReading: " + ex);
        }

    }
    
    

    public void setTypeOfStatus(int type)
    {
        this.status_type = type;
    }

    public void setStatusReceiver(BackupRestoreWorkGiver giver)
    {
        this.work_giver = giver;
    }

    public void statusSetMaxEntry(int max_entry)
    {
        //System.out.println("max entries: " + max_entry);
        this.status_max_entry = max_entry;
    }

    public void writeStatus(int every_x_entry, int count)
    {
        if (this.status_type == ExportTool.STATUS_NONE)
            return;
        else if (this.status_type == ExportTool.STATUS_PROCENT)
            writeStatusProcent(every_x_entry, count);
        else if (this.status_type == ExportTool.STATUS_DOT)
            this.writeStatusDots(every_x_entry, count);
        else if (this.status_type == ExportTool.STATUS_SPECIAL)
            this.writeStatusSpecial(every_x_entry, count);
    }

    public void writeStatusDots(int every_x_entry, int count)
    {
        if (count % every_x_entry == 0)
            Console.WriteLine(".");
    }

    public void writeStatusProcent(int every_x_entry, int count)
    {
        if (count % every_x_entry == 0)
        {
            if (this.status_max_entry <= 0)
                return;
            else
            {
                float val = (count * 1.0f) / this.status_max_entry;

                if (val > 1)
                    Console.WriteLine("100% (?)");
                else
                    Console.WriteLine((int)(val * 100) + "%");
            }
        }
    }

    public void writeStatusSpecial(int every_x_entry, int count)
    {
        if (this.status_max_entry <= 0)
        {
            Console.WriteLine("Status main entry problem");
            return;
        }
        else
        {
            float val = (count * 1.0f) / this.status_max_entry;

            if (val > 1)
            {
                Console.WriteLine("Set status 100");
                this.work_giver.setStatus(100);
            }
            else
            {
                // System.out.println("Set Status: " + (int)(val * 100)) ;
                this.work_giver.setStatus((int) (val * 100));
            }
        }

    }

    public void writeToFile(String entry)
    {
        try
        {
            bw_file.Write(entry);
            bw_file.Flush();
        }
        catch (Exception ex)
        {
            println("Exception on writeToFile: " + ex);
        }

    }

    public void closeFile()
    {
        try
        {
            if (bw_file != null)
                bw_file.Close();

            if (br_file != null)
                br_file.Close();
        }
        catch (Exception ex)
        {
            println("Exception on closeFile: " + ex);
        }

    }

    protected String getCurrentDate()
    {
        DateTime gc = DateTime.Now;
        //gc.setTimeInMillis(System.currentTimeMillis());

        return gc.Day + "/"
                + gc.Month  + "/"
                + gc.Year + "  "
                + gc.Hour + ":"
                + gc.Minute + ":"
                + gc.Second;
    }

    public String getCurrentDateForFile()
    {
        DateTime gc = DateTime.Now; // new GregorianCalendar();
        //gc.setTimeInMillis(System.currentTimeMillis());

        return gc.Year + "_"
                + getLeadingZero(gc.Month, 2)
                + "_"
                + getLeadingZero(gc.Day, 2);

    }

    public String getLeadingZero(int number, int places)
    {
        return DataAccessMobile.Instance.getLeadingZero(number, places);
    }

    /*
    protected bool dbExecute(Connection conn, String sql) //throws Exception
    {
        try
        {
            Statement st = conn.createStatement();
            st.execute(sql);
        }
        catch(Exception ex)
        {
            throw ex;
        }
        
        return true;
        
    }
    

    protected ResultSet dbExecuteQuery(Connection conn, String sql) //throws Exception
    {
        try
        {
            Statement st = conn.createStatement();
            return st.executeQuery(sql);
        }
        catch(Exception ex)
        {
            throw ex;
        }
        
        
    }
    */
    
    

}
}