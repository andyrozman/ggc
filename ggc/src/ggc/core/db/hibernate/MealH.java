package ggc.core.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MealH implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8237292183858746065L;

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private long group_id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String name_i18n;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private String parts;

    /** nullable persistent field */
    private String nutritions;

    /** nullable persistent field */
    private String extended;

    /** nullable persistent field */
    private String comment;

    /** nullable persistent field */
    private long changed;

    /** full constructor 
     * @param group_id 
     * @param name 
     * @param name_i18n 
     * @param description 
     * @param parts 
     * @param nutritions 
     * @param extended 
     * @param comment 
     * @param changed */
    public MealH(long group_id, String name, String name_i18n, String description, String parts, String nutritions, String extended, String comment, long changed) {
        this.group_id = group_id;
        this.name = name;
        this.name_i18n = name_i18n;
        this.description = description;
        this.parts = parts;
        this.nutritions = nutritions;
        this.extended = extended;
        this.comment = comment;
        this.changed = changed;
    }

    /** default constructor */
    public MealH() {
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
     * Get Group Id
     * 
     * @return group_id value
     */
    public long getGroup_id() 
    {
        return this.group_id;
    }

    /**
     * Set Group Id
     * 
     * @param group_id value
     */
    public void setGroup_id(long group_id) 
    {
        this.group_id = group_id;
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
     * Get Name (I18n)
     * 
     * @return name
     */
    public String getName_i18n() 
    {
        return this.name_i18n;
    }

    /**
     * Set Name (I18n)
     * 
     * @param name_i18n as string
     */
    public void setName_i18n(String name_i18n) 
    {
        this.name_i18n = name_i18n;
    }

    /**
     * Get Description
     * 
     * @return description parameter
     */
    public String getDescription() 
    {
        return this.description;
    }

    /**
     * Set Description
     * 
     * @param description parameter
     */
    public void setDescription(String description) 
    {
        this.description = description;
    }

    /**
     * Get Parts
     * 
     * @return parts value
     */
    public String getParts() 
    {
        return this.parts;
    }

    /**
     * Set Parts
     * 
     * @param parts value
     */
    public void setParts(String parts) 
    {
        this.parts = parts;
    }

    /**
     * Get Nutritions
     * 
     * @return nutritions value
     */
    public String getNutritions() 
    {
        return this.nutritions;
    }

    /**
     * Set Nutritions
     * 
     * @param nutritions value
     */
    public void setNutritions(String nutritions) 
    {
        this.nutritions = nutritions;
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
     * Custom equals implementation
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) 
    {
        if ( !(other instanceof MealH) ) return false;
        MealH castOther = (MealH) other;
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
