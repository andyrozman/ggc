package ggc.plugin.graph.data;

public class GraphValue
{

    public long datetime = 0L;

    public int date = 0;
    public int time = 0;

    public int value_type = 0;

    public float value = 0.0f;

    public String extended_value = null;

    public GraphValue(long dt, int type, float val)
    {
        this(dt, type, val, null);
    }

    public GraphValue(long dt, int type, String val)
    {
        this(dt, type, val, null);
    }

    public GraphValue(long dt, int type, float val, String ext)
    {
        this.setDateTime(dt);
        this.setType(type);
        this.setValue(val);
        this.setExtendedValue(ext);
    }

    public GraphValue(long dt, int type, String val, String ext)
    {
        this.setDateTime(dt);
        this.setType(type);
        this.setValue(val);
        this.setExtendedValue(ext);
    }

    public void setDateTime(long dt)
    {
        this.datetime = dt;

        // 20101010 235900

        date = (int) dt / 1000000;
        time = (int) (dt - date * 1000000);

    }

    public void setType(int type)
    {
        this.value_type = type;
    }

    public void setValue(float val)
    {
        this.value = val;
    }

    public void setValue(String val)
    {
        try
        {
            this.value = Float.parseFloat(val);
        }
        catch (Exception ex)
        {
            this.value = 0.0f;
        }
    }

    public void setExtendedValue(String extended)
    {
        this.extended_value = extended;
    }

}
