package ggc.core.db.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class InetLinkGroupH implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = 8120450189760694491L;

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String group_id;

    /** nullable persistent field */
    private String link_id;

    /** full constructor 
     * @param group_id 
     * @param link_id */
    public InetLinkGroupH(String group_id, String link_id)
    {
        this.group_id = group_id;
        this.link_id = link_id;
    }

    /** default constructor */
    public InetLinkGroupH()
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
     * Get Group Id
     * 
     * @return group_id value
     */
    public String getGroup_id()
    {
        return this.group_id;
    }

    /**
     * Set Group Id
     * 
     * @param group_id value
     */
    public void setGroup_id(String group_id)
    {
        this.group_id = group_id;
    }

    /**
     * Get Link
     * 
     * @return link_id value
     */
    public String getLink_id()
    {
        return this.link_id;
    }

    /**
     * Set Link
     * 
     * @param link_id value
     */
    public void setLink_id(String link_id)
    {
        this.link_id = link_id;
    }

    /** 
     * Custom equals implementation
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof InetLinkGroupH))
            return false;
        InetLinkGroupH castOther = (InetLinkGroupH) other;
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
