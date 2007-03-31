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
    private String class_name;

    /** persistent field */
    private String import_manager;

    /** full constructor */
    public MeterImplementationH(String class_name, String import_manager) {
        this.class_name = class_name;
        this.import_manager = import_manager;
    }

    /** default constructor */
    public MeterImplementationH() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClass_name() {
        return this.class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getImport_manager() {
        return this.import_manager;
    }

    public void setImport_manager(String import_manager) {
        this.import_manager = import_manager;
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
