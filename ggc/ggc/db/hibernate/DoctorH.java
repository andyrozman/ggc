package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class DoctorH implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private long doctor_type_id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String address;

    /** nullable persistent field */
    private String phone_gsm;

    /** nullable persistent field */
    private String phone;

    /** nullable persistent field */
    private String working_time;

    /** nullable persistent field */
    private String extended;

    /** nullable persistent field */
    private String comment;

    /** full constructor */
    public DoctorH(long doctor_type_id, String name, String address, String phone_gsm, String phone, String working_time, String extended, String comment) {
        this.doctor_type_id = doctor_type_id;
        this.name = name;
        this.address = address;
        this.phone_gsm = phone_gsm;
        this.phone = phone;
        this.working_time = working_time;
        this.extended = extended;
        this.comment = comment;
    }

    /** default constructor */
    public DoctorH() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDoctor_type_id() {
        return this.doctor_type_id;
    }

    public void setDoctor_type_id(long doctor_type_id) {
        this.doctor_type_id = doctor_type_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_gsm() {
        return this.phone_gsm;
    }

    public void setPhone_gsm(String phone_gsm) {
        this.phone_gsm = phone_gsm;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWorking_time() {
        return this.working_time;
    }

    public void setWorking_time(String working_time) {
        this.working_time = working_time;
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
        if ( !(other instanceof DoctorH) ) return false;
        DoctorH castOther = (DoctorH) other;
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
