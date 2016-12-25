package ggc.core.db.hibernate.cgms;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.atech.db.hibernate.HibernateObject;

/**
 * @author Hibernate CodeGenerator
 */
public class CGMSDataH extends HibernateObject
{

    private static final long serialVersionUID = 9052976780137113098L;

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
    public CGMSDataH(long dt_info, int base_type, int sub_type, String value, String extended, int person_id,
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
    public CGMSDataH()
    {
    }


    /**
     * minimal constructor
     * 
     * @param dt_info
     * @param person_id
     */
    public CGMSDataH(long dt_info, int person_id)
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
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        CGMSDataH cgmsDataH = (CGMSDataH) o;

        return new EqualsBuilder() //
                .append(id, cgmsDataH.id) //
                .append(dt_info, cgmsDataH.dt_info) //
                .append(base_type, cgmsDataH.base_type) //
                .append(sub_type, cgmsDataH.sub_type) //
                .append(person_id, cgmsDataH.person_id) //
                .append(changed, cgmsDataH.changed) //
                .append(value, cgmsDataH.value) //
                .append(extended, cgmsDataH.extended) //
                .append(comment, cgmsDataH.comment) //
                .isEquals();
    }


    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(17, 37) //
                .appendSuper(super.hashCode()) //
                .append(id) //
                .append(dt_info) //
                .append(base_type) //
                .append(sub_type) //
                .append(value) //
                .append(extended) //
                .append(person_id) //
                .append(comment) //
                .append(changed) //
                .toHashCode();
    }
}