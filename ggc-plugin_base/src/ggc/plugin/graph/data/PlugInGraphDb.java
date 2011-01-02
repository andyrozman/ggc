package ggc.plugin.graph.data;


import java.util.GregorianCalendar;

public interface PlugInGraphDb
{

    /**
     * getGraphData - Returns data for selected period. Filter will determine what data will be
     *      returned. It is advisable to never filter data (it will work much faster, when
     *      selecting different things to draw). When changing period, new data will have to be 
     *      retrieved.
     *       
     * @param gc_from begining of period 
     * @param gc_to end of period
     * @param filter how we should filter data. 0 usually means get all data. 
     * @return
     */
    public GraphValuesCollection getGraphData(GregorianCalendar gc_from, GregorianCalendar gc_to, int filter);
    
    
}
