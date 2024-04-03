/*
 * GGC - GNU Gluco Control
 * 
 * A pure java app to help you manage your diabetes.
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename: ImportDailyValues Purpose: Imports daily values (from export from
 * Meter Tool Import, or some export)
 * 
 * Author: andyrozman {andy@atech-software.com}
 */

package ggc.mobile.db.tool.transfer;

import ggc.mobile.db.GGCDbMobile;
import ggc.mobile.db.objects.DayValueDAO;
import ggc.mobile.util.DataAccessMobile;

import java.io.File;
import java.sql.Connection;
import java.util.StringTokenizer;

import com.atech.mobile.db.transfer.BackupRestoreWorkGiver;
import com.atech.mobile.db.transfer.ImportTool;
import com.atech.mobile.db.transfer.RestoreFileInfo;
import com.atech.mobile.utils.log.SimpleLogger;
import com.db4o.ObjectContainer;

public class ImportDailyValues extends ImportTool implements Runnable
{

    GGCDbMobile m_db = null;
    //public String file_name;
    private static SimpleLogger log = new SimpleLogger(); //LogFactory.getLog(ImportDailyValues.class);

    
    
    DataAccessMobile m_da = DataAccessMobile.getInstance();
    boolean clean_db = false; 

    
    public ImportDailyValues(BackupRestoreWorkGiver giver)
    {
        super(DataAccessMobile.getInstance().getDb().getConnection());

        this.setStatusReceiver(giver);
        this.setTypeOfStatus(ImportTool.STATUS_SPECIAL);
    }
    

    public ImportDailyValues(BackupRestoreWorkGiver giver, RestoreFileInfo res)
    {
        super(DataAccessMobile.getInstance().getDb().getConnection(), res);

        this.setStatusReceiver(giver);
        this.setTypeOfStatus(ImportTool.STATUS_SPECIAL);
    }
    
    
    public ImportDailyValues(String file_name)
    {
        this(file_name, true);
    }

    public ImportDailyValues(String file_name, boolean identify)
    {
        super();

        m_db = new GGCDbMobile();
        m_db.initDb();
        this.setConnection(m_db.getConnection());
        this.restore_file = new File(file_name);

        if (identify)
            importDailyValues();

        System.out.println();

    }

    public ImportDailyValues(Connection conn, String file_name)
    {
        super(conn);
        // m_db = new GGCDb();
        // m_db.initDb();
        this.restore_file = new File(file_name);

        importDailyValues();

        System.out.println();
    }

    
    public void setImportClean(boolean clean)
    {
        this.clean_db = clean;
    }
    
    
    public int getActiveSession()
    {
        return 2;
    }
    
    
    
    public void importDailyValues()
    {

        String line = null;
        boolean append = false;

        // TODO
        ObjectContainer db = this.m_db.getDb();
        
        
        try
        {
            DayValueDAO dvh;
            
            if (clean_db)
            {
                dvh = new DayValueDAO();
                this.clearExistingData(dvh.getTableName());
            }
            else
                append = true;
            
            System.out.println("\nLoading DailiyValues (5/dot)");
            
            this.openFileForReading(this.restore_file);

            //BufferedReader br = new BufferedReader(new FileReader(this.restore_file)); //new File(file_name)));

           // int i = 0;
            
            int dot_mark = 5;
            int count = 0;
            
            
            

            while ((line = this.br_file.readLine()) != null)
            {
                if (line.startsWith(";"))
                    continue;

                // line = line.replaceAll("||", "| |");
                line = m_da.replaceExpression(line, "||", "| |");

                StringTokenizer strtok = new StringTokenizer(line, "|");

                dvh = new DayValueDAO();

                // ; Columns: id,dt_info,bg,ins1,ins2,ch,meals_ids,act,comment

                // 1|200603250730|0|10.0|0.0|0.0|null|null|
                // id; dt_info; bg; ins1; ins2; ch; meals_ids; extended;
                // person_id; comment

                if (!append)
                {
                    long id = this.getLong(strtok.nextToken());
    
                    if (id != 0)
                        dvh.setId(id);
                }
                else
                {
                    strtok.nextToken();
                }
                
                dvh.setDt_info(getLong(strtok.nextToken()));
                dvh.setBg(getInt(strtok.nextToken()));
                dvh.setIns1((int) getFloat(strtok.nextToken()));
                dvh.setIns2((int) getFloat(strtok.nextToken()));
                dvh.setCh(getFloat(strtok.nextToken()));
                dvh.setMeals_ids(getString(strtok.nextToken()));
                dvh.setExtended(getString(strtok.nextToken()));

                int person_id = this.getInt(strtok.nextToken());

                if (person_id == 0)
                    dvh.setPerson_id(1);
                else
                    dvh.setPerson_id(person_id);

                dvh.setComment(getString(strtok.nextToken()));
                dvh.setChanged(getLong(strtok.nextToken()));

                /*
                 * String act = getString(strtok.nextToken());
                 * 
                 * if (act != null) { dvh.setExtended("ACTIVITY=" + act); }
                 * 
                 * String bef = "MTI"; // String bef = null;
                 * 
                 * if (strtok.hasMoreElements()) { String comm =
                 * getString(strtok.nextToken());
                 * 
                 * // remove if (bef!=null) comm += ";" + bef;
                 * 
                 * if (comm!=null) dvh.setComment(comm); } else { if (bef!=null)
                 * dvh.setComment(bef); }
                 */

                // TODO
                db.store(dvh);
                
//XA                this.m_db.addDAO(dvh);
                //this.hibernate_util.addHibernate(dvh);

                
                count++;
                this.writeStatus(dot_mark, count);
                
                /*
                i++;

                if (i % 5 == 0)
                    System.out.print(".");*/
            }
            
            this.closeFile();

        }
        catch (Exception ex)
        {
            // System.err.println("Error on loadDailyValues: " + ex);
            log.error("Error on importDailyValues: \nData: " + line + "\nException: " + ex, ex);
            // ex.printStackTrace();
        }
        finally
        {
            db.close();
        }

    }

    
    public void run()
    {
        this.importDailyValues();
    }    
    
    
    public static void main(String args[])
    {
        if (args.length == 0)
        {
            System.out.println("You need to specify import file !");
            return;
        }

        // GGCDb db = new GGCDb();

        new ImportDailyValues(args[0]);
    }

}
