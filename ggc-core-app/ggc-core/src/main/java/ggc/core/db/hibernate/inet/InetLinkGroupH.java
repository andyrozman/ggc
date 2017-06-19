package ggc.core.db.hibernate.inet;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.atech.db.hibernate.HibernateObject;

/** @author Hibernate CodeGenerator */
public class InetLinkGroupH extends HibernateObject
{

    private static final long serialVersionUID = 8120450189760694491L;

    private long id;
    private String groupId;
    private String linkId;


    /** full constructor 
     * @param groupId
     * @param linkId */
    public InetLinkGroupH(String groupId, String linkId)
    {
        this.groupId = groupId;
        this.linkId = linkId;
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
     * @return groupId value
     */
    public String getGroupId()
    {
        return this.groupId;
    }


    /**
     * Set Group Id
     * 
     * @param groupId value
     */
    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }


    /**
     * Get Link
     * 
     * @return linkId value
     */
    public String getLinkId()
    {
        return this.linkId;
    }


    /**
     * Set Link
     * 
     * @param linkId value
     */
    public void setLinkId(String linkId)
    {
        this.linkId = linkId;
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
