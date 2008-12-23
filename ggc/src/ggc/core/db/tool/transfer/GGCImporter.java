package ggc.core.db.tool.transfer;

import ggc.core.db.GGCDb;
import ggc.core.db.datalayer.DailyValue;
import ggc.core.db.datalayer.SettingsColorScheme;
import ggc.core.util.DataAccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.db.hibernate.HibernateConfiguration;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.db.hibernate.transfer.BackupRestoreWorkGiver;
import com.atech.db.hibernate.transfer.ExportTool;
import com.atech.db.hibernate.transfer.ImportTool;
import com.atech.db.hibernate.transfer.RestoreFileInfo;

public class GGCImporter extends ImportTool implements Runnable
{

    GGCDb m_db = null;
    public String file_name;
    private static Log log = LogFactory.getLog(GGCImporter.class);
    String selected_class = null;
    DataAccess m_da = DataAccess.getInstance();

    
    public GGCImporter(BackupRestoreWorkGiver giver)
    {
        super(DataAccess.getInstance().getDb().getHibernateConfiguration());

        //checkPrerequisitesForResAutoBackup();

        this.setStatusReceiver(giver);
        this.setTypeOfStatus(ExportTool.STATUS_SPECIAL);
        this.setRootPath("../data/import/");

        // exportAll();
    }

    public GGCImporter(HibernateConfiguration cfg)
    {
        super(cfg);

        this.setTypeOfStatus(ExportNutritionDb.STATUS_DOT);
        this.setRootPath("../data/import/");

        //checkPrerequisites();
        //exportAll();
    }
    
    
    public GGCImporter(BackupRestoreWorkGiver giver, RestoreFileInfo res)
    {
        super(DataAccess.getInstance().getDb().getHibernateConfiguration(), res);

        this.setStatusReceiver(giver);
        this.setTypeOfStatus(ImportTool.STATUS_SPECIAL);
        this.setRootPath("../data/import/");
    }
    
    
    public GGCImporter(String file_name)
    {
        this.file_name = file_name;
        this.identifyAndImport();
    }

    public void identifyAndImport()
    {

        this.checkFileTarget();

        log.debug("Importing file '" + this.file_name + "'.");

        if (this.selected_class.equals("None"))
        {
            System.out.println("Class type for export class was unidentified. Exiting !");
            log.debug("File was not identified as valid import file !!!");
        }
        else if (this.selected_class.equals("ggc.core.db.hibernate.FoodUserDescriptionH"))
        {
            log.debug("File was identified as 'ggc.core.db.hibernate.FoodUserDescriptionH'.");
            ImportNutrition in = new ImportNutrition(this.file_name, false);
            in.importUserFood();
        }
        else if (this.selected_class.equals("ggc.core.db.hibernate.FoodUserGroupH"))
        {
            log.debug("File was identified as 'ggc.core.db.hibernate.FoodUserGroupH'.");
            ImportNutrition in = new ImportNutrition(this.file_name, false);
            in.importUserGroups();
        }
        else if (this.selected_class.equals("ggc.core.db.hibernate.MealH"))
        {
            log.debug("File was identified as 'ggc.core.db.hibernate.MealH'.");
            ImportNutrition in = new ImportNutrition(this.file_name, false);
            in.importMeals();
        }
        else if (this.selected_class.equals("ggc.core.db.hibernate.MealGroupH"))
        {
            log.debug("File was identified as 'ggc.core.db.hibernate.MealGroupH'.");
            ImportNutrition in = new ImportNutrition(this.file_name, false);
            in.importMealGroups();
        }
        else if (this.selected_class.equals("ggc.core.db.hibernate.DayValueH"))
        {
            log.debug("File was identified as 'ggc.core.db.hibernate.DayValueH'.");
            ImportDailyValues idv = new ImportDailyValues(this.file_name, false);
            idv.importDailyValues();
        }

        System.out.println();

    }

    public void checkFileTarget()
    {
        selected_class = "None";

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(new File(file_name)));

            String line;

            while ((line = br.readLine()) != null)
            {
                // ; Class: ggc.core.db.hibernate.FoodUserDescriptionH

                if (line.contains("Class:"))
                {
                    String[] cls = m_da.splitString(line, " ");
                    this.selected_class = cls[2];
                }

            }

        }
        catch (Exception ex)
        {

        }
    }
    

    private BackupRestoreObject getBackupRestoreObject(BackupRestoreObject bro)
    {
        return getBackupRestoreObject(bro.getBackupClassName());
    }
    
    
    private BackupRestoreObject getBackupRestoreObject(String class_name)
    {
        if (class_name.equals("ggc.core.db.hibernate.DayValueH"))
        {
     //       DayValueH eh = (DayValueH)obj;
            return new DailyValue();
        }
        else if (class_name.equals("ggc.core.db.hibernate.ColorSchemeH"))
        {
       //     ColorSchemeH eh = (ColorSchemeH)obj;
            return new SettingsColorScheme();
        }
        else
            return null;
           
            
    }

    
    public void importData(String object_class_name)
    {
        importData(getBackupRestoreObject(object_class_name));   
    }
    
    
    public void importData(BackupRestoreObject bro)
    {
        importData(bro, true);
    }

    
    public void importData(BackupRestoreObject bro, boolean clean_db)
    {

        String line = null;
        boolean append = false;
        
        try
        {
            
            //if (!clean_db)
            //    append = true;
            
            if ((bro.hasToBeCleaned()) || (clean_db))
                this.clearExistingData(bro.getBackupClassName());
            else
                append = true;
            
            //System.out.println("\nLoading DailyValues (5/dot)");
            //System.out.println("getRoot: " + this.getRootPath());
            
            
            this.openFileForReading(new File(this.getRootPath() + bro.getBackupFile() + ".dbe"));

            //BufferedReader br = new BufferedReader(new FileReader(this.restore_file)); //new File(file_name)));

           // int i = 0;
            
            int dot_mark = 5;
            int count = 0;

            while ((line = this.br_file.readLine()) != null)
            {
                if ((line.startsWith(";")) || (line.trim().length()==0))
                    continue;

                BackupRestoreObject bro_new = this.getBackupRestoreObject(bro);
                
                if (append)
                    bro_new.dbImport(bro.getTableVersion(), line, new Object[1]);
                else
                    bro_new.dbImport(bro.getTableVersion(), line);

                
                
                /*
                // line = line.replaceAll("||", "| |");
                line = m_da.replaceExpression(line, "||", "| |");

                StringTokenizer strtok = new StringTokenizer(line, "|");

                DayValueH dvh = new DayValueH();

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

                this.hibernate_util.add(bro_new); //dvh);

                
                count++;
                this.writeStatus(dot_mark, count);
                
            }
            
            this.closeFile();

        }
        catch (Exception ex)
        {
            log.error("Error on importData: \nData: " + line + "\nException: " + ex, ex);
        }

    }
    
    
    

    public static void main(String args[])
    {
        if (args.length == 0)
        {
            System.out.println("You need to specify import file !");
            return;
        }

        new GGCImporter(args[0]);
    }

    @Override
    public int getActiveSession()
    {
        return 2;
    }

    public void run()
    {
        // TODO Auto-generated method stub
        
    }

}
