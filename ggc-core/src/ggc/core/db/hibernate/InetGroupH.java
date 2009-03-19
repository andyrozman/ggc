package ggc.core.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class InetGroupH implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2162686900534669008L;

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String parent;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String name_i18n;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private String comment;

    /** full constructor 
     * @param parent 
     * @param name 
     * @param name_i18n 
     * @param description 
     * @param comment */
    public InetGroupH(String parent, String name, String name_i18n, String description, String comment) {
        this.parent = parent;
        this.name = name;
        this.name_i18n = name_i18n;
        this.description = description;
        this.comment = comment;
    }

    /** default constructor */
    public InetGroupH() {
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
     * Get Parent Id
     * 
     * @return parent_id parameter
     */
    public String getParent() 
    {
        return this.parent;
    }

    /**
     * Set Parent Id
     * 
     * @param parent parameter
     */
    public void setParent(String parent) 
    {
        this.parent = parent;
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
     * Get Name (I18n)
     * 
     * @return name
     */
    public String getName_i18n() 
    {
        return this.name_i18n;
    }

    /**
     * Set Name (I18n)
     * 
     * @param name_i18n as string
     */
    public void setName_i18n(String name_i18n) 
    {
        this.name_i18n = name_i18n;
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
        if ( !(other instanceof InetGroupH) ) return false;
        InetGroupH castOther = (InetGroupH) other;
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
