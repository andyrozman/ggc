package ggc.pump.data.dto;

import com.atech.utils.data.ATechDate;

/**
 * Created by andy on 10.07.17.
 */
public class BasalPatternEntryDTO
{

    private ATechDate starTime; // AtechDateTime - TIME, so 1300, would mean
                                // 13:00
    private Integer endTime; // this might or might not be used, in most cases
                             // we record
    // start time only
    Integer duration; // also mostly not used

    private float rate;


    public BasalPatternEntryDTO()
    {
    }


    public BasalPatternEntryDTO(ATechDate starTime, float rate)
    {
        this.starTime = starTime;
        this.rate = rate;
    }


    public float getRate()
    {
        return rate;
    }


    public void setRate(float rate)
    {
        this.rate = rate;
    }


    public ATechDate getStarTime()
    {
        return starTime;
    }


    public void setStarTime(ATechDate starTime)
    {
        this.starTime = starTime;
    }
}
