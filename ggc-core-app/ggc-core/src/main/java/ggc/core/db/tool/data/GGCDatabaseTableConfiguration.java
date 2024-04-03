package ggc.core.db.tool.data;

import com.atech.db.hibernate.HibernateObject;
import com.atech.db.hibernate.tool.data.DatabaseImportStrategy;
import com.atech.db.hibernate.tool.data.DatabaseTableConfiguration;

import ggc.core.db.hibernate.doc.DoctorAppointmentH;
import ggc.core.db.hibernate.doc.DoctorH;
import ggc.core.db.hibernate.doc.DoctorTypeH;
import ggc.core.db.hibernate.food.*;
import ggc.core.db.hibernate.settings.ColorSchemeH;
import ggc.core.db.hibernate.settings.SettingsH;

public enum GGCDatabaseTableConfiguration implements DatabaseTableConfiguration
{
    // core
    // FIXME
    DayValueH(), // DailyValue - Pen/Injection data
    SettingsH(SettingsH.class, null, 1, //
            "id; key; value; type; description; person_id", //
            "SETTINGS", DatabaseImportStrategy.Clean), // Settings
    ColorSchemeH(ColorSchemeH.class, null, 1, //
            "id; name; custom_type; color_bg; color_bg_avg; color_bg_low; color_bg_high; color_bg_target; color_ins; color_ins1; color_ins2; color_ins_perbu; color_ch", //
            "COLOR_SCHEMES", DatabaseImportStrategy.Clean), // Color Scheme for BG Data

    // pump
    // FIXME
    PumpDataH(), //
    // FIXME
    PumpDataExtendedH(), //
    // FIXME
    PumpProfileH(), //

    // cgms
    // FIXME
    CGMSDataH(), //
    // FIXME
    CGMSDataExtendedH(), //

    // nutrition
    FoodGroupH(FoodGroupH.class, 1, //
            "id; name; name_i18n; description"), //
    FoodDescriptionH(FoodDescriptionH.class, 1, //
            "id; group_id; name; name_i18n; refuse; nutritions; home_weights"), //
    FoodUserGroupH(FoodUserGroupH.class, null, 1, //
            "id; name; name_i18n; description; parent_id; changed", "USER_FOOD_GROUPS", DatabaseImportStrategy.Clean), //
    FoodUserDescriptionH(FoodUserDescriptionH.class, 1, //
            "id; name; name_i18n; group_id; refuse; description; home_weights; nutritions; changed"), //
    MealH(MealH.class, 1, //
            "id; name; name_i18n; group_id; description; parts; nutritions; extended; comment; changed"), //
    MealGroupH(MealGroupH.class, 1, //
            "id; name; name_i18n; description; parent_id; changed"), //
    NutritionDefinitionH(NutritionDefinitionH.class, 1, //
            "id; weight_unit; tag; name; decimal_places; static_entry"), //
    NutritionHomeWeightTypeH(NutritionHomeWeightTypeH.class, 1, //
            "id; name; static_entry"), //

    // stocks
    StocksXYZ(), //

    // doctors
    DoctorH(DoctorH.class, null, 1, //
            "id; name; doctor_type_id; address; phone; phone_gsm; email; working_time; active_from; active_till; person_id; extended; comment",
            "DOCTORS", DatabaseImportStrategy.Clean), //

    DoctorTypeH(DoctorTypeH.class, null, 1, //
            "id; name; predefined", //
            "DOCTOR_TYPES", DatabaseImportStrategy.Clean), //

    DoctorAppointmentH(DoctorAppointmentH.class, null, 1, //
            "id; doctor; dt_apoint; apoint_text; person_id; extended; comment", //
            "APPOINTMENTS", DatabaseImportStrategy.Clean), //

    // inet

    // InventoryH(InventoryH.class, null, 1, "", //
    // "INVENTORY", DatabaseImportStrategy.Clean);

    ;

    //

    String tableName;
    int tableVersion;
    Class<? extends HibernateObject> clazz;
    String columns;
    String backupTargetName;
    DatabaseImportStrategy databaseImportStrategy;


    @Deprecated
    GGCDatabaseTableConfiguration()
    {
    }


    @Deprecated
    GGCDatabaseTableConfiguration(Class<? extends HibernateObject> clazz, int tableVersion, String columns)
    {
    }


    // GGCDatabaseTableConfiguration(Class<? extends HibernateObject> clazz,
    // String tableName, //
    // int tableVersion, String columns, String backupTargetName, boolean
    // doWeNeedCleanup)
    // {
    // this(clazz, tableName, tableVersion, columns, backupTargetName,
    // doWeNeedCleanup, false);
    // }

    GGCDatabaseTableConfiguration(Class<? extends HibernateObject> clazz, String tableName, //
            int tableVersion, String columns, String backupTargetName, DatabaseImportStrategy databaseImportStrategy)
    {
        this.tableName = tableName;
        this.tableVersion = tableVersion;
        this.clazz = clazz;
        this.columns = columns;
        this.backupTargetName = backupTargetName;
        this.databaseImportStrategy = databaseImportStrategy;
    }


    public String getObjectName()
    {
        return this.clazz.getSimpleName();
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
        return this.clazz.getName();
    }


    public int getTableVersion()
    {
        return this.tableVersion;
    }


    public String getColumns()
    {
        return this.columns;
    }


    public String getBackupTargetName()
    {
        return this.backupTargetName;
    }


    public DatabaseImportStrategy getDatabaseImportStrategy()
    {
        return this.databaseImportStrategy;
    }

}
