package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class StockSubTypeH implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private long stock_type_id;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String description;

    /** nullable persistent field */
    private float content;

    /** nullable persistent field */
    private String comment;

    /** full constructor */
    public StockSubTypeH(long stock_type_id, String name, String description, float content, String comment) {
        this.stock_type_id = stock_type_id;
        this.name = name;
        this.description = description;
        this.content = content;
        this.comment = comment;
    }

    /** default constructor */
    public StockSubTypeH() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getStock_type_id() {
        return this.stock_type_id;
    }

    public void setStock_type_id(long stock_type_id) {
        this.stock_type_id = stock_type_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getContent() {
        return this.content;
    }

    public void setContent(float content) {
        this.content = content;
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
        if ( !(other instanceof StockSubTypeH) ) return false;
        StockSubTypeH castOther = (StockSubTypeH) other;
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
