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
    DontFeelRight("MARKER_DONT_FEEL_RIGHT"), //
    Sick("MARKER_SICK"), //
    Stress("MARKER_STRESS"), //
    Activity("MARKER_ACTIVITY"), //
    ControlResult("MARKER_CONTROL_RESULT"), //
    AfterFoodWithTime("MARKER_AFTER_FOOD"), //
    ;

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
