package ggc.core.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class NutritionDefinitionH implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -587797287729046076L;

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private String weight_unit;

    /** nullable persistent field */
    private String tag;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String decimal_places;

    /** nullable persistent field */
    private int static_entry;

    /** full constructor 
     * @param weight_unit 
     * @param tag 
     * @param name 
     * @param decimal_places 
     * @param static_entry */
    public NutritionDefinitionH(String weight_unit, String tag, String name, String decimal_places, int static_entry) {
        this.weight_unit = weight_unit;
        this.tag = tag;
        this.name = name;
        this.decimal_places = decimal_places;
        this.static_entry = static_entry;
    }

    /** default constructor */
    public NutritionDefinitionH() {
    }

    /**
     * Get Id
     * 
     * @return
     */
    public long getId()
    {
        return this.id;
    }

    /**
     * Set Id
     * 
     * @param id
     */
    public void setId(long id)
    {
        this.id = id;
    }

    public String getWeight_unit() {
        return this.weight_unit;
    }

    public void setWeight_unit(String weight_unit) {
        this.weight_unit = weight_unit;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    /**
     * Get Name
     * 
     * @return name
     */
    public String getName() 
    {
        return this.name;
    }

    /**
     * Set Name
     * 
     * @param name as string
     */
    public void setName(String name) 
    {
        this.name = name;
    }


    public String getDecimal_places() {
        return this.decimal_places;
    }

    public void setDecimal_places(String decimal_places) {
        this.decimal_places = decimal_places;
    }

    public int getStatic_entry() {
        return this.static_entry;
    }

    public void setStatic_entry(int static_entry) {
        this.static_entry = static_entry;
    }


    public boolean equals(Object other) {
        if ( !(other instanceof NutritionDefinitionH) ) return false;
        NutritionDefinitionH castOther = (NutritionDefinitionH) other;
        return new EqualsBuilder()
            .append(this.getId(), castOther.getId())
            .isEquals();
    }

    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() 
    {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }
    
    
    /**
     * Create Hash Code
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() 
    {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

}
