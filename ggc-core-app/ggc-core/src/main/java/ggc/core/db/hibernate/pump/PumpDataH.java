package ggc.core.db.hibernate.pump;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.atech.db.hibernate.HibernateObject;

public class PumpDataH extends HibernateObject
{

    private static final long serialVersionUID = -3935397524396978150L;

    private long id;
    private long dtInfo;
    private int baseType;
    private int subType;
    private String value;
    private String extended;
    private int personId;
    private String comment;
    private long changed;


    /** full constructor 
     * @param dtInfo
     * @param baseType
     * @param subType
     * @param value 
     * @param extended 
     * @param personId
     * @param comment 
     * @param changed */
    public PumpDataH(long dtInfo, int baseType, int subType, String value, String extended, int personId,
            String comment, long changed)
    {
        this.dtInfo = dtInfo;
        this.baseType = baseType;
        this.subType = subType;
        this.value = value;
        this.extended = extended;
        this.personId = personId;
        this.comment = comment;
        this.changed = changed;
    }


    /** default constructor */
    public PumpDataH()
    {
    }


    /** minimal constructor 
     * @param dtInfo
     * @param personId */
    public PumpDataH(long dtInfo, int personId)
    {
        this.dtInfo = dtInfo;
        this.personId = personId;
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
     * Get Base Type 
     * 
     * @return baseType value
     */
    public int getBaseType()
    {
        return this.baseType;
    }


    /**
     * Set Base Type 
     * 
     * @param baseType value
     */
    public void setBaseType(int baseType)
    {
        this.baseType = baseType;
    }


    /**
     * Get Sub Type 
     * 
     * @return subType value
     */
    public int getSubType()
    {
        return this.subType;
    }


    /**
     * Set Sub Type 
     * 
     * @param subType value
     */
    public void setSubType(int subType)
    {
        this.subType = subType;
    }


    /**
     * Get Value 
     * 
     * @return value value
     */
    public String getValue()
    {
        return this.value;
    }


    /**
     * Set Extended
     *  
     * @param value parameter
     */
    public void setValue(String value)
    {
        this.value = value;
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
     * Equals - method to check equalicy of object
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof PumpDataH))
            return false;
        PumpDataH castOther = (PumpDataH) other;
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
