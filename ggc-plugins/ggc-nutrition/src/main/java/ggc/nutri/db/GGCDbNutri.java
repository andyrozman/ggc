package ggc.nutri.db;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.HibernateConfiguration;
import com.atech.db.hibernate.HibernateDb;
import com.atech.db.hibernate.HibernateObject;
import com.atech.graphics.dialogs.selector.SelectableInterface;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.food.*;
import ggc.core.util.DataAccess;
import ggc.nutri.data.GGCTreeRoot;
import ggc.nutri.data.GGCTreeRootDyn;
import ggc.nutri.db.datalayer.*;

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
 *  Filename:     GGCDbNutri
 *  Description:  Class for working with database (Hibernate) in Nutrition Plugin
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class GGCDbNutri extends HibernateDb
{

    private static final Logger LOG = LoggerFactory.getLogger(GGCDbNutri.class);

    HibernateConfiguration hib_config;
    private boolean debug = true;
    GGCDbCache cache_db = null;


    /**
     * Constructor 
     * 
     * @param db 
     */
    public GGCDbNutri(GGCDb db)
    {
        super(DataAccess.getInstance(), db);

        this.hib_config = db.getHibernateConfiguration();

        // System.out.println("hib_Config:" + this.hib_config);
        // this.hib_config.getS

        cache_db = new GGCDbCache(this);

    }


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
     * Load Nutrition Db Base
     */
    public void loadNutritionDbBase()
    {
        this.loadNutritionDefinitions();
        this.loadHomeWeights();

        this.loadNutritionDb1();
        this.loadNutritionDb2();
        this.loadMealsDb();
    }


    /**
     * Load Nutrition Db 1 - USDA
     */
    public void loadNutritionDb1()
    {
        // tree root, now in static data
        cache_db.tree_roots.put("1", new GGCTreeRootDyn(GGCTreeRoot.TREE_USDA_NUTRITION, this));

        // dataAccess.m_nutrition_treeroot = new GGCTreeRoot(1, this);

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
    public List<FoodGroup> getFoodGroups(int type, long parent_id)
    {
        if (type == GGCTreeRoot.TREE_USER_NUTRITION)
            return this.getUserFoodGroups(parent_id);
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
        if (type == GGCTreeRoot.TREE_USER_NUTRITION)
            return this.getUserFoodGroup(id);
        else
            return this.getUSDAFoodGroup(id);
    }


    /**
     * Get Food Groups - USDA
     * 
     * @return
     */
    public List<FoodGroup> getUSDAFoodGroups()
    {
        List<FoodGroupH> fgList = getHibernateObjectListByParameter(null, null, Order.asc("name"), FoodGroupH.class, 2);

        return getDAOGroupList(fgList, FoodGroup.class);
    }


    public <T extends HibernateObject> T getHibernateObjectByParameter(String parameterName, Long parameterValue,
            Order orderBy, Class<T> clazz, Integer sessionId)
    {
        LOG.info("get " + clazz.getSimpleName() + " (id=" + parameterValue + ")");

        try
        {
            Criteria criteria = this.getSession(sessionId).createCriteria(clazz);
            criteria.add(Restrictions.eq(parameterName, parameterValue));

            List list = criteria.list();

            if (list.size() > 0)
            {
                return (T) list.get(0);
            }

        }
        catch (Exception ex)
        {
            LOG.info(
                "get " + clazz.getSimpleName() + " (id=" + parameterValue + ") problem. Exception: " + ex.getMessage(),
                ex);
        }

        return (T) null;
    }


    public <T extends HibernateObject> List<T> getHibernateObjectListByParameter(String parameterName,
            Long parameterValue, Order orderBy, Class<T> clazz, Integer sessionId)
    {
        LOG.info("get {} - {}", clazz.getSimpleName(), getParameterDebugString(parameterName, parameterValue));

        List<T> outList = new ArrayList<T>();

        try
        {
            Criteria criteria = this.getSession(sessionId).createCriteria(clazz);

            if (parameterName != null)
                criteria.add(Restrictions.eq(parameterName, parameterValue));

            if (orderBy != null)
            {
                criteria.addOrder(orderBy);
            }

            List list = criteria.list();

            for (Object obj : list.toArray())
            {
                outList.add((T) obj);
            }

        }
        catch (Exception ex)
        {
            LOG.error("get {} - {} problem. Exception: {}", clazz.getSimpleName(),
                getParameterDebugString(parameterName, parameterValue), ex.getMessage(), ex);
        }

        return outList;
    }


    private String getParameterDebugString(String parameterName, Long parameterValue)
    {
        return parameterName != null ? String.format(" (%s=%s)", parameterName, parameterValue) : "";
    }


    /**
     * Get USDA Food Group
     * 
     * @param id 
     * @return
     */
    public FoodGroup getUSDAFoodGroup(long id)
    {
        FoodGroupH fg = getHibernateObjectByParameter("id", id, Order.asc("name"), FoodGroupH.class, 2);

        return fg != null ? new FoodGroup(fg) : null;
    }


    /**
     * Get Food Groups - User
     * 
     * @param id 
     * @return
     */
    public FoodGroup getUserFoodGroup(long id)
    {
        FoodUserGroupH fg = getHibernateObjectByParameter("id", id, Order.asc("name"), FoodUserGroupH.class, 2);

        return fg != null ? new FoodGroup(fg) : null;
    }


    /**
     * Get Food Groups - User
     * 
     * @return
     */
    public List<FoodGroup> getUserFoodGroups()
    {
        List<FoodUserGroupH> fgList = getHibernateObjectListByParameter(null, null, Order.asc("name"),
            FoodUserGroupH.class, 2);

        return getDAOGroupList(fgList, FoodGroup.class);
    }


    private <T extends DAOObject> List<T> getDAOGroupList(List<? extends HibernateObject> fgList, Class<T> clazz)
    {
        List<T> outList = new ArrayList<T>();

        for (HibernateObject hibernateObject : fgList)
        {
            if (hibernateObject instanceof FoodGroupH)
            {
                outList.add((T) new FoodGroup((FoodGroupH) hibernateObject));
            }
            else if (hibernateObject instanceof FoodUserGroupH)
            {
                outList.add((T) new FoodGroup((FoodUserGroupH) hibernateObject));
            }
            else if (hibernateObject instanceof MealGroupH)
            {
                outList.add((T) new MealGroup((MealGroupH) hibernateObject));
            }
            else if (hibernateObject instanceof FoodDescriptionH)
            {
                outList.add((T) new FoodDescription((FoodDescriptionH) hibernateObject));
            }

        }

        return outList;
    }


    /**
     * Get Food Groups - User
     * 
     * @param parentId parent Group
     * 
     * @return
     */
    public List<FoodGroup> getUserFoodGroups(long parentId)
    {
        List<FoodUserGroupH> fgList = getHibernateObjectListByParameter("parentId", parentId, Order.asc("name"),
            FoodUserGroupH.class, 2);

        return getDAOGroupList(fgList, FoodGroup.class);
    }


    /**
     * Get Meal Groups
     * 
     * @return
     */
    public List<MealGroup> getMealGroups()
    {
        List<MealGroupH> fgList = getHibernateObjectListByParameter(null, null, Order.asc("name"), MealGroupH.class, 2);

        return getDAOGroupList(fgList, MealGroup.class);
    }


    /**
     * Get Meal Groups
     * 
     * @param parentId parentId of groups
     * 
     * @return List of meal groups
     */
    public List<MealGroup> getMealGroups(long parentId)
    {
        List<MealGroupH> fgList = getHibernateObjectListByParameter("parent_id", parentId, Order.asc("name"),
            MealGroupH.class, 2);

        return getDAOGroupList(fgList, MealGroup.class);
    }


    /**
     * Get Food Descriptions - USDA
     * 
     * @param type 
     * @param parent_id 
     * 
     * @return
     */
    public List<FoodDescription> getFoodsByParent(int type, long parent_id)
    {
        logInfo("getFoodsByParent(type=" + type + ",parentId=" + parent_id + ")");

        if (type == GGCTreeRoot.TREE_USDA_NUTRITION)
            return this.getUSDAFoodDescriptionsByParent(parent_id);
        else
            return this.getUserFoodDescriptionsByParent(parent_id);

    }


    /**
     * Get Food Descriptions - USDA
     * 
     * @return
     */
    public List<FoodDescription> getUSDAFoodDescriptions()
    {
        List<FoodDescriptionH> fgList = getHibernateObjectListByParameter(null, null, Order.asc("name"),
            FoodDescriptionH.class, 2);

        return getDAOGroupList(fgList, FoodDescription.class);
    }


    /**
     * Get Food Descriptions By Parent - USDA
     * 
     * @param parentId
     * 
     * @return
     */
    public List<FoodDescription> getUSDAFoodDescriptionsByParent(long parentId)
    {
        List<FoodDescriptionH> fgList = getHibernateObjectListByParameter("groupId", parentId, Order.asc("name"),
            FoodDescriptionH.class, 2);

        return getDAOGroupList(fgList, FoodDescription.class);
    }


    /**
     * Get Food Descriptions - USDA
     * 
     * @param id 
     * @return
     */
    public FoodDescription getUSDAFoodDescriptionById(long id)
    {
        logInfo("getUSDAFoodDescriptionById(id=" + id + ")");

        try
        {

            Query q = getSession(2).createQuery(
                "select pst from ggc.core.db.hibernate.food.FoodDescriptionH as pst where pst.id=" + id);

            Iterator<?> it = q.iterate();

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
        if (type == GGCTreeRoot.TREE_USER_NUTRITION)
            return getUserFoodDescriptionById(id);
        else
            return this.getUSDAFoodDescriptionById(id);
    }


    /**
     * Get User Food Description By Id
     * 
     * @param id 
     * @return
     */
    public FoodDescription getUserFoodDescriptionById(long id)
    {

        logInfo("getUserFoodDescriptionById(id=" + id + ")");
        // ArrayList<FoodDescription> list = new ArrayList<FoodDescription>();

        try
        {

            Query q = getSession(2).createQuery(
                "select pst from ggc.core.db.hibernate.food.FoodUserDescriptionH as pst where pst.id=" + id);

            Iterator<?> it = q.iterate();

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
    public ArrayList<FoodDescription> getUserFoodDescriptionsByParent(long parent_id)
    {

        logInfo("getUserFoodDescriptions()");
        ArrayList<FoodDescription> list = new ArrayList<FoodDescription>();

        try
        {

            Query q = getSession(2).createQuery(
                "select pst from ggc.core.db.hibernate.food.FoodUserDescriptionH as pst where pst.groupId=" + parent_id
                        + " order by pst.name");

            Iterator<?> it = q.iterate();

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
    public List<FoodDescription> getUserFoodDescriptions()
    {

        logInfo("getUserFoodDescriptions()");
        List<FoodDescription> list = new ArrayList<FoodDescription>();

        try
        {

            Query q = getSession(2).createQuery(
                "select pst from ggc.core.db.hibernate.food.FoodUserDescriptionH as pst order by pst.groupId, pst.name");

            Iterator<?> it = q.iterate();

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
    public List<Meal> getMeals()
    {
        logInfo("getMeals()");

        List<Meal> list = new ArrayList<Meal>();

        try
        {

            Query q = getSession(2).createQuery(
                "select pst from ggc.core.db.hibernate.food.MealH as pst order by pst.groupId, pst.name");

            Iterator<?> it = q.iterate();

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
    public Meal getMealById(int type, long meal_id)
    {
        MealH fg = getHibernateObjectByParameter("id", meal_id, null, MealH.class, 2);

        return fg != null ? new Meal(fg) : null;
    }


    /**
     * Get Meals Descriptions
     * 
     * @param parent_id 
     * 
     * @return
     */
    public List<Meal> getMealsByParent(long parent_id)
    {
        logInfo("getMealsByParent()");

        List<Meal> list = new ArrayList<Meal>();

        try
        {

            Query q = getSession(2)
                    .createQuery("select pst from ggc.core.db.hibernate.food.MealH as pst where pst.groupId="
                            + parent_id + " order by pst.groupId, pst.name");

            Iterator<?> it = q.iterate();

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
    public Meal getMealById(long id)
    {
        MealH fg = getHibernateObjectByParameter("id", id, null, MealH.class, 2);

        return fg != null ? new Meal(fg) : null;
    }


    /**
     * Get Meal Group
     * @param id 
     * 
     * @return
     */
    public MealGroup getMealGroupById(long id)
    {
        MealGroupH fg = getHibernateObjectByParameter("id", id, null, MealGroupH.class, 2);

        return fg != null ? new MealGroup(fg) : null;
    }


    private void loadNutritionDefinitions()
    {

        logInfo("loadNutritionDefinitions()");

        try
        {

            int[] ids = { 4000, 4001, 4002, 4003, 4004, 4005 };
            String[] tags = { "GI", "GL", "GI_MIN", "GI_MAX", "GL_MIN", "GL_MAX" };

            Hashtable<String, NutritionDefinition> nut_defs = new Hashtable<String, NutritionDefinition>();
            ArrayList<SelectableInterface> nut_defs_lst = new ArrayList<SelectableInterface>();

            Query q = getSession(2)
                    .createQuery("select pst from ggc.core.db.hibernate.food.NutritionDefinitionH as pst");

            Iterator<?> it = q.iterate();

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
                {
                    continue;
                }

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


    private void loadHomeWeights()
    {

        logInfo("loadHomeWeights()");

        try
        {
            Hashtable<String, NutritionHomeWeightType> nut_hw = new Hashtable<String, NutritionHomeWeightType>();
            ArrayList<SelectableInterface> nut_hw_lst = new ArrayList<SelectableInterface>();

            Query q = getSession(2)
                    .createQuery("select pst from ggc.core.db.hibernate.food.NutritionHomeWeightTypeH as pst");

            Iterator<?> it = q.iterate();

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
                {
                    continue;
                }

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
    // **** NUTRITION DATA ****
    // *************************************************************

    /**
     * Get Nutrition Home Weights
     * 
     * @return
     */
    public List<SelectableInterface> getNutritionHomeWeights()
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
    public List<SelectableInterface> getNutritionDefinitions()
    {
        return this.cache_db.nutrition_defs_list;
    }


    /**
     * Debug Out
     */
    @Override
    public void debugOut(String source, Exception ex)
    {

        this.m_errorCode = 1;
        this.m_errorDesc = ex.getMessage();

        if (debug)
        {
            System.out.println("  " + source + "::Exception: " + ex);
        }

        if (debug)
        {
            ex.printStackTrace();
        }

    }


    /**
     * Get Application Db Name
     */
    @Override
    public String getApplicationDbName()
    {
        return "ggc";
    }


    @Override
    protected void initDataTransformer()
    {

    }


    @Override
    protected <E extends HibernateObject> void specialFilteringOfCriteria(Class<E> clazz, Criteria criteria)
    {

    }


    @Override
    protected <E extends HibernateObject> boolean isTypeCached(Class<E> clazz)
    {
        return false;
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
        LOG.error(source + "::Exception: " + ex.getMessage(), ex);
    }


    protected void logDebug(String source, String action)
    {
        LOG.debug(source + " - " + action);
    }


    protected void logInfo(String source, String action)
    {
        LOG.info(source + " - " + action);
    }


    protected void logInfo(String source)
    {
        LOG.info(source + " - Process");
    }


    /**
     * Get Session
     */
    @Override
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
    }

    // 1457

}
