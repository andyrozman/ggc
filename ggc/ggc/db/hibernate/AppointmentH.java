package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class AppointmentH implements Serializable {

    /** identifier field */
    private long id;

    /** persistent field */
    private long dt_apoint;

    /** nullable persistent field */
    private String apoint_text;

    /** nullable persistent field */
    private String extended;

    /** nullable persistent field */
    private String comment;

    /** nullable persistent field */
    private ggc.db.hibernate.DoctorH doctor;

    /** full constructor */
    public AppointmentH(long dt_apoint, String apoint_text, String extended, String comment, ggc.db.hibernate.DoctorH doctor) {
        this.dt_apoint = dt_apoint;
        this.apoint_text = apoint_text;
        this.extended = extended;
        this.comment = comment;
        this.doctor = doctor;
    }

    /** default constructor */
    public AppointmentH() {
    }

    /** minimal constructor */
    public AppointmentH(long dt_apoint) {
        this.dt_apoint = dt_apoint;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDt_apoint() {
        return this.dt_apoint;
    }

    public void setDt_apoint(long dt_apoint) {
        this.dt_apoint = dt_apoint;
    }

    public String getApoint_text() {
        return this.apoint_text;
    }

    public void setApoint_text(String apoint_text) {
        this.apoint_text = apoint_text;
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

    public ggc.db.hibernate.DoctorH getDoctor() {
        return this.doctor;
    }

    public void setDoctor(ggc.db.hibernate.DoctorH doctor) {
        this.doctor = doctor;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof AppointmentH) ) return false;
        AppointmentH castOther = (AppointmentH) other;
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
