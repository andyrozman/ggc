package ggc.core.enums;

/**
 * Created by andy on 14.04.17.
 */
public enum GGCFunctionality
{
    DoctorsAppointments(false, "0.9"), // 0.9
    UserManagement(false, "0.10"), // 0.9
    Inventory(false, "0.10"), //0.9
    Minimed_Contour(false, "0.11"), //
    Minimed_Contour24(false, "0.11"), //
    ; //

    private boolean enabled = false;
    private String plannedVersion;
    private static boolean developerVersion = false;


    GGCFunctionality(boolean enabled, String plannedVersion)
    {
        this.enabled = enabled;
        this.plannedVersion = plannedVersion;
    }


    public static void setDeveloperVersion(boolean devVersion)
    {
        developerVersion = devVersion;
    }

    public boolean isEnabled()
    {
        if (developerVersion)
            return true;
        else
            return enabled;
    }

    public String getPlannedVersion() {
        return plannedVersion;
    }
}
