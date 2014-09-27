package ggc.core.db.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class DbInfoH implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -6483987777425549082L;

    /** identifier field */
    private long id;

    /** persistent field */
    private String key;

    /** nullable persistent field */
    private String value;

    /** nullable persistent field */
    private int type;

    /** nullable persistent field */
    private String description;

    /** full constructor 
     * @param key 
     * @param value 
     * @param type 
     * @param description */
    public DbInfoH(String key, String value, int type, String description)
    {
        this.key = key;
        this.value = value;
        this.type = type;
        this.description = description;
    }

    /** default constructor */
    public DbInfoH()
    {
    }

    /** minimal constructor 
     * @param key */
    public DbInfoH(String key)
    {
        this.key = key;
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
     * Get Key 
     * 
     * @return key value
     */
    public String getKey()
    {
        return this.key;
    }

    /**
     * Set Key
     *  
     * @param key parameter
     */
    public void setKey(String key)
    {
        this.key = key;
    }

    /**
     * Get Value 
     * 
     * @return value value
     */
    public String getValue()
    {
        return this.value;
    }

    /**
     * Set Extended
     *  
     * @param value parameter
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * Get Type 
     * 
     * @return type value
     */
    public int getType()
    {
        return this.type;
    }

    /**
     * Set Type
     *  
     * @param type parameter
     */
    public void setType(int type)
    {
        this.type = type;
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
        if (!(other instanceof DbInfoH))
            return false;
        DbInfoH castOther = (DbInfoH) other;
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
