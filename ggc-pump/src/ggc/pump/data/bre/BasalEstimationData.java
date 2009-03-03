package ggc.pump.data.bre;


public class BasalEstimationData
    {
        public int time;
        
        public double basal_value;
        
        public double insulin_value;
        
        public String toString()
        {
            return "BasalEstimationData [time=" + time + ",basal=" + basal_value + ",insulin=" + insulin_value;
        }
    }