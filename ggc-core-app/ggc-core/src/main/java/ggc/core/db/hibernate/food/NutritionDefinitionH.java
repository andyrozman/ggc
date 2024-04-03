package ggc.core.db.hibernate.food;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.atech.db.hibernate.HibernateObject;

public class NutritionDefinitionH extends HibernateObject implements Serializable
{

    private static final long serialVersionUID = -587797287729046076L;

    private long id;
    private String weightUnit;
    private String tag;
    private String name;
    private String decimalPlaces;
    private int staticEntry;


    /** full constructor 
     * @param weightUnit
     * @param tag 
     * @param name 
     * @param decimalPlaces
     * @param staticEntry */
    public NutritionDefinitionH(String weightUnit, String tag, String name, String decimalPlaces, int staticEntry)
    {
        this.weightUnit = weightUnit;
        this.tag = tag;
        this.name = name;
        this.decimalPlaces = decimalPlaces;
        this.staticEntry = staticEntry;
    }


    /** default constructor */
    public NutritionDefinitionH()
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
     * Get Weight Unit
     * 
     * @return
     */
    public String getWeightUnit()
    {
        return this.weightUnit;
    }


    /**
     * Set Weight Unit
     * 
     * @param weightUnit
     */
    public void setWeightUnit(String weightUnit)
    {
        this.weightUnit = weightUnit;
    }


    /**
     * Get Tag
     * 
     * @return
     */
    public String getTag()
    {
        return this.tag;
    }


    /**
     * Set Tag
     * 
     * @param tag 
     */
    public void setTag(String tag)
    {
        this.tag = tag;
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
     * Get Decimal Places
     * 
     * @return
     */
    public String getDecimalPlaces()
    {
        return this.decimalPlaces;
    }


    /**
     * Set Decimal Places
     * 
     * @param decimalPlaces
     */
    public void setDecimalPlaces(String decimalPlaces)
    {
        this.decimalPlaces = decimalPlaces;
    }


    /**
     * Get Is Static Entry (non user supplied)
     * 
     * @return
     */
    public int getStaticEntry()
    {
        return this.staticEntry;
    }


    /**
     * Set Is Static Entry (non user supplied)
     * 
     * @param staticEntry
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
        if (!(other instanceof NutritionDefinitionH))
            return false;
        NutritionDefinitionH castOther = (NutritionDefinitionH) other;
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
