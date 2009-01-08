package ggc.core.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class DoctorH implements Serializable 
{

    private static final long serialVersionUID = -203432582979938654L;
    private long id;
    private String name;
    private String address;
    private String phone_gsm;
    private String phone;
    private String working_time;
    private String extended;
    private int visible;
    private int person_id;
    private String comment;
    private ggc.core.db.hibernate.DoctorTypeH doctor_type;

    /** 
     * full constructor
     *  
     * @param name 
     * @param address 
     * @param phone_gsm 
     * @param phone 
     * @param working_time 
     * @param extended 
     * @param visible 
     * @param person_id 
     * @param comment 
     * @param doctor_type 
     */
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

    /** 
     * minimal constructor
     *  
     * @param person_id 
     */
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


    /**
     * Get Address
     * 
     * @return
     */
    public String getAddress() 
    {
        return this.address;
    }

    /**
     * Set Address
     * 
     * @param address 
     */
    public void setAddress(String address) 
    {
        this.address = address;
    }

    /**
     * Get Phone GSM
     * 
     * @return
     */
    public String getPhone_gsm() 
    {
        return this.phone_gsm;
    }

    /**
     * Set Phone GSM
     * 
     * @param phone_gsm 
     */
    public void setPhone_gsm(String phone_gsm) 
    {
        this.phone_gsm = phone_gsm;
    }

    /**
     * Get Phone
     * 
     * @return
     */
    public String getPhone() 
    {
        return this.phone;
    }

    /**
     * Set Phone
     * 
     * @param phone
     */
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    /**
     * Get Working Time
     * 
     * @return
     */
    public String getWorking_time() 
    {
        return this.working_time;
    }

    /**
     * Set Working Time
     * 
     * @param working_time 
     */
    public void setWorking_time(String working_time) 
    {
        this.working_time = working_time;
    }

    /**
     * Get Extended 
     * 
     * @return extended value
     */
    public String getExtended()
    {
        return this.extended;
    }

    /**
     * Set Extended
     *  
     * @param extended parameter
     */
    public void setExtended(String extended)
    {
        this.extended = extended;
    }

    /**
     * Get Visible
     * 
     * @return
     */
    public int getVisible() 
    {
        return this.visible;
    }

    /**
     * Set Visible
     * 
     * @param visible 
     */
    public void setVisible(int visible) 
    {
        this.visible = visible;
    }

    /**
     * Get Person Id
     * 
     * @return person id parameter
     */
    public int getPerson_id()
    {
        return this.person_id;
    }

    /**
     * Set Person Id
     * 
     * @param person_id parameter
     */
    public void setPerson_id(int person_id)
    {
        this.person_id = person_id;
    }

    /**
     * Get Comment
     * 
     * @return
     */
    public String getComment()
    {
        return this.comment;
    }

    /**
     * Set Comment
     * 
     * @param comment
     */
    public void setComment(String comment)
    {
        this.comment = comment;
    }

    /**
     * Get Doctor Type
     * 
     * @return
     */
    public ggc.core.db.hibernate.DoctorTypeH getDoctor_type() 
    {
        return this.doctor_type;
    }

    /**
     * Set Doctor Type
     * 
     * @param doctor_type 
     */
    public void setDoctor_type(ggc.core.db.hibernate.DoctorTypeH doctor_type) 
    {
        this.doctor_type = doctor_type;
    }

    /** 
     * Custom equals implementation
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) 
    {
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
