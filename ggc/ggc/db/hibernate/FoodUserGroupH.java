package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FoodUserGroupH implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private String description_i18n;

    /** nullable persistent field */
    private long parent_id;

    /** full constructor */
    public FoodUserGroupH(String description, String description_i18n, long parent_id) {
        this.description = description;
        this.description_i18n = description_i18n;
        this.parent_id = parent_id;
    }

    /** default constructor */
    public FoodUserGroupH() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
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

    public long getParent_id() {
        return this.parent_id;
    }

    public void setParent_id(long parent_id) {
        this.parent_id = parent_id;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof FoodUserGroupH) ) return false;
        FoodUserGroupH castOther = (FoodUserGroupH) other;
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
