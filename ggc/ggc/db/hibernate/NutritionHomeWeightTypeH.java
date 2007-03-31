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

    /** full constructor */
    public NutritionHomeWeightTypeH(String name) {
        this.name = name;
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
