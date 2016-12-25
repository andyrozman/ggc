package ggc.core.db.hibernate;

import java.io.Serializable;

import ggc.core.data.defs.StockTypeBase;
import ggc.core.data.defs.StockUsageUnit;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


public class StockSubTypeH implements Serializable
{
    private static final long serialVersionUID = -7076410510425754609L;

    private long id;
    private long stockTypeId;
    private String name;
    private String description;
    private long packageContent;
    private String packageContentUnit;
    private int usageUnit;
    private int usageMin;
    private int usageMax;
    private boolean active;
    private int personId;
    private String extended;
    private String comment;



    /** default constructor */
    public StockSubTypeH()
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


    public long getStockTypeId()
    {
        return stockTypeId;
    }

    public StockTypeBase getStockType()
    {
        return StockTypeBase.getByCode((int)this.stockTypeId);
    }



    public void setStockTypeId(long stockTypeId)
    {
        this.stockTypeId = stockTypeId;
    }

    public long getPackageContent()
    {
        return packageContent;
    }

    public void setPackageContent(long packageContent)
    {
        this.packageContent = packageContent;
    }

    public String getPackageContentUnit()
    {
        return packageContentUnit;
    }

    public void setPackageContentUnit(String packageContentUnit)
    {
        this.packageContentUnit = packageContentUnit;
    }



    public int getUsageMin()
    {
        return usageMin;
    }

    public void setUsageMin(int usageMin)
    {
        this.usageMin = usageMin;
    }

    public int getUsageMax()
    {
        return usageMax;
    }

    public void setUsageMax(int usageMax)
    {
        this.usageMax = usageMax;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public int getPersonId()
    {
        return personId;
    }

    public void setPersonId(int personId)
    {
        this.personId = personId;
    }

    public String getExtended()
    {
        return extended;
    }

    public void setExtended(String extended)
    {
        this.extended = extended;
    }

    public String getUsageDescription()
    {
        return "" + this.usageMin + " - " + this.usageMax + " / " + StockUsageUnit.getByCode(this.getUsageUnit()).getTranslation();
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
     * Custom equals implementation
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof StockSubTypeH))
            return false;
        StockSubTypeH castOther = (StockSubTypeH) other;
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


    public int getUsageUnit()
    {
        return usageUnit;
    }

    public void setUsageUnit(int usageUnit)
    {
        this.usageUnit = usageUnit;
    }
}
