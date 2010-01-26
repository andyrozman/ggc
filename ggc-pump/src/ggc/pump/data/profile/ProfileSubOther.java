package ggc.pump.data.profile;

public class ProfileSubOther extends ProfileSubEntry
{
    
    public String profile_id = "";

    
    public static final int EVENT_PATTERN_CHANGED = 1;
    
    
    /**
     * The time_start.
     */
    public long time_event;
    

    public int event_type;

    
    
    @Override
    public int getType()
    {
        return ProfileSubEntry.PROFILE_SUB_EVENT;
    }
    

    
    
}
