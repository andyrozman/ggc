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
    private int meal_group;

    /** nullable persistent field */
    private String name;

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
    public MealH(long id, int meal_group, String name, float fat_g, float CH_g, float energy_kcal, float energy_kJ, float sugar_g) {
        this.id = id;
        this.meal_group = meal_group;
        this.name = name;
        this.fat_g = fat_g;
        this.CH_g = CH_g;
        this.energy_kcal = energy_kcal;
        this.energy_kJ = energy_kJ;
        this.sugar_g = sugar_g;
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

    public int getMeal_group() {
        return this.meal_group;
    }

    public void setMeal_group(int meal_group) {
        this.meal_group = meal_group;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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
