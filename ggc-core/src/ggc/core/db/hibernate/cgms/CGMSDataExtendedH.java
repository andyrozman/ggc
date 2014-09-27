package ggc.core.db.hibernate.cgms;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class CGMSDataExtendedH implements Serializable
{

    private static final long serialVersionUID = -8690831306276025195L;

    /** identifier field */
    private long id;

    /** persistent field */
    private long dt_info;

    /** nullable persistent field */
    private int type;

    /** nullable persistent field */
    private String value;

    /** nullable persistent field */
    private String extended;

    /** persistent field */
    private int person_id;

    /** nullable persistent field */
    private String comment;

    /** nullable persistent field */
    private long changed;

    /** full constructor 
     * @param dt_info 
     * @param type 
     * @param value 
     * @param extended 
     * @param person_id 
     * @param comment 
     * @param changed */
    public CGMSDataExtendedH(long dt_info, int type, String value, String extended, int person_id, String comment,
            long changed)
    {
        this.dt_info = dt_info;
        this.type = type;
        this.value = value;
        this.extended = extended;
        this.person_id = person_id;
        this.comment = comment;
        this.changed = changed;
    }

    /** default constructor */
    public CGMSDataExtendedH()
    {
    }

    /** minimal constructor 
     * @param dt_info 
     * @param person_id */
    public CGMSDataExtendedH(long dt_info, int person_id)
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
     * Get Type 
     * 
     * @return type value
     */
    public int getType()
    {
        return this.type;
    }

    /**
     * Set Type
     *  
     * @param type parameter
     */
    public void setType(int type)
    {
        this.type = type;
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
     * Equals - method to check equalicy of object
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof CGMSDataExtendedH))
            return false;
        CGMSDataExtendedH castOther = (CGMSDataExtendedH) other;
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
