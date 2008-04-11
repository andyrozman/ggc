package ggc.core.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class StocksH implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private long stock_subtype_id;

    /** nullable persistent field */
    private long dt_stock;

    /** nullable persistent field */
    private float amount;

    /** nullable persistent field */
    private String desc;

    /** nullable persistent field */
    private float usage_per_day_min;

    /** nullable persistent field */
    private float usage_per_day_max;

    /** nullable persistent field */
    private String extended;

    /** persistent field */
    private int person_id;

    /** nullable persistent field */
    private String comment;

    /** full constructor */
    public StocksH(long stock_subtype_id, long dt_stock, float amount, String desc, float usage_per_day_min, float usage_per_day_max, String extended, int person_id, String comment) {
        this.stock_subtype_id = stock_subtype_id;
        this.dt_stock = dt_stock;
        this.amount = amount;
        this.desc = desc;
        this.usage_per_day_min = usage_per_day_min;
        this.usage_per_day_max = usage_per_day_max;
        this.extended = extended;
        this.person_id = person_id;
        this.comment = comment;
    }

    /** default constructor */
    public StocksH() {
    }

    /** minimal constructor */
    public StocksH(int person_id) {
        this.person_id = person_id;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStock_subtype_id() {
        return this.stock_subtype_id;
    }

    public void setStock_subtype_id(long stock_subtype_id) {
        this.stock_subtype_id = stock_subtype_id;
    }

    public long getDt_stock() {
        return this.dt_stock;
    }

    public void setDt_stock(long dt_stock) {
        this.dt_stock = dt_stock;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public float getUsage_per_day_min() {
        return this.usage_per_day_min;
    }

    public void setUsage_per_day_min(float usage_per_day_min) {
        this.usage_per_day_min = usage_per_day_min;
    }

    public float getUsage_per_day_max() {
        return this.usage_per_day_max;
    }

    public void setUsage_per_day_max(float usage_per_day_max) {
        this.usage_per_day_max = usage_per_day_max;
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

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof StocksH) ) return false;
        StocksH castOther = (StocksH) other;
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