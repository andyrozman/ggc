package ggc.pump.data.bre;

import java.util.ArrayList;

public class BREDataCollection //extends ArrayList<BasalRateEstimatorData> 
{

    ArrayList<BREData> bg;
    ArrayList<BREData> basal_old;
    ArrayList<BREData> basal_new;
    
    ArrayList<RatioData> ratios;
    ArrayList<RatioData> ratios_graph;
    
    public BREDataCollection()
    {
        bg = new ArrayList<BREData>();
        basal_old = new ArrayList<BREData>();
        basal_new = new ArrayList<BREData>();
        ratios = new ArrayList<RatioData>();
        ratios_graph = new ArrayList<RatioData>();
    }

    public void add(int type, BREData data)
    {
        switch (type)
        {
            case BREData.BRE_DATA_BASAL_NEW:
            {
                this.basal_new.add(data);
            } break;
            
            case BREData.BRE_DATA_BASAL_OLD:
            {
                this.basal_old.add(data);
            } break;
                
            case BREData.BRE_DATA_BG:
            {
                this.bg.add(data);
            } break;
                
                
            default:
                break;
        }
        
        
    }
    
    public void add(int type, int time, float value)
    {
        BREData data = new BREData(time, value, type);
        add(type, data);
    }

    
    public void add(int time_i, float ratio_ch_insulin, float ratio_bg_insulin, float ratio_ch_bg)
    {
        RatioData data = new RatioData(time_i, ratio_ch_insulin, ratio_bg_insulin, ratio_ch_bg);
        this.ratios.add(data);
    }
    

    public ArrayList<BREData> getDataByType(int type)
    {
        switch (type)
        {
            case BREData.BRE_DATA_BASAL_NEW:
                return this.basal_new;
            
            case BREData.BRE_DATA_BASAL_OLD:
                return this.basal_old;
                
            default:
            case BREData.BRE_DATA_BG:
                return this.bg;
                
        }
        
    }

    public ArrayList<RatioData> getRatiosCollection(int type)
    {
        switch (type)
        {
            case BREData.BRE_DATA_BASAL_RATIO:
                return this.ratios;
            
            case BREData.BRE_DATA_BASAL_RATIO_GRAPH:
            default:
                return this.ratios_graph;
                
        }
        
    }
    
    
    
    public void processRatios()
    {
        
        for(int i=0; i<this.ratios.size(); i++)
        {
            RatioData rd = this.ratios.get(i);
            
            if (i==(this.ratios.size()-1))
            {
                rd.time_end = 2359;
            }
            else
            {
                RatioData rd2 = this.ratios.get(i+1);
                rd.time_end = rd2.time_start - 1;
            }
        }
        
    }

    
    public void processOldBasals()
    {
        processBasals(this.basal_old);
        /*
        for(int i=0; i<this.basal_old.size(); i++)
        {
            BREData rd = this.basal_old.get(i);
            
            if (i==(this.basal_old.size()-1))
            {
                rd.time_end = 2359;
            }
            else
            {
                BREData rd2 = this.basal_old.get(i+1);
                rd.time_end = rd2.time - 1;
            }
        }*/
        
    }

    public void processNewBasals()
    {
        processBasals(this.basal_new);
    }
    

    public void processBasals(ArrayList<BREData> list)
    {
        
        for(int i=0; i<list.size(); i++)
        {
            BREData rd = list.get(i);
            
            if (i==0)
            {
                rd.time = 0;
            }
            
            if (i==(list.size()-1))
            {
                rd.time_end = 2359;
            }
            else
            {
                BREData rd2 = list.get(i+1);
                rd.time_end = rd2.time - 1;
            }
        }
    }
    
    
    
    
}

