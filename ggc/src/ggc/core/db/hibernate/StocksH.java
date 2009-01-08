package ggc.core.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** 
 * @author Hibernate CodeGenerator 
 * */
public class StocksH implements Serializable 
{

    private static final long serialVersionUID = -5410889916190454790L;

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private long stock_subtype_id;

    /** nullable persistent field */
    private long dt_stock;

    /** nullable persistent field */
    private float amount;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private float usage_per_day_min;

    /** nullable persistent field */
    private float usage_per_day_max;

    /** nullable persistent field */
    private String extended;

    /** persistent field */
    private int person_id;

    /** nullable persistent field */
    private String comment;

    /** full constructor 
     * @param stock_subtype_id 
     * @param dt_stock 
     * @param amount 
     * @param description 
     * @param usage_per_day_min 
     * @param usage_per_day_max 
     * @param extended 
     * @param person_id 
     * @param comment */
    public StocksH(long stock_subtype_id, long dt_stock, float amount, String description, float usage_per_day_min, float usage_per_day_max, String extended, int person_id, String comment) {
        this.stock_subtype_id = stock_subtype_id;
        this.dt_stock = dt_stock;
        this.amount = amount;
        this.description = description;
        this.usage_per_day_min = usage_per_day_min;
        this.usage_per_day_max = usage_per_day_max;
        this.extended = extended;
        this.person_id = person_id;
        this.comment = comment;
    }

    /** default constructor */
    public StocksH() {
    }

    /** minimal constructor 
     * @param person_id */
    public StocksH(int person_id) {
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
     * Get Stock Subtype
     * 
     * @return
     */
    public long getStock_subtype_id() 
    {
        return this.stock_subtype_id;
    }

    /**
     * Set Stock Subtype
     * 
     * @param stock_subtype_id
     */
    public void setStock_subtype_id(long stock_subtype_id) 
    {
        this.stock_subtype_id = stock_subtype_id;
    }

    /**
     * Get DateTime for Stock
     * 
     * @return
     */
    public long getDt_stock() 
    {
        return this.dt_stock;
    }

    /**
     * Set DateTime for Stock
     * 
     * @param dt_stock
     */
    public void setDt_stock(long dt_stock) 
    {
        this.dt_stock = dt_stock;
    }

    /**
     * Get Amount
     * 
     * @return
     */
    public float getAmount() 
    {
        return this.amount;
    }

    /**
     * Set Amount
     * 
     * @param amount
     */
    public void setAmount(float amount) {
        this.amount = amount;
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
     * Get Usage per Day - Minimal
     * 
     * @return
     */
    public float getUsage_per_day_min() 
    {
        return this.usage_per_day_min;
    }

    /**
     * Set Usage per Day - Minimal
     * 
     * @param usage_per_day_min 
     */
    public void setUsage_per_day_min(float usage_per_day_min) 
    {
        this.usage_per_day_min = usage_per_day_min;
    }

    /**
     * Get Usage per Day - Maximal
     * 
     * @return
     */
    public float getUsage_per_day_max() 
    {
        return this.usage_per_day_max;
    }

    /**
     * Set Usage per Day - Maximal
     * 
     * @param usage_per_day_max 
     */
    public void setUsage_per_day_max(float usage_per_day_max) 
    {
        this.usage_per_day_max = usage_per_day_max;
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
     * Custom equals implementation
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) 
    {
        if ( !(other instanceof StocksH) ) return false;
        StocksH castOther = (StocksH) other;
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
