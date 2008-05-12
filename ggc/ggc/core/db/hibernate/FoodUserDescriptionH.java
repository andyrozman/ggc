package ggc.core.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FoodUserDescriptionH implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private long group_id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String name_i18n;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private float refuse;

    /** nullable persistent field */
    private String nutritions;

    /** nullable persistent field */
    private String home_weights;

    /** nullable persistent field */
    private long changed;

    /** full constructor */
    public FoodUserDescriptionH(long group_id, String name, String name_i18n, String description, float refuse, String nutritions, String home_weights, long changed) {
        this.group_id = group_id;
        this.name = name;
        this.name_i18n = name_i18n;
        this.description = description;
        this.refuse = refuse;
        this.nutritions = nutritions;
        this.home_weights = home_weights;
        this.changed = changed;
    }

    /** default constructor */
    public FoodUserDescriptionH() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGroup_id() {
        return this.group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_i18n() {
        return this.name_i18n;
    }

    public void setName_i18n(String name_i18n) {
        this.name_i18n = name_i18n;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getRefuse() {
        return this.refuse;
    }

    public void setRefuse(float refuse) {
        this.refuse = refuse;
    }

    public String getNutritions() {
        return this.nutritions;
    }

    public void setNutritions(String nutritions) {
        this.nutritions = nutritions;
    }

    public String getHome_weights() {
        return this.home_weights;
    }

    public void setHome_weights(String home_weights) {
        this.home_weights = home_weights;
    }

    public long getChanged() {
        return this.changed;
    }

    public void setChanged(long changed) {
        this.changed = changed;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof FoodUserDescriptionH) ) return false;
        FoodUserDescriptionH castOther = (FoodUserDescriptionH) other;
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
