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
    private int bg_unit;

    /** nullable persistent field */
    private float bg1_low;

    /** nullable persistent field */
    private float bg1_high;

    /** nullable persistent field */
    private float bg1_target_low;

    /** nullable persistent field */
    private float bg1_target_high;

    /** nullable persistent field */
    private float bg2_low;

    /** nullable persistent field */
    private float bg2_high;

    /** nullable persistent field */
    private float bg2_target_low;

    /** nullable persistent field */
    private float bg2_target_high;

    /** nullable persistent field */
    private int laf_type;

    /** nullable persistent field */
    private String laf_name;

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
    private String print_pdf_viewer_path;

    /** nullable persistent field */
    private int print_lunch_start_time;

    /** nullable persistent field */
    private int print_dinner_start_time;

    /** nullable persistent field */
    private int print_night_start_time;

    /** nullable persistent field */
    private String print_empty_value;

    /** nullable persistent field */
    private String color_scheme;

    /** full constructor */
    public SettingsMainH(String name, String ins1_name, String ins1_abbr, String ins2_name, String ins2_abbr, int meter_type, String meter_port, int bg_unit, float bg1_low, float bg1_high, float bg1_target_low, float bg1_target_high, float bg2_low, float bg2_high, float bg2_target_low, float bg2_target_high, int laf_type, String laf_name, int render_rendering, int render_dithering, int render_interpolation, int render_antialiasing, int render_textantialiasing, int render_colorrendering, int render_fractionalmetrics, String print_pdf_viewer_path, int print_lunch_start_time, int print_dinner_start_time, int print_night_start_time, String print_empty_value, String color_scheme) {
        this.name = name;
        this.ins1_name = ins1_name;
        this.ins1_abbr = ins1_abbr;
        this.ins2_name = ins2_name;
        this.ins2_abbr = ins2_abbr;
        this.meter_type = meter_type;
        this.meter_port = meter_port;
        this.bg_unit = bg_unit;
        this.bg1_low = bg1_low;
        this.bg1_high = bg1_high;
        this.bg1_target_low = bg1_target_low;
        this.bg1_target_high = bg1_target_high;
        this.bg2_low = bg2_low;
        this.bg2_high = bg2_high;
        this.bg2_target_low = bg2_target_low;
        this.bg2_target_high = bg2_target_high;
        this.laf_type = laf_type;
        this.laf_name = laf_name;
        this.render_rendering = render_rendering;
        this.render_dithering = render_dithering;
        this.render_interpolation = render_interpolation;
        this.render_antialiasing = render_antialiasing;
        this.render_textantialiasing = render_textantialiasing;
        this.render_colorrendering = render_colorrendering;
        this.render_fractionalmetrics = render_fractionalmetrics;
        this.print_pdf_viewer_path = print_pdf_viewer_path;
        this.print_lunch_start_time = print_lunch_start_time;
        this.print_dinner_start_time = print_dinner_start_time;
        this.print_night_start_time = print_night_start_time;
        this.print_empty_value = print_empty_value;
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

    public int getBg_unit() {
        return this.bg_unit;
    }

    public void setBg_unit(int bg_unit) {
        this.bg_unit = bg_unit;
    }

    public float getBg1_low() {
        return this.bg1_low;
    }

    public void setBg1_low(float bg1_low) {
        this.bg1_low = bg1_low;
    }

    public float getBg1_high() {
        return this.bg1_high;
    }

    public void setBg1_high(float bg1_high) {
        this.bg1_high = bg1_high;
    }

    public float getBg1_target_low() {
        return this.bg1_target_low;
    }

    public void setBg1_target_low(float bg1_target_low) {
        this.bg1_target_low = bg1_target_low;
    }

    public float getBg1_target_high() {
        return this.bg1_target_high;
    }

    public void setBg1_target_high(float bg1_target_high) {
        this.bg1_target_high = bg1_target_high;
    }

    public float getBg2_low() {
        return this.bg2_low;
    }

    public void setBg2_low(float bg2_low) {
        this.bg2_low = bg2_low;
    }

    public float getBg2_high() {
        return this.bg2_high;
    }

    public void setBg2_high(float bg2_high) {
        this.bg2_high = bg2_high;
    }

    public float getBg2_target_low() {
        return this.bg2_target_low;
    }

    public void setBg2_target_low(float bg2_target_low) {
        this.bg2_target_low = bg2_target_low;
    }

    public float getBg2_target_high() {
        return this.bg2_target_high;
    }

    public void setBg2_target_high(float bg2_target_high) {
        this.bg2_target_high = bg2_target_high;
    }

    public int getLaf_type() {
        return this.laf_type;
    }

    public void setLaf_type(int laf_type) {
        this.laf_type = laf_type;
    }

    public String getLaf_name() {
        return this.laf_name;
    }

    public void setLaf_name(String laf_name) {
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

    public String getPrint_pdf_viewer_path() {
        return this.print_pdf_viewer_path;
    }

    public void setPrint_pdf_viewer_path(String print_pdf_viewer_path) {
        this.print_pdf_viewer_path = print_pdf_viewer_path;
    }

    public int getPrint_lunch_start_time() {
        return this.print_lunch_start_time;
    }

    public void setPrint_lunch_start_time(int print_lunch_start_time) {
        this.print_lunch_start_time = print_lunch_start_time;
    }

    public int getPrint_dinner_start_time() {
        return this.print_dinner_start_time;
    }

    public void setPrint_dinner_start_time(int print_dinner_start_time) {
        this.print_dinner_start_time = print_dinner_start_time;
    }

    public int getPrint_night_start_time() {
        return this.print_night_start_time;
    }

    public void setPrint_night_start_time(int print_night_start_time) {
        this.print_night_start_time = print_night_start_time;
    }

    public String getPrint_empty_value() {
        return this.print_empty_value;
    }

    public void setPrint_empty_value(String print_empty_value) {
        this.print_empty_value = print_empty_value;
    }

    public String getColor_scheme() {
        return this.color_scheme;
    }

    public void setColor_scheme(String color_scheme) {
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
