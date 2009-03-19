package ggc.core.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class AppointmentH implements Serializable {

    private static final long serialVersionUID = 5137365456725058472L;

    /** identifier field */
    private long id;

    /** persistent field */
    private long dt_apoint;

    /** nullable persistent field */
    private String apoint_text;

    /** nullable persistent field */
    private String extended;

    /** persistent field */
    private int person_id;

    /** nullable persistent field */
    private String comment;

    /** nullable persistent field */
    private ggc.core.db.hibernate.DoctorH doctor;

    /** full constructor 
     * @param dt_apoint 
     * @param apoint_text 
     * @param extended 
     * @param person_id 
     * @param comment 
     * @param doctor */
    public AppointmentH(long dt_apoint, String apoint_text, String extended, int person_id, String comment, ggc.core.db.hibernate.DoctorH doctor) {
        this.dt_apoint = dt_apoint;
        this.apoint_text = apoint_text;
        this.extended = extended;
        this.person_id = person_id;
        this.comment = comment;
        this.doctor = doctor;
    }

    /** default constructor */
    public AppointmentH() {
    }

    /** minimal constructor 
     * @param dt_apoint 
     * @param person_id */
    public AppointmentH(long dt_apoint, int person_id) {
        this.dt_apoint = dt_apoint;
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
     * Get Date/Time Info (this is long packed as AtechDateTime yyyymmddhhss)
     * @return
     */
    public long getDt_apoint()
    {
        return this.dt_apoint;
    }

    /**
     * Set Date/Time Info (this is long packed as AtechDateTime yyyymmddhhss)
     * 
     * @param dt_info 
     */
    public void setDt_apoint(long dt_info)
    {
        this.dt_apoint = dt_info;
    }


    /**
     * Get Appointment Text
     * 
     * @return apoint_text values
     */
    public String getApoint_text() 
    {
        return this.apoint_text;
    }

    /**
     * Set Appointment Text
     * 
     * @param apoint_text values
     */
    public void setApoint_text(String apoint_text) 
    {
        this.apoint_text = apoint_text;
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
     * Get Doctor
     * 
     * @return doctor value (DoctorH instance)
     */
    public ggc.core.db.hibernate.DoctorH getDoctor() 
    {
        return this.doctor;
    }

    /**
     * Set Doctor
     * 
     * @param doctor value (DoctorH instance)
     */
    public void setDoctor(ggc.core.db.hibernate.DoctorH doctor) 
    {
        this.doctor = doctor;
    }

    /**
     * Equals - method to check equalicy of object
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) 
    {
        if ( !(other instanceof AppointmentH) ) return false;
        AppointmentH castOther = (AppointmentH) other;
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
