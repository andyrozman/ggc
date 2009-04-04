package ggc.nutri.db;

import ggc.core.data.DailyValues;
import ggc.core.data.DailyValuesRow;
import ggc.core.data.DayValuesData;
import ggc.core.data.HbA1cValues;
import ggc.core.data.MonthlyValues;
import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.DayValueH;
import ggc.core.db.hibernate.FoodDescriptionH;
import ggc.core.db.hibernate.FoodGroupH;
import ggc.core.db.hibernate.FoodUserDescriptionH;
import ggc.core.db.hibernate.FoodUserGroupH;
import ggc.core.db.hibernate.MealGroupH;
import ggc.core.db.hibernate.MealH;
import ggc.core.db.hibernate.NutritionDefinitionH;
import ggc.core.db.hibernate.NutritionHomeWeightTypeH;
import ggc.core.util.DataAccess;
import ggc.nutri.data.GGCTreeRoot;
import ggc.nutri.data.GGCTreeRootDyn;
import ggc.nutri.db.datalayer.FoodDescription;
import ggc.nutri.db.datalayer.FoodGroup;
import ggc.nutri.db.datalayer.Meal;
import ggc.nutri.db.datalayer.MealGroup;
import ggc.nutri.db.datalayer.NutritionDefinition;
import ggc.nutri.db.datalayer.NutritionHomeWeightType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.db.hibernate.HibernateConfiguration;
import com.atech.db.hibernate.HibernateDb;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.utils.ATechDate;


