package ggc.core.db.hibernate.pump;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.atech.db.hibernate.HibernateObject;

public class PumpProfileH extends HibernateObject
{

    private static final long serialVersionUID = 8212384538572472772L;

    private long id;
    private String name;
    private float basalBase;
    private String basalDiffs;
    private long activeFrom;
    private long activeTill;
    private String extended;
    private int personId;
    private String comment;
    private long changed;


    /** full constructor 
     * @param name 
     * @param basalBase
     * @param basalDiffs
     * @param activeFrom
     * @param activeTill
     * @param extended 
     * @param personId
     * @param comment 
     * @param changed */
    public PumpProfileH(String name, float basalBase, String basalDiffs, long activeFrom, long activeTill,
            String extended, int personId, String comment, long changed)
    {
        this.name = name;
        this.basalBase = basalBase;
        this.basalDiffs = basalDiffs;
        this.activeFrom = activeFrom;
        this.activeTill = activeTill;
        this.extended = extended;
        this.personId = personId;
        this.comment = comment;
        this.changed = changed;
    }


    /** default constructor */
    public PumpProfileH()
    {
    }


    /** minimal constructor 
     * @param personId */
    public PumpProfileH(int personId)
    {
        this.personId = personId;
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


    /**
     * Get Basal Rate - Base
     * 
     * @return basalBase value
     */
    public float getBasalBase()
    {
        return this.basalBase;
    }


    /**
     * Set Basal Rate - Base
     * 
     * @param basalBase value
     */
    public void setBasalBase(float basalBase)
    {
        this.basalBase = basalBase;
    }


    /**
     * Get Basal Rate - Diffs (example: 1100-1230=1.26;1330-1430=1.35)
     * 
     * @return basalDiffs value
     */
    public String getBasalDiffs()
    {
        return this.basalDiffs;
    }


    /**
     * Set Basal Rate - Diffs (example: 1100-1230=1.26;1330-1430=1.35)
     * 
     * @param basalDiffs value
     */
    public void setBasalDiffs(String basalDiffs)
    {
        this.basalDiffs = basalDiffs;
    }


    /**
     * Get Active From
     * 
     * @return activeFrom value
     */
    public long getActiveFrom()
    {
        return this.activeFrom;
    }


    /**
     * Set Active From
     * 
     * @param activeFrom value
     */
    public void setActiveFrom(long activeFrom)
    {
        this.activeFrom = activeFrom;
    }


    /**
     * Get Active Till
     * 
     * @return activeTill value
     */
    public long getActiveTill()
    {
        return this.activeTill;
    }


    /**
     * Set Active Till
     * 
     * @param activeTill value
     */
    public void setActiveTill(long activeTill)
    {
        this.activeTill = activeTill;
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
     * Get Person Id
     * 
     * @return person id parameter
     */
    public int getPersonId()
    {
        return this.personId;
    }


    /**
     * Set Person Id
     * 
     * @param personId parameter
     */
    public void setPersonId(int personId)
    {
        this.personId = personId;
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
     * Get Changed
     * 
     * @return changed value
     */
    public long getChanged()
    {
        return this.changed;
    }


    /**
     * Set Changed
     * 
     * @param changed parameter
     */
    public void setChanged(long changed)
    {
        this.changed = changed;
    }


    /**
     * Equals - method to check equalicy of object
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other)
    {
        if (!(other instanceof PumpProfileH))
            return false;
        PumpProfileH castOther = (PumpProfileH) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }


    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("id", getId()).toString();
    }


    /**
     * Create Hash Code
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }

}
