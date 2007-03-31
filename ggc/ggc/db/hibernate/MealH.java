package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MealH implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private long meal_group_id;

    /** nullable persistent field */
    private int type;

    /** nullable persistent field */
    private long food_id;

    /** full constructor */
    public MealH(long id, long meal_group_id, int type, long food_id) {
        this.id = id;
        this.meal_group_id = meal_group_id;
        this.type = type;
        this.food_id = food_id;
    }

    /** default constructor */
    public MealH() {
    }

    /** minimal constructor */
    public MealH(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMeal_group_id() {
        return this.meal_group_id;
    }

    public void setMeal_group_id(long meal_group_id) {
        this.meal_group_id = meal_group_id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getFood_id() {
        return this.food_id;
    }

    public void setFood_id(long food_id) {
        this.food_id = food_id;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MealH) ) return false;
        MealH castOther = (MealH) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getId())
            .toHashCode();
    }

}
