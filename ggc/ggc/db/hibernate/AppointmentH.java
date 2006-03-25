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
    private long doctor_id;

    /** persistent field */
    private long dt_apoint;

    /** nullable persistent field */
    private String comment;

    /** full constructor */
    public AppointmentH(long doctor_id, long dt_apoint, String comment) {
        this.doctor_id = doctor_id;
        this.dt_apoint = dt_apoint;
        this.comment = comment;
    }

    /** default constructor */
    public AppointmentH() {
    }

    /** minimal constructor */
    public AppointmentH(long doctor_id, long dt_apoint) {
        this.doctor_id = doctor_id;
        this.dt_apoint = dt_apoint;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDoctor_id() {
        return this.doctor_id;
    }

    public void setDoctor_id(long doctor_id) {
        this.doctor_id = doctor_id;
    }

    public long getDt_apoint() {
        return this.dt_apoint;
    }

    public void setDt_apoint(long dt_apoint) {
        this.dt_apoint = dt_apoint;
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
