package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class SettingsMainH implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String ins1_name;

    /** nullable persistent field */
    private String ins1_abbr;

    /** nullable persistent field */
    private String ins2_name;

    /** nullable persistent field */
    private String ins2_abbr;

    /** nullable persistent field */
    private int meter_type;

    /** nullable persistent field */
    private String meter_port;

    /** nullable persistent field */
    private float bg_low;

    /** nullable persistent field */
    private float bg_high;

    /** nullable persistent field */
    private float bg_target_high;

    /** nullable persistent field */
    private float bg_target_low;

    /** nullable persistent field */
    private int bg_unit;

    /** nullable persistent field */
    private int laf_type;

    /** nullable persistent field */
    private int laf_name;

    /** nullable persistent field */
    private int render_rendering;

    /** nullable persistent field */
    private int render_dithering;

    /** nullable persistent field */
    private int render_interpolation;

    /** nullable persistent field */
    private int render_antialiasing;

    /** nullable persistent field */
    private int render_textantialiasing;

    /** nullable persistent field */
    private int render_colorrendering;

    /** nullable persistent field */
    private int render_fractionalmetrics;

    /** nullable persistent field */
    private int color_scheme;

    /** full constructor */
    public SettingsMainH(String name, String ins1_name, String ins1_abbr, String ins2_name, String ins2_abbr, int meter_type, String meter_port, float bg_low, float bg_high, float bg_target_high, float bg_target_low, int bg_unit, int laf_type, int laf_name, int render_rendering, int render_dithering, int render_interpolation, int render_antialiasing, int render_textantialiasing, int render_colorrendering, int render_fractionalmetrics, int color_scheme) {
        this.name = name;
        this.ins1_name = ins1_name;
        this.ins1_abbr = ins1_abbr;
        this.ins2_name = ins2_name;
        this.ins2_abbr = ins2_abbr;
        this.meter_type = meter_type;
        this.meter_port = meter_port;
        this.bg_low = bg_low;
        this.bg_high = bg_high;
        this.bg_target_high = bg_target_high;
        this.bg_target_low = bg_target_low;
        this.bg_unit = bg_unit;
        this.laf_type = laf_type;
        this.laf_name = laf_name;
        this.render_rendering = render_rendering;
        this.render_dithering = render_dithering;
        this.render_interpolation = render_interpolation;
        this.render_antialiasing = render_antialiasing;
        this.render_textantialiasing = render_textantialiasing;
        this.render_colorrendering = render_colorrendering;
        this.render_fractionalmetrics = render_fractionalmetrics;
        this.color_scheme = color_scheme;
    }

    /** default constructor */
    public SettingsMainH() {
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

    public String getIns1_name() {
        return this.ins1_name;
    }

    public void setIns1_name(String ins1_name) {
        this.ins1_name = ins1_name;
    }

    public String getIns1_abbr() {
        return this.ins1_abbr;
    }

    public void setIns1_abbr(String ins1_abbr) {
        this.ins1_abbr = ins1_abbr;
    }

    public String getIns2_name() {
        return this.ins2_name;
    }

    public void setIns2_name(String ins2_name) {
        this.ins2_name = ins2_name;
    }

    public String getIns2_abbr() {
        return this.ins2_abbr;
    }

    public void setIns2_abbr(String ins2_abbr) {
        this.ins2_abbr = ins2_abbr;
    }

    public int getMeter_type() {
        return this.meter_type;
    }

    public void setMeter_type(int meter_type) {
        this.meter_type = meter_type;
    }

    public String getMeter_port() {
        return this.meter_port;
    }

    public void setMeter_port(String meter_port) {
        this.meter_port = meter_port;
    }

    public float getBg_low() {
        return this.bg_low;
    }

    public void setBg_low(float bg_low) {
        this.bg_low = bg_low;
    }

    public float getBg_high() {
        return this.bg_high;
    }

    public void setBg_high(float bg_high) {
        this.bg_high = bg_high;
    }

    public float getBg_target_high() {
        return this.bg_target_high;
    }

    public void setBg_target_high(float bg_target_high) {
        this.bg_target_high = bg_target_high;
    }

    public float getBg_target_low() {
        return this.bg_target_low;
    }

    public void setBg_target_low(float bg_target_low) {
        this.bg_target_low = bg_target_low;
    }

    public int getBg_unit() {
        return this.bg_unit;
    }

    public void setBg_unit(int bg_unit) {
        this.bg_unit = bg_unit;
    }

    public int getLaf_type() {
        return this.laf_type;
    }

    public void setLaf_type(int laf_type) {
        this.laf_type = laf_type;
    }

    public int getLaf_name() {
        return this.laf_name;
    }

    public void setLaf_name(int laf_name) {
        this.laf_name = laf_name;
    }

    public int getRender_rendering() {
        return this.render_rendering;
    }

    public void setRender_rendering(int render_rendering) {
        this.render_rendering = render_rendering;
    }

    public int getRender_dithering() {
        return this.render_dithering;
    }

    public void setRender_dithering(int render_dithering) {
        this.render_dithering = render_dithering;
    }

    public int getRender_interpolation() {
        return this.render_interpolation;
    }

    public void setRender_interpolation(int render_interpolation) {
        this.render_interpolation = render_interpolation;
    }

    public int getRender_antialiasing() {
        return this.render_antialiasing;
    }

    public void setRender_antialiasing(int render_antialiasing) {
        this.render_antialiasing = render_antialiasing;
    }

    public int getRender_textantialiasing() {
        return this.render_textantialiasing;
    }

    public void setRender_textantialiasing(int render_textantialiasing) {
        this.render_textantialiasing = render_textantialiasing;
    }

    public int getRender_colorrendering() {
        return this.render_colorrendering;
    }

    public void setRender_colorrendering(int render_colorrendering) {
        this.render_colorrendering = render_colorrendering;
    }

    public int getRender_fractionalmetrics() {
        return this.render_fractionalmetrics;
    }

    public void setRender_fractionalmetrics(int render_fractionalmetrics) {
        this.render_fractionalmetrics = render_fractionalmetrics;
    }

    public int getColor_scheme() {
        return this.color_scheme;
    }

    public void setColor_scheme(int color_scheme) {
        this.color_scheme = color_scheme;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof SettingsMainH) ) return false;
        SettingsMainH castOther = (SettingsMainH) other;
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
