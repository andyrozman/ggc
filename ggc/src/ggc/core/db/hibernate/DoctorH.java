package ggc.core.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class DoctorH implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -203432582979938654L;

    /** identifier field */
    private long id;

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
    private int visible;

    /** persistent field */
    private int person_id;

    /** nullable persistent field */
    private String comment;

    /** nullable persistent field */
    private ggc.core.db.hibernate.DoctorTypeH doctor_type;

    /** full constructor 
     * @param name 
     * @param address 
     * @param phone_gsm 
     * @param phone 
     * @param working_time 
     * @param extended 
     * @param visible 
     * @param person_id 
     * @param comment 
     * @param doctor_type */
    public DoctorH(String name, String address, String phone_gsm, String phone, String working_time, String extended, int visible, int person_id, String comment, ggc.core.db.hibernate.DoctorTypeH doctor_type) {
        this.name = name;
        this.address = address;
        this.phone_gsm = phone_gsm;
        this.phone = phone;
        this.working_time = working_time;
        this.extended = extended;
        this.visible = visible;
        this.person_id = person_id;
        this.comment = comment;
        this.doctor_type = doctor_type;
    }

    /** default constructor */
    public DoctorH() {
    }

    /** minimal constructor 
     * @param person_id */
    public DoctorH(int person_id) {
        this.person_id = person_id;
    }

    /**
     * Get Id
     * 
     * @return
     */
    public long getId()
    {
        return this.id;
    }

    /**
     * Set Id
     * 
     * @param id
     */
    public void setId(long id)
    {
        this.id = id;
    }

    /**
     * Get Name
     * 
     * @return name
     */
    public String getName() 
    {
        return this.name;
    }

    /**
     * Set Name
     * 
     * @param name as string
     */
    public void setName(String name) 
    {
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

    public int getVisible() {
        return this.visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    public int getPerson_id() {
        return this.person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ggc.core.db.hibernate.DoctorTypeH getDoctor_type() {
        return this.doctor_type;
    }

    public void setDoctor_type(ggc.core.db.hibernate.DoctorTypeH doctor_type) {
        this.doctor_type = doctor_type;
    }


    public boolean equals(Object other) {
        if ( !(other instanceof DoctorH) ) return false;
        DoctorH castOther = (DoctorH) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() 
    {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }
    
    
    /**
     * Create Hash Code
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() 
    {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

}
