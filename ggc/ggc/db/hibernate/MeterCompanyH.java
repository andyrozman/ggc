package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class MeterCompanyH implements Serializable {

    /** identifier field */
    private long id;

    /** persistent field */
    private String name;

    /** nullable persistent field */
    private String aka_names;

    /** nullable persistent field */
    private String www;

    /** nullable persistent field */
    private String description;

    /** full constructor */
    public MeterCompanyH(String name, String aka_names, String www, String description) {
        this.name = name;
        this.aka_names = aka_names;
        this.www = www;
        this.description = description;
    }

    /** default constructor */
    public MeterCompanyH() {
    }

    /** minimal constructor */
    public MeterCompanyH(String name) {
        this.name = name;
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

    public String getAka_names() {
        return this.aka_names;
    }

    public void setAka_names(String aka_names) {
        this.aka_names = aka_names;
    }

    public String getWww() {
        return this.www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof MeterCompanyH) ) return false;
        MeterCompanyH castOther = (MeterCompanyH) other;
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
