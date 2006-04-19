package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MealFoodH implements Serializable
{

    /** identifier field */
    private int id;

    /** nullable persistent field */
    private long meal_id;

    /** nullable persistent field */
    private int type;

    /** nullable persistent field */
    private long food_id;

    /** full constructor */
    public MealFoodH(int id, long meal_id, int type, long food_id)
    {
        this.id = id;
        this.meal_id = meal_id;
        this.type = type;
        this.food_id = food_id;
    }

    /** default constructor */
    public MealFoodH()
    {
    }

    /** minimal constructor */
    public MealFoodH(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public long getMeal_id()
    {
        return this.meal_id;
    }

    public void setMeal_id(long meal_id)
    {
        this.meal_id = meal_id;
    }

    public int getType()
    {
        return this.type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public long getFood_id()
    {
        return this.food_id;
    }

    public void setFood_id(long food_id)
    {
        this.food_id = food_id;
    }

    public String toString()
    {
        return new ToStringBuilder(this)
        .append("id", getId())
        .toString();
    }

    public boolean equals(Object other)
    {
        if ( !(other instanceof MealFoodH) ) return false;
        MealFoodH castOther = (MealFoodH) other;
        return new EqualsBuilder()
        .append(this.getId(), castOther.getId())
        .isEquals();
    }

    public int hashCode()
    {
        return new HashCodeBuilder()
        .append(getId())
        .toHashCode();
    }

}
