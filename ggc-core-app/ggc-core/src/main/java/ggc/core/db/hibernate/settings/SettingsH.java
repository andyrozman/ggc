package ggc.core.db.hibernate.settings;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.atech.db.hibernate.HibernateObject;

/** @author Hibernate CodeGenerator */
public class SettingsH extends HibernateObject
{

    private static final long serialVersionUID = 2231322090212148293L;

    private long id;
    private String key;
    private String value;
    private int type;
    private String description;
    private int personId;


    /** full constructor 
     * @param key 
     * @param value 
     * @param type 
     * @param description 
     * @param personId */
    public SettingsH(String key, String value, int type, String description, int personId)
    {
        this.key = key;
        this.value = value;
        this.type = type;
        this.description = description;
        this.personId = personId;
    }


    /** default constructor */
    public SettingsH()
    {
    }


    /** minimal constructor 
     * @param key 
     * @param personId */
    public SettingsH(String key, int personId)
    {
        this.key = key;
        this.personId = personId;
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
     * Get Person Id
     * 
     * @return person id parameter
     */
    public int getPersonId()
    {
        return this.personId;
    }


    /**
     * Set Person Id
     * 
     * @param personId parameter
     */
    public void setPersonId(int personId)
    {
        this.personId = personId;
    }


    /** 
     * Custom equals implementation
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof SettingsH))
            return false;
        SettingsH castOther = (SettingsH) other;
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
