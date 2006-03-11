/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: FoodHomeWeightH
 *  Purpose:  This is Hibernate generated class file, that is registered with Hibernate
 *      and we work with (indirectly through classes with similar names that are part
 *      of datalayer.
 *      It represents table with food home weights.
 *
 *  Author:   andyrozman
 *  Author:   Hibernate CodeGenerator
 */


package ggc.db.hibernate;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class FoodHomeWeightH implements Serializable {

    /** identifier field */
    private long id;

    /** nullable persistent field */
    private long food_number;

    /** persistent field */
    private int sequence;

    /** nullable persistent field */
    private float amount;

    /** nullable persistent field */
    private String msr_desc;

    /** nullable persistent field */
    private float weight_g;

    /** full constructor */
    public FoodHomeWeightH(long food_number, int sequence, float amount, String msr_desc, float weight_g) {
        this.food_number = food_number;
        this.sequence = sequence;
        this.amount = amount;
        this.msr_desc = msr_desc;
        this.weight_g = weight_g;
    }

    /** default constructor */
    public FoodHomeWeightH() {
    }

    /** minimal constructor */
    public FoodHomeWeightH(int sequence) {
        this.sequence = sequence;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFood_number() {
        return this.food_number;
    }

    public void setFood_number(long food_number) {
        this.food_number = food_number;
    }

    public int getSequence() {
        return this.sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getMsr_desc() {
        return this.msr_desc;
    }

    public void setMsr_desc(String msr_desc) {
        this.msr_desc = msr_desc;
    }

    public float getWeight_g() {
        return this.weight_g;
    }

    public void setWeight_g(float weight_g) {
        this.weight_g = weight_g;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof FoodHomeWeightH) ) return false;
        FoodHomeWeightH castOther = (FoodHomeWeightH) other;
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
