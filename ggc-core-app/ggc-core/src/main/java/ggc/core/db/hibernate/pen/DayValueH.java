package ggc.core.db.hibernate.pen;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.atech.db.hibernate.HibernateObject;

public class DayValueH extends HibernateObject
{

    private static final long serialVersionUID = -1661031856854546844L;

    private long id;
    private long dtInfo;
    private int bg;
    private int ins1;
    private int ins2;
    private float ch;
    private String mealsIds;
    private String extended;
    private int personId;
    private String comment;
    private long changed;


    /**
     * full constructor
     * 
     * @param dtInfo
     * @param bg
     * @param ins1
     * @param ins2
     * @param ch
     * @param mealsIds
     * @param extended
     * @param personId
     * @param comment
     * @param changed
     */
    public DayValueH(long dtInfo, int bg, int ins1, int ins2, float ch, String mealsIds, String extended, int personId,
            String comment, long changed)
    {
        this.dtInfo = dtInfo;
        this.bg = bg;
        this.ins1 = ins1;
        this.ins2 = ins2;
        this.ch = ch;
        this.mealsIds = mealsIds;
        this.extended = extended;
        this.personId = personId;
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
     * @param dtInfo
     * @param person_id
     */
    public DayValueH(long dtInfo, int person_id)
    {
        this.dtInfo = dtInfo;
        this.personId = person_id;
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
    public long getDtInfo()
    {
        return this.dtInfo;
    }


    /**
     * Set Date/Time Info (this is long packed as AtechDateTime yyyymmddhhss)
     * 
     * @param dtInfo
     */
    public void setDtInfo(long dtInfo)
    {
        this.dtInfo = dtInfo;
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
    public String getMealsIds()
    {
        return this.mealsIds;
    }


    /**
     * Set Meal Ids
     * 
     * @param mealsIds
     */
    public void setMealsIds(String mealsIds)
    {
        this.mealsIds = mealsIds;
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
    public int getPersonId()
    {
        return this.personId;
    }


    /**
     * Set Person Id
     * 
     * @param personId parameter
     */
    public void setPersonId(int personId)
    {
        this.personId = personId;
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