/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     GGCDb  
 *  Description:  Class for working with database (Hibernate)
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class GGCDbNutri extends HibernateDb //extends GGCDb 
{
    //public static final int DB_CONFIG_LOADED = 1;
    //public static final int DB_INITIALIZED = 2;
    //public static final int DB_STARTED = 3;

    private boolean debug = true;
    // x private boolean db_debug = false;

    private static Log log = LogFactory.getLog(GGCDbNutri.class);
//    private Session m_session = null;
//    private Session m_session_2 = null;
//    private SessionFactory sessions = null;
//    private int m_errorCode = 0;
//    private String m_errorDesc = "";
//    private String m_addId = "";

    private DataAccess m_da;

    private int m_loadStatus = 0;

    // GLOBAL DATA

    /*
     * public ArrayList<MeterCompanyH> meter_companies = null; public
     * Hashtable<String,ArrayList<MeterH>> meters_by_cmp = null; public
     * Hashtable<String,MeterH> meters_full = null;
     */

    // ---
    // --- DB Settings
    // ---
    /*
     * protected int db_num = 0; protected String db_hib_dialect = null;
     * protected String db_driver_class = null; protected String db_conn_name =
     * null; protected String db_conn_url = null; protected String
     * db_conn_username = null; protected String db_conn_password = null;
     */

    HibernateConfiguration hib_config;
    
    /**
     * Constructor 
     * 
     * @param db 
     */
    public GGCDbNutri(GGCDb db)
    {
        this.hib_config = db.getHibernateConfiguration();

        System.out.println("hib_Config:" + this.hib_config);
        //this.hib_config.getS
        
        cache_db = new GGCDbCache(this);
        
    }

    


    
    // *************************************************************
    // **** SETTINGS ****
    // *************************************************************

    
    // *************************************************************
    // **** SETTINGS ****
    // *************************************************************

    GGCDbCache cache_db = null;
    

    /**
     * Get Db Cache
     * 
     * @return
     */
    public GGCDbCache getDbCache()
    {
        return cache_db;
    }
    
    
    // *************************************************************
    // **** DATABASE INIT METHODS ****
    // *************************************************************

    
    /**
     * Load Nutrition Database
     */
    public void loadNutritionDatabase()
    {
        
        /*
        this.loadNutritionDbBase();
        this.loadNutritionDb1();
        this.loadNutritionDb2();
        this.loadMealsDb();*/
    }
    
    
    
    /**
     * Load Nutrition Db Base
     */
    public void loadNutritionDbBase()
    {
        this.loadNutritionDefinitions();
        this.loadHomeWeights();
    }

    /**
     * Load Nutrition Db 1 - USDA
     */
    public void loadNutritionDb1()
    {
        // tree root, now in static data
        cache_db.tree_roots.put("1", new GGCTreeRootDyn(GGCTreeRoot.TREE_USDA_NUTRITION, this));

        // m_da.m_nutrition_treeroot = new GGCTreeRoot(1, this);

    }

    /**
     * Load Nutrition Db 2 - User
     */
    public void loadNutritionDb2()
    {
        cache_db.tree_roots.put("2", new GGCTreeRootDyn(GGCTreeRoot.TREE_USER_NUTRITION, this));
    }

    /**
     * Load Meals Db
     */
    public void loadMealsDb()
    {
        cache_db.tree_roots.put("3", new GGCTreeRootDyn(GGCTreeRoot.TREE_MEALS, this));
    }


    // *************************************************************
    // **** NUTRITION DATA ****
    // *************************************************************
    

    /**
     * Get Food Groups - USDA
     * 
     * @param type 
     * @param parent_id 
     * @return
     */
    public ArrayList<FoodGroup> getFoodGroups(int type, long parent_id)
    {
        if (type==GGCTreeRoot.TREE_USER_NUTRITION)
        {
            return this.getUserFoodGroups(parent_id);
        }
        else
        {
            if (parent_id == 0)
                return this.getUSDAFoodGroups();
            else
                return new ArrayList<FoodGroup>();
        }
    }
    
    
    /**
     * Get Food Group By Id
     * 
     * @param type
     * @param id
     * @return
     */
    public FoodGroup getFoodGroupById(int type, long id)
    {
        if (type==GGCTreeRoot.TREE_USER_NUTRITION)
        {
            return this.getUserFoodGroup(id);
        }
        else
        {
            return this.getUSDAFoodGroup(id);
        }
    }
    
    
    /**
     * Get Food Groups - USDA
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<FoodGroup> getUSDAFoodGroups()
    {

        logInfo("getUSDAFoodGroups()");

        ArrayList<FoodGroup> list = new ArrayList<FoodGroup>();

        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.FoodGroupH as pst order by pst.name");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                FoodGroupH eh = (FoodGroupH) it.next();
                list.add(new FoodGroup(eh));
            }

        }
        catch (Exception ex)
        {
            logException("getUSDAFoodGroups()", ex);
        }

        return list;

    }


    /**
     * Get USDA Food Group
     * 
     * @param id 
     * @return
     */
    @SuppressWarnings("unchecked")
    public FoodGroup getUSDAFoodGroup(long id)
    {

        logInfo("getUSDAFoodGroup(id=)" + id + ")");

        //ArrayList<FoodGroup> list = new ArrayList<FoodGroup>();

        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.FoodGroupH as pst where pst.id=" + id);

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                FoodGroupH eh = (FoodGroupH) it.next();
                return new FoodGroup(eh);
            }

        }
        catch (Exception ex)
        {
            logException("getUSDAFoodGroups()", ex);
        }

        return null;

    }

    
    /**
     * Get Food Groups - User
     * 
     * @param id 
     * @return
     */
    @SuppressWarnings("unchecked")
    public FoodGroup getUserFoodGroup(long id)
    {
        logInfo("getUserFoodGroup(id=" + id + ")");

        //ArrayList<FoodGroup> list = new ArrayList<FoodGroup>();

        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.FoodUserGroupH as pst order by pst.name");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                FoodUserGroupH eh = (FoodUserGroupH) it.next();
                return new FoodGroup(eh);
            }
        }
        catch (Exception ex)
        {
            logException("getUserFoodGroup()", ex);

            // log.error("Exception on getloadConfigData: " + ex.getMessage(),
            // ex);
        }

        return null;
    }
    
    
    
    /**
     * Get Food Groups - User
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<FoodGroup> getUserFoodGroups()
    {
        logInfo("getUserFoodGroups()");

        ArrayList<FoodGroup> list = new ArrayList<FoodGroup>();

        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.FoodUserGroupH as pst order by pst.name");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                FoodUserGroupH eh = (FoodUserGroupH) it.next();
                list.add(new FoodGroup(eh));
            }

            return list;
        }
        catch (Exception ex)
        {
            logException("getUserFoodGroups()", ex);

            // log.error("Exception on getloadConfigData: " + ex.getMessage(),
            // ex);
        }

        return list;
    }

    
    
    /**
     * Get Food Groups - User
     * 
     * @param parent_id 
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<FoodGroup> getUserFoodGroups(long parent_id)
    {
        logInfo("getUserFoodGroups()");

        ArrayList<FoodGroup> list = new ArrayList<FoodGroup>();

        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.FoodUserGroupH as pst where pst.parent_id=" + parent_id + " order by pst.name");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                FoodUserGroupH eh = (FoodUserGroupH) it.next();
                list.add(new FoodGroup(eh));
            }

            return list;
        }
        catch (Exception ex)
        {
            logException("getUserFoodGroups()", ex);

            // log.error("Exception on getloadConfigData: " + ex.getMessage(),
            // ex);
        }

        return list;
    }
    
    
    
    /**
     * Get Meal Groups
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<MealGroup> getMealGroups()
    {

        logInfo("getMealGroups()");

        ArrayList<MealGroup> list = new ArrayList<MealGroup>();

        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.MealGroupH as pst order by pst.name");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                MealGroupH eh = (MealGroupH) it.next();
                list.add(new MealGroup(eh));
            }

        }
        catch (Exception ex)
        {
            logException("getMealGroups()", ex);
        }

        return list;
    }

    
    /**
     * Get Meal Groups
     * 
     * @param parent_id 
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<MealGroup> getMealGroups(long parent_id)
    {

        logInfo("getMealGroups()");

        ArrayList<MealGroup> list = new ArrayList<MealGroup>();

        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.MealGroupH as pst where pst.parent_id=" + parent_id + " order by pst.name");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                MealGroupH eh = (MealGroupH) it.next();
                list.add(new MealGroup(eh));
            }

        }
        catch (Exception ex)
        {
            logException("getMealGroups()", ex);
        }

        return list;
    }
    

    /**
     * Get Food Descriptions - USDA
     * 
     * @param type 
     * @param parent_id 
     * 
     * @return
     */
    public ArrayList<FoodDescription> getFoodsByParent(int type, long parent_id)
    {
        logInfo("getFoodsByParent(type=" + type + ",parent_id=" + parent_id + ")");

        if (type==GGCTreeRoot.TREE_USDA_NUTRITION)
        {
            return this.getUSDAFoodDescriptionsByParent(parent_id);
        }
        else
        {
            return this.getUserFoodDescriptionsByParent(parent_id);
            
        }
        
    }
    
    
    
    /**
     * Get Food Descriptions - USDA
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<FoodDescription> getUSDAFoodDescriptions()
    {
        logInfo("getUSDAFoodDescriptions()");

        ArrayList<FoodDescription> list = new ArrayList<FoodDescription>();

        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.FoodDescriptionH as pst order by pst.name");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                FoodDescriptionH eh = (FoodDescriptionH) it.next();
                list.add(new FoodDescription(eh));
            }

            return list;
        }
        catch (Exception ex)
        {
            logException("getUSDAFoodDescriptions()", ex);
        }

        return list;
    }

    
    /**
     * Get Food Descriptions - USDA
     * 
     * @param parent_id 
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<FoodDescription> getUSDAFoodDescriptionsByParent(long parent_id)
    {
        logInfo("getUSDAFoodDescriptionsByParent(parent_id=" + parent_id + ")");

        ArrayList<FoodDescription> list = new ArrayList<FoodDescription>();

        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.FoodDescriptionH as pst where pst.group_id=" + parent_id + " order by pst.name");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                FoodDescriptionH eh = (FoodDescriptionH) it.next();
                list.add(new FoodDescription(eh));
            }

            return list;
        }
        catch (Exception ex)
        {
            logException("getUSDAFoodDescriptions()", ex);
        }

        return list;
    }
    
    
    
    /**
     * Get Food Descriptions - USDA
     * 
     * @param id 
     * @return
     */
    @SuppressWarnings("unchecked")
    public FoodDescription getUSDAFoodDescriptionById(long id)
    {
        logInfo("getUSDAFoodDescriptionById(id=" + id + ")");

        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.FoodDescriptionH as pst where pst.id=" + id);

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                FoodDescriptionH eh = (FoodDescriptionH) it.next();
                return new FoodDescription(eh);
            }

        }
        catch (Exception ex)
        {
            logException("getUSDAFoodDescriptions()", ex);
        }

        return null;
    }
    
    
    
    /**
     * Get Food by Id
     * 
     * @param type
     * @param id
     * @return
     */
    public FoodDescription getFoodDescriptionById(int type, long id)
    {
        if (type==GGCTreeRoot.TREE_USER_NUTRITION)
        {
            return getUserFoodDescriptionById(id);
        }
        else
        {
            return this.getUSDAFoodDescriptionById(id);
        }
    }

    
    /**
     * Get User Food Description By Id
     * 
     * @param id 
     * @return
     */
    @SuppressWarnings("unchecked")
    public FoodDescription getUserFoodDescriptionById(long id)
    {

        logInfo("getUserFoodDescriptionById(id=" + id +")");
        //ArrayList<FoodDescription> list = new ArrayList<FoodDescription>();

        try
        {

            Query q = getSession(2).createQuery(
                "select pst from ggc.core.db.hibernate.FoodUserDescriptionH as pst where pst.id=" + id);

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                FoodUserDescriptionH eh = (FoodUserDescriptionH) it.next();
                return new FoodDescription(eh);
            }

            // System.out.println("Loaded Food Descriptions: " + list.size());

        }
        catch (Exception ex)
        {
            logException("getUserFoodDescriptions()", ex);
        }

        return null;

    }
    

    
    /**
     * Get Food Descriptions - User
     * 
     * @param parent_id 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<FoodDescription> getUserFoodDescriptionsByParent(long parent_id)
    {

        logInfo("getUserFoodDescriptions()");
        ArrayList<FoodDescription> list = new ArrayList<FoodDescription>();

        try
        {

            Query q = getSession(2).createQuery(
                "select pst from ggc.core.db.hibernate.FoodUserDescriptionH as pst where pst.group_id=" + parent_id + " order by pst.name");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                FoodUserDescriptionH eh = (FoodUserDescriptionH) it.next();
                list.add(new FoodDescription(eh));
            }

            // System.out.println("Loaded Food Descriptions: " + list.size());

        }
        catch (Exception ex)
        {
            logException("getUserFoodDescriptions()", ex);
        }

        return list;

    }
    
    
    /**
     * Get Food Descriptions - User
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<FoodDescription> getUserFoodDescriptions()
    {

        logInfo("getUserFoodDescriptions()");
        ArrayList<FoodDescription> list = new ArrayList<FoodDescription>();

        try
        {

            Query q = getSession(2).createQuery(
                "select pst from ggc.core.db.hibernate.FoodUserDescriptionH as pst order by pst.group_id, pst.name");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                FoodUserDescriptionH eh = (FoodUserDescriptionH) it.next();
                list.add(new FoodDescription(eh));
            }

            // System.out.println("Loaded Food Descriptions: " + list.size());

        }
        catch (Exception ex)
        {
            logException("getUserFoodDescriptions()", ex);
        }

        return list;

    }

    /**
     * Get Meals Descriptions
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Meal> getMeals()
    {
        logInfo("getMeals()");

        ArrayList<Meal> list = new ArrayList<Meal>();

        try
        {

            Query q = getSession(2).createQuery(
                "select pst from ggc.core.db.hibernate.MealH as pst order by pst.group_id, pst.name");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                MealH eh = (MealH) it.next();
                // list.put("" + eh.getId(), new Meal(eh));
                list.add(new Meal(eh));
            }

            return list;
        }
        catch (Exception ex)
        {
            logException("getMeals()", ex);
        }

        return list;

    }

    
    
    /**
     * Get Meals Descriptions
     * 
     * @param type 
     * @param meal_id 
     * @return
     */
    @SuppressWarnings("unchecked")
    public Meal getMealById(int type, long meal_id)
    {
        logInfo("getMealById()");


        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.MealH as pst where pst.id=" + meal_id);

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                MealH eh = (MealH) it.next();
                // list.put("" + eh.getId(), new Meal(eh));
                return new Meal(eh);
            }

        }
        catch (Exception ex)
        {
            logException("getMealById()", ex);
        }

        return null;

    }
    

    /**
     * Get Meals Descriptions
     * 
     * @param parent_id 
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Meal> getMealsByParent(long parent_id)
    {
        logInfo("getMealsByParent()");

        ArrayList<Meal> list = new ArrayList<Meal>();

        try
        {

            Query q = getSession(2).createQuery(
                "select pst from ggc.core.db.hibernate.MealH as pst where pst.group_id=" + parent_id  + " order by pst.group_id, pst.name");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                MealH eh = (MealH) it.next();
                // list.put("" + eh.getId(), new Meal(eh));
                list.add(new Meal(eh));
            }

            return list;
        }
        catch (Exception ex)
        {
            logException("getMealsByParent()", ex);
        }

        return list;

    }
    
    
    /**
     * Get Meal
     * @param id 
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public Meal getMealById(long id)
    {
        logInfo("getMealById(id=" + id + ")");

        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.MealH as pst where pst.id=" + id);

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                MealH eh = (MealH) it.next();
                return new Meal(eh);
            }
        }
        catch (Exception ex)
        {
            logException("getMealById()", ex);
        }

        return null;

    }
    

    /**
     * Get Meal Group
     * @param id 
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public MealGroup getMealGroupById(long id)
    {
        logInfo("getMealGroupById(id=" + id + ")");

        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.MealGroupH as pst where pst.id=" + id);

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                MealGroupH eh = (MealGroupH) it.next();
                return new MealGroup(eh);
            }
        }
        catch (Exception ex)
        {
            logException("getMealGroupById()", ex);
        }

        return null;

    }
    
    
    
    @SuppressWarnings("unchecked")
    private void loadNutritionDefinitions()
    {

        logInfo("loadNutritionDefinitions()");

        try
        {

            int[] ids = { 4000, 4001, 4002, 4003, 4004, 4005 };
            String[] tags = { "GI", "GL", "GI_MIN", "GI_MAX", "GL_MIN", "GL_MAX" };

            Hashtable<String, NutritionDefinition> nut_defs = new Hashtable<String, NutritionDefinition>();
            ArrayList<SelectableInterface> nut_defs_lst = new ArrayList<SelectableInterface>();

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.NutritionDefinitionH as pst");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                NutritionDefinitionH eh = (NutritionDefinitionH) it.next();

                NutritionDefinition fnd = new NutritionDefinition(eh);
                nut_defs.put("" + fnd.getId(), fnd);
                nut_defs_lst.add(fnd);

            }

            // static nutrition - not in database yet
            // needs to be added to init

            // GI = 4000, GL = 4001, GI_MIN = 4002, GI_MAX = 4003,
            // GL_MIN = 4004, GL_MAX = 4005

            String[] units = { "gi", "gl", "gi", "gi", "gl", "gl" };
            String[] name = { "Glycemic Index", "Glycemic Load", "Glycemic Index (Min)", "Glycemic Index (Max)",
                             "Glycemic Load (Min)", "Glycemic Load (Max)" };

            for (int i = 0; i < ids.length; i++)
            {
                if (nut_defs.containsKey("" + ids[i]))
                    continue;

                NutritionDefinitionH eh = new NutritionDefinitionH(units[i], tags[i], name[i], "0", 1);
                eh.setId(ids[i]);

                NutritionDefinition ndef = new NutritionDefinition(eh);

                nut_defs.put("" + eh.getId(), ndef);
                nut_defs_lst.add(ndef);

            }

            this.cache_db.nutrition_defs = nut_defs;
            this.cache_db.nutrition_defs_list = nut_defs_lst;
        }
        catch (Exception ex)
        {
            logException("loadNutritionDefinitions()", ex);
        }

    }

    @SuppressWarnings("unchecked")
    private void loadHomeWeights()
    {

        logInfo("loadHomeWeights()");

        try
        {
            Hashtable<String, NutritionHomeWeightType> nut_hw = new Hashtable<String, NutritionHomeWeightType>();
            ArrayList<SelectableInterface> nut_hw_lst = new ArrayList<SelectableInterface>();

            Query q = getSession(2)
                    .createQuery("select pst from ggc.core.db.hibernate.NutritionHomeWeightTypeH as pst");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                NutritionHomeWeightTypeH eh = (NutritionHomeWeightTypeH) it.next();

                NutritionHomeWeightType fnd = new NutritionHomeWeightType(eh);
                nut_hw.put("" + fnd.getId(), fnd);
                nut_hw_lst.add(fnd);
            }

            int[] ids = { 4000, 4001, 4002, 4003, 4004, 4005, 4006, 4007, 4008, 4009, 4010, 4011, 4012, 4013, 4014,
                         4015, 4016, 4017, 4018, 4019 };
            String[] names = { "PORTION", "PORTION,_BIG", "PORTION,_MEDIUM", "PORTION,_SMALL", "SPOON", "SPOON,_BIG",
                              "STEAK,_SMALL", "STEAK,_SMALLER", "ITEMS", "SMALLER", "ITEM,_SMALLER",
                              "ITEM,_MEDIUM_SIZE", "ITEMS,_BIGGER", "SPOON,_SMALL", "ITEM,_SMALL", "CAN_(_100_G_)",
                              "HEADS", "ROOT,_MEDIUM", "SIZE", "FRUIT,_MEDIUM_SIZE" };

            // static (need to be done in init)

            for (int i = 0; i < ids.length; i++)
            {
                if (nut_hw.containsKey("" + ids[i]))
                    continue;

                NutritionHomeWeightTypeH eh = new NutritionHomeWeightTypeH(names[i], 1);
                eh.setId(ids[i]);

                NutritionHomeWeightType fnd = new NutritionHomeWeightType(eh);

                nut_hw.put("" + fnd.getId(), fnd);
                nut_hw_lst.add(fnd);
            }

            this.cache_db.homeweight_defs = nut_hw;
            this.cache_db.homeweight_defs_list = nut_hw_lst;

        }
        catch (Exception ex)
        {
            logException("loadHomeWeights()", ex);
        }

    }

    // *************************************************************
    // **** GGC Main Data ****
    // **** Comment: Were implemnted in DataBaseHandler ****
    // *************************************************************

    HbA1cValues hba1c_object = null;

    /**
     * Get HbA1c
     * 
     * @param day
     * @param force
     * @return
     */
    public HbA1cValues getHbA1c(GregorianCalendar day, boolean force)
    {
        if (this.hba1c_object == null)
        {
            hba1c_object = loadHbA1c(day);
        }
        else
        {
            if (force)
                hba1c_object = loadHbA1c(day);
        }

        return this.hba1c_object;

    }

    /**
     * Load HbA1c
     * 
     * @param day
     * @return
     */
    @SuppressWarnings("unchecked")
    public HbA1cValues loadHbA1c(GregorianCalendar day)
    {
        // System.out.println("Hibernate: getHbA1c() B1 Stat:" + m_loadStatus);

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        logInfo("getHbA1c()");

        HbA1cValues hbVal = new HbA1cValues();

        try
        {
            GregorianCalendar gc1 = (GregorianCalendar) day.clone();
            gc1.add(Calendar.MONTH, -3);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String eDay = sdf.format(day.getTime()) + "2359";
            String sDay = sdf.format(gc1.getTime()) + "0000";

            Query q = getSession().createQuery(
                "SELECT dv from " + "ggc.nutri.db.hibernate.DayValueH as dv " + "WHERE dv.bg > 0 AND dv.dt_info >=  "
                        + sDay + " AND dv.dt_info <= " + eDay + " ORDER BY dv.dt_info");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                DayValueH dv = (DayValueH) it.next();
                hbVal.addDayValueRow(new DailyValuesRow(dv));
            }

            hbVal.processDayValues();

            logDebug("getHbA1c()", "Readings: " + hbVal.getDayCount() + " " + hbVal.getReadings());
            
            //System.out.println("Avg BG: " + hbVal.getAvgBGForMethod3());

        }
        catch (Exception ex)
        {
            logException("getHbA1c()", ex);
        }

        return hbVal;
    }

    /**
     * Get Day Stats
     * 
     * @param day
     * @return
     */
    @SuppressWarnings("unchecked")
    public DailyValues getDayStats(GregorianCalendar day)
    {

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        logInfo("getDayStats()");

        DailyValues dV = new DailyValues();
        //dV.setDate(m_da.getDateTimeFromDateObject(day.getTime()) / 10000);

        dV.setDate(ATechDate.getATDateTimeFromGC(day, ATechDate.FORMAT_DATE_ONLY));
        
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String sDay = sdf.format(day.getTime());

            Query q = getSession().createQuery(
                "SELECT dv from " + "ggc.nutri.db.hibernate.DayValueH as dv " + "WHERE dv.dt_info >=  " + sDay
                        + "0000 AND dv.dt_info <= " + sDay + "2359 ORDER BY dv.dt_info");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                DayValueH dv = (DayValueH) it.next();

                DailyValuesRow dVR = new DailyValuesRow(dv);
                dV.addRow(dVR);
            }

        }
        catch (Exception ex)
        {
            logException("getDayStats()", ex);
        }

        return dV;
    }

    
    
    
    
    /**
     * Get Monthly Values
     * 
     * @param year
     * @param month
     * @return
     */
    @SuppressWarnings("unchecked")
    public MonthlyValues getMonthlyValues(int year, int month)
    {

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        logInfo("getMonthlyValues()");

        MonthlyValues mv = new MonthlyValues(year, month);

        try
        {
            String days = year + "" + m_da.getLeadingZero(month, 2);

            Query q = getSession().createQuery(
                "SELECT dv from " + "ggc.nutri.db.hibernate.DayValueH as dv " + "WHERE dv.dt_info >=  " + days
                        + "010000 AND dv.dt_info <= " + days + "312359 ORDER BY dv.dt_info");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                DayValueH dv = (DayValueH) it.next();

                DailyValuesRow dVR = new DailyValuesRow(dv);
                mv.addDayValueRow(dVR);
            }

        }
        catch (Exception ex)
        {
            logException("getMonthlyValues()", ex);
        }

        return mv;

    }

    
    /**
     * Get DayValuesData
     * 
     * @param from
     * @param till
     * @return
     */
    @SuppressWarnings("unchecked")
    public DayValuesData getDayValuesData(long from, long till)
    {

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        logInfo("getDayValuesData()");

        DayValuesData dvd = new DayValuesData(from, till);
        
        try
        {
            Query q = getSession().createQuery(
                "SELECT dv from " + "ggc.nutri.db.hibernate.DayValueH as dv " + "WHERE dv.dt_info >=  " + from
                        + "0000 AND dv.dt_info <= " + till + "2359 ORDER BY dv.dt_info");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                DayValueH dv = (DayValueH) it.next();

                DailyValuesRow dVR = new DailyValuesRow(dv);
                dvd.addDayValueRow(dVR);
            }

        }
        catch (Exception ex)
        {
            logException("getDayValuesData()", ex);
        }

        return dvd;

    }
    
    
    
    /**
     * Save Day Stats
     * 
     * @param dV
     */
    public void saveDayStats(DailyValues dV)
    {

        if (dV.hasChanged())
        {
            logInfo("saveDayStats()");

            logDebug("saveDayStats()", "Data has changed");

            Session sess = getSession();

            try
            {

                // deleted entries

                if (dV.hasDeletedItems())
                {
                    logDebug("saveDayStats()", "Removing deleted entry");

                    Transaction tx = sess.beginTransaction();

                    ArrayList<DayValueH> list = dV.getDeletedItems();
                    for (int i = 0; i < list.size(); i++)
                    {
                        DayValueH d = list.get(i);
                        sess.delete(d);
                        tx.commit();
                    }

                }

                // see if any of elements were changed or added

                for (int i = 0; i < dV.getRowCount(); i++)
                {
                    DailyValuesRow dwr = dV.getRow(i);

                    if (dwr.isNew())
                    {
                        logDebug("saveDayStats()", "Adding new entry");

                        Transaction tx = sess.beginTransaction();

                        DayValueH dvh = dwr.getHibernateObject();
                        Long l = (Long) sess.save(dvh);

                        dvh.setId(l.longValue());
                        tx.commit();
                    }
                    else if (dwr.hasChanged())
                    {
                        Transaction tx = sess.beginTransaction();

                        logDebug("saveDayStats()", "Changing entry");

                        DayValueH dvh = dwr.getHibernateObject();
                        sess.update(dvh);

                        tx.commit();
                    }

                } // for

            }
            catch (Exception ex)
            {
                logException("saveDayStats()", ex);
            }

        } // hasChanged
        else
        {
            logDebug("saveDayStats()", "No entries changed");
        }

    }

    /**
     * DateTime Exists
     * 
     * @param datetime
     * @return
     */
    public boolean dateTimeExists(long datetime)
    {
        if (m_loadStatus == DB_CONFIG_LOADED)
            return false;

        // if (db_debug)
        // System.out.println("Hibernate: dateTimeExists()");

        try
        {
            Query q = getSession().createQuery(
                "SELECT dv from " + "ggc.nutri.db.hibernate.DayValueH as dv " + "WHERE dv.dt_info = " + datetime);

            return (q.list().size() == 1);
        }
        catch (Exception ex)
        {
            logException("dateTimeExists()", ex);
            return false;
        }

    }

    
    
    
    
    
    
    
    // *************************************************************
    // **** NUTRITION DATA ****
    // *************************************************************

    /**
     * Get Nutrition Home Weights
     * 
     * @return
     */
    public ArrayList<SelectableInterface> getNutritionHomeWeights()
    {
        return this.cache_db.homeweight_defs_list;
    }

    /**
     * Get Nutrition Home Weight
     * 
     * @param id 
     * @return
     */
    public NutritionHomeWeightType getNutritionHomeWeight(long id)
    {
        return this.cache_db.homeweight_defs.get("" + id);
    }

    /**
     * Get Nutrition Definitions
     * 
     * @return
     */
    public ArrayList<SelectableInterface> getNutritionDefinitions()
    {
        return this.cache_db.nutrition_defs_list;
    }

    
    
    
    
    // *************************************************************
    // ****                      TOOLS Db METHODS               ****
    // *************************************************************
    
