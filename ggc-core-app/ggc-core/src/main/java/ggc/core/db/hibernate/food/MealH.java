package ggc.core.db.hibernate.food;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.atech.db.hibernate.HibernateObject;

public class MealH extends HibernateObject implements Serializable
{

    private static final long serialVersionUID = 8237292183858746065L;

    private long id;
    private long groupId;
    private String name;
    private String nameI18n;
    private String description;
    private String parts;
    private String nutritions;
    private String extended;
    private String comment;
    private long changed;


    /** full constructor 
     * @param groupId
     * @param name 
     * @param nameI18n
     * @param description 
     * @param parts 
     * @param nutritions 
     * @param extended 
     * @param comment 
     * @param changed */
    public MealH(long groupId, String name, String nameI18n, String description, String parts, String nutritions,
            String extended, String comment, long changed)
    {
        this.groupId = groupId;
        this.name = name;
        this.nameI18n = nameI18n;
        this.description = description;
        this.parts = parts;
        this.nutritions = nutritions;
        this.extended = extended;
        this.comment = comment;
        this.changed = changed;
    }


    /** default constructor */
    public MealH()
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
    public long getGroupId()
    {
        return this.groupId;
    }


    /**
     * Set Group Id
     * 
     * @param groupId value
     */
    public void setGroupId(long groupId)
    {
        this.groupId = groupId;
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
    public String getNameI18n()
    {
        return this.nameI18n;
    }


    /**
     * Set Name (I18n)
     * 
     * @param nameI18n as string
     */
    public void setNameI18n(String nameI18n)
    {
        this.nameI18n = nameI18n;
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
     * Get Parts
     * 
     * @return parts value
     */
    public String getParts()
    {
        return this.parts;
    }


    /**
     * Set Parts
     * 
     * @param parts value
     */
    public void setParts(String parts)
    {
        this.parts = parts;
    }


    /**
     * Get Nutritions
     * 
     * @return nutritions value
     */
    public String getNutritions()
    {
        return this.nutritions;
    }


    /**
     * Set Nutritions
     * 
     * @param nutritions value
     */
    public void setNutritions(String nutritions)
    {
        this.nutritions = nutritions;
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


    /**
     * Get Changed
     * 
     * @return changed value
     */
    public long getChanged()
    {
        return this.changed;
    }


    /**
     * Set Changed
     * 
     * @param changed parameter
     */
    public void setChanged(long changed)
    {
        this.changed = changed;
    }


    /** 
     * Custom equals implementation
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof MealH))
            return false;
        MealH castOther = (MealH) other;
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
