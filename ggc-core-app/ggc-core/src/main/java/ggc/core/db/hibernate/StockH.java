package ggc.core.db.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class StockH implements Serializable
{

    private static final long serialVersionUID = -3068125166287873609L;

    private long id;
    private long stockSubtypeId;
    private StockSubTypeH stockSubtype;
    private long stocktakingId;
    private long amount;
    private String location;
    private Long validFrom;
    private Long validTill;

    private String extended;
    private int personId;
    private String comment;


    public StockH()
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
     * Get Amount
     * 
     * @return
     */
    public long getAmount()
    {
        return this.amount;
    }


    /**
     * Set Amount
     * 
     * @param amount
     */
    public void setAmount(long amount)
    {
        this.amount = amount;
    }


    public long getStockSubtypeId()
    {
        return stockSubtypeId;
    }


    public void setStockSubtypeId(long stockSubtypeId)
    {
        this.stockSubtypeId = stockSubtypeId;
    }


    public long getStocktakingId()
    {
        return stocktakingId;
    }


    public void setStocktakingId(long stocktakingId)
    {
        this.stocktakingId = stocktakingId;
    }


    public String getLocation()
    {
        return location;
    }


    public void setLocation(String location)
    {
        this.location = location;
    }


    public int getPersonId()
    {
        return personId;
    }


    public void setPersonId(int personId)
    {
        this.personId = personId;
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


    public Long getValidFrom()
    {
        return validFrom;
    }


    public void setValidFrom(Long validFrom)
    {
        this.validFrom = validFrom;
    }


    public Long getValidTill()
    {
        return validTill;
    }


    public void setValidTill(Long validTill)
    {
        this.validTill = validTill;
    }


    /**
     * Custom equals implementation
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof StockH))
            return false;
        StockH castOther = (StockH) other;
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


    public StockSubTypeH getStockSubtype()
    {
        return stockSubtype;
    }


    public void setStockSubtype(StockSubTypeH stockSubtype)
    {
        this.stockSubtype = stockSubtype;
    }
}
