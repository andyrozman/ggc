package ggc.pump.data.dto;

import java.util.ArrayList;
import java.util.Iterator;

import ggc.plugin.output.OutputWriter;

/**
 * Created by andy on 10.07.17.
 */
public class BasalPatternDTO extends ArrayList<BasalPatternEntryDTO>
{

    private int patternNumber;
    private String name;


    public BasalPatternDTO(String name)
    {
        super();
        this.name = name;
    }


    public BasalPatternDTO(int patternNumber)
    {
        super();
        this.patternNumber = patternNumber;
        this.name = "" + patternNumber; // this is preset, it might be changed
                                        // later
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public float getTotalAmount()
    {
        Iterator<BasalPatternEntryDTO> iterator = this.iterator();

        float sum = 0.0f;

        while (iterator.hasNext())
        {
            sum += iterator.next().getRate();
        }

        return sum;
    }


    public void writeToOutputWriter(OutputWriter outputWriter)
    {
        // FIXME
    }
}
