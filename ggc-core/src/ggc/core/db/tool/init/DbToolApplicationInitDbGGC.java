package ggc.core.db.tool.init;

import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.hibernate.cfg.Configuration;

import com.atech.db.hibernate.HibernateUtil;
import com.atech.db.hibernate.tool.app.DbInitType;
import com.atech.db.hibernate.tool.app.DbToolApplicationInitDbAbstract;
import com.atech.db.hibernate.tool.app.DbToolApplicationInterface;
import com.atech.db.hibernate.tool.data.defs.DbInitTaskType;
import com.atech.db.hibernate.tool.data.dto.DbInitTaskDto;
import com.atech.db.hibernate.tool.data.management.DbToolDbHandler;
import com.atech.db.hibernate.tool.data.management.init.DbToolInitDbRunner;

import ggc.core.db.hibernate.food.FoodDescriptionH;
import ggc.core.db.hibernate.food.NutritionHomeWeightTypeH;

public class DbToolApplicationInitDbGGC extends DbToolApplicationInitDbAbstract
{

    HibernateUtil hibernateUtil = null;
    HibernateUtil m_db = null;
    static String path = "../data/nutrition/";

    private static String nutritionFiles[] = { //
                                               "FD_GROUP.txt", //
                                               "FOOD_DES.txt", //
                                               "NUTR_DEF.txt", //
                                               "NUT_DATA.txt", //
                                               "WEIGHT.txt", //
    };
    private static String nutritionFilesDescription[] = { //
                                                          "DBTT_FOOD_GROUPS", //
                                                          "DBTT_FOOD_DESCRIPTION", //
                                                          "DBTT_NUTRITION_DEFINITIONS", //
                                                          "DBTT_NUTRITION_DATA", //
                                                          "DBTT_HOME_WEIGHT_TYPES", //
    };


    public DbToolApplicationInitDbGGC(DbToolApplicationInterface applicationInterface)
    {
        super(applicationInterface);
        createCustomTasks();
    }


    @Override
    public void createCustomTasks()
    {
        addCustomTask("DBTT_SETTINGS", DbInitTaskType.FillDatabase, 1);
        addCustomTask("DBTT_DOCTOR_TYPES", DbInitTaskType.FillDatabase, 76);

        if (checkIfNutritionDbFilesExist())
        {

            System.out.println("Nutrition files exists: ");
            for (int i = 0; i < nutritionFiles.length; i++)
            {
                File f = new File(path + nutritionFiles[i]);

                System.out.println("Nutrition files: " + nutritionFiles[i]);
                try
                {
                    LineNumberReader reader = new LineNumberReader(new FileReader(f));

                    while (reader.readLine() != null)
                    {}

                    addCustomTask(nutritionFilesDescription[i], DbInitTaskType.FillDatabase, reader.getLineNumber());

                    if (nutritionFiles[i].equals("WEIGHT.txt"))
                    {
                        addCustomTask("DBTT_HOME_WEIGHT_DATA", DbInitTaskType.FillDatabase, reader.getLineNumber());
                    }

                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }


    // this.insertFoodGroups();
    // this.insertFoodDescription();
    // this.insertNutritionData();
    // this.insertHomeWeightTypes();
    // this.insertHomeWeightData();
    // this.insertNutritionDefintions();

    private boolean checkIfNutritionDbFilesExist()
    {

        for (String file : nutritionFiles)
        {
            File f = new File(path + file);

            System.out.println("File: " + f.getAbsolutePath());

            if (!f.exists())
                return false;
        }

        return true;
    }


    public DbInitType getDbInitType()
    {
        return null;
    }


    public boolean fillDatabase(Configuration hibernateConfigurationExternal)
    {
        return false;
    }


    public DbToolInitDbRunner getInitDbRunner(DbToolDbHandler.DbInitPreStatus dbInitPreStatus,
            HibernateUtil hibernateUtil)
    {
        Map<String, DbInitTaskDto> dbInitTasks = new HashMap<String, DbInitTaskDto>();

        // if (dbInitPreStatus != DbToolDbHandler.DbInitPreStatus.EmptyDatabase)
        // addTask("Delete Database", DbInitTaskType.DeleteDatabase, 1,
        // dbInitTasks);

        addTask("Create Database", DbInitTaskType.CreateDatabase, 1, dbInitTasks);

        // get custom tasks
        for (DbInitTaskDto task : this.customTasks)
        {
            dbInitTasks.put(task.getTaskName(), task);
        }

        return new GGCDbToolInitDbRunner(dbInitTasks, hibernateUtil, this, this.getDbToolApplication());
    }


    protected void addTask(String taskName, DbInitTaskType initTaskType, int maxCount,
            Map<String, DbInitTaskDto> dbInitTasks)
    {
        DbInitTaskDto initTask = new DbInitTaskDto(taskName, initTaskType, maxCount);

        dbInitTasks.put(taskName, initTask);
    }

    boolean load_nutrition = true;

    // GGCDb m_db = null;

    Hashtable<String, NutritionHomeWeightTypeH> home_weight_type_list = null;
    // DataAccess m_da = DataAccess.getInstance();

    Hashtable<Long, FoodDescriptionH> lst_food_desc = new Hashtable<Long, FoodDescriptionH>();

    /**
     * Password for Init
     */
    public static String passWord = "I_Really_Want_To_Do_This";


    // /**
    // * Constructor
    // */
    // public InitDb()
    // {
    // this(true);
    // }

    /**
     * Constructor
     *
     * @param load_nutr
     */
    public void InitDb(boolean load_nutr)
    {
        long time_start = System.currentTimeMillis();

        // m_db = new GGCDb();
        // m_db.initDb();
        // m_db.createDatabase();
        // this.load_nutrition = load_nutr;
        //
        // setDbInfo();
        // loadSettings();
        // insertDoctorTypes();
        //
        // if (load_nutrition)
        // {
        // loadNutritionDatabase();
        // }
        //
        // // loadMeters();
        // m_db.closeDb();
        // System.out.println();

        long dif = System.currentTimeMillis() - time_start;

        System.out.println("We needed " + dif / 1000 + " seconds to create and fill database.");

    }


    // private void setDbInfo()
    // {
    // DbInfoH dbi = new DbInfoH();
    //
    // dbi.setId(1);
    // dbi.setKey("DB_INFO");
    // dbi.setValue(GGCDb.CURRENT_DB_VERSION);
    //
    // m_db.addHibernate(dbi);
    // }

    /**
     * Set Load Nutrition
     *
     * @param load_nutr
     */
    public void setLoadNutrition(boolean load_nutr)
    {
        this.load_nutrition = load_nutr;
    }


    // /**
    // * Instantiates a new db tool init db.
    // * @param config the config
    // * @param init_type_ the init_type_
    // */
    // public DbToolInitDbGGC(HibernateConfiguration config, int init_type_)
    // {
    // super(config, init_type_);
    // }
    //
    //
    // @Override
    // public boolean fillData()
    // {
    // return false;
    // }

    public static void main(String[] args)
    {
        // new DbToolApplicationInitDbGGC();
    }


    public void setStatus(int procents)
    {

    }


    public void setTask(String task_name)
    {

    }
}
