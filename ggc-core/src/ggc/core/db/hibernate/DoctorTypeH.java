package ggc.core.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class DoctorTypeH implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 8775376037164641226L;

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private int predefined;

    /** full constructor 
     * @param name 
     * @param predefined */
    public DoctorTypeH(String name, int predefined) {
        this.name = name;
        this.predefined = predefined;
    }

    /** default constructor */
    public DoctorTypeH() {
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
     * Get Predefined
     * 
     * @return predefined value
     */
    public int getPredefined() 
    {
        return this.predefined;
    }

    /**
     * Set Predefined
     * 
     * @param predefined value
     */
    public void setPredefined(int predefined) 
    {
        this.predefined = predefined;
    }

    /** 
     * Custom equals implementation
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) 
    {
        if ( !(other instanceof DoctorTypeH) ) return false;
        DoctorTypeH castOther = (DoctorTypeH) other;
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
