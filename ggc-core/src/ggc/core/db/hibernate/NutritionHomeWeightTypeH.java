package ggc.core.db.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class NutritionHomeWeightTypeH implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 2481733598356516091L;

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private int static_entry;

    /** full constructor 
     * @param name 
     * @param static_entry */
    public NutritionHomeWeightTypeH(String name, int static_entry)
    {
        this.name = name;
        this.static_entry = static_entry;
    }

    /** default constructor */
    public NutritionHomeWeightTypeH()
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
     * Get Is Static Entry
     * 
     * @return static_entry
     */
    public int getStatic_entry()
    {
        return this.static_entry;
    }

    /**
     * Set Is Static Entry
     * 
     * @param static_entry as string
     */
    public void setStatic_entry(int static_entry)
    {
        this.static_entry = static_entry;
    }

    /** 
     * Custom equals implementation
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof NutritionHomeWeightTypeH))
            return false;
        NutritionHomeWeightTypeH castOther = (NutritionHomeWeightTypeH) other;
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
