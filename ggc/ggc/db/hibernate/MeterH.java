package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MeterH implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private long company_id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String picture_name;

    /** nullable persistent field */
    private String extended;

    /** nullable persistent field */
    private long implementation_id;

    /** full constructor */
    public MeterH(long company_id, String name, String picture_name, String extended, long implementation_id) {
        this.company_id = company_id;
        this.name = name;
        this.picture_name = picture_name;
        this.extended = extended;
        this.implementation_id = implementation_id;
    }

    /** default constructor */
    public MeterH() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCompany_id() {
        return this.company_id;
    }

    public void setCompany_id(long company_id) {
        this.company_id = company_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture_name() {
        return this.picture_name;
    }

    public void setPicture_name(String picture_name) {
        this.picture_name = picture_name;
    }

    public String getExtended() {
        return this.extended;
    }

    public void setExtended(String extended) {
        this.extended = extended;
    }

    public long getImplementation_id() {
        return this.implementation_id;
    }

    public void setImplementation_id(long implementation_id) {
        this.implementation_id = implementation_id;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MeterH) ) return false;
        MeterH castOther = (MeterH) other;
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
