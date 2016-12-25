package ggc.pump.data.bre;

// TODO: Auto-generated Javadoc
/**
 * The Class BasalEstimationData.
 */
public class BasalEstimationData
{

    /**
     * The time.
     */
    public int time;

    /**
     * The basal_value.
     */
    public double basal_value;

    /**
     * The insulin_value.
     */
    public double insulin_value;

    /** 
     * toString
     */
    @Override
    public String toString()
    {
        return "BasalEstimationData [time=" + time + ",basal=" + basal_value + ",insulin=" + insulin_value;
    }
}
