package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class FoodDescriptionH implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private int food_group_id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String i18n_name;

    /** nullable persistent field */
    private float refuse;

    /** nullable persistent field */
    private float fat_g;

    /** nullable persistent field */
    private float CH_g;

    /** nullable persistent field */
    private float energy_kcal;

    /** nullable persistent field */
    private float energy_kJ;

    /** nullable persistent field */
    private float sugar_g;

    /** full constructor */
    public FoodDescriptionH(long id, int food_group_id, String name, String i18n_name, float refuse, float fat_g, float CH_g, float energy_kcal, float energy_kJ, float sugar_g) {
        this.id = id;
        this.food_group_id = food_group_id;
        this.name = name;
        this.i18n_name = i18n_name;
        this.refuse = refuse;
        this.fat_g = fat_g;
        this.CH_g = CH_g;
        this.energy_kcal = energy_kcal;
        this.energy_kJ = energy_kJ;
        this.sugar_g = sugar_g;
    }

    /** default constructor */
    public FoodDescriptionH() {
    }

    /** minimal constructor */
    public FoodDescriptionH(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFood_group_id() {
        return this.food_group_id;
    }

    public void setFood_group_id(int food_group_id) {
        this.food_group_id = food_group_id;
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

    public float getFat_g() {
        return this.fat_g;
    }

    public void setFat_g(float fat_g) {
        this.fat_g = fat_g;
    }

    public float getCH_g() {
        return this.CH_g;
    }

    public void setCH_g(float CH_g) {
        this.CH_g = CH_g;
    }

    public float getEnergy_kcal() {
        return this.energy_kcal;
    }

    public void setEnergy_kcal(float energy_kcal) {
        this.energy_kcal = energy_kcal;
    }

    public float getEnergy_kJ() {
        return this.energy_kJ;
    }

    public void setEnergy_kJ(float energy_kJ) {
        this.energy_kJ = energy_kJ;
    }

    public float getSugar_g() {
        return this.sugar_g;
    }

    public void setSugar_g(float sugar_g) {
        this.sugar_g = sugar_g;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof FoodDescriptionH) ) return false;
        FoodDescriptionH castOther = (FoodDescriptionH) other;
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
