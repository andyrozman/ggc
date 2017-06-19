package ggc.core.db.hibernate.cgms;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.atech.db.hibernate.HibernateObject;

public class CGMSDataExtendedH extends HibernateObject
{

    private static final long serialVersionUID = -8690831306276025195L;

    private long id;
    private long dtInfo;
    private int type;
    private String value;
    private String extended;
    private int personId;
    private String comment;
    private long changed;


    /** full constructor 
     * @param dtInfo
     * @param type 
     * @param value 
     * @param extended 
     * @param personId
     * @param comment 
     * @param changed */
    public CGMSDataExtendedH(long dtInfo, int type, String value, String extended, int personId, String comment,
            long changed)
    {
        this.dtInfo = dtInfo;
        this.type = type;
        this.value = value;
        this.extended = extended;
        this.personId = personId;
        this.comment = comment;
        this.changed = changed;
    }


    /** default constructor */
    public CGMSDataExtendedH()
    {
    }


    /** minimal constructor 
     * @param dtInfo
     * @param personId */
    public CGMSDataExtendedH(long dtInfo, int personId)
    {
        this.dtInfo = dtInfo;
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
     * Get Date/Time Info (this is long packed as AtechDateTime yyyymmddhhss)
     * @return
     */
    public long getDtInfo()
    {
        return this.dtInfo;
    }


    /**
     * Set Date/Time Info (this is long packed as AtechDateTime yyyymmddhhss)
     * 
     * @param dtInfo
     */
    public void setDtInfo(long dtInfo)
    {
        this.dtInfo = dtInfo;
    }


    /**
     * Get Type 
     * 
     * @return type value
     */
    public int getType()
    {
        return this.type;
    }


    /**
     * Set Type
     *  
     * @param type parameter
     */
    public void setType(int type)
    {
        this.type = type;
    }


    /**
     * Get Value 
     * 
     * @return value value
     */
    public String getValue()
    {
        return this.value;
    }


    /**
     * Set Extended
     *  
     * @param value parameter
     */
    public void setValue(String value)
    {
        this.value = value;
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
        if (!(other instanceof CGMSDataExtendedH))
            return false;
        CGMSDataExtendedH castOther = (CGMSDataExtendedH) other;
        return new EqualsBuilder().append(this.getId(), castOther.getId()).isEquals();
    }

}
