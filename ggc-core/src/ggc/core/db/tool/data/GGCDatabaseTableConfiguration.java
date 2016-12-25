package ggc.core.db.tool.data;

import com.atech.db.hibernate.tool.data.DatabaseTableConfiguration;

import ggc.core.db.hibernate.food.*;

public enum GGCDatabaseTableConfiguration implements DatabaseTableConfiguration
{
    // core
    DayValueH(), // DailyValue - Pen/Injection data
    SettingsH(), // Settings
    ColorSchemeH(), // Color Scheme for BG Data

    // pump
    PumpDataH(), //
    PumpDataExtendedH(), //
    PumpProfileH(), //

    // cgms
    CGMSDataH(), //
    CGMSDataExtendedH(), //

    // nutrition
    FoodGroupH(1, FoodGroupH.class, //
            "id, name, name_i18n, description"), //
    FoodDescriptionH(1, FoodDescriptionH.class, //
            "id, group_id, name, name_i18n, refuse, nutritions, home_weights"), //
    FoodUserGroupH(1, FoodUserGroupH.class, //
            "id; name; name_i18n; description; parent_id; changed"), //
    FoodUserDescriptionH(1, FoodUserDescriptionH.class, //
            "id; name; name_i18n; group_id; refuse; description; home_weights; nutritions; changed"), //
    MealH(1, MealH.class, //
            "id; name; name_i18n; group_id; description; parts; nutritions; extended; comment; changed"), //
    MealGroupH(1, MealGroupH.class, //
            "id; name; name_i18n; description; parent_id; changed"), //
    NutritionDefinitionH(1, NutritionDefinitionH.class, //
            "id; weight_unit; tag; name; decimal_places; static_entry"), //
    NutritionHomeWeightTypeH(1, NutritionHomeWeightTypeH.class, //
            "id; name; static_entry"), //

    // stocks
    StocksXYZ(), //

    // doctors

    // inet

    ;

    // import ggc.core.db.hibernate.food.FoodUserDescriptionH;
    // import ggc.core.db.hibernate.food.FoodUserGroupH;
    // import ggc.core.db.hibernate.food.MealGroupH;
    // import ggc.core.db.hibernate.food.MealH;

    String tableName;
    String fullClassName;

    int tableVersion;
    Class clazz;
    String columns;


    @Deprecated
    GGCDatabaseTableConfiguration()
    {
    }


    GGCDatabaseTableConfiguration(int tableVersion, Class clazz, String columns)
    {
        this(null, tableVersion, clazz, columns);
    }


    GGCDatabaseTableConfiguration(String tableName, int tableVersion, Class clazz, String columns)
    {
        this.tableName = tableName;
        this.fullClassName = clazz.getName();

        this.tableVersion = tableVersion;
        this.clazz = clazz;
        this.columns = columns;
    }


    // public String getSqlForExport()
    // {
    // return null;
    // }

    public String getObjectName()
    {
        return this.name();
    }


    public Class getObjectClass()
    {
        return this.clazz;
    }


    public String getTableName()
    {
        return this.tableName;
    }


    public String getObjectFullName()
    {
        return fullClassName;
    }


    public int getTableVersion()
    {
        return this.tableVersion;
    }


    public String getColumns()
    {
        return this.columns;
    }

}
