package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FoodGroupH implements Serializable {

    /** identifier field */
    private int id;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private String description_i18n;

    /** full constructor */
    public FoodGroupH(int id, String description, String description_i18n) {
        this.id = id;
        this.description = description;
        this.description_i18n = description_i18n;
    }

    /** default constructor */
    public FoodGroupH() {
    }

    /** minimal constructor */
    public FoodGroupH(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription_i18n() {
        return this.description_i18n;
    }

    public void setDescription_i18n(String description_i18n) {
        this.description_i18n = description_i18n;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof FoodGroupH) ) return false;
        FoodGroupH castOther = (FoodGroupH) other;
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
