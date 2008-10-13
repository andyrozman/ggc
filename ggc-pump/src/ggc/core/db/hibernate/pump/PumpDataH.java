package ggc.core.db.hibernate.pump;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/*  @author Hibernate CodeGenerator */
public class PumpDataH implements Serializable
{

    private static final long serialVersionUID = -3935397524396978150L;

    /** identifier field */
    private long id;

    /** persistent field */
    private long dt_info;

    /** nullable persistent field */
    private int base_type;

    /** nullable persistent field */
    private int sub_type;

    /** nullable persistent field */
    private String value;

    /** nullable persistent field */
    private String extended;

    /** persistent field */
    private int person_id;

    /** nullable persistent field */
    private String comment;

    /** nullable persistent field */
    private long changed;

    /** full constructor */
    public PumpDataH(long dt_info, int base_type, int sub_type, String value, String extended, int person_id,
            String comment, long changed)
    {
        this.dt_info = dt_info;
        this.base_type = base_type;
        this.sub_type = sub_type;
        this.value = value;
        this.extended = extended;
        this.person_id = person_id;
        this.comment = comment;
        this.changed = changed;
    }

    /** default constructor */
    public PumpDataH()
    {
    }

    /** minimal constructor */
    public PumpDataH(long dt_info, int person_id)
    {
        this.dt_info = dt_info;
        this.person_id = person_id;
    }

    public long getId()
    {
        return this.id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getDt_info()
    {
        return this.dt_info;
    }

    public void setDt_info(long dt_info)
    {
        this.dt_info = dt_info;
    }

    public int getBase_type()
    {
        return this.base_type;
    }

    public void setBase_type(int base_type)
    {
        this.base_type = base_type;
    }

    public int getSub_type()
    {
        return this.sub_type;
    }

    public void setSub_type(int sub_type)
    {
        this.sub_type = sub_type;
    }

    public String getValue()
    {
        return this.value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getExtended()
    {
        return this.extended;
    }

    public void setExtended(String extended)
    {
        this.extended = extended;
    }

    public int getPerson_id()
    {
        return this.person_id;
    }

    public void setPerson_id(int person_id)
    {
        this.person_id = person_id;
    }

    public String getComment()
    {
        return this.comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public long getChanged()
    {
        return this.changed;
    }

    public void setChanged(long changed)
    {
        this.changed = changed;
    }

    public String toString()
    {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }

    public boolean equals(Object other)
    {
        if (!(other instanceof PumpDataH))
            return false;
        PumpDataH castOther = (PumpDataH) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    public int hashCode()
    {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

}
