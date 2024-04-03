package ggc.core.db.hibernate.food;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.atech.db.hibernate.HibernateObject;

public class FoodDescriptionH extends HibernateObject implements Serializable
{

    private static final long serialVersionUID = 7477410336917496421L;

    private long id;
    private long groupId;
    private String name;
    private String nameI18n;
    private float refuse;
    private String nutritions;
    private String homeWeights;


    /** full constructor 
     * @param groupId
     * @param name 
     * @param nameI18n
     * @param refuse 
     * @param nutritions 
     * @param homeWeights */
    public FoodDescriptionH(long groupId, String name, String nameI18n, float refuse, String nutritions,
            String homeWeights)
    {
        this.groupId = groupId;
        this.name = name;
        this.nameI18n = nameI18n;
        this.refuse = refuse;
        this.nutritions = nutritions;
        this.homeWeights = homeWeights;
    }


    /** default constructor */
    public FoodDescriptionH()
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
     * Custom equals implementation
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof FoodDescriptionH))
            return false;
        FoodDescriptionH castOther = (FoodDescriptionH) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

}
