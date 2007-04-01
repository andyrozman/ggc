package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MeterImplementationH implements Serializable {

    /** identifier field */
    private long id;

    /** persistent field */
    private String description;

    /** persistent field */
    private String class_name;

    /** nullable persistent field */
    private long interface_id;

    /** nullable persistent field */
    private int status;

    /** full constructor */
    public MeterImplementationH(String description, String class_name, long interface_id, int status) {
        this.description = description;
        this.class_name = class_name;
        this.interface_id = interface_id;
        this.status = status;
    }

    /** default constructor */
    public MeterImplementationH() {
    }

    /** minimal constructor */
    public MeterImplementationH(String description, String class_name) {
        this.description = description;
        this.class_name = class_name;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClass_name() {
        return this.class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public long getInterface_id() {
        return this.interface_id;
    }

    public void setInterface_id(long interface_id) {
        this.interface_id = interface_id;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MeterImplementationH) ) return false;
        MeterImplementationH castOther = (MeterImplementationH) other;
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