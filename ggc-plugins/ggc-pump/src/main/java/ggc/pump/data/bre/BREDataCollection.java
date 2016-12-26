package ggc.pump.data.bre;

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class BREDataCollection.
 */
public class BREDataCollection // extends ArrayList<BasalRateEstimatorData>
{

    /**
     * The bg.
     */
    ArrayList<BREData> bg;

    /**
     * The basal_old.
     */
    ArrayList<BREData> basal_old;

    /**
     * The basal_new.
     */
    ArrayList<BREData> basal_new;

    /**
     * The ratios.
     */
    ArrayList<RatioData> ratios;

    /**
     * The ratios_graph.
     */
    ArrayList<RatioData> ratios_graph;

    /**
     * The basal_estimation.
     */
    ArrayList<BasalEstimationData> basal_estimation;


    /**
     * Instantiates a new bRE data collection.
     */
    public BREDataCollection()
    {
        bg = new ArrayList<BREData>();
        basal_old = new ArrayList<BREData>();
        basal_new = new ArrayList<BREData>();
        ratios = new ArrayList<RatioData>();
        ratios_graph = new ArrayList<RatioData>();
        basal_estimation = new ArrayList<BasalEstimationData>();
    }


    /**
     * Adds the BREData
     * 
     * @param type the type
     * @param data the data
     */
    public void add(int type, BREData data)
    {
        switch (type)
        {
            case BREData.BRE_DATA_BASAL_NEW:
                {
                    this.basal_new.add(data);
                }
                break;

            case BREData.BRE_DATA_BASAL_OLD:
                {
                    this.basal_old.add(data);
                }
                break;

            case BREData.BRE_DATA_BG:
                {
                    this.bg.add(data);
                }
                break;

            default:
                break;
        }

    }


    /**
     * Adds the BREData
     * 
     * @param type the type
     * @param time the time
     * @param value the value
     */
    public void add(int type, int time, float value)
    {
        BREData data = new BREData(time, value, type);
        add(type, data);
    }


    /**
     * Adds the BREData
     * 
     * @param time_i the time_i
     * @param ratio_ch_insulin the ratio_ch_insulin
     * @param ratio_bg_insulin the ratio_bg_insulin
     * @param ratio_ch_bg the ratio_ch_bg
     */
    public void add(int time_i, float ratio_ch_insulin, float ratio_bg_insulin, float ratio_ch_bg)
    {
        RatioData data = new RatioData(time_i, ratio_ch_insulin, ratio_bg_insulin, ratio_ch_bg);
        this.ratios.add(data);
    }


    /**
     * Gets the data by type
     * 
     * @param type the type
     * 
     * @return the data by type
     */
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


    /**
     * Gets the ratios collection
     * 
     * @param type the type
     * 
     * @return the ratios collection
     */
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


    /**
     * Process ratios
     */
    public void processRatios()
    {

        for (int i = 0; i < this.ratios.size(); i++)
        {
            RatioData rd = this.ratios.get(i);

            if (i == this.ratios.size() - 1)
            {
                rd.time_end = 2359;
            }
            else
            {
                RatioData rd2 = this.ratios.get(i + 1);
                rd.time_end = rd2.time_start - 1;
            }
        }

    }


    /**
     * Process old basals
     */
    public void processOldBasals()
    {
        processBasals(this.basal_old);
        /*
         * for(int i=0; i<this.basal_old.size(); i++)
         * {
         * BREData rd = this.basal_old.get(i);
         * if (i==(this.basal_old.size()-1))
         * {
         * rd.time_end = 2359;
         * }
         * else
         * {
         * BREData rd2 = this.basal_old.get(i+1);
         * rd.time_end = rd2.time - 1;
         * }
         * }
         */

    }


    /**
     * Process new basals
     */
    public void processNewBasals()
    {
        processBasals(this.basal_new);
    }


    /**
     * Process basals
     * 
     * @param list the list
     */
    public void processBasals(ArrayList<BREData> list)
    {

        for (int i = 0; i < list.size(); i++)
        {
            BREData rd = list.get(i);

            if (i == 0)
            {
                rd.time = 0;
            }

            if (i == list.size() - 1)
            {
                rd.time_end = 2359;
            }
            else
            {
                BREData rd2 = list.get(i + 1);
                rd.time_end = rd2.time - 1;
            }
        }
    }


    /**
     * Sets the basal estimation data
     * 
     * @param data the new basal estimation data
     */
    public void setBasalEstimationData(ArrayList<BasalEstimationData> data)
    {
        this.basal_estimation = data;
    }


    /**
     * Gets the basal estimation data
     * 
     * @return the basal estimation data
     */
    public ArrayList<BasalEstimationData> getBasalEstimationData()
    {
        return this.basal_estimation;
    }

}
