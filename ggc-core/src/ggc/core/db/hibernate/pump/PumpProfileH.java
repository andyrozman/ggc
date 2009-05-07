package ggc.core.db.hibernate.pump;

import ggc.core.db.hibernate.GGCHibernateObject;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class PumpProfileH implements Serializable, GGCHibernateObject
{

    private static final long serialVersionUID = 8212384538572472772L;

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private float basal_base;

    /** nullable persistent field */
    private String basal_diffs;

    /** nullable persistent field */
    private long active_from;

    /** nullable persistent field */
    private long active_till;

    /** nullable persistent field */
    private String extended;

    /** persistent field */
    private int person_id;

    /** nullable persistent field */
    private String comment;

    /** nullable persistent field */
    private long changed;

    /** full constructor 
     * @param name 
     * @param basal_base 
     * @param basal_diffs 
     * @param active_from 
     * @param active_till 
     * @param extended 
     * @param person_id 
     * @param comment 
     * @param changed */
    public PumpProfileH(String name, float basal_base, String basal_diffs, long active_from, long active_till, String extended, int person_id, String comment, long changed) {
        this.name = name;
        this.basal_base = basal_base;
        this.basal_diffs = basal_diffs;
        this.active_from = active_from;
        this.active_till = active_till;
        this.extended = extended;
        this.person_id = person_id;
        this.comment = comment;
        this.changed = changed;
    }

    /** default constructor */
    public PumpProfileH() {
    }

    /** minimal constructor 
     * @param person_id */
    public PumpProfileH(int person_id) {
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
     * Get Basal Rate - Base
     * 
     * @return basal_base value
     */
    public float getBasal_base() 
    {
        return this.basal_base;
    }

    /**
     * Set Basal Rate - Base
     * 
     * @param basal_base value
     */
    public void setBasal_base(float basal_base) 
    {
        this.basal_base = basal_base;
    }

    /**
     * Get Basal Rate - Diffs (example: 1100-1230=1.26;1330-1430=1.35)
     * 
     * @return basal_diffs value
     */
    public String getBasal_diffs() {
        return this.basal_diffs;
    }

    /**
     * Set Basal Rate - Diffs (example: 1100-1230=1.26;1330-1430=1.35)
     * 
     * @param basal_diffs value
     */
    public void setBasal_diffs(String basal_diffs) 
    {
        this.basal_diffs = basal_diffs;
    }

    /**
     * Get Active From
     * 
     * @return active_from value
     */
    public long getActive_from() 
    {
        return this.active_from;
    }

    /**
     * Set Active From
     * 
     * @param active_from value
     */
    public void setActive_from(long active_from) 
    {
        this.active_from = active_from;
    }

    /**
     * Get Active Till
     * 
     * @return active_till value
     */
    public long getActive_till() 
    {
        return this.active_till;
    }

    /**
     * Set Active Till
     * 
     * @param active_till value
     */
    public void setActive_till(long active_till) 
    {
        this.active_till = active_till;
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
        if ( !(other instanceof PumpProfileH) ) return false;
        PumpProfileH castOther = (PumpProfileH) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
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
