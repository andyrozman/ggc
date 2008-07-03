package ggc.core.db.hibernate.meter;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class GlucoValueH implements Serializable {

    /** identifier field */
    private long id;

    /** persistent field */
    private long dt_info;

    /** nullable persistent field */
    private int bg;

    /** persistent field */
    private int person_id;

    /** nullable persistent field */
    private long changed;

    /** full constructor */
    public GlucoValueH(long dt_info, int bg, int person_id, long changed) {
        this.dt_info = dt_info;
        this.bg = bg;
        this.person_id = person_id;
        this.changed = changed;
    }

    /** default constructor */
    public GlucoValueH() {
    }

    /** minimal constructor */
    public GlucoValueH(long dt_info, int person_id) {
        this.dt_info = dt_info;
        this.person_id = person_id;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDt_info() {
        return this.dt_info;
    }

    public void setDt_info(long dt_info) {
        this.dt_info = dt_info;
    }

    public int getBg() {
        return this.bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public int getPerson_id() {
        return this.person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public long getChanged() {
        return this.changed;
    }

    public void setChanged(long changed) {
        this.changed = changed;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof GlucoValueH) ) return false;
        GlucoValueH castOther = (GlucoValueH) other;
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
