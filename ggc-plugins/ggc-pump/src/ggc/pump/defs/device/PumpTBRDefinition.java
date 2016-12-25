package ggc.pump.defs.device;

/**
 * Created by andy on 21.10.15.
 */
public enum PumpTBRDefinition
{

    AnimasTBR("TYPE=Percent;STEP=10;MIN=-100;MAX=200%;TIME_START=30;TIME_END=1440;TIME_STEP=30", //
            -100f, 200f, 10f, //
            null, null, null, //
            30, 24 * 60, 30), //

    RocheTBR("TYPE=Percent;STEP=10;MIN=0;MAX=200", //
            0f, 200f, 10f, //
            null, null, null, //
            15, 24 * 60, 15), // FIXME time

    MinimedTBR("", //
            0f, 100f, 10f, // FIXME start, end, step
            0f, 20f, 0.1f, // FIXME start, end, step
            30, 24 * 60, 30), //

    DanaTBR("TYPE=Percent;STEP=10;MIN=0;MAX=200;TIME_START=30;TIME_END=720;TIME_STEP=15", //
            0f, 200f, 10f, //
            null, null, null, //
            30, 12 * 60, 30), //

    AsanteTBR("TYPE=Percent;STEP=10;MIN=0;MAX=500;TIME_START=15;TIME_END=1440;TIME_STEP=15", //
            0f, 500f, 10f, //
            null, null, null, //
            15, 24 * 60, 15), //

    TandemTBR("TYPE=Percent;STEP=1;MIN=0;MAX=250;TIME_START=15;TIME_END=4320;TIME_STEP=1", //
            0f, 250f, 1f, //
            null, null, null, //
            15, 72 * 60, 1), //
            ;

    String definition;
    Float percentMin;
    Float percentMax;
    Float percentStep;
    Float valueMin;
    Float valueMax;
    Float valueStep;
    Integer timeMinStart;
    Integer timeMinStop;
    Integer timeMinStep;


    @Deprecated
    PumpTBRDefinition(String definition)
    {
    }


    @Deprecated
    PumpTBRDefinition(String definition, Float percentMin, Float percentMax, Float percentStep, Float valueMin,
            Float valueMax, Float valueStep)
    {
        this(definition, percentMin, percentMax, percentStep, valueMin, valueMax, valueStep, null, null, null);
    }


    /**
     *
     * @param definition definition as string (won't be used anymore)
     * @param percentMin percent min value (ex.: 0%)
     * @param percentMax percent min value (ex.: 200%)
     * @param percentStep percent step (ex.: 10%)
     * @param valueMin value min value (ex.: 0)
     * @param valueMax value max value (ex.: 5)
     * @param valueStep value step (ex.: 0.1)
     * @param timeMinStart time interval start (in minutes) (ex.: 30 min)
     * @param timeMinStop time interval stop (in minutes) (ex.: 24 * 60 min)
     * @param timeMinStep time interval step (ex.: 30 minutes)
     */
    PumpTBRDefinition(String definition, Float percentMin, Float percentMax, Float percentStep, Float valueMin,
            Float valueMax, Float valueStep, Integer timeMinStart, Integer timeMinStop, Integer timeMinStep)
    {
        this.definition = definition;
        this.percentMin = percentMin;
        this.percentMax = percentMax;
        this.percentStep = percentStep;
        this.valueMin = valueMin;
        this.valueMax = valueMax;
        this.valueStep = valueStep;
        this.timeMinStart = timeMinStart;
        this.timeMinStop = timeMinStop;
        this.timeMinStep = timeMinStep;
    }


    public String getDefinition()
    {
        return definition;
    }


    public Float getPercentMin()
    {
        return percentMin;
    }


    public Float getPercentMax()
    {
        return percentMax;
    }


    public Float getPercentStep()
    {
        return percentStep;
    }


    public Float getValueMin()
    {
        return valueMin;
    }


    public Float getValueMax()
    {
        return valueMax;
    }


    public Float getValueStep()
    {
        return valueStep;
    }

}
