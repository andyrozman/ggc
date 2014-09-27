package ggc.core.db.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *  @author Hibernate CodeGenerator 
 */
public class DayValueH implements Serializable, GGCHibernateObject
{

    private static final long serialVersionUID = -1661031856854546844L;

    /** identifier field */
    private long id;

    /** persistent field */
    private long dt_info;

    /** nullable persistent field */
    private int bg;

    /** nullable persistent field */
    private int ins1;

    /** nullable persistent field */
    private int ins2;

    /** nullable persistent field */
    private float ch;

    /** nullable persistent field */
    private String meals_ids;

    /** nullable persistent field */
    private String extended;

    /** persistent field */
    private int person_id;

    /** nullable persistent field */
    private String comment;

    /** nullable persistent field */
    private long changed;

    /**
     * full constructor
     * 
     * @param dt_info
     * @param bg
     * @param ins1
     * @param ins2
     * @param ch
     * @param meals_ids
     * @param extended
     * @param person_id
     * @param comment
     * @param changed
     */
    public DayValueH(long dt_info, int bg, int ins1, int ins2, float ch, String meals_ids, String extended,
            int person_id, String comment, long changed)
    {
        this.dt_info = dt_info;
        this.bg = bg;
        this.ins1 = ins1;
        this.ins2 = ins2;
        this.ch = ch;
        this.meals_ids = meals_ids;
        this.extended = extended;
        this.person_id = person_id;
        this.comment = comment;
        this.changed = changed;
    }

    /** 
     * default constructor 
     */
    public DayValueH()
    {
    }

    /**
     * minimal constructor
     * 
     * @param dt_info
     * @param person_id
     */
    public DayValueH(long dt_info, int person_id)
    {
        this.dt_info = dt_info;
        this.person_id = person_id;
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
     * Get Date/Time Info (this is long packed as AtechDateTime yyyymmddhhss)
     * @return
     */
    public long getDt_info()
    {
        return this.dt_info;
    }

    /**
     * Set Date/Time Info (this is long packed as AtechDateTime yyyymmddhhss)
     * 
     * @param dt_info 
     */
    public void setDt_info(long dt_info)
    {
        this.dt_info = dt_info;
    }

    /**
     * Get Bg
     * 
     * @return
     */
    public int getBg()
    {
        return this.bg;
    }

    /**
     * Set Bg
     * 
     * @param bg
     */
    public void setBg(int bg)
    {
        this.bg = bg;
    }

    /**
     * Get Insulin 1
     * 
     * @return
     */
    public int getIns1()
    {
        return this.ins1;
    }

    /**
     * Set Insulin 1
     * 
     * @param ins1
     */
    public void setIns1(int ins1)
    {
        this.ins1 = ins1;
    }

    /**
     * Get Insulin 2
     * 
     * @return
     */
    public int getIns2()
    {
        return this.ins2;
    }

    /**
     * Set Insulin 2
     * 
     * @param ins2
     */
    public void setIns2(int ins2)
    {
        this.ins2 = ins2;
    }

    /**
     * Get Carbohydrates
     * 
     * @return
     */
    public float getCh()
    {
        return this.ch;
    }

    /**
     * Set Carbohydrates
     * 
     * @param ch
     */
    public void setCh(float ch)
    {
        this.ch = ch;
    }

    /**
     * Get Meal Ids
     * 
     * @return
     */
    public String getMeals_ids()
    {
        return this.meals_ids;
    }

    /**
     * Set Meal Ids
     * 
     * @param meals_ids
     */
    public void setMeals_ids(String meals_ids)
    {
        this.meals_ids = meals_ids;
    }

    /**
     * Get Extended 
     * 
     * @return extended value
     */
    public String getExtended()
    {
        return this.extended;
    }

    /**
     * Set Extended
     *  
     * @param extended parameter
     */
    public void setExtended(String extended)
    {
        this.extended = extended;
    }

    /**
     * Get Person Id
     * 
     * @return person id parameter
     */
    public int getPerson_id()
    {
        return this.person_id;
    }

    /**
     * Set Person Id
     * 
     * @param person_id parameter
     */
    public void setPerson_id(int person_id)
    {
        this.person_id = person_id;
    }

    /**
     * Get Comment
     * 
     * @return
     */
    public String getComment()
    {
        return this.comment;
    }

    /**
     * Set Comment
     * 
     * @param comment
     */
    public void setComment(String comment)
    {
        this.comment = comment;
    }

    /**
     * Get Changed
     * 
     * @return changed value
     */
    public long getChanged()
    {
        return this.changed;
    }

    /**
     * Set Changed
     * 
     * @param changed parameter
     */
    public void setChanged(long changed)
    {
        this.changed = changed;
    }

    /**
     * Custom toString implementation
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }

    /** 
     * Custom equals implementation
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof DayValueH))
            return false;
        DayValueH castOther = (DayValueH) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    /**
     * Hash code generator
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

}
