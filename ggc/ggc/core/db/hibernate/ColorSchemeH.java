package ggc.core.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class ColorSchemeH implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private int custom_type;

    /** nullable persistent field */
    private int color_bg;

    /** nullable persistent field */
    private int color_bg_avg;

    /** nullable persistent field */
    private int color_bg_low;

    /** nullable persistent field */
    private int color_bg_high;

    /** nullable persistent field */
    private int color_bg_target;

    /** nullable persistent field */
    private int color_ins;

    /** nullable persistent field */
    private int color_ins1;

    /** nullable persistent field */
    private int color_ins2;

    /** nullable persistent field */
    private int color_ins_perbu;

    /** nullable persistent field */
    private int color_ch;

    /** full constructor */
    public ColorSchemeH(String name, int custom_type, int color_bg, int color_bg_avg, int color_bg_low, int color_bg_high, int color_bg_target, int color_ins, int color_ins1, int color_ins2, int color_ins_perbu, int color_ch) {
        this.name = name;
        this.custom_type = custom_type;
        this.color_bg = color_bg;
        this.color_bg_avg = color_bg_avg;
        this.color_bg_low = color_bg_low;
        this.color_bg_high = color_bg_high;
        this.color_bg_target = color_bg_target;
        this.color_ins = color_ins;
        this.color_ins1 = color_ins1;
        this.color_ins2 = color_ins2;
        this.color_ins_perbu = color_ins_perbu;
        this.color_ch = color_ch;
    }

    /** default constructor */
    public ColorSchemeH() {
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

    public int getCustom_type() {
        return this.custom_type;
    }

    public void setCustom_type(int custom_type) {
        this.custom_type = custom_type;
    }

    public int getColor_bg() {
        return this.color_bg;
    }

    public void setColor_bg(int color_bg) {
        this.color_bg = color_bg;
    }

    public int getColor_bg_avg() {
        return this.color_bg_avg;
    }

    public void setColor_bg_avg(int color_bg_avg) {
        this.color_bg_avg = color_bg_avg;
    }

    public int getColor_bg_low() {
        return this.color_bg_low;
    }

    public void setColor_bg_low(int color_bg_low) {
        this.color_bg_low = color_bg_low;
    }

    public int getColor_bg_high() {
        return this.color_bg_high;
    }

    public void setColor_bg_high(int color_bg_high) {
        this.color_bg_high = color_bg_high;
    }

    public int getColor_bg_target() {
        return this.color_bg_target;
    }

    public void setColor_bg_target(int color_bg_target) {
        this.color_bg_target = color_bg_target;
    }

    public int getColor_ins() {
        return this.color_ins;
    }

    public void setColor_ins(int color_ins) {
        this.color_ins = color_ins;
    }

    public int getColor_ins1() {
        return this.color_ins1;
    }

    public void setColor_ins1(int color_ins1) {
        this.color_ins1 = color_ins1;
    }

    public int getColor_ins2() {
        return this.color_ins2;
    }

    public void setColor_ins2(int color_ins2) {
        this.color_ins2 = color_ins2;
    }

    public int getColor_ins_perbu() {
        return this.color_ins_perbu;
    }

    public void setColor_ins_perbu(int color_ins_perbu) {
        this.color_ins_perbu = color_ins_perbu;
    }

    public int getColor_ch() {
        return this.color_ch;
    }

    public void setColor_ch(int color_ch) {
        this.color_ch = color_ch;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof ColorSchemeH) ) return false;
        ColorSchemeH castOther = (ColorSchemeH) other;
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
