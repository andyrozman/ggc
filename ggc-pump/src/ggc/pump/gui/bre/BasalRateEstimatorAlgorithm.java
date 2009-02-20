package ggc.pump.gui.bre;

import ggc.pump.data.graph.GraphViewBasalRateEstimator;

public class BasalRateEstimatorAlgorithm 
{
    GraphViewBasalRateEstimator m_gv;
    
    public BasalRateEstimatorAlgorithm(GraphViewBasalRateEstimator gv)
    {
        this.m_gv = gv;
    }

    
    public void setData(BasalRateEstimatorDataCollection data_coll)
    {
        // do algorithm data
        m_gv.setData(data_coll);
    }
    
    
    
    
}