/*    
    <class name="ggc.nutri.db.hibernate.GlucoValueH" table="data_dayvalues"  >
    <id name="id" type="long" unsaved-value="0">
        <generator class="org.hibernate.id.AssignedIncrementGenerator"/>
    </id>            
    <property name="dt_info" type="long" not-null="true"/>    
    <property name="bg"  type="int" />
    <property name="person_id"  type="int" not-null="true" />
    <property name="changed" type="long" not-null="false" />    
</class>
*/
    
    
    
    
    
    
    // *************************************************************
    // **** U T I L S ****
    // *************************************************************
/*
    public String changeCase(String in)
    {

        StringTokenizer stok = new StringTokenizer(in, " ");

        boolean first = true;
        String out = "";

        while (stok.hasMoreTokens())
        {
            if (!first)
                out += " ";

            out += changeCaseWord(stok.nextToken());
            first = false;
        }

        return out;

    }

    public String changeCaseWord(String in)
    {

        String t = "";

        t = in.substring(0, 1).toUpperCase();
        t += in.substring(1).toLowerCase();

        return t;

    }

    public void showByte(byte[] in)
    {

        for (int i = 0; i < in.length; i++)
        {
            System.out.println((char) in[i] + " " + in[i]);
        }

    }
*/
    
    
    /**
     * Debug Out
     */
    public void debugOut(String source, Exception ex)
    {

        this.m_errorCode = 1;
        this.m_errorDesc = ex.getMessage();

        if (debug)
            System.out.println("  " + source + "::Exception: " + ex);

        if (debug)
            ex.printStackTrace();

    }


    /**
     * Get Application Db Name
     */
    @Override
    public String getApplicationDbName()
    {
        return "ggc";
    }


    /** 
     * Create Configuration
     */
    @Override
    public HibernateConfiguration createConfiguration()
    {
        // TODO Auto-generated method stub
        return this.hib_config;
    }

    
    protected void logException(String source, Exception ex)
    {
        log.error(source + "::Exception: " + ex.getMessage(), ex);
    }

    protected void logDebug(String source, String action)
    {
        log.debug(source + " - " + action);
    }

    protected void logInfo(String source, String action)
    {
        log.info(source + " - " + action);
    }

    protected void logInfo(String source)
    {
        log.info(source + " - Process");
    }
    

    
    /**
     * Get Session
     */
    public Session getSession()
    {
        return getSession(1);
    }

    /**
     * Get Session
     * 
     * @param session_nr 
     * @return 
     */
    public Session getSession(int session_nr)
    {
        return this.hib_config.getSession(session_nr);
        /*
        if (session_nr == 1)
        {
            m_session.clear();
            return m_session;
        }
        else
        {
            m_session_2.clear();
            return m_session_2;
        }*/
    }
    
    
}
