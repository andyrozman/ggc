package ggc.connect.software.web.nightscout.data.defs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andy on 4/8/18.
 */
public enum TreatmentEventType {

    CorrectionBolus("Correction Bolus"), //
    BolusWizard("Bolus Wizard"), //
    SensorChange("Sensor Change"), //
    SensorStart("Sensor Start"), //
    TempBasal("Temp Basal"), //
    InsulinChange("Insulin Change"), //
    SiteChange("Site Change"), //
    PumpBatteryChange("Pump Battery Change"), //
    Note("Note"), //
    Announcement("Announcement"), //
    ProfileSwitch("Profile Switch"), //
    MealBolus("Meal Bolus"), //
    Unknown(""), //

    ;


    private String description;
    private static Map<String,TreatmentEventType> treatmentEventTypeMap;

    static
    {
        treatmentEventTypeMap = new HashMap<String, TreatmentEventType>();

        for (TreatmentEventType treatmentEventType : values()) {
            treatmentEventTypeMap.put(treatmentEventType.getDescription(), treatmentEventType);
        }
    }



    TreatmentEventType(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return description;
    }

    public static TreatmentEventType getByDescription(String eventType)
    {
        if (treatmentEventTypeMap.containsKey(eventType))
            return treatmentEventTypeMap.get(eventType);
        else
            return Unknown;
    }
}