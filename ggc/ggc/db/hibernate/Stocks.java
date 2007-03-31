package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class Stocks implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private long stock_type_sub_id;

    /** nullable persistent field */
    private long dt_stock;

    /** nullable persistent field */
    private float amount;

    /** nullable persistent field */
    private float usage_per_day_min;

    /** nullable persistent field */
    private float usage_per_day_max;

    /** full constructor */
    public Stocks(long stock_type_sub_id, long dt_stock, float amount, float usage_per_day_min, float usage_per_day_max) {
        this.stock_type_sub_id = stock_type_sub_id;
        this.dt_stock = dt_stock;
        this.amount = amount;
        this.usage_per_day_min = usage_per_day_min;
        this.usage_per_day_max = usage_per_day_max;
    }

    /** default constructor */
    public Stocks() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStock_type_sub_id() {
        return this.stock_type_sub_id;
    }

    public void setStock_type_sub_id(long stock_type_sub_id) {
        this.stock_type_sub_id = stock_type_sub_id;
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

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof Stocks) ) return false;
        Stocks castOther = (Stocks) other;
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
