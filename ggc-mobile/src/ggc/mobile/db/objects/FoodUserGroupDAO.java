package ggc.mobile.db.objects;

import java.sql.Connection;
import java.sql.ResultSet;

import com.atech.mobile.db.objects.DatabaseAccessObject;

public class FoodUserGroupDAO extends DatabaseAccessObject 
{

    public static final int CHANGED_ID = 1;
    public static final int CHANGED_NAME = 2;
    public static final int CHANGED_NAME_I18N = 4;
    public static final int CHANGED_DESCRIPTION = 8;
    public static final int CHANGED_PARENT_ID = 16;
    public static final int CHANGED_CHANGED = 32;

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String name_i18n;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private long parent_id;

    /** nullable persistent field */
    private long changed;

    /** full constructor */
    public FoodUserGroupDAO(String name, String name_i18n, String description, long parent_id, long changed) {
        this.name = name;
        this.name_i18n = name_i18n;
        this.description = description;
        this.parent_id = parent_id;
        this.changed = changed;
    }

    /** default constructor */
    public FoodUserGroupDAO() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = this.setChangedValueLong(this.id, id, FoodUserGroupDAO.CHANGED_ID);
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = this.setChangedValueString(this.name, name, FoodUserGroupDAO.CHANGED_NAME);
    }

    public String getName_i18n()
    {
        return this.name_i18n;
    }

    public void setName_i18n(String name_i18n)
    {
        this.name_i18n = this.setChangedValueString(this.name_i18n, name_i18n, FoodUserGroupDAO.CHANGED_NAME_I18N);
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = this.setChangedValueString(this.description, description, FoodUserGroupDAO.CHANGED_DESCRIPTION);
    }

    public long getParent_id() 
    {
        return this.parent_id;
    }

    public void setParent_id(long parent_id) 
    {
        this.parent_id = this.setChangedValueLong(this.parent_id, parent_id, FoodUserGroupDAO.CHANGED_PARENT_ID);
    }

    public long getChanged() 
    {
        return this.changed;
    }

    public void setChanged(long changed) 
    {
        this.changed = this.setChangedValueLong(this.changed, changed, FoodUserGroupDAO.CHANGED_CHANGED);
    }

    public String toString() 
    {
        return "FoodUserGroupDAO";
    }

    
    /**
     * dbAdd - add entry to database
     * 
     * @param conn
     * @return
     * @throws Exception
     */
    @Override
    public boolean dbAdd(Connection conn) throws Exception
    {
        return this.executeDb(conn, 
            "insert into nutrition_user_food_group (id, name, name_i18n, description, parent_id, changed) values (" +
            this.getNextId(conn) + ", " +
            this.getStringForDb(this.name) + "," +
            this.getStringForDb(this.name_i18n) + "," +
            this.getStringForDb(this.description) + "," +
            this.parent_id + "," +
            System.currentTimeMillis() + 
            ")");
    }

    
    /**
     * dbCreate - create table in database
     * 
     * @param conn
     * @return
     * @throws Exception
     */     
    @Override
    public boolean dbCreate(Connection conn) throws Exception
    {
        return this.executeDb(conn, 
            "CREATE TABLE nutrition_user_food_group (" +
            "id bigint NOT NULL PRIMARY KEY, " +
            "name varchar(255)," +
            "name_i18n varchar(255), " +
            "description varchar(1000)," +
            "parent_id bigint, " +
            "changed bigint" +
            ")"); 
    }

    /**
     * dbEdit - edit entry to database
     * 
     * @param conn
     * @return
     * @throws Exception
     */ 
    @Override
    public boolean dbEdit(Connection conn) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    
    /**
     * dbGet - get data from database entry
     * 
     * @param conn
     * @throws Exception
     */     
    @Override
    public void dbGet(ResultSet rs) throws Exception
    {
        this.id = rs.getLong("id");
        this.name = rs.getString("name");
        this.name_i18n = rs.getString("name_i18n");
        this.description = rs.getString("description");
        this.parent_id = rs.getLong("parent_id");
        this.changed = rs.getLong("changed");
    }

    /**
     * dbHasChildren - if entry has children
     * 
     * @param conn
     * @return
     * @throws Exception
     */ 
    @Override
    public boolean dbHasChildren(Connection conn) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /** 
     * getIdColumnName - get name of column containing id
     * @return
     */     
    @Override
    public String getIdColumnName()
    {
        return "id";
    }

    /**
     * getObjectName - name of object
     * 
     * @return
     */
    @Override
    public String getObjectName()
    {
        return "FoodUserGroup";
    }

    
    /**
     * getObjectUniqueId - get unique id of this object
     * 
     * @return id as String
     */
    @Override
    public String getObjectUniqueId()
    {
        return "" + this.getId();
    }

    /**
     * getTableName - get name of table
     * 
     * @return get name of table
     */
    @Override
    public String getTableName()
    {
        return "nutrition_user_food_group";
    }
    

}
