package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MealGroupH implements Serializable
{

    /** identifier field */
    private int id;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private int type;

    /** full constructor */
    public MealGroupH(int id, String description, int type)
    {
        this.id = id;
        this.description = description;
        this.type = type;
    }

    /** default constructor */
    public MealGroupH()
    {
    }

    /** minimal constructor */
    public MealGroupH(int id)
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

    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getType()
    {
        return this.type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
        .append("id", getId())
        .toString();
    }

    @Override
    public boolean equals(Object other)
    {
        if ( !(other instanceof MealGroupH) ) return false;
        MealGroupH castOther = (MealGroupH) other;
        return new EqualsBuilder()
        .append(this.getId(), castOther.getId())
        .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
        .append(getId())
        .toHashCode();
    }

}
