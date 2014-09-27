package ggc.core.db.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FoodGroupH implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 5806121161463589856L;

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String name_i18n;

    /** nullable persistent field */
    private String description;

    /** full constructor 
     * @param name 
     * @param name_i18n 
     * @param description */
    public FoodGroupH(String name, String name_i18n, String description)
    {
        this.name = name;
        this.name_i18n = name_i18n;
        this.description = description;
    }

    /** default constructor */
    public FoodGroupH()
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
     * Custom equals implementation
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof FoodGroupH))
            return false;
        FoodGroupH castOther = (FoodGroupH) other;
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
