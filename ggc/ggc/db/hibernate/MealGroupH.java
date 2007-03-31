package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MealGroupH implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String name_18n;

    /** nullable persistent field */
    private int parent_id;

    /** full constructor */
    public MealGroupH(long id, String name, String name_18n, int parent_id) {
        this.id = id;
        this.name = name;
        this.name_18n = name_18n;
        this.parent_id = parent_id;
    }

    /** default constructor */
    public MealGroupH() {
    }

    /** minimal constructor */
    public MealGroupH(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_18n() {
        return this.name_18n;
    }

    public void setName_18n(String name_18n) {
        this.name_18n = name_18n;
    }

    public int getParent_id() {
        return this.parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MealGroupH) ) return false;
        MealGroupH castOther = (MealGroupH) other;
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
