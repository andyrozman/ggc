package ggc.core.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MealH implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8237292183858746065L;

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
    private String parts;

    /** nullable persistent field */
    private String nutritions;

    /** nullable persistent field */
    private String extended;

    /** nullable persistent field */
    private String comment;

    /** nullable persistent field */
    private long changed;

    /** full constructor */
    public MealH(long group_id, String name, String name_i18n, String description, String parts, String nutritions, String extended, String comment, long changed) {
        this.group_id = group_id;
        this.name = name;
        this.name_i18n = name_i18n;
        this.description = description;
        this.parts = parts;
        this.nutritions = nutritions;
        this.extended = extended;
        this.comment = comment;
        this.changed = changed;
    }

    /** default constructor */
    public MealH() {
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

    public String getParts() {
        return this.parts;
    }

    public void setParts(String parts) {
        this.parts = parts;
    }

    public String getNutritions() {
        return this.nutritions;
    }

    public void setNutritions(String nutritions) {
        this.nutritions = nutritions;
    }

    public String getExtended() {
        return this.extended;
    }

    public void setExtended(String extended) {
        this.extended = extended;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
