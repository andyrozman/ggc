package ggc.core.db.hibernate.cgm;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Hibernate CodeGenerator
 */
public class CGMDataH implements Serializable
{

    private static final long serialVersionUID = 1926001888869758766L;

    /** identifier field */
    private long id;

    /** persistent field */
    private long dt_info;

    /** nullable persistent field */
    private int base_type;

    /** nullable persistent field */
    private int sub_type;

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

    /**
     * full constructor
     * 
     * @param dt_info
     * @param base_type
     * @param sub_type
     * @param value
     * @param extended
     * @param person_id
     * @param comment
     * @param changed
     * */
    public CGMDataH(long dt_info, int base_type, int sub_type, String value, String extended, int person_id,
            String comment, long changed)
    {
        this.dt_info = dt_info;
        this.base_type = base_type;
        this.sub_type = sub_type;
        this.value = value;
        this.extended = extended;
        this.person_id = person_id;
        this.comment = comment;
        this.changed = changed;
    }

    /**
     * default constructor
     */
    public CGMDataH()
    {
    }

    /**
     * minimal constructor
     * 
     * @param dt_info
     * @param person_id
     */
    public CGMDataH(long dt_info, int person_id)
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
     * Get Base Type 
     * 
     * @return base_type value
     */
    public int getBase_type()
    {
        return this.base_type;
    }

    /**
     * Set Base Type 
     * 
     * @param base_type value
     */
    public void setBase_type(int base_type)
    {
        this.base_type = base_type;
    }

    /**
     * Get Sub Type 
     * 
     * @return sub_type value
     */
    public int getSub_type()
    {
        return this.sub_type;
    }

    /**
     * Set Sub Type 
     * 
     * @param sub_type value
     */
    public void setSub_type(int sub_type)
    {
        this.sub_type = sub_type;
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
    public boolean equals(Object other)
    {
        if (!(other instanceof CGMDataH))
            return false;
        CGMDataH castOther = (CGMDataH) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() 
    {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }
    
    
    /**
     * Create Hash Code
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() 
    {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

}
