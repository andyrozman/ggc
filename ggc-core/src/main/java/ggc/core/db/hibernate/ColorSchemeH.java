package ggc.core.db.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ColorSchemeH implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -80774589554863692L;

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private int custom_type;

    /** nullable persistent field */
    private int color_bg;

    /** nullable persistent field */
    private int color_bg_avg;

    /** nullable persistent field */
    private int color_bg_low;

    /** nullable persistent field */
    private int color_bg_high;

    /** nullable persistent field */
    private int color_bg_target;

    /** nullable persistent field */
    private int color_ins;

    /** nullable persistent field */
    private int color_ins1;

    /** nullable persistent field */
    private int color_ins2;

    /** nullable persistent field */
    private int color_ins_perbu;

    /** nullable persistent field */
    private int color_ch;

    /** full constructor 
     * @param name 
     * @param custom_type 
     * @param color_bg 
     * @param color_bg_avg 
     * @param color_bg_low 
     * @param color_bg_high 
     * @param color_bg_target 
     * @param color_ins 
     * @param color_ins1 
     * @param color_ins2 
     * @param color_ins_perbu 
     * @param color_ch */
    public ColorSchemeH(String name, int custom_type, int color_bg, int color_bg_avg, int color_bg_low,
            int color_bg_high, int color_bg_target, int color_ins, int color_ins1, int color_ins2, int color_ins_perbu,
            int color_ch)
    {
        this.name = name;
        this.custom_type = custom_type;
        this.color_bg = color_bg;
        this.color_bg_avg = color_bg_avg;
        this.color_bg_low = color_bg_low;
        this.color_bg_high = color_bg_high;
        this.color_bg_target = color_bg_target;
        this.color_ins = color_ins;
        this.color_ins1 = color_ins1;
        this.color_ins2 = color_ins2;
        this.color_ins_perbu = color_ins_perbu;
        this.color_ch = color_ch;
    }

    /** default constructor */
    public ColorSchemeH()
    {
    }

    /**
     * Get Id
     * 
     * @return
     */
    public long getId()
    {
        return this.id;
    }

    /**
     * Set Id
     * 
     * @param id
     */
    public void setId(long id)
    {
        this.id = id;
    }

    /**
     * Get Name
     * 
     * @return name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Set Name
     * 
     * @param name as string
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get Is Custom Type
     * 
     * @return
     */
    public int getCustom_type()
    {
        return this.custom_type;
    }

    /**
     * Set Is Custom Type
     * 
     * @param custom_type 
     */
    public void setCustom_type(int custom_type)
    {
        this.custom_type = custom_type;
    }

    /**
     * Get Color BG
     *  
     * @return
     */
    public int getColor_bg()
    {
        return this.color_bg;
    }

    /**
     * Set Color BG
     * 
     * @param color_bg 
     */
    public void setColor_bg(int color_bg)
    {
        this.color_bg = color_bg;
    }

    /**
     * Get Color BG Average
     *  
     * @return
     */
    public int getColor_bg_avg()
    {
        return this.color_bg_avg;
    }

    /**
     * Set Color BG Average
     * 
     * @param color_bg_avg 
     */
    public void setColor_bg_avg(int color_bg_avg)
    {
        this.color_bg_avg = color_bg_avg;
    }

    /**
     * Get Color BG Low
     *  
     * @return
     */
    public int getColor_bg_low()
    {
        return this.color_bg_low;
    }

    /**
     * Set Color BG Low
     * 
     * @param color_bg_low 
     */
    public void setColor_bg_low(int color_bg_low)
    {
        this.color_bg_low = color_bg_low;
    }

    /**
     * Get Color BG High
     *  
     * @return
     */
    public int getColor_bg_high()
    {
        return this.color_bg_high;
    }

    /**
     * Set Color BG High
     * 
     * @param color_bg_high 
     */
    public void setColor_bg_high(int color_bg_high)
    {
        this.color_bg_high = color_bg_high;
    }

    /**
     * Get Color BG Target
     *  
     * @return
     */
    public int getColor_bg_target()
    {
        return this.color_bg_target;
    }

    /**
     * Set Color BG Target
     * 
     * @param color_bg_target 
     */
    public void setColor_bg_target(int color_bg_target)
    {
        this.color_bg_target = color_bg_target;
    }

    /**
     * Get Color Insulin
     *  
     * @return
     */
    public int getColor_ins()
    {
        return this.color_ins;
    }

    /**
     * Set Color Insulin
     * 
     * @param color_ins 
     */
    public void setColor_ins(int color_ins)
    {
        this.color_ins = color_ins;
    }

    /**
     * Get Color Insulin 1
     *  
     * @return
     */
    public int getColor_ins1()
    {
        return this.color_ins1;
    }

    /**
     * Set Color Insulin 1
     * 
     * @param color_ins1
     */
    public void setColor_ins1(int color_ins1)
    {
        this.color_ins1 = color_ins1;
    }

    /**
     * Get Color Insulin 2
     *  
     * @return
     */
    public int getColor_ins2()
    {
        return this.color_ins2;
    }

    /**
     * Set Color Insulin 2
     * 
     * @param color_ins2 
     */
    public void setColor_ins2(int color_ins2)
    {
        this.color_ins2 = color_ins2;
    }

    /**
     * Get Color Insulin per CH
     *  
     * @return
     */
    public int getColor_ins_perbu()
    {
        return this.color_ins_perbu;
    }

    /**
     * Set Color Insulin per CH
     * 
     * @param color_ins_perbu 
     */
    public void setColor_ins_perbu(int color_ins_perbu)
    {
        this.color_ins_perbu = color_ins_perbu;
    }

    /**
     * Get Color CH
     *  
     * @return
     */
    public int getColor_ch()
    {
        return this.color_ch;
    }

    /**
     * Set Color CH
     * @param color_ch 
     */
    public void setColor_ch(int color_ch)
    {
        this.color_ch = color_ch;
    }

    /**
     * Equals - method to check equalicy of object
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof ColorSchemeH))
            return false;
        ColorSchemeH castOther = (ColorSchemeH) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }

    /**
     * Create Hash Code
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

}
