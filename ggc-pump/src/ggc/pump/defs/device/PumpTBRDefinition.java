package ggc.pump.defs.device;

/**
 * Created by andy on 21.10.15.
 */
public enum PumpTBRDefinition
{

    AnimasTBR("TYPE=Percent;STEP=10;MIN=-100;MAX=200%", -100f, 200f, 10f, null, null, null),

    RocheTBR("TYPE=Percent;STEP=10;MIN=0;MAX=200", 0f, 200f, 10f, null, null, null),

    // FIXME
    DanaTBR("dd");

    String definition;
    Float percentMin;
    Float percentMax;
    Float percentStep;
    Float valueMin;
    Float valueMax;
    Float valueStep;


    PumpTBRDefinition(String definition)
    {
    }


    PumpTBRDefinition(String definition, Float percentMin, Float percentMax, Float percentStep, Float valueMin, Float valueMax,
                      Float valueStep)
    {
        this.setDefinition(definition);
        this.setPercentMin(percentMin);
        this.setPercentMax(percentMax);
        this.setPercentStep(percentStep);
        this.setValueMin(valueMin);
        this.setValueMax(valueMax);
        this.setValueStep(valueStep);
    }

    public String getDefinition()
    {
        return definition;
    }

    public void setDefinition(String definition)
    {
        this.definition = definition;
    }

    public Float getPercentMin()
    {
        return percentMin;
    }

    public void setPercentMin(Float percentMin)
    {
        this.percentMin = percentMin;
    }

    public Float getPercentMax()
    {
        return percentMax;
    }

    public void setPercentMax(Float percentMax)
    {
        this.percentMax = percentMax;
    }

    public Float getPercentStep()
    {
        return percentStep;
    }

    public void setPercentStep(Float percentStep)
    {
        this.percentStep = percentStep;
    }

    public Float getValueMin()
    {
        return valueMin;
    }

    public void setValueMin(Float valueMin)
    {
        this.valueMin = valueMin;
    }

    public Float getValueMax()
    {
        return valueMax;
    }

    public void setValueMax(Float valueMax)
    {
        this.valueMax = valueMax;
    }

    public Float getValueStep()
    {
        return valueStep;
    }

    public void setValueStep(Float valueStep)
    {
        this.valueStep = valueStep;
    }
}
