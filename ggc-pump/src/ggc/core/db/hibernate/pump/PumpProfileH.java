package ggc.core.db.hibernate.pump;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class PumpProfileH implements Serializable {

    private static final long serialVersionUID = 8212384538572472772L;

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private float basal_base;

    /** nullable persistent field */
    private String basal_diffs;

    /** nullable persistent field */
    private long active_from;

    /** nullable persistent field */
    private long active_till;

    /** nullable persistent field */
    private String extended;

    /** persistent field */
    private int person_id;

    /** nullable persistent field */
    private String comment;

    /** nullable persistent field */
    private long changed;

    /** full constructor */
    public PumpProfileH(String name, float basal_base, String basal_diffs, long active_from, long active_till, String extended, int person_id, String comment, long changed) {
        this.name = name;
        this.basal_base = basal_base;
        this.basal_diffs = basal_diffs;
        this.active_from = active_from;
        this.active_till = active_till;
        this.extended = extended;
        this.person_id = person_id;
        this.comment = comment;
        this.changed = changed;
    }

    /** default constructor */
    public PumpProfileH() {
    }

    /** minimal constructor */
    public PumpProfileH(int person_id) {
        this.person_id = person_id;
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

    public float getBasal_base() {
        return this.basal_base;
    }

    public void setBasal_base(float basal_base) {
        this.basal_base = basal_base;
    }

    public String getBasal_diffs() {
        return this.basal_diffs;
    }

    public void setBasal_diffs(String basal_diffs) {
        this.basal_diffs = basal_diffs;
    }

    public long getActive_from() {
        return this.active_from;
    }

    public void setActive_from(long active_from) {
        this.active_from = active_from;
    }

    public long getActive_till() {
        return this.active_till;
    }

    public void setActive_till(long active_till) {
        this.active_till = active_till;
    }

    public String getExtended() {
        return this.extended;
    }

    public void setExtended(String extended) {
        this.extended = extended;
    }

    public int getPerson_id() {
        return this.person_id;
    }

    public void setPerson_id(int person_id) {
        this.person_id = person_id;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        if ( !(other instanceof PumpProfileH) ) return false;
        PumpProfileH castOther = (PumpProfileH) other;
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
