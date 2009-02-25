package ggc.pump.gui.bre;

import ggc.pump.data.bre.BREDataCollection;
import ggc.pump.data.graph.bre.BREGraphsAbstract;

import java.util.Hashtable;

public class BasalRateEstimatorAlgorithm 
{
    //GraphViewBasalRateEstimator m_gv;
    Hashtable<String, BREGraphsAbstract> m_graphs;
    
    
    public BasalRateEstimatorAlgorithm(Hashtable<String, BREGraphsAbstract> gvs)
    {
        this.m_graphs = gvs;
    }

    
    public void setData(BREDataCollection data_coll)
    {
        // do algorithm data
        //m_gv.setData(data_coll);
        m_graphs.get("" + BasalRateEstimator.GRAPH_OLD_RATE).setData(data_coll);
        
        m_graphs.get("" + BasalRateEstimator.GRAPH_RATIO).setData(data_coll);
        m_graphs.get("" + BasalRateEstimator.GRAPH_BASALS).setData(data_coll);
        
    }
    
    
    
    
}

