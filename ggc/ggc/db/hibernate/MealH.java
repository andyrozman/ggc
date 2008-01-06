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
    private String name;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private String parts;

    /** nullable persistent field */
    private String req_values;

    /** nullable persistent field */
    private String extended;

    /** nullable persistent field */
    private String comment;

    /** full constructor */
    public MealH(long id, long meal_group_id, String name, String description, String parts, String req_values, String extended, String comment) {
        this.id = id;
        this.meal_group_id = meal_group_id;
        this.name = name;
        this.description = description;
        this.parts = parts;
        this.req_values = req_values;
        this.extended = extended;
        this.comment = comment;
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

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParts() {
        return this.parts;
    }

    public void setParts(String parts) {
        this.parts = parts;
    }

    public String getReq_values() {
        return this.req_values;
    }

    public void setReq_values(String req_values) {
        this.req_values = req_values;
    }

    public String getExtended() {
        return this.extended;
    }

    public void setExtended(String extended) {
        this.extended = extended;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
