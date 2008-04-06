package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class NutritionDefinitionH implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String weight_unit;

    /** nullable persistent field */
    private String tag;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String decimal_places;

    /** nullable persistent field */
    private int static_entry;

    /** full constructor */
    public NutritionDefinitionH(String weight_unit, String tag, String name, String decimal_places, int static_entry) {
        this.weight_unit = weight_unit;
        this.tag = tag;
        this.name = name;
        this.decimal_places = decimal_places;
        this.static_entry = static_entry;
    }

    /** default constructor */
    public NutritionDefinitionH() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWeight_unit() {
        return this.weight_unit;
    }

    public void setWeight_unit(String weight_unit) {
        this.weight_unit = weight_unit;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDecimal_places() {
        return this.decimal_places;
    }

    public void setDecimal_places(String decimal_places) {
        this.decimal_places = decimal_places;
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
        if ( !(other instanceof NutritionDefinitionH) ) return false;
        NutritionDefinitionH castOther = (NutritionDefinitionH) other;
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
