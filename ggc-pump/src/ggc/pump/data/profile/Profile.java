package ggc.pump.data.profile;

import java.util.ArrayList;



public class Profile 
{
    
    public String profile_id = "";
    
    // if we are resolving
    public long date_at = 0L;
    
    public ArrayList<ProfileSubPattern> pattern_entries = new ArrayList<ProfileSubPattern>();
    
    public ArrayList<ProfileSubOther> other_entries = new ArrayList<ProfileSubOther>();
    
    
    
    public Profile()
    {
    }
    

    public void add(ProfileSubEntry entry)
    {
        if (entry.getType()==ProfileSubEntry.PROFILE_SUB_EVENT)
            this.add((ProfileSubOther)entry);
        else
            this.add((ProfileSubPattern)entry);
    }
    
    
    public void add(ProfileSubOther entry)
    {
        this.other_entries.add(entry);
    }
    

    public void add(ProfileSubPattern entry)
    {
        this.pattern_entries.add(entry);
    }
    
    
}
