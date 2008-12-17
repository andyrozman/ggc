package ggc.core.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class StockSubTypeH implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2088376312790478773L;

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private long stock_type_id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private float content;

    /** nullable persistent field */
    private String comment;

    /** full constructor 
     * @param stock_type_id 
     * @param name 
     * @param description 
     * @param content 
     * @param comment */
    public StockSubTypeH(long stock_type_id, String name, String description, float content, String comment) {
        this.stock_type_id = stock_type_id;
        this.name = name;
        this.description = description;
        this.content = content;
        this.comment = comment;
    }

    /** default constructor */
    public StockSubTypeH() {
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
     * Get Stock Type Id
     * 
     * @return stock_type_id value
     */
    public long getStock_type_id() 
    {
        return this.stock_type_id;
    }

    /**
     * Set Stock Type Id
     * 
     * @param stock_type_id value
     */
    public void setStock_type_id(long stock_type_id) 
    {
        this.stock_type_id = stock_type_id;
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

    /**
     * Get Content
     * 
     * @return content parameter
     */
    public float getContent() 
    {
        return this.content;
    }

    /**
     * Get Content
     * 
     * @param content parameter
     */
    public void setContent(float content) 
    {
        this.content = content;
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
        if ( !(other instanceof StockSubTypeH) ) return false;
        StockSubTypeH castOther = (StockSubTypeH) other;
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
