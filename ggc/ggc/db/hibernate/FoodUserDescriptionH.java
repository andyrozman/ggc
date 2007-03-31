package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FoodUserDescriptionH implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private long food_user_group_id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String i18n_name;

    /** nullable persistent field */
    private float refuse;

    /** nullable persistent field */
    private String nutritions;

    /** nullable persistent field */
    private String home_weights;

    /** full constructor */
    public FoodUserDescriptionH(long food_user_group_id, String name, String i18n_name, float refuse, String nutritions, String home_weights) {
        this.food_user_group_id = food_user_group_id;
        this.name = name;
        this.i18n_name = i18n_name;
        this.refuse = refuse;
        this.nutritions = nutritions;
        this.home_weights = home_weights;
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

    public long getFood_user_group_id() {
        return this.food_user_group_id;
    }

    public void setFood_user_group_id(long food_user_group_id) {
        this.food_user_group_id = food_user_group_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getI18n_name() {
        return this.i18n_name;
    }

    public void setI18n_name(String i18n_name) {
        this.i18n_name = i18n_name;
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
