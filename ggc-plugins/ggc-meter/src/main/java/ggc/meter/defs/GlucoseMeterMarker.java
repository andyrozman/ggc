package ggc.meter.defs;

/**
 * Created by andy on 12/11/16.
 */
public enum GlucoseMeterMarker
{

    LowGlucose("MARKER_LO"), //
    HighGlucose("MARKER_HI"), //
    PreMeal("MARKER_PRE_MEAL"), //
    PostMeal("MARKER_POST_MEAL"), //
    PostMealTime("MARKER_POST_MEAL_TIME"), //
    DontFeelRight("MARKER_DONT_FEEL_RIGHT"), //
    Sick("MARKER_SICK"), //
    Stress("MARKER_STRESS"), //
    Activity("MARKER_ACTIVITY"), //
    ControlResult("MARKER_CONTROL_RESULT"), //
    AfterFoodWithTime("MARKER_AFTER_FOOD"), //
    MealLevel1("MARKER_MEAL_LEVEL_1"), //
    MealLevel2("MARKER_MEAL_LEVEL_2"), //
    MealLevel3("MARKER_MEAL_LEVEL_3"), //
    Hypoglycemia("MARKER_HYPO"), //
    BedTime("MARKER_BED_TIME");

    private String description;


    GlucoseMeterMarker(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return description;
    }
}
