package ggc.pump.gui.bre;

import java.util.ArrayList;

public class BasalRateEstimatorDataCollection //extends ArrayList<BasalRateEstimatorData> 
{

    ArrayList<BasalRateEstimatorData> bg;
    ArrayList<BasalRateEstimatorData> basal_old;
    ArrayList<BasalRateEstimatorData> basal_new;
    
    
    
    public BasalRateEstimatorDataCollection()
    {
        bg = new ArrayList<BasalRateEstimatorData>();
        basal_old = new ArrayList<BasalRateEstimatorData>();
        basal_new = new ArrayList<BasalRateEstimatorData>();
    }

    public void add(int type, BasalRateEstimatorData data)
    {
        switch (type)
        {
            case BasalRateEstimatorData.DATA_TYPE_BASAL_NEW:
            {
                this.basal_new.add(data);
            } break;
            
            case BasalRateEstimatorData.DATA_TYPE_BASAL_OLD:
            {
                this.basal_old.add(data);
            } break;
                
            case BasalRateEstimatorData.DATA_TYPE_BG:
            {
                this.bg.add(data);
            } break;
                
                
            default:
                break;
        }
        
        
    }
    
    public void add(int type, int time, float value)
    {
        BasalRateEstimatorData data = new BasalRateEstimatorData(time, value, type);
        add(type, data);
    }


    public ArrayList<BasalRateEstimatorData> getDataByType(int type)
    {
        switch (type)
        {
            case BasalRateEstimatorData.DATA_TYPE_BASAL_NEW:
                return this.basal_new;
            
            case BasalRateEstimatorData.DATA_TYPE_BASAL_OLD:
                return this.basal_old;
                
            default:
            case BasalRateEstimatorData.DATA_TYPE_BG:
                return this.bg;
                
        }
        
    }
    
}

