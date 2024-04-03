package ggc.core.db.hibernate.food;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.atech.db.hibernate.HibernateObject;

public class NutritionHomeWeightTypeH extends HibernateObject implements Serializable
{

    private static final long serialVersionUID = 2481733598356516091L;

    private long id;
    private String name;
    private int staticEntry;


    /** full constructor 
     * @param name 
     * @param staticEntry */
    public NutritionHomeWeightTypeH(String name, int staticEntry)
    {
        this.name = name;
        this.staticEntry = staticEntry;
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
     * @return staticEntry
     */
    public int getStaticEntry()
    {
        return this.staticEntry;
    }


    /**
     * Set Is Static Entry
     * 
     * @param staticEntry as string
     */
    public void setStaticEntry(int staticEntry)
    {
        this.staticEntry = staticEntry;
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
