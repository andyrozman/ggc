package ggc.core.db.hibernate.food;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.atech.db.hibernate.HibernateObject;

public class FoodUserDescriptionH extends HibernateObject
{

    private static final long serialVersionUID = -4807400056386914224L;

    private long id;
    private long groupId;
    private String name;
    private String nameI18n;
    private String description;
    private float refuse;
    private String nutritions;
    private String homeWeights;
    private long changed;


    /** full constructor 
     * @param groupId
     * @param name 
     * @param nameI18n
     * @param description 
     * @param refuse 
     * @param nutritions 
     * @param homeWeights
     * @param changed */
    public FoodUserDescriptionH(long groupId, String name, String nameI18n, String description, float refuse,
            String nutritions, String homeWeights, long changed)
    {
        this.groupId = groupId;
        this.name = name;
        this.nameI18n = nameI18n;
        this.description = description;
        this.refuse = refuse;
        this.nutritions = nutritions;
        this.homeWeights = homeWeights;
        this.changed = changed;
    }


    /** default constructor */
    public FoodUserDescriptionH()
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
     * Get Refuse
     * 
     * @return refuse value
     */
    public float getRefuse()
    {
        return this.refuse;
    }


    /**
     * Set Refuse
     * 
     * @param refuse value
     */
    public void setRefuse(float refuse)
    {
        this.refuse = refuse;
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
     * Get Home Weights
     * 
     * @return homeWeights parameter
     */
    public String getHomeWeights()
    {
        return this.homeWeights;
    }


    /**
     * Set Home Weights
     * 
     * @param homeWeights parameter
     */
    public void setHomeWeights(String homeWeights)
    {
        this.homeWeights = homeWeights;
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
        if (!(other instanceof FoodUserDescriptionH))
            return false;
        FoodUserDescriptionH castOther = (FoodUserDescriptionH) other;
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
