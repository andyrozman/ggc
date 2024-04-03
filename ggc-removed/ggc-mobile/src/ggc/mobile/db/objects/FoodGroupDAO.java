package ggc.mobile.db.objects;

import java.sql.Connection;
import java.sql.ResultSet;

import com.atech.mobile.db.objects.DatabaseAccessObject;

public class FoodGroupDAO extends DatabaseAccessObject 
{
    
    public static final int CHANGED_ID = 1;
    public static final int CHANGED_NAME = 2;
    public static final int CHANGED_NAME_I18N = 4;
    public static final int CHANGED_DESCRIPTION = 8;

    /*  identifier field */
    private long id;

    /*  nullable persistent field */
    private String name;

    /*  nullable persistent field */
    private String name_i18n;

    /*  nullable persistent field */
    private String description;

    /*  full constructor */
    public FoodGroupDAO(String name, String name_i18n, String description)
    {
        this.name = name;
        this.name_i18n = name_i18n;
        this.description = description;
    }

    /*  default constructor */
    public FoodGroupDAO()
    {
    }

    public long getId()
    {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = this.setChangedValueLong(this.id, id, FoodGroupDAO.CHANGED_ID);
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = this.setChangedValueString(this.name, name, FoodGroupDAO.CHANGED_NAME);
    }

    public String getName_i18n()
    {
        return this.name_i18n;
    }

    public void setName_i18n(String name_i18n)
    {
        this.name_i18n = this.setChangedValueString(this.name_i18n, name_i18n, FoodGroupDAO.CHANGED_NAME_I18N);
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = this.setChangedValueString(this.description, description, FoodGroupDAO.CHANGED_DESCRIPTION);
    }

    public String toString()
    {
        return "FoodGroupDAO";
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
            "insert into nutrition_usda_food_group (id, name, name_i18n, description) values (" +
            this.getNextId(conn) + ", " +
            this.getStringForDb(this.name) + "," +
            this.getStringForDb(this.name_i18n) + "," +
            this.getStringForDb(this.description) + ")");
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
            "CREATE TABLE nutrition_usda_food_group (" +
            "id bigint NOT NULL PRIMARY KEY, " +
            "name varchar(255)," +
            "name_i18n varchar(255), " +
            "description varchar(1000)" +
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
        return "FoodGroup";
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
        return "nutrition_usda_food_group";
    }

}
