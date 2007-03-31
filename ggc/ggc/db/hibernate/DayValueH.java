package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class DayValueH implements Serializable {

    /** identifier field */
    private long id;

    /** persistent field */
    private long dt_info;

    /** nullable persistent field */
    private int bg;

    /** nullable persistent field */
    private int ins1;

    /** nullable persistent field */
    private int ins2;

    /** nullable persistent field */
    private float ch;

    /** nullable persistent field */
    private String meals_ids;

    /** nullable persistent field */
    private String act;

    /** nullable persistent field */
    private String extended;

    /** nullable persistent field */
    private String comment;

    /** full constructor */
    public DayValueH(long dt_info, int bg, int ins1, int ins2, float ch, String meals_ids, String act, String extended, String comment) {
        this.dt_info = dt_info;
        this.bg = bg;
        this.ins1 = ins1;
        this.ins2 = ins2;
        this.ch = ch;
        this.meals_ids = meals_ids;
        this.act = act;
        this.extended = extended;
        this.comment = comment;
    }

    /** default constructor */
    public DayValueH() {
    }

    /** minimal constructor */
    public DayValueH(long dt_info) {
        this.dt_info = dt_info;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDt_info() {
        return this.dt_info;
    }

    public void setDt_info(long dt_info) {
        this.dt_info = dt_info;
    }

    public int getBg() {
        return this.bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public int getIns1() {
        return this.ins1;
    }

    public void setIns1(int ins1) {
        this.ins1 = ins1;
    }

    public int getIns2() {
        return this.ins2;
    }

    public void setIns2(int ins2) {
        this.ins2 = ins2;
    }

    public float getCh() {
        return this.ch;
    }

    public void setCh(float ch) {
        this.ch = ch;
    }

    public String getMeals_ids() {
        return this.meals_ids;
    }

    public void setMeals_ids(String meals_ids) {
        this.meals_ids = meals_ids;
    }

    public String getAct() {
        return this.act;
    }

    public void setAct(String act) {
        this.act = act;
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

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof DayValueH) ) return false;
        DayValueH castOther = (DayValueH) other;
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
