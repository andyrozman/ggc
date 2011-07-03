package ggc.pump.device.minimed.file;

import ggc.plugin.device.impl.minimed.file.MinimedCareLink;
import ggc.plugin.device.impl.minimed.file.MinimedCareLinkData;
import ggc.pump.data.profile.Profile;
import ggc.pump.data.profile.ProfileSubPattern;
import ggc.pump.util.DataAccessPump;

import java.util.Hashtable;

public class MinimedCareLinkPumpProfile extends MinimedCareLinkData //extends ArrayList<MinimedCareLinkPumpData>
{
    private String date_time_full = null;
    private long dt_full = 0L;
    
    private String pattern_name = null;
    private int num_profiles = 0;
    private Hashtable<String, ProfileSubPattern> entries_index = new Hashtable<String, ProfileSubPattern>();  
    
    
    public MinimedCareLinkPumpProfile(String date_time, MinimedCareLink mcl)
    {
        super(mcl);
        this.date_time_full = date_time;
    }
    
    
//    ChangeBasalProfilePattern = "PATTERN_NAME=pattern a, NUM_PROFILES=20, ACTION_REQUESTOR=pump"
//        ChangeBasalProfile = "PATTERN_DATUM=879228396, PROFILE_INDEX=0, RATE=0, 2, START_TIME=0"
    
    
    public void add(MinimedCareLinkPumpData entry)
    {
        if (entry.raw_type.equals("ChangeBasalProfilePattern"))
        {
            this.pattern_name = this.getDataBetween(entry.raw_values, "PATTERN_NAME=", "NUM_PROFILES=");
            this.num_profiles = Integer.parseInt(getDataNumber(entry.raw_values, "NUM_PROFILES=", "ACTION_REQUESTOR="));

            this.dt_full = entry.getAtechDateLong();
            
            System.out.println("Pattern name=" + this.pattern_name + ", Num profiles=" + this.num_profiles);
        }
        else if (entry.raw_type.equals("ChangeBasalProfile"))
        {
            //this.num_profiles = Integer.parseInt(getDataNumber(entry.raw_values, "NUM_PROFILES=", "ACTION_REQUESTOR="));
            
            // ChangeBasalProfile = "PATTERN_DATUM=879228396, PROFILE_INDEX=0, RATE=0, 2, START_TIME=0"
            String index = this.getDataBetween(entry.raw_values, "PROFILE_INDEX=", "RATE=");
            String rate = this.getDataNumber(entry.raw_values, "RATE=", "START_TIME=");
            String start_time = this.getDataDuration(entry.raw_values + ",END", "START_TIME=", "\",END");
            
            ProfileSubPattern psp = new ProfileSubPattern();
            psp.amount = Float.parseFloat(rate);
            psp.time_start = Integer.parseInt(DataAccessPump.replaceExpression(start_time, ":", ""));
            psp.profile_id = this.pattern_name;
            
            this.entries_index.put(index, psp);
            
            
            
            //psp.time_start = 
            //psp.amount =
            
            
            System.out.print("Index=" + index + ",Rate=" + rate + ",Start time=" + start_time + "\n");
            
            
        }
        else
        {
            System.out.println("Error: Unknown type: " + entry.raw_type);
        }
            
    }


    @Override
    public void processData()
    {
        for(int i=0; i<this.num_profiles-1; i++)
        {
            this.entries_index.get("" + i).time_end = this.entries_index.get("" + (i+1)).time_start;
        }
        
        this.entries_index.get("" + this.num_profiles).dt_end = 2359;
        
    }
    
    public Profile getProfile()
    {
        Profile p = new Profile();
        //p.
        p.profile_id = this.pattern_name;
        p.profile_active_from = 0L;
        
        p.pattern_entries.addAll(this.entries_index.values());
        
        return p;
    }
    
    
    
}
