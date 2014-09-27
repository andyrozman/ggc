package ggc.core.db.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MealGroupH implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -6895815504898328042L;

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String name_i18n;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private long parent_id;

    /** nullable persistent field */
    private long changed;

    /** full constructor 
     * @param name 
     * @param name_i18n 
     * @param description 
     * @param parent_id 
     * @param changed */
    public MealGroupH(String name, String name_i18n, String description, long parent_id, long changed)
    {
        this.name = name;
        this.name_i18n = name_i18n;
        this.description = description;
        this.parent_id = parent_id;
        this.changed = changed;
    }

    /** default constructor */
    public MealGroupH()
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
     * Get Parent Id
     * 
     * @return parent_id parameter
     */
    public long getParent_id()
    {
        return this.parent_id;
    }

    /**
     * Set Parent Id
     * 
     * @param parent_id parameter
     */
    public void setParent_id(long parent_id)
    {
        this.parent_id = parent_id;
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
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof MealGroupH))
            return false;
        MealGroupH castOther = (MealGroupH) other;
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
