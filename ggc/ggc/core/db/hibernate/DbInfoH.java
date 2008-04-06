package ggc.core.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class DbInfoH implements Serializable {

    /** identifier field */
    private long id;

    /** persistent field */
    private String key;

    /** nullable persistent field */
    private String value;

    /** nullable persistent field */
    private int type;

    /** nullable persistent field */
    private String description;

    /** full constructor */
    public DbInfoH(String key, String value, int type, String description) {
        this.key = key;
        this.value = value;
        this.type = type;
        this.description = description;
    }

    /** default constructor */
    public DbInfoH() {
    }

    /** minimal constructor */
    public DbInfoH(String key) {
        this.key = key;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
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
        if ( !(other instanceof DbInfoH) ) return false;
        DbInfoH castOther = (DbInfoH) other;
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
