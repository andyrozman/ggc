package ggc.core.db.hibernate;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


public class StocktakingH implements Serializable
{
    private static final long serialVersionUID = 7711915066573628279L;

    private long id;
    private long datetime;
    private String description;
    private String extended;
    private int personId;
    private String comment;
    private long changed;



    public StocktakingH()
    {
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




    public int getPersonId()
    {
        return personId;
    }

    public void setPersonId(int personId)
    {
        this.personId = personId;
    }

    public long getDatetime()
    {
        return datetime;
    }

    public void setDatetime(long datetime)
    {
        this.datetime = datetime;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public long getChanged()
    {
        return changed;
    }

    public void setChanged(long changed)
    {
        this.changed = changed;
    }


    /**
     * Get Extended
     *
     * @return extended value
     */
    public String getExtended()
    {
        return this.extended;
    }

    /**
     * Set Extended
     *
     * @param extended parameter
     */
    public void setExtended(String extended)
    {
        this.extended = extended;
    }



    /**
     * Get Comment
     *
     * @return
     */
    public String getComment()
    {
        return this.comment;
    }

    /**
     * Set Comment
     *
     * @param comment
     */
    public void setComment(String comment)
    {
        this.comment = comment;
    }



    /**
     * Custom equals implementation
     *
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof StocktakingH))
            return false;
        StocktakingH castOther = (StocktakingH) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

    /**
     * To String
     *
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }

    /**
     * Create Hash Code
     *
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }



}
