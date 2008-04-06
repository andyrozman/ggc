package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class InetGroupH implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String parent;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String name_i18n;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private String comment;

    /** full constructor */
    public InetGroupH(String parent, String name, String name_i18n, String description, String comment) {
        this.parent = parent;
        this.name = name;
        this.name_i18n = name_i18n;
        this.description = description;
        this.comment = comment;
    }

    /** default constructor */
    public InetGroupH() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getParent() {
        return this.parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_i18n() {
        return this.name_i18n;
    }

    public void setName_i18n(String name_i18n) {
        this.name_i18n = name_i18n;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if ( !(other instanceof InetGroupH) ) return false;
        InetGroupH castOther = (InetGroupH) other;
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
