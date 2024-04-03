package ggc.core.db.hibernate;

import com.atech.data.mng.DataDefinitionTableInterface;

/**
 * Created by andy on 25.02.17.
 */
public enum GGCDataDefinitionTable implements DataDefinitionTableInterface
{
    // // core
    // DayValueH(), // DailyValue - Pen/Injection data
    // SettingsH(), // Settings
    // ColorSchemeH(), // Color Scheme for BG Data
    //
    // // pump
    // PumpDataH(), //
    // PumpDataExtendedH(), //
    // PumpProfileH(), //
    //
    // // cgms
    // CGMSDataH(), //
    // CGMSDataExtendedH(), //
    //
    // // nutrition
    // FoodGroupH(ggc.core.db.hibernate.food.FoodGroupH.class), //
    // FoodDescriptionH(ggc.core.db.hibernate.food.FoodDescriptionH.class), //
    // FoodUserGroupH(1, ggc.core.db.hibernate.food.FoodUserGroupH.class, //
    // "id; name; name_i18n; description; parent_id; changed"), //
    // FoodUserDescriptionH(1,
    // ggc.core.db.hibernate.food.FoodUserDescriptionH.class, //
    // "id; name; name_i18n; group_id; refuse; description; home_weights;
    // nutritions; changed"), //
    // MealH(1, ggc.core.db.hibernate.food.MealH.class, //
    // "id; name; name_i18n; group_id; description; parts; nutritions; extended;
    // comment; changed"), //
    // MealGroupH(1, MealGroupH.class, //
    // "id; name; name_i18n; description; parent_id; changed"), //
    // NutritionDefinitionH(1, NutritionDefinitionH.class, //
    // "id; weight_unit; tag; name; decimal_places; static_entry"), //
    // NutritionHomeWeightTypeH(1, NutritionHomeWeightTypeH.class, //
    // "id; name; static_entry"), //
    //
    // // stocks
    // StocksXYZ(), //

    // doctors

    // inet

    ;

    GGCDataDefinitionTable(Class clazz)
    {

    }


    public Class getObjectClass()
    {
        return null;
    }
}
