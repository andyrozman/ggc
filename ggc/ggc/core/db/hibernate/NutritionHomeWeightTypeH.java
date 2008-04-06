package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class NutritionHomeWeightTypeH implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private int static_entry;

    /** full constructor */
    public NutritionHomeWeightTypeH(String name, int static_entry) {
        this.name = name;
        this.static_entry = static_entry;
    }

    /** default constructor */
    public NutritionHomeWeightTypeH() {
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

    public int getStatic_entry() {
        return this.static_entry;
    }

    public void setStatic_entry(int static_entry) {
        this.static_entry = static_entry;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof NutritionHomeWeightTypeH) ) return false;
        NutritionHomeWeightTypeH castOther = (NutritionHomeWeightTypeH) other;
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
