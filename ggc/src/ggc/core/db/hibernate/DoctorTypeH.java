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

    /** full constructor */
    public DoctorTypeH(String name, int predefined) {
        this.name = name;
        this.predefined = predefined;
    }

    /** default constructor */
    public DoctorTypeH() {
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

    public int getPredefined() {
        return this.predefined;
    }

    public void setPredefined(int predefined) {
        this.predefined = predefined;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof DoctorTypeH) ) return false;
        DoctorTypeH castOther = (DoctorTypeH) other;
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
